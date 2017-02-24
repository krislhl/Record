package com.kris.record;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Kris on 2017/2/23.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MediaRecorder mediaRecorder;
    private String FILE_PATH;
    private SoundView soundView;
    private Timer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        soundView = (SoundView) findViewById(R.id.sv_sound);
        findViewById(R.id.btn_record).setOnClickListener(this);
        findViewById(R.id.btn_play).setOnClickListener(this);
        FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"recorder.mp3";
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_play:
                if (mediaRecorder != null) {
                    if (timer != null){
                        timer.cancel();
                    }
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    soundView.stopDraw();
                }
                break;
            case R.id.btn_record:
                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mediaRecorder.setOutputFile(FILE_PATH);
                try {
                    mediaRecorder.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaRecorder.start();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        float maxAmplitude = mediaRecorder.getMaxAmplitude() /  3276f +1;
                        Log.e(MainActivity.this.getClass().getSimpleName(),maxAmplitude+"------");
                        soundView.addData(maxAmplitude);
                    }
                },0,300);
                soundView.startDraw();
                break;
        }
    }
}
