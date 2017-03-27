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

/**
 * Created by wangqiang on 2016/10/12.
 */
public class LibrarySqlHelper extends SQLiteOpenHelper{
    private String TAG = "LibrarySqlHelper";
    private static final int version = 1;//数据库版本
    private static final String name = "library";//数据库名
    private SQLiteDatabase sd;
    public LibrarySqlHelper(Context context){
        super(context, name, null, version);
    }
    public LibrarySqlHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
    }
    public void setSQL(SQLiteDatabase sd){
        this.sd = sd;
    }

    /**
     * 第一次创建数据库的时候才会执行
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.e("sql","sql is created");
        if(sqLiteDatabase!=null){
            Log.e("sql","sql is not null");
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+ librarytable.tableName+" ( "
                    + librarytable.book+" VARCHAR, "
                    + librarytable.editor + " VARCHAR null, "
                    + librarytable.available +" VARCHAR null, "
                    + librarytable.number + " VARCHAR, "
                    + librarytable.url + " VARCHAR unique);");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+ librarytable.tableName);
        sqLiteDatabase.close();
    }
   public long addCollect(String book, String editor,String available,String number,String url){
        ContentValues cv = new ContentValues();
        cv.put(librarytable.book,book);
        cv.put(librarytable.editor,editor);
        cv.put(librarytable.available,available);
        cv.put(librarytable.number,number);
        cv.put(librarytable.url,url);
//       sb.execSQL("insert into person("+name, age+") values(?,?)", new Object[]{"炸死特", 4});
       long result = sd.insert(librarytable.tableName, librarytable.book,cv);
       return result;
    }
    public ArrayList<Map<String,String>> selectAll(){
        ArrayList<Map<String,String>> list = new ArrayList<>();
        Cursor cursor =  sd.rawQuery("SELECT * FROM "+ librarytable.tableName,null);
        while(cursor.moveToNext()){
            HashMap<String,String> map =  new HashMap<>();
            map.put("book",cursor.getString(0));
            if(cursor.getString(1)!=null){
                map.put("editor",cursor.getString(1));
                map.put("available",cursor.getString(2));
            }
            map.put("number",cursor.getString(3));
            map.put("url",cursor.getString(4));
            list.add(map);
            Log.e("LibrarySqlHelper"," book is "+map.get("book")+" editor is "+map.get("editor"));
        }
        try{
            cursor.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
    public boolean hasURL(String url){
        Cursor cursor = sd.rawQuery("select * from "+
                librarytable.tableName+" where "
                + librarytable.url+" =?",
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
        String where = librarytable.url+"=?";
        String[] value = {url};
        sd.delete(librarytable.tableName,where,value);
        Log.e(TAG,"delete success");
    }
}
