package com.example.reciteword;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    // 从 assets 文件夹中读取 words.txt 文件内容并解析
    public static List<Word> readWordsFromFile(Context context) {
        List<Word> wordList = new ArrayList<>();
        AssetManager assetManager = context.getAssets();

        try {
            // 读取 assets 文件夹中的 words.txt 文件
            BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open("word.txt")));
            String line;
            while ((line = reader.readLine()) != null) {
                // 假设每行数据是：单词 发音 定义
                String[] parts = line.split(" ", 3); // 按照空格分割
                if (parts.length == 3) {
                    String word = parts[0];
                    String pron = parts[1];
                    String definition = parts[2];
                    Word wordObj = new Word(word, pron, definition, 0, 0); // 默认显示次数和标志为0
                    wordList.add(wordObj);
                }
            }
            reader.close();
        } catch (IOException e) {
            Log.e("FileUtils", "Error reading file", e);
        }

        return wordList;
    }
}
