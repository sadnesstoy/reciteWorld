package com.example.reciteword;

import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    private WordRepository wordRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wordRepository = new WordRepository(this);
        // 如果数据库为空，重新加载数据
        if (wordRepository.isDatabaseEmpty()) {
            // 从文件读取单词数据并插入到数据库
            List<Word> wordList = FileUtils.readWordsFromFile(this, "word.txt");
            for (Word word : wordList) {
                wordRepository.insertWord(word);
            }
            Data.initWordList(this);  // 初始化数据
        }
        Intent intent = new Intent(MainActivity.this,InterfaceActivity.class);
        startActivity(intent);
    }
}