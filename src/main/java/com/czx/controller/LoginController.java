package com.czx.controller;

import lombok.extern.slf4j.Slf4j;
import com.czx.pojo.Emp;
import com.czx.pojo.Result;
import com.czx.service.EmpService;
import com.czx.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Slf4j
@RestController
public class LoginController {
    @Autowired
    private EmpService empService;

    @PostMapping("/login")
    public Result login(@RequestBody Emp emp){
        log.info("员工登录: {}", emp);
        Emp e = empService.login(emp);

        //登录成功,生成令牌,下发令牌
        if (e != null) {
            HashMap<String, Object> claims = new HashMap<>();
            claims.put("id", e.getId());
            claims.put("username",e.getUsername());
            claims.put("password",e.getPassword());

            String jwt = JwtUtils.generateJwt(claims); //jwt包含了当前登
            return Result.success(jwt);
        }
        //登录失败, 返回错误信息
        return Result.error("用户名或密码错误");
    }
}
