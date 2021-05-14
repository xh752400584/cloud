package com.xh.cloud.entity;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "tb_employee")
public class Employee extends BaseEntity {

    @Column(name="name")
    private String name;

    @Column(name="age")
    private String age;

    @Column(name="gender")
    private String gender;
}
