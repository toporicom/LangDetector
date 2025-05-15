package com.mirea.kt.ribo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private String login;
    private String password;
    private String group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String server = "https://android-for-students.ru";
        String serverPath = "/coursework/login.php";
        //login = "Student571196";
        //password = "TMWS9C";

        Button logButton = findViewById(R.id.login_button);
        TextView errorTV = findViewById(R.id.logError);
        TextInputEditText loginET = findViewById(R.id.login);
        TextInputEditText passwordET = findViewById(R.id.password);

        logButton.setOnClickListener(v -> {
            try {
                login = loginET.getText().toString();
                password = passwordET.getText().toString();
                group = "RIBO-02-23";

                Log.i("Login", login);
                Log.i("Password", password);
                Log.i("Group", group);

                HashMap<String, String> map = new HashMap<>();
                map.put("lgn", login);
                map.put("pwd", password);
                map.put("g", group);

                HTTPRunnable httpRunnable = new HTTPRunnable(server + serverPath, map);
                Thread th = new Thread(httpRunnable);
                th.start();

                try {
                    th.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        JSONObject jsonObject = new JSONObject(httpRunnable.getResponseBody());

                        Log.i("Title", "Title: " + jsonObject.getString("title"));
                        Log.i("Task", "Task: " + jsonObject.getString("task"));
                        Log.i("Variant", "Variant: " + jsonObject.getString("variant"));

                        errorTV.setVisibility(View.GONE);

                        Intent loadingPageIntent = new Intent(this, LangDetector.class);
                        startActivity(loadingPageIntent);
                    } catch (JSONException e) {
                        Log.i("MainActivityError", "Error, invalid login or pass");
                        errorTV.setVisibility(View.VISIBLE);
                    }
                }
            }catch (RuntimeException e){
            }
        });
    }
}