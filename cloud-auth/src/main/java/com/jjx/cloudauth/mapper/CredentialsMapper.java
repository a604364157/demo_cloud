package com.jjx.cloudauth.mapper;

import com.jjx.cloudauth.pojo.Credentials;
import com.jjx.cloudauth.pojo.CredentialsExample;
import java.util.List;

public interface CredentialsMapper {
    long countByExample(CredentialsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Credentials record);

    int insertSelective(Credentials record);

    List<Credentials> selectByExample(CredentialsExample example);

    Credentials selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Credentials record);

    int updateByPrimaryKey(Credentials record);
}