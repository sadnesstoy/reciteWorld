package com.example.reciteword;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class reciteFragment extends Fragment {

    private Button knowButton, unknowButton;
    private ImageButton tipsButton;
    private TextView wordText, definitionText;
    private boolean isFirstUnknowClick = true;  // 用于标记第一次点击 unknowButton
    private boolean isFirstClick = true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recite, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        knowButton = (Button) getActivity().findViewById(R.id.knowButton);
        unknowButton = (Button) getActivity().findViewById(R.id.unknowButton);
        tipsButton = (ImageButton) getActivity().findViewById(R.id.tipsButton);
        wordText = (TextView) getActivity().findViewById(R.id.wordText);
        definitionText = (TextView) getActivity().findViewById(R.id.definitionText);
        super.onActivityCreated(savedInstanceState);

        final SharedPreferences sharedPre = getActivity().getSharedPreferences("t", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPre.edit();

        // 设置初始单词
        wordText.setText(Data.getWord(Data.getRandNum()));
        definitionText.setText("");

        final int[] wrongNum = {sharedPre.getInt("wrongNum", 0)};
        int cnt = sharedPre.getInt("word" + Data.getRandNum(), 0);
        cnt++;
        editor.putInt("word" + Data.getRandNum(), cnt);
        editor.apply();

        // 点击知道按钮
        knowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFirstClick) {
                    // 第一次点击，显示下一个单词并修改按钮文本
                    definitionText.setText(""); // 清空定义
                    definitionText.setText(Data.getPron(Data.getRandNum()) + "\n" + Data.getwordDefine(Data.getRandNum()));
                    knowButton.setText("下一词"); // 修改按钮文本
                    unknowButton.setText("记错了"); // 修改按钮文本
                    isFirstClick = false; // 设置标志位为 false
                } else {
                    // 第二次点击，显示下一个单词
                    definitionText.setText(""); // 清空定义
                    knowButton.setText("认识"); // 修改按钮文本
                    unknowButton.setText("不认识"); // 修改按钮文本
                    Data.setRandNum();
                    wordText.setText(Data.getWord(Data.getRandNum()));
                    isFirstClick = true;  // 重置标志位
                    // 更新单词的次数
                    int cnt = sharedPre.getInt("word" + Data.getRandNum(), 0);
                    cnt++;
                    editor.putInt("word" + Data.getRandNum(), cnt);
                    editor.apply();
                }

            }
        });

        // 点击不懂按钮
        unknowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFirstClick) {
                    // 第一次点击，显示单词的定义和发音
                    knowButton.setText("下一词"); // 修改按钮文本
                    unknowButton.setText("下一词"); // 修改按钮文本
                    definitionText.setText(Data.getPron(Data.getRandNum()) + "\n" + Data.getwordDefine(Data.getRandNum()));
                    isFirstClick = false;
                } else {
                    // 第二次点击，跳转到下一个单词
                    definitionText.setText(""); // 清空定义
                    knowButton.setText("认识"); // 修改按钮文本
                    unknowButton.setText("不认识"); // 修改按钮文本
                    wrongNum[0]++;
                    editor.putInt("wrongNum", wrongNum[0]);
                    editor.putInt("wrong" + wrongNum[0], Data.getRandNum());
                    Data.setRandNum();
                    int cnt = sharedPre.getInt("word" + Data.getRandNum(), 0);
                    cnt++;
                    editor.putInt("word" + Data.getRandNum(), cnt);
                    editor.apply();
                    definitionText.setText("");
                    wordText.setText(Data.getWord(Data.getRandNum()));
                    isFirstClick = true;  // 重置标志位
                }
            }
        });

        // 显示提示按钮
        tipsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                definitionText.setText(Data.getPron(Data.getRandNum()) + "\n" + Data.getwordDefine(Data.getRandNum()));
            }
        });
    }
}
