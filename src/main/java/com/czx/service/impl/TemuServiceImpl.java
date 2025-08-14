package com.czx.service.impl;

import com.czx.pojo.Result;
import com.czx.pojo.UserConfig;
import com.czx.service.TemuService;
import com.czx.service.UserConfigService;
import com.czx.utils.NetworkRequest;
import com.czx.utils.JsonUtils;
import com.czx.utils.JwtUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class TemuServiceImpl implements TemuService {
    
    @Autowired
    private UserConfigService userConfigService;
    
    @Autowired
    private NetworkRequest networkRequest;
    
    @Override
    public Result getComplianceList(int page, int pageSize) {
        try {
            // 这里需要从当前用户上下文获取配置
            // 暂时使用固定用户ID，后续需要集成认证
            UserConfig config = userConfigService.getConfigByUserId(1);
            if (config == null) {
                return Result.error("用户配置不存在");
            }
            
            String agentsellerCookie = config.getAgentseller_cookie();
            String mallid = config.getMallid();
            String originUrl = "https://agentseller.temu.com";
            String apiUrl = "https://agentseller.temu.com/mms/tmod_punish/agent/merchant_appeal/entrance/list";
            
            Map<String, Object> payload = new HashMap<>();
            payload.put("page_num", page);
            payload.put("page_size", pageSize);
            payload.put("target_type", "goods");
            
            var response = networkRequest.post(apiUrl, payload, agentsellerCookie, mallid, originUrl);
            if (response == null || response.getStatusCode() != org.springframework.http.HttpStatus.OK) {
                return Result.error("获取数据失败");
            }
            
            // 解析响应数据
            String responseBody = response.getBody();
            if (responseBody == null) {
                return Result.error("响应数据为空");
            }
            
            try {
                JsonNode jsonNode = JsonUtils.parseJson(responseBody);
                Boolean success = JsonUtils.getBoolean(jsonNode, "success");
                
                if (success != null && success) {
                    JsonNode resultNode = JsonUtils.getNode(jsonNode, "result");
                    if (resultNode != null) {
                        JsonNode itemsNode = JsonUtils.getNode(resultNode, "punish_appeal_entrance_list");
                        if (itemsNode != null) {
                            return Result.success(itemsNode);
                        }
                    }
                    return Result.success("获取违规列表成功");
                } else {
                    String msg = JsonUtils.getString(jsonNode, "msg");
                    return Result.error("获取数据失败: " + (msg != null ? msg : "未知错误"));
                }
            } catch (Exception e) {
                return Result.error("解析响应数据失败: " + e.getMessage());
            }
            
        } catch (Exception e) {
            return Result.error("获取违规列表失败: " + e.getMessage());
        }
    }
    
    @Override
    public Result getComplianceTotal(int page, int pageSize) {
        try {
            // 这里需要从当前用户上下文获取配置
            // 暂时使用固定用户ID，后续需要集成认证
            UserConfig config = userConfigService.getConfigByUserId(1);
            if (config == null) {
                return Result.error("用户配置不存在");
            }
            
            String agentsellerCookie = config.getAgentseller_cookie();
            String mallid = config.getMallid();
            String originUrl = "https://agentseller.temu.com";
            String apiUrl = "https://agentseller.temu.com/mms/tmod_punish/agent/merchant_appeal/entrance/list";
            
            Map<String, Object> payload = new HashMap<>();
            payload.put("page_num", page);
            payload.put("page_size", pageSize);
            payload.put("target_type", "goods");
            
            var response = networkRequest.post(apiUrl, payload, agentsellerCookie, mallid, originUrl);
            if (response == null || response.getStatusCode() != org.springframework.http.HttpStatus.OK) {
                return Result.error("获取数据失败");
            }
            
            // 解析响应数据获取总数
            String responseBody = response.getBody();
            if (responseBody == null) {
                return Result.error("响应数据为空");
            }
            
            try {
                JsonNode jsonNode = JsonUtils.parseJson(responseBody);
                Boolean success = JsonUtils.getBoolean(jsonNode, "success");
                
                if (success != null && success) {
                    JsonNode resultNode = JsonUtils.getNode(jsonNode, "result");
                    if (resultNode != null) {
                        Integer total = JsonUtils.getInteger(resultNode, "total");
                        if (total != null) {
                            return Result.success(total);
                        }
                    }
                    return Result.success("获取违规总数成功");
                } else {
                    String msg = JsonUtils.getString(jsonNode, "msg");
                    return Result.error("获取数据失败: " + (msg != null ? msg : "未知错误"));
                }
            } catch (Exception e) {
                return Result.error("解析响应数据失败: " + e.getMessage());
            }
            
        } catch (Exception e) {
            return Result.error("获取违规总数失败: " + e.getMessage());
        }
    }
    
    @Override
    public Result getProducts(List<Integer> productIds, String productName, int page, int pageSize) {
        try {
            // 这里需要从当前用户上下文获取配置
            // 暂时使用固定用户ID，后续需要集成认证
            UserConfig config = userConfigService.getConfigByUserId(1);
            if (config == null) {
                return Result.error("用户配置不存在");
            }
            
            String agentsellerCookie = config.getAgentseller_cookie();
            String mallid = config.getMallid();
            String originUrl = "https://agentseller.temu.com";
            String url = "https://agentseller.temu.com/visage-agent-seller/product/skc/pageQuery";
            
            Map<String, Object> payload = new HashMap<>();
            payload.put("page", page);
            payload.put("pageSize", pageSize);
            
            if (productIds != null) {
                payload.put("productIds", productIds);
            }
            if (productName != null) {
                payload.put("productName", productName);
            }
            
            var response = networkRequest.post(url, payload, agentsellerCookie, mallid, originUrl);
            if (response == null || response.getStatusCode() != org.springframework.http.HttpStatus.OK) {
                return Result.error("查询失败");
            }
            
            // 解析响应数据
            String responseBody = response.getBody();
            if (responseBody == null) {
                return Result.error("响应数据为空");
            }
            
            try {
                JsonNode jsonNode = JsonUtils.parseJson(responseBody);
                Boolean success = JsonUtils.getBoolean(jsonNode, "success");
                
                if (success != null && success) {
                    JsonNode resultNode = JsonUtils.getNode(jsonNode, "result");
                    if (resultNode != null) {
                        JsonNode itemsNode = JsonUtils.getNode(resultNode, "pageItems");
                        if (itemsNode != null) {
                            return Result.success(itemsNode);
                        }
                    }
                    return Result.success("获取商品列表成功");
                } else {
                    String msg = JsonUtils.getString(jsonNode, "msg");
                    return Result.error("查询失败: " + (msg != null ? msg : "未知错误"));
                }
            } catch (Exception e) {
                return Result.error("解析响应数据失败: " + e.getMessage());
            }
            
        } catch (Exception e) {
            return Result.error("获取商品列表失败: " + e.getMessage());
        }
    }
    
    @Override
    public Result offlineProducts(List<Integer> productIds, int maxThreads) {
        try {
            // 这里需要从当前用户上下文获取配置
            // 暂时使用固定用户ID，后续需要集成认证
            UserConfig config = userConfigService.getConfigByUserId(1);
            if (config == null) {
                return Result.error("用户配置不存在");
            }
            
            // 调用批量下架方法
            return batchOfflineProducts(config, productIds, maxThreads);
            
        } catch (Exception e) {
            return Result.error("批量下架失败: " + e.getMessage());
        }
    }
    
    /**
     * 从HttpServletRequest中获取用户ID
     */
    private Integer getUserIdFromRequest(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (token == null || token.isEmpty()) {
            return null;
        }
        return JwtUtils.getUserIdFromToken(token);
    }
    
    /**
     * 批量下架商品的核心逻辑
     * 参考Python版本的实现
     */
    private Result batchOfflineProducts(UserConfig config, List<Integer> productIds, int maxThreads) {
        try {
            String kuajingmaihuoCookie = config.getKuajingmaihuo_cookie();
            String mallid = config.getMallid();
            String parentMsgId = config.getParent_msg_id();
            String toolId = config.getTool_id();
            String parentMsgTimestamp = config.getParent_msg_timestamp();
            String originUrl = "https://seller.kuajingmaihuo.com";
            
            // 检查缓存是否有效（24小时）
            boolean cacheValid = false;
            if (parentMsgId != null && toolId != null && parentMsgTimestamp != null) {
                try {
                    long cacheTime = Long.parseLong(parentMsgTimestamp);
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - cacheTime < 24 * 60 * 60 * 1000) { // 24小时
                        cacheValid = true;
                    }
                } catch (NumberFormatException e) {
                    // 时间戳解析失败，缓存无效
                }
            }
            
            if (!cacheValid) {
                // 缓存无效，重新获取parentMsgId和toolId
                Result initResult = initializeOfflineSession(kuajingmaihuoCookie, mallid, originUrl);
                if (!initResult.getCode().equals(1)) {
                    return initResult;
                }
                
                // 这里需要从initResult中获取新的parentMsgId和toolId
                // 并更新到用户配置中
                // 暂时跳过具体实现
            }
            
            // 使用多线程处理商品下架
            int actualThreads = Math.min(maxThreads, productIds.size());
            ExecutorService executor = Executors.newFixedThreadPool(actualThreads);
            
            try {
                // 提交所有任务
                List<CompletableFuture<Map<String, Object>>> futures = productIds.stream()
                    .map(productId -> CompletableFuture.supplyAsync(() -> 
                        processSingleProduct(productId, parentMsgId, toolId, kuajingmaihuoCookie, mallid, originUrl), executor))
                    .collect(Collectors.toList());
                
                // 等待所有任务完成
                List<Map<String, Object>> results = futures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());
                
                // 统计结果
                long successCount = results.stream().filter(r -> (Boolean) r.get("success")).count();
                long totalCount = results.size();
                
                Map<String, Object> resultData = new HashMap<>();
                resultData.put("total", totalCount);
                resultData.put("success", successCount);
                resultData.put("failed", totalCount - successCount);
                resultData.put("results", results);
                resultData.put("threadInfo", Map.of(
                    "maxThreads", maxThreads,
                    "actualThreads", actualThreads,
                    "productCount", productIds.size()
                ));
                
                return Result.success(resultData);
                
            } finally {
                executor.shutdown();
            }
            
        } catch (Exception e) {
            return Result.error("批量下架处理失败: " + e.getMessage());
        }
    }
    
    /**
     * 初始化下架会话
     */
    private Result initializeOfflineSession(String cookie, String mallid, String originUrl) {
        try {
            String initUrl = "https://seller.kuajingmaihuo.com/bg/cute/api/merchantService/chat/sendMessage";
            
            Map<String, Object> payload = new HashMap<>();
            payload.put("contentType", 1);
            payload.put("content", "商品下架");
            
            var response = networkRequest.post(initUrl, payload, cookie, mallid, originUrl);
            if (response == null || response.getStatusCode() != org.springframework.http.HttpStatus.OK) {
                return Result.error("初始化下架对话失败");
            }
            
            // 解析响应获取msgId
            String responseBody = response.getBody();
            if (responseBody != null) {
                try {
                    JsonNode jsonNode = JsonUtils.parseJson(responseBody);
                    Boolean success = JsonUtils.getBoolean(jsonNode, "success");
                    
                    if (success != null && success) {
                        JsonNode resultNode = JsonUtils.getNode(jsonNode, "result");
                        if (resultNode != null) {
                            String msgId = JsonUtils.getString(resultNode, "msgId");
                            if (msgId != null) {
                                // 这里应该更新用户配置中的parentMsgId
                                return Result.success("初始化成功，获取到消息ID: " + msgId);
                            }
                        }
                    }
                } catch (Exception e) {
                    // JSON解析失败，继续执行
                }
            }
            
            return Result.success("初始化成功");
            
        } catch (Exception e) {
            return Result.error("初始化下架会话失败: " + e.getMessage());
        }
    }
    
    /**
     * 处理单个商品的下架
     */
    private Map<String, Object> processSingleProduct(Integer productId, String parentMsgId, String toolId, 
                                                   String cookie, String mallid, String originUrl) {
        Map<String, Object> result = new HashMap<>();
        result.put("productId", productId);
        result.put("success", false);
        result.put("message", "");
        result.put("details", new HashMap<>());
        
        try {
            // 1. 查询商品基础信息
            String productInfoUrl = "https://seller.kuajingmaihuo.com/marvel-supplier/api/ultraman/chat/reception/queryProductSkcBasicInfo";
            Map<String, Object> productInfoPayload = new HashMap<>();
            productInfoPayload.put("productSkcId", productId);
            
            var productInfoResponse = networkRequest.post(productInfoUrl, productInfoPayload, cookie, mallid, originUrl);
            if (productInfoResponse == null || productInfoResponse.getStatusCode() != org.springframework.http.HttpStatus.OK) {
                result.put("message", "查询商品信息失败");
                return result;
            }
            
            // 2. 预检查是否可以下架
            String precheckUrl = "https://seller.kuajingmaihuo.com/marvel-supplier/api/ultraman/chat/reception/queryPreInterceptForToolSubmit";
            Map<String, Object> precheckPayload = new HashMap<>();
            precheckPayload.put("toolId", toolId);
            precheckPayload.put("dataId", String.valueOf(productId));
            
            var precheckResponse = networkRequest.post(precheckUrl, precheckPayload, cookie, mallid, originUrl);
            if (precheckResponse == null || precheckResponse.getStatusCode() != org.springframework.http.HttpStatus.OK) {
                result.put("message", "预检查失败");
                return result;
            }
            
            // 3. 发送商品信息进行下架
            String offlineUrl = "https://seller.kuajingmaihuo.com/bg/cute/api/merchantService/chat/sendMessage";
            Map<String, Object> offlinePayload = new HashMap<>();
            offlinePayload.put("parentMsgId", parentMsgId);
            offlinePayload.put("contentType", 7);
            
            Map<String, Object> offlineContent = new HashMap<>();
            offlineContent.put("name", "商品名称"); // 从商品信息中获取
            offlineContent.put("img", "商品图片"); // 从商品信息中获取
            offlineContent.put("dataType", 1);
            offlineContent.put("dataId", String.valueOf(productId));
            offlineContent.put("toolId", toolId);
            
            offlinePayload.put("content", offlineContent);
            
            var offlineResponse = networkRequest.post(offlineUrl, offlinePayload, cookie, mallid, originUrl);
            if (offlineResponse == null || offlineResponse.getStatusCode() != org.springframework.http.HttpStatus.OK) {
                result.put("message", "发送下架请求失败");
                return result;
            }
            
            // 4. 轮询查询下架结果（简化版，实际需要实现轮询逻辑）
            result.put("success", true);
            result.put("message", "下架请求已发送");
            
        } catch (Exception e) {
            result.put("message", "处理异常：" + e.getMessage());
        }
        
        return result;
    }
}
