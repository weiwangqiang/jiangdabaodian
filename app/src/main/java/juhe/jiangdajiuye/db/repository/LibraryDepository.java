package juhe.jiangdajiuye.db.repository;

import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.bean.BookBean;
import juhe.jiangdajiuye.base.BaseApplication;
import juhe.jiangdajiuye.db.helper.LibrarySqlHelper;

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
        collectSqlHelper = new LibrarySqlHelper(BaseApplication.getContext());
    }

    public boolean add(BookBean bookBean) {
        return collectSqlHelper.add(bookBean);
    }

    public boolean add(List<BookBean> data){
        return collectSqlHelper.add(data);
    }
    public ArrayList<BookBean> selectAll() {
        return collectSqlHelper.selectAll();
    }

    public boolean hasURL(String url) {
        return collectSqlHelper.hasURL(url);
    }

    public void delete(BookBean bookBean) {
        collectSqlHelper.delete(bookBean);
    }
}
