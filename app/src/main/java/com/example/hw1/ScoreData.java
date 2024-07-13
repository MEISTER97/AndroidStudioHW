package com.example.hw1;

public class ScoreData {
    private int score;
    private int meterScore;
    private double latitude ;
    private double longitude ;

    public ScoreData() {
        this.meterScore=0;
        this.score=0;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getMeterScore() {
        return meterScore;
    }

    public void setMeterScore(int meterScore) {
        this.meterScore = meterScore;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
