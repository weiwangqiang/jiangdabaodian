package juhe.jiangdajiuye.view.activity.userCenter;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.bean.UserCenterBean;
import juhe.jiangdajiuye.bean.bmobUser.UserBean;
import juhe.jiangdajiuye.base.BaseActivity;
import juhe.jiangdajiuye.utils.ResourceUtils;
import juhe.jiangdajiuye.utils.ToastUtils;
import juhe.jiangdajiuye.adapter.UserCenterAdapter;
import juhe.jiangdajiuye.view.activity.userCenter.engine.UserManager;

/**
 * 提供他人查看用户信息的界面
 */
public class UserMessageActivity extends BaseActivity {
    private String userId  ;
    private RecyclerView recyclerView;
    private UserCenterAdapter adapter;
    private List<UserCenterBean> data = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_message);
        userId = getIntent().getStringExtra("userId") ;
        supportToolbar(R.id.fragment_logout_toolbar,
                ResourceUtils.getString(R.string.title_user_message));
        initRecycler();
        upDataUserMes();
    }

    private void upDataUserMes() {
        BmobQuery<UserBean> query = new BmobQuery<>();
        query.getObject(userId
                , new QueryListener<UserBean>() {
                    @Override
                    public void done(UserBean userBean, BmobException e) {
                        if (e == null) {
                            UserManager.getInStance().setLogin(userBean);
                            upData();
                        }else{
                            ToastUtils.showToast(ResourceUtils.getString(R.string.toast_network_error));
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
                , false));
        data.add(new UserCenterBean(ResourceUtils.getString(R.string.textview_user_center_target_company)
                , userBean.getCompany()
                , false));
        data.add(new UserCenterBean(ResourceUtils.getString(R.string.textview_user_center_target_work)
                , userBean.getWork()
                , false));
        adapter.notifyDataSetChanged();
    }


    private void initRecycler() {
        recyclerView = findViewById(R.id.fragment_logout_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserCenterAdapter(this, data, R.layout.item_recycler_user_center);
        recyclerView.setAdapter(adapter);

    }
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }
}
