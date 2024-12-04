package com.bishaoshao.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Schema(description = "首页卡片")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class SimpleResult {

    @Schema(description = "商品信息ID")
    private long id;

    @Schema(description = "商品名称")
    private String name;

    @Schema(description = "商品价格")
    private BigDecimal price;
    
    @Schema(description = "商品图片链接")
    private String image;
    
}
