package juhe.jiangdajiuye.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import juhe.jiangdajiuye.bean.BookBean;

/**
 * Created by wangqiang on 2016/10/12.
 */
public class LibrarySqlHelper extends SQLiteOpenHelper{
    private String TAG = "LibrarySqlHelper";
    private static final int version = 1;//数据库版本
    private static final String name = "Library";//数据库名
    public LibrarySqlHelper(Context context){
        super(context, name, null, version);
    }
    public LibrarySqlHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
    }

    /**
     * 第一次创建数据库的时候才会执行
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        if(sqLiteDatabase!=null){
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+ LibraryTable.tableName+" ( "
                    + LibraryTable.book+" VARCHAR, "
                    + LibraryTable.editor + " VARCHAR  , "
                    + LibraryTable.available +" VARCHAR , "
                    + LibraryTable.number + " VARCHAR, "
                    + LibraryTable.url + " VARCHAR unique);");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+ LibraryTable.tableName);
        sqLiteDatabase.close();
    }
   public long addCollect(BookBean bookBean){
        ContentValues cv = new ContentValues();
        cv.put(LibraryTable.book,bookBean.getBook());
        cv.put(LibraryTable.editor,bookBean.getEditor());
        cv.put(LibraryTable.available,bookBean.getAvailable());
        cv.put(LibraryTable.number,bookBean.getNumber());
        cv.put(LibraryTable.url,bookBean.getUrl());
       long result = getWritableDatabase().insert(LibraryTable.tableName, LibraryTable.book,cv);
       return result;
    }
    public ArrayList<BookBean> selectAll(){
        ArrayList<BookBean> list = new ArrayList<>();
        Cursor cursor =  getReadableDatabase().rawQuery("SELECT * FROM "+ LibraryTable.tableName,null);
        while(cursor.moveToNext()){
           BookBean bookBean = new BookBean();
            bookBean.setBook(cursor.getString(0));
            if(cursor.getString(1)!=null){
                bookBean.setEditor(cursor.getString(1));
                bookBean.setAvailable(cursor.getString(2));
            }
            bookBean.setNumber(cursor.getString(3));
            bookBean.setUrl(cursor.getString(4));
            list.add(bookBean);
        }
        try{
            cursor.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
    public boolean hasURL(String url){
        Cursor cursor = getReadableDatabase().rawQuery("select * from "+
                LibraryTable.tableName+" where "
                + LibraryTable.url+"=?",
                new String[]{url});
        Boolean has = cursor.moveToFirst();
        try{
            cursor.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return has;
    }

    public void delete(String url){
        String where = LibraryTable.url+"=?";
        String[] value = {url};
        getWritableDatabase().delete(LibraryTable.tableName,where,value);
    }
}
