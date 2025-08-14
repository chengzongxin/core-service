package com.czx.pojo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserConfig {
    private Integer id;
    private Integer userId;
    private String kuajingmaihuoCookie;
    private String agentsellerCookie;
    private String mallid;
    private String parentMsgId;
    private String parentMsgTimestamp;
    private String toolId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
