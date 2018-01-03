package juhe.jiangdajiuye.sql.repository;

import java.util.ArrayList;

import juhe.jiangdajiuye.bean.BookBean;
import juhe.jiangdajiuye.core.MyApplication;
import juhe.jiangdajiuye.sql.LibrarySqlHelper;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2017-12-31
 */

public class LibraryRepository {
    public static LibraryRepository libraryRepository;
    private LibrarySqlHelper collectSqlHelper;

    public static LibraryRepository getInstance() {
        if (null == libraryRepository)
            libraryRepository = new LibraryRepository();
        return libraryRepository;
    }

    private LibraryRepository() {
        collectSqlHelper = new LibrarySqlHelper(MyApplication.getContext());
    }

    public long addCollect(BookBean bookBean) {
        return collectSqlHelper.addCollect(bookBean);
    }

    public ArrayList<BookBean> selectAll() {
        return collectSqlHelper.selectAll();
    }

    public boolean hasURL(String url) {
        return collectSqlHelper.hasURL(url);
    }

    public void delete(String url) {
        collectSqlHelper.delete(url);
    }
}
