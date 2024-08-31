package com.wordbox.wordbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wordbox.wordbox.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    public SharedPreferences sp_sonliste,sp_sonliste_cumle;
    public String yeni_veri,yeni_veri_cümle;
    public SharedPreferences.Editor spe_sonliste,spe_sonliste_cumle;


    TextView ingilizce,ingilizce2,turkce2;
    TextView turkce;
    private TextToSpeech textToSpeech;

    public Button buton,buton2;
    public String ingilizcekelimeg,ingilizcecumleg,turkcekelimeg,turkcecumleg;
    public ImageButton save1,save2,sound1,sound2,return_button;
    public ArrayList<String> stringList,stringList_sentences;

    private Context appContext;
    public SharedPreferences ses_kontrol;
    int ses_kontrol_verisi;
    public List<String> turkishWords,englishwords,turkishsentences,englishsentences;

    private MediaPlayer mediaPlayer; //Her seferinde mediaplayer oluşturmayalım diye
    private static final String TAG = "saad";
    public Drawable drawable_button,drawable_button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Navigasyon çubuğunu ana ekrandan gizleme:
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        super.onCreate(savedInstanceState);  // Bu satırı doğru konuma taşıdım
        setContentView(R.layout.activity_main); // Bu satırı da doğru konuma taşıdım
        //Gece modunu kapatma:
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        drawable_button=getResources().getDrawable(R.drawable.rounded_button);
        drawable_button2=getResources().getDrawable(R.drawable.rounded_button2);


        // Dosya ID'si (res/raw/words.txt dosyasının ID'si)
        int fileResourceId_words = R.raw.words;
        int fileResourceId_sentences = R.raw.sentences;

        // İngilizce ve Türkçe kelimeleri al
        englishwords = FileReaderUtil.readEnglishWords(this, fileResourceId_words);
        turkishWords = FileReaderUtil.readTurkishWords(this, fileResourceId_words);

        // İngilizce ve Türkçe kelimeleri al
        englishsentences = FileReaderUtil.readEnglishWords(this, fileResourceId_sentences);
        turkishsentences = FileReaderUtil.readTurkishWords(this, fileResourceId_sentences);

        // Elde edilen kelimeleri logcat'e yazdır
        Log.d(TAG, "English Words: " + englishwords);
        Log.d(TAG, "Turkish Words: " + turkishWords);

        stringList = new ArrayList<>();
        ingilizce= findViewById(R.id.ingilizce);
        turkce = findViewById(R.id.turkce);
        buton = findViewById(R.id.change);
        return_button=findViewById(R.id.imageButton5);
        ingilizce2= findViewById(R.id.ingilizce2);
        turkce2 = findViewById(R.id.turkce2);
        buton2= findViewById(R.id.change2);
        save1= findViewById(R.id.imageButton2);
        save2 = findViewById(R.id.image2);
        sound1 = findViewById(R.id.imageButton3);
        sound2=findViewById(R.id.imageButton4);
        buton2.setBackgroundColor(255);
        buton.setBackgroundColor(255);
        appContext = getApplicationContext();
        sp_sonliste=getSharedPreferences("veri_liste",MODE_PRIVATE);
        stringList = okuStringList();
        sp_sonliste_cumle=getSharedPreferences("veri_liste_cümle",MODE_PRIVATE);
        stringList_sentences = okuStringList_cumle();

// Listelerin boyutunu kontrol ederek işlemleri gerçekleştir
        if (stringList != null && !stringList.isEmpty()) {
            for (int i = 0; i < stringList.size(); i++) {
                System.out.println("Kelime: " + stringList.get(i));
            }
        } else {
            System.out.println("Kelime listesi boş.");
        }

        if (stringList_sentences != null && !stringList_sentences.isEmpty()) {
            for (int i = 0; i < stringList_sentences.size(); i++) {
                System.out.println("Cümle: " + stringList_sentences.get(i));
            }
        } else {
            System.out.println("Cümle listesi boş.");
        }

        //Ses için kontrol verisini alma;
        //Sayaç verisini alma:
        ses_kontrol = getSharedPreferences("ses_kontrol", MODE_PRIVATE);
        ses_kontrol_verisi = ses_kontrol.getInt("ses_kontrol1", 1);
        mediaPlayer = MediaPlayer.create(this, R.raw.change);
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {

                int result = textToSpeech.setLanguage(new Locale("en-EN"));

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    // Dil verileri eksik veya desteklenmiyorsa uyarı verilebilir
                    Toast.makeText(this, "Dil desteklenmiyor veya eksik", Toast.LENGTH_SHORT).show();
                } else {
                    // Metni sesli olarak okuma
                    String metin = "";
                    textToSpeech.speak(metin, TextToSpeech.QUEUE_FLUSH, null, null);
                }
            } else {
                // TextToSpeech başlatılamadı
                Toast.makeText(this, "TextToSpeech başlatılamadı", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public ArrayList<String> okuStringList() {
        Set<String      > set = sp_sonliste.getStringSet("liste_anahtar", new HashSet<>());
        return new ArrayList<>(set);
    }
    public ArrayList<String> okuStringList_cumle() {
        Set<String      > set = sp_sonliste_cumle.getStringSet("liste_anahtar_cümle", new HashSet<>());
        return new ArrayList<>(set);
    }
    private void metniSesliOku(String metin) {
        if (textToSpeech != null) {
            // Metni sesli olarak okuma
            textToSpeech.speak(metin, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    public void sound1(View view){
        sound1.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.abc_hint_foreground_material_dark));


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sound1.setBackground(drawable_button);
            }
        }, 66); // 1000 mi
        if(ingilizcecumleg==null){
            showCustomToast(appContext, "Cümle bulunamadı, Change butonuna basın!", Color.TRANSPARENT);
        }
        else{
        metniSesliOku(ingilizcecumleg);}
    }
    public void sound2(View view){
        sound2.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.abc_hint_foreground_material_dark));


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sound2.setBackground(drawable_button);
            }
        }, 66); // 1000 mi
        if(ingilizcekelimeg==null){
            showCustomToast(appContext, "Kelime bulunamadı, Change butonuna basın!", Color.TRANSPARENT);
        }
        else{
            metniSesliOku(ingilizcekelimeg);}


    }
    public void change(View view){
        try {
            if (ses_kontrol_verisi == 1 && mediaPlayer != null) {
                mediaPlayer.reset(); // Önceki durumu temizle
                mediaPlayer.setDataSource(this, Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.change));
                mediaPlayer.setVolume(1.0f, 1.0f); // Ses seviyesini en yüksek değere ayarla

                // Medya dosyası hazır olduğunda çağrılacak dinleyici
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        // Medya dosyası hazır olduğunda işlemler burada yapılır
                        mediaPlayer.start(); // Ses çalmaya başla
                    }
                });

                mediaPlayer.prepareAsync(); // Medya dosyasını hazırla (asenkron)
            }
        } catch (IllegalStateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        buton.setBackgroundColor(256);

        Random random = new Random();
        System.out.println("sayi:"+englishwords.size()+turkishWords.size());

        int randomNumber = random.nextInt(497); // 0 ile 156 arasında bir sayı

        String t2 = buyukHarfeCevir(turkishWords.get(randomNumber));
        String i2 = buyukHarfeCevir(englishwords.get(randomNumber));
        ingilizcekelimeg=i2;
        turkcekelimeg=t2;
        ingilizce.setText(i2);
        turkce.setText(t2);

    }

    public static String buyukHarfeCevir(String kelime) {
        if (kelime == null || kelime.isEmpty()) {
            return kelime;
        }
        return Character.toUpperCase(kelime.charAt(0)) + kelime.substring(1).toLowerCase();
    }

    //Cümle kaydedici Buton:
    public void save_sentences(View view) throws InterruptedException {
        try {
            if (ses_kontrol_verisi == 1 && mediaPlayer != null) {
                mediaPlayer.reset(); // Önceki durumu temizle
                mediaPlayer.setDataSource(this, Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.save));
                mediaPlayer.setVolume(1.0f, 1.0f); // Ses seviyesini en yüksek değere ayarla

                // Medya dosyası hazır olduğunda çağrılacak dinleyici
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        // Medya dosyası hazır olduğunda işlemler burada yapılır
                        mediaPlayer.start(); // Ses çalmaya başla
                    }
                });

                mediaPlayer.prepareAsync(); // Medya dosyasını hazırla (asenkron)
            }
        } catch (IllegalStateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        save1.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.abc_hint_foreground_material_dark));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                save1.setBackground(drawable_button);
            }
        }, 66); // 1000 mi

        String kontrol = ingilizcecumleg;
        System.out.println(kontrol);
        if (kontrol==null) {

            showCustomToast(appContext, "Cümle bulunamadı, Change butonuna basın!", Color.TRANSPARENT);
        } else {

            yeni_veri_cümle = ingilizcecumleg + " : " + turkcecumleg;

            if (stringList_sentences.contains(yeni_veri_cümle)) {
                System.out.println("Zaten dahil");
                showCustomToast(appContext, "Cümle zaten kayıtlı!", Color.TRANSPARENT);


            } else {
                //Yeni veri kaydı:
                showCustomToast(appContext, "Cümle kaydedildi", Color.TRANSPARENT);
                stringList_sentences.add(yeni_veri_cümle);
                for (int i=0;i<stringList_sentences.size();i++){
                    System.out.println("Veri:"+stringList_sentences.get(i));
                }
                Set<String> set = new HashSet<>(stringList_sentences);
                spe_sonliste_cumle=getSharedPreferences("veri_liste_cümle",MODE_PRIVATE).edit();
                spe_sonliste_cumle.putStringSet("liste_anahtar_cümle", set);
                spe_sonliste_cumle.apply();

                sp_sonliste_cumle=getSharedPreferences("veri_liste_cümle",MODE_PRIVATE);
                stringList_sentences = okuStringList_cumle();


        }}


    }



    //Kelime Kaydedici Buton:
    public void save2(View view) throws InterruptedException {

        try {
            if (ses_kontrol_verisi == 1 && mediaPlayer != null) {
                mediaPlayer.reset(); // Önceki durumu temizle
                mediaPlayer.setDataSource(this, Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.save));
                mediaPlayer.setVolume(1.0f, 1.0f); // Ses seviyesini en yüksek değere ayarla

                // Medya dosyası hazır olduğunda çağrılacak dinleyici
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        // Medya dosyası hazır olduğunda işlemler burada yapılır
                        mediaPlayer.start(); // Ses çalmaya başla
                    }
                });

                mediaPlayer.prepareAsync(); // Medya dosyasını hazırla (asenkron)
            }
        } catch (IllegalStateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        save2.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.abc_hint_foreground_material_dark));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                save2.setBackground(drawable_button);
            }
        }, 66); // 1000 mi

        String kontrol=ingilizcekelimeg;
        if(kontrol==null){
            System.out.println("Boş");
            showCustomToast(appContext, "Kelime bulunamadı, Change butonuna basın!", Color.TRANSPARENT);
        }
        else {
            yeni_veri = ingilizcekelimeg + " : " + turkcekelimeg;
            System.out.println("kelime:"+yeni_veri);

        if (stringList.contains(yeni_veri)) {
            showCustomToast(appContext, "Kelime zaten kayıtlı!", Color.TRANSPARENT);

        }
        else {
            System.out.println("kelime2:"+yeni_veri);
            showCustomToast(appContext, "Kelime kaydedildi", Color.TRANSPARENT);
            //Yeni veri kaydı:
            stringList.add(yeni_veri);

            for (int i=0;i<stringList.size();i++){
                System.out.println("gör"+stringList.get(i));
            }
            Set<String> set = new HashSet<>(stringList);
            spe_sonliste=getSharedPreferences("veri_liste",MODE_PRIVATE).edit();
            spe_sonliste.putStringSet("liste_anahtar", set);
            spe_sonliste.apply();

            sp_sonliste=getSharedPreferences("veri_liste",MODE_PRIVATE);
            stringList = okuStringList();

        }

        }}

    public void change2(View view){
        try {
            if (ses_kontrol_verisi == 1 && mediaPlayer != null) {
                mediaPlayer.reset(); // Önceki durumu temizle
                mediaPlayer.setDataSource(this, Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.change));
                mediaPlayer.setVolume(1.0f, 1.0f); // Ses seviyesini en yüksek değere ayarla

                // Medya dosyası hazır olduğunda çağrılacak dinleyici
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        // Medya dosyası hazır olduğunda işlemler burada yapılır
                        mediaPlayer.start(); // Ses çalmaya başla
                    }
                });

                mediaPlayer.prepareAsync(); // Medya dosyasını hazırla (asenkron)
            }
        } catch (IllegalStateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        buton2.setBackgroundColor(255);
        Random random = new Random();
        // Belirli bir aralıkta rastgele bir tamsayı elde etmek için nextInt() kullanın
        int randomNumber2 = random.nextInt(174); // 0 ile 99 arasında bir sayı
        ingilizcecumleg=englishsentences.get(randomNumber2);
        turkcecumleg=turkishsentences.get(randomNumber2);
        ingilizce2.setText(ingilizcecumleg);

        turkce2.setText(turkcecumleg);



    }



    public static void showCustomToast(Context context, String message, int backgroundColor) {
        // TextView oluştur ve özelliklerini ayarla
        TextView textView = new TextView(context);
        textView.setText(message);
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundColor(backgroundColor);
        textView.setPadding(20, 0, 20, 0); // İsteğe bağlı: içeriği kenarlardan ayırma

        // Toast mesajını özelleştirmek için bir Toast oluştur
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 125); // (Gravity, xOffset, yOffset) //Sayfa konumunu ayarladık.
        toast.setDuration(Toast.LENGTH_SHORT); // Toast süresi (LENGTH_SHORT veya LENGTH_LONG)
        toast.setView(textView); // Toast'a özel View'i ayarla
        toast.show();
    }

    public void return_mainpage(View view){

        if(ses_kontrol_verisi==1){
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.return_main);
            mediaPlayer.start();
        }
        return_button.setBackground(drawable_button2);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                return_button.setBackgroundColor(000);
            }
        }, 33); // 1000 mi
        Intent go = new Intent(MainActivity.this,tostart.class);
        startActivity(go);
        finish();
    }

}


