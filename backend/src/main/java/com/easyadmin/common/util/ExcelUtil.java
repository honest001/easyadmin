package com.easyadmin.common.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * Excel工具类
 */
public class ExcelUtil {
    /**
     * 导出Excel
     * @param title 标题
     * @param headers 表头
     * @param dataList 数据列表
     * @param outputStream 输出流
     * @throws IOException IO异常
     */
    public static void exportExcel(String title, String[] headers, List<Map<String, Object>> dataList, OutputStream outputStream) throws IOException {
        // 创建工作簿
        Workbook workbook = new XSSFWorkbook();
        // 创建工作表
        Sheet sheet = workbook.createSheet(title);
        // 设置列宽
        for (int i = 0; i < headers.length; i++) {
            sheet.setColumnWidth(i, 20 * 256);
        }
        
        // 创建标题行
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(title);
        titleCell.setCellStyle(getCellStyle(workbook, true));
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, headers.length - 1));
        
        // 创建表头行
        Row headerRow = sheet.createRow(1);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(getCellStyle(workbook, false));
        }
        
        // 填充数据
        if (dataList != null && !dataList.isEmpty()) {
            for (int i = 0; i < dataList.size(); i++) {
                Row dataRow = sheet.createRow(i + 2);
                Map<String, Object> dataMap = dataList.get(i);
                for (int j = 0; j < headers.length; j++) {
                    Cell cell = dataRow.createCell(j);
                    Object value = dataMap.get(headers[j]);
                    if (value != null) {
                        cell.setCellValue(value.toString());
                    }
                }
            }
        }
        
        // 写入输出流
        workbook.write(outputStream);
        workbook.close();
    }
    
    /**
     * 获取单元格样式
     * @param workbook 工作簿
     * @param isTitle 是否为标题
     * @return 单元格样式
     */
    private static CellStyle getCellStyle(Workbook workbook, boolean isTitle) {
        CellStyle style = workbook.createCellStyle();
        // 设置边框
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        // 设置对齐方式
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置字体
        Font font = workbook.createFont();
        if (isTitle) {
            font.setFontName("微软雅黑");
            font.setFontHeightInPoints((short) 14);
            font.setBold(true);
        } else {
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 12);
        }
        style.setFont(font);
        return style;
    }
}