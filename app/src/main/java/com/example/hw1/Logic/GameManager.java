package com.example.hw1.Logic;
import android.util.Log;

import java.util.Random;

public class GameManager {
    int rows = 4;
    int cols = 3;

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    int[][] objectMatrix = new int[rows][cols];
    int score = 0;
    public static int playerCol = 1; // Middle column
    private int lifeCount;

    public GameManager() {

        this.lifeCount=3;
        // Initialize the matrix
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                objectMatrix[i][j] = 0; // Example: filling with zeros
            }
        }
    }

    public GameManager(int lifeCount) {
        this.lifeCount = lifeCount;
    }

    public int spawnObjects(){
        Random random = new Random();
        int object= random.nextInt(3) + 1;
        objectMatrix[0][object-1]=1;
        return object;
    }

    public void updateObject(){

        for (int i = rows-1; i >=0; i--) {
            for (int j = cols-1; j >= 0; j--) {
                if (objectMatrix[i][j]==1)
                {
                    if(i+1==rows) {
                        checkIfHit();
                        objectMatrix[i][j] = 0;
                    }
                    else {
                        objectMatrix[i + 1][j] = 1;
                        objectMatrix[i][j] = 0;
                        Log.d("Game status:", "I have moved to" +i+j);

                    }
                }

            }
        }

    }



    public boolean checkObject(int row,int col)
    {
        return objectMatrix[row][col] == 1;
    }

    public int getScore() {
        return score;
    }

    public GameManager setScore(int score) {
        this.score = score;
        return this;
    }


    public int getPlayerCol() {
        return playerCol;
    }

    public static void setPlayerCol(int playerCol) {
        GameManager.playerCol = playerCol;
    }

    public int getLifeCount() {
        return lifeCount;
    }

    public GameManager setLifeCount(int lifeCount) {
        this.lifeCount = lifeCount;
        return this;
    }

    public boolean isGameLost(){
        return getLifeCount()==0;
    }

    private void checkIfHit(){

        for(int i=0;i<3;i++) {
            if (objectMatrix[rows - 1][i]== 1 && getPlayerCol()==i) {
                this.lifeCount--;
                Log.d("Game status:", "I have been hit :(");

            }


        }

    }

    public void playerMove(int move) {
        // Check if the move is valid within the bounds of the matrix
        if (move == 1 && playerCol < cols - 1) { // Moving right
            playerCol++; // Move the player to the right
        } else if (move == -1 && playerCol > 0) { // Moving left
            playerCol--; // Move the player to the left
        }
    }


}

