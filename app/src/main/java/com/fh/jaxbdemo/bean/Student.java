package com.fh.jaxbdemo.bean;

import com.fh.jaxbdemo.annotation.XmlAttribute;
import com.fh.jaxbdemo.annotation.XmlElementWrapper;
import com.fh.jaxbdemo.annotation.XmlRootElement;

import java.util.List;

/**
 * Created by ChuanWang on 2018/5/2.
 */

@XmlRootElement(name = "student")
public class Student {
    private static final long serialVersionUID = 1L;

    @XmlAttribute(name = "name")
    private String studentname;

    @XmlElementWrapper(elementName = "course", elementType = Course.class)
    private List<Course> courses;

    public String getStudentname() {
        return studentname;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}
