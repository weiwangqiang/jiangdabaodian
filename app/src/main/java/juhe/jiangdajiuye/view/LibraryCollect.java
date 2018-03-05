package juhe.jiangdajiuye.view;

import android.content.Intent;
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
import juhe.jiangdajiuye.view.adapter.LibraryCollectAdapter;
import juhe.jiangdajiuye.bean.BookBean;
import juhe.jiangdajiuye.bean.bmobUser.UserBean;
import juhe.jiangdajiuye.consume.SlipRecyclerView.SlipItemClickListener;
import juhe.jiangdajiuye.consume.SlipRecyclerView.SlipRecyclerView;
import juhe.jiangdajiuye.consume.recyclerView.LibraryCollectDecoration;
import juhe.jiangdajiuye.core.BaseActivity;
import juhe.jiangdajiuye.sql.repository.LibraryRepository;
import juhe.jiangdajiuye.user.UserManager;
import juhe.jiangdajiuye.utils.ResourceUtils;
import juhe.jiangdajiuye.utils.ToastUtils;

/**
 * Created by wangqiang on 2016/10/6.
 */
public class LibraryCollect extends BaseActivity {
    private String TAG = "LibraryCollect";
    private Boolean first = true;
    public SlipRecyclerView recyclerView;
    private Toolbar toolbar;
    private View nothing;
    private LinearLayoutManager manager;
    public LibraryCollectAdapter adapter;
    private LibraryRepository libraryRepository;
    private ArrayList<BookBean> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect_fragment);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (UserManager.getInStance().isLogin()) {
            getMenuInflater().inflate(R.menu.menu_syndata, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
        UserBean userBean = UserManager.getInStance().getUserBean();
        if (null == userBean) {
            return;
        }
        BmobQuery<BookBean> query = new BmobQuery<>();
        query.addWhereEqualTo("userId", userBean.getObjectId());
        query.findObjects(new FindListener<BookBean>() {
            @Override
            public void done(List<BookBean> list, BmobException e) {
                if (null == e) {
                    saveDataToLocal(list);
                } else {
                    ToastUtils.showToast(ResourceUtils.getString(R.string.toast_syn_data_download_failure));
                }
            }
        });

    }

    private void saveDataToLocal(List<BookBean> list) {
        if(list.size() == 0){
             return;
        }
        libraryRepository.add(list);
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
        for (BookBean item : data) {
            item.setUserId(userBean.getObjectId());
        }
        BmobQuery<BookBean> query = new BmobQuery<>();
        query.addWhereEqualTo("userId", userBean.getObjectId());
        query.findObjects(new FindListener<BookBean>() {
            @Override
            public void done(List<BookBean> list, BmobException e) {
                if (null == e) {
                    List<BmobObject> pushData = new ArrayList<>();
                    pushData.addAll(data);
                    for (BookBean bookBean : data) {
                        if (list.contains(bookBean)) {
                            pushData.remove(bookBean);
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
        if(null == pushData || pushData.size() == 0){
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


    public void initView() {
        libraryRepository = LibraryRepository.getInstance();
        findId();
        initList();
        initToolbar();
    }

    public void findId() {
        recyclerView = (SlipRecyclerView) findViewById(R.id.Collect_listView);
        toolbar = (Toolbar) findViewById(R.id.Collect_toolbar);
        nothing = findViewById(R.id.nothing);
    }

    public void initList() {
        initDate();
        adapter = new LibraryCollectAdapter(this, R.layout.library_listitem, data);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new LibraryCollectDecoration(this,
                LibraryCollectDecoration.VERTICAL_LIST));
        adapter.setOnItemLister(new SlipItemClickListener<BookBean>() {
            @Override
            public void onItemClick(BookBean bookBean, int position) {
                Intent intent = new Intent(LibraryCollect.this, LibraryDetails.class);
                intent.putExtra("url", bookBean.getUrl());
                intent.putExtra("book", bookBean.getBook());
                intent.putExtra("editor", bookBean.getEditor());
                intent.putExtra("available", bookBean.getAvailable());
                intent.putExtra("number", bookBean.getNumber());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
            }

            @Override
            public void onSubViewClick(View view) {

            }

        });
    }

    public void initToolbar() {
        toolbar.setTitle(ResourceUtils.getString(R.string.library_collect_title));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void initDate() {
        data.clear();
        data.addAll(libraryRepository.selectAll());
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
}
