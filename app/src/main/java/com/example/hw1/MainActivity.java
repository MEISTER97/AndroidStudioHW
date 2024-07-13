package com.example.hw1;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;

import com.example.hw1.Logic.GameManager;
import com.example.hw1.Utilities.SharedPreferencesManager;
import com.example.hw1.Utilities.TiltDetector;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.Random;

import com.example.hw1.Utilities.SoundPlayer;

import com.example.hw1.Interfaces.TiltCallback;
import com.example.hw1.Interfaces.callback_menuOptionForMain;

public class MainActivity extends AppCompatActivity implements callback_menuOptionForMain {
    private static MainActivity instance;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private MaterialTextView main_LBL_score;
    private MaterialTextView main_LBL_scoreMeter;
    private MaterialButton main_BTN_right;
    private MaterialButton main_BTN_left;
    private MaterialButton Main_BTN_startNewGame;
    private AppCompatImageView[] main_IMG_hearts;
    private AppCompatImageView[] main_IMG_Rex;
    private AppCompatImageView[] main_IMG_Cactus;
    private AppCompatImageView[] main_IMG_Hamburger;
    private ImageButton main_BTN_menu;

    private GameManager gameManager;
    private SoundPlayer soundPlayer;
    private TiltDetector tiltDetector;

    private long lastTiltXTime = 0;
    private long lastTiltYTime = 0;
    private static final long TILT_DELAY = 100; // 1000 milliseconds (1 second)

    private CountDownTimer objectCreationTimer;
    private CountDownTimer pointCreationTimer;
    private CountDownTimer refreshTimer;
    private boolean gameRun = false;
    private SharedPreferencesManager sharedPreferencesManager;


    private static long REFRESH_DELAY = 1000; // 1 second delay for refreshing UI
    private static long MIN_OBJECT_CREATION_DELAY = 2000; // Minimum 2 seconds delay for object creation
    private static long MAX_OBJECT_CREATION_DELAY = 4000; // Maximum 4 seconds delay for object creation
    final private static long MAXS_SPEED=500;
    final private static long  NORMAL_SPEED=1000;

    private Random random = new Random();

    private int life = 3;
    private int lifeIngame;
    private int score = 0;
    private int scoreMeter = 0;
    private boolean sensorMode = false;
    private ScoreData scoreData;
    private FusedLocationProviderClient fusedLocationProviderClient;


    private final boolean useMockLocation=false; // used for testing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferencesManager = SharedPreferencesManager.init(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        findViews();
        gameManager = new GameManager(main_IMG_hearts.length);
        initViews();
        gameIsRunning();

    }

    public static MainActivity getInstance() {
        return instance;
    }


    private void moveToMenuActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }


    private void setRefreshSpeed(long delay) {
        REFRESH_DELAY = delay;
    }

    private void restartRefreshTimer() {
        if (refreshTimer != null) {
            refreshTimer.cancel();
            refreshTimer=null;
            startRefreshTimer();
        }
        else {
            startRefreshTimer();
        }

        startObjectCreationTimer();
    }

    private void startTimers() {
        startRefreshTimer();
        startObjectCreationTimer();
    }

    private void stopTimner() {
        if (refreshTimer != null) {
            refreshTimer.cancel();
            refreshTimer = null; // Reset the timer instance
        }
        if (objectCreationTimer != null) {
            objectCreationTimer.cancel();
            objectCreationTimer = null; // Reset the timer instance
        }
        if (pointCreationTimer != null) {
            pointCreationTimer.cancel();
            pointCreationTimer = null;
        }
    }

    private void startRefreshTimer() {
        if (refreshTimer != null) {
            refreshTimer.cancel(); // Cancel the existing timer if running
            refreshTimer = null;
        }
        refreshTimer = new CountDownTimer(9999999, REFRESH_DELAY) { // Arbitrarily large countdown duration
            @Override
            public void onTick(long millisUntilFinished) {
                refreshUI();
            }

            @Override
            public void onFinish() {
            }
        }.start();
    }


    private void pauseTimers() {
        if (refreshTimer != null) {
            refreshTimer.cancel();
            refreshTimer = null; // Reset the timer instance
        }
        if (objectCreationTimer != null) {
            objectCreationTimer.cancel();
            objectCreationTimer = null; // Reset the timer instance
        }
        if (pointCreationTimer != null) {
            pointCreationTimer.cancel();
            pointCreationTimer = null;
        }

    }

    private void resumeTimers() {
        if (refreshTimer == null) {
            startRefreshTimer();
        }
        if (objectCreationTimer == null) {
            startObjectCreationTimer();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseTimers();
        if (soundPlayer != null) {
            soundPlayer.stopSound();
        }

        if (tiltDetector != null) {
            tiltDetector.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeTimers();
        if (sensorMode) {
            initTiltDetector();
        }
    }

    private void startObjectCreationTimer() {
        if(objectCreationTimer != null)
        {
            objectCreationTimer.cancel();
            objectCreationTimer=null;
            scheduleNextObjectCreation();
        }
        scheduleNextObjectCreation();

        if(pointCreationTimer!=null){
            pointCreationTimer.cancel();
            pointCreationTimer=null;
            scheduleNextPointCreation();
        }
        scheduleNextPointCreation();
    }


    private void scheduleNextObjectCreation() {

        long delay = MIN_OBJECT_CREATION_DELAY + random.nextInt((int) (MAX_OBJECT_CREATION_DELAY - MIN_OBJECT_CREATION_DELAY + 1));
        objectCreationTimer = new CountDownTimer(delay, delay) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                createObject();
                scheduleNextObjectCreation();
            }
        }.start();
    }

    private void scheduleNextPointCreation() {

        long delay = MIN_OBJECT_CREATION_DELAY + random.nextInt((int) (MAX_OBJECT_CREATION_DELAY - MIN_OBJECT_CREATION_DELAY + 1));
        pointCreationTimer = new CountDownTimer(delay, delay) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                createPoints();
                scheduleNextPointCreation();
            }
        }.start();
    }

    // if game start
    private void gameIsRunning() {
        gameRun = true;
        main_IMG_Rex[gameManager.getPlayerCol()].setVisibility(View.VISIBLE);
        startTimers();
    }


    private void initViews() {
        main_LBL_score.setText(String.valueOf(score));
        main_BTN_right.setOnClickListener(view -> moveRight());
        main_BTN_left.setOnClickListener(view -> moveLeft());
        main_BTN_menu.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 moveToMenuActivity();
                                             }
                                         }
        );


        Main_BTN_startNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartNewGame();
            }
        });

    }

    private void moveRight() {
        if (gameRun) {
            main_IMG_Rex[gameManager.getPlayerCol()].setVisibility(View.INVISIBLE);
            gameManager.playerMove(1);
            int playerPos = gameManager.getPlayerCol();
            main_IMG_Rex[playerPos].setVisibility(View.VISIBLE);
        }
    }

    private void moveLeft() {
        if (gameRun) {
            main_IMG_Rex[gameManager.getPlayerCol()].setVisibility(View.INVISIBLE);
            gameManager.playerMove(-1);
            int playerPos = gameManager.getPlayerCol();
            main_IMG_Rex[playerPos].setVisibility(View.VISIBLE);
        }
    }


    private void refreshUI() {
        // lost:
        if (gameManager.isGameLost()) {
            // show Lost
            stopGame();
            return;

        }
        // Update game objects
        gameManager.updateObject();

        updateScore();

        // Refresh the UI based on the updated game state
        movingObject();

        // Check remaining lives
        checklife();

        updateScoreMeter();
        // The game is still running
    }

    // stop the game and update score
    private void stopGame() {
        if(gameRun) {
            scoreData = new ScoreData();
            gameRun = false;
            resetGame();
            stopTimner();
            Log.d("Game status:", "GAME OVER " + gameManager.getScore());
            toastAndVibrate("GAME OVER ");
            scoreData.setScore(score);
            scoreData.setMeterScore(scoreMeter);
            getUserLocationGsonSave(scoreData);
        }
    }
    // reset locations in game
    private void resetGame() {

        main_IMG_Rex[gameManager.getPlayerCol()].setVisibility(View.INVISIBLE);
        for (int i = 0; i < 30; i++) {
            if (i < main_IMG_Cactus.length && main_IMG_Cactus[i] != null) {
                main_IMG_Cactus[i].setVisibility(View.INVISIBLE);
            }
            if (i < main_IMG_Hamburger.length && main_IMG_Hamburger[i] != null) {
                main_IMG_Hamburger[i].setVisibility(View.INVISIBLE);
            }
        }
        Main_BTN_startNewGame.setVisibility(View.VISIBLE);

    }
// create a new game
    private void StartNewGame() {
        Main_BTN_startNewGame.setVisibility(View.INVISIBLE);
        for (int i = 0; i < main_IMG_hearts.length; i++) {
            main_IMG_hearts[i].setVisibility(View.VISIBLE);
        }
        life = 3;
        gameManager.resetGame();
        score = 0;
        scoreMeter = 0;
        gameIsRunning();
    }
    // check if life got change from the gamemanager
    private void checklife() {
        lifeIngame = gameManager.getLifeCount();

        if (lifeIngame < life && gameRun) {
            if (life > 0) {
            main_IMG_hearts[lifeIngame].setVisibility(View.INVISIBLE);
                life--;
            }
            isHitSound();
        }


    }

    private void isHitSound() {
        soundPlayer = new SoundPlayer(this);
        soundPlayer.playSound(R.raw.punchsound);
    }

    private void updateScoreMeter() {
        scoreMeter++;
        main_LBL_scoreMeter.setText(String.valueOf(scoreMeter));
    }

    // move all objects per tick
    private void movingObject() {
        int rows = gameManager.getRows(); // 6
        int cols = gameManager.getCols(); // 5

        for (int i = 0; i < 30; i++) {
            if (i < main_IMG_Cactus.length && main_IMG_Cactus[i] != null) {
                main_IMG_Cactus[i].setVisibility(View.INVISIBLE);
            }
            if (i < main_IMG_Hamburger.length && main_IMG_Hamburger[i] != null) {
                main_IMG_Hamburger[i].setVisibility(View.INVISIBLE);
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

// Show ImageView elements where points exist
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int index = i * cols + j;
                if (index < main_IMG_Hamburger.length && gameManager.checkPoint(i, j) && main_IMG_Hamburger[index] != null) {
                    main_IMG_Hamburger[index].setVisibility(View.VISIBLE);
                }
            }
        }


    }

    //create objects
    private void createObject() {
        int object = gameManager.spawnObjects();
  //      Log.d("Object Created At:", "Object ID: " + object);

        if (object == -1)
            return;
        // Set the visibility for the spawned object
        if (object >= 1 && object <= 5) {
            main_IMG_Cactus[object - 1].setVisibility(View.VISIBLE);
        }
    }
    //create points
    private void createPoints() {
        int point = gameManager.spawnPoints();
   //     Log.d("Point Created At:", "point ID: " + point);

        if (point == -1)
            return;
        // Set the visibility for the spawned object
        if (point >= 1 && point <= 5) {
            main_IMG_Hamburger[point - 1].setVisibility(View.VISIBLE);
        }
    }

// update the scores
    private void updateScore() {

        if (score != gameManager.getScore()) {
            soundPlayer = new SoundPlayer(this);
            soundPlayer.playSound(R.raw.eatsound);
        }
        score = gameManager.getScore();

        // Convert the updated score back to a string
        String updatedScoreString = String.valueOf(score);

        // Set the updated score in the TextView
        main_LBL_score.setText(updatedScoreString);
    }

    private void toastAndVibrate(String text) {
        vibrate();
        toast(text);
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(500);

        }

    }


    private void findViews() {
        main_LBL_score = findViewById(R.id.main_LBL_score);
        main_LBL_scoreMeter = findViewById(R.id.main_LBL_scoreMeter);
        main_BTN_right = findViewById(R.id.main_BTN_right);
        main_BTN_left = findViewById(R.id.main_BTN_left);

        main_BTN_menu = findViewById(R.id.main_BTN_menu);
        Main_BTN_startNewGame = findViewById(R.id.Main_BTN_startNewGame);

        main_IMG_Rex = new AppCompatImageView[]{
                findViewById(R.id.main_IMG_REX1),
                findViewById(R.id.main_IMG_REX2),
                findViewById(R.id.main_IMG_REX3),
                findViewById(R.id.main_IMG_REX4),
                findViewById(R.id.main_IMG_REX5)

        };

        main_IMG_Cactus = new AppCompatImageView[30];
        for (int i = 0; i < 30; i++) {
            int resID = getResources().getIdentifier("main_IMG_Cactus" + (i + 1), "id", getPackageName());
            main_IMG_Cactus[i] = findViewById(resID);
        }

        main_IMG_Hamburger = new AppCompatImageView[30];
        for (int i = 0; i < 30; i++) {
            int resID = getResources().getIdentifier("main_IMG_Hamburger" + (i + 1), "id", getPackageName());
            main_IMG_Hamburger[i] = findViewById(resID);
        }


        main_IMG_hearts = new AppCompatImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)
        };
    }

    // change speen in click
    @Override
    public void changeSpeedClick(double speed, boolean fast) {
        if(gameRun) {
            if (fast) {
                setRefreshSpeed((long) speed);
                Log.d("Game status:", "Speed change to " + speed);
                MIN_OBJECT_CREATION_DELAY = 1000;
                MAX_OBJECT_CREATION_DELAY = 3000;
            } else {
                setRefreshSpeed((long) speed);
                Log.d("Game status:", "Speed change to " + speed);
                MIN_OBJECT_CREATION_DELAY = 2000;
                MAX_OBJECT_CREATION_DELAY = 4000;
            }
        }

    }

    // change to and from sensor mode
    @Override
    public void changeSensormode(boolean sensor) {
        if (sensor) {
            main_BTN_right.setVisibility(View.INVISIBLE);
            main_BTN_left.setVisibility(View.INVISIBLE);
            sensorMode = true;
        } else {
            main_BTN_right.setVisibility(View.VISIBLE);
            main_BTN_left.setVisibility(View.VISIBLE);
            sensorMode = false;
        }

    }

// tiltFUnction for x and y phone
    private void initTiltDetector() {
        tiltDetector = new TiltDetector(this,
                new TiltCallback() {

                    @Override
                    public void tiltX(int x) {
                        long currentTime = SystemClock.elapsedRealtime();
                        if (currentTime - lastTiltXTime > TILT_DELAY) {
                            lastTiltXTime = currentTime;
                            MovexPlayerSensor(x);
                        }
                    }

                    @Override
                    public void tiltY(int y) {
                        long currentTime = SystemClock.elapsedRealtime();
                        if (currentTime - lastTiltYTime > TILT_DELAY) {
                            lastTiltYTime = currentTime;
                            if (y == -1) {
                                changeFastSpeed();
                            } else if (y == 1) {
                                changeSlowSpeed();
                            }
                        }
                    }

                }

        );
        tiltDetector.start();
    }
    // move the player in sensormode
    private void MovexPlayerSensor(int x) {
        if (gameRun) {
            main_IMG_Rex[gameManager.getPlayerCol()].setVisibility(View.INVISIBLE);
            gameManager.playerMove(x);
            int playerPos = gameManager.getPlayerCol();
            main_IMG_Rex[playerPos].setVisibility(View.VISIBLE);
        }

    }
    //Changes speed linearly to fast speed
    private void changeFastSpeed(){
        if(gameRun) {

            if(MAXS_SPEED<REFRESH_DELAY) {
                REFRESH_DELAY = REFRESH_DELAY-100;
       //         Log.d("SPEED", "YAY FAST"+REFRESH_DELAY);
                startRefreshTimer();
            }

        }
        }

        //Changes speed linearly to normal speed
    private void changeSlowSpeed(){
        if(gameRun) {

            if(NORMAL_SPEED>REFRESH_DELAY) {
                REFRESH_DELAY = REFRESH_DELAY+100;
      //          Log.d("SPEED", "NAY SLOW"+REFRESH_DELAY);
                startRefreshTimer();
            }

        }
    }


    private void getUserLocationGsonSave(final ScoreData scoreData) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {

                    // used for testing mock
                    if (useMockLocation) {
                        // Use mock location for testing
                        Location mockLocation = new Location("mock_provider");
                        mockLocation.setLatitude(35.677583);  // Example mock latitude
                        mockLocation.setLongitude(139.772917);  // Example mock longitude
                        saveLocationData(scoreData, mockLocation);
                    } else {
                        Log.d("Location test", "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
                        scoreData.setLatitude(location.getLatitude());
                        scoreData.setLongitude(location.getLongitude());
                        sharedPreferencesManager.saveScoreData(scoreData);
                    }

                }
            }
        });
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        stopTimner();
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with getting the location
                getUserLocationGsonSave(scoreData);
            } else {
                // Permission denied, handle accordingly
                Log.d("Permissions", "Location permission denied");
            }
        }
    }

    // used for testing mock
    private void saveLocationData(ScoreData scoreData, Location location) {
        Log.d("Location 2 I got here", "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
        scoreData.setLatitude(location.getLatitude());
        scoreData.setLongitude(location.getLongitude());
        sharedPreferencesManager.saveScoreData(scoreData);
    }



}


