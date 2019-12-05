package cn.demo.repo.frame;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * DataGrid
 * 返回列表
 * @auther dingzl
 * @create 2018-05-01 17:04
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class DataGrid<T> {

    private Long total = 0L;

    private List<T> rows;

    private String message;

    public DataGrid() {
    }

    public DataGrid(List<T> rows, Long total) {
        this.rows = rows;
        this.total = total;
    }

    public List<T> getRows() {
        return rows == null ? new ArrayList<T>() : rows;
    }
}
