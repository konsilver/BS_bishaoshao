package com.bishaoshao.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;



@Data
@AllArgsConstructor
@RequiredArgsConstructor
@TableName("subscribe")
public class Subscribe implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id; 

    @TableField("thing_id")
    private Long thingId;
    
    @TableField("user_id")
    private Long userId;
}
