package juhe.jiangdajiuye.utils.httpUtils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import juhe.jiangdajiuye.utils.httpUtils.Inter.IHttpListener;
import juhe.jiangdajiuye.utils.httpUtils.Inter.IHttpService;

/**
 * class description here
 * <p>
 * 执行具体网络请求的类
 * 并通过IHttpListener返回inputStream 给
 *
 * @author wangqiang
 * @since 2018-02-14
 */

public class HttpService implements IHttpService {
    private static final String TAG = "HttpService";
    private IHttpListener mIHttpListener;
    private HttpURLConnection mHttpURLConnection;

    private String mUrl;
    private byte[] mRequestParams;
    private BufferedReader mBufferedReader;
    private int mResponseCode;

    public HttpService(String url, byte[] mRequestParams, IHttpListener iHttpListener) {
        this.mUrl = url;
        this.mRequestParams = mRequestParams;
        this.mIHttpListener = iHttpListener;
    }

    /**
     * 执行请求
     */
    @Override
    public void execute() {

        if (null == mIHttpListener) {
            throw new NullPointerException("IHttpListener can not be null !");
        }
        Log.i(TAG, "execute: "+mUrl);
        try {
//            System.setProperty("http.proxyHost", "192.168.0.104");
//            System.setProperty("http.proxyPort", "8888");
            URL url = new URL(mUrl);
            mHttpURLConnection = (HttpURLConnection) url.openConnection();
            mHttpURLConnection.setConnectTimeout(10000);
            //设置请求属性
            mHttpURLConnection.setRequestProperty("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            mHttpURLConnection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
            mHttpURLConnection.setRequestMethod("GET");
            mHttpURLConnection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
            mHttpURLConnection.setRequestProperty("Cache-Control", "max-age=0");
            mHttpURLConnection.setRequestProperty("Connection", "keep-alive");
            mHttpURLConnection.connect();
            mResponseCode = mHttpURLConnection.getResponseCode();
            Map<String, List<String>> map = mHttpURLConnection.getHeaderFields();
            String strings[] = map.get("Content-Type").get(0).split(";");
            //如果响应头的编码格式是 GBK 就采用 GBK格式解码
            mResponseCode = mHttpURLConnection.getResponseCode();
            if (containGBK(strings)) {
                mBufferedReader = new BufferedReader(new InputStreamReader(mHttpURLConnection.getInputStream(), "GBK"));
            } else {
                mBufferedReader = new BufferedReader(new InputStreamReader(mHttpURLConnection.getInputStream()));
            }
            if (mResponseCode == 200) {
                mIHttpListener.onSuccess(mBufferedReader);
            } else {
                mIHttpListener.onFail(null, mResponseCode);
            }
        } catch (MalformedURLException e) {
            mIHttpListener.onFail(e, mResponseCode);
            e.printStackTrace();
        } catch (IOException e) {
            mIHttpListener.onFail(e, mResponseCode);
            e.printStackTrace();
        }catch (Exception e){
            mIHttpListener.onFail(e, mResponseCode);
            e.printStackTrace();
        }
        finally {
            if(null != mHttpURLConnection){
                mHttpURLConnection.disconnect();
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
}
