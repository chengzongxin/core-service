package com.czx.pojo;

import lombok.Data;
import java.util.List;

@Data
public class ProductQueryRequest {
    private List<Integer> productIds;
    private String productName;
    private int page = 1;
    private int pageSize = 500;
}
