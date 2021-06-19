package com.xh.cloud.controller;

import com.xh.cloud.dao.IEmployeeDao;
import com.xh.cloud.entity.Employee;
import com.xh.cloud.service.IDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/controller")
public class HelloController {

    @Autowired
    private IDemo iDemo;
    @Autowired
    private IEmployeeDao iEmployeeDao;
    @Value("${server.port}")
    private String serverPort;
//    @Autowired
//    private Redis;

    @RequestMapping(value = "/hello")
    public String helloController() {
//        List<Employee> employeeList = (List<Employee>) iEmployeeDao.findAll();
        Employee insertEmployee = new Employee();
        insertEmployee.setAge(24);
        insertEmployee.setGender("男");
        insertEmployee.setName("渣渣辉");
        Employee employee = iEmployeeDao.save(insertEmployee);
        return "hello spring boot:" + employee.getName() + "\r\n serverPort:" + serverPort;
//        return "hello spring boot:";
    }

}
