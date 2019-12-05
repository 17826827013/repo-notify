package cn.demo.repo.frame.excel;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

/**
 * @author daisy
 * @date 2018/10/18
 */
public interface ExportExcelService<T> {

    /**
     * 导出excel到指定文件
     *
     * @param file    目标文件
     * @param title   默认模板中的文档标题
     * @param header  列信息
     * @param data    数据
     */
    void exportExcelToFile(File file, String title, List<Column<T>> header, List<T> data, List<FooterCell> footer);

    /**
     * 导出excel到servlet
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param fileName 导出文件名
     * @param title    默认模板中的文档标题
     * @param header   列信息
     * @param data     数据
     */
    void exportExcelToServlet(HttpServletRequest request, HttpServletResponse response, String fileName, String title, List<Column<T>> header, List<T> data, List<FooterCell> footer);

    /**
     * 导出excel到servlet
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param fileName 导出文件名
     * @param title    默认模板中的文档标题
     * @param subTitle    默认模板中的文档副标题
     * @param header   列信息
     * @param data     数据
     */
    void exportExcelToServlet(HttpServletRequest request, HttpServletResponse response, String fileName, String title, String subTitle, List<Column<T>> header, List<T> data, List<FooterCell> footer);
}
