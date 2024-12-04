package com.bishaoshao.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@RequiredArgsConstructor
@TableName("card")
public class Card implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private BigDecimal price;

    private String shop;

    @TableField("img_url")
    private String imgUrl;

    private String source;

    @TableField("child_type")
    private String childType;

    @TableField("thing_url")
    private String thingUrl;

    @TableField("crawl_date")
    private LocalDate crawlDate;
}
