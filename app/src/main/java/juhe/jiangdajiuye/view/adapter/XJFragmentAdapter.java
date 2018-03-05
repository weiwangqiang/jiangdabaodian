package juhe.jiangdajiuye.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by wangqiang on 2016/5/15.
 *首界面宣讲会tab的adapter
 */
public class XJFragmentAdapter extends FragmentPagerAdapter {
    public List<Fragment> list;
    public String[] text;

    public XJFragmentAdapter(FragmentManager fm, List<Fragment> list, String[] text) {
        super(fm);
        this.list = list;
        this.text = text;
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
        return text[position % text.length];
    }
}
