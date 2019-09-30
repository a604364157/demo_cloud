package com.jjx.cloudnacosprovider.service.impl;

import com.jjx.cloudnacosprovider.entity.Student;
import com.jjx.cloudnacosprovider.mapper.IStudentMapper;
import com.jjx.cloudnacosprovider.service.IStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
@Primary
@Service
public class StudentServiceImpl extends ServiceImpl<IStudentMapper, Student> implements IStudentService {

}
