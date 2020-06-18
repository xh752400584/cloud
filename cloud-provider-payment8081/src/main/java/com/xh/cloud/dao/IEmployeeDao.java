package com.xh.cloud.dao;



import com.xh.cloud.entity.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IEmployeeDao extends CrudRepository<Employee, Long> {

    @Query(value = "SELECT e FROM Employee e WHERE name=:name")
    public Employee findName(@Param("name") String name);
}