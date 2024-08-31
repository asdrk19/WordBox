package com.wordbox.wordbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.wordbox.wordbox.R;


public class splash extends AppCompatActivity {
    public SharedPreferences sp;
    public SharedPreferences.Editor sp_edit;
    public int sayac=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        //Gece modunu kapatma:
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sp=getSharedPreferences("foronboarding",MODE_PRIVATE);
        sayac=sp.getInt("data",0);

        Handler h=new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sayac==0){
                    System.out.println("onboard1");
                    Intent go = new Intent(splash.this,onboarding1.class);
                    startActivity(go);
                    finish();
                    sp_edit=sp.edit();
                    sp_edit.putInt("data",1);
                    sp_edit.apply();
                }
                else {
                System.out.println("onboard2");
                Intent go = new Intent(splash.this,tostart.class);
                startActivity(go);
                finish();
                System.out.println("hata yok");
            }}
        },3000);
    }
}