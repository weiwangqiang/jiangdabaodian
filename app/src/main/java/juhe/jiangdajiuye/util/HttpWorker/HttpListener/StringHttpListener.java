package juhe.jiangdajiuye.util.HttpWorker.HttpListener;

import java.io.BufferedReader;
import java.io.IOException;

import juhe.jiangdajiuye.core.BaseApplication;
import juhe.jiangdajiuye.util.HttpWorker.Inter.IDataListener;
import juhe.jiangdajiuye.util.HttpWorker.Inter.IHttpListener;

/**
 * class description here
 *
 *  返回数据类型是string
 *
 * @author wangqiang
 * @since 2018-02-14
 */

public class StringHttpListener implements IHttpListener {
    private IDataListener mIDataListener;

    public StringHttpListener(IDataListener<String> mIDataListener) {
        this.mIDataListener = mIDataListener;
        if (null == mIDataListener) {
            throw new NullPointerException("IDataListener can not be null ");
        }
    }

    @Override
    public void onSuccess(BufferedReader bufferedReader) {
        final StringBuilder result = new StringBuilder();
        String line;
        try {
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
                mIDataListener.onSuccess(result.toString());
            }
        });
    }

    @Override
    public void onFail(Exception exception, int responseCode) {
        mIDataListener.onFail(exception, responseCode);

    }
}
