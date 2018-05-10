package juhe.jiangdajiuye.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.adapter.CollectAdapter;
import juhe.jiangdajiuye.bean.MessageBean;
import juhe.jiangdajiuye.bean.bmobUser.UserBean;
import juhe.jiangdajiuye.ui.SlipRecyclerView.SlipItemClickListener;
import juhe.jiangdajiuye.ui.SlipRecyclerView.SlipRecyclerView;
import juhe.jiangdajiuye.ui.recyclerView.RecyclerDecoration;
import juhe.jiangdajiuye.base.BaseActivity;
import juhe.jiangdajiuye.db.repository.CollectDepository;
import juhe.jiangdajiuye.view.activity.browse.MesBrowseActivity;
import juhe.jiangdajiuye.view.activity.userCenter.engine.UserManager;
import juhe.jiangdajiuye.utils.ResourceUtils;
import juhe.jiangdajiuye.utils.ToastUtils;

/**
 * Created by wangqiang on 2016/10/6.
 * 收藏夹
 */
public class MesCollectActivity extends BaseActivity {
    private String TAG = "fragmentCollect";
    private Boolean first = true;
    private SlipRecyclerView recyclerView;
    private Toolbar toolbar;
    private View nothing;
    private LinearLayoutManager manager;
    private CollectAdapter adapter;
    private List<MessageBean> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect_fragment);
        initView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_syn_data_push:
                getPushData();
                break;
            case R.id.menu_syn_data_download:
                downLoadData();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //下载数据
    private void downLoadData() {
        UserBean userBean = UserManager.getInStance().getUserBean() ;
        if(null == userBean){
            return;
        }
        BmobQuery<MessageBean> query = new BmobQuery<>();
        query.addWhereEqualTo("userId",userBean.getObjectId());
        query.findObjects(new FindListener<MessageBean>() {
            @Override
            public void done(List<MessageBean> list, BmobException e) {
                if(null == e){
                    saveDataToLocal(list);
                }else {
                    ToastUtils.showToast(ResourceUtils.getString(R.string.toast_syn_data_download_failure));
                }
            }
        });

    }

    private void saveDataToLocal(List<MessageBean> list) {
        if(list.size() == 0){
            return;
        }
        CollectDepository.getInstance().add(list);
        initDate();
        adapter.notifyDataSetChanged();
        ToastUtils.showToast(ResourceUtils.getString(R.string.toast_syn_data_download_success));
    }


    //获取需要上传的数据，避免重复
    private void getPushData() {
        UserBean userBean = UserManager.getInStance().getUserBean();
        if (null == userBean) {
            return;
        }
        for (MessageBean item : data) {
            item.setUserId(userBean.getObjectId());
        }
        BmobQuery<MessageBean> query = new BmobQuery<>();
        query.addWhereEqualTo("userId", userBean.getObjectId());
        query.findObjects(new FindListener<MessageBean>() {
            @Override
            public void done(List<MessageBean> list, BmobException e) {
                if (null == e) {
                    List<BmobObject> pushData = new ArrayList<>();
                    pushData.addAll(data);
                    if(list.size() != 0){
                        for(MessageBean item : data){
                            if(list.contains(item)){
                                pushData.remove(item);
                            }
                        }
                    }
                    pushLocalData(pushData);
                } else {
                    ToastUtils.showToast(ResourceUtils.getString(R.string.toast_submit_failure));
                }
            }
        });
    }
    //提交数据
    private void pushLocalData(List<BmobObject> pushData) {
        if(pushData == null || pushData.size() == 0){
            ToastUtils.showToast(ResourceUtils.getString(R.string.toast_syn_data_not_data_to_push));
            return;
        }
        new BmobBatch().insertBatch(pushData).doBatch(new QueryListListener<BatchResult>() {

            @Override
            public void done(List<BatchResult> list, BmobException e) {
                if (null == e) {
                    ToastUtils.showToast(ResourceUtils.getString(R.string.toast_submit_success));
                } else {
                    ToastUtils.showToast(ResourceUtils.getString(R.string.toast_submit_failure));
                }
            }
        });
    }

    private void initView() {
        findId();
        initList();
        initToolbar();
    }

    private void findId() {
        recyclerView = (SlipRecyclerView) findViewById(R.id.Collect_listView);
        toolbar = (Toolbar) findViewById(R.id.Collect_toolbar);
        nothing = findViewById(R.id.nothing);
    }

    private void initList() {
        initDate();
        adapter = new CollectAdapter(this, R.layout.main_list_item, data);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new RecyclerDecoration(this,
                RecyclerDecoration.VERTICAL_LIST));
        adapter.setOnItemLister(new SlipItemClickListener<MessageBean>() {
            @Override
            public void onItemClick(MessageBean messageBean, int position) {
                MesBrowseActivity.StartActivity(MesCollectActivity.this, messageBean);
            }

            @Override
            public void onSubViewClick(View view) {

            }

        });
    }

    private void initToolbar() {
        toolbar.setTitle(ResourceUtils.getString(R.string.title_collect));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initDate() {
        data.clear();
        data.addAll( CollectDepository.getInstance().selectAll());
        first = false;
        if (data.size() == 0) {
            nothing.setVisibility(View.VISIBLE);
        } else
            nothing.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case android.R.id.home:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (first) {
            initList();
        } else {
            initDate();
            adapter.RefreshDate(data);
        }
    }

    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     * <p>
     * <p>This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * {@link #onPrepareOptionsMenu}.
     * <p>
     * <p>The default implementation populates the menu with standard system
     * menu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     * <p>
     * <p>You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     * <p>
     * <p>When you add items to the menu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (UserManager.getInStance().isLogin()) {
            getMenuInflater().inflate(R.menu.menu_syndata, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

}
