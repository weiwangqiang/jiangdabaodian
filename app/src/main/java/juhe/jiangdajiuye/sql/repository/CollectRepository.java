package juhe.jiangdajiuye.sql.repository;

import java.util.List;

import juhe.jiangdajiuye.bean.MessageItem;
import juhe.jiangdajiuye.core.MyApplication;
import juhe.jiangdajiuye.sql.CollectSqlHelper;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2018-01-06
 */

public class CollectRepository{
    public static CollectRepository collectRepository ;
    private CollectSqlHelper collectSqlHelper ;
    public static CollectRepository getInstance(){
        if(null == collectRepository){
            collectRepository = new CollectRepository();
        }
        return collectRepository ;
    }
    private CollectRepository(){
        collectSqlHelper = new CollectSqlHelper(MyApplication.getContext());
    }
    public void add(MessageItem messageItem){
        collectSqlHelper.add(messageItem);
    }
    public void add(List<MessageItem> data){
        collectSqlHelper.add(data);
    }
    public List<MessageItem> selectAll() {
        return collectSqlHelper.selectAll() ;
    }

    public boolean contain(MessageItem messageItem) {
        return  collectSqlHelper.contain(messageItem);
    }

    public void delete(MessageItem messageItem) {
        collectSqlHelper.delete(messageItem);
    }
}
