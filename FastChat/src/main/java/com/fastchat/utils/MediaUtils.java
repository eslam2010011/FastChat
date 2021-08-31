package com.fastchat.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.Vibrator;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;


import com.fastchat.BuildConfig;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.fastchat.Core.FastChat.getContext;


public class MediaUtils {

    private static Activity activity;

    public static String pictureImagePath;

    public static Uri uri;

    private MediaRecorder mediaRecorder;
    private String audioFileNameWithPath;

    public MediaUtils() {

    }


    public void startRecording(String audioFileNameWithPath) {
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(audioFileNameWithPath);
            try {
                mediaRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
          /*  timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    int currentMaxAmp = 0;
                    try {
                        currentMaxAmp = mediaRecorder != null ? mediaRecorder.getMaxAmplitude() : 0;
                        audioRecordView.update(currentMaxAmp);
                        if (mediaRecorder==null)
                            timer = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }}, 0, 100);*/
            mediaRecorder.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopRecording(boolean isCancel,String audioFileNameWithPath) {
        try {
            if (mediaRecorder != null) {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                if (isCancel) {
                    new File(audioFileNameWithPath).delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void openFile(String url, String textType, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(url), textType);
        context.startActivity(intent);
    }



    private static Uri getCaptureImageOutputUri() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(storageDir.getAbsolutePath() + "/" + imageFileName);
        return Uri.fromFile(file);
    }

    public static String handleCameraImage() {
        return pictureImagePath;
    }


    public static File makeEmptyFileWithTitle(String title) {
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        return new File(root, title);
    }


    public static void playSendSound(Context context, int ringId) {
        MediaPlayer mMediaPlayer = MediaPlayer.create(context, ringId);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.start();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            }
        });

    }

    public static void vibrate(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);

    }
}