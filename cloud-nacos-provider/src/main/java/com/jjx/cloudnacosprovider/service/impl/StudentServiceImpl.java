package com.jjx.cloudnacosprovider.service.impl;

import com.jjx.cloudnacosprovider.entity.Student;
import com.jjx.cloudnacosprovider.mapper.IStudentMapper;
import com.jjx.cloudnacosprovider.service.IStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jiangjx
 * @since 2019-09-30
 */
@Service
public class StudentServiceImpl extends ServiceImpl<IStudentMapper, Student> implements IStudentService {

    @Autowired
    private IStudentMapper studentMapper;

    @Override
    public Student getByName(String name) {
        return studentMapper.getByName(name);
    }

    @Override
    public Student getByName2(String name) {
        return studentMapper.getByName2(name);
    }

}
