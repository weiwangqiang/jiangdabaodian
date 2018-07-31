package juhe.jiangdajiuye.db.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import juhe.jiangdajiuye.bean.BookBean;
import juhe.jiangdajiuye.db.LibraryTable;

/**
 * 使用数据库要开启事务
 * Created by wangqiang on 2016/10/12.
 */
public class LibrarySqlHelper extends SQLiteOpenHelper {
    private String TAG = "LibrarySqlHelper";
    private static final int version = 1;//数据库版本
    private static final String name = "Library";//数据库名

    public LibrarySqlHelper(Context context) {
        super(context, name, null, version);
    }

    public LibrarySqlHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
    }

    /**
     * 第一次创建数据库的时候才会执行
     *
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + LibraryTable.tableName + " ( "
                + LibraryTable.book + " VARCHAR, "
                + LibraryTable.editor + " VARCHAR  , "
                + LibraryTable.available + " VARCHAR , "
                + LibraryTable.number + " VARCHAR, "
                + LibraryTable.url + " VARCHAR unique);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        sqLiteDatabase.execSQL("drop table if exists "+ LibraryTable.tableName);
//        sqLiteDatabase.close();
    }

    public boolean add(BookBean bookBean) {
        ContentValues cv = new ContentValues();
        cv.put(LibraryTable.book, bookBean.getBook());
        cv.put(LibraryTable.editor, bookBean.getEditor());
        cv.put(LibraryTable.available, bookBean.getAvailable());
        cv.put(LibraryTable.number, bookBean.getNumber());
        cv.put(LibraryTable.url, bookBean.getUrl());
        SQLiteDatabase db = startTransaction(true);
        try {
            long result = db.insert(LibraryTable.tableName, LibraryTable.book, cv);
            return result > 0 ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            endTransaction(db);
        }
        return false;
    }


    public boolean add(List<BookBean> data) {
        SQLiteDatabase db = startTransaction(true);
        for (BookBean bookBean : data) {
            ContentValues cv = new ContentValues();
            cv.put(LibraryTable.book, bookBean.getBook());
            cv.put(LibraryTable.editor, bookBean.getEditor());
            cv.put(LibraryTable.available, bookBean.getAvailable());
            cv.put(LibraryTable.number, bookBean.getNumber());
            cv.put(LibraryTable.url, bookBean.getUrl());
            db.insert(LibraryTable.tableName, LibraryTable.book, cv);
        }
        endTransaction(db);
        return true;
    }

    public ArrayList<BookBean> selectAll() {
        ArrayList<BookBean> list = new ArrayList<>();
        SQLiteDatabase db = startTransaction(false);
        Cursor cursor = db.rawQuery("SELECT * FROM " + LibraryTable.tableName, null);
        while (cursor.moveToNext()) {
            BookBean bookBean = new BookBean();
            bookBean.setBook(cursor.getString(0));
            if (cursor.getString(1) != null) {
                bookBean.setEditor(cursor.getString(1));
                bookBean.setAvailable(cursor.getString(2));
            }
            bookBean.setNumber(cursor.getString(3));
            bookBean.setUrl(cursor.getString(4));
            list.add(bookBean);
        }
        try {
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        endTransaction(db);
        return list;
    }

    @NonNull
    private SQLiteDatabase startTransaction(boolean writable) {
        SQLiteDatabase db;
        if (writable) {
            db = getWritableDatabase();
        } else {
            db = getReadableDatabase();
        }
        db.beginTransaction(); //使用事务
        return db;
    }

    private void endTransaction(SQLiteDatabase db) {
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public boolean hasURL(String url) {
        SQLiteDatabase db = startTransaction(false);
        Cursor cursor = db.rawQuery("select * from " +
                        LibraryTable.tableName + " where "
                        + LibraryTable.url + " =? ",
                new String[]{url});
        Boolean has = false;
        try {
            has = cursor.moveToFirst();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        endTransaction(db);
        return has;
    }

    public void delete(BookBean bookBean) {
        String where = LibraryTable.url + " =? ";
        String[] value = {bookBean.getUrl()};
        try {
            getWritableDatabase().delete(LibraryTable.tableName, where, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
