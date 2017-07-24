package juhe.jiangdajiuye.imageUtil;

import android.graphics.Bitmap;
import android.widget.ImageView;


/**
 * class description here
 *
 * @version 1.0.0
 * @outher wangqiang
 * @project 51Bike
 * @since 2017-03-23
 */
public class ImageManager {
    private static String TAG = "ImageManager";

    public ImageManager() {
    }
    public static void bindImageWithBitmap(ImageView imageView, String url){
        ImageLFMemory mmemory =  ImageLFMemory.InStance();
        Bitmap bitmap = mmemory.getBitmapFromMemory(url);
        if(bitmap != null ){
            imageView.setImageBitmap(bitmap);
            return ;
        }
        ImageLocalLoad imageLocalLoad = new ImageLocalLoad();
        bitmap  = imageLocalLoad.getBitmapFromLocal(url);
        if(bitmap != null ){
            imageView.setImageBitmap(bitmap);
            return ;
        }
        ImageLFNet net = new ImageLFNet();
        net.getBitmapFromNet(imageView,url);
//        imageView.setImageResource(R.drawable.default_icn);
    }
}
