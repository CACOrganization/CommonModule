package com.caccommonmodule.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Map;
import java.util.Set;

@SuppressWarnings("deprecation")
public class DBHelper {

    private static final int DATABASE_VERSION = 1;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public DBHelper(Context context, String dbName) {
        dbHelper = new DatabaseHelper(context, dbName);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
        db.setLockingEnabled(false);
    }

    public Cursor select(String sql) {
        return db.rawQuery(sql, null);
    }

    public void update(String sql) {
        db.execSQL(sql);
    }

    public long insert(String tableName, Map<String, String> values) {
        ContentValues insertValues = new ContentValues();
        Set<String> keys = values.keySet();
        for (String key : keys) {
            insertValues.put(key, values.get(key));
        }
        return db.insert(tableName, null, insertValues);
    }

    public void close() {
        dbHelper.close();
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context, String dbName) {
            super(context, dbName, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }

    }

}
