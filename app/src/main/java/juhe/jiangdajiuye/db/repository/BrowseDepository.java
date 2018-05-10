package juhe.jiangdajiuye.db.repository;

import java.util.List;

import juhe.jiangdajiuye.base.BaseApplication;
import juhe.jiangdajiuye.db.helper.BrowseHelper;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2018-04-12
 */
public class BrowseDepository implements IDepository<String> {
    private BrowseHelper browseHelper ;
    private static BrowseDepository instance;
    private BrowseDepository(){
        browseHelper = new BrowseHelper(BaseApplication.getContext());
    }
    public static BrowseDepository getInstance(){
        if(instance == null){
            instance = new BrowseDepository();
        }
        return instance ;
    }
    @Override
    public void add(String s) {
        browseHelper.add(s);
    }

    @Override
    public void add(List<String> data) {
    }

    @Override
    public void delete(String s) {
        browseHelper.delete(s);
    }

    @Override
    public boolean contain(String s) {
        return browseHelper.contain(s);
    }

    @Override
    public List<String> selectAll() {
        return null;
    }
}
