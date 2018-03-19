package juhe.jiangdajiuye.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.bean.bmobUser.UserBean;
import juhe.jiangdajiuye.bean.school.School;
import juhe.jiangdajiuye.bean.school.SchoolList;
import juhe.jiangdajiuye.core.BaseActivity;
import juhe.jiangdajiuye.user.UserManager;
import juhe.jiangdajiuye.utils.ResourceUtils;
import juhe.jiangdajiuye.utils.ToastUtils;
import juhe.jiangdajiuye.utils.httpUtils.HttpHelper;
import juhe.jiangdajiuye.utils.httpUtils.Inter.IDataListener;
import juhe.jiangdajiuye.utils.httpUtils.task.HttpTask;
import juhe.jiangdajiuye.view.adapter.ChoiceSchoolAdapter;
import juhe.jiangdajiuye.view.dialog.ProgressDialog;

public class ChoiceSchool extends BaseActivity {

    private RecyclerView recyclerView;
    private ChoiceSchoolAdapter adapter;
    private ProgressDialog progressDialog;
    private EditText editText;
    private String url = "http://www.hisihi.com/app.php?s=/school/school/provinceid/";
    private List<String> data;
    private static int kind = 0;
    public static final int GET_SCHOOL = 1;
    public static final int GET_COMPANY = 2;
    public static final int GET_WORK = 3;
    public static void startActivity(Context context, int kind) {
        startActivity(context,kind,"");
    }
    public static void startActivity(Context context, int kind,String id){
        ChoiceSchool.kind = kind;
        Intent intent = new Intent(context, ChoiceSchool.class);
        intent.putExtra("school",id);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_school);
        switch (kind) {
            case GET_SCHOOL:
                initSchoolView();
                break;
            case GET_COMPANY:
                initEditView(R.string.title_choice_company);
                break;
            case GET_WORK:
                initEditView(R.string.title_choice_work);
                break;
        }
    }

    private void initEditView(int string) {
        supportToolbar(R.id.choice_school_toolbar, ResourceUtils.getString(string));
        findViewById(R.id.choice_school_recycler).setVisibility(View.GONE);
        findViewById(R.id.choice_edit_parent).setVisibility(View.VISIBLE);
        editText = findViewById(R.id.school_input);
        editText.setHint(string);
        progressDialog = new ProgressDialog(this) ;
    }

    private void initSchoolView() {
        supportToolbar(R.id.choice_school_toolbar, ResourceUtils.getString(R.string.title_choice_school));
        initRecycler();
        url = url + getIntent().getStringExtra("school");
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
        School p = new Gson().fromJson(result, School.class);
        for (SchoolList pro : p.getData()) {
            data.add(pro.getSchool_name());
        }
        adapter.append(data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(kind == GET_SCHOOL){
            progressDialog.show();
            initData();
        }
    }

    private void initRecycler() {
        recyclerView = findViewById(R.id.choice_school_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChoiceSchoolAdapter(this, R.layout.item_recycler_choice_school);
        recyclerView.setAdapter(adapter);
        adapter.setItemClick(new ChoiceSchoolAdapter.ItemClick() {
            @Override
            public void onItemClick(String school, int position) {
                UserManager.getInStance().getUserBean().setSchool(school);
                pushMes();
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

    public void sure(View view) {
        if (TextUtils.isEmpty(editText.getText().toString())) {
            ToastUtils.showToast(ResourceUtils.getString(R.string.toast_edit_text_empty));
            return;
        }
        progressDialog.show();
        switch (kind) {
            case GET_COMPANY:
                UserManager.getInStance().getUserBean().setCompany(editText.getText().toString());
                break;
            case GET_WORK:
                UserManager.getInStance().getUserBean().setWork(editText.getText().toString());
                break;
            default:
                break;
        }
        pushMes();
    }

    private void pushMes() {
        UserBean bean = UserManager.getInStance().getUserBean() ;
        bean.update(new UpdateListener(){
            @Override
            public void done(BmobException e) {
                if(e ==null){
                    finish();
                }else{
                    ToastUtils.showToast(ResourceUtils.getString(R.string.toast_network_error));
                }
                progressDialog.cancel();
            }
        });
    }
}
