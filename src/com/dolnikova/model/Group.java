package com.dolnikova.model;

import java.util.ArrayList;
import java.util.List;

public class Group {

    private String id;
    private String name;
    private String startDate;
    private List<Student> studentList = new ArrayList<>();

    public Group() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    @Override
    public String toString() {
        return "Group{" +
                "name = '" + name + '\'' +
                ", startDate = '" + startDate + '\'' +
                ", studentList = " + studentList +
                '}';
    }
}
