package com.example.mp3chandu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.sql.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ImageView play,imageView,prev,next;
    TextView songTitle;
    SeekBar mSeekBarVol,mSeekBarTime;
    static MediaPlayer mMediaPlayer;
    private Runnable runnable;
    private AudioManager mAudioManager;
    int currentIndex =0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = findViewById(R.id.play);
        imageView = findViewById(R.id.image_view);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        songTitle = findViewById(R.id.song_title);
        mSeekBarTime = findViewById(R.id.seek_bar_title);
        mSeekBarVol = findViewById(R.id.seek_bar_volume);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        final ArrayList<Integer> songs = new ArrayList<>();
        songs.add(0, R.raw.mp3_icone);
        songs.add(1, R.raw.mp3_icone1);


        mMediaPlayer = MediaPlayer.create(getApplicationContext(),songs.get(currentIndex));










        int maxV = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        int curV = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mSeekBarVol.setMax(maxV);
        mSeekBarVol.setProgress(curV);

        mSeekBarVol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBarTime.setMax(mMediaPlayer.getDuration());
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    play.setImageResource(R.drawable.ic_play);
                } else {

                    mMediaPlayer.start();
                    play.setImageResource(R.drawable.ic_pause);
                }
                songsDetails();
            }



        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer != null){
                    play.setImageResource(R.drawable.ic_pause);
                }
                if (currentIndex < songs.size()-1){
                    currentIndex ++;
                }else{
                    currentIndex = 0;
                }
                if (mMediaPlayer.isPlaying()){
                    mMediaPlayer.stop();
                }
                mMediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex));
                mMediaPlayer.start();
                songsDetails();

            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer != null){
                    play.setImageResource(R.drawable.ic_pause);
                }
                if(currentIndex > 0){
                    currentIndex--;

                }
                else
                {
                    currentIndex = songs.size()-1;
                }
                if (mMediaPlayer.isPlaying()){
                    mMediaPlayer.stop();
                }
                mMediaPlayer = MediaPlayer.create(getApplicationContext(),songs.get(currentIndex));
                mMediaPlayer.start();
                songsDetails();
            }
        });

        mSeekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    mMediaPlayer.seekTo(progress);
                    mSeekBarTime.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
            private void songsDetails() {

                if (currentIndex == 0){
                    songTitle.setText("mp3_icone");
                    imageView.setImageResource(R.drawable.ic_image_icon);
                }
                if (currentIndex == 1){
                    songTitle.setText("mp3_icom1");
                    imageView.setImageResource(R.drawable.image_icone);
                }
                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mSeekBarTime.setMax(mMediaPlayer.getDuration());
                        mMediaPlayer.start();

                    }
                });

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (mMediaPlayer != null){
                            try {
                                if (mMediaPlayer.isPlaying()){
                                    Message message = new Message();
                                    message.what=mMediaPlayer.getCurrentPosition();
                                    handler.sendMessage(message);
                                    Thread.sleep(1000);
                                }
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();

    }
    @SuppressLint("HandlerLeak")

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            mSeekBarTime.setProgress(msg.what);

        }
    };
}