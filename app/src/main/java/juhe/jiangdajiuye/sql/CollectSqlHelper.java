package juhe.jiangdajiuye.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wangqiang on 2016/10/12.
 */
public class CollectSqlHelper extends SQLiteOpenHelper{
    private String TAG = "CollectSqlHelper";
    private static final int version = 1;//数据库版本
    private static final String name = "sql";//数据库名
    private SQLiteDatabase sd;
    public CollectSqlHelper(Context context){
        super(context, name, null, version);
    }
    public CollectSqlHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+ collectTable.tableName+" ( "
                    + collectTable.title+" VARCHAR, "
                    + collectTable.company + " VARCHAR null, "
                    + collectTable.location +" VARCHAR null, "
                    + collectTable.time + " VARCHAR, "
                    + collectTable.url + " VARCHAR unique);");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("drop table if exists "+ collectTable.tableName);
        onCreate(sqLiteDatabase);
        sqLiteDatabase.close();
    }
   public long addCollect(String title, String company,String location,String time,String url){
            ContentValues cv = new ContentValues();
        cv.put(collectTable.title,title);
        cv.put(collectTable.company,company);
        cv.put(collectTable.location,location);
        cv.put(collectTable.time,time);
        cv.put(collectTable.url,url);
       long result = sd.insert(collectTable.tableName, collectTable.title,cv);
       return result;
    }
    public ArrayList<HashMap<String,String>> selectAll(){
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        Cursor cursor =  sd.rawQuery("SELECT * FROM "+ collectTable.tableName,null);
        while(cursor.moveToNext()){
            HashMap<String,String> map =  new HashMap<>();
            map.put("title",cursor.getString(0));
            if(cursor.getString(1)!=null){
                map.put("company",cursor.getString(1));
                map.put("location",cursor.getString(2));
            }
            map.put("time",cursor.getString(3));
            map.put("url",cursor.getString(4));
            list.add(map);
        }
        try{
            cursor.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
    public boolean hasURL(String url){
        if(url == null) return false;
        Cursor cursor = sd.rawQuery("select * from "+
                collectTable.tableName+" where "
                + collectTable.url+" =?",
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
        String where = collectTable.url+"=?";
        String[] value = {url};
        sd.delete(collectTable.tableName,where,value);
        Log.e(TAG,"delete success");
    }
}
