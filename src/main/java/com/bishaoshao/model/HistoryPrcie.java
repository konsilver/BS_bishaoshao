package com.bishaoshao.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "商品历史价格")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class HistoryPrcie {

    @Schema(description = "商品信息日期")
    private LocalDate date;

    @Schema(description = "商品价格")
    private BigDecimal price;
    
}
