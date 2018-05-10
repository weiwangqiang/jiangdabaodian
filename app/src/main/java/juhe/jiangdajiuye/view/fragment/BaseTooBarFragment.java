package juhe.jiangdajiuye.view.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import juhe.jiangdajiuye.R;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2018-02-05
 */

public class BaseTooBarFragment extends Fragment {
    /**
     * Called when a fragment is first attached to its context.
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.hold, R.anim.slide_out_right);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    protected void finish(){
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.hold, R.anim.slide_out_right);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
//        BaseApplication.getRefWatcher().watch(this);
    }
}
