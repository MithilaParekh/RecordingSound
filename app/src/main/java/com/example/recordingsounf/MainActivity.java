package com.example.recordingsounf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    final int PERMISSION_CODE = 1000;
    Button recordstart, recordstop, play, stop;
    String pathSave = "";
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;

    private void setupMediaPlayer() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(pathSave);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, PERMISSION_CODE);
    }

    private boolean checkPermissionFromDevice() {
        int write_external_storage_result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int recor_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                recor_audio_result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkPermissionFromDevice())
            requestPermission();


        recordstart = findViewById(R.id.recordstart);
        recordstop = findViewById(R.id.recordstop);
        play = findViewById(R.id.play);
        stop = findViewById(R.id.stop);
        recordstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPermissionFromDevice()) {
                    pathSave = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + UUID.randomUUID().toString() + "_audio_record.3gp";
                    setupMediaPlayer();
                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    recordstop.setEnabled(true);
                    play.setEnabled(false);
                    stop.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "Recording Start", Toast.LENGTH_SHORT).show();
                } else {
                    requestPermission();
                }


            }
        });
        recordstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaRecorder.stop();
                recordstart.setEnabled(true);
                play.setEnabled(true);
                recordstop.setEnabled(true);
                stop.setEnabled(false);
                Toast.makeText(getApplicationContext(),"Recording Stop",Toast.LENGTH_SHORT).show();

            }
        });
play.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        stop.setEnabled(true);
        recordstart.setEnabled(false);
        recordstop.setEnabled(false);
        mediaPlayer=new MediaPlayer();
        try{
            mediaPlayer.setDataSource(pathSave);
            mediaPlayer.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }
        mediaPlayer.start();
        Toast.makeText(getApplicationContext(),"Start Playing",Toast.LENGTH_SHORT).show();

    }
});
stop.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        recordstart.setEnabled(true);
        play.setEnabled(true);
        recordstop.setEnabled(false);
        stop.setEnabled(false);
        if(mediaPlayer!=null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            setupMediaPlayer();
            Toast.makeText(getApplicationContext(),"Stop Playing",Toast.LENGTH_SHORT).show();
        }

    }
});
    }
}