package com.czx.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String username;
    private String email;
    private String passwordHash; // 密码哈希
    private Integer isActive;    // 是否激活 (1:激活, 0:未激活)
    private Integer isAdmin;     // 是否管理员 (1:是, 0:否)
    private LocalDateTime createdAt; //创建时间
    private LocalDateTime updatedAt; //修改时间
    
    // 用于登录的构造函数
    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }
}
