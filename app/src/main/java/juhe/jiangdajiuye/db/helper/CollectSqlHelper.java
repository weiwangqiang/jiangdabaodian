package juhe.jiangdajiuye.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.bean.MessageBean;

/**
 * Created by wangqiang on 2016/10/12.
 *
 * 使用数据库要开启事务
 */
public class CollectSqlHelper extends SQLiteOpenHelper {
    private String TAG = "CollectSqlHelper";
    private static final int version = 1;//数据库版本
    private static final String name = "sql";//数据库名
    private String add = "";
    public CollectSqlHelper(Context context) {
        super(context, name, null, version);
    }

    public CollectSqlHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
    }


    /**
     * 第一次创建数据库的时候才会执行
     *
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        if (sqLiteDatabase != null) {
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + MessageBean.keyVal.tableName + " ( "
                    + MessageBean.keyVal.title + " VARCHAR, "
                    + MessageBean.keyVal.from + " VARCHAR null, "
                    + MessageBean.keyVal.locate + " VARCHAR null, "
                    + MessageBean.keyVal.time + " VARCHAR, "
                    + MessageBean.keyVal.url + " VARCHAR unique);");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        sqLiteDatabase.execSQL("alter table " + MessageBean.keyVal.tableName+" add "+"列名 verchar(20)");
//        onCreate(sqLiteDatabase);
//        sqLiteDatabase.close();
    }

    public boolean add(MessageBean messageBean) {
        ContentValues cv = new ContentValues();
        cv.put(MessageBean.keyVal.title, messageBean.getTitle());
        cv.put(MessageBean.keyVal.from, messageBean.getFrom());
        cv.put(MessageBean.keyVal.locate, messageBean.getLocate());
        cv.put(MessageBean.keyVal.time, messageBean.getTime());
        cv.put(MessageBean.keyVal.url, messageBean.getUrl());
        SQLiteDatabase db = startTransaction(true) ;
        long result = db.insert(MessageBean.keyVal.tableName, MessageBean.keyVal.title, cv);
        endTransaction(db);
        return result > 0 ? true : false  ;
    }

    public boolean add(List<MessageBean> data){
        for(MessageBean item : data ){
            add(item);
        }
        return true ;
    }
    public List<MessageBean> selectAll() {
        ArrayList<MessageBean> list = new ArrayList<>();
        SQLiteDatabase db = startTransaction(false);
        Cursor cursor = db.rawQuery("SELECT * FROM " + MessageBean.keyVal.tableName, null);
        while (cursor.moveToNext()) {
            MessageBean messageBean = new MessageBean() ;
            messageBean.setTitle(cursor.getString(0));
            if (cursor.getString(1) != null) {
                messageBean.setFrom(cursor.getString(1));
                messageBean.setLocate(cursor.getString(2));
            }
            messageBean.setTime(cursor.getString(3));
            messageBean.setUrl(cursor.getString(4));
            list.add(messageBean);
        }
        try {
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        endTransaction(db);
        return list;
    }

    public boolean contain(MessageBean messageBean) {
        if(null == messageBean) return false ;
        SQLiteDatabase db = startTransaction(false);
        String url = messageBean.getUrl() ;

        if (url == null || url.length() == 0) return false;
        Cursor cursor = db.rawQuery("select * from " +
                        MessageBean.keyVal.tableName + " where "
                        + MessageBean.keyVal.url + " =?",
                new String[]{url});
        Boolean has = cursor.moveToFirst();
        try {
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        endTransaction(db);
        return has;
    }

    public void delete(MessageBean messageBean) {
        if(null == messageBean) return;
        String where = MessageBean.keyVal.url + "=?";
        String[] value = {messageBean.getUrl()};
        SQLiteDatabase db = startTransaction(false);
        db.delete(MessageBean.keyVal.tableName, where, value);
        endTransaction(db);
    }
    @NonNull
    private SQLiteDatabase startTransaction(boolean writable) {
        SQLiteDatabase db ;
        if(writable){
            db = getWritableDatabase() ;
        }else{
            db = getReadableDatabase();
        }
        db.beginTransaction(); //使用事务
        return db;
    }
    private void endTransaction(SQLiteDatabase db) {
        db.setTransactionSuccessful();
        db.endTransaction();
    }
}
