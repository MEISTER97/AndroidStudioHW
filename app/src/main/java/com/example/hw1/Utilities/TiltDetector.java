package com.example.hw1.Utilities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.example.hw1.Interfaces.TiltCallback;

public class TiltDetector {
    private static final String TAG = "TiltDetector";

    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;

    private long timestamp = 0L;
    private int moveCountX = 0;
    private int moveCountY = 0;

    private TiltCallback tiltCallback;

    public TiltDetector(Context context, TiltCallback tiltCallback) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        } else {
            Log.e(TAG, "SensorManager is null. Cannot initialize TiltDetector.");
            return;
        }
        this.tiltCallback = tiltCallback;
        initEventListener();
    }

    private void initEventListener() {
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    float x = event.values[0];
                    float y = event.values[1];
                    calculateMove(x, y);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // Not used in this implementation
            }
        };
    }

    private void calculateMove(float x, float y) {
        if (System.currentTimeMillis() - timestamp > 500) {
            timestamp = System.currentTimeMillis();
            } if (x > 3.0) {
                if (tiltCallback != null) {
                    tiltCallback.tiltX(-1); // Moderate positive tilt
                    Log.d(TAG, "Moved left");
                }

            } else if (x < -3.0) {
                if (tiltCallback != null) {
                    tiltCallback.tiltX(1); // Moderate negative tilt
                    Log.d(TAG, "Moved right");
                }
            }
        if (y > 3.0) {
        if (tiltCallback != null) {
            tiltCallback.tiltY(1); // Moderate positive tilt
            Log.d(TAG, "Moved backward");
        }

     } else if (y < -3.0) {
        if (tiltCallback != null) {
            tiltCallback.tiltY(-1); // Moderate negative tilt
            Log.d(TAG, "Moved forward");
        }
    }

    }

    public void start() {
        if (sensorManager != null && sensor != null) {
            sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            resetMoveCounts();
        } else {
            Log.e(TAG, "Cannot start TiltDetector. SensorManager or Sensor is null.");
        }
    }

    public void stop() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(sensorEventListener);
        }
    }

    private void resetMoveCounts() {
        moveCountX = 0;
        moveCountY = 0;
    }

    public int getMoveCountX() {
        return moveCountX;
    }

    public int getMoveCountY() {
        return moveCountY;
    }
}
