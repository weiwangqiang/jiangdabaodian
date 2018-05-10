package juhe.jiangdajiuye.db.repository;

import java.util.List;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2018-04-12
 */
public interface IRespository<T> {
    void add(T t);
    void add(List<T> data);
    void delete(T t);
    boolean contain(T t);
    T selectAll();
}
