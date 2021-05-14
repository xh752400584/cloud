package com.xh.cloud.util;

import com.lowagie.text.pdf.BaseFont;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Copyright 2021
 * Tools for export issue
 *
 * @author XiangHui
 * @date 2021/4/22 23:23:03
 */
public class ExportUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ExportUtil.class);

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
        ExcelUtil.exportExcelFile(response, fileName, sheetName, fieldMap, dataCollection);
    }

    /**
     * 导出多表格至压缩文件
     *
     * @param response    response导出到浏览器
     * @param workbookMap 表格文件Map(文件名(带后缀)->文件簿>
     * @param zipFileName 压缩文件名
     */
    public static void exportWorkbookFilesToZip(@NotNull HttpServletResponse response, LinkedHashMap<String, Workbook> workbookMap, String zipFileName) {
        try (ByteArrayOutputStream dataOutputStream = new ByteArrayOutputStream();
             OutputStream outputStream = response.getOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
            response.reset();
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(zipFileName, "UTF-8"));
            response.setContentType("application/octet-stream");
            for (String fileName : workbookMap.keySet()) {
                dataOutputStream.reset();
                Workbook workbook = workbookMap.get(fileName);
                workbook.write(dataOutputStream);
                byte[] bytes = dataOutputStream.toByteArray();
                ZipEntry entry = new ZipEntry(fileName);
                zipOutputStream.putNextEntry(entry);
                zipOutputStream.write(bytes);
                zipOutputStream.closeEntry();
            }
        } catch (Exception e) {
            LOG.error("导出表格文件失败", e);
        }
    }

    /**
     * 导出PDF文件
     *
     * @param response    response导出到浏览器
     * @param pdfContent  PDF文件html内容
     * @param pdfFileName PDF文件名
     */
    public static void exportPdfFile(HttpServletResponse response, String pdfContent, String pdfFileName) {
        PdfUtil.exportPdfFile(response, pdfContent, pdfFileName);
    }

    /**
     * 导出多PDF文件至压缩文件
     *
     * @param response      response导出到浏览器
     * @param pdfContentMap PDF文件Map(文件名(带后缀)->html内容>
     * @param zipFileName   压缩文件名
     */
    public static void exportPdfFilesToZip(HttpServletResponse response, LinkedHashMap<String, String> pdfContentMap, String zipFileName) {
        try (ByteArrayOutputStream dataOutputStream = new ByteArrayOutputStream();
             OutputStream outputStream = response.getOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
            response.reset();
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(zipFileName, "UTF-8"));
            response.setContentType("application/octet-stream");
            String pdfFontPath = PdfUtil.getPdfFontPath();
            for (String fileName : pdfContentMap.keySet()) {
                dataOutputStream.reset();
                // 将pdf写入
                ITextRenderer renderer = new ITextRenderer();
                ITextFontResolver fontResolver = renderer.getFontResolver();
                //指定字体。为了支持中文字体
                fontResolver.addFont(pdfFontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                renderer.setDocumentFromString(pdfContentMap.get(fileName));
                renderer.layout();
                renderer.createPDF(dataOutputStream);
                renderer.finishPDF();
                byte[] bytes = dataOutputStream.toByteArray();
                ZipEntry entry = new ZipEntry(fileName);
                zipOutputStream.putNextEntry(entry);
                zipOutputStream.write(bytes);
                zipOutputStream.closeEntry();
            }
        } catch (Exception e) {
            LOG.error("导出PDF文件失败", e);
        }
    }

    /**
     * 导出多表格及PDF文件至压缩文件
     *
     * @param response      response导出到浏览器
     * @param workbookMap   表格文件Map(文件名(带后缀)->文件簿>
     * @param pdfContentMap PDF文件Map(文件名(带后缀)->html内容>
     * @param zipFileName   压缩文件名
     */
    public static void exportFilesToZip(HttpServletResponse response, LinkedHashMap<String, Workbook> workbookMap,
                                        LinkedHashMap<String, String> pdfContentMap, String zipFileName) {
        try (ByteArrayOutputStream dataOutputStream = new ByteArrayOutputStream();
             OutputStream outputStream = response.getOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
            response.reset();
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(zipFileName, "UTF-8"));
            response.setContentType("application/octet-stream");
            String pdfFontPath = PdfUtil.getPdfFontPath();
            byte[] bytes;
            ZipEntry entry;
            // excel表格写入
            for (String fileName : workbookMap.keySet()) {
                dataOutputStream.reset();
                Workbook workbook = workbookMap.get(fileName);
                workbook.write(dataOutputStream);
                bytes = dataOutputStream.toByteArray();
                entry = new ZipEntry(fileName);
                zipOutputStream.putNextEntry(entry);
                zipOutputStream.write(bytes);
                zipOutputStream.closeEntry();
            }
            // pdf文件写入
            for (String fileName : pdfContentMap.keySet()) {
                dataOutputStream.reset();
                ITextRenderer renderer = new ITextRenderer();
                ITextFontResolver fontResolver = renderer.getFontResolver();
                fontResolver.addFont(pdfFontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                renderer.setDocumentFromString(pdfContentMap.get(fileName));
                renderer.layout();
                renderer.createPDF(dataOutputStream);
                renderer.finishPDF();
                bytes = dataOutputStream.toByteArray();
                entry = new ZipEntry(fileName);
                zipOutputStream.putNextEntry(entry);
                zipOutputStream.write(bytes);
                zipOutputStream.closeEntry();
            }
        } catch (Exception e) {
            LOG.error("导出压缩文件失败", e);
        }
    }

}
