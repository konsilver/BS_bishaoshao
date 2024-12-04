package com.bishaoshao.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bishaoshao.entity.Subscribe;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface SubscribeMapper extends BaseMapper<Subscribe>{
 
    @Delete("DELETE FROM subscribe WHERE user_id = #{user_id} AND thing_id = #{thing_id}")
    int deleteByUserIdAndThingId(@Param("user_id") Long user_id, @Param("thing_id") Long thing_id);

    // 根据用户ID获取订阅的商品ID列表
    @Select("SELECT thing_id FROM subscribe WHERE user_id = #{user_id}")
    List<Long> findSubscribedProductIds(@Param("user_id") Long user_id);

    @Select("SELECT* FROM subscribe WHERE user_id = #{user_id} AND thing_id = #{thing_id}")
    Subscribe selectByUserIdAndThingId(@Param("user_id") Long user_id, @Param("thing_id") Long thing_id);

    @Select("SELECT* FROM subscribe")
    List<Subscribe> selectAll();
}
