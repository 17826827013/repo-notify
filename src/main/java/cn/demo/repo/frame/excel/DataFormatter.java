package cn.demo.repo.frame.excel;

/**
 * @author daisy
 * @date 2018/10/18
 */
public interface DataFormatter<T> {
    Object formatter(T t);
}
