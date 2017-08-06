package juhe.jiangdajiuye.util.getImageUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import java.io.File;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2017-08-04
 */

public class GetImageFromCamera {
    public static final int REQUEST_CODE = 0x20;
    public static void getImageFromCamera(Context mCtx ,@NonNull String filePath){
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            File localFile = new File(filePath);
            if (localFile.exists()) {
                localFile.delete();
            }
            localFile.mkdir();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri imageUri = Uri.fromFile(new File(filePath));
            //设置输出路径
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            //设置输出格式
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            //是否以bitmap 形式返回照片
            intent.putExtra("return-data", false);
            ((Activity)mCtx).startActivityForResult(intent, REQUEST_CODE);

        }
    }
}
