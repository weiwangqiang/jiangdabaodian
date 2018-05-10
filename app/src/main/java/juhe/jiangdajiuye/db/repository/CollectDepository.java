package juhe.jiangdajiuye.db.repository;

import java.util.List;

import juhe.jiangdajiuye.bean.MessageBean;
import juhe.jiangdajiuye.base.BaseApplication;
import juhe.jiangdajiuye.db.helper.CollectSqlHelper;

/**
 * class description here
 * 用来管理helper对象的
 *
 * @author wangqiang
 * @since 2018-01-06
 */

public class CollectDepository implements IDepository<MessageBean> {
    public static CollectDepository collectDepository;
    private CollectSqlHelper collectSqlHelper;

    public static CollectDepository getInstance() {
        if (null == collectDepository) {
            collectDepository = new CollectDepository();
        }
        return collectDepository;
    }

    private CollectDepository() {
        collectSqlHelper = new CollectSqlHelper(BaseApplication.getContext());
    }
    @Override
    public void add(MessageBean messageBean) {
        collectSqlHelper.add(messageBean);
    }
    @Override

    public void add(List<MessageBean> data) {
        collectSqlHelper.add(data);
    }
    @Override
    public List<MessageBean> selectAll() {
        return collectSqlHelper.selectAll();
    }

    @Override
    public boolean contain(MessageBean messageBean) {
        return collectSqlHelper.contain(messageBean);
    }
    @Override
    public void delete(MessageBean messageBean) {
        collectSqlHelper.delete(messageBean);
    }
}
