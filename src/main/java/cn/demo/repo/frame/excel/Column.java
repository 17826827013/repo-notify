package cn.demo.repo.frame.excel;

/**
 * @author daisy
 * @date 2018/10/18
 */
public class Column<T> {
    private String field;
    private String title;
    private DataFormatter<T> formatter;

    public Column(String field, String title, DataFormatter<T> formatter) {
        this.field = field;
        this.title = title;
        this.formatter = formatter;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DataFormatter<T> getFormatter() {
        return formatter;
    }

    public void setFormatter(DataFormatter<T> formatter) {
        this.formatter = formatter;
    }
}
