package com.xh.cloud.controller;

import com.xh.cloud.dao.IEmployeeDao;
import com.xh.cloud.entity.Employee;
import com.xh.cloud.service.IDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author xianghui
 */
@RestController
@RequestMapping("/controller")
public class HelloController {

    @Autowired
    private IDemo iDemo;
    @Autowired
    private IEmployeeDao iEmployeeDao;
    @Value("${server.port}")
    private String serverPort;

    @RequestMapping(value = "/hello")
    public String helloController() {
        Employee insertEmployee = Employee.builder().id(2).age(12).gender("male").name("elephant").createTime(LocalDateTime.now()).build();
        Employee employee = iEmployeeDao.save(insertEmployee);
        return "hello spring boot:" + employee.getName() + "\r\n serverPort:" + serverPort;
    }

}
