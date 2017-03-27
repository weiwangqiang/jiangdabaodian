package juhe.jiangdajiuye.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.tool.ProgressDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

/**
 * Created by wangqiang on 2016/10/6.
 */
public class suggest extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "fragmentCollect";
    private String url = "http://119.29.178.251:8080/asaad/selectData";
    private Toolbar toolbar;
    private EditText getemail ,getcontent;
    private Button send;
    private String email,content;
    private ProgressDialog dialog;
    private InputMethodManager imm;
    private OkHttpClient mOkHttpClient ;
    private Handler handler = new Handler(){
        public void handleMessage(Message mes){
            switch (mes.what){
                case 0x1:
                    dialog.cancel();
                    Toast.makeText(suggest.this,"谢谢你的反馈！！",Toast.LENGTH_SHORT).show();
                    getemail.setText("");
                    getcontent.setText("");
                    break;
                case 0x2:
                    break;
                case 0x3:
                    Toast.makeText(suggest.this,"失败！",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggest);
        dialog = new  ProgressDialog(this,R.drawable.waiting);
       initView();
    }

    public void initView(){
        mOkHttpClient = new OkHttpClient();
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
            Toast.makeText(this,"联系方式不能为空",0).show();
            return;
        }
        if(getcontent.getText().toString().length()==0){
            Toast.makeText(this,"内容不能为空",0).show();
            return;
        }
        email = getemail.getText().toString().trim();
        content = getcontent.getText().toString().trim();
        putMes();
    }

    private void putMes(){
       dialog.show();
        String u = url+"?email="+email+"&content="+content;
        Log.w(TAG,"url is "+u);
        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url(u)
                .get()
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e(TAG,"error!");
                handler.sendEmptyMessage(0x3);
            }
            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                Log.i(TAG," postJson response code is "+response.code()+" body is  "+response.body().string());
                handler.sendEmptyMessage(0x1);
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
