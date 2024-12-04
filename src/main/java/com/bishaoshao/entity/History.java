package com.bishaoshao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@RequiredArgsConstructor
@TableName("history")
public class History implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id; 

    @TableField("thing_id")
    private Long thingId;

    private BigDecimal price;
    
    @TableField("crawl_date")
    private LocalDate crawlDate;
}
    
