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
        for (int i = 0; i < 10; i++) {
            JSONObject json = new JSONObject();
            json.put("语文", "133");
            json.put("数学", "126");
            json.put("英语", "142");
            json.put("物理", "90");
            json.put("化学", "89");
            json.put("生物", "76");
            list.add(json);
        }
        File file = ExcelUtil.workBook2File(ExcelUtil.createExcel2007(list, false, false), "test.xlsx");
        file.createNewFile();
    }

}
