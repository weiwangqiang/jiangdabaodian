package juhe.jiangdajiuye.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.core.BaseActivity;
import juhe.jiangdajiuye.entity.recordEntity.LeaveMes;
import juhe.jiangdajiuye.tool.ProgressDialog;
import juhe.jiangdajiuye.util.AppUtils;
import juhe.jiangdajiuye.util.ToastUtils;

/**
 * Created by wangqiang on 2016/10/6.
 * 建议界面
 */
public class Suggest extends BaseActivity {
    private String TAG = "fragmentCollect";
    private Toolbar toolbar;
    private EditText getemail ,getcontent;
    private Button send;
    private ProgressDialog dialog;
    private InputMethodManager imm;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggest);
        dialog = new  ProgressDialog(this,R.drawable.waiting);
       initView();
    }

    public void initView(){
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        findid();
        initToolbar();
    }
    public void findid(){
        toolbar = (Toolbar)findViewById(R.id.suggest_toolbar);
        getemail = (EditText) findViewById(R.id.suggest_email);
        getcontent = (EditText) findViewById(R.id.suggest_content);
        send = (Button) findViewById(R.id.suggest_send);
        send.setOnClickListener(this);
    }

    public void initToolbar(){
        toolbar.setTitle("反馈意见");
//        toolbar.setNavigationIcon(R.drawable.menue);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case android.R.id.home:
                Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();
                break;
            case R.id.suggest_send:
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
                sendContent();
                break;
        }
    }
    public void sendContent(){
        if(getemail.getText().toString().length()==0){
            ToastUtils.showToast("联系方式不能为空");
            return;
        }
        if(getcontent.getText().toString().length()==0){
            ToastUtils.showToast("内容不能为空");
            return;
        }
        LeaveMes mes = new LeaveMes() ;
        mes.setEmail(getemail.getText().toString().trim());
        mes.setContent(getcontent.getText().toString().trim());
        mes.setAppLevel(AppUtils.getVersionName());
        mes.setDevice(AppUtils.getDevice());
        mes.setDeviceLevel(AppUtils.getDeviceLevel());
        try {
            onRequestYun(mes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void onRequestYun(LeaveMes mes) throws Exception{
        dialog.show();
        Log.i(TAG, "onRequestYun: "+mes.getContent());
        mes.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                Log.i(TAG, "done: "+s);
                if(e == null){
                    ToastUtils.showToast("提交成功");
                    getemail.setText("");
                    getcontent.setText("");
                }
                else{
                    ToastUtils.showToast("提交失败");
                    e.printStackTrace();
                }
                dialog.cancel();
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                 finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onResume(){
        super.onResume();
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

}
