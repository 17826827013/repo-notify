package cn.demo.repo.frame.exception;


/**
 * @author weiq
 * @date 2018/10/11
 */
public class ExcelExportException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String message;

    public ExcelExportException(String message) {
        super();
        this.message = message;
    }

    public ExcelExportException(String message, Throwable cause) {
        super(cause);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
