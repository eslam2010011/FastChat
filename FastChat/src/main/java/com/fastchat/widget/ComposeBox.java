package com.fastchat.widget;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.fastchat.Model.BaseMessage;
import com.fastchat.Model.FastChatConstants;
import com.fastchat.Model.LinkMessage;
import com.fastchat.Model.MediaMessage;
import com.fastchat.Model.ReplyMessageG;
import com.fastchat.Model.TextMessage;
import com.fastchat.R;
import com.fastchat.utils.Utils;
import com.fastchat.widget.AudioVisualizer.AudioRecordView;
import com.fastchat.widget.richlinkpreview.RichLinkView;

import com.fastchat.widget.richlinkpreview.ViewListener;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;


public class ComposeBox extends RelativeLayout implements View.OnClickListener {

    private AudioRecordView audioRecordView;

    private MediaRecorder mediaRecorder;

    private MediaPlayer mediaPlayer;

    private Runnable timerRunnable;

    private Handler seekHandler = new Handler(Looper.getMainLooper());

    private Timer timer = new Timer();

    private String audioFileNameWithPath;

    private boolean isOpen, reply, islink, isRecording, isPlaying, voiceMessage;

    public ImageView ivMic, ivCamera, ivGallery, ivFile, ivSend, ivDelete;

    private SeekBar voiceSeekbar;

    private Chronometer recordTime;

    public EmojiEditText etComposeBox;

    private RelativeLayout composeBox;

    private CardView flBox;

    private RelativeLayout voiceMessageLayout, rl_resend_root_img;

    //private RelativeLayout rlActionContainer;

    EmojiPopup emojiPopup;

    private boolean hasFocus;

    private ComposeActionListener composeActionListener;

    private Context context;

    ImageView imageViewEmoji;
    private int color;
    public CardView imageViewSend, imageViewAudio;
    boolean isTyping;
    ImageView resend_image, resend_clear_img;
    TextView tv_resend_name, resend_type;
    BaseMessage baseMessage;
    RichLinkView richLinkView;

    public ComposeBox(Context context) {
        super(context);
        initViewComponent(context, null, -1, -1);
    }

    public ComposeBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewComponent(context, attrs, -1, -1);
    }

    public ComposeBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViewComponent(context, attrs, defStyleAttr, -1);
    }

    private void initViewComponent(Context context, AttributeSet attributeSet, int defStyleAttr, int defStyleRes) {

        View view = View.inflate(context, R.layout.layout_compose_box, null);

        TypedArray a = getContext().getTheme().obtainStyledAttributes(attributeSet, R.styleable.ComposeBox, 0, 0);
        color = a.getColor(R.styleable.ComposeBox_color, getResources().getColor(R.color.colorPrimary));
        addView(view);

        this.context = context;

        ViewGroup viewGroup = (ViewGroup) view.getParent();
        viewGroup.setClipChildren(false);

        mediaPlayer = new MediaPlayer();
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager.isMusicActive()) {
            audioManager.requestAudioFocus(new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {

                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        stopRecording(true);
                    }
                }
            }, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }
        imageViewSend = this.findViewById(R.id.imageViewSend);
        imageViewAudio = this.findViewById(R.id.imageViewAudio);
        composeBox = this.findViewById(R.id.message_box);
        flBox = this.findViewById(R.id.flBox);
        ivMic = this.findViewById(R.id.ivMic);
        ivDelete = this.findViewById(R.id.ivDelete);
        audioRecordView = this.findViewById(R.id.record_audio_visualizer);
        voiceMessageLayout = this.findViewById(R.id.voiceMessageLayout);
        recordTime = this.findViewById(R.id.record_time);
        voiceSeekbar = this.findViewById(R.id.voice_message_seekbar);
        ivCamera = this.findViewById(R.id.ivCamera);
        ivGallery = this.findViewById(R.id.ivImage);
        //  ivAudio = this.findViewById(R.id.ivAudio);
        ivFile = this.findViewById(R.id.ivFile);
        ivSend = this.findViewById(R.id.ivSend);
        // ivArrow=this.findViewById(R.id.ivArrow);
        etComposeBox = this.findViewById(R.id.etComposeBox);
        rl_resend_root_img = this.findViewById(R.id.rl_resend_root_img);
        imageViewEmoji = this.findViewById(R.id.imageViewEmoji);
        richLinkView = this.findViewById(R.id.richLink);



        resend_image = view.findViewById(R.id.resend_image);
        tv_resend_name = view.findViewById(R.id.tv_resend_name);
        resend_type = view.findViewById(R.id.resend_type);
        resend_clear_img = view.findViewById(R.id.resend_clear_img);


        emojiPopup = EmojiPopup.Builder.fromRootView(this)
                .setKeyboardAnimationStyle(R.style.emoji_fade_animation_style)
                .build(etComposeBox);

        imageViewEmoji.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                emojiPopup.toggle();
            }
        });
        // rlActionContainer=this.findViewById(R.id.rlActionContainers);


        //  ivAudio.setOnClickListener(this);
        // ivArrow.setOnClickListener(this);
        ivSend.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        ivFile.setOnClickListener(this);
        ivMic.setOnClickListener(this);
        ivGallery.setOnClickListener(this);
        ivCamera.setOnClickListener(this);


        etComposeBox.addTextChangedListener(new TextWatcher() {
            private Timer timer = new Timer();
            private final long DELAY = 2500;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (composeActionListener != null) {
                    composeActionListener.beforeTextChanged(charSequence, i, i1, i2);
                }

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    isTyping = true;
                    Log.d("isText", "started typing");
                    if (composeActionListener != null) {
                        composeActionListener.IsTyping(isTyping);
                    }

                }
                if (composeActionListener != null) {
                    composeActionListener.onTextChanged(charSequence, i, i1, i2);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (composeActionListener != null) {
                    composeActionListener.afterTextChanged(editable);
                }

                if (!isTyping) {
                    Log.d("isText", "started typing");
                    // Send notification for start typing event
                    isTyping = true;
                }
                timer.cancel();
                timer = new Timer();
                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                isTyping = false;
                                Log.d("isText", "stopped typing");
                                if (composeActionListener != null) {
                                    composeActionListener.IsTyping(isTyping);
                                }

                            }
                        },
                        DELAY
                );
                if (Pattern.compile("(^|[\\s.:;?\\-\\]<\\(])" +
                        "((https?://|www\\.|pic\\.)[-\\w;/?:@&=+$\\|\\_.!~*\\|'()\\[\\]%#,☺]+[\\w/#](\\(\\))?)" +
                        "(?=$|[\\s',\\|\\(\\).:;?\\-\\[\\]>\\)])").matcher(editable.toString()).matches()) {
                    islink = true;
                    resend_type.setVisibility(GONE);
                    richLinkView.setVisibility(VISIBLE);
                    rl_resend_root_img.setVisibility(VISIBLE);
                    richLinkView.setLink(editable.toString(), new ViewListener() {
                        @Override
                        public void onSuccess(boolean b) {

                        }

                        @Override
                        public void onError(@NotNull Exception e) {

                        }
                    });
                    resend_clear_img.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                             ComposeBox.this.islink = false;
                            rl_resend_root_img.setVisibility(GONE);
                        }
                    });
                    richLinkView.setDefaultClickListener(false);
                }


            }
        });


        a.recycle();
    }

    public void setText(String text) {
        etComposeBox.setText(text);
    }

    public void setColor(int color) {


    }

    public void setComposeBoxListener(ComposeActionListener composeActionListener) {
        this.composeActionListener = composeActionListener;

        this.composeActionListener.getCameraActionView(ivCamera);
        this.composeActionListener.getGalleryActionView(ivGallery);
        this.composeActionListener.getFileActionView(ivFile);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivDelete) {
            stopRecording(true);
            stopPlayingAudio();
            flBox.setVisibility(VISIBLE);
            voiceMessageLayout.setVisibility(GONE);
            etComposeBox.setVisibility(View.VISIBLE);
            imageViewAudio.setVisibility(View.VISIBLE);
            imageViewSend.setVisibility(View.GONE);
            //ivArrow.setVisibility(View.VISIBLE);
            ivMic.setVisibility(View.VISIBLE);
            ivMic.setImageDrawable(getResources().getDrawable(R.drawable.record_audio_ic));
            isPlaying = false;
            isRecording = false;
            voiceMessage = false;
            ivDelete.setVisibility(GONE);
            ivSend.setVisibility(View.GONE);
        }
        if (view.getId() == R.id.ivCamera) {
            composeActionListener.onCameraActionClicked(ivCamera);
        }
        if (view.getId() == R.id.ivImage) {
            composeActionListener.onGalleryActionClicked(ivGallery);
        }
        if (view.getId() == R.id.ivSend) {
            if (!voiceMessage && !reply && !islink) {
                composeActionListener.onSendActionClicked(etComposeBox);
            } else if (reply) {
                composeActionListener.onReplyComplete(baseMessage, etComposeBox, reply);
                imageViewAudio.setVisibility(View.GONE);
                imageViewSend.setVisibility(View.VISIBLE);
            } else if (islink) {
                composeActionListener.onLinkComplete(baseMessage, etComposeBox, reply);
                islink=false;
                rl_resend_root_img.setVisibility(GONE);
                richLinkView.setVisibility(GONE);
                imageViewAudio.setVisibility(View.GONE);
                imageViewSend.setVisibility(View.VISIBLE);
            } else {
                composeActionListener.onVoiceNoteComplete(audioFileNameWithPath);
                audioFileNameWithPath = "";
                voiceMessageLayout.setVisibility(GONE);
                etComposeBox.setVisibility(View.VISIBLE);
                imageViewSend.setVisibility(GONE);
                // ivArrow.setVisibility(View.VISIBLE);
                imageViewAudio.setVisibility(View.VISIBLE);
                isRecording = false;
                isPlaying = false;
                voiceMessage = false;
                flBox.setVisibility(View.VISIBLE);
                ivMic.setImageResource(R.drawable.ic_record_audio_ic);
            }

        }
        if (view.getId() == R.id.ivAudio) {
            //  composeActionListener.onAudioActionClicked(ivAudio);
        }
        if (view.getId() == R.id.ivFile) {
            composeActionListener.onFileActionClicked(ivFile);
        }
        if (view.getId() == R.id.ivArrow) {
            if (isOpen) {
                closeActionContainer();
            } else {
                openActionContainer();
            }
        }
        if (view.getId() == R.id.ivMic) {
            if (Utils.hasPermissions(context, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE})) {

                if (isOpen) {
                    closeActionContainer();
                }
                if (!isRecording) {
                    flBox.setVisibility(GONE);
                    startRecord();
                    ivMic.setImageResource(R.drawable.ic_stop_24dp);
                    isRecording = true;
                    isPlaying = false;
                } else {
                    if (isRecording && !isPlaying) {
                        isPlaying = true;
                        stopRecording(false);
                        recordTime.stop();
                    }
                    ivMic.setImageResource(R.drawable.ic_pause_24dp);
                    audioRecordView.setVisibility(GONE);
                    imageViewAudio.setVisibility(View.GONE);
                    imageViewSend.setVisibility(View.VISIBLE);
                    ivDelete.setVisibility(View.VISIBLE);
                    voiceSeekbar.setVisibility(View.VISIBLE);
                    voiceMessage = true;
                    if (audioFileNameWithPath != null)
                        startPlayingAudio(audioFileNameWithPath);
                    else
                        Toast.makeText(getContext(), "No File Found. Please", Toast.LENGTH_LONG).show();
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ((Activity) context).requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            30);
                }
            }
        }
    }

    public void openActionContainer() {
     /*   ivArrow.setRotation(45f);
        isOpen = true;
        Animation rightAnimate = AnimationUtils.loadAnimation(getContext(), R.anim.animate_right_slide);
        rlActionContainer.startAnimation(rightAnimate);
        rlActionContainer.setVisibility(View.VISIBLE);
  */
    }

    public void closeActionContainer() {
       /* ivArrow.setRotation(0);
        isOpen = false;
        Animation leftAnim = AnimationUtils.loadAnimation(getContext(), R.anim.animate_left_slide);
        rlActionContainer.startAnimation(leftAnim);
        rlActionContainer.setVisibility(GONE);
        */

    }

    public void startRecord() {
        etComposeBox.setVisibility(GONE);
        recordTime.setBase(SystemClock.elapsedRealtime());
        recordTime.start();
        //ivArrow.setVisibility(GONE);
        voiceSeekbar.setVisibility(GONE);
        voiceMessageLayout.setVisibility(View.VISIBLE);
        audioRecordView.recreate();
        audioRecordView.setVisibility(View.VISIBLE);
        startRecording();
    }

    private void startPlayingAudio(String path) {
        try {

            if (timerRunnable != null) {
                seekHandler.removeCallbacks(timerRunnable);
                timerRunnable = null;
            }

            mediaPlayer.reset();
            if (Utils.hasPermissions(context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ((Activity) context).requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            50);
                }
            }

            final int duration = mediaPlayer.getDuration();
            voiceSeekbar.setMax(duration);
            recordTime.setBase(SystemClock.elapsedRealtime());
            recordTime.start();
            timerRunnable = new Runnable() {
                @Override
                public void run() {
                    int pos = mediaPlayer.getCurrentPosition();
                    voiceSeekbar.setProgress(pos);

                    if (mediaPlayer.isPlaying() && pos < duration) {
//                        audioLength.setText(Utils.convertTimeStampToDurationTime(player.getCurrentPosition()));
                        seekHandler.postDelayed(this, 100);
                    } else {
                        seekHandler
                                .removeCallbacks(timerRunnable);
                        timerRunnable = null;
                    }
                }

            };
            seekHandler.postDelayed(timerRunnable, 100);
            mediaPlayer.setOnCompletionListener(mp -> {
                seekHandler
                        .removeCallbacks(timerRunnable);
                timerRunnable = null;
                mp.stop();
                recordTime.stop();
//                audioLength.setText(Utils.convertTimeStampToDurationTime(duration));
                voiceSeekbar.setProgress(0);
//                playButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
            });

        } catch (Exception e) {
            Log.e("playAudioError: ", e.getMessage());
            stopPlayingAudio();
            ;
        }
    }


    private void stopPlayingAudio() {
        if (mediaPlayer != null)
            mediaPlayer.stop();
    }

    private void startRecording() {
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            audioFileNameWithPath = Utils.getOutputMediaFile(getContext());
            mediaRecorder.setOutputFile(audioFileNameWithPath);
            try {
                mediaRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    int currentMaxAmp = 0;
                    try {
                        currentMaxAmp = mediaRecorder != null ? mediaRecorder.getMaxAmplitude() : 0;
                        audioRecordView.update(currentMaxAmp);
                        if (mediaRecorder == null)
                            timer = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 0, 100);
            mediaRecorder.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopRecording(boolean isCancel) {
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

    public void reply(boolean reply, BaseMessage baseMessage) {
        if (baseMessage.getDeletedAt() == 0) {
            this.reply = reply;
            this.baseMessage = baseMessage;
            resend_clear_img.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ComposeBox.this.baseMessage = null;
                    ComposeBox.this.reply = false;
                    rl_resend_root_img.setVisibility(GONE);
                }
            });
            richLinkView.setVisibility(GONE);
            resend_type.setVisibility(VISIBLE);
            if (reply) {
                rl_resend_root_img.setVisibility(VISIBLE);
                if (baseMessage instanceof TextMessage) {
                    resend_image.setVisibility(GONE);
                    TextMessage textMessage = (TextMessage) baseMessage;
                    resend_type.setText(textMessage.getText());
                }
                if (baseMessage instanceof LinkMessage) {
                    resend_image.setVisibility(GONE);
                    LinkMessage textMessage = (LinkMessage) baseMessage;
                    resend_type.setText(textMessage.getText());
                }
                else if (baseMessage instanceof ReplyMessageG) {
                    resend_image.setVisibility(GONE);
                    ReplyMessageG textMessage = (ReplyMessageG) baseMessage;
                    resend_type.setText(textMessage.getText());
                } else if (baseMessage instanceof MediaMessage) {
                    MediaMessage mediaMessage = (MediaMessage) baseMessage;
                    resend_type.setText(mediaMessage.getType());
                    if (mediaMessage.getType().equals(FastChatConstants.MESSAGE_TYPE_IMAGE)) {
                        resend_image.setVisibility(VISIBLE);
                        RequestOptions options = new RequestOptions()
                                .centerCrop()
                                //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                                //   .skipMemoryCache(true)
                                .priority(Priority.HIGH);
                        Glide.with(context)
                                .load(mediaMessage.getAttachment().getFileUrl())
                                //  .placeholder(R.drawable.ic_defaulf_image)
                                //  .error(R.drawable.ic_defaulf_image)
                                .apply(options)
                                .into(resend_image);
                    } else if (mediaMessage.getType().equals(FastChatConstants.MESSAGE_TYPE_AUDIO)) {
                        resend_type.setText(mediaMessage.getType());

                    } else if (mediaMessage.getType().equals(FastChatConstants.MESSAGE_TYPE_VIDEO)) {
                        resend_type.setText(mediaMessage.getType());

                    }

                }
            } else {
                rl_resend_root_img.setVisibility(GONE);
                ComposeBox.this.baseMessage = null;
                ComposeBox.this.reply = false;
            }
        }


    }
}
