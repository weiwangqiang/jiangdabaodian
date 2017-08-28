package juhe.jiangdajiuye.imageUtil;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import juhe.jiangdajiuye.R;


/**
 * class description here
 *  从网络中获取 bitmap
 * @version 1.0.0
 * @outher wangqiang
 * @project 51Bike
 * @since 2017-03-22
 */
public class ImageLFNet {
    private String TAG = "ImageLoad";
    private String url = "";
    public ImageLFNet() {
    }
    public  void getBitmapFromNet(ImageView imageView, String url ){
        Log.i(TAG, "downLoadImage: url "+url);
        this.url = url ;
//        Glide.get(imageView.getContext()).load
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.load_waiting)
                .error(R.drawable.load_waiting)
                .skipMemoryCache(true)//跳过内存缓存
//                .animate(R.anim.item_alpha_in)//提供加载动画
//                .thumbnail(0.1f) //先加载略缩图在加载大图
//                .override(800, 800) //设置加载尺寸
//                .centerCrop()//设置动态转换 or fitCenter()
//                .transform(new GlideRoundTransform(this))或者设置转换器
                .priority(Priority.NORMAL)//设置下载优先级
                .into(imageView);
//        ImageOptions imageOptions = new ImageOptions.Builder()
//                .setSize(DensityUtil.dip2px(80), DensityUtil.dip2px(80))
//                .setRadius(DensityUtil.dip2px(5))
//                // 如果ImageView的大小不是定义为wrap_content, 不要crop.
////                .setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
//                // 加载中或错误图片的ScaleType
//                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
//                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
//                .setLoadingDrawableId(R.drawable.load_waiting)
//                .setFailureDrawableId(R.drawable.load_waiting)
//                .build();
//
//        x.image().bind(imageView,
//                url,
//                imageOptions,
//                new CustomBitmapLoadCallBack());
    }
//    public class CustomBitmapLoadCallBack implements Callback.ProgressCallback<Drawable> {
//
//        public CustomBitmapLoadCallBack() {
//
//        }
//
//        @Override
//        public void onWaiting() {
//
//        }
//
//        @Override
//        public void onStarted() {
//
//        }
//
//        @Override
//        public void onLoading(long total, long current, boolean isDownloading) {
//            Log.i(TAG, "onLoading: total "+total+
//                    " current "+current+" isDownLoading "+isDownloading);
//        }
//
//        @Override
//        public void onSuccess(Drawable result) {
//            saveBitmap(result);
//        }
//
//        @Override
//        public void onError(Throwable ex, boolean isOnCallback) {
//        }
//
//        @Override
//        public void onCancelled(CancelledException cex) {
//
//        }
//
//        @Override
//        public void onFinished() {
//
//        }
//    }

    public void saveDrawable(final Drawable result,final String url) {
        Log.i(TAG, "saveBitmap: ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = DrawableToiBitmap(result);
                saveToMemory(bitmap);
                saveTolocal(bitmap,url);
            }
        }).start();

    }
    private void saveBitmap(final Bitmap result,final String url) {
        Log.i(TAG, "saveBitmap: ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                saveToMemory(result);
                saveTolocal(result,url);
            }
        }).start();

    }

    private void saveToMemory(Bitmap bitmap) {
        ImageLFMemory imageLFMemory  = ImageLFMemory.InStance();
        imageLFMemory.setBitmapToMemory(url,bitmap);
    }

    private void saveTolocal(Bitmap bitmap,String url) {
        ImageLocalLoad load = new ImageLocalLoad();
        load.saveBitmapToLocal(url,"name","png",bitmap);
    }

    @NonNull
    private Bitmap DrawableToiBitmap(Drawable result) {
        Bitmap bitmap = Bitmap.createBitmap(
                result.getIntrinsicWidth(),
                result.getIntrinsicHeight(),
                result.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        //canvas.setBitmap(bitmap);
        result.setBounds(0, 0, result.getIntrinsicWidth(), result.getIntrinsicHeight());
        result.draw(canvas);
        return bitmap;
    }
}
