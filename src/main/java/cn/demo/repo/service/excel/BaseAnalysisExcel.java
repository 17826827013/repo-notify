package cn.demo.repo.service.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BaseAnalysisExcel {

    /**
     * 导入excel操作，通过继承这个接口，自定义实现将Cell组装成bean
     * @param list
     * @return
     */
    <T> T  warpCell(List<Cell> list);

}
