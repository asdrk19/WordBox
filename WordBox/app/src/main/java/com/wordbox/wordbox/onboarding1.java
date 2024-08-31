package com.wordbox.wordbox;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import com.wordbox.wordbox.R;

public class onboarding1 extends AppCompatActivity {
    public SharedPreferences ses_kontrol;
    public int ses_kontrol_verisi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        //Gece modunu kapatma:
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ana_ekran_deneme_onboard1);

        CardView myCardView = findViewById(R.id.kart_kay);
        int colorStart = Color.parseColor("#8B00AB");
        int colorEnd = Color.parseColor("#C64FE2");
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorStart, colorEnd);
        colorAnimation.setDuration(1205); // 1 second
        colorAnimation.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimation.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                myCardView.setCardBackgroundColor((int) animator.getAnimatedValue());
            }
        });

        colorAnimation.start();

        ses_kontrol = getSharedPreferences("ses_kontrol", MODE_PRIVATE);
        ses_kontrol_verisi = ses_kontrol.getInt("ses_kontrol1", 1);
    }
    public void next_page(View view){
        if(ses_kontrol_verisi==1){
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.grammer_change);
            mediaPlayer.start();
        }
        Intent go = new Intent(onboarding1.this,onboarding2.class);
        startActivity(go);
        finish();
    }

}
