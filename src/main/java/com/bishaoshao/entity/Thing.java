package com.bishaoshao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bishaoshao.utils.JsonTypeHandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@TableName(value = "thing", autoResultMap = true) // 开启 autoResultMap
public class Thing implements Serializable {

    @TableId
    private Integer id;

    @TableField(value = "params", typeHandler = JsonTypeHandler.class)
    private Map<String, Object> params;
}


