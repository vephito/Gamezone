package com.example.gamezone;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private AlertDialog helpAlert;
    private MediaPlayer mediaPlayer;
    boolean otn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create(this, R.raw.game_audio);

        ImageView playBtn = findViewById(R.id.playBtn);
        ImageView playBtn_ttt = findViewById(R.id.playBtn_ttt);
        playBtn_ttt.setOnClickListener(this);
        playBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.playBtn) {
            mediaPlayer.stop();
            Intent playIntent = new Intent(this, GameActivity.class);
            this.startActivity(playIntent);
        }
        if (view.getId() == R.id.playBtn_ttt) {
            mediaPlayer.stop();
            Intent playIntent = new Intent(this, ttt_Activity.class);
            this.startActivity(playIntent);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.audio, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.audio:

                // Change the icon of the menu item
                if (otn) {
                    item.setIcon(R.drawable.ic_volumn_down);
                    otn = false;
                    mediaPlayer.stop();
                } else {
                    item.setIcon(R.drawable.ic_volumn_up);
                    otn = true;
                    audio();
                }
                return true;
            default:

                return super.onOptionsItemSelected(item);

        }
    }

    public void audio() {

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }


    }
}


