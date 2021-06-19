package com.xh.cloud.entity;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

// 可以用@EqualsAndHashCode(callSuper = true)注释解决，但每个子类都得修改，很不方便
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "tb_employee")
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends BaseEntity implements Serializable {

    @Column(name="name")
    private String name;

    @Column(name="age")
    private Integer age;

    @Column(name="gender")
    private String gender;

    @Builder
    public Employee(long id, String createUserName, LocalDateTime createTime, String updateUserName, LocalDateTime updateTime, String name, Integer age, String gender) {
        super(id, createUserName, createTime, updateUserName, updateTime);
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

}
