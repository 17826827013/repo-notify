package cn.demo.repo.frame.excel;


import cn.demo.repo.frame.exception.ExcelExportException;
import cn.demo.repo.util.ParamUtil;
import cn.demo.repo.util.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.apache.poi.ss.usermodel.CellStyle.ALIGN_CENTER;

/**
 * @author daisy
 * @date 2018/10/09
 */
@Component
public class ExportExcelServiceImpl<T> implements ExportExcelService<T> {
    private static final Logger logger = LoggerFactory.getLogger(ExportExcelServiceImpl.class);

    @Value("${excel.max.rows:2}")
    private int maxRows;

    /**
     * 自动列宽设定
     *
     * @param listSize
     */
    private void autoResizeColumns(int listSize, SXSSFWorkbook wb) {
        int count = wb.getNumberOfSheets();
        for (int i = 0; i < count; i++) {
            Sheet sh = wb.getSheetAt(i);
            for (int j = 0; j < listSize; j++) {
                sh.autoSizeColumn(j);
            }
        }
    }

    /**
     * 表格列头样式
     *
     * @return
     */
    private CellStyle getTableHeaderStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setAlignment(ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        Font font = wb.createFont();
        font.setFontName("宋体");
        style.setFont(font);
        return style;
    }

    /**
     * 文档标题样式，字体默认宋体粗体，居中显示
     *
     * @return
     */
    private CellStyle getHeaderTitleStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 16);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(font);
        return style;
    }

    /**
     * 正文cell样式，默认无任何样式
     *
     * @return
     */
    private CellStyle getNormalStyle(Workbook wb) {
        return wb.createCellStyle();
    }

    private SXSSFWorkbook exportExcel(String title, List<Column<T>> header, List<T> data, List<FooterCell> footer) throws NoSuchFieldException, IllegalAccessException {
        SXSSFWorkbook wb = fillHeader(title, header, data.size());
        autoResizeColumns(header.size(), wb);
        genexcel(header, data, footer, wb);
        return wb;
    }

    private SXSSFWorkbook exportExcel(String title, String subTitle, List<Column<T>> header, List<T> data, List<FooterCell> footer) throws NoSuchFieldException, IllegalAccessException {
        SXSSFWorkbook wb = fillHeader(title, subTitle, header, data.size());
        autoResizeColumns(header.size(), wb);
        genexcel(header, data, footer, wb);
        return wb;
    }

    private void genexcel(List<Column<T>> header, List<T> data, List<FooterCell> footer, SXSSFWorkbook wb) throws NoSuchFieldException, IllegalAccessException {
        if (!CollectionUtils.isEmpty(data)) {
            int count = wb.getNumberOfSheets();
            for (int i = 0; i < count; i++) {
                Sheet sh = wb.getSheetAt(i);
                List<T> subData = data.subList(i * maxRows, (i + 1) * maxRows > data.size() ? data.size() : (i + 1) * maxRows);
                int footerRowNum = fillData(header, subData, sh);
                if (!CollectionUtils.isEmpty(footer)) {
                    fillFooter(footer, footerRowNum, sh);
                }
            }
        }
    }

    protected SXSSFWorkbook fillHeader(String title, String subTitle, List<Column<T>> header, int size) {
        return null;
    }

    /**
     * 填充footer
     *
     * @param footerData
     * @param sh
     */
    private void fillFooter(List<FooterCell> footerData, int rowNum, Sheet sh) {
        Row titleRow = sh.createRow(rowNum);
        Cell cell;
        for (FooterCell fd : footerData) {
            cell = titleRow.createCell(fd.getColumnIdx());
            String cellCol = CellReference.convertNumToColString(fd.getColumnIdx());
            String calRange = cellCol.concat("4").concat(":").concat(cellCol).concat(String.valueOf(rowNum));
            cell.setCellStyle(getNormalStyle(sh.getWorkbook()));
            if (!StringUtils.isEmpty(fd.getValue())) {
                cell.setCellValue(fd.getValue());
            }else {
                switch (fd.getFormula()) {
                    case SUM:
                        cell.setCellFormula("SUM(".concat(calRange).concat(")"));
                        break;
                    case COUNT:
                        cell.setCellFormula("COUNT(".concat(calRange).concat(")"));
                        break;
                    default:
                        cell.setCellValue("");
                        break;
                }
            }
        }
    }

    @Override
    public void exportExcelToFile(File file, String title, List<Column<T>> header, List<T> data, List<FooterCell> footer) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            SXSSFWorkbook wb = exportExcel(title, header, data, footer);
            wb.write(fos);
            fos.flush();
        } catch (FileNotFoundException e) {
            logger.error("Excel导出文件.", e);
            throw new ExcelExportException("Excel导出文件失败.", e);
        } catch (IOException | IllegalAccessException | NoSuchFieldException e) {
            logger.error("Excel导出失败.", e);
            throw new ExcelExportException("Excel导出文件失败.", e);
        }
    }

    @Override
    public void exportExcelToServlet(HttpServletRequest request, HttpServletResponse response, String fileName, String title, List<Column<T>> header, List<T> data, List<FooterCell> footer) {
        SXSSFWorkbook wb = null;
        try {
            wb = exportExcel(title, header, data, footer);
            response.setContentType("application/octet-stream;charset=UTF-8");
            String outPutName = fileName.concat("_").concat(cn.demo.repo.util.DateUtil.detailCalendarFormatter(new Date())).concat(".xlsx");
            response.setHeader("Content-disposition", "attachment; filename=" + ParamUtil.fileNameEncoder(outPutName, request));
            wb.write(response.getOutputStream());
        } catch (IOException | IllegalAccessException | NoSuchFieldException e) {
            logger.error("Excel文件下载失败.", e);
            throw new ExcelExportException("Excel文件下载失败.", e);
        }
    }

    @Override
    public void exportExcelToServlet(HttpServletRequest request, HttpServletResponse response, String fileName, String title, String subTitle, List<Column<T>> header, List<T> data, List<FooterCell> footer) {
        try {
            SXSSFWorkbook wb = exportExcel(title, subTitle, header, data, footer);
            response.setContentType("application/octet-stream;charset=UTF-8");
            String outPutName = fileName.concat("_").concat(cn.demo.repo.util.DateUtil.detailCalendarFormatter(new Date())).concat(".xlsx");
            response.setHeader("Content-disposition", "attachment; filename=" + ParamUtil.fileNameEncoder(outPutName, request));
            wb.write(response.getOutputStream());
        } catch (IOException | IllegalAccessException | NoSuchFieldException e) {
            logger.error("Excel文件下载失败.", e);
            throw new ExcelExportException("Excel文件下载失败.", e);
        }
    }

    /**
     * 填充文档头
     *
     * @param title   文档标题
     * @param columns 表头列信息
     */
    protected SXSSFWorkbook fillHeader(String title, List<Column<T>> columns, int dataSize) {
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        int maxSheets = dataSize % maxRows == 0 ? dataSize/maxRows : dataSize/maxRows + 1;
        maxSheets = maxSheets == 0 ? 1 : maxSheets;
        Sheet sh;
        Row titleRow;
        Cell titleCell, cell;
        Row tabHeader;
        for (int i = 1; i <= maxSheets; i++) {
            sh = wb.createSheet();

            titleRow = sh.createRow(0);
            titleCell = titleRow.createCell(0);
            titleRow.setHeight((short) 630);
            titleCell.setCellValue(title);
            titleCell.setCellStyle(getHeaderTitleStyle(wb));
            sh.addMergedRegion(new CellRangeAddress(0, 0, 0, columns.size() - 1));

            sh.createRow(1);

            tabHeader = sh.createRow(2);
            tabHeader.setHeight((short) 465);
            for (int cellNum = 0; cellNum < columns.size(); cellNum++) {
                cell = tabHeader.createCell(cellNum);
                cell.setCellValue(columns.get(cellNum).getTitle());
                cell.setCellStyle(getTableHeaderStyle(wb));
            }
            sh.createFreezePane(0, 3);
        }
        return wb;
    }

    /**
     * 填充数据部分
     *
     * @param columns 列信息
     * @param data    数据
     * @param sh
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    protected int fillData(List<Column<T>> columns, List<T> data, Sheet sh) throws NoSuchFieldException, IllegalAccessException {
        CellStyle normalStyle = getNormalStyle(sh.getWorkbook());
        int rowNum = 3;
        Row row;
        Cell cell;
        for (T rec : data) {
            row = sh.createRow(rowNum);
            for (int i = 0; i < columns.size(); i++) {
                cell = row.createCell(i, Cell.CELL_TYPE_STRING);
                cell.setCellStyle(normalStyle);
                Column<T> column = columns.get(i);
                if (column.getFormatter() != null) {
                    Object value = column.getFormatter().formatter(rec);
                    if (value instanceof Number) {
                        cell.setCellValue(((Number) value).doubleValue());
                    }else {
                        cell.setCellValue((String) value);
                    }
                } else {
                    if (StringUtils.isNotEmpty(column.getField())) {
                        boolean exists = false;
                        Field field = null;
                        Field[] fields = rec.getClass().getDeclaredFields();
                        for (Field f : fields) {
                            if (f.getName().equals(column.getField())) {
                                field = f;
                                exists = true;
                                break;
                            }
                        }
                        if (!exists) {
                            Field[] superFields = rec.getClass().getSuperclass().getDeclaredFields();
                            for (Field sf : superFields) {
                                if (sf.getName().equals(column.getField())) {
                                    field = sf;
                                    break;
                                }
                            }
                        }
                        if (field != null){
                            field.setAccessible(true);
                            Object value = field.get(rec);
                            if (value instanceof BigDecimal) {
                                cell.setCellValue(((Number) value).doubleValue());
                            }else {
                                cell.setCellValue(null == value ? "" : String.valueOf(value));
                            }
                        }
                    }else {
                        cell.setCellValue("");
                    }
                }
            }
            rowNum++;
        }
        return rowNum;
    }
}
