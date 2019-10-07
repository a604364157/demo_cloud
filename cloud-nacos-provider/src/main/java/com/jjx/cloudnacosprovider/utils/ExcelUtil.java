package com.jjx.cloudnacosprovider.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jiangjx
 */
@Slf4j
@SuppressWarnings("unused")
public class ExcelUtil {

    /**
     * 创建EXCEL文件
     *
     * @param list 数据集合
     * @return excel文件实体
     */
    public static XSSFWorkbook createExcel2007(List<Map<String, Object>> list, boolean hasTitle, boolean readOnly) {
        if (!hasTitle) {
            addTitle(list);
        }
        //生成xlsx文件
        XSSFWorkbook workBook = new XSSFWorkbook();
        // 生成一个sheet
        XSSFSheet sheet = workBook.createSheet("sheet");
        if (readOnly) {
            encryption(sheet);
        }
        CellStyle style = workBook.createCellStyle();
        Font font = workBook.createFont();
        font.setColor(Font.COLOR_NORMAL);
        style.setFont(font);
        //插入需导出的数据
        AtomicInteger i = new AtomicInteger();
        list.forEach(map -> {
            XSSFRow row = sheet.createRow(i.get());
            AtomicInteger j = new AtomicInteger();
            map.forEach((key, value) -> {
                if (i.get() == 0) {
                    XSSFCell cell = row.createCell(j.get());
                    getStyle(style);
                    cell.setCellStyle(style);
                    cell.setCellValue(String.valueOf(key));
                } else {
                    XSSFCell cell = row.createCell(j.get());
                    getStyle(style);
                    cell.setCellStyle(style);
                    cell.setCellValue(String.valueOf(value));
                }
                j.getAndIncrement();
            });
            i.getAndIncrement();
        });
/*        try {
            putWaterRemarkToExcel(workBook, sheet, "qhyf.png", 1, 1);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        setPrint(sheet);*/
        return workBook;
    }

    public static HSSFWorkbook createExcel2003(List<Map<String, Object>> list, boolean hasTitle, boolean readOnly) {
        if (!hasTitle) {
            addTitle(list);
        }
        HSSFWorkbook workBook = new HSSFWorkbook();
        HSSFSheet sheet = workBook.createSheet("sheet");
        if (readOnly) {
            encryption(sheet);
        }
        int i = 0;
        CellStyle style = workBook.createCellStyle();
        Font font = workBook.createFont();
        font.setColor(Font.COLOR_NORMAL);
        style.setFont(font);
        for (Map<String, Object> map : list) {
            HSSFRow row = sheet.createRow(i);
            int j = 0;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (i == 0) {
                    HSSFCell cell = row.createCell(j);
                    getStyle(style);
                    cell.setCellStyle(style);
                    cell.setCellValue(String.valueOf(entry.getKey()));
                } else {
                    HSSFCell cell = row.createCell(j);
                    getStyle(style);
                    cell.setCellStyle(style);
                    cell.setCellValue(String.valueOf(entry.getValue()));
                }
                j++;
            }
            i++;
        }
        return workBook;
    }

    private static void addTitle(List<Map<String, Object>> list) {
        list.add(0, list.get(0));
    }

    private static void encryption(Sheet sheet) {
        sheet.protectSheet(UUID.randomUUID().toString());
    }

    private static void setPrint(Sheet sheet) {
        //设置边框
        sheet.setDisplayGridlines(true);
        //设置打印的边框
        sheet.setPrintGridlines(true);
        //设置打印对象
        PrintSetup printSetup = sheet.getPrintSetup();
        //设置打印方向，横向就是true
        printSetup.setLandscape(true);
        //设置A4纸
        printSetup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
    }

    /**
     * 为Excel打上水印工具函数
     * 请自行确保参数值，以保证水印图片之间不会覆盖。
     * 在计算水印的位置的时候，并没有考虑到单元格合并的情况，请注意
     *
     * @param wb              Excel Workbook
     * @param sheet           需要打水印的Excel
     * @param waterRemarkPath 水印地址，classPath，目前只支持png格式的图片，
     *                        因为非png格式的图片打到Excel上后可能会有图片变红的问题，且不容易做出透明效果。
     *                        同时请注意传入的地址格式，应该为类似："\\excelTemplate\\test.png"
     * @param XCount          横向共有水印多少个
     * @param YCount          纵向共有水印多少个
     * @throws IOException 异常
     */
    private static void putWaterRemarkToExcel(Workbook wb, Sheet sheet, String waterRemarkPath, int XCount, int YCount) throws IOException {
        //校验传入的水印图片格式
        if (!waterRemarkPath.endsWith("png") && !waterRemarkPath.endsWith("PNG")) {
            throw new RuntimeException("向Excel上面打印水印，目前支持png格式的图片。");
        }
        //加载图片
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        InputStream imageIn = Thread.currentThread().getContextClassLoader().getResourceAsStream(waterRemarkPath);
        if (null == imageIn || imageIn.available() < 1) {
            throw new RuntimeException("向Excel上面打印水印，读取水印图片失败(1)。");
        }
        BufferedImage bufferImg = ImageIO.read(imageIn);
        if (null == bufferImg) {
            throw new RuntimeException("向Excel上面打印水印，读取水印图片失败(2)。");
        }
        ImageIO.write(bufferImg, "png", byteArrayOut);
        //计算宽高
        int w = 1;
        int h = 1;
        //开始打水印
        Drawing drawing = sheet.createDrawingPatriarch();
        //按照共需打印多少行水印进行循环
        for (int yCount = 0; yCount < YCount; yCount++) {
            //按照每行需要打印多少个水印进行循环
            for (int xCount = 0; xCount < XCount; xCount++) {
                //创建水印图片位置
                int xIndexInteger = xCount * w;
                int yIndexInteger = yCount * h;
                /*
                 * 参数定义：
                 * 第1个参数是（x轴的开始节点）；
                 * 第2个参数是（是y轴的开始节点）；
                 * 第3个参数是（是x轴的结束节点）；
                 * 第4个参数是（是y轴的结束节点）；
                 * 第5个参数是（是从Excel的第几列开始插入图片，从0开始计数）；
                 * 第6个参数是（是从excel的第几行开始插入图片，从0开始计数）；
                 * 第7个参数是（图片宽度，共多少列）；
                 * 第8个参数是（图片高度，共多少行）；
                 */
                ClientAnchor anchor = drawing.createAnchor(0, 0, Short.MAX_VALUE, Integer.MAX_VALUE, xIndexInteger, yIndexInteger, 0, 0);
                anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);
                Picture pic = drawing.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), Workbook.PICTURE_TYPE_PNG));
                pic.resize();
            }
        }
    }

    private static void getStyle(CellStyle style) {
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
    }

    public static File workBook2File(Workbook workbook, String fileName) {
        try (FileOutputStream outStream = new FileOutputStream(fileName)) {
            workbook.write(outStream);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return new File(fileName);
    }

}