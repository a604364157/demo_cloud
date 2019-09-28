package com.jjx.cloudauth.generate.utils;

import com.alibaba.druid.pool.DruidDataSourceC3P0Adapter;
import com.alibaba.druid.util.JdbcUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jiangjx
 */
public class CommonUtil {

    private static DataSource crateDateSource(String user, String password, String url) {
        DruidDataSourceC3P0Adapter source = new DruidDataSourceC3P0Adapter();
        source.setDriverClass("com.mysql.jdbc.Driver");
        source.setUser(user);
        source.setPassword(password);
        source.setJdbcUrl(url);
        return source;
    }

    public static List<Map<String, Object>> queryTable(String tableName, String user, String password, String url) {
        DataSource dataSource = crateDateSource(user, password, url);
        String sql = "DESC " + tableName;
        List<Map<String, Object>> list;
        try {
            list = JdbcUtils.executeQuery(dataSource, sql, new ArrayList<>());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询表模型失败");
        }
        //截掉公共的字段
        list.subList(0, 9).clear();
        return list;
    }

    private static Pattern p1 = Pattern.compile("[A-Z]");

    public static String ca2str(String str) {
        Matcher matcher = p1.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static Pattern p2 = Pattern.compile("_[a-z]");

    public static String str2Ca(String str) {
        Matcher matcher = p2.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String t = matcher.group(0);
            matcher.appendReplacement(sb, t.substring(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static String first2Up(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}
