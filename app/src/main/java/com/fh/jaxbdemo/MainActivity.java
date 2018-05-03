package com.fh.jaxbdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.fh.jaxbdemo.bean.Student;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    InputStream inputStream = null;
    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            inputStream = MainActivity.this.getAssets().open("student.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        JaxbUtils<Student> jaxbUtils = new JaxbUtils<>();
        if (null != inputStream) {
            student = jaxbUtils.parse(inputStream, Student.class);
            if (null != student) {
                String coursename = student.getCourses().get(0).getEnglishs().get(1).getCoursename();
                Toast.makeText(this, coursename, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
