package juhe.jiangdajiuye.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.tencent.tauth.Tencent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import juhe.jiangdajiuye.R;

/**
 * Created by wangqiang on 2016/7/19.
 */
public class ShareDialog {
    private Context context;
    private Dialog dialog;
    public Button cancel;
    private   int ItemHeight = 250;
    private GridView shareGride;
    private SimpleAdapter gridAdapter ;
    private List<Map<String,String>> Gdate = new ArrayList<Map<String,String>>();
    private int[] shareIcn = new int[]{R.drawable.share_qkong,
            R.drawable.share_qq, R.drawable.share_wx, R.drawable.share_pyq};
    private String[] shareText = new String[]{"QQ空间","QQ","微信","朋友圈"};
    private View view;
    private Tencent tencet;
    private String APP_ID = "1105550774";
    private Itemlister itemlister;
    public interface Itemlister{
        //分享到QQ空间
        void shareToQzone();
        //分享到QQ好友
        void shareToQQ();
        //分享到微信
        void shareTowei();
        //分享到微信朋友圈
        void shareTopyq();
    }
    public void setItemlister(Itemlister itemlister){
        this.itemlister = itemlister;
    }
    public Dialog getDialog(Context context) {
        this.context = context;
        // 使用不带theme的构造器，获得的dialog边框距离屏幕仍有几毫米的缝隙。
        // Dialog dialog = new Dialog(getActivity());
         dialog = new Dialog(context);
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view  = inflater.inflate(R.layout.share, null);
        findid();
        initGridView();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // must be called before set content
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        // 设置宽度为屏宽、靠近屏幕底部。
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.color.transparent);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        ListAdapter listAdapter = shareGride.getAdapter();
        if (listAdapter != null) {

            View listItem = listAdapter.getView(1, null, shareGride);
            listItem.measure(0, 0);
             ItemHeight = listItem.getMeasuredHeight();
            Log.e("ShareDialog","listAdapter is not null and get the height of adapterItem is "+ItemHeight);
        }
        ViewGroup.LayoutParams params = shareGride.getLayoutParams();
        params.height = ItemHeight;
        shareGride.setLayoutParams(params);
//        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(WindowManager.
//                LayoutParams.MATCH_PARENT,ItemHeight );
//        shareGride.setLayoutParams(param);
        window.setAttributes(wlp);
        window.setWindowAnimations(R.style.popWindow_animation);
        return dialog;
    }
    private void findid(){
        cancel = (Button)view.findViewById(R.id.jianzhi_share_cancel);
        shareGride = (GridView)view.findViewById(R.id.share_popGrid);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }
    public void initGridView(){
        //分享资源界面
        for(int i = 0;i<shareIcn.length;i++){
            Map<String,String> map = new HashMap<String,String>();
            map.put("icn",shareIcn[i]+"");
            map.put("text",shareText[i]+"");
            Gdate.add(map);
        }
        gridAdapter = new SimpleAdapter(context,
                Gdate, R.layout.share_item,
                new String[]{"icn","text"},
                new int[]{R.id.share_item_image, R.id.share_item_text});
        shareGride.setAdapter(gridAdapter);
        shareGride.setOnItemClickListener(new myItemlister());
    }
    private class myItemlister implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position){
                case 0:
                    //qq空间
                    itemlister.shareToQzone();
                    break;
                case 1:
                    //QQ
                    itemlister.shareToQQ();
                    break;
                case 2:
                    //微信
                    itemlister.shareTowei();
                    break;
                case 3:
                    //朋友圈
                    itemlister.shareTopyq();
                    break;
                default:
                    break;
            }
        }
    }
}
