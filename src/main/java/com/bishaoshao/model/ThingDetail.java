package com.bishaoshao.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.Map;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ThingDetail {


    private long id;


    private String name;


    private LocalDate date;


    private double price;
    

    private String source;


    private String image;


    private String url;


    private Map<String, Object> params;
}