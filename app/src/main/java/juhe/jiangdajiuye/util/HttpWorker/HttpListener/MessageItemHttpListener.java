package juhe.jiangdajiuye.util.HttpWorker.HttpListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import juhe.jiangdajiuye.bean.MessageItem;
import juhe.jiangdajiuye.core.BaseApplication;
import juhe.jiangdajiuye.util.HttpWorker.Inter.IDataListener;
import juhe.jiangdajiuye.util.HttpWorker.Inter.IHttpListener;
import juhe.jiangdajiuye.util.ParseUtils;
import juhe.jiangdajiuye.view.xuanJiang.control.XuanParseControl;
import juhe.jiangdajiuye.view.xuanJiang.entity.XuanJiangMesHolder;

/**
 * class description here
 *
 *  返回数据类型 ； List<MessageItem>
 *
 * @author wangqiang
 * @since 2018-02-14
 */

public class MessageItemHttpListener implements IHttpListener {
    private IDataListener mIDataListener;
    private XuanJiangMesHolder mHolder  ;
    public MessageItemHttpListener(IDataListener<List<MessageItem>> mIDataListener,XuanJiangMesHolder mHolder) {
        this.mIDataListener = mIDataListener;
        this.mHolder = mHolder ;
        if (null == mIDataListener) {
            throw new NullPointerException("IDataListener can not be null ");
        }
    }

    @Override
    public void onSuccess(BufferedReader bufferedReader) {
        final StringBuilder result = new StringBuilder();
        try {
            String line;
            while (null != bufferedReader && (line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
        } catch (final IOException e) {
            e.printStackTrace();
            BaseApplication.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    mIDataListener.onFail(e, 0);
                }
            });
        }
        BaseApplication.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                mIDataListener.onSuccess(parseResponse(result.toString(),mHolder,0));
            }
        });
    }

    private List<MessageItem> parseResponse(String response, XuanJiangMesHolder holder,int tab) {
        if (null != holder) {
            //解析各个学校的宣讲会
            return XuanParseControl.getInStance().parse(response, holder);
        }else{
            //解析江苏大学的信息
            return ParseUtils.getInstance().parseMainMes(response, tab);
        }
    }

    @Override
    public void onFail(Exception exception, int responseCode) {
            mIDataListener.onFail(exception,responseCode);
    }
}
