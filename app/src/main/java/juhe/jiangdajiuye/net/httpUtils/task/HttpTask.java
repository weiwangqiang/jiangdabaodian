package juhe.jiangdajiuye.utils.httpUtils.task;


import java.io.UnsupportedEncodingException;

import juhe.jiangdajiuye.utils.httpUtils.HttpListener.BookHttpListener;
import juhe.jiangdajiuye.utils.httpUtils.HttpListener.MessageItemHttpListener;
import juhe.jiangdajiuye.utils.httpUtils.HttpListener.StringHttpListener;
import juhe.jiangdajiuye.utils.httpUtils.HttpService;
import juhe.jiangdajiuye.utils.httpUtils.Inter.IDataListener;
import juhe.jiangdajiuye.utils.httpUtils.Inter.IHttpListener;
import juhe.jiangdajiuye.utils.httpUtils.Inter.IHttpService;
import juhe.jiangdajiuye.view.activity.xuanJiang.entity.MesItemHolder;

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
    private MesItemHolder mHolder ;
    public enum Type {
        string, json, data,MessageItem,book
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


    public <T> HttpTask(String url, MesItemHolder holder , IDataListener<T> iDataListener, Type type) {
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
            case book:
                return new BookHttpListener(iDataListener);
            default:
                return null ;
        }
    }
}
