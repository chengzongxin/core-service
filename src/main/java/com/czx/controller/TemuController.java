package com.czx.controller;

import com.czx.pojo.Result;
import com.czx.pojo.OfflineRequest;
import com.czx.pojo.ProductQueryRequest;
import com.czx.service.TemuService;
import com.czx.utils.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/temu")
public class TemuController {
    
    @Autowired
    private TemuService temuService;
    
    @GetMapping("/compliance/list")
    public Result getComplianceList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            HttpServletRequest request) {
        
        // 检查用户是否已认证
        if (!RequestUtils.isAuthenticated(request)) {
            return Result.error("用户未认证");
        }
        
        return temuService.getComplianceList(page, pageSize);
    }
    
    @GetMapping("/compliance/total")
    public Result getComplianceTotal(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            HttpServletRequest request) {
        
        // 检查用户是否已认证
        if (!RequestUtils.isAuthenticated(request)) {
            return Result.error("用户未认证");
        }
        
        return temuService.getComplianceTotal(page, pageSize);
    }
    
    @PostMapping("/seller/product")
    public Result getProducts(@RequestBody ProductQueryRequest request, HttpServletRequest httpRequest) {
        // 检查用户是否已认证
        if (!RequestUtils.isAuthenticated(httpRequest)) {
            return Result.error("用户未认证");
        }
        
        return temuService.getProducts(
            request.getProductIds(), 
            request.getProductName(), 
            request.getPage(), 
            request.getPageSize()
        );
    }
    
    @PostMapping("/seller/offline")
    public Result offlineProducts(@RequestBody OfflineRequest request, HttpServletRequest httpRequest) {
        // 检查用户是否已认证
        if (!RequestUtils.isAuthenticated(httpRequest)) {
            return Result.error("用户未认证");
        }
        
        return temuService.offlineProducts(request.getProductIds(), request.getMax_threads());
    }
}
