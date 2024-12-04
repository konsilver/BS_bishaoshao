package com.bishaoshao.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.bishaoshao.mapper.*;
import com.bishaoshao.model.*;
import com.bishaoshao.utils.result.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bishaoshao.entity.*;

import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.time.LocalDate;


import com.huaban.analysis.jieba.JiebaSegmenter;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class SubscribeService {

    @Autowired
    private InfoMapper infoMapper;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private InfoService infoService;
    @Autowired
    private SubscribeMapper subscribeMapper;
    @Autowired
    private HistoryMapper historyMapper;
    @Autowired
    private MailService mailService;
    
    public Result<?> add(Long user_id,Long thing_id){
        Subscribe subscribe=new Subscribe();
        subscribe.setUserId(user_id);
        subscribe.setThingId(thing_id);
        subscribeMapper.insert(subscribe);
        return Result.success();
    }

    public Result<?> sub(Long user_id,Long thing_id){
        Subscribe subscribe=new Subscribe();
        subscribe.setUserId(user_id);
        subscribe.setThingId(thing_id);
        subscribeMapper.deleteByUserIdAndThingId(user_id,thing_id);
        return Result.success();
    }

    public Result<?> recmall(){
        List<SimpleResult> pushableCards =findPushableCards(12);
        return Result.success(pushableCards);
    }

    public Result<?> recmlike(Long user_id){
        System.out.println("begin");
        List<SimpleResult> pushableCards = new ArrayList<>();
        
        // 获取用户订阅的商品ID
        List<Long> subscribedProductIds = subscribeMapper.findSubscribedProductIds(user_id);

        // 如果用户没有订阅商品，则直接执行原始推送逻辑
        if (subscribedProductIds.isEmpty()) {
            pushableCards=findPushableCards(15);
            return Result.success(pushableCards);
        }

        // 优先寻找符合条件的商品
        int limit = 50; // 初始查询商品数量
        Set<String> checkedChildTypes = new HashSet<>(); // 存储已经检查过的子类别
        List<String> childTypesToCheck = new ArrayList<>(); // 订阅商品的子类别

        // 获取用户订阅商品的子类别
        for (Long productId : subscribedProductIds) {
            Card card = listMapper.selectById(productId);
            if (card != null) {
                childTypesToCheck.add(card.getChildType());
            }
        }

        // 优先查找符合用户订阅商品价格条件的商品
        while (pushableCards.size() < 15) {
            List<Card> randomCards = listMapper.findRandomCards(limit);

            for (Card card : randomCards) {
                // 获取该商品的最新历史价格
                History latestHistory = historyMapper.findLatestHistoryByThingId(card.getId());
                if (latestHistory != null && card.getPrice().compareTo(latestHistory.getPrice().multiply(BigDecimal.valueOf(0.95))) <= 0) {
                    // 当前价格 <= 历史价格的 95%
                    // 优先选择订阅商品或同一子类别的商品
                    if (subscribedProductIds.contains(card.getId())) {
                        addPushableCard(pushableCards, card);
                    } else {
                        for (String childType : childTypesToCheck) {
                            if (card.getChildType().equals(childType) && !checkedChildTypes.contains(childType)) {
                                addPushableCard(pushableCards, card);
                                checkedChildTypes.add(childType);
                                break;
                            }
                        }
                    }
                    if (pushableCards.size() >= 15) {
                        break;
                    }
                }
            }
        }

        // 如果仍不足20件商品，用随机降价商品补充
        if (pushableCards.size() < 15) {
            pushableCards.addAll(findPushableCards(15-pushableCards.size()));
        }

        System.out.println(pushableCards.size());
        return Result.success(pushableCards);

    }

    private void addPushableCard(List<SimpleResult> pushableCards, Card card) {
        SimpleResult simpleResult = new SimpleResult();
        simpleResult.setId(card.getId());
        simpleResult.setName(card.getTitle());
        simpleResult.setPrice(card.getPrice());
        simpleResult.setImage(card.getImgUrl());
        pushableCards.add(simpleResult);
    }
    

    public List<SimpleResult> findPushableCards(int count) {
        List<SimpleResult> pushableCards = new ArrayList<>();
        int limit = 50; // 先从100个商品中随机选取

        while (pushableCards.size() < count) {
            // 获取随机商品
            List<Card> randomCards = listMapper.findRandomCards(limit);

            for (Card card : randomCards) {
                // 获取该商品的最新历史价格
                History latestHistory = historyMapper.findLatestHistoryByThingId(card.getId());

                if (latestHistory != null && card.getPrice().compareTo(latestHistory.getPrice().multiply(BigDecimal.valueOf(0.9))) <= 0) {
                    // 如果当前价格 <= 历史价格的 90%
                    SimpleResult simpleResult = new SimpleResult();
                    simpleResult.setId(card.getId());
                    simpleResult.setName(card.getTitle());
                    simpleResult.setPrice(card.getPrice());
                    simpleResult.setImage(card.getImgUrl());
                    
                    pushableCards.add(simpleResult);

                    // 如果已经找到16个推送商品，则退出
                    if (pushableCards.size() >= count) {
                        break;
                    }
                }
            }
        }

        return pushableCards;
    }

    public Result<?> sure(Long user_id,Long thing_id){
        Subscribe subscribe=subscribeMapper.selectByUserIdAndThingId(user_id, thing_id);
        if(subscribe==null) return Result.success(false);
        else return Result.success(true);
    }

    public Result<?> remind(){
        // 遍历所有订阅
        List<Subscribe> subscriptions = subscribeMapper.selectAll();
        
        for (Subscribe subscribe : subscriptions) {
            Long user_id= subscribe.getUserId();
            Long thing_id= subscribe.getThingId();
            // 查询商品的当前价格（card表）
            Card card = listMapper.selectById(thing_id);
            if (card == null) continue;

            // 查询商品的历史最近价格（history表）
            History latestHistory = historyMapper.findLatestHistoryByThingId(thing_id);
            if (latestHistory == null) continue;

            BigDecimal priceA = card.getPrice();
            BigDecimal priceB = latestHistory.getPrice();


            // 判断是否降价超过5%
            if (priceA.compareTo(priceB.multiply(new BigDecimal("0.95"))) <= 0) {
                // 价格降价，发送提醒邮件
                String msg = "比少少提醒您：您订阅的商品 " + card.getTitle() + " 降价了！当前价格：" + priceA;
                String mail=infoService.getUserById(user_id).getData().getEmail();
                mailService.remind(mail, msg);
            }
        }
        return Result.success();
    }
}
