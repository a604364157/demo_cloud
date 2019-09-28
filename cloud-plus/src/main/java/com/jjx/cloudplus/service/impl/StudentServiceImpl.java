package com.jjx.cloudplus.service.impl;

import com.jjx.cloudplus.entity.Student;
import com.jjx.cloudplus.mapper.IStudentMapper;
import com.jjx.cloudplus.service.IStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jiangjx
 * @since 2019-09-27
 */
@Primary
@Service
public class StudentServiceImpl extends ServiceImpl<IStudentMapper, Student> implements IStudentService {

}
