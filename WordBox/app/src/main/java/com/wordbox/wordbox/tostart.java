package com.wordbox.wordbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import com.wordbox.wordbox.R;


public class tostart extends AppCompatActivity {


    public SharedPreferences ses_kontrol;
    int ses_kontrol_verisi;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_ekran_deneme);
        //Gece modunu kapatma:
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);


        //Ses için kontrol verisini alma;
        //Sayaç verisini alma:
        ses_kontrol = getSharedPreferences("ses_kontrol", MODE_PRIVATE);
        ses_kontrol_verisi = ses_kontrol.getInt("ses_kontrol1", 1);    }

    public void start_word(View view){

        if(ses_kontrol_verisi==1){
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.grammer_open);
            mediaPlayer.start();
        }

        Intent go = new Intent(tostart.this,MainActivity.class);
        startActivity(go);
        finish();
        System.out.println("hata yok");


    }


    public void start_dictionary(View view){
        if(ses_kontrol_verisi==1){
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.grammer_open);
            mediaPlayer.start();
        }
        Intent go = new Intent(tostart.this,WordBox_dictionary.class);
        startActivity(go);
        finish();
        System.out.println("hata yok");


    }


    public void start_settings(View view){
        if(ses_kontrol_verisi==1){
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.grammer_open);
            mediaPlayer.start();
        }

        System.out.println("basıldı");
        Intent go = new Intent(tostart.this,WordBox_settings.class);
        startActivity(go);
        finish();
        System.out.println("hata yok");
    }

}