package juhe.jiangdajiuye.view.activity.userCenter;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.bean.school.Province;
import juhe.jiangdajiuye.bean.school.ProvinceList;
import juhe.jiangdajiuye.base.BaseActivity;
import juhe.jiangdajiuye.utils.ResourceUtils;
import juhe.jiangdajiuye.utils.ToastUtils;
import juhe.jiangdajiuye.net.httpUtils.HttpHelper;
import juhe.jiangdajiuye.net.httpUtils.inter.IDataListener;
import juhe.jiangdajiuye.net.httpUtils.task.HttpTask;
import juhe.jiangdajiuye.adapter.ChoiceSchoolAdapter;
import juhe.jiangdajiuye.view.dialog.ProgressDialog;

public class ChoiceProvince extends BaseActivity {

    private static final String TAG = "choiceProvince";
    private RecyclerView recyclerView;
    private ChoiceSchoolAdapter adapter;
    private ProgressDialog progressDialog;
    private String url = "http://www.hisihi.com/app.php?s=/school/province";
    private List<String> data;
    private List<ProvinceList> provinceList ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_school);
        supportToolbar(R.id.choice_school_toolbar, ResourceUtils.getString(R.string.title_choice_province));
        initRecycler();
        progressDialog = new ProgressDialog(this);
        data = new ArrayList<>();
    }

    private void initData() {
        HttpHelper httpHelper = HttpHelper.getInstance();
        httpHelper.get(url, null, new IDataListener<String>() {
            @Override
            public void onSuccess(String school) {
                upData(school);
                progressDialog.cancel();
            }
            @Override
            public void onFail(Exception exception, int responseCode) {
                ToastUtils.showToast(ResourceUtils.getString(R.string.toast_network_error));
                progressDialog.cancel();
            }
        }, HttpTask.Type.string);
    }

    private void upData(String result) {
        Province p = new Gson().fromJson(result, Province.class);
        provinceList = new ArrayList<>();
        provinceList.addAll(p.getData());
        for(ProvinceList pro : provinceList){
            data.add(pro.getProvince_name());
        }
        adapter.append(data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressDialog.show();
        initData();
    }

    private void initRecycler() {
        recyclerView = findViewById(R.id.choice_school_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChoiceSchoolAdapter(this, R.layout.item_recycler_choice_school);
        recyclerView.setAdapter(adapter);
        adapter.setItemClick(new ChoiceSchoolAdapter.ItemClick() {
            @Override
            public void onItemClick(String school,int position) {
                ChoiceSchool.startActivity(ChoiceProvince.this,
                        ChoiceSchool.GET_SCHOOL,provinceList.get(position).getProvince_id());
                finish();
            }
        });
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
