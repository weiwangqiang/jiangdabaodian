package juhe.jiangdajiuye.util.getImageUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2017-08-04
 */

public class GetImageFromLocate {
    public static final String TAG = "GetImageFromLocate";
    public static final int REQUEST_CODE = 0x10;
    public static void getImageFromLocate(Context mCtx){
        Intent intent ;
//        if (Build.VERSION.SDK_INT >= 19 ) {
//            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        }else
        intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
//        intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*");
        intent.putExtra("return_data", true);
        ((Activity)mCtx).startActivityForResult(intent, REQUEST_CODE);
    }

    /**
     *     解析结果
     * @param mCtx
     * @param intent
     * @return
     */
    public static File getResult(Context mCtx,Intent intent){
        try {
            return UriToFile(mCtx, intent.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null ;
    }
    private static File UriToFile(Context mCtx , Uri uri) throws Exception{
        Log.i(TAG, "UriToFile: "+uri);
        String picturePath = null;
//        if (Build.VERSION.SDK_INT >= 19 ) {
//            String wholeID = DocumentsContract.getDocumentId(uri);
//            String id = wholeID.split(":")[1];
//            String[] column = { MediaStore.Images.Media.DATA };
//            String sel = MediaStore.Images.Media._ID +"=?";
//            Cursor cursor = mCtx.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column,
//                    sel, new String[] { id }, null);
//            int columnIndex = cursor.getColumnIndex(column[0]);
//            if (cursor.moveToFirst()) {
//                picturePath = cursor.getString(columnIndex);
//            }
//            cursor.close();
//        }else{
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = mCtx.getContentResolver().query(uri, proj, null, null, null);
            if(cursor.moveToFirst()){
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                picturePath = cursor.getString(column_index);
            }
            cursor.close();
            if(picturePath == null){
                picturePath  = uri.getPath();
            }
//        }
        return new File(picturePath);
    }
}
