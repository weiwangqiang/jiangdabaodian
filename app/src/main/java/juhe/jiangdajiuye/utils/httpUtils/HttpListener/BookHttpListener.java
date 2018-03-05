package juhe.jiangdajiuye.utils.httpUtils.HttpListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import juhe.jiangdajiuye.bean.BookBean;
import juhe.jiangdajiuye.utils.ParseUtils;
import juhe.jiangdajiuye.utils.httpUtils.Inter.IDataListener;
import juhe.jiangdajiuye.utils.httpUtils.Inter.IHttpListener;
import juhe.jiangdajiuye.utils.httpUtils.ThreadHelper;

/**
 * class description here
 *  搜索书 结果的listener
 * @author wangqiang
 * @since 2018-02-25
 */

public class BookHttpListener implements IHttpListener {
    private IDataListener iDataListener ;
    private StringBuilder result = new StringBuilder();
    private ParseUtils parseTools = ParseUtils.getInstance();
    private List<BookBean> resData ;
    public BookHttpListener(IDataListener<List<BookBean>> iDataListener ){
        this.iDataListener = iDataListener ;
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
                    iDataListener.onFail(e, 0);
                }
            });
        }
        int totalNum = parseTools.parseSearchNumber(result.toString());
        resData = parseTools.parseSearch(result.toString(),totalNum);
        ThreadHelper.getInstance().runOnMainThread(new Runnable() {
            @Override
            public void run() {
                iDataListener.onSuccess(resData);
            }
        });
    }

    @Override
    public void onFail(final Exception exception, final int responseCode) {
        ThreadHelper.getInstance().runOnMainThread(new Runnable() {
            @Override
            public void run() {
                iDataListener.onFail(exception, responseCode);

            }
        });
    }
}
