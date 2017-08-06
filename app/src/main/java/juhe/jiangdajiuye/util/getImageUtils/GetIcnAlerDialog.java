package juhe.jiangdajiuye.util.getImageUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import juhe.jiangdajiuye.R;
import juhe.jiangdajiuye.util.getImageUtils.api.DialogInterface;

/**
 * 获取用户头像的对话框
 *
 * Created by wangqiang on 2017/1/9.
 */

public class GetIcnAlerDialog extends AlertDialog implements View.OnClickListener ,DialogInterface {
    String TAG ="MarginAlerDialog";
    String filePath;

    //拍照和图库的Intent请求码
    public final int TAKE_PHOTO_WITH_DATE = 200;
    public final int TAKE_PHOTO_FROM_IMAGE = 201;
    private Button camera;
    private Button pictrue;
    private Button cancel;


    private Context context;
    public GetIcnAlerDialog(Context context, String filePath) {
        super(context);
        this.context = context;
        this.filePath = filePath;

    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.dialog_geticn);
        getWindow().setBackgroundDrawable(new ColorDrawable());

        setCanceledOnTouchOutside(true);
        // 设置宽度为屏宽、靠近屏幕底部。
        Window window = getWindow();
        window.setBackgroundDrawableResource(R.color.transparent);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT ;
        window.setWindowAnimations(R.style.popWindow_animation);
    }
    @Override
    protected  void onStart(){
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    @Override
    public void myShow() {
        show();
    }

    @Override
    public void myCancel() {
        cancel();
    }
}
