package juhe.jiangdajiuye.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2017-08-16
 */

public class NetStateUtils {
    private String TAG = "NetStateUtils";
    public static final int TYPE_WIFI = 0x1;
    public static final int TYPE_MOBILE = 0x2;
    public static final int TYPE_ERROR = 0x3;

    public static int getNetWorkState() {
        return mState;
    }
    //判断是否处于WiFi状态
    public static boolean isWifiState(){
        return mState == TYPE_WIFI ;
    }
    public static int mState = TYPE_ERROR;
    public static void setNetWorkState(int state){
        switch (state){
            case TYPE_WIFI:
                mState = TYPE_WIFI;
                break;
            case TYPE_MOBILE:
                mState = TYPE_MOBILE;
                break;
            default:
                mState = TYPE_ERROR ;
        }
    }
    //初始化网络状态参数
    public static void initNetWorkState(Context context){
        ConnectivityManager manager = (ConnectivityManager)context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.isConnected()) {
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    mState = TYPE_WIFI;
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    mState = TYPE_MOBILE;
                }
            } else {
                mState = TYPE_ERROR;
            }
        }else{
            mState = TYPE_ERROR;
        }

    }
}
