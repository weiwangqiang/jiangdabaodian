package juhe.jiangdajiuye.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import juhe.jiangdajiuye.bean.MessageItem;

/**
 * Created by wangqiang on 2016/10/12.
 */
public class CollectSqlHelper extends SQLiteOpenHelper {
    private String TAG = "CollectSqlHelper";
    private static final int version = 1;//数据库版本
    private static final String name = "sql";//数据库名
    private SQLiteDatabase sd;

    public CollectSqlHelper(Context context) {
        super(context, name, null, version);
    }

    public CollectSqlHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
    }

    public void setSQL(SQLiteDatabase sd) {
        this.sd = sd;
    }

    /**
     * 第一次创建数据库的时候才会执行
     *
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        if (sqLiteDatabase != null) {
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + MessageItem.keyVal.tableName + " ( "
                    + MessageItem.keyVal.title + " VARCHAR, "
                    + MessageItem.keyVal.from + " VARCHAR null, "
                    + MessageItem.keyVal.locate + " VARCHAR null, "
                    + MessageItem.keyVal.time + " VARCHAR, "
                    + MessageItem.keyVal.url + " VARCHAR unique);");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + MessageItem.keyVal.tableName);
        onCreate(sqLiteDatabase);
        sqLiteDatabase.close();
    }

    public long addCollect(String title, String from, String location, String time, String url) {
        ContentValues cv = new ContentValues();
        cv.put(MessageItem.keyVal.title, title);
        cv.put(MessageItem.keyVal.from, from);
        cv.put(MessageItem.keyVal.locate, location);
        cv.put(MessageItem.keyVal.time, time);
        cv.put(MessageItem.keyVal.url, url);
        long result = sd.insert(MessageItem.keyVal.tableName, MessageItem.keyVal.title, cv);
        return result;
    }

    public ArrayList<HashMap<String, String>> selectAll() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        Cursor cursor = sd.rawQuery("SELECT * FROM " + MessageItem.keyVal.tableName, null);
        while (cursor.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            map.put(MessageItem.keyVal.title, cursor.getString(0));
            if (cursor.getString(1) != null) {
                map.put(MessageItem.keyVal.from, cursor.getString(1));
                map.put(MessageItem.keyVal.locate, cursor.getString(2));
            }
            map.put(MessageItem.keyVal.time, cursor.getString(3));
            map.put(MessageItem.keyVal.url, cursor.getString(4));
            list.add(map);
        }
        try {
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean hasURL(String url) {
        if (url == null || url.length() == 0) return false;
        Cursor cursor = sd.rawQuery("select * from " +
                        MessageItem.keyVal.tableName + " where "
                        + MessageItem.keyVal.url + " =?",
                new String[]{url});
        Boolean has = cursor.moveToFirst();
        try {
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return has;
    }

    public void delete(String url) {
        String where = MessageItem.keyVal.url + "=?";
        String[] value = {url};
        sd.delete(MessageItem.keyVal.tableName, where, value);
        Log.e(TAG, "delete success");
    }
}
