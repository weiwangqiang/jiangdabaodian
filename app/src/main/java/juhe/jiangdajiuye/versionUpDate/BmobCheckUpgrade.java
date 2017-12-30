package juhe.jiangdajiuye.versionUpDate;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import juhe.jiangdajiuye.bean.bmobBean.AppVersionBean;

/**
 * class description here
 * 检查更新
 * @author wangqiang
 * @since 2017-12-30
 */

public class BmobCheckUpDate {
    //检查更新
    public static void checkUpDate(){
        BmobQuery<AppVersionBean> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<AppVersionBean>() {
            @Override
            public void done(List<AppVersionBean> object, BmobException e) {
                if(e!=null){
                   return ;
                }
                for (AppVersionBean appVersionBean : object) {

                }
            }
        });
    }
    public void showDialog(){

    }
    public void downLoad(AppVersionBean appVersionBean){
        BmobFile bmobFile = new BmobFile(new File(appVersionBean.getDownLoadUrl()));
        if(bmobFile!= null){
            //调用bmobfile.download方法
            bmobFile.download();
        }
    }
}
