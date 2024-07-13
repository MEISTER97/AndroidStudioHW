package com.example.hw1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hw1.Fragments.ListFragment;
import com.example.hw1.Fragments.MapFragment;
import com.google.android.material.button.MaterialButton;

public class ActivityHighscore extends AppCompatActivity {

    private FrameLayout Highscore_FRAME_list;
    private FrameLayout Highscore_FRAME_map;
    private MaterialButton Highscore_BTN_Menu;

    private ListFragment listFragment;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore); // Ensure this matches the correct layout file
        findViews();
        initViews();
    }

    private void findViews() {
        Highscore_FRAME_list = findViewById(R.id.Highscore_FRAME_list);
        Highscore_FRAME_map = findViewById(R.id.Highscore_FRAME_map);
        Highscore_BTN_Menu = findViewById(R.id.Highscore_BTN_Menu);
    }

    private void initViews() {
        listFragment = new ListFragment();
        listFragment.setCallbackListItemClicked((lat, lon) -> {
            mapFragment.zoom(lat, lon);
        });
        getSupportFragmentManager().beginTransaction().add(R.id.Highscore_FRAME_list, listFragment).commit();

        mapFragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.Highscore_FRAME_map, mapFragment).commit();

        Highscore_BTN_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToMenuActivity();
            }
        });
    }

    private void moveToMenuActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); // Reorder MenuActivity to the front
        startActivity(intent);
        finish(); // Close current activity
    }
}
