package juhe.jiangdajiuye.net.httpUtils.task;


import java.io.UnsupportedEncodingException;
import java.util.List;

import juhe.jiangdajiuye.bean.BookBean;
import juhe.jiangdajiuye.bean.MessageBean;
import juhe.jiangdajiuye.net.httpUtils.listener.MesItemHttpListener;
import juhe.jiangdajiuye.net.httpUtils.listener.StringHttpListener;
import juhe.jiangdajiuye.net.httpUtils.HttpService;
import juhe.jiangdajiuye.net.httpUtils.inter.IDataListener;
import juhe.jiangdajiuye.net.httpUtils.inter.IHttpService;
import juhe.jiangdajiuye.net.httpUtils.listener.BookHttpListener;
import juhe.jiangdajiuye.net.httpUtils.inter.IHttpListener;
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

    private <T> IHttpListener getHttpListener(Type type, IDataListener<T> iDataListener) {
        switch (type) {
            case data:
            case string:
                return new StringHttpListener((IDataListener<String>) iDataListener);
            case json:
            case MessageItem:
                return new MesItemHttpListener((IDataListener<List<MessageBean>>) iDataListener,mHolder);
            case book:
                return new BookHttpListener((IDataListener<List<BookBean>>) iDataListener);
            default:
                return null ;
        }
    }
}
