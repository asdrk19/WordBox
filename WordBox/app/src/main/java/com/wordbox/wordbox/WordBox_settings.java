package com.wordbox.wordbox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.wordbox.wordbox.R;

public class WordBox_settings extends AppCompatActivity {
    public SharedPreferences kontrol_al;
    int ses_kontrol;
    public SharedPreferences.Editor kontrol_gonder;
    ImageButton geridon;
    Drawable drawable_button,drawable_button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_box_settings);
        View decorView = getWindow().getDecorView();
        //Gece modunu kapatma:
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        geridon=findViewById(R.id.imageButton14);
        //Geri dönme butonu arkaplanları:
        drawable_button=getResources().getDrawable(R.drawable.rounded_button);
        drawable_button2=getResources().getDrawable(R.drawable.rounded_button2);

        Switch mySwitch = findViewById(R.id.switch1);
        kontrol_al = getSharedPreferences("ses_kontrol", MODE_PRIVATE);
        ses_kontrol = kontrol_al.getInt("ses_kontrol1", 1);
        mySwitch.setChecked(ses_kontrol == 1);

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ses_kontrol = isChecked ? 1 : 0;
                kontrol_gonder = getSharedPreferences("ses_kontrol", MODE_PRIVATE).edit();
                kontrol_gonder.putInt("ses_kontrol1", ses_kontrol);
                kontrol_gonder.apply();
            }
        });
    }

    public void return_mainpage(View view){
        if(ses_kontrol==1){
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.return_main);
            mediaPlayer.start();
        }
        geridon.setBackground(drawable_button2);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                geridon.setBackground(drawable_button); // Renk kodunu düzeltme
            }
        }, 33); // Zamanlama süresini düzeltme
        Intent go = new Intent(WordBox_settings.this,tostart.class);
        startActivity(go);
        finish();
    }

    public void show_tutorial(View view){
        if(ses_kontrol==1){
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.grammer_open);
            mediaPlayer.start();
        }
        Intent go = new Intent(WordBox_settings.this,onboarding1.class);
        startActivity(go);
        finish();
    }
    public void sendEmail(View view) {
        if(ses_kontrol==1){
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.grammer_open);
            mediaPlayer.start();
        }
        String[] recipients = {"developz.technology@gmail.com"};
        String subject = "WordBox";
        String message = "Body of the email";

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", recipients[0], null));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, recipients);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } catch (android.content.ActivityNotFoundException ex) {
            // Handle the error
        }
    }
}