package com.mirea.kt.ribo.runnable;

import android.util.Log;

import com.mirea.kt.ribo.db.DBManager;
import com.mirea.kt.ribo.model.Meaning;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class HTTPLangDetector implements Runnable {
    private String address;
    private String word;
    private ArrayList<Meaning> responseBody = new ArrayList<>();
    private void parseJSONResponse(String response) {

        try {
            JSONArray rootArray = new JSONArray(response);

            if (rootArray.length() == 0) {
                return;
            }

            JSONObject wordObject = rootArray.getJSONObject(0);
            JSONArray meaningsArray = wordObject.getJSONArray("meanings");

            for (int i = 0; i < meaningsArray.length(); i++) {
                JSONObject meaningObject = meaningsArray.getJSONObject(i);

                String partOfSpeech = meaningObject.optString("partOfSpeech", "");

                JSONArray definitionsArray = meaningObject.optJSONArray("definitions");

                if (definitionsArray == null) {
                    continue;
                }

                for (int j = 0; j < definitionsArray.length(); j++) {
                    JSONObject definitionObject = definitionsArray.getJSONObject(j);

                    String definition = definitionObject.optString("definition", "");
                    String example = definitionObject.optString("example", "");

                    Meaning meaning = new Meaning(
                            word,
                            partOfSpeech,
                            definition,
                            example,
                            0
                    );

                    responseBody.add(meaning);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public HTTPLangDetector(String address, String word) {
        this.address = address + word;
        this.word = word;
    }

    public ArrayList<Meaning> getResponseBody() {
        return responseBody;
    }

    @Override
    public void run() {
        if (this.address != null && !this.address.isEmpty()) {
            try {
                URL url = new URL(this.address);
                URLConnection connection = url.openConnection();
                HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
                httpURLConnection.setRequestMethod("GET");
                int responseCode = httpURLConnection.getResponseCode();
                Log.i("HTTP Detect Language", "Response code: " + responseCode);
                if (responseCode == 200) {
                    InputStreamReader isr = new InputStreamReader(httpURLConnection.getInputStream());
                    BufferedReader br = new BufferedReader(isr);
                    String currentLine;
                    StringBuilder sbResponse = new StringBuilder();
                    while ((currentLine = br.readLine()) != null) {
                        sbResponse.append(currentLine);
                    }
                    parseJSONResponse(sbResponse.toString());
                } else {
                    Log.i("my_tag", "Error!");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
