package juhe.jiangdajiuye.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.bean.bmobRecordEntity.LeaveMes;
import juhe.jiangdajiuye.core.BaseActivity;
import juhe.jiangdajiuye.dialog.ProgressDialog;
import juhe.jiangdajiuye.util.AppConfigUtils;
import juhe.jiangdajiuye.util.ResourceUtils;
import juhe.jiangdajiuye.util.ToastUtils;

/**
 * Created by wangqiang on 2016/10/6.
 * 建议界面
 */
public class FeedBack extends BaseActivity {
    private String TAG = "fragmentCollect";
    private Toolbar toolbar;
    private EditText ETEmail, ETContent;
    private Button send;
    private ProgressDialog dialog;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        dialog = new ProgressDialog(this, R.drawable.waiting);
        initView();
    }

    public void initView() {
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        findId();
        initToolbar();
    }

    public void findId() {
        toolbar = (Toolbar) findViewById(R.id.suggest_toolbar);
        ETEmail = (EditText) findViewById(R.id.suggest_email);
        ETContent = (EditText) findViewById(R.id.suggest_content);
        send = (Button) findViewById(R.id.suggest_send);
        send.setOnClickListener(this);
    }

    public void initToolbar() {
        toolbar.setTitle(ResourceUtils.getString(R.string.title_feed_back));
//        toolbar.setNavigationIcon(R.drawable.menue);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case android.R.id.home:
                break;
            case R.id.suggest_send:
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
                sendContent();
                break;
        }
    }

    public void sendContent() {
        if (ETEmail.getText().toString().length() == 0) {
            ToastUtils.showToast(ResourceUtils.getString(R.string.toast_feedback_contact_information_warn));
            return;
        }
        if (ETContent.getText().toString().length() == 0) {
            ToastUtils.showToast(ResourceUtils.getString(R.string.toast_feedback_content_warn));
            return;
        }
        if(!correctContactInfor(ETEmail.getText().toString())){
            ToastUtils.showToast(ResourceUtils.getString(R.string.toast_feedback_contact_information_error));
            return;
        }
        LeaveMes mes = new LeaveMes();
        mes.setEmail(ETEmail.getText().toString().trim());
        mes.setContent(ETContent.getText().toString().trim());
        mes.setAppLevel(AppConfigUtils.getVersionName());
        mes.setDevice(AppConfigUtils.getDevice());
        mes.setDeviceLevel(AppConfigUtils.getDeviceLevel());
        try {
            onRequestYun(mes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //判断是否满足联系方式
    public boolean correctContactInfor(String contactInfor){
        if(TextUtils.isEmpty(contactInfor)){
            return false ;
        }
        return contactInfor.contains("@") || contactInfor.length() >=11 ;
    }
    private void onRequestYun(LeaveMes mes) throws Exception {
        dialog.show();
        mes.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    ToastUtils.showToast(ResourceUtils.getString(R.string.toast_submit_success));
                    ETEmail.setText("");
                    ETContent.setText("");
                } else {
                    ToastUtils.showToast(ResourceUtils.getString(R.string.toast_submit_failure));
                    e.printStackTrace();
                }
                dialog.cancel();
            }
        });

    }
}
