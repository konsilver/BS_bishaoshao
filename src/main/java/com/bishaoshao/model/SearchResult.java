package com.bishaoshao.model;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class SearchResult {


    private long id;


    private String name;


    private LocalDate date;


    private BigDecimal price;
    

    private String source;


    private String image;
}
