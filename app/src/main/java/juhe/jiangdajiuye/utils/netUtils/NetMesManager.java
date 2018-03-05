package juhe.jiangdajiuye.utils.netUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.google.gson.Gson;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import juhe.jiangdajiuye.bean.bmobAppMes.IpBean;
import juhe.jiangdajiuye.utils.httpUtils.HttpHelper;
import juhe.jiangdajiuye.utils.httpUtils.task.HttpTask;
import juhe.jiangdajiuye.utils.httpUtils.Inter.IDataListener;
import juhe.jiangdajiuye.utils.userInforUtils.UserActionRecordUtils;

/**
 * class description here
 * 获取用户网络状态
 *
 * @author wangqiang
 * @since 2017-08-02
 */

public class NetMesManager {
    private static final String ipUrl = "http://ip.chinaz.com/getip.aspx";
    /**
     * 获取IP
     *
     * @param context
     * @return
     */
    public static String getIp(final Context context) {
        String ip = null;
        ConnectivityManager conMan = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // mobile 3G Data Network
        android.net.NetworkInfo.State mobile = conMan.getNetworkInfo(
                ConnectivityManager.TYPE_MOBILE).getState();
        // wifi
        android.net.NetworkInfo.State wifi = conMan.getNetworkInfo(
                ConnectivityManager.TYPE_WIFI).getState();

        // 如果3G网络和wifi网络都未连接，且不是处于正在连接状态 则进入Network Setting界面 由用户配置网络连接
        if (mobile == android.net.NetworkInfo.State.CONNECTED
                || mobile == android.net.NetworkInfo.State.CONNECTING) {
            ip = getLocalIpAddress();
        }
        if (wifi == android.net.NetworkInfo.State.CONNECTED
                || wifi == android.net.NetworkInfo.State.CONNECTING) {
            //获取wifi服务
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            //判断wifi是否开启
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            ip = (ipAddress & 0xFF) + "." +
                    ((ipAddress >> 8) & 0xFF) + "." +
                    ((ipAddress >> 16) & 0xFF) + "." +
                    (ipAddress >> 24 & 0xFF);
        }
        return ip;

    }

    /**
     * @return 手机GPRS网络的IP
     */
    private static String getLocalIpAddress() {
        try {
            //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {//获取IPv4的IP地址
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }


        return null;
    }

    public static void setIP() {
        HttpHelper httpHelper = HttpHelper.getInstance() ;
        httpHelper.get(ipUrl, null, new IDataListener<String>() {
            @Override
            public void onSuccess(String response) {
                Gson gson = new Gson();
                try {
                    IpBean ipbean = gson.fromJson(response, IpBean.class);
                    UserActionRecordUtils.setIpBean(ipbean);
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
