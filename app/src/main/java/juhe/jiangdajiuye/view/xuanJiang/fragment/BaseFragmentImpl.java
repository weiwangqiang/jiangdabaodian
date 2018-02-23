package juhe.jiangdajiuye.view.xuanJiang.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import juhe.jiangdajiuye.view.xuanJiang.control.XuanUrlControl;
import juhe.jiangdajiuye.view.xuanJiang.entity.XuanJiangMesHolder;


/**
 * class description here
 * <p>
 * 具体实现 宣讲的fragment
 *
 * @author wangqiang
 * @since 2017-09-30
 */

public class BaseFragmentImpl extends BaseFragment {
    private XuanUrlControl xuanUrlControl = XuanUrlControl.getInStance();
    private int page = 1;

    public static Fragment newInstance(String BaseUrl, String college, int collegeId, int provinceId) {
        BaseFragmentImpl f = new BaseFragmentImpl();
        Bundle b = new Bundle();
        b.putString("BaseUrl", BaseUrl);
        b.putString("college", college);
        b.putInt("collegeId", collegeId);
        b.putInt("provinceId", provinceId);
        f.setArguments(b);
        return f;
    }

    @Override
    public String getUrl(boolean isPull, XuanJiangMesHolder holder) {
        if (isPull)
            page = 1;
        holder.setPager(page);
        holder.setPull(isPull);
        return xuanUrlControl.getUrl(holder);
    }

    @Override
    public void RequestSuccess() {
        page++;
    }
}
