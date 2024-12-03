package com.example.reciteword;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "word_db"; // 数据库名称
    private static final int DATABASE_VERSION = 1; // 数据库版本

    // 构造函数
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建表结构
        String createTableQuery = "CREATE TABLE words (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "word TEXT, " +
                "pron TEXT, " +
                "definition TEXT, " +
                "showNum INTEGER, " +
                "flag INTEGER" +
                ");";
        db.execSQL(createTableQuery); // 执行 SQL 创建表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 如果数据库升级，会删除旧表并创建新表
        db.execSQL("DROP TABLE IF EXISTS words");
        onCreate(db);
    }
}
