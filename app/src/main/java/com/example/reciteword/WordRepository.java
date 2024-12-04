package com.example.reciteword;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class WordRepository {

    private DBHelper dbHelper;

    public WordRepository(Context context) {
        dbHelper = new DBHelper(context);
    }

    // 插入单词数据
    public long insertWord(Word word) {
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // 获取可写数据库
        ContentValues values = new ContentValues();
        values.put("word", word.getWord());
        values.put("pron", word.getPron());
        values.put("definition", word.getDefinition());
        values.put("showNum", word.getShowNum());
        values.put("flag", word.getFlag());

        long id = db.insert("words", null, values); // 插入数据，返回插入的行 ID
        db.close();
        return id;
    }

    // 获取所有单词
    public List<Word> getAllWords() {
        List<Word> wordList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("words", null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String word = cursor.getString(cursor.getColumnIndex("word"));
                String pron = cursor.getString(cursor.getColumnIndex("pron"));
                String definition = cursor.getString(cursor.getColumnIndex("definition"));
                int showNum = cursor.getInt(cursor.getColumnIndex("showNum"));
                int flag = cursor.getInt(cursor.getColumnIndex("flag"));

                Word w = new Word(word, pron, definition, showNum, flag);
                wordList.add(w);
            }
            cursor.close();
        }
        db.close();
        return wordList;
    }

    // 清空数据库
    public void clearDatabase() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM words"); // 删除所有单词数据
        db.close();
    }

    // 从文件加载数据并插入数据库
    public void loadWordsFromFileAndInsert(Context context) {
        List<Word> words = FileUtils.readWordsFromFile(context);
        for (Word word : words) {
            insertWord(word);
        }
    }
}