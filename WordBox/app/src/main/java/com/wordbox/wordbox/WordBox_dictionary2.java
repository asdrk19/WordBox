package com.wordbox.wordbox;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wordbox.wordbox.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class WordBox_dictionary2 extends AppCompatActivity {
    ImageButton geridon;
    public SharedPreferences sp_liste;
    public ArrayList<String> stringList;
    LinearLayout ana_lineer;
    public SharedPreferences ses_kontrol;
    int ses_kontrol_verisi;
    public TextToSpeech textToSpeech;
    Drawable drawable_button,drawable_button2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        //Gece modunu kapatma:
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_box_dictionary2);
        geridon=findViewById(R.id.imageButton10);

        //Ses için kontrol verisini alma;
        ses_kontrol = getSharedPreferences("ses_kontrol", MODE_PRIVATE);
        ses_kontrol_verisi = ses_kontrol.getInt("ses_kontrol1", 1);

        //Eklenen StringDizi
        ana_lineer=findViewById(R.id.Ana_layout);
        sp_liste= getSharedPreferences("veri_liste_cümle",MODE_PRIVATE);
        stringList = okuStringList();

        //Geri dönme butonu arkaplanları:
        drawable_button=getResources().getDrawable(R.drawable.rounded_button);
        drawable_button2=getResources().getDrawable(R.drawable.rounded_button2);

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
        final String[] selectedText = {""};
        //CardView ve ImageView oluşturma işlemleri
        for (int i = 0; i < stringList.size(); i++) {
            String item = stringList.get(i);
            System.out.println(item);

            // Yeni bir CardView oluşturun (TextView için)
            CardView cardViewText = new CardView(this);
            LinearLayout.LayoutParams layoutParamsText = new LinearLayout.LayoutParams(
                    getResources().getDimensionPixelSize(R.dimen.text_card_width),
                    getResources().getDimensionPixelSize(R.dimen.text_card_height)
            );
            layoutParamsText.setMargins(0, 0, getResources().getDimensionPixelSize(R.dimen.margin_between_cards), 0); // Araya boşluk ekleyin
            cardViewText.setLayoutParams(layoutParamsText);
            cardViewText.setRadius(getResources().getDimensionPixelSize(R.dimen.cardview_radius)); // Radius ayarı
            int myColor = getResources().getColor(R.color.white_2);
            cardViewText.setCardBackgroundColor(myColor);


            // LinearLayout oluşturun ve dikey olarak ayarlayın (TextView için)
            LinearLayout verticalLayoutText = new LinearLayout(this);
            verticalLayoutText.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            verticalLayoutText.setOrientation(LinearLayout.VERTICAL);
            verticalLayoutText.setGravity(Gravity.CENTER); // Ortalama ayarı
            verticalLayoutText.setPadding(16, 16, 16, 16); // Padding ekleyin

            // TextView oluşturun ve ayarlayın (TextView için)
            TextView textView = new TextView(this);
            LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            textView.setLayoutParams(textViewParams);
            textView.setText(item);
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            textView.setTypeface(null, Typeface.BOLD);

            // TextView'ı LinearLayout'a ekleyin (TextView için)
            verticalLayoutText.addView(textView);

            // CardView'a LinearLayout'ı ekleyin (TextView için)
            cardViewText.addView(verticalLayoutText);

            // Yeni bir CardView oluşturun (Resimler için)
            CardView cardViewImages = new CardView(this);
            LinearLayout.LayoutParams layoutParamsImages = new LinearLayout.LayoutParams(
                    getResources().getDimensionPixelSize(R.dimen.image_card_width),
                    getResources().getDimensionPixelSize(R.dimen.image_card_height)
            );
            layoutParamsImages.setMargins(0, 0, 0, 0); // Araya boşluk ekleyin
            cardViewImages.setLayoutParams(layoutParamsImages);
            cardViewImages.setRadius(getResources().getDimensionPixelSize(R.dimen.cardview_radius)); // Radius ayarı
            cardViewImages.setCardBackgroundColor(Color.BLACK); // Arka plan rengi ayarı

            // LinearLayout oluşturun ve dikey olarak ayarlayın (Resimler için)
            LinearLayout verticalLayoutImages = new LinearLayout(this);
            verticalLayoutImages.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            verticalLayoutImages.setOrientation(LinearLayout.VERTICAL);
            verticalLayoutImages.setGravity(Gravity.CENTER); // Ortalama ayarı
            verticalLayoutImages.setPadding(16, 16, 16, 16); // Padding ekleyin

            // İlk ImageView oluşturun ve ayarlayın (Örneğin delete resmi)
            ImageView imageViewDelete = new ImageView(this);
            LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(
                    getResources().getDimensionPixelSize(R.dimen.image_width),
                    getResources().getDimensionPixelSize(R.dimen.image_height)
            );
            imageViewParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL; // Üstte hizalama
            imageViewParams.setMargins(0, getResources().getDimensionPixelSize(R.dimen.margin_top), 0, getResources().getDimensionPixelSize(R.dimen.margin_between_items)); // Araya boşluk ekleyin
            imageViewDelete.setLayoutParams(imageViewParams);
            imageViewDelete.setBackgroundResource(R.drawable.delete); // R.drawable.delete yerine kendi resminizi ayarlayın

// İkinci ImageView oluşturun ve ayarlayın (Örneğin sound resmi)
            ImageView imageViewSound = new ImageView(this);
            LinearLayout.LayoutParams imageViewParams2 = new LinearLayout.LayoutParams(
                    getResources().getDimensionPixelSize(R.dimen.image_width),
                    getResources().getDimensionPixelSize(R.dimen.image_height)
            );
            imageViewParams2.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL; // Altta hizalama
            imageViewParams2.setMargins(0, getResources().getDimensionPixelSize(R.dimen.margin_between_items), 0, getResources().getDimensionPixelSize(R.dimen.margin_top)); // Araya boşluk ekleyin
            imageViewSound.setLayoutParams(imageViewParams2);
            imageViewSound.setBackgroundResource(R.drawable.sound); // R.drawable.sound yerine kendi resminizi ayarlayın

            // LinearLayout'a ImageView'ları ekleyin (Resimler için)
            verticalLayoutImages.addView(imageViewDelete);
            verticalLayoutImages.addView(imageViewSound);

            // CardView'a LinearLayout'ı ekleyin (Resimler için)
            cardViewImages.addView(verticalLayoutImages);

            // Ana LinearLayout'a her iki CardView'ı da ekleyin
            LinearLayout horizontalLayout = new LinearLayout(this);
            horizontalLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
            horizontalLayout.setGravity(Gravity.CENTER); // Ortalama ayarı
            horizontalLayout.setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.padding_between_items));

            horizontalLayout.addView(cardViewText);
            horizontalLayout.addView(cardViewImages);

            ana_lineer.addView(horizontalLayout);

            // Silme işlemi için OnClickListener ekleyin
            imageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ses_kontrol_verisi==1){
                        MediaPlayer mediaPlayer = MediaPlayer.create(v.getContext(),R.raw.dictionary_delete);
                        mediaPlayer.start();
                    }
                    // Silme işlemi
                    View parent = (View) v.getParent().getParent(); // cardViewImages
                    View grandParent = (View) parent.getParent(); // horizontalLayout

                    if (grandParent != null) {
                        // Görünürlüğü kaldırın
                        imageViewDelete.setVisibility(View.INVISIBLE);

                        // Belirli bir süre sonra eski haline getirin
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Veri kaynağından öğeyi silme işlemi
                                stringList.remove(item);

                                // Görünümü güncelleme
                                ana_lineer.removeView(grandParent);

                                // Kalıcı olarak saklanan veriyi güncelleme
                                updateStoredList(stringList);

                                // Görünürlüğü geri getirin
                                imageViewDelete.setVisibility(View.VISIBLE);
                            }
                        }, 200); // 200 milisaniye sonra eski haline döndür
                    }
                }
            });

            imageViewSound.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      // Hangi öğeye tıklandığını doğrulayın
                                                      Log.d("Click", "Clicked on imageViewSound");

                                                      // Seçilen metni kaydedin
                                                      selectedText[0] = item;
                                                      metniSesliOku(item);
                                                      Log.d("Selected Text", "Selected text: " + selectedText[0]);

                                                      imageViewSound.setVisibility(View.INVISIBLE); // Görünürlüğü kaldırın

                                                      // Belirli bir süre sonra eski haline getirin
                                                      new Handler().postDelayed(new Runnable() {
                                                          @Override
                                                          public void run() {
                                                              //imageViewDelete.setBackgroundColor(Color.TRANSPARENT); // Eski rengi geri al
                                                              imageViewSound.setVisibility(View.VISIBLE); // Görünürlüğü geri getirin
                                                          }
                                                      }, 200);
                                                  }
                                              }
            );
        }}
    private void metniSesliOku(String metin) {
        String part2= "";
        String part1 = "";
        int index = metin.indexOf(":");
        if (index != -1) {
            part1 = metin.substring(0, index).trim(); // İlk kısmı alır
            part2 = metin.substring(index + 1).trim(); // İkinci kısmı alır
            System.out.println("Part 1: " + part1);
            System.out.println("Part 2: " + part2);
        } else {
            System.out.println("Belirtilen karakter ':' bulunamadı.");
        }
        if (textToSpeech != null) {
            // Metni sesli olarak okuma
            textToSpeech.speak(part1, TextToSpeech.QUEUE_FLUSH, null, null);

        }
    }
    public void updateStoredList(List<String> updatedList) {
        SharedPreferences preferences = getSharedPreferences("veri_liste_cümle", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet("liste_anahtar_cümle", new HashSet<>(updatedList));
        editor.apply();
    }

    public ArrayList<String> okuStringList() {
        Set<String      > set = sp_liste.getStringSet("liste_anahtar_cümle", new HashSet<>());
        return new ArrayList<>(set);
    }

    public void word(View view){
        if(ses_kontrol_verisi==1){
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.grammer_open);
            mediaPlayer.start();
        }

        Intent go = new Intent(WordBox_dictionary2.this,WordBox_dictionary.class);
        startActivity(go);
        finish();

    }

    public void input(View view) {
        if (ses_kontrol_verisi == 1) {
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.dictionary_button);
            mediaPlayer.start();
        }

        // AlertDialog oluştur
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // EditText'ler için bir konteyner görünümü oluştur
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // İngilizce metni için EditText ekle
        final EditText editTextEnglish = new EditText(this);
        editTextEnglish.setHint("İngilizce cümleyi girin");
        layout.addView(editTextEnglish);

        // Türkçe metni için EditText ekle
        final EditText editTextTurkish = new EditText(this);
        editTextTurkish.setHint("Türkçe karşılığını girin");
        layout.addView(editTextTurkish);

        builder.setView(layout);
        builder.setTitle("                  Cümle Girişi");


        builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String englishText = editTextEnglish.getText().toString().trim();
                String turkishText = editTextTurkish.getText().toString().trim();

                if (englishText.isEmpty() || turkishText.isEmpty()) {
                    Context appContext = getApplicationContext();
                    showCustomToast(appContext, "Hata! Boşlukları doldurun", Color.TRANSPARENT);
                } else {
                    // String.format ile sayac değerini ve metni birleştirme
                    String eklenecek_Veri = englishText +" : "+ turkishText;
                    stringList.add(eklenecek_Veri);
                    updateStoredList(stringList);
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            }
        });
        builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void return_mainpage(View view){
        if(ses_kontrol_verisi==1){
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.return_main);
            mediaPlayer.start();
        }

        geridon.setBackground(drawable_button2);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                geridon.setBackground(drawable_button);
            }
        }, 33); // 1000 mi
        Intent go = new Intent(WordBox_dictionary2.this,tostart.class);
        startActivity(go);
        finish();

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
        //Toast toast2 = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 125); // (Gravity, xOffset, yOffset) //Sayfa konumunu ayarladık.
        toast.setDuration(Toast.LENGTH_SHORT); // Toast süresi (LENGTH_SHORT veya LENGTH_LONG)
        toast.setView(textView); // Toast'a özel View'i ayarla
        toast.show();
    }






    }




