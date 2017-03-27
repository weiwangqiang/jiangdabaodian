package juhe.jiangdajiuye.tool;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by wangqiang on 2016/11/12.
 */

public class toast {
    public Toast mytoast = null;

    public  void makeText(Context context,String text){
        if(mytoast!=null){
            mytoast.cancel();
        }
        mytoast = new Toast(context);
        mytoast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }

}
