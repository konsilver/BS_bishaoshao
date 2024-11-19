package com.bishaoshao.controller;

import com.bishaoshao.utils.result.Result;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.bishaoshao.service.SearchService;



@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private SearchService searchService;
    
    @Operation(summary = "搜索关键词")
    @PostMapping("/lookfor")
    public Result<?> search(@RequestBody Map<String, String> params) {
        String keyword = params.get("keyword");  // 从请求体中获取 keyword 参数
        return searchService.search(keyword);
    }
}

