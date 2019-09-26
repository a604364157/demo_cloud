package com.jjx.cloudplus.service.impl;

import com.jjx.cloudplus.entity.Student;
import com.jjx.cloudplus.mapper.StudentMapper;
import com.jjx.cloudplus.service.StudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jiangjx
 * @since 2019-09-26
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

}
