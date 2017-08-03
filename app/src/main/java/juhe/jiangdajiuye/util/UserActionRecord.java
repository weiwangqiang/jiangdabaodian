package juhe.jiangdajiuye.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import juhe.jiangdajiuye.entity.IPBean;
import juhe.jiangdajiuye.entity.visitCount;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2017-08-02
 */

public class UserActionRecord {
    public static long comeTime ;

    public static void setIpbean(IPBean ipbean) {
        UserActionRecord.ipbean = ipbean;
    }

    public static IPBean ipbean ;
    public static void setComeTime(long comeTime) {
        UserActionRecord.comeTime = comeTime;
    }

    public static void setOutTime(long outTime) {
        UserActionRecord.outTime = outTime;
        String stayTime = getStayTime();
        visitCount count = new visitCount();
        if(ipbean == null){
          count.setIp(NetMesManager.getIp(MyApplication.getApplication()));
        }
        else{
            count.setIp(ipbean.getIp());
            count.setAddress(ipbean.getAddress());
        }
        Date dt=new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置显示
        count.setVisitTime(df.format(dt));
        count.setStayTime(stayTime);
        UserActionPush.pushVisitCount(count);
    }

    public static long outTime ;
    public static String  getStayTime(){
        long time = outTime - comeTime;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置显示
        return (df.format(new Date(time)));
    }
}
