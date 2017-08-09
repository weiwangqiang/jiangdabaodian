package juhe.jiangdajiuye.util;


import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wangqiang on 2016/9/29.
 */
public class urlConnection   {
    private String TAG = "urlConnection";
    public NetListener lister;
    public ExecutorService service;
    public Context mCtx;
    public urlConnection(Context mCtx){
        //指定线程池数
        this.mCtx = mCtx;
        int size  = Runtime.getRuntime().availableProcessors() ;
        service = Executors.newFixedThreadPool (size +1);
    }

    public interface NetListener{
        void success(String response ,int code);
        void failure(Exception e,String Error,int code);
    }
    public void setNetListener(NetListener lister){
        this.lister = lister;
    }

    /**
     * 用线程池来访问网络数据
     * @param url
     */
    public void get(final String url) {

        service.execute(new Runnable() {
            @Override
            public void run() {
                realget(url);
            }
        });
    }
    String result = "";
    HttpURLConnection connect = null;
    int ResponseCode = 0;
    public  void realget(String url1){
        result = "";
        Log.i(TAG, "realget: "+url1);
        BufferedReader reader =  null;
        try{
            URL url = new URL(url1);
            connect = (HttpURLConnection)url.openConnection();
            connect.setConnectTimeout(5000);
            //设置请求属性
            connect.setRequestProperty("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            connect.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0");
            connect.setRequestProperty("Connection",
                    "keep-alive");
            connect.connect();
            reader = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            String line ;

            while ((line = reader.readLine()) !=null){
                result += line;
            }
            ResponseCode = connect.getResponseCode();
            if(ResponseCode==200){
                ((Activity)mCtx).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lister.success(result,ResponseCode);
                    }
                });
            }
            else{
                ((Activity)mCtx).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lister.failure(null,result, ResponseCode);
                    }
                });

            }
            connect.disconnect();
        }catch(final Exception e){
            ((Activity)mCtx).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lister.failure(e,result, ResponseCode);
                }
            });
        }
        finally {
            try{
                if(reader!=null){
                    reader.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
