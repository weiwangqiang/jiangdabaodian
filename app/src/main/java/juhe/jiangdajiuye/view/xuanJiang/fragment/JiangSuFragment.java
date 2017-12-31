package juhe.jiangdajiuye.view.xuanJiang.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.List;

import juhe.jiangdajiuye.bean.MessageItem;
import juhe.jiangdajiuye.view.xuanJiang.entity.XuanMesBean;
import juhe.jiangdajiuye.view.xuanJiang.control.XuanParseControl;
import juhe.jiangdajiuye.view.xuanJiang.control.XuanUrlControl;


/**
 * class description here
 *
 *  江苏省宣讲的fragment
 *
 * @author wangqiang
 * @since 2017-09-30
 */

public class JiangSuFragment extends BaseFragment {
    XuanUrlControl xuanUrlControl = XuanUrlControl.getInStance();
    public static Fragment newInstance(String BaseUrl, String college, int collegeId,int provinceId) {
        JiangSuFragment f = new JiangSuFragment();
        Bundle b = new Bundle();
        b.putString("BaseUrl",BaseUrl);
        b.putString("college",college);
        b.putInt("collegeId",collegeId);
        b.putInt("provinceId",provinceId);
        f.setArguments(b);
        return f;
    }
    int page =1 ;
    @Override
    public String getUrl(boolean isPull, XuanMesBean holder) {
        if(isPull)
            page = 1 ;
        holder.setPager(page);
        holder.setPull(isPull);
       return xuanUrlControl.getUrl(holder);
    }


    @Override
    public List<MessageItem> parseMes(String result,XuanMesBean holder) {
        return XuanParseControl.getInStance().parse(result,holder);
    }

    @Override
    public void RequestSuccess() {
        page++;
    }
}
