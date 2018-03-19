package juhe.jiangdajiuye.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.bean.UserCenterBean;
import juhe.jiangdajiuye.bean.bmobUser.UserBean;
import juhe.jiangdajiuye.user.UserManager;
import juhe.jiangdajiuye.utils.ResourceUtils;
import juhe.jiangdajiuye.utils.SharePreUtils;
import juhe.jiangdajiuye.view.activity.ChoiceProvince;
import juhe.jiangdajiuye.view.activity.ChoiceSchool;
import juhe.jiangdajiuye.view.adapter.UserCenterAdapter;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2018-02-05
 */

public class LogoutFragment extends BaseTooBarFragment implements View.OnClickListener {
    private static final String TAG = "LogoutFragment ";
    private View viewRoot;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private UserCenterAdapter adapter;
    private final int GET_STRING = 0x12;
    private List<UserCenterBean> data = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (viewRoot != null) {
            return viewRoot;
        }
        viewRoot = inflater.inflate(R.layout.fragment_logout, container, false);
        init();
        if (UserManager.getInStance().isLogin()) {
            upDataUserMes();
        }
        return viewRoot;
    }

    private void upDataUserMes() {
        BmobQuery<UserBean> query = new BmobQuery<>();
        query.getObject(UserManager.getInStance().getUserBean().getObjectId()
                , new QueryListener<UserBean>() {
                    @Override
                    public void done(UserBean userBean, BmobException e) {
                        if (e == null) {
                            UserManager.getInStance().setLogin(userBean);
                            upData();
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        upData();
    }

    private void init() {
        toolbar = viewRoot.findViewById(R.id.fragment_logout_toolbar);
        viewRoot.findViewById(R.id.fragment_logout_button).setOnClickListener(this);
        initToolBar();
        initRecycler();
    }

    private void initRecycler() {
        recyclerView = viewRoot.findViewById(R.id.fragment_logout_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UserCenterAdapter(getActivity(), data, R.layout.item_recycler_user_center);
        recyclerView.setAdapter(adapter);
        adapter.setItemClick(new UserCenterAdapter.ItemClick() {
            @Override
            public void onItemClick(UserCenterBean userCenterBean, int position) {
                Log.i(TAG, "onItemClick: ");
                switch (position) {
                    case 1:
                        startActivity(new Intent(getActivity(), ChoiceProvince.class));
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
                        break;
                    case 2:
                        ChoiceSchool.startActivity(getActivity(), ChoiceSchool.GET_COMPANY);
                        break;
                    case 3:
                        ChoiceSchool.startActivity(getActivity(), ChoiceSchool.GET_WORK);
                        break;
                }
            }
        });
    }

    private void upData() {
        UserBean userBean = UserManager.getInStance().getUserBean();
        data.clear();
        data.add(new UserCenterBean(ResourceUtils.getString(R.string.textview_user_center_nick_name)
                , userBean.getName()
                , false));
        data.add(new UserCenterBean(ResourceUtils.getString(R.string.textview_user_center_school)
                , userBean.getSchool()
                , true));
        data.add(new UserCenterBean(ResourceUtils.getString(R.string.textview_user_center_target_company)
                , userBean.getCompany()
                , true));
        data.add(new UserCenterBean(ResourceUtils.getString(R.string.textview_user_center_target_work)
                , userBean.getWork()
                , true));
        adapter.notifyDataSetChanged();
    }

    private void initToolBar() {
        toolbar.setTitle(ResourceUtils.getString(R.string.title_fragment_logout));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_logout_button:
                UserManager.getInStance().setLogout();
                SharePreUtils.setString(SharePreUtils.KEY_USER_OBJECT_ID, "");
                finish();
                break;
        }
    }

}
