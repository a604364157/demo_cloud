package com.jjx.cloudnacosprovider.service;

import com.jjx.cloudnacosprovider.entity.Student;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jiangjx
 * @since 2019-09-30
 */
public interface IStudentService extends IService<Student> {

    /**
     * get
     * @param name name
     * @return student
     */
    Student getByName(String name);

    /**
     * get
     * @param name name
     * @return student
     */
    Student getByName2(String name);
}
