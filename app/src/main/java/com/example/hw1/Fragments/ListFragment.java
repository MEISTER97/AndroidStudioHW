package com.example.hw1.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hw1.R;
import com.example.hw1.ScoreData;
import com.example.hw1.Utilities.SharedPreferencesManager;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import com.example.hw1.Interfaces.Callback_ListItemClicked;


public class ListFragment extends Fragment {
    private MaterialTextView list_LBL_leaderboard;
    private MaterialTextView list_LBL_MeterScore;
    private MaterialTextView list_LBL_Score;
    private MaterialTextView[] list_LBL_LeaderBoardMeterScores;
    private MaterialTextView[] list_LBL_LeaderBoardScores;
    private final Gson gson = new Gson();

    private List<ScoreData> topScoresList;
   // private ScoreData scoreData;
    private static final int MAX_SCORES_TO_REMEMBER = 10; // Maximum number of scores to remember
    private int minSize;
    private Callback_ListItemClicked callbackListItemClicked;

    public void setCallbackListItemClicked(Callback_ListItemClicked callbackListItemClicked) {
        this.callbackListItemClicked = callbackListItemClicked;
    }

    public ListFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      topScoresList = new ArrayList<>();

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(v);
        readFromJson();
        return v;
    }


    private void readFromJson() {
        topScoresList = SharedPreferencesManager.getInstance().getScoresList();

        if (topScoresList != null) {
            for (ScoreData data : topScoresList) {
            }
        }
        else {
            Log.d("ScoreData", "No scores found in SharedPreferences");
            topScoresList = new ArrayList<>();
        }
        initViews();

    }


    private void initViews() {
        for (int i = 0; i < topScoresList.size(); i++) {
            ScoreData scoreData = topScoresList.get(i);
            list_LBL_LeaderBoardMeterScores[i].setText(String.valueOf(scoreData.getMeterScore()));
            list_LBL_LeaderBoardScores[i].setText(String.valueOf(scoreData.getScore()));
            list_LBL_LeaderBoardMeterScores[i].setOnClickListener(v -> {
                Log.d("LOCATION IN FRAGMENT", "Location is here"+scoreData.getLatitude() +" " + scoreData.getLongitude());

                // Call itemClicked with latitude and longitude from scoreData
                itemClicked(scoreData.getLatitude(), scoreData.getLongitude());
            });
        }
    }

    private void itemClicked(double lat, double lon) {
        if(callbackListItemClicked !=null) {
            callbackListItemClicked.listItemCLicked(lat, lon);
        }

    }

    private void findViews(View view) {
        list_LBL_leaderboard = view.findViewById(R.id.list_LBL_leaderboard);
        list_LBL_MeterScore = view.findViewById(R.id.list_LBL_MeterScore);
        list_LBL_Score = view.findViewById(R.id.list_LBL_Score);

        list_LBL_LeaderBoardMeterScores = new MaterialTextView[]{
                view.findViewById(R.id.list_LBL_LeaderBoardMeterScore1),
            view.findViewById(R.id.list_LBL_LeaderBoardMeterScore2),
            view.findViewById(R.id.list_LBL_LeaderBoardMeterScore3),
                view.findViewById(R.id.list_LBL_LeaderBoardMeterScore4),
                view.findViewById(R.id.list_LBL_LeaderBoardMeterScore5),
                view.findViewById(R.id.list_LBL_LeaderBoardMeterScore6),
                view.findViewById(R.id.list_LBL_LeaderBoardMeterScore7),
                view.findViewById(R.id.list_LBL_LeaderBoardMeterScore8),
                view.findViewById(R.id.list_LBL_LeaderBoardMeterScore9),
                view.findViewById(R.id.list_LBL_LeaderBoardMeterScore10)
        };


        list_LBL_LeaderBoardScores = new MaterialTextView[]{
                view.findViewById(R.id.list_LBL_LeaderBoardScore1),
                view.findViewById(R.id.list_LBL_LeaderBoardScore2),
                view.findViewById(R.id.list_LBL_LeaderBoardScore3),
                view.findViewById(R.id.list_LBL_LeaderBoardScore4),
                view.findViewById(R.id.list_LBL_LeaderBoardScore5),
                view.findViewById(R.id.list_LBL_LeaderBoardScore6),
                view.findViewById(R.id.list_LBL_LeaderBoardScore7),
                view.findViewById(R.id.list_LBL_LeaderBoardScore8),
                view.findViewById(R.id.list_LBL_LeaderBoardScore9),
                view.findViewById(R.id.list_LBL_LeaderBoardScore10)
        };


    }
}