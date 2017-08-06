package juhe.jiangdajiuye.fragment;

import android.support.v4.app.Fragment;


/**
 * @author wangqiang
 * @project jiangdajiuye
 * @since 2017-08-03
 */
public abstract class BaseFragment extends Fragment {
    private String TAG = "BaseFragment";

    private boolean mHasInitView = false ;

    public abstract void canAddData();
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(mHasInitView && isVisibleToUser)
            canAddData();
    }

}
