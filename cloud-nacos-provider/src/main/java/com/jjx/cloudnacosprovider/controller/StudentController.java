package com.jjx.cloudnacosprovider.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jjx.cloudnacosprovider.entity.Student;
import com.jjx.cloudnacosprovider.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("{id}")
    public Student getById(@PathVariable("id") String id) {
        return studentService.getById(id);
    }

    @GetMapping("list")
    public List<Student> list() {
        return studentService.list();
    }

    @GetMapping("page")
    public IPage<Student> page(@RequestParam int offset, @RequestParam int limit) {
        return studentService.page(new Page<>(offset, limit));
    }

    @GetMapping("query")
    public List<Student> queryByName(@RequestParam String name) {
        Wrapper<Student> query = Wrappers.<Student>lambdaQuery().like(Student::getName, name);
        return studentService.list(query);
    }

    @GetMapping("get")
    public Student getByName(@RequestParam String name) {
        return studentService.getByName(name);
    }

    @GetMapping("get2")
    public Student getByName2(@RequestParam String name) {
        return studentService.getByName2(name);
    }

}

