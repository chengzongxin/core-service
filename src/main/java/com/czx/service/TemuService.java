package com.czx.service;

import com.czx.pojo.Result;
import java.util.List;

public interface TemuService {
    Result getComplianceList(int page, int pageSize);
    Result getComplianceTotal(int page, int pageSize);
    Result getProducts(List<Integer> productIds, String productName, int page, int pageSize);
    Result offlineProducts(List<Integer> productIds, int maxThreads);
}
