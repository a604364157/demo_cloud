package com.jjx.cloudauth.mapper;

import com.jjx.cloudauth.pojo.Authority;
import com.jjx.cloudauth.pojo.AuthorityExample;
import java.util.List;

public interface AuthorityMapper {
    long countByExample(AuthorityExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Authority record);

    int insertSelective(Authority record);

    List<Authority> selectByExample(AuthorityExample example);

    Authority selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Authority record);

    int updateByPrimaryKey(Authority record);
}