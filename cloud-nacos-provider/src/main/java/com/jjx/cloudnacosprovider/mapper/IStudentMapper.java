package com.jjx.cloudnacosprovider.mapper;

import com.jjx.cloudnacosprovider.entity.Student;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jiangjx
 * @since 2019-09-30
 */
public interface IStudentMapper extends BaseMapper<Student> {

    /**
     * getByName
     * @param name name
     * @return student
     */
    @Select("select * from web_student where name = #{name}")
    Student getByName(String name);

    /**
     * getByName
     * @param name name
     * @return student
     */
    Student getByName2(String name);
}
