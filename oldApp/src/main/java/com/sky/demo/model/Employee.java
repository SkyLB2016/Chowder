package com.sky.demo.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
/**
 * Created by sky on 15/12/9 下午8:54.
 *  员工的属性
 */
public class Employee extends Person {
    private static final long serialVersionUID = 1L;
    private String departmentName;//部门名称
    private String employeeId;//员工id
    private String position;//员工职位

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return super.toString() +"\n"+ "Employee{" +
                "departmentName='" + departmentName + "'" +
                ", employeeId='" + employeeId + '\'' +
                ", position='" + position + '\'' +
                "}";
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeInt(getAge());
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        setAge(stream.readInt());
    }

}
