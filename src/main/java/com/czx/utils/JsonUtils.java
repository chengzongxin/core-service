package com.czx.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JsonUtils {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 解析JSON字符串为JsonNode
     */
    public static JsonNode parseJson(String jsonString) throws JsonProcessingException {
        return objectMapper.readTree(jsonString);
    }
    
    /**
     * 解析JSON字符串为Map
     */
    public static Map<String, Object> parseJsonToMap(String jsonString) throws JsonProcessingException {
        return objectMapper.readValue(jsonString, Map.class);
    }
    
    /**
     * 将对象转换为JSON字符串
     */
    public static String toJsonString(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
    
    /**
     * 从JsonNode中安全获取字符串值
     */
    public static String getString(JsonNode node, String fieldName) {
        if (node != null && node.has(fieldName)) {
            JsonNode fieldNode = node.get(fieldName);
            return fieldNode.isTextual() ? fieldNode.asText() : fieldNode.toString();
        }
        return null;
    }
    
    /**
     * 从JsonNode中安全获取整数值
     */
    public static Integer getInteger(JsonNode node, String fieldName) {
        if (node != null && node.has(fieldName)) {
            JsonNode fieldNode = node.get(fieldName);
            return fieldNode.isNumber() ? fieldNode.asInt() : null;
        }
        return null;
    }
    
    /**
     * 从JsonNode中安全获取布尔值
     */
    public static Boolean getBoolean(JsonNode node, String fieldName) {
        if (node != null && node.has(fieldName)) {
            JsonNode fieldNode = node.get(fieldName);
            return fieldNode.isBoolean() ? fieldNode.asBoolean() : null;
        }
        return null;
    }
    
    /**
     * 从JsonNode中安全获取子节点
     */
    public static JsonNode getNode(JsonNode node, String fieldName) {
        if (node != null && node.has(fieldName)) {
            return node.get(fieldName);
        }
        return null;
    }
}
