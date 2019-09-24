package com.jjx.cloudauth.generate;

import com.alibaba.druid.pool.DruidDataSourceC3P0Adapter;
import com.alibaba.druid.util.JdbcUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jiangjx
 */
@SuppressWarnings("unused")
public class GeneratePd {

    public static void main(String[] args) {
        String url = "jdbc:mysql://192.168.138.233:33306/elestamp?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull";
        String user = "root";
        String password = "123456";
        String dbName = "elestamp";
        String tableName = "es_electronics_file_certificate_mapping";
        String root = "base.biz.svc.dianzizhang\\src\\main\\java";
        String pojoPack = "com.cdyfsz.base.biz.svc.dianzizhang.pojo2";
        String daoPack = "com.cdyfsz.base.biz.svc.dianzizhang.entcermgr.dao2";

        generate(url, user, password, dbName, tableName, root, pojoPack, daoPack);
    }

    private static void generate(String url, String user, String password, String dbName, String tableName, String root, String pojoPack, String daoPack) {
        GeneratePd pd = new GeneratePd();
        List<Map<String, Object>> list = pd.queryTable(tableName, user, password, url);
        pd.generatePojo(root, pojoPack, pd.first2Up(pd.str2Ca(tableName)), dbName, list);
        pd.generateDao(root, daoPack, pojoPack + "." + pd.first2Up(pd.str2Ca(tableName)));
    }

    private void generatePojo(String root, String pack, String className, String dbName, List<Map<String, Object>> fields) {
        File pojo = createJava(root, pack, className);
        try (FileOutputStream fos = new FileOutputStream(pojo)) {
            List<String> words = createPojo(pack, className, dbName, fields);
            words.forEach(word -> {
                try {
                    fos.write(word.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> createPojo(String packPath, String className, String dbName, List<Map<String, Object>> fields) {
        List<String> list = new ArrayList<>();
        list.add("package " + packPath + ";" + "\n\n");
        list.add("import club.newepoch.db.common.DbDataType;\n" +
                "import club.newepoch.db.interfaces.TableBind;\n" +
                "import club.newepoch.db.interfaces.TableCol;\n" +
                "import club.newepoch.isf.pojo.BaseModeByKeyId;\n" +
                "import lombok.Data;\n\n");
        list.add("@Data\n");
        list.add("@TableBind(dsName = \"" + dbName + "\")\n");
        list.add("public class " + className + " extends BaseModeByKeyId<" + className + "> {\n\n");
        list.add("\tprivate static final long serialVersionUID = 1L;\n");
        for (Map<String, Object> field : fields) {
            String fieldName = str2Ca(String.valueOf(field.get("Field")));
            String fieldType = String.valueOf(field.get("Type"));
            String tmp = "\t@TableCol(dataType = DbDataType." + getType(fieldType) + ", maxLen = " + getLength(fieldType) + ", memo = \"\")\n";
            list.add(tmp);
            String fieldTmp = "\tprivate " + getFieldType(fieldType) + " " + str2Ca(fieldName) + ";\n\n";
            list.add(fieldTmp);
        }
        list.add("}");
        return list;
    }

    private String icon = "(";

    private String getType(String type) {
        if (type.contains(icon)) {
            type = type.substring(0, type.indexOf(icon));
        }
        switch (type) {
            case "bigint":
                return "BIGINT";
            case "varchar":
                return "VARCHAR";
            case "char":
                return "CHAR";
            case "timestamp":
                return "DATE";
            default:
                return "";
        }
    }

    private String getFieldType(String type) {
        if (type.contains(icon)) {
            type = type.substring(0, type.indexOf(icon));
        }
        switch (type) {
            case "bigint":
                return "Long";
            case "varchar":
            case "char":
                return "String";
            case "timestamp":
                return "Date";
            default:
                return "";
        }
    }

    private String getLength(String type) {
        if (type.contains(icon)) {
            return type.substring(type.indexOf(icon) + 1, type.indexOf(")"));
        } else {
            //暂时不管，（无长度的）理论是时间类型
            return "10";
        }
    }

    private void generateDao(String root, String pack, String className) {
        String interName = "I" + className.substring(className.lastIndexOf(".") + 1) + "Dao";
        File daoInter = createJava(root, pack, interName);
        try (FileOutputStream fos = new FileOutputStream(daoInter)) {
            List<String> words = createDaoInter(pack, className);
            words.forEach(word -> {
                try {
                    fos.write(word.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        String daoName = className.substring(className.lastIndexOf(".") + 1) + "DaoImpl";
        pack = pack + ".impl";
        File dao = createJava(root, pack, daoName);
        try (FileOutputStream fos = new FileOutputStream(dao)) {
            List<String> words = createDao(pack, className);
            words.forEach(word -> {
                try {
                    fos.write(word.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createJava(String root, String path, String name) {
        String packPath = path.replaceAll("\\.", "\\\\");
        if (root != null && !"".equals(root)) {
            packPath = root + "\\" + packPath;
        }
        File file = new File(packPath + File.separator + name + ".java");
        if (!file.exists()) {
            try {
                System.out.println(file.getParentFile().mkdirs());
                System.out.println(file.createNewFile());
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }
        return file;
    }

    private List<String> createDaoInter(String packPath, String entity) {
        List<String> list = new ArrayList<>();
        list.add("package " + packPath + ";\n\n");
        list.add("import club.newepoch.db.pojo.Parameter;\n" +
                "import club.newepoch.db.pojo.SortInfo;\n" +
                "import club.newepoch.isf.pojo.ApiReq;\n");
        list.add("import " + entity + ";\n\n");
        list.add("import java.util.List;\n\n");
        String entityName = entity.substring(entity.lastIndexOf(".") + 1);
        list.add("public interface I" + entityName + "Dao {\n\n");
        list.add("\tBoolean save(ApiReq apiReq, " + entityName + " entity);\n\n");
        list.add("\tBoolean del(ApiReq apiReq, long keyId);\n\n");
        list.add("\tBoolean update(ApiReq apiReq, long keyId, " + entityName + " entity);\n\n");
        list.add("\t" + entityName + " getByKeyId(long keyId);\n\n");
        list.add("\tList<" + entityName + "> getList(List<Parameter> parameters, List<SortInfo> sortInfos);\n\n");
        list.add("}");
        return list;
    }

    private List<String> createDao(String packPath, String entity) {
        List<String> list = new ArrayList<>();
        list.add("package " + packPath + ";\n\n");
        list.add("import club.newepoch.db.pojo.Parameter;\n" +
                "import club.newepoch.db.pojo.SortInfo;\n" +
                "import club.newepoch.isf.dao.BaseDao;\n" +
                "import club.newepoch.isf.pojo.ApiReq;\n\n");
        String entityName = entity.substring(entity.lastIndexOf(".") + 1);
        String dao = packPath.substring(0, packPath.lastIndexOf(".")) + ".I" + entityName + "Dao";
        list.add("import " + dao + ";\n");
        list.add("import " + entity + ";\n");
        list.add("import org.springframework.stereotype.Component;\n\n");
        list.add("import java.util.List;\n\n");
        list.add("@Component\n");
        list.add("public class " + entityName + "DaoImpl" + " extends BaseDao implements " + "I" + entityName + "Dao" + " {\n\n");
        list.add("\t@Override\n");
        list.add("\tpublic Boolean save(ApiReq apiReq, " + entityName + " entity) {\n");
        list.add("\t\treturn entity.save(apiReq);\n");
        list.add("\t}\n\n");
        list.add("\t@Override\n");
        list.add("\tpublic Boolean del(ApiReq apiReq, long keyId) {\n");
        list.add("\t\t" + entityName + " entity = new " + entityName + "();\n");
        list.add("\t\tentity.setKeyId(keyId);\n");
        list.add("\t\treturn entity.del(apiReq) > 0;\n");
        list.add("\t}\n\n");
        list.add("\t@Override\n");
        list.add("\tpublic Boolean update(ApiReq apiReq, long keyId, " + entityName + " entity) {\n");
        list.add("\t\tentity.setKeyId(keyId);\n");
        list.add("\t\treturn entity.updateByKey(apiReq) > 0;\n");
        list.add("\t}\n\n");
        list.add("\t@Override\n");
        list.add("\tpublic " + entityName + " getByKeyId(long keyId) {\n");
        list.add("\t\t" + entityName + " entity = new " + entityName + "();\n");
        list.add("\t\treturn entity.findByKey(keyId);\n");
        list.add("\t}\n\n");
        list.add("\t@Override\n");
        list.add("\tpublic List<" + entityName + "> getList(List<Parameter> parameters, List<SortInfo> sortInfos) {\n");
        list.add("\t\t" + entityName + " entity = new " + entityName + "();\n");
        list.add("\t\treturn entity.findList(parameters, sortInfos);\n");
        list.add("\t}\n\n");
        list.add("}");
        return list;
    }

    private Pattern humpPattern = Pattern.compile("[A-Z]");

    private String ca2str(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private Pattern p = Pattern.compile("_[a-z]");

    private String str2Ca(String str) {
        Matcher matcher = p.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String t = matcher.group(0);
            matcher.appendReplacement(sb, t.substring(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private String first2Up(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private List<Map<String, Object>> queryTable(String tableName, String user, String password, String url) {
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

    private DataSource crateDateSource(String user, String password, String url) {
        DruidDataSourceC3P0Adapter source = new DruidDataSourceC3P0Adapter();
        source.setDriverClass("com.mysql.jdbc.Driver");
        source.setUser(user);
        source.setPassword(password);
        source.setJdbcUrl(url);
        return source;
    }

}
