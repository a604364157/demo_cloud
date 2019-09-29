package com.jjx.cloudplus.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Test {

    public static void main(String[] args) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            JSONObject json = new JSONObject();
            json.put("语文", "133");
            json.put("数学", "126");
            json.put("英语", "142");
            json.put("物理", "90");
            json.put("化学", "89");
            json.put("生物", "76");
            json.put("生物1", "76");
            json.put("生物2", "76");
            json.put("生物3", "76");
            json.put("生物4", "76");
            json.put("生物5", "76");
            json.put("生物6", "76");
            json.put("生物7", "76");
            json.put("生物8", "76");
            json.put("生物9", "76");
            json.put("生物q", "76");
            json.put("生物w", "76");
            json.put("生物e", "76");
            json.put("生物r", "76");
            json.put("生物t", "76");
            json.put("生物y", "76");
            json.put("生物u", "76");
            json.put("生物i", "76");
            list.add(json);
        }
        File file = ExcelUtil.workBook2File(ExcelUtil.createExcel2003(list, false, false), "test.xls");
        file.createNewFile();
    }

}
