package juhe.jiangdajiuye.sql;

import android.content.Context;
import android.content.SharedPreferences;

import juhe.jiangdajiuye.core.BaseApplication;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2018-02-05
 */

public class SharePreferHelper {
    private final String SP_NAME = "config";
    private static final SharePreferHelper ourInstance = new SharePreferHelper();
    //key
    public static final String KEY_USER_NAME = "userName";

    private SharedPreferences sp ;
    public static SharePreferHelper getInstance() {
        return ourInstance;
    }

    private SharePreferHelper() {
        sp = BaseApplication.getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }
    public String getString(String key,String defValue){
        return sp.getString(key,defValue);
    }
    public void setString(String key,String value){
        sp.edit().putString(key,value).apply();
    }
    public boolean getBoolean(String key,boolean defValue){
        return sp.getBoolean(key,defValue);
    }
    public void setBoolean(String key,boolean value){
        sp.edit().putBoolean(key,value).apply();
    }
    public void clearData(){
        sp.edit().clear().apply();
    }
}
