package com.xh.cloud.controller;

import com.xh.cloud.entity.Employee;
import com.xh.cloud.entity.User;
import com.xh.cloud.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
public class UserController {
    public static final String PAYMENT_URL = "http://CLOUD-PROVIDER-PAYMENT";

    @Autowired
    private UserMapper userMapper;
    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") Integer id) {
        return userMapper.getUserById(id);
    }

    @GetMapping("/rpc/{id}")
    public String getRpc(@PathVariable("id") Integer id) {
        StringBuilder result = new StringBuilder();
        result.append(restTemplate.postForObject(PAYMENT_URL + "/controller/hello", "", String.class));
        result.append("\r\n" + userMapper.getUserById(id).toString());
        return result.toString();
    }
}
