package com.xh.cloud.util;

import cn.hutool.core.util.ObjectUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Copyright 2021
 * Tools for excel
 *
 * @author XiangHui
 * @date 2021/4/22 23:23:03
 */
public class ExcelUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * 2007 版本以上 最大支持1048576行
     */
    public static final String EXCEL_FILE_2007 = "2007";
    private static final Integer EXCEL_FILE_2007_DATA_MAX = 1048576;
    /**
     * 2003 版本 最大支持65536 行
     */
    public static final String EXCEL_FILE_2003 = "2003";
    private static final Integer EXCEL_FILE_2003_DATA_MAX = 65536;


    /**
     * 导出Excel文件
     *
     * @param response       response导出到浏览器
     * @param fileName       文件名
     * @param sheetName      表格标题
     * @param fieldMap       表格标题行映射(标题名-><T>属性名)
     * @param dataCollection 数据集合
     * @param <T>            泛型
     */
    public static <T> void exportExcelFile(@NotNull HttpServletResponse response, @NotEmpty String fileName, String sheetName,
                                           @NotEmpty LinkedHashMap<String, String> fieldMap, Collection<T> dataCollection) {
        exportExcelFile(response, fileName, sheetName, fieldMap, dataCollection, null);
    }

    /**
     * 导出Excel文件
     *
     * @param response       response导出到浏览器
     * @param fileName       文件名
     * @param sheetName      表格标题
     * @param fieldMap       表格标题行映射(标题名-><T>属性名)
     * @param dataCollection 数据集合
     * @param version        2003 或者 2007，不传时按数据长度选择版本
     * @param <T>            泛型
     */
    public static <T> void exportExcelFile(@NotNull HttpServletResponse response, @NotEmpty String fileName, String sheetName,
                                           @NotEmpty LinkedHashMap<String, String> fieldMap, Collection<T> dataCollection, String version) {
        // 清空输出流
        response.reset();
        try (OutputStream outputStream = response.getOutputStream()) {
            Workbook workbook;
            // 指定导出版本按指定版本导出，未指定版本按数据长度导出
            switch (version) {
                case EXCEL_FILE_2003:
                    // 声明一个工作薄
                    workbook = new HSSFWorkbook();
                    fileName = fileName + ".xls";
                    break;
                case EXCEL_FILE_2007:
                    // 声明一个工作薄
                    workbook = new XSSFWorkbook();
                    fileName = fileName + ".xlsx";
                    break;
                default:
                    if (ObjectUtil.isNotEmpty(dataCollection) && EXCEL_FILE_2003_DATA_MAX < dataCollection.size()) {
                        workbook = new XSSFWorkbook();
                        fileName = fileName + ".xlsx";
                    } else {
                        workbook = new HSSFWorkbook();
                        fileName = fileName + ".xls";
                    }
                    break;
            }
            // 设定输出文件头
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder
                .encode(fileName, StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20"));
            // 定义输出类型
            response.setContentType("application/msexcel");
            // 将普通表格数据写入workbook
            writeDataToExcelWorkbook(workbook, sheetName, fieldMap, dataCollection);
            workbook.write(outputStream);

        } catch (Exception e) {
            LOG.error("导出Excel文件失败:", e);
        }
    }


    /**
     * 导出Excel 2003 版本 最大支持65536 行
     *
     * @param outputStream   输出流
     * @param title          表格标题
     * @param fieldMap       <标题,属性>的顺序映射
     * @param dataCollection 数据集合
     * @param <T>            泛型
     */
    public static <T> void exportExcel2003(OutputStream outputStream, String title,
                                           LinkedHashMap<String, String> fieldMap, Collection<T> dataCollection) {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet;
        if (StringUtils.isEmpty(title)) {
            sheet = workbook.createSheet();
        } else {
            sheet = workbook.createSheet(title);
        }
        // 表头格式
        CellStyle headerStyle = getHeaderStyle(workbook);
        // 值格式
        CellStyle valueStyle = getValueStyle(workbook);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(20);
        // 将数据填入Excel表格
        fillExcelData(headerStyle, valueStyle, sheet, fieldMap, dataCollection);
        // 将Excel内容写入流
        try {
            workbook.write(outputStream);
        } catch (IOException e) {
            LOG.error("Excel2003写入表格内容失败", e);
        }
    }

    /**
     * 导出Excel 2007 版本以上 最大支持1048576行
     *
     * @param outputStream   输出流
     * @param title          表格标题
     * @param fieldMap       <标题,属性>的顺序映射
     * @param dataCollection 数据集合
     * @param <T>            泛型
     */
    public static <T> void exportExcel2007(OutputStream outputStream, String title,
                                           LinkedHashMap<String, String> fieldMap, Collection<T> dataCollection) {
        // 声明一个工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 生成一个表格
        XSSFSheet sheet;
        if (StringUtils.isEmpty(title)) {
            sheet = workbook.createSheet();
        } else {
            sheet = workbook.createSheet(title);
        }
        // 表头格式
        CellStyle headerStyle = getHeaderStyle(workbook);
        // 值格式
        CellStyle valueStyle = getValueStyle(workbook);
        // 将数据填入Excel表格
        fillExcelData(headerStyle, valueStyle, sheet, fieldMap, dataCollection);
        // 将Excel内容写入流
        try {
            workbook.write(outputStream);
        } catch (IOException e) {
            LOG.error("Excel2007写入表格内容失败", e);
        }
    }


    /**
     * 将数据填入Excel表格
     *
     * @param sheet          表格
     * @param fieldMap       标题映射
     * @param dataCollection 数据集合
     * @param <T>            泛型
     */
    private static <T> void fillExcelData(CellStyle headerStyle, CellStyle valueStyle, Sheet sheet,
                                          LinkedHashMap<String, String> fieldMap, Collection<T> dataCollection) {
        // 产生表格标题行
        int rowNum = 0;
        Row row = sheet.createRow(rowNum);
        Cell cell;
        int headerColumn = 0;
        for (String key : fieldMap.keySet()) {
            cell = row.createCell(headerColumn);
            cell.setCellValue(key);
            cell.setCellStyle(headerStyle);
            headerColumn++;
        }
        // 遍历集合数据，产生数据行
        Field field;
        Object fieldValue;
        String value;
        try {
            if (ObjectUtil.isNotEmpty(dataCollection)) {
                for (T t : dataCollection) {
                    rowNum++;
                    row = sheet.createRow(rowNum);
                    int dataColumn = 0;
                    // 以LinkedHashMap为<标题,属性>的顺序映射直接获取数据行
                    for (String fieldObject : fieldMap.values()) {
                        cell = row.createCell(dataColumn);
                        field = t.getClass().getDeclaredField(fieldObject);
                        field.setAccessible(true);
                        fieldValue = field.get(t);
                        // 数据处理
                        value = dealFieldValue(fieldValue);
                        cell.setCellValue(value);
                        cell.setCellStyle(valueStyle);
                        dataColumn++;
                    }
                }
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            LOG.error("Excel数据映射错误", e);
        }
    }

    /**
     * 处理单元格内容值
     *
     * @param value 单元格内容对象
     * @return 处理过后的单元格内容(目前统一处理成字符串类型处理)
     */
    private static String dealFieldValue(Object value) {
        String textValue;
        if (ObjectUtil.isNull(value)) {
            return StringUtils.EMPTY;
        }
        if (value instanceof Boolean) {
            textValue = "是";
            if (!(Boolean) value) {
                textValue = "否";
            }
        } else if (value instanceof LocalDateTime) {
            textValue = DateTimeUtil.dataConverterStr((LocalDateTime) value, DateTimeUtil.DATE_YMDHMS_PATTERN);
        } else {
            textValue = String.valueOf(value);
        }
        return textValue;
    }

    /**
     * 获取Excel值样式
     *
     * @param workbook 工作台
     * @return 值样式
     */
    private static CellStyle getValueStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setWrapText(true);
        return cellStyle;
    }

    /**
     * 获取Excel头样式
     *
     * @param workbook 工作台
     * @return Excel头样式
     */
    private static CellStyle getHeaderStyle(Workbook workbook) {
        // 生成一个样式
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.PALE_BLUE.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 生成一个字体
        Font font = workbook.createFont();
        font.setColor(IndexedColors.BLACK.index);
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 指定当单元格内容显示不下时自动换行
        style.setWrapText(true);
        return style;
    }


    /**
     * 将普通表格写入workbook
     *
     * @param workbook       要写入的workbook
     * @param title          表格标题
     * @param fieldMap       <标题,属性>的顺序映射
     * @param dataCollection 数据集合
     * @param <T>            泛型
     */
    public static <T> void writeDataToExcelWorkbook(Workbook workbook, String title,
                                                    LinkedHashMap<String, String> fieldMap, Collection<T> dataCollection) {
        // 生成一个表格
        Sheet sheet;
        if (StringUtils.isEmpty(title)) {
            sheet = workbook.createSheet();
        } else {
            sheet = workbook.createSheet(title);
        }
        // 表头格式
        CellStyle headerStyle = getHeaderStyle(workbook);
        // 值格式
        CellStyle valueStyle = getValueStyle(workbook);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(20);
        // 将数据填入Excel表格
        fillExcelData(headerStyle, valueStyle, sheet, fieldMap, dataCollection);
    }

    /**
     * 写入多个workbook表格
     *
     * @param titleMap          表格标题(文件名->标题)
     * @param fileFieldMap      (文件名->标题映射<标题,属性>的顺序映射)
     * @param dataCollectionMap 数据集合(文件名->数据结合)
     */
    public static Map<String, Workbook> writeDataToExcelWorkbook(Map<String, String> titleMap,
                                                                 Map<String, LinkedHashMap<String, String>> fileFieldMap,
                                                                 Map<String, Collection> dataCollectionMap) {
        Map<String, Workbook> workbookMap = new LinkedHashMap<>();
        Workbook workbook;
        for (String fileName : dataCollectionMap.keySet()) {
            String title = titleMap.get(fileName);
            LinkedHashMap<String, String> fieldMap = fileFieldMap.get(fileName);
            Collection dataCollection = dataCollectionMap.get(fileName);
            if (ObjectUtil.isNotEmpty(dataCollection) && EXCEL_FILE_2003_DATA_MAX < dataCollection.size()) {
                workbook = new XSSFWorkbook();
                fileName += ".xlsx";
            } else {
                workbook = new HSSFWorkbook();
                fileName += ".xls";
            }
            Sheet sheet = StringUtils.isEmpty(title) ? workbook.createSheet() : workbook.createSheet(title);
            // 表头格式
            CellStyle headerStyle = getHeaderStyle(workbook);
            // 值格式
            CellStyle valueStyle = getValueStyle(workbook);
            // 设置表格默认列宽度为15个字节
            sheet.setDefaultColumnWidth(20);
            // 将数据填入Excel表格
            fillExcelData(headerStyle, valueStyle, sheet, fieldMap, dataCollection);
            workbookMap.put(fileName, workbook);
        }
        return workbookMap;
    }

}
