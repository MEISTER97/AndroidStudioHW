package com.example.hw1.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.hw1.ScoreData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesManager {
    private static final String SP_FILE = "SP_FILE";
    private static final String KEY_SCORES_LIST = "scores_list";
    private static final int MAX_SCORES_TO_REMEMBER = 10;

    private static volatile SharedPreferencesManager instance;
    private static Gson gson = new Gson();
    private final SharedPreferences sharedPreferences;
    private Context context;

    private SharedPreferencesManager(Context context) {
        this.context=context;
        sharedPreferences = context.getApplicationContext().getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);
    }

    public static SharedPreferencesManager init(Context context) {
        if (instance == null) {
            synchronized (SharedPreferencesManager.class) {
                if (instance == null) {
                    instance = new SharedPreferencesManager(context);
                }
            }
        }
        return getInstance();
    }

    public static SharedPreferencesManager getInstance() {
        return instance;
    }

    public void saveScoreData(ScoreData scoreData) {
        List<ScoreData> scoresList = getScoresList();

        if (scoresList == null) {
            scoresList = new ArrayList<>();
        }

        scoresList.add(scoreData);

        // Sort scoresList in descending order based on the score
        scoresList.sort((o1, o2) -> Integer.compare(o2.getMeterScore(), o1.getMeterScore()));

        // Trim the list to only keep the top 10 scores
        if (scoresList.size() > MAX_SCORES_TO_REMEMBER) {
            scoresList = scoresList.subList(0, MAX_SCORES_TO_REMEMBER);
        }

        // Convert scoresList to JSON string
        String scoresListJson = gson.toJson(scoresList);

        // Save scoresListJson to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SCORES_LIST, scoresListJson);
        editor.apply();
    }

    public List<ScoreData> getScoresList() {
        String scoresListJson = sharedPreferences.getString(KEY_SCORES_LIST, null);

        if (scoresListJson != null) {
            Type listType = new TypeToken<List<ScoreData>>() {}.getType();
            return gson.fromJson(scoresListJson, listType);
        } else {
            return null; // Return null if scoresListJson is not found
        }
    }
}
