package com.xh.cloud.util;

import cn.hutool.core.util.ObjectUtil;
import com.lowagie.text.pdf.BaseFont;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

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
public class PdfUtil {

    private static final Logger LOG = LoggerFactory.getLogger(PdfUtil.class);


    /**
     * 导出PDF文件
     *
     * @param response    response导出到浏览器
     * @param pdfContent  PDF文件html内容
     * @param pdfFileName PDF文件名
     */
    public static void exportPdfFile(HttpServletResponse response, String pdfContent, String pdfFileName) {
        try (OutputStream outputStream = response.getOutputStream()) {
            response.reset();
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(pdfFileName, "UTF-8"));
            response.setContentType("application/octet-stream");
            ITextRenderer renderer = new ITextRenderer();
            ITextFontResolver fontResolver = renderer.getFontResolver();
            //指定字体。为了支持中文字体
            String pdfFontPath = getPdfFontPath();
            fontResolver.addFont(pdfFontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            renderer.setDocumentFromString(pdfContent);
            renderer.layout();
            renderer.createPDF(outputStream);
            renderer.finishPDF();
        } catch (Exception e) {
            LOG.error("导出PDF文件失败", e);
        }
    }


    /**
     * 获取支持pdf文件中文字体的地址
     *
     * @return 中文字体的地址
     */
    public static String getPdfFontPath() {
        // 指定字体，为了支持中文字体
        if ("linux".equals(System.getProperty("os.name").toLowerCase())) {
            return "/usr/share/fonts/chiness/simsun.ttc";
        } else {
            return "c:/Windows/Fonts/simsun.ttc";
        }
    }



}
