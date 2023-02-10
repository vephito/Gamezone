package com.example.gamezone;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ttt_Activity extends AppCompatActivity implements View.OnClickListener{
    /*
    * Player 1 and Player 2 Score Display
    * */
    private TextView txtPlayer1Score;
    private TextView txtPlayer2Score;
    /*
    * Displays the game Status after the games has ended
    * */
    private TextView txtStatus;
    /*
    * Buttons for Play Again and Reset
    * */
    private Button btnPlayAgain,btnResetGame;
    /*
    * The Button of all the moves made in the game
    * */
    private Button[] buttons = new Button[9];
    /*
    * Keep tracks of player 1 and player 2 Score
    * */
    private int player1ScoreCount, player2ScoreCount;
    /*
    * To keep track of Players turn
    * */
    boolean playerActive;
    /*
    * Keep track of the number of rounds played
    * */
    int rounds;
    /*
    * Keep track of the state of each button
    * */
    int[] gameState = {2,2,2,2,2,2,2,2,2};
    /*
    * All the possible winning combinations
    * */
    int[][] winState = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ttt_activity_main);



        /*
        * Initialize UI elements
        * */
        txtPlayer1Score = findViewById(R.id.txtPlayer1Score);
        txtPlayer2Score = findViewById(R.id.txtPlayer2Score);
        txtStatus = findViewById(R.id.txtStatus);

        buttons[0] = findViewById(R.id.btn0);
        buttons[1] = findViewById(R.id.btn1);
        buttons[2] = findViewById(R.id.btn2);
        buttons[3] = findViewById(R.id.btn3);
        buttons[4] = findViewById(R.id.btn4);
        buttons[5] = findViewById(R.id.btn5);
        buttons[6] = findViewById(R.id.btn6);
        buttons[7] = findViewById(R.id.btn7);
        buttons[8] = findViewById(R.id.btn8);
        for(int i=0;i<buttons.length;i++){
            buttons[i].setOnClickListener(this);
        }

        btnPlayAgain = findViewById(R.id.btnPlayAgain);
        btnResetGame = findViewById(R.id.btnResetGame);
        player1ScoreCount = 0;
        player2ScoreCount = 0;
        playerActive = true;
        rounds = 0;
    }
    @Override
    public void onClick(View view){
        if(!((Button)view).getText().toString().equals("")){
            return;
        }else if(checkWinner()){
            return;
        }
        String buttonID = view.getResources().getResourceEntryName(view.getId());
        int gameStatePointer = Integer.parseInt(buttonID.substring(buttonID.length()-1,buttonID.length()));

        if(playerActive){
            ((Button)view).setText("X");
            ((Button)view).setTextColor(Color.parseColor("#ffc34a"));
            gameState[gameStatePointer] = 0;
        }else{
            ((Button)view).setText("O");
            ((Button)view).setTextColor(Color.parseColor("#70fc3a"));
            gameState[gameStatePointer] = 1;
        }
        rounds++;
        if(checkWinner()){
            if(playerActive){
                player1ScoreCount++;
                updatePlayerScore();
                txtStatus.setText("PLayer 1 has won");
            }else{
                player2ScoreCount++;
                updatePlayerScore();
                txtStatus.setText("PLayer 2 has won");
            }
        }else if(rounds==9){
            txtStatus.setText("No Winner");
        }
        else{
            playerActive = !playerActive;
        }
        /*
        * Reset the Scores back to 0
        * */
        btnResetGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAgain();
                player1ScoreCount = 0;
                player2ScoreCount = 0;
                updatePlayerScore();

            }
        });
        btnPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAgain();
            }
        });
    }
    /*
    * Clears the screen and board
    * */
    private void playAgain(){
        rounds = 0;
        playerActive = true;
        for(int i=0;i<buttons.length;i++){
            gameState[i] = 2;
            buttons[i].setText("");
        }
        txtStatus.setText("");
    }
    /*
    * Checks if there is a winner
    * */
    private boolean checkWinner(){
        boolean winner = false;
        for(int[] winState: winState){
            if(gameState[winState[0]] == gameState[winState[1]]&&
                    gameState[winState[1]] == gameState [winState[2]] &&
                    gameState[winState[0]]!=2){
                winner = true;
            }
        }
        return winner;
    }
    /*
    * Updates players Score
    * */
    private void updatePlayerScore(){
        txtPlayer1Score.setText(Integer.toString(player1ScoreCount));
        txtPlayer2Score.setText(Integer.toString(player2ScoreCount));
    }

}
