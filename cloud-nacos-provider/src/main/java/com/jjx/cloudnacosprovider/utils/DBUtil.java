package com.jjx.cloudnacosprovider.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.fastjson.JSON;
import org.apache.poi.ss.formula.functions.T;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DBUtil {

    private static DruidDataSource dataSource;


    /*
     * 静态创建数据库连接池
     */
    static {
        try {
            dataSource = new DruidDataSource();
            dataSource.setDriverClassName("com.mysql.jdbc.Driver");
            dataSource.setUrl("jdbc:mysql://192.168.138.232:3306/companydb?serverTimezone=Asia/Shanghai");
            dataSource.setUsername("qhyf");
            dataSource.setPassword("123456");
            dataSource.setInitialSize(1);
            dataSource.setMaxActive(3);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("初始化数据库连接时，发生异常");
        }
    }

    public static List<T> qryDataForList(String sql, List<Object> params, Class<T> clazz) {
        try {
            return JSON.parseArray(JSON.toJSONString(JdbcUtils.executeQuery(dataSource, sql, params)), clazz);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Map<String, Object>> qryDataForList(String sql, List<Object> params) {
        try {
            return JdbcUtils.executeQuery(dataSource, sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}

