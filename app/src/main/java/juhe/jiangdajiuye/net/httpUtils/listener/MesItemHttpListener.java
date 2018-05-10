package juhe.jiangdajiuye.net.httpUtils.listener;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.bean.MessageBean;
import juhe.jiangdajiuye.utils.JiangDaParseUtils;
import juhe.jiangdajiuye.net.httpUtils.inter.IDataListener;
import juhe.jiangdajiuye.net.httpUtils.inter.IHttpListener;
import juhe.jiangdajiuye.net.httpUtils.ThreadHelper;
import juhe.jiangdajiuye.view.activity.JobFair.control.JobParseControl;
import juhe.jiangdajiuye.view.activity.xuanJiang.control.XuanParseControl;
import juhe.jiangdajiuye.view.activity.xuanJiang.entity.MesItemHolder;

/**
 * class description here
 * <p>
 * 返回数据类型 ； List<MessageBean>
 * <p>
 * 当前线程处于异步线程，可以执行耗时操作
 *
 * @author wangqiang
 * @since 2018-02-14
 */

public class MesItemHttpListener implements IHttpListener {
    private static final String TAG = "MesItemHttpListener";
    private IDataListener mIDataListener;
    private MesItemHolder mHolder;
    private StringBuilder result = new StringBuilder();
    private List<MessageBean> resData;

    public MesItemHttpListener(IDataListener<List<MessageBean>> mIDataListener, MesItemHolder mHolder) {
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
    private List<MessageBean> parseResponse(String response, MesItemHolder holder) {
        if (null == holder) {
            return new ArrayList<>();
        }
        if (holder.getTab() != -1) {
            //解析江苏大学的信息
            return JiangDaParseUtils.getInstance().parseMainMes(response, holder.getTab());
        }
        //解析各个学校的宣讲会、招聘会
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
