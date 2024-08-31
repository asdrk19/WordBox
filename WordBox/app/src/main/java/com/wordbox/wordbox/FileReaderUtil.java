package com.wordbox.wordbox;

import android.content.Context;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileReaderUtil {

    private static final String TAG = "FileReaderUtil";

    public static List<String> readEnglishWords(Context context, int resourceId) {
        List<String> englishWords = new ArrayList<>();
        InputStream inputStream = context.getResources().openRawResource(resourceId);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int colonIndex = line.indexOf(':');
                if (colonIndex != -1) {
                    String englishWord = line.substring(0, colonIndex).trim();
                    englishWords.add(englishWord);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Error reading file", e);
        }
        return englishWords;
    }

    public static List<String> readTurkishWords(Context context, int resourceId) {
        List<String> turkishWords = new ArrayList<>();
        InputStream inputStream = context.getResources().openRawResource(resourceId);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int colonIndex = line.indexOf(':');
                if (colonIndex != -1) {
                    String turkishWord = line.substring(colonIndex + 1).trim();
                    turkishWords.add(turkishWord);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Error reading file", e);
        }
        return turkishWords;
    }
}
