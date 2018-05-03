package com.fh.jaxbdemo.bean;

import com.fh.jaxbdemo.annotation.XmlElementWrapper;

import java.util.List;

/**
 * Created by ChuanWang on 2018/5/2.
 */

public class Course {
    private String grade; // 年级

    @XmlElementWrapper(elementName = "science", elementType = Science.class)
    private List<Science> sciences; // 自然

    @XmlElementWrapper(elementName = "english", elementType = English.class)
    private List<English> englishs; // 英文

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public List<Science> getSciences() {
        return sciences;
    }

    public void setSciences(List<Science> sciences) {
        this.sciences = sciences;
    }

    public List<English> getEnglishs() {
        return englishs;
    }

    public void setEnglishs(List<English> englishs) {
        this.englishs = englishs;
    }
}
