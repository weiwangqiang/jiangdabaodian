package juhe.jiangdajiuye.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by wangqiang on 2016/5/15.
 *
 *   主界面的Viewpager
 */
public class FragmentAdapter extends FragmentPagerAdapter {
    public List<Fragment> list;
    private String[] title = {"今日宣讲","校内宣讲","招聘公告","招聘会","招聘职位","信息速递"};
    public FragmentAdapter(FragmentManager fm, List<Fragment> list ) {
        super(fm);
        this.list = list;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return title[position % list.size()];
    }
}
