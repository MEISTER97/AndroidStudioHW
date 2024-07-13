package com.example.hw1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import com.example.hw1.Interfaces.callback_menuOptionForMain;

public class MenuActivity extends AppCompatActivity  {
    private RadioGroup radioGroup;
    private RadioButton radioButton_fast;
    private RadioButton radioButton_slow;
    private ImageButton menu_BTN_Main;
    private ToggleButton menu_ToggleMode;
    private MaterialButton menu_BTN_LeaderBoards;

    private callback_menuOptionForMain callback;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);  // Set the content view to activity_menu.xml
        findViews();
        initViews();
        callback = MainActivity.getInstance(); // Assuming MainActivity is accessible as a singleton
        if (callback == null) {
            Log.e("MenuActivity", "Callback reference is null");
            // Handle case where callback is null (MainActivity instance not available)
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButton_fast) {
                    // Fast Mode selected
                    if (callback != null) {
                        Log.d("Game status:", "Speed change");
                        callback.changeSpeedClick(500,true); // Fast mode speed
                    }
                } else if (checkedId == R.id.radioButton_slow) {
                    // Slow Mode selected
                    if (callback != null) {
                        callback.changeSpeedClick(1000,false); // Slow mode speed
                    }
                }
            }
        });
        //perform toggle off as default
        performActionOnToggleOff();
        menu_ToggleMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Toggle button is ON
                    performActionOnToggleOn();
                } else {
                    // Toggle button is OFF
                    performActionOnToggleOff();
                }
            }
        });



    }

    private void initViews() {
        menu_BTN_Main.setOnClickListener(new View.OnClickListener()
                                         {
                                             @Override
                                             public void onClick(View v) {
                                                 moveToMainActivity();
                                             }
                                         }
        );

        menu_BTN_LeaderBoards.setOnClickListener(new View.OnClickListener()
                                                 {
                                                     @Override
                                                     public void onClick(View v) {
                                                         moveToLeaderBoardActivity();
                                                     }


                                                 }
        );

    }

    private void moveToLeaderBoardActivity() {
        Intent intent = new Intent(this, ActivityHighscore.class);
        startActivity(intent);
        finish();
    }

    private void performActionOnToggleOn() {
        if (callback != null) {
            callback.changeSensormode(true); // Fast mode speed
        }
    }

    private void performActionOnToggleOff() {
        if (callback != null) {
            callback.changeSensormode(false); // Slow mode speed
        }
    }

    private void moveToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); // Reorder MainActivity to the front
        startActivity(intent);
        finish(); // Close MenuActivity
    }



    private void findViews() {
        radioGroup = findViewById(R.id.radioGroup);
        radioButton_fast = findViewById(R.id.radioButton_fast);
        radioButton_slow = findViewById(R.id.radioButton_slow);
        menu_BTN_Main= findViewById(R.id.menu_BTN_Main);
        menu_ToggleMode=findViewById(R.id.menu_ToggleMode);
        menu_BTN_LeaderBoards=findViewById(R.id.menu_BTN_LeaderBoards);
    }


}
