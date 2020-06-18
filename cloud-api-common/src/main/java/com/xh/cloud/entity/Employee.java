package com.xh.cloud.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity//告诉JPA这是一个实体类（和数据表映射的类）
@Table(name = "Table_Employee")//指定和那个数据表对应，如果省略默认表名就是类名
public class Employee extends BaseEntity {

    @Column(name="name")//这是和数据表对应的一个列，省略默认列名就是属性名
    private String name;

    @Column(name="age")
    private String age;

    @Column(name="gender")
    private String gender;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
