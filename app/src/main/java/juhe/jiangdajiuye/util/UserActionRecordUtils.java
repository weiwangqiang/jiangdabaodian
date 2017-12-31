package juhe.jiangdajiuye.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import juhe.jiangdajiuye.core.MyApplication;
import juhe.jiangdajiuye.bean.IpBean;
import juhe.jiangdajiuye.bean.recordEntity.VisitCount;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2017-08-02
 */

public class UserActionRecordUtils {
    public static long comeTime ;
    public static long outTime ;
    public static Map<String,Integer> mRecordMap = new HashMap<>();
    public static String TAG = "UserActionRecordUtils";
    public static void setIpbean(IpBean ipbean) {
        UserActionRecordUtils.ipbean = ipbean;
    }

    public static IpBean getIpbean() {
        return ipbean;
    }

    public static IpBean ipbean ;
    public static void setComeTime(long comeTime) {
        UserActionRecordUtils.comeTime = comeTime;
    }

    /**
     * 记录用户离开时间
     * @param outTime
     */
    public static void setOutTime(long outTime) {
        UserActionRecordUtils.outTime = outTime;
        long time = outTime - comeTime;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置显示
        String stayTime = df.format(new Date(time));
        VisitCount count = new VisitCount();
        if(ipbean == null){
            Log.i(TAG, "setOutTime: ipbean is null ");
            count.setIp(NetMesManager.getIp(MyApplication.getApplication()));
            count.setAddress("unKnow");
        }
        else{
            Log.i(TAG, "setOutTime: ipbean not  null ");

            count.setIp(ipbean.getIp());
            count.setAddress(ipbean.getAddress());
        }
        Date dt=new Date();
        count.setVisitTime(df.format(dt));
        count.setStayTime(stayTime);
        UserActionPushUtils.pushVisitCount(count);
    }

}
