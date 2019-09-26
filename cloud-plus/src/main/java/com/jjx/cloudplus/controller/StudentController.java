package com.jjx.cloudplus.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jjx.cloudplus.entity.Student;
import com.jjx.cloudplus.service.StudentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jiangjx
 * @since 2019-09-26
 */
@RestController
@RequestMapping("/student")
public class StudentController {

    @Resource
    private StudentService studentService;

    @GetMapping("list")
    public List<Student> list() {
        return studentService.list();
    }

    @GetMapping("page")
    public Object page(int offset, int limit) {
        return studentService.page(new Page<>(offset, limit));
    }

}

