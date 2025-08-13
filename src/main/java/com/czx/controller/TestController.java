package com.czx.controller;

import com.czx.pojo.Result;
import com.czx.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    TestService testService;

    @GetMapping("/test")
    public Result test() {
        return Result.success(testService.test());
    }
}
