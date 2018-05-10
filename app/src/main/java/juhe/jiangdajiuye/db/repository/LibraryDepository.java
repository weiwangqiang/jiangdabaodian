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

public class LibraryDepository implements IDepository<BookBean>{
    public static LibraryDepository libraryDepository;
    private LibrarySqlHelper collectSqlHelper;

    public static LibraryDepository getInstance() {
        if (null == libraryDepository)
            libraryDepository = new LibraryDepository();
        return libraryDepository;
    }

    private LibraryDepository() {
        collectSqlHelper = new LibrarySqlHelper(BaseApplication.getContext());
    }
    @Override
    public void add(BookBean bookBean) {
        collectSqlHelper.add(bookBean);
    }
    @Override
    public void add(List<BookBean> data){
         collectSqlHelper.add(data);
    }
    @Override
    public ArrayList<BookBean> selectAll() {
        return collectSqlHelper.selectAll();
    }

    @Override
    public void delete(BookBean bookBean) {
        collectSqlHelper.delete(bookBean);
    }

    @Override
    public boolean contain(BookBean bookBean) {
        return collectSqlHelper.hasURL(bookBean.getUrl());
    }
}
