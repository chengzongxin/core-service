package com.czx.utils;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class NetworkRequest {
    
    private final RestTemplate restTemplate;
    
    public NetworkRequest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public ResponseEntity<String> post(String url, Map<String, Object> data, 
                                     String cookie, String mallid, String origin) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Cookie", cookie);
        headers.set("mallid", mallid);
        headers.set("Origin", origin);
        headers.set("Referer", origin);
        
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(data, headers);
        return restTemplate.postForEntity(url, request, String.class);
    }
    
    public ResponseEntity<String> get(String url, Map<String, Object> params,
                                    String cookie, String mallid, String origin) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", cookie);
        headers.set("mallid", mallid);
        headers.set("Origin", origin);
        headers.set("Referer", origin);
        
        HttpEntity<Void> request = new HttpEntity<>(headers);
        
        // 构建查询参数
        StringBuilder urlBuilder = new StringBuilder(url);
        if (params != null && !params.isEmpty()) {
            urlBuilder.append("?");
            params.forEach((key, value) -> 
                urlBuilder.append(key).append("=").append(value).append("&"));
            urlBuilder.setLength(urlBuilder.length() - 1); // 移除最后的&
        }
        
        return restTemplate.exchange(urlBuilder.toString(), HttpMethod.GET, request, String.class);
    }
}
