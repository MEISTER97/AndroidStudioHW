package com.example.hw1.Logic;
import android.util.Log;

import java.util.Random;

public class GameManager {
    int rows = 6;
    int cols = 5;

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    int[][] objectMatrix = new int[rows][cols];
    int score;
    public static int playerCol=2 ; // Middle column
    private int lifeCount;

    public GameManager() {
        resetGame();
    }

    public void resetGame() {
        this.score = 0;
        playerCol = 2;
        this.lifeCount = 3;
        // Initialize the matrix
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                objectMatrix[i][j] = 0;
            }
        }
    }



    public GameManager(int lifeCount) {
        this.lifeCount = lifeCount;
    }


    public int spawnObjects() {
        Random random = new Random();
        int object = random.nextInt(5) + 1; // Generates a random number between 1 and 5
        if(objectMatrix[0][object - 1] ==0) {
            objectMatrix[0][object - 1] = 1; // Places the object in the first row at the random column
            return object; // Returns the 1-based column number
        }
        return -1;
    }

    public int spawnPoints() {
        Random random = new Random();
        int object = random.nextInt(5) + 1; // Generates a random number between 1 and 5
        if(objectMatrix[0][object - 1] ==0) {
            objectMatrix[0][object - 1] = 2; // Places the object in the first row at the random column
            return object; // Returns the 1-based column number
        }
        return -1;

    }


    public void updateObject() {
        for (int i = rows - 1; i >= 0; i--) {
            for (int j = cols - 1; j >= 0; j--) {
                if (objectMatrix[i][j] == 1) {
                    if (i + 1 == rows) {
                        // Object reaches bottom row
                        checkIfHit();
                        objectMatrix[i][j] = 0;
                    } else {
                        // Move object down
                        objectMatrix[i + 1][j] = 1;
                        objectMatrix[i][j] = 0;
                 //       Log.d("Game status:", "Object moved to (" + (i + 1) + "," + j + ")");
                    }
                } else if (objectMatrix[i][j] == 2) {
                    if (i + 1 == rows) {
                        // Point reaches bottom row
                        checkIfGotPoint();
                        objectMatrix[i][j] = 0;
                    } else {
                        // Move point down
                        objectMatrix[i + 1][j] = 2;
                        objectMatrix[i][j] = 0;
                //        Log.d("Game status:", "Point moved to (" + (i + 1) + "," + j + ")");
                    }
                }
            }
        }
    }




    public boolean checkObject(int row,int col)
    {
        return objectMatrix[row][col] == 1;
    }

    public boolean checkPoint(int row,int col)
    {
        return objectMatrix[row][col] == 2;
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

        for(int i=0;i<getCols();i++) {
            if (objectMatrix[rows - 1][i]== 1 && getPlayerCol()==i) {
                this.lifeCount--;
           //     Log.d("Game status:", "I have been hit :(");
                return;
            }


        }

    }

    private void checkIfGotPoint() {
        for (int i = 0; i < getCols(); i++) {
            if (objectMatrix[rows - 1][i] == 2 && getPlayerCol() == i) {
                score++;
             //   Log.d("Game status:", "num num ");
                return;
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

