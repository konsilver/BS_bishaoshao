package com.bishaoshao.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;



@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class SimpleResult {


    private long id;


    private String name;


    private BigDecimal price;
    

    private String image;
    
}
