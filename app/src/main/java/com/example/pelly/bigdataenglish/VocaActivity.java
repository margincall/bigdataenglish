package com.example.pelly.bigdataenglish;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;

public class VocaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca);

        //db
        VocaDatabase db_helper = VocaDatabase.getInstance(this);
        try {
            db_helper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("d", "IntroAct. : db creating error");
        }

        VocaDao vocaDao = new VocaDao(getApplicationContext());

        TextView vocaWord = (TextView) findViewById(R.id.vocaWord);
        TextView vocaPronunciation = (TextView) findViewById(R.id.vocaPronunciation);
        TextView vocaMeaning = (TextView) findViewById(R.id.vocaMeaning);

        vocaWord.setText(vocaDao.getString("word","").get(0));
        vocaPronunciation.setText(vocaDao.getString("pronunciation","").get(0));
        vocaMeaning.setText(vocaDao.getString("meaning","").get(0));
    }
}
