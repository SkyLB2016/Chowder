package com.sky.demo.model;

import java.util.Arrays;

public class Student extends Person implements Comparable<Student> {
    private static final long serialVersionUID = 1L;
    private String schoolName;//学校名称
    private String studentId;//学生id
    private String grade;//年级
    private String clazz;//班级
    private String[] course;//选修科目
    private double[] courseNumber;//科目得分

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String[] getCourse() {
        return course;
    }

    public void setCourse(String[] course) {
        this.course = course;
    }

    public double[] getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(double[] courseNumber) {
        this.courseNumber = courseNumber;
    }

    @Override
    public String toString() {
        return super.toString() +"\n"+ "Student{" +
                "schoolName='" + schoolName + '\'' +
                ", studentId='" + studentId + '\'' +
                ", grade='" + grade + '\'' +
                ", clazz='" + clazz + '\'' +
                ", course=" + Arrays.toString(course) +
                ", courseNumber=" + Arrays.toString(courseNumber) +
                '}';
    }

    /**
     * Collections.sort(student)
     * @param another
     * @return
     */
    @Override
    public int compareTo(Student another) {
        return this.studentId.compareTo(another.getStudentId());
    }
}
