package cn.demo.repo.frame.excel;

/**
 * @author daisy
 * @date 2018/10/23
 */
public class FooterCell {
    private int columnIdx;
    private String value;
    private ExcelFormula formula;

    public FooterCell(int columnIdx, String value, ExcelFormula formula) {
        this.columnIdx = columnIdx;
        this.value = value;
        this.formula = formula;
    }

    public int getColumnIdx() {
        return columnIdx;
    }

    public void setColumnIdx(int columnIdx) {
        this.columnIdx = columnIdx;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ExcelFormula getFormula() {
        return formula;
    }

    public void setFormula(ExcelFormula formula) {
        this.formula = formula;
    }
}
