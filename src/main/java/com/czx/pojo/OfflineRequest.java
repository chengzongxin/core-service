package com.czx.pojo;

import lombok.Data;
import java.util.List;

@Data
public class OfflineRequest {
    private List<Integer> productIds;
    private int maxThreads = 8;
}
