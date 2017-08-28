package juhe.jiangdajiuye.broadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.util.NetWork.NetStateUtils;

/**网络接口
 * Created by wangqiang on 2016/7/14.
 */
public class NetStateReceiver extends BroadcastReceiver {
    private String TAG = "NetStateReceiver" ;
    public static final int TYPE_WIFI = 0x1;
    public static final int TYPE_MOBILE = 0x2;
    public static final int TYPE_ERROR = 0x3;
    public static int mState = TYPE_ERROR;

    public static List<NetLister> listers = new ArrayList<>();
    public interface NetLister{
        void OutInternet();
        void GetInternet(int type);
    }
    public static void addNetLister(NetLister myNetLister){
        if(!listers.contains(myNetLister))
          listers.add(myNetLister);
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager manager = (ConnectivityManager)context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.isConnected() || activeNetwork.isAvailable()) {
                switch (activeNetwork.getType()){
                    case ConnectivityManager.TYPE_WIFI :
                        setmState(TYPE_WIFI);
                        break;
                    case ConnectivityManager.TYPE_MOBILE:
                        setmState(TYPE_MOBILE);
                        break;
                }
            } else {
                setmState(TYPE_ERROR);
            }
        }else{
            setmState(TYPE_ERROR);
        }
    }

    private void setmState(int type) {
        if(mState == type) return;
        mState = type ;
        switch (type){
            case TYPE_ERROR:
                NetStateUtils.setNetWorkState(NetStateUtils.TYPE_ERROR);
                for(NetLister netLister : listers){
                    netLister.OutInternet();
                }
                break;
            case TYPE_MOBILE:
                NetStateUtils.setNetWorkState(NetStateUtils.TYPE_MOBILE);
                for(NetLister netLister : listers){
                    netLister.GetInternet(TYPE_MOBILE);
                }
                break;
            case TYPE_WIFI:
                NetStateUtils.setNetWorkState(NetStateUtils.TYPE_WIFI);
                for(NetLister netLister : listers){
                    netLister.GetInternet(TYPE_WIFI);
                }
                break;
        }
    }

}
