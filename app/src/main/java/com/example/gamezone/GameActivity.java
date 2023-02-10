package com.example.gamezone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import java.util.Random;

/*
 * HangmanActivity adapted from Sue Smith's tutorial in envatotuts+
 * <https://code.tutsplus.com/series/create-a-hangman-game-for-android--cms-702>
 * Retrieved: November 2018
 * Title: Create a Hangman Game for Android
 * Author: Sue Smith
 */

public class GameActivity extends AppCompatActivity {

    //the words
    private String[] words;
    //random for word selection
    private Random rand;
    //store the current word
    private String currWord;
    //the layout holding the answer
    private LinearLayout wordLayout;
    //text views for each letter in the answer
    private TextView[] charViews;
    //letter button grid
    private GridView letters;
    //letter button adapter
    private LetterAdapter ltrAdapt;
    //body part images
    private ImageView[] bodyParts;
    //total parts
    private int numParts=6;
    //current part
    private int currPart;
    //num chars in word
    private int numChars;
    //num correct so far
    private int numCorr;
    //help
    private AlertDialog helpAlert;
    private int hintsRemaining = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        //read answer words in
        Resources res = getResources();
        words = res.getStringArray(R.array.words);

        //initialize random
        rand = new Random();
        //initialize word
        currWord="";


        //get answer area
        wordLayout = (LinearLayout)findViewById(R.id.word);

        //get letter button grid
        letters = (GridView)findViewById(R.id.letters);
        //set home as up



        //get body part images
        bodyParts = new ImageView[numParts];
        bodyParts[0] = (ImageView)findViewById(R.id.head);
        bodyParts[1] = (ImageView)findViewById(R.id.body);
        bodyParts[2] = (ImageView)findViewById(R.id.arm1);
        bodyParts[3] = (ImageView)findViewById(R.id.arm2);
        bodyParts[4] = (ImageView)findViewById(R.id.leg1);
        bodyParts[5] = (ImageView)findViewById(R.id.leg2);




        //start gameplay
        playGame();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_help:
                showHelp();
                return true;
            case R.id.hint:
                showHint();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //play a new game
    private void playGame(){

        //choose a word
        String newWord = words[rand.nextInt(words.length)];
        //make sure not same word as last time
        while(newWord.equals(currWord)) newWord = words[rand.nextInt(words.length)];
        //update current word
        currWord = newWord;

        //create new array for character text views
        charViews = new TextView[currWord.length()];


        //remove any existing letters
        wordLayout.removeAllViews();

        //loop through characters
        for(int c=0; c<currWord.length(); c++){
            charViews[c] = new TextView(this);
            //set the current letter
            charViews[c].setText(""+currWord.charAt(c));
            //set layout
            charViews[c].setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
            charViews[c].setGravity(Gravity.CENTER);
            charViews[c].setTextColor(Color.WHITE);
            charViews[c].setTextSize(30);
            charViews[c].setBackgroundResource(R.drawable.letter_bg);
            //add to display
            wordLayout.addView(charViews[c]);
        }

        //reset adapter
        ltrAdapt=new LetterAdapter(this);
        letters.setAdapter(ltrAdapt);

        //start part at zero
        currPart=0;
        //set word length and correct choices
        numChars=currWord.length();
        numCorr=0;

        //hide all parts
        for(int p=0; p<numParts; p++){
            bodyParts[p].setVisibility(View.INVISIBLE);
        }

    }

    //letter pressed method
    public void letterPressed(View view){
        //find out which letter was pressed
        String ltr=((TextView)view).getText().toString();
        char letterChar = ltr.charAt(0);
        //disable view
        view.setEnabled(false);
        view.setBackgroundResource(R.drawable.letter_down);
        //check if correct
        boolean correct=false;
        for(int k=0; k<currWord.length(); k++){
            if(currWord.charAt(k)==letterChar){
                correct=true;
                numCorr++;
                charViews[k].setTextColor(Color.BLACK);
            }
        }
        //check in case won
        if(correct){
            if(numCorr==numChars){
                //disable all buttons
                disableBtns();
                //let user know they have won, ask if they want to play again
                AlertDialog.Builder winBuild = new AlertDialog.Builder(this);
                winBuild.setTitle("YAY");
                winBuild.setMessage("You win!\n\nThe answer was:\n\n"+currWord);
                winBuild.setPositiveButton("Play Again",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                GameActivity.this.playGame();
                            }});
                winBuild.setNegativeButton("Exit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                GameActivity.this.finish();
                            }});
                winBuild.show();
            }
        }
        //check if user still has guesses
        else if(currPart<numParts){
            //show next part
            bodyParts[currPart].setVisibility(View.VISIBLE);
            currPart++;
        }
        else{
            //user has lost
            disableBtns();
            //let the user know they lost, ask if they want to play again
            AlertDialog.Builder loseBuild = new AlertDialog.Builder(this);
            loseBuild.setTitle("OOPS");
            loseBuild.setMessage("You lose!\n\nThe answer was:\n\n"+currWord);
            loseBuild.setPositiveButton("Play Again",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            GameActivity.this.playGame();
                        }});
            loseBuild.setNegativeButton("Exit",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            GameActivity.this.finish();
                        }});
            loseBuild.show();
        }
    }

    //disable letter buttons
    public void disableBtns(){
        int numLetters = letters.getChildCount();
        for(int l=0; l<numLetters; l++){
            letters.getChildAt(l).setEnabled(false);
        }
    }

    //show help information
    public void showHelp(){
        AlertDialog.Builder helpBuild = new AlertDialog.Builder(this);
        helpBuild.setTitle("Help");
        helpBuild.setMessage("Guess the word by selecting the letters.\n\n"
                + "You only have 6 wrong selections then it's game over!");
        helpBuild.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        helpAlert.dismiss();
                    }});
        helpAlert = helpBuild.create();
        helpBuild.show();
    }
    //show hint

    public void showHint() {
        // Check if there are any hints remaining

        if (hintsRemaining > 0) {

            // Pick a random letter from the current word that has not been revealed yet
            char hintLetter = ' ';

            do {
                hintLetter = currWord.charAt(rand.nextInt(currWord.length()));
            } while (charViews[currWord.indexOf(hintLetter)].getCurrentTextColor() == Color.BLACK);


            // Decrement the number of hints remaining
            hintsRemaining--;
            Toast.makeText(this, "hint Letter: "+hintLetter+" \n"+"hint Remaining: "+ hintsRemaining, Toast.LENGTH_LONG).show();



        } else {
            // Display a message if there are no hints remaining
            Toast.makeText(this, "No hints remaining", Toast.LENGTH_SHORT).show();
        }
    }





}
