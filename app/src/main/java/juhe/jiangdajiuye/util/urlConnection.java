package juhe.jiangdajiuye.util;

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
    public getLister lister;
    public ExecutorService service;
    public urlConnection(){
        //指定线程池数
        service = Executors.newFixedThreadPool (4);
    }

    public interface getLister{
        void success(String response ,int code);
        void failure(Exception e,String Error,int code);
    }
    public void setgetLister(getLister lister){
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
    public  void realget(String url1){
        String result = "";
        BufferedReader reader =  null;
        try{
            URL url = new URL(url1);
            HttpURLConnection connect = (HttpURLConnection)url.openConnection();
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
            if(connect.getResponseCode()==200){
                lister.success(result,connect.getResponseCode());
            }
            else{
                lister.failure(null,result,connect.getResponseCode());
            }
            connect.disconnect();
        }catch(Exception e){
            lister.failure(e,result,0);
            e.printStackTrace();
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
