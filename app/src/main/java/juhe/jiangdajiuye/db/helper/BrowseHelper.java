package juhe.jiangdajiuye.db.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import juhe.jiangdajiuye.db.BrowseTable;

/**
 * class description here
 *
 * @author wangqiang
 * @since 2018-04-12
 */
public class BrowseHelper extends SQLiteOpenHelper{
    private static final int version = 1;//数据库版本
    private static final String name = "BrowseRecord";//数据库名
    public BrowseHelper(Context context) {
        super(context, name, null, version);
    }
    public BrowseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + BrowseTable.tableName + " ( "
                + BrowseTable.url + " VARCHAR);");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public boolean add(String url) {
        ContentValues cv = new ContentValues();
        cv.put(BrowseTable.url,url);
        SQLiteDatabase db = startTransaction(true) ;
        long result = db.insert(BrowseTable.tableName, BrowseTable.url, cv);
        endTransaction(db);
        return result > 0 ? true : false  ;
    }
    public boolean contain(String url) {
        if(TextUtils.isEmpty(url)){
            return false ;
        }
        SQLiteDatabase db = startTransaction(false);
        if (url == null || url.length() == 0) return false;
        Cursor cursor = db.rawQuery("select * from " +
                        BrowseTable.tableName + " where "
                        + BrowseTable.url + " =?",
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

    public void delete(String url ) {
        if(TextUtils.isEmpty(url)) {
            return;
        }
        String where = BrowseTable.url + "=?";
        String[] value = {url};
        SQLiteDatabase db = startTransaction(false);
        db.delete(BrowseTable.tableName, where, value);
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
