package com.example.reciteword;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Data {
    private static List<Word> wordList = new ArrayList<>();
    private static WordRepository wordRepository;
    /*单词清单*/

    // 初始化方法，接受一个 Context 以便使用数据库
    public static void initWordList(Context context) {
        wordRepository = new WordRepository(context);
        wordList = wordRepository.getAllWords();  // 从数据库加载所有单词
    }


    private static int numCount = 0,randNum = getNum(wordList.size()+1);
    public static void setNumCount(int numCount){
        Data.numCount = numCount;
    }

    public static void setRandNum(){
        Data.randNum = getNum(wordList.size()+1);
    }

    public static int getNumCount(){return numCount;}
    public static int getRandNum(){return randNum;}
    public static String getWord(int cnt){
        return wordList.get(cnt).getWord();
    }
    public static String getPron(int cnt){
        return wordList.get(cnt).getPron();
    }
    public static String getwordDefine(int cnt){
        return wordList.get(cnt).getDefinition();
    }
    public static int getShowNum(int cnt){
        return wordList.get(cnt).getShowNum();
    }

    private static int getNum(int endNum){
        if(endNum > 0){
            Random random = new Random();
            return random.nextInt(endNum);
        }
        return 0;
    }
}
