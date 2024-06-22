package com.example.hw1;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hw1.Logic.GameManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class MainActivity extends AppCompatActivity {
    private MaterialTextView main_LBL_score;
    private MaterialButton main_BTN_right;
    private MaterialButton main_BTN_left;
    private AppCompatImageView[] main_IMG_hearts;
    private AppCompatImageView[] main_IMG_Rex;
    private AppCompatImageView[] main_IMG_Cactus;
    private GameManager gameManager;

    private boolean gameRun=false;
    private boolean flag=false;


    private final Handler handler = new Handler(Looper.getMainLooper());

    private int score=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        gameManager=new GameManager(main_IMG_hearts.length);
        initViews();
        gameIsRunning();

    }

    private void gameIsRunning() {
        gameRun = true;
        flag=true;
        main_IMG_Rex[1].setVisibility(View.VISIBLE);
        startRefreshing();
        startCreatingObjects();
        refreshUI();
    }

    private void startRefreshing() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (gameRun) {



                    refreshUI();
                    handler.postDelayed(this, 1500); // Schedule next refresh in 1.5 second
                }
            }
        }, 1500); // Initial delay of 1.5 second
    }

    private void initViews() {
        main_LBL_score.setText(String.valueOf(score));
        main_BTN_right.setOnClickListener(view -> moveRight());
        main_BTN_left.setOnClickListener(view -> moveLeft());
    }

    private void moveRight(){
        main_IMG_Rex[gameManager.getPlayerCol()].setVisibility(View.INVISIBLE);
        gameManager.playerMove(1);
        int playerPos=gameManager.getPlayerCol();
        main_IMG_Rex[playerPos].setVisibility(View.VISIBLE);

    }

    private void moveLeft(){
        main_IMG_Rex[gameManager.getPlayerCol()].setVisibility(View.INVISIBLE);
        gameManager.playerMove(-1);
        int playerPos=gameManager.getPlayerCol();
        main_IMG_Rex[playerPos].setVisibility(View.VISIBLE);
    }

    private void refreshUI() {
        // lost:
        if (gameManager.isGameLost() && flag) {
            // show Lost
            stopGame();
            flag=false;
            //     return;
        }
        // Update game objects
        gameManager.updateObject();

        // Refresh the UI based on the updated game state
        movingObject();

        // Check remaining lives
        checklife();
        // The game is still running, continue updates if necessary
    }

    private void stopGame(){
     //   gameRun=false;
     //   handler.removeCallbacksAndMessages(null); // Remove all callbacks

        Log.d("Game status:", "GAME OVER " + gameManager.getScore());
        toastAndVibrate("GAME OVER ");
    }
    
    private void checklife(){

       int life= gameManager.getLifeCount();
       if(life != 3 && flag) {
           main_IMG_hearts[life].setVisibility(View.INVISIBLE);
       }

    }
    


    private void startCreatingObjects() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (gameRun) {
                    createObject();
                    handler.postDelayed(this, 4000); // Schedule next object creation in 4 seconds

                }
            }
        }, 4000); // Initial delay of 3 seconds
    }

    private void movingObject() {
        int rows = gameManager.getRows();
        int cols = gameManager.getCols();

        // Hide all ImageView elements in main_IMG_Cactus array
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int index = i * cols + j;
                if (index < main_IMG_Cactus.length && main_IMG_Cactus[index] != null) {
                    main_IMG_Cactus[index].setVisibility(View.INVISIBLE);
                }
            }
        }

        // Show ImageView elements where objects exist
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int index = i * cols + j;
                if (index < main_IMG_Cactus.length && gameManager.checkObject(i, j) && main_IMG_Cactus[index] != null) {
                    main_IMG_Cactus[index].setVisibility(View.VISIBLE);
                }
            }
        }
    }



    private void createObject() {
        int object = gameManager.spawnObjects();

        switch (object) {
            case 1:
                main_IMG_Cactus[0].setVisibility(View.VISIBLE);
                break;

            case 2:
                main_IMG_Cactus[1].setVisibility(View.VISIBLE);
                break;

            case 3:
                main_IMG_Cactus[2].setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }
    }

        private void toastAndVibrate(String text)
        {
            vibrate();
            toast(text);
        }

    private void toast(String text) {
        Toast.makeText(this,text, Toast.LENGTH_LONG).show();
    }

    private void vibrate() {
        Vibrator v=(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            v.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
        }
        else {
            v.vibrate(500);

        }

    }


    private void findViews(){
        main_LBL_score=findViewById(R.id.main_LBL_score);

        main_BTN_right= findViewById(R.id.main_BTN_right);
        main_BTN_left=findViewById(R.id.main_BTN_left);

        main_IMG_Rex = new AppCompatImageView[]{
                findViewById(R.id.main_IMG_REX1),
                findViewById(R.id.main_IMG_REX2),
                findViewById(R.id.main_IMG_REX3)
        };

        main_IMG_Cactus = new AppCompatImageView[]{
                findViewById(R.id.main_IMG_Cactus1),
                findViewById(R.id.main_IMG_Cactus2),
                findViewById(R.id.main_IMG_Cactus3),
                findViewById(R.id.main_IMG_Cactus4),
                findViewById(R.id.main_IMG_Cactus5),
                findViewById(R.id.main_IMG_Cactus6),
                findViewById(R.id.main_IMG_Cactus7),
                findViewById(R.id.main_IMG_Cactus8),
                findViewById(R.id.main_IMG_Cactus9),
                findViewById(R.id.main_IMG_Cactus10),
                findViewById(R.id.main_IMG_Cactus11),
                findViewById(R.id.main_IMG_Cactus12)
        };


        main_IMG_hearts = new AppCompatImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)
        };
    }


}


