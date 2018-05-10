package juhe.jiangdajiuye.net.httpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2018-04-12
 */
public class CookieManager {
    private static CookieManager instance ;
    private Map<String ,List<String>> cookies = new HashMap<>();
    private CookieManager(){}
    public static CookieManager getInstance(){
        if(instance == null){
            instance = new CookieManager();
        }
        return instance ;
    }
    public void addCookie(String host,String cookie){
        if(!cookies.containsKey(host)){
            List<String> list = new ArrayList<>();
            list.add(cookie);
            cookies.put(host,list);
        }else if(cookies.get(host).contains(cookie)){
            cookies.get(host).add(cookie);
        }
    }
    public String getCookie(String host){
        if(!cookies.containsKey(host)){
            return "";
        }
        return cookies.get(host).get(0);
    }
}
