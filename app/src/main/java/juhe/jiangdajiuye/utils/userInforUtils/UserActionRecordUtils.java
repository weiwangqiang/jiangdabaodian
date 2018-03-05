package juhe.jiangdajiuye.utils.userInforUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import juhe.jiangdajiuye.bean.bmobAppMes.IpBean;
import juhe.jiangdajiuye.bean.bmobRecordEntity.VisitCount;
import juhe.jiangdajiuye.core.BaseApplication;
import juhe.jiangdajiuye.utils.netUtils.NetMesManager;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2017-08-02
 */

public class UserActionRecordUtils {
    public static long comeTime;
    public static long outTime;
    public static Map<String, Integer> mRecordMap = new HashMap<>();
    public static String TAG = "UserActionRecordUtils";

    public static void setIpBean(IpBean ipbean) {
        UserActionRecordUtils.ipbean = ipbean;
    }

    public static IpBean getIpBean() {
        return ipbean;
    }

    public static IpBean ipbean;

    public static void setComeTime(long comeTime) {
        UserActionRecordUtils.comeTime = comeTime;
    }

    /**
     * 记录用户离开时间
     *   并提交数据到bmob
     * @param outTime
     */
    public static void setOutTime(long outTime) {
        UserActionRecordUtils.outTime = outTime;
        long time = outTime - comeTime;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置显示
        String stayTime = df.format(new Date(time));
        VisitCount count = new VisitCount();
        if (ipbean == null) {
            count.setIp(NetMesManager.getIp(BaseApplication.getApplication()));
            count.setAddress("unKnow");
        } else {
            count.setIp(ipbean.getIp());
            count.setAddress(ipbean.getAddress());
        }
        Date dt = new Date();
        count.setVisitTime(df.format(dt));
        count.setStayTime(stayTime);
        count.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
            }
        });
    }
}
