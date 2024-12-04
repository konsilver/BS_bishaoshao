package com.bishaoshao.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.Map;

@Schema(description = "搜索结果结构")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ThingDetail {

    @Schema(description = "商品信息ID")
    private long id;

    @Schema(description = "商品名称")
    private String name;

    @Schema(description = "商品信息日期")
    private LocalDate date;

    @Schema(description = "商品价格")
    private double price;
    
    @Schema(description = "商品所属商城与店铺")
    private String source;

    @Schema(description = "商品图片链接")
    private String image;

    @Schema(description = "商品直达链接")
    private String url;

    @Schema(description = "商品参数信息")
    private Map<String, Object> params;
}