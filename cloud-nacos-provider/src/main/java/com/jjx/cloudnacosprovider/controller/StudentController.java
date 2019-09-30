package com.jjx.cloudnacosprovider.controller;


import com.jjx.cloudnacosprovider.entity.Student;
import com.jjx.cloudnacosprovider.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jiangjx
 * @since 2019-09-30
 */
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private IStudentService studentService;

    @GetMapping("list")
    public List<Student> list() {
        return studentService.list();
    }

}

