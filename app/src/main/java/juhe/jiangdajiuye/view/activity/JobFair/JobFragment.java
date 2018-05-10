package juhe.jiangdajiuye.view.activity.JobFair;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import juhe.jiangdajiuye.view.activity.JobFair.control.JobUrlControl;
import juhe.jiangdajiuye.view.activity.xuanJiang.entity.MesItemHolder;
import juhe.jiangdajiuye.view.activity.xuanJiang.fragment.BaseFragment;


/**
 * class description here
 * <p>
 * 具体实现 招聘会的fragment
 *
 * @author wangqiang
 * @since 2017-09-30
 */

public class JobFragment extends BaseFragment {
    private JobUrlControl jobUrlControl = JobUrlControl.getInStance();
    private int page = 1;

    public static Fragment newInstance(String BaseUrl, String college, int collegeId, int provinceId) {
        JobFragment f = new JobFragment();
        Bundle b = new Bundle();
        b.putString("BaseUrl", BaseUrl);
        b.putString("college", college);
        b.putInt("collegeId", collegeId);
        b.putInt("provinceId", provinceId);
        f.setArguments(b);
        return f;
    }

    @Override
    public String getUrl(boolean isPull, MesItemHolder holder) {
        if (isPull){
            page = 1;
        }
        holder.setPager(page);
        holder.setPull(isPull);
        holder.setMessKind(MesItemHolder.mes_job_fair);

        return jobUrlControl.getUrl(holder);
    }

    @Override
    public void RequestSuccess() {
        page++;
    }
}
