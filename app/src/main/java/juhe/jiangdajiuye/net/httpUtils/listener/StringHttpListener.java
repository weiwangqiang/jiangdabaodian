package juhe.jiangdajiuye.net.httpUtils.listener;

import java.io.BufferedReader;
import java.io.IOException;

import juhe.jiangdajiuye.net.httpUtils.inter.IDataListener;
import juhe.jiangdajiuye.net.httpUtils.inter.IHttpListener;
import juhe.jiangdajiuye.net.httpUtils.ThreadHelper;

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
    private  StringBuilder result = new StringBuilder();
    public StringHttpListener(IDataListener<String> mIDataListener) {
        this.mIDataListener = mIDataListener;
        if (null == mIDataListener) {
            throw new NullPointerException("IDataListener can not be null ");
        }
    }

    @Override
    public void onSuccess(BufferedReader bufferedReader) {
        result.setLength(0);
        String line;
        try {
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
        ThreadHelper.getInstance().runOnMainThread(new Runnable() {
            @Override
            public void run() {
                mIDataListener.onSuccess(result.toString());
            }
        });
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
