package juhe.jiangdajiuye.util.HttpWorker;


import java.io.UnsupportedEncodingException;

import juhe.jiangdajiuye.util.HttpWorker.HttpListener.MessageItemHttpListener;
import juhe.jiangdajiuye.util.HttpWorker.HttpListener.StringHttpListener;
import juhe.jiangdajiuye.util.HttpWorker.Inter.IDataListener;
import juhe.jiangdajiuye.util.HttpWorker.Inter.IHttpListener;
import juhe.jiangdajiuye.util.HttpWorker.Inter.IHttpService;
import juhe.jiangdajiuye.view.xuanJiang.entity.XuanJiangMesHolder;

/**
 * class description here
 *
 *  任务的包装类
 *
 * @author wangqiang
 * @since 2018-02-14
 */

public class HttpTask implements Runnable {

    private IHttpService iHttpService;
    private IDataListener mIDataListener;
    private byte[] mRequestData;
    private XuanJiangMesHolder mHolder ;
    public enum Type {
        string, json, data,MessageItem
    }

    public <T> HttpTask(String url, T requestBean, IDataListener<T> iDataListener, Type type) {
        this.mIDataListener = iDataListener;
        if (null != requestBean) {
            String requestString = requestBean.toString();
            try {
                mRequestData = requestString.getBytes("utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        iHttpService = new HttpService(url, mRequestData, getHttpListener(type, mIDataListener));
    }


    public <T> HttpTask(String url, XuanJiangMesHolder holder , IDataListener<T> iDataListener, Type type) {
        this.mIDataListener = iDataListener;
        this.mHolder = holder ;
        iHttpService = new HttpService(url, mRequestData, getHttpListener(type, mIDataListener));
    }
    @Override
    public void run() {
        iHttpService.execute();
    }

    private IHttpListener getHttpListener(Type type, IDataListener iDataListener) {
        switch (type) {
            case data:
            case string:
                return new StringHttpListener((IDataListener<String>) iDataListener);
            case json:
            case MessageItem:
                return new MessageItemHttpListener(iDataListener,mHolder);
            default:
                return null ;
        }
    }
}
