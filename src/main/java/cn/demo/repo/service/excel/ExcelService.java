package cn.demo.repo.service.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    //将file转换成Workbook
    public Workbook fileTransToWorkbook(MultipartFile file){

        Workbook wb = null;
        try {
            //把上传的文件转换成文件流
            FileInputStream fis = (FileInputStream) file.getInputStream();
            String fileName = file.getOriginalFilename();
            String[] split = fileName.split("\\.");
            //校验格式

            if ("xls".equals(split[split.length-1])) {
                wb = new HSSFWorkbook(fis);
            } else if ("xlsx".equals(split[split.length-1])) {
                wb = new XSSFWorkbook(fis);
            } else {
                return wb;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return wb;

    }

    //解析wb
    public <T> List<T> analysiscWorkbook(Workbook wb , BaseAnalysisExcel analysisExcel){
        List<T> list = new ArrayList<>();
        int sheets = wb.getNumberOfSheets();
        //遍历每个sheet
        for (int i = 0;i<sheets;i++){
            Sheet sheet = wb.getSheetAt(i);
            //总行数
            int rows = sheet.getLastRowNum();
            //总列数
            int cols = sheet.getRow(0).getLastCellNum();
            List<Cell> cells;
            //遍历行,从第二行开始
            for (int j = 1;j<=rows;j++){
                Row row = sheet.getRow(j);
                if(row != null){
                    //遍历列
                    cells = new ArrayList<>();
                    for (int k = 0 ;k<cols;k++) {
                        Cell cell = row.getCell(k);
                        cells.add(cell);
                    }
                    T t = analysisExcel.warpCell(cells);
                    if(t != null){
                        list.add(t);
                    }
                }
            }
        }
        return list;
    }




}
