package juhe.jiangdajiuye.utils.imageUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import juhe.jiangdajiuye.utils.AppConfigUtils;

/**
 * class description here
 *
 * @version 1.0.0
 * @outher wangqiang
 * @project 51Bike
 * @since 2017-03-23
 */
public class ImageLocalLoad {
    private String TAG = "ImageLocalLoad";
    private String rootUrl = AppConfigUtils.savePictureUrl;
    private StringBuilder sb = new StringBuilder(rootUrl);
    public ImageLocalLoad() {
        File file  = new File(rootUrl);
        if (!file.exists()) {
            file.mkdir();
        }
    }
    public Bitmap getBitmapFromLocal(String imageUrl){
        String s[] =  imageUrl.split("/");
        int length = s.length;
        sb.append(s[length -2 ]);
        sb.append("/");
        sb.append(s[length-1]);
        try {
            File file = new File(sb.toString());
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(
                        file));
                return bitmap;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean saveBitmapToLocal(String saveUri,String name,String kind , Bitmap bitmap){
    try{
        File file = new File(saveUri);
        if(!file.exists()){
            file.mkdirs();
        }
        File saveFile = new File(saveUri+name);
        if(saveFile.exists()){
            saveFile.delete();
        }
        try{
            FileOutputStream out = new FileOutputStream(saveFile);
            if(kind != null &&  kind.equals("jpeg") || kind.equals("jpg"))
              bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            else
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

}
