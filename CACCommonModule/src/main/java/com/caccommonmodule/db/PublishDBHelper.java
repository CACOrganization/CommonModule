package com.caccommonmodule.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Map;
import java.util.Set;

//當SearchJob 有多加欄位 DATABASE_VERSION 要＋１
public class PublishDBHelper extends SQLiteOpenHelper {

    private static SQLiteDatabase database;
    private final static String DATABASE_NAME = "cacpublish.db";
    private final static int DATABASE_VERSION = 1;
    public final static String TBL_RECORD_JOBLIST = "notice";
    private final static String TABLE_NAME = TBL_RECORD_JOBLIST;

    private Context mContext;
    private static PublishDBHelper mLocalDBHelper;

    private final String INIT_TABLE =
            "CREATE TABLE " + TABLE_NAME +  " (f0 TEXT, bulletin_version NUMERIC, banner NUMERIC, next_show_time TEXT, title TEXT, show_type NUMERIC, button_link2 TEXT, button_name2 TEXT, button_link1 TEXT, button_name1 TEXT, login_btn NUMERIC, x3_y3 NUMERIC, version TEXT, message TEXT, start TEXT, end TEXT, x1_y1 NUMERIC, x2_y1 NUMERIC, x3_y1 NUMERIC, x1_y2 NUMERIC, x2_y2 NUMERIC, x3_y2 NUMERIC, x1_y3 NUMERIC, x2_y3 NUMERIC" +
                    ", f1 TEXT, f2 TEXT, f3 TEXT, f4 TEXT, f5 TEXT, f6 TEXT, f7 TEXT, f8 TEXT, f9 TEXT, f10 TEXT" +
                    ", f11 TEXT, f12 TEXT, f13 TEXT, f14 TEXT, f15 TEXT, f16 TEXT, f17 TEXT, f18 TEXT, f19 TEXT, f20 TEXT" +
                    ");";

    private final String INSERT_TABLE =
            "INSERT INTO `notice` VALUES (NULL,0,1,'201201010000','公告',1,NULL,NULL,NULL,'確定',1,1,'1.0.3','您好：104人力銀行於2012/1/7(六)進行資料庫維護更新作業，00:00~06:00全站將暫停服務，造成不便深感抱歉。','201201060000','201201070600',1,1,1,1,1,1,1,1" +
                    ",1,1,1,1,1,1,1,1,1,1" +
                    ",1,1,1,1,1,1,1,1,1,1" +
                    ");";

    public PublishDBHelper(Context context, String name, CursorFactory factory,
                           int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        final String INIT_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + query()
//                + ");";
//        final String INIT_TABLE =
//        "CREATE TABLE notice (f0 TEXT, bulletin_version NUMERIC, banner NUMERIC, next_show_time TEXT, title TEXT, show_type NUMERIC, button_link2 TEXT, button_name2 TEXT, button_link1 TEXT, button_name1 TEXT, login_btn NUMERIC, x3_y3 NUMERIC, version TEXT, message TEXT, start TEXT, end TEXT, x1_y1 NUMERIC, x2_y1 NUMERIC, x3_y1 NUMERIC, x1_y2 NUMERIC, x2_y2 NUMERIC, x3_y2 NUMERIC, x1_y3 NUMERIC, x2_y3 NUMERIC);"
//        + "INSERT INTO `notice` VALUES (NULL,0,1,'201201010000','公告',1,NULL,NULL,NULL,'確定',1,1,'1.0.3','您好：104人力銀行於2012/1/7(六)進行資料庫維護更新作業，00:00~06:00全站將暫停服務，造成不便深感抱歉。','201201060000','201201070600',1,1,1,1,1,1,1,1);"
        try{
            db.execSQL(INIT_TABLE);
            Log.e("PublishDBHelper", "table created ");
            db.execSQL(INSERT_TABLE);
        } catch (Exception e) {
            Log.e("PublishDBHelper", e.toString());
//            Toast.makeText(mContext, "ERROR " + e.toString(), Toast.LENGTH_LONG).show();
        }


    }

    // 需要資料庫的元件呼叫這個方法，這個方法在一般的應用都不需要修改
    public static SQLiteDatabase getDatabase() {
        if (database == null || !database.isOpen()) {
            database = mLocalDBHelper.getWritableDatabase();
        }

        return database;
    }

    public static PublishDBHelper getInstance(Context context) {
        if (mLocalDBHelper == null) {
            mLocalDBHelper = new PublishDBHelper(context, DATABASE_NAME, null,
                    DATABASE_VERSION);
        }
        database = getDatabase();
        return mLocalDBHelper;
    }

    public long insert(String tableName, Map<String, String> values) {
        ContentValues insertValues = new ContentValues();
        Set<String> keys = values.keySet();
        for (String key : keys) {
            insertValues.put(key, values.get(key));
        }

        long result = database.insert(tableName, null, insertValues);
//        database.close();

        return result;
    }

    public void update(String sql) {
        database.execSQL(sql);
//        database.close();
    }

    public Cursor select(String sql) {
        Cursor cursor = database.rawQuery(sql, null);
//        database.close();
        return cursor;
    }

    public void close(){
        if(database != null && database.isOpen())
            database.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        mContext.deleteDatabase(TABLE_NAME);
        onCreate(db);
    }

}