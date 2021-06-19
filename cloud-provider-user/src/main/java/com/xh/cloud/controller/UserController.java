package com.xh.cloud.controller;

import com.xh.cloud.entity.Employee;
import com.xh.cloud.entity.po.UserPO;
import com.xh.cloud.mapper.UserMapper;
import com.xh.cloud.service.MessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    public static final String PAYMENT_URL = "http://CLOUD-PROVIDER-PAYMENT";

    @Resource
    private UserMapper userMapper;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private MessageService messageService;

    @GetMapping("/user/{id}")
    public UserPO getUser(@PathVariable("id") Integer id) {
        userMapper.selectList(null);
        return userMapper.getUserById(id);
    }

    @GetMapping("/rpc/{id}")
    public String getRpc(@PathVariable("id") Integer id) {
        StringBuilder result = new StringBuilder();
        result.append(restTemplate.postForObject(PAYMENT_URL + "/controller/hello", "", String.class));
//        result.append("\r\n" + userMapper.getUserById(id).toString());
        return result.toString();
    }

    @GetMapping("/msg/{msg}")
    public String sendMsg(@PathVariable("msg") String msg) {
        try {
            messageService.sendLoginMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "sucess";
    }

    @GetMapping("/message/{message}")
    public String sendMsg(@PathVariable("message") Integer message) {
        try {
            Employee user = Employee.builder().id(231).createTime(LocalDateTime.now()).createUserName("dasd").gender("女").build();
            Employee user1 = Employee.builder().id(2312).age(123).name("erqwe").createTime(LocalDateTime.now()).createUserName("dasd").gender("女").build();
            List<Employee> list = new ArrayList<>();
            list.add(user);
            list.add(user1);
            messageService.sendLoginMessage(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "sucess";
    }

    @GetMapping("/batchMessage")
    public String sendBatchMessage() {
        try {
            Employee user = Employee.builder().id(231).createTime(LocalDateTime.now()).createUserName("dasd").gender("女").build();
            Employee user1 = Employee.builder().id(2312).age(123).name("erqwe").createTime(LocalDateTime.now()).createUserName("dasd").gender("女").build();
            List<Employee> list = new ArrayList<>();
            list.add(user);
            list.add(user1);
            // 循环10次，测试批量消息消费
            for (int i = 0; i < 10; i++) {
                messageService.sendLoginMessage(list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "sucess";
    }
}
