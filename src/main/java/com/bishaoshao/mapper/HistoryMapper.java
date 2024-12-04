package com.bishaoshao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bishaoshao.entity.History;
import com.bishaoshao.model.HistoryPrcie;

import java.util.List;

import org.springframework.data.repository.query.Param;

public interface HistoryMapper extends BaseMapper<History>{
    
    @Select("SELECT crawl_date AS date, price FROM history WHERE thing_id = #{thingId} ORDER BY crawl_date")
    List<HistoryPrcie> lookback(@Param("thingId") Long thingId);


    // 查询某商品最近的历史价格
    @Select("SELECT * FROM history WHERE thing_id = #{thing_id} ORDER BY crawl_date DESC LIMIT 1")
    History findLatestHistoryByThingId(@Param("thing_id") long thing_id);
}
