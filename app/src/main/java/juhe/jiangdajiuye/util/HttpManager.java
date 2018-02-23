package juhe.jiangdajiuye.util;


import android.app.Activity;
import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import juhe.jiangdajiuye.bean.MessageItem;
import juhe.jiangdajiuye.view.xuanJiang.control.XuanParseControl;
import juhe.jiangdajiuye.view.xuanJiang.entity.XuanJiangMesHolder;

/**
 * Created by wangqiang on 2016/9/29.
 * 网络请求类
 * <p>
 * 1、考虑到 解析网页需要耗时，所以直接将网页解析工作放在这里，直接返回MessageItem 对象
 * <p>
 * 2、在处于栈底的activity 初始化该对象
 */
public class HttpManager {
    private String TAG = "HttpHelper";
    private NetListener lister;
    private static ExecutorService service = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() + 1);
    private Context mCtx;
    private List<MessageItem> resultData = new ArrayList<>();
    private StringBuilder result = new StringBuilder();
    private HttpURLConnection connect = null;
    private int ResponseCode = 0;

    public HttpManager(Context mCtx) {
        //指定线程池数
        this.mCtx = mCtx;
    }

    public interface NetListener {
        void success(List<MessageItem> data, int code);

        void success(String data, int code);

        void failure(Exception e, String Error, int code);
    }

    public void setNetListener(NetListener lister) {
        this.lister = lister;
    }

    public void get(final String url) {
        service.execute(new Runnable() {
            @Override
            public void run() {
                realGet(url, null, -1);
            }
        });
    }

    public void get(final String url, final XuanJiangMesHolder holder) {
        service.execute(new Runnable() {
            @Override
            public void run() {
                realGet(url, holder, -1);
            }
        });
    }

    public void get(final String url, final int tab) {
        service.execute(new Runnable() {
            @Override
            public void run() {
                realGet(url, null, tab);
            }
        });
    }

    private void realGet(String url1, final XuanJiangMesHolder holder, final int tab) {
        result.setLength(0);
        BufferedReader reader = null;
        try {
            URL url = new URL(url1);
            connect = (HttpURLConnection) url.openConnection();
            connect.setConnectTimeout(10000);
            //设置请求属性
            connect.setRequestProperty("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            connect.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
            connect.setRequestMethod("GET");
            connect.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
            connect.setRequestProperty("Cache-Control", "max-age=0");
            connect.setRequestProperty("Connection", "keep-alive");
            connect.connect();
            Map<String, List<String>> map = connect.getHeaderFields();
            String strings[] = map.get("Content-Type").get(0).split(";");
            //如果响应头的编码格式是 GBK 就采用 GBK格式解码
            ResponseCode = connect.getResponseCode();
            if (containGBK(strings)) {
                reader = new BufferedReader(new InputStreamReader(connect.getInputStream(), "GBK"));
            } else {
                reader = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            }
            String line;
            while (null != reader && (line = reader.readLine()) != null) {
                result.append(line);
            }
            if (ResponseCode == 200) {
                resultData.clear();
                resultData.addAll(parseResponse(result.toString(), holder, tab));
                ((Activity) mCtx).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (null != holder || -1 != tab) {
                            lister.success(new ArrayList<>(resultData), ResponseCode);
                        } else {
                            lister.success(result.toString(), ResponseCode);
                        }
                    }
                });
            } else {
                ((Activity) mCtx).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lister.failure(null, result.toString(), ResponseCode);
                    }
                });

            }
        } catch (final Exception e) {
            ((Activity) mCtx).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lister.failure(e, result.toString(), ResponseCode);
                }
            });
        } finally {
            if (null != connect) {
                connect.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //是否应该采用GBK编码方式
    private boolean containGBK(String[] header) {
        for (String s : header) {
            if (s.equals("charset=GBK")) {
                return true;
            }
        }
        return false;
    }

    private List<MessageItem> parseResponse(String response, XuanJiangMesHolder holder, int tab) {
        if (null != holder) {
            //解析各个学校的宣讲会
            return XuanParseControl.getInStance().parse(response, holder);
        } else if (-1 != tab) {
            //解析江苏大学的信息
            return ParseUtils.getInstance().parseMainMes(response, tab);
        }
        return new ArrayList<>();
    }
}
