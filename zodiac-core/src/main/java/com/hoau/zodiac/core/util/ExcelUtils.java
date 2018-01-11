package com.hoau.zodiac.core.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hoau.zodiac.core.constant.ExcelConstants;
import com.hoau.zodiac.core.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhangchao
 * @version V1.0
 * @Title: ExcelUtils
 * @Package com.hoau.aquarius.util
 * @Description:
 * @date 2017/11/17 15:14
 */
public class ExcelUtils {
    static Logger logger = LoggerFactory.getLogger(com.hoau.zodiac.core.util.ExcelUtils.class);

    public static SimpleDateFormat dfSimple = new SimpleDateFormat(
            "yyyy-MM-dd");
    public static SimpleDateFormat df = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");


    /**
     * @param multipartFile 输入流
     * @param startRowNum   开始读取的行(0开始) 不能小于0
     * @author zhangchao
     * @date 2017/11/17 20:17
     * Description: excel文件转换为一个map.  map的key为行数，value 中的 key 为列数，value中的value 为值
     */
    @Deprecated
    public static Map<Integer, Map<Integer, String>> readToMapByFile(MultipartFile multipartFile, int startRowNum) {
        if (multipartFile.getSize() > ExcelConstants.MAX_IMPORT_UPLOAD) {
            throw new BusinessException("文件超过2M，请检查文件重新上传");
        }
        if (!multipartFile.getOriginalFilename().toLowerCase().endsWith(ExcelConstants.FILE_EXTENSION_XLSX)) {
            throw new BusinessException("excel文件格式不正确，必须是xlsx结尾的文件");
        }
        InputStream inputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Map<Integer, Map<Integer, String>> resultData = new HashMap<Integer, Map<Integer, String>>();
            for (int i = startRowNum; i < sheet.getLastRowNum() + 1; i++) {
                XSSFRow XSSFRow = sheet.getRow(i);
                Map<Integer, String> cellData = new HashMap<Integer, String>();
                String cellValue = "";
                if (XSSFRow == null) {//处理空行
                    if (i == startRowNum) {
                        return resultData;
                    }
                    resultData.put(i + 1, null);
                    continue;
                }
                for (int k = 0; k < XSSFRow.getLastCellNum(); k++) {
                    if (XSSFRow.getCell(k) == null) { //处理空列
                        cellData.put(k + 1, "");
                        continue;
                    }
                    switch (XSSFRow.getCell(k).getCellType()) {
                        case Cell.CELL_TYPE_NUMERIC:
                            if (HSSFDateUtil.isCellDateFormatted(XSSFRow.getCell(k))) {
                                //  如果是date类型则 ，获取该cell的date值
                                cellValue = df.format(XSSFRow.getCell(k).getDateCellValue());
                            } else { // 纯数字
                                cellValue = String.valueOf(XSSFRow.getCell(k).getNumericCellValue());
                            }
                            break;
                        case Cell.CELL_TYPE_STRING:
                            cellValue = XSSFRow.getCell(k).getStringCellValue();
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            cellValue = XSSFRow.getCell(k).getBooleanCellValue() + "";
                            break;
//                        case Cell.CELL_TYPE_BLANK:
//                            cellValue = "";
//                            break;
                        default:
                            cellValue = "";
                            break;
                    }
                    cellData.put(k + 1, cellValue);
                }
                resultData.put(i + 1, cellData);
            }
            return resultData;
        } catch (Exception e) {
            logger.error("excel文件解析失败!!!", e);
            throw new BusinessException("excel文件解析失败，请严格按照模板填写数据");
        } finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (IOException e) {
                logger.error("输入流关闭失败", e);
            }
        }
    }



    /**
     * @param excelFile 输入流
     * @param startRowNum   开始读取的行(0开始) 不能小于0
     * @param maxSize   限定一个最大文件SIZE
     * @author zhangchao
     * @date 2017/11/17 20:17
     * Description: excel文件转换为一个map.  map的key为行数，value 中的 key 为列数，value中的value 为值
     */
    public static Map<Integer, Map<Integer, String>> readToMapByFileObj(Object[] excelFile , int startRowNum,Long maxSize) {
        if(excelFile==null||excelFile.length<=0||excelFile[0]==null){
            throw new BusinessException("数据为空，请检查文件重新上传");
        }
        JSONObject jsonObj = JSON.parseObject(excelFile[0].toString());
        String fileName = jsonObj.getString("name");
        Long fileSize = jsonObj.getLong("size");
        String str = jsonObj.getString("url");
        String base64Data = StringUtils.substringAfterLast(str, "base64,");
        if (fileSize> maxSize) {
            throw new BusinessException("文件超过指定大小，请检查文件重新上传");
        }
        if (!fileName.endsWith(ExcelConstants.FILE_EXTENSION_XLSX)) {
            throw new BusinessException("excel文件格式不正确，必须是xlsx结尾的文件");
        }
        InputStream inputStream = null;
        try {
            byte[] data = Base64.decode(base64Data.getBytes());
            inputStream =new ByteArrayInputStream(data) ;
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Map<Integer, Map<Integer, String>> resultData = new HashMap<Integer, Map<Integer, String>>();
            for (int i = startRowNum; i < sheet.getLastRowNum() + 1; i++) {
                XSSFRow XSSFRow = sheet.getRow(i);
                Map<Integer, String> cellData = new HashMap<Integer, String>();
                String cellValue = "";
                if (XSSFRow == null) {//处理空行
                    if (i == startRowNum) {
                        return resultData;
                    }
                    resultData.put(i + 1, null);
                    continue;
                }
                for (int k = 0; k < XSSFRow.getLastCellNum(); k++) {
                    if (XSSFRow.getCell(k) == null) { //处理空列
                        cellData.put(k + 1, "");
                        continue;
                    }
                    switch (XSSFRow.getCell(k).getCellType()) {
                        case Cell.CELL_TYPE_NUMERIC:
                            if (HSSFDateUtil.isCellDateFormatted(XSSFRow.getCell(k))) {
                                //  如果是date类型则 ，获取该cell的date值
                                cellValue = df.format(XSSFRow.getCell(k).getDateCellValue());
                            } else { // 纯数字
                                cellValue = String.valueOf(XSSFRow.getCell(k).getNumericCellValue());
                            }
                            break;
                        case Cell.CELL_TYPE_STRING:
                            cellValue = XSSFRow.getCell(k).getStringCellValue();
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            cellValue = XSSFRow.getCell(k).getBooleanCellValue() + "";
                            break;
//                        case Cell.CELL_TYPE_BLANK:
//                            cellValue = "";
//                            break;
                        default:
                            cellValue = "";
                            break;
                    }
                    cellData.put(k + 1, cellValue);
                }
                resultData.put(i + 1, cellData);
            }
            return resultData;
        } catch (Exception e) {
            logger.error("excel文件解析失败!!!", e);
            throw new BusinessException("excel文件解析失败，请严格按照模板填写数据");
        } finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (IOException e) {
                logger.error("输入流关闭失败", e);
            }
        }
    }



    /**
     * @param maps           <String,String> maps
     * @param listData       <T> list 需要导出的数据列表对象
     * @param fileName       导出的完整文件名   支持 xlsx 和 xls
     * @param DateFormatType 0位不带时分秒 1为带时分秒  没有时间的导出忽略
     * @param fileName       指定输出文件名称，只能导出xlsx
     * @return
     */
    public static <T> void excelExport(Map<String, String> maps,
                                       List<T> listData, String fileName, Integer DateFormatType, HttpServletResponse response) {

//        if (listData.size() > ExcelConstants.MAX_EXPORT_SIZE) {
//            throw new BusinessException("数据单次最多只能导出" + ExcelConstants.MAX_EXPORT_SIZE + "条");
//        }
        if (StringUtils.isEmpty(fileName)) {
            throw new BusinessException("文件名不合法!!!");
        }
        fileName += ExcelConstants.FILE_EXTENSION_XLSX;
        Workbook wb = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        OutputStream toClient = null;
        try {
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            wb = new XSSFWorkbook();
            CreationHelper createHelper = wb.getCreationHelper();
            Sheet sheet = wb.createSheet("sheet1");
            Set<String> sets = maps.keySet();
            Row row = sheet.createRow(0);
            int i = 0;
            // 定义表头
            for (Iterator<String> it = sets.iterator(); it.hasNext(); ) {
                String key = it.next();
                Cell cell = row.createCell(i++);
                cell.setCellValue(createHelper.createRichTextString(maps
                        .get(key)));
            }
            float avg = listData.size() / 20f;
            int count = 1;
            int size = listData.size();
            for (int j = 0; j < size; j++) {
                T p = listData.get(j);
                Class classType = p.getClass();
                int index = 0;
                Row row1 = sheet.createRow(j + 1);
                for (Iterator<String> it = sets.iterator(); it.hasNext(); ) {
                    String key = it.next();
                    String firstLetter = key.substring(0, 1).toUpperCase();
                    // 获得和属性对应的getXXX()方法的名字
                    String getMethodName = "get" + firstLetter
                            + key.substring(1);
                    // 获得和属性对应的getXXX()方法
                    Method getMethod = classType.getMethod(getMethodName,
                            new Class[]{});
                    // 调用原对象的getXXX()方法
                    Object value = getMethod.invoke(p, new Object[]{});
                    if (value instanceof Date) {
                        if (DateFormatType == ExcelConstants.DATE_FORMAT_TYPE_ALL) {
                            value = df.format(value);
                        } else {
                            value = dfSimple.format(value);
                        }
                    }
                    Cell cell = row1.createCell(index++);
                    cell.setCellValue(value == null ? "" : value.toString());
                }
                if (j > avg * count) {
                    count++;
                    logger.info("I");
                }
                if (count == 20) {
                    logger.info("I100%");
                    count++;
                }
            }
            byteArrayOutputStream = new ByteArrayOutputStream();
            wb.write(byteArrayOutputStream);
            byteArrayOutputStream.flush();
            byte[] aa = byteArrayOutputStream.toByteArray();
            // 清空response
            response.reset();
            setResponse(response, fileName);
            toClient = new BufferedOutputStream(
                    response.getOutputStream());
            toClient.write(aa);
            toClient.flush();
        } catch (IOException e) {
            logger.error("IOException", e);
        } catch (IllegalAccessException e) {
            logger.error("IllegalAccessException", e);
        } catch (NoSuchMethodException e) {
            logger.error("NoSuchMethodException", e);
        } catch (InvocationTargetException e) {
            logger.error("InvocationTargetException", e);
        } catch (Exception e) {
            logger.error("Exception", e);
        } finally {
            try {
                byteArrayOutputStream.close();
                toClient.close();
            } catch (IOException e) {
                logger.error("输入流关闭失败", e);
            }
        }
    }

    /**
     * @param maps           <String,String> maps
     * @param listData       <T> list 需要导出的数据列表对象
     * @param file           excel文件 支持 xlsx 和 xls 文件
     * @param dateFormatType 0位不带时分秒 1为带时分秒  没有时间的导出忽略
     * @return
     */
    public static <T> void writeData(Map<String, String> maps, List<T> listData, File file, Integer dateFormatType, Integer startRow) {
        if (file == null) {
            throw new BusinessException("文件不能为空!!!");
        }
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            if (!file.exists()) {
                writeHeader(maps, file);
            }
            in = new FileInputStream(file);
            Workbook wb = new XSSFWorkbook(in);
            Sheet sheet = wb.getSheetAt(0);
            Set<String> sets = maps.keySet();
            if (startRow == null) {
                startRow = 1;
            }
            int size = listData.size();
            for (int j = 0; j < size; j++) {
                Row row = sheet.createRow(j + startRow);
                T p = listData.get(j);
                setRowData(sets, dateFormatType, row, p);
            }
            out = new FileOutputStream(file);
            wb.write(out);
            out.flush();
        } catch (Exception e) {
            logger.error("IOException", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                logger.error("输入流关闭失败", e);
            }
        }
    }

    private static <T> void setRowData(Set<String> sets, Integer dateFormatType, Row row, T p) {
        int index = 0;
        Class classType = p.getClass();
        for (Iterator<String> it = sets.iterator(); it.hasNext(); ) {
            try {
                String key = it.next();
                String firstLetter = key.substring(0, 1).toUpperCase();
                // 获得和属性对应的getXXX()方法的名字
                String getMethodName = "get" + firstLetter
                        + key.substring(1);
                // 获得和属性对应的getXXX()方法
                Method getMethod = classType.getMethod(getMethodName,
                        new Class[]{});
                // 调用原对象的getXXX()方法
                Object value = getMethod.invoke(p, new Object[]{});
                if (value instanceof Date) {
                    if (dateFormatType == ExcelConstants.DATE_FORMAT_TYPE_ALL) {
                        value = df.format(value);
                    } else {
                        value = dfSimple.format(value);
                    }
                }
                Cell cell = row.createCell(index++);
                cell.setCellValue(value == null ? "" : value.toString());
            } catch (Exception e) {
                logger.error("write excel error", e);
            }
        }
    }

    /**
     * 写excel头部
     * @param maps   <String,String> maps
     * @param file   excel文件
     * @return
     */
    private static Workbook writeHeader(Map<String, String> maps, File file) {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            Workbook wb = new XSSFWorkbook();
            CreationHelper createHelper = wb.getCreationHelper();
            Sheet sheet = wb.createSheet("sheet1");
            Set<String> sets = maps.keySet();
            Row row = sheet.createRow(0);
            int i = 0;
            // 定义表头
            for (Iterator<String> it = sets.iterator(); it.hasNext(); ) {
                String key = it.next();
                Cell cell = row.createCell(i++);
                cell.setCellValue(createHelper.createRichTextString(maps.get(key)));
            }
            wb.write(os);
            os.flush();
        } catch (Exception e) {
            logger.error("IOException", e);
        } finally {
            try {
                if (os != null) {
                   os.close();
                }
            } catch (IOException e) {
                logger.error("输入流关闭失败", e);
            }
        }
        return null;
    }

    private static void setResponse(HttpServletResponse response, String fileName) {
        // 设置response的Header
        response.addHeader("Content-Disposition", "attachment;filename="
                + new String(fileName.getBytes()));
        response.addHeader("Content-Length", "" + fileName.length());
        response.setContentType("application/vnd.ms-excel;charset=gb2312");
    }
}
