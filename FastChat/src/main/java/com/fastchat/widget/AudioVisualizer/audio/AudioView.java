package com.fastchat.widget.AudioVisualizer.audio;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


import com.fastchat.Model.MediaMessage;
import com.fastchat.R;

import java.io.IOException;
import java.util.UUID;



public class AudioView implements AudioSlidePlayer.Listener, AudioWaveSeekBar.SeekBarChangeListener {

  private static final String TAG = AudioView.class.getSimpleName();

   private final @NonNull ImageView       playButton;
  private final @NonNull ImageView       pauseButton;
  private final @NonNull AudioWaveSeekBar         seekBar;
  private final @NonNull TextView        timestamp;

  private @Nullable AudioSlidePlayer   audioSlidePlayer;
  private int backwardsCounter;

  public int Progress=0;
  private final @NonNull AnimatingToggle controlToggle;

  Context context;
  public AudioView(Context context, View view) {
    this.context=context;
    this.controlToggle    = (AnimatingToggle)view. findViewById(R.id.control_toggle);
     this.playButton       = (ImageView)view. findViewById(R.id.playBtn);
    this.pauseButton      = (ImageView) view.findViewById(R.id.pause);
    this.seekBar          = (AudioWaveSeekBar)view. findViewById(R.id.seekBar);
    this.timestamp        = (TextView) view.findViewById(R.id.txtTime);
    this.timestamp.setText("00:00");
    this.playButton.setOnClickListener(new PlayClickedListener());
    this.pauseButton.setOnClickListener(new PauseClickedListener());
    this.seekBar.setWaveform(UUID.randomUUID().toString().getBytes());
     this.seekBar.setOnSeekBarChangeListener(this);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      //this.playButton.setImageDrawable(context.getDrawable(R.drawable.play_icon));
    //  this.pauseButton.setImageDrawable(context.getDrawable(R.drawable.pause_icon));
     //this.playButton.setBackground(context.getDrawable(R.drawable.ic_circle_fill_white_48dp));
     // this.pauseButton.setBackground(context.getDrawable(R.drawable.ic_circle_fill_white_48dp));
    }

   // setTint(getContext().getResources().getColor(R.color.audio_icon));
  }

  public void setAudio(final @NonNull MediaMessage audio, int duration)
  {
    controlToggle.displayQuick(playButton);
    seekBar.setEnabled(true);
    audioSlidePlayer = AudioSlidePlayer.createFor(context, audio, this);
    timestamp.setText(DateUtils.getFormatedDuration(duration));
  //  title.setText(audio.getAttachment().ge.get());
    /* if(audio.asAttachment().isVoiceNote() || !audio.getFileName().isPresent()) {
      title.setVisibility(View.GONE);
    }
    else {

    }*/
  }

  public void setDuration(int duration) {
    if (Progress==0)
      this.timestamp.setText(DateUtils.getFormatedDuration(duration));
  }

  public void cleanup() {
    if (this.audioSlidePlayer != null && pauseButton.getVisibility() == View.VISIBLE) {
      this.audioSlidePlayer.stop();
    }
  }

  @Override
  public void onReceivedDuration(int millis) {
    this.timestamp.setText(DateUtils.getFormatedDuration(millis));
  }

  @Override
  public void onStart() {
    if (this.pauseButton.getVisibility() != View.VISIBLE) {
      togglePlayToPause();
    }
  }

  @Override
  public void onStop() {
    if (this.playButton.getVisibility() != View.VISIBLE) {
      togglePauseToPlay();
    }

    /*if (seekBar.getProgress() + 5 >= seekBar.getMax()) {
      backwardsCounter = 4;
    }*/
    onProgress(0.0, -1);

  }



  @Override
  public void onProgress(double progress, long millis) {
    this.seekBar.setProgress((float) progress);
    if (millis != -1) {
      this.timestamp.setText(DateUtils.getFormatedDuration(millis));
    }
  }

  public void setTint(int foregroundTint) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      this.playButton.setBackgroundTintList(ColorStateList.valueOf(foregroundTint));
      this.pauseButton.setBackgroundTintList(ColorStateList.valueOf(foregroundTint));
    } else {
      this.playButton.setColorFilter(foregroundTint, PorterDuff.Mode.SRC_IN);
      this.pauseButton.setColorFilter(foregroundTint, PorterDuff.Mode.SRC_IN);
    }



  }



  private void togglePlayToPause() {
    controlToggle.displayQuick(pauseButton);

    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      AnimatedVectorDrawable playToPauseDrawable = (AnimatedVectorDrawable)getContext().getDrawable(R.drawable.play_to_pause_animation);
      pauseButton.setImageDrawable(playToPauseDrawable);
      playToPauseDrawable.start();
    }*/
  }

  private void togglePauseToPlay() {
    controlToggle.displayQuick(playButton);

    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      AnimatedVectorDrawable pauseToPlayDrawable = (AnimatedVectorDrawable)getContext().getDrawable(R.drawable.pause_to_play_animation);
      playButton.setImageDrawable(pauseToPlayDrawable);
      pauseToPlayDrawable.start();
    }*/
  }

  @Override
  public void OnSeekBarChangeListener(int progress) {
    try {
      Progress=progress;
      if (audioSlidePlayer != null && pauseButton.getVisibility() == View.VISIBLE) {
        audioSlidePlayer.play(progress);
      }
    } catch (IOException e) {
      Log.w(TAG, e);
    }
  }

  private class PlayClickedListener implements View.OnClickListener {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
      try {
        Log.w(TAG, "playbutton onClick");
        if (audioSlidePlayer != null) {
          togglePlayToPause();
          audioSlidePlayer.play(Progress);
        }
      } catch (IOException e) {
        Log.w(TAG, e);
      }
    }
  }

  private class PauseClickedListener implements View.OnClickListener {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
      Log.w(TAG, "pausebutton onClick");
      if (audioSlidePlayer != null) {
        togglePauseToPlay();
        audioSlidePlayer.stop();
      }
    }
  }

  private class SeekBarModifiedListener implements SeekBar.OnSeekBarChangeListener {
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}

    @Override
    public synchronized void onStartTrackingTouch(SeekBar seekBar) {
      if (audioSlidePlayer != null && pauseButton.getVisibility() == View.VISIBLE) {
        audioSlidePlayer.stop();
      }
    }

    @Override
    public synchronized void onStopTrackingTouch(SeekBar seekBar) {

    }
  }


}