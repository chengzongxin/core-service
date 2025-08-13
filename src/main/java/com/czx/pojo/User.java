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
    private String passwordHash;
    private Integer isActive;
    private Integer isAdmin;
    private LocalDateTime createdAt; //创建时间
    private LocalDateTime updatedAt; //修改时间
}
