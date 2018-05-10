package juhe.jiangdajiuye.utils;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import juhe.jiangdajiuye.bean.bmobAppMes.IpBean;
import juhe.jiangdajiuye.bean.bmobRecordEntity.VisitCount;
import juhe.jiangdajiuye.net.httpUtils.HttpHelper;
import juhe.jiangdajiuye.net.httpUtils.Inter.IDataListener;
import juhe.jiangdajiuye.net.httpUtils.task.HttpTask;

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
    private static final String ipUrl = "http://ip.chinaz.com/getip.aspx";
    private static boolean hasPushMes = false ;//是否将用户位置上传

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
     */
    public static void pushMes() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置显示
        VisitCount count = new VisitCount();
        if (ipbean == null) {
            return;
        }
        count.setIp(ipbean.getIp());
        count.setAddress(ipbean.getAddress());
        count.setVisitTime(df.format(comeTime));
        count.setVersionName(AppConfigUtils.getVersionName());
        count.setStayTime(df.format(System.currentTimeMillis() - comeTime));
//        count.save()
        count.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e != null){
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取用户位置
     */
    public static void setIP() {
        if (getIpBean() != null) {
            return;
        }
        HttpHelper httpHelper = HttpHelper.getInstance() ;
        httpHelper.get(ipUrl, null, new IDataListener<String>() {
            @Override
            public void onSuccess(String response) {
                try {
                    String[] params = response.split("\'");
                    IpBean ipbean = new IpBean();
                    if(params.length >= 4){
                        ipbean.setAddress(params[3]);
                        ipbean.setIp(params[1]);
                    }
                    setIpBean(ipbean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(Exception exception, int responseCode) {

            }
        }, HttpTask.Type.string);
    }
}
