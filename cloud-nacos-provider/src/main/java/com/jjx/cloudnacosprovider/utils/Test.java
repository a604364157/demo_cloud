package com.jjx.cloudnacosprovider.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Test {

    public static void main(String[] args) throws IOException {
        List<Map<String, Object>> list = DBUtil.qryDataForList("SELECT * FROM companyconfigset", new ArrayList<>());
        File file = ExcelUtil.workBook2File(ExcelUtil.createExcel2007(list, false, false), "test.xls");
        file.createNewFile();
    }

}
