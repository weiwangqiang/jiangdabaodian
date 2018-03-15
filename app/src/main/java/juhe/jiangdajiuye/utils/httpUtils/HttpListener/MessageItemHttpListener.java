package juhe.jiangdajiuye.utils.httpUtils.HttpListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.bean.MessageItem;
import juhe.jiangdajiuye.utils.ParseUtils;
import juhe.jiangdajiuye.utils.httpUtils.Inter.IDataListener;
import juhe.jiangdajiuye.utils.httpUtils.Inter.IHttpListener;
import juhe.jiangdajiuye.utils.httpUtils.ThreadHelper;
import juhe.jiangdajiuye.view.JobFair.control.JobParseControl;
import juhe.jiangdajiuye.view.xuanJiang.control.XuanParseControl;
import juhe.jiangdajiuye.view.xuanJiang.entity.MesItemHolder;

/**
 * class description here
 * <p>
 * 返回数据类型 ； List<MessageItem>
 * <p>
 * 当前线程处于异步线程，可以执行耗时操作
 *
 * @author wangqiang
 * @since 2018-02-14
 */

public class MessageItemHttpListener implements IHttpListener {
    private static final String TAG = "MessageItemHttpListener";
    private IDataListener mIDataListener;
    private MesItemHolder mHolder;
    private StringBuilder result = new StringBuilder();
    private List<MessageItem> resData;

    public MessageItemHttpListener(IDataListener<List<MessageItem>> mIDataListener, MesItemHolder mHolder) {
        this.mIDataListener = mIDataListener;
        this.mHolder = mHolder;
        if (null == mIDataListener) {
            throw new NullPointerException("IDataListener can not be null ");
        }
    }

    @Override
    public void onSuccess(BufferedReader bufferedReader) {
        result.setLength(0);
        try {
            String line;
            while (null != bufferedReader && (line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
        } catch (final IOException e) {
            e.printStackTrace();
            ThreadHelper.getInstance().runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    mIDataListener.onFail(e, 0);
                }
            });
        }
        resData = parseResponse(result.toString(), mHolder);
        ThreadHelper.getInstance().runOnMainThread(new Runnable() {
            @Override
            public void run() {
                mIDataListener.onSuccess(resData);
            }
        });
    }

    /**
     * 解析信息
     *
     * @param response 服务器返回的结果
     * @param holder   控制解析的holder
     * @return 解析结果
     */
    private List<MessageItem> parseResponse(String response, MesItemHolder holder) {
        if (null == holder) {
            return new ArrayList<>();
        }
        if (holder.getTab() != -1) {
            //解析江苏大学的信息
            return ParseUtils.getInstance().parseMainMes(response, holder.getTab());
        }
        //解析各个学校的宣讲会
        switch (holder.getMessKind()) {
            case MesItemHolder.mes_xuan_jiang:
                return XuanParseControl.getInStance().parse(response, holder);
            case MesItemHolder.mes_job_fair:
                return JobParseControl.getInStance().parse(response, holder);
            default:
                return new ArrayList<>();
        }

    }

    @Override
    public void onFail(final Exception exception, final int responseCode) {
        ThreadHelper.getInstance().runOnMainThread(new Runnable() {
            @Override
            public void run() {
                mIDataListener.onFail(exception, responseCode);
            }
        });
    }
}
