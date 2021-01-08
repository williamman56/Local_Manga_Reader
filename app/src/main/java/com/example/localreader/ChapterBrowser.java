package com.example.localreader;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.io.File;
import java.util.Arrays;

public class ChapterBrowser extends AppCompatActivity {
    private float[] chap_numbers;
    private float current_chapter;
    private String current_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_browser);

        Bundle param = getIntent().getExtras();
        current_path = param.getString("pathname");

        TableLayout chap_list = (TableLayout)findViewById(R.id.chapter_list);

        File directory = new File(current_path);
        File[] chapters = directory.listFiles();
        chap_numbers = getChapterNumbers(chapters, numOfChapters(chapters));
        int count = 0;
        TableRow tr = null;
        for (float chap_num : chap_numbers) {
            if (count == 0) {
                tr = new TableRow(this);
                tr.setGravity(Gravity.CENTER_HORIZONTAL);
                chap_list.addView(tr);
            }
            Button chap_but = new Button(this);
            chap_but.setText(chapFromFloat(chap_num));
            chap_but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button b = (Button) v;
                    current_chapter = Float.parseFloat(b.getText().toString());
                    readChapter(current_path, b.getText().toString());
                }
            });

            tr.addView(chap_but);
            count = (count + 1) % 4;
        }

        getSupportActionBar().setTitle(directory.getName());
    }

    private String chapFromFloat(float chapNum) {
        String chap_title = String.valueOf(chapNum);
        if (chap_title.charAt(chap_title.length()-1) == '0') {
            chap_title = chap_title.substring(0, chap_title.indexOf('.'));
        }
        return chap_title;
    }

    private float[] getChapterNumbers(File[] chapters, int num) {
        float[] numbers = new float[num];

        int count = 0;
        for (File chapter : chapters) {
            if (chapter.isDirectory()) {
                numbers[count] = Float.parseFloat(chapter.getName());
                ++count;
            }
        }

        Arrays.sort(numbers);

        return numbers;
    }

    private void readChapter(String pathname, String chapterName) {
        String chapterPath = pathname + "/" + chapterName;
        Intent intent = new Intent(this, ChapterReader.class);
        intent.putExtra("pathname", chapterPath);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            int index = Arrays.binarySearch(chap_numbers, current_chapter);
            ++index;
            if (index < chap_numbers.length) {
                current_chapter = chap_numbers[index];
                readChapter(current_path, chapFromFloat(chap_numbers[index]));
            }
        }
        else if (resultCode == 2) {
            int index = Arrays.binarySearch(chap_numbers, current_chapter);
            --index;
            if (index >= 0) {
                current_chapter = chap_numbers[index];
                readChapter(current_path, chapFromFloat(chap_numbers[index]));
            }
        }
    }

    private int numOfChapters(File[] chapters) {
        int count = 0;
        for (File chapter : chapters) {
            if (chapter.isDirectory()) {
                ++count;
            }
        }
        return count;
    }
}
