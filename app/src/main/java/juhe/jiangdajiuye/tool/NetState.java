package juhe.jiangdajiuye.tool;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**网络接口
 * Created by wangqiang on 2016/7/14.
 */
public class NetState extends BroadcastReceiver {
    private String TAG = "NetState" ;
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
            if (activeNetwork.isConnected()) {
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    setmState(TYPE_WIFI);
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    setmState(TYPE_MOBILE);
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
        Log.i(TAG, "setmState: listener size is "+listers.size() );
        switch (type){
            case TYPE_ERROR:
                for(NetLister netLister : listers){
                    netLister.OutInternet();
                }
                break;
            case TYPE_MOBILE:
                for(NetLister netLister : listers){
                    netLister.GetInternet(TYPE_MOBILE);
                }
                break;
            case TYPE_WIFI:
                for(NetLister netLister : listers){
                    netLister.GetInternet(TYPE_WIFI);
                }
                break;
        }
    }

}
