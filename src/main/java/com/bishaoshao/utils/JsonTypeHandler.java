package com.bishaoshao.utils;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class JsonTypeHandler extends AbstractJsonTypeHandler<Map<String, Object>> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected Map<String, Object> parse(String json) {
        try {
            //System.out.println("Parsing JSON: " + json); // 调试输出
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            throw new RuntimeException("JSON 解析失败: " + json, e);
        }
    }
    

    @Override
    protected String toJson(Map<String, Object> obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("JSON 转换失败: " + obj, e);
        }
    }
}
