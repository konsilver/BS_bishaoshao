package com.bishaoshao.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;

import com.bishaoshao.mapper.*;
import com.bishaoshao.model.*;
import com.bishaoshao.utils.result.Result;
import com.bishaoshao.entity.*;

import java.awt.*;
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
public class SearchService {

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ThingMapper thingMapper;

    @Autowired
    private ListMapper cardMapper;

    @Autowired
    private HistoryMapper historyMapper;

    public Result<List<SearchResult>> search(String keyword){

        // 如果关键词为空，则返回失败
        if (keyword == null || keyword.trim().isEmpty()) {
            return Result.failed("关键词不能为空");
        }

        JiebaSegmenter segmenter = new JiebaSegmenter();
        List<String> segments = segmenter.sentenceProcess(keyword);

        // 将分词后的词语进行匹配查询
        List<Card> cards = cardMapper.searchByKeywords(segments);
        
        // 返回查询结果
        if (cards.isEmpty()) {
            return Result.failed("未找到相关商品");
        }

        return Result.success(convertCardToSearchResult(cards));
    }

    public List<SearchResult> convertCardToSearchResult(List<Card> cards) {
        return cards.stream().map(card -> 
            SearchResult.builder()
                .id(card.getId())
                .name(card.getTitle())  // title 对应 name
                .date(card.getCrawlDate())  // crawlDate 对应 date
                .price(card.getPrice())  // price 转为 double
                .source(card.getSource())
                .image(card.getImgUrl())  // imgUrl 对应 image
                .build()
        ).collect(Collectors.toList());
    }

    public Result<?> getDetailsById(Long id) {
        // 根据 ID 查询 Card 对象
        Card card = listMapper.selectById(id);
        Thing thing = thingMapper.selectById(id);
        
        if (card == null) {
            return Result.failed("找不到对应商品了哦");
        }

        // 构造返回的 ThingDetail 对象
        ThingDetail thingDetail = new ThingDetail();
        thingDetail.setId(id);  // 设置商品 ID
        thingDetail.setName(card.getTitle());  // 使用 Card 中的 title 作为商品名称
        thingDetail.setPrice(card.getPrice().doubleValue());  // 使用 Card 中的 price
        thingDetail.setSource(card.getShop()+" "+card.getSource());  
        thingDetail.setImage(card.getImgUrl());  // 使用 Card 中的 imgUrl
        thingDetail.setUrl(card.getThingUrl());  // 使用 Card 中的 thingUrl
        thingDetail.setDate(card.getCrawlDate());  // 使用 Card 中的 crawlDate 转换为 LocalDate

        if(thing==null) thingDetail.setParams(null);
        else
            thingDetail.setParams(thing.getParams());  // 使用 Thing 中的 params

        return Result.success(thingDetail);
    }

    public Result<?> getHistoryById(Long id){
        List<HistoryPrcie> historyPrcies=historyMapper.lookback(id);
        Card card=listMapper.selectById(id);
        HistoryPrcie now= new HistoryPrcie();
        now.setDate(card.getCrawlDate());
        now.setPrice(card.getPrice());
        historyPrcies.add(now);

        //if(historyPrcies==null) return Result.failed("暂未抓获商品历史价格");
        return Result.success(historyPrcies);
    }
}
