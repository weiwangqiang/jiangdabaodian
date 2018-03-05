package juhe.jiangdajiuye.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.bean.MessageItem;

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
//        sqLiteDatabase.execSQL("alter table " + MessageItem.keyVal.tableName+" add "+"列名 verchar(20)");
//        onCreate(sqLiteDatabase);
//        sqLiteDatabase.close();
    }

    public boolean add(MessageItem messageItem ) {
        ContentValues cv = new ContentValues();
        cv.put(MessageItem.keyVal.title, messageItem.getTitle());
        cv.put(MessageItem.keyVal.from, messageItem.getFrom());
        cv.put(MessageItem.keyVal.locate, messageItem.getLocate());
        cv.put(MessageItem.keyVal.time, messageItem.getTime());
        cv.put(MessageItem.keyVal.url, messageItem.getUrl());
        SQLiteDatabase db = startTransaction(true) ;
        long result = db.insert(MessageItem.keyVal.tableName, MessageItem.keyVal.title, cv);
        endTransaction(db);
        return result > 0 ? true : false  ;
    }

    public boolean add(List<MessageItem> data){
        for(MessageItem item : data ){
            add(item);
        }
        return true ;
    }
    public List<MessageItem> selectAll() {
        ArrayList<MessageItem> list = new ArrayList<>();
        SQLiteDatabase db = startTransaction(false);
        Cursor cursor = db.rawQuery("SELECT * FROM " + MessageItem.keyVal.tableName, null);
        while (cursor.moveToNext()) {
            MessageItem messageItem = new MessageItem() ;
            messageItem.setTitle(cursor.getString(0));
            if (cursor.getString(1) != null) {
                messageItem.setFrom(cursor.getString(1));
                messageItem.setLocate(cursor.getString(2));
            }
            messageItem.setTime(cursor.getString(3));
            messageItem.setUrl(cursor.getString(4));
            list.add(messageItem);
        }
        try {
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        endTransaction(db);
        return list;
    }

    public boolean contain(MessageItem messageItem) {
        if(null == messageItem) return false ;
        SQLiteDatabase db = startTransaction(false);
        String url = messageItem.getUrl() ;

        if (url == null || url.length() == 0) return false;
        Cursor cursor = db.rawQuery("select * from " +
                        MessageItem.keyVal.tableName + " where "
                        + MessageItem.keyVal.url + " =?",
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

    public void delete(MessageItem messageItem) {
        if(null == messageItem) return;
        String where = MessageItem.keyVal.url + "=?";
        String[] value = {messageItem.getUrl()};
        SQLiteDatabase db = startTransaction(false);
        db.delete(MessageItem.keyVal.tableName, where, value);
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
