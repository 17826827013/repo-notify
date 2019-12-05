package cn.demo.repo.util;

import org.apache.poi.ss.usermodel.Cell;

/**
 * @ Date  : Create in 10:39 2019/5/29
 */
public class ExcelUtil {

    public static String getValue(Cell cell) {
        int type = cell.getCellType();
        String value = null;
        switch (type) {
            case 0:
                //数字
                value = Double.toString(cell.getNumericCellValue());
                break;
            case 1:
                //字符串
                value = cell.getStringCellValue();
                break;
            case 2:
                //公式
                value = cell.getCellFormula();
                break;
            case 3:
                //blank空格
                break;
            case 4:
                //布尔
                value = Boolean.toString(cell.getBooleanCellValue());
                break;
            case 5:
                break;
            default:
                break;
        }
        return value;
    }
}
