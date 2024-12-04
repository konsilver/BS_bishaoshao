package com.bishaoshao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bishaoshao.entity.Card;
import com.bishaoshao.model.HistoryPrcie;

import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface ListMapper extends BaseMapper<Card> {

    /**
     * 根据多个关键词对商品进行模糊查询，支持权重匹配
     * @param keywords 用户输入的关键词列表
     * @return 返回匹配的商品列表
     */
    @Select("<script>" +
            "SELECT * FROM card " +
            "WHERE " +
            "<foreach item='keyword' collection='keywords' open='(' separator=' AND ' close=')'>" +
            "(title LIKE CONCAT('%', #{keyword}, '%') OR child_type LIKE CONCAT('%', #{keyword}, '%'))" +
            "</foreach>" +
            "ORDER BY " +
            "CASE " +
            "WHEN child_type LIKE CONCAT('%', #{keywords[0]}, '%') THEN 1 " + // 优先匹配子类
            "WHEN title LIKE CONCAT('%', #{keywords[0]}, '%') THEN 2 " + // 再次匹配标题
            "ELSE 3 END" + // 其他情况
            "</script>")
    List<Card> searchByKeywords(@Param("keywords") List<String> keywords);


    // 查询随机商品
    @Select("SELECT * FROM card ORDER BY RAND() LIMIT #{limit}")
    List<Card> findRandomCards(@Param("limit") int limit);

}
