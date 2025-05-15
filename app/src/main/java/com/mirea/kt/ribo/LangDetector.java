package com.mirea.kt.ribo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.detectlanguage.DetectLanguage;
import com.detectlanguage.Result;
import com.detectlanguage.errors.APIError;

import java.util.List;
import java.util.Locale;

public class LangDetector extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_langdetector);

        EditText inputText = findViewById(R.id.input_text);
        Button button = findViewById(R.id.detect_text_btn);

        inputText.setOnLongClickListener(v -> {
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

            ClipData clipData = ClipData.newPlainText("text", inputText.getText());
            if (clipboardManager != null) {
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(LangDetector.this, "Текст скопирован в буфер обмена", Toast.LENGTH_LONG).show();
            }
            return false;
        });

        button.setOnClickListener(v -> {
            new Thread(() -> {
                try {
                    DetectLanguage.ssl = true;
                    DetectLanguage.apiKey = "14f216be21b115323966d6ec158344d2";

                    String language = DetectLanguage.simpleDetect(String.valueOf(inputText.getText()));

                    runOnUiThread(() -> InfoDialog.showDialog(this, getLanguageName(language)));
                } catch (APIError e) {
                    Log.e("APIError", "Ошибка в API", e);
                    runOnUiThread(() -> Toast.makeText(this, "Ошибка определения языка", Toast.LENGTH_SHORT).show());
                }
            }).start();
        });
    }
    public String getLanguageName(String languageCode) {
        try {
            Locale locale = new Locale(languageCode);
            return locale.getDisplayLanguage(locale);
        } catch (Exception e) {
            return "Неизвестный язык";
        }
    }
}
