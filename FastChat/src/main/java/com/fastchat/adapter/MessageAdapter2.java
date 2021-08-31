package com.fastchat.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.util.Linkify;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.adprogressbarlib.AdCircleProgress;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dueeeke.videocontroller.StandardVideoController;
import com.fastchat.Core.FastChat;
import com.fastchat.Model.Action;
import com.fastchat.Model.Attachment;
import com.fastchat.Model.BaseMessage;
import com.fastchat.Model.Call;
import com.fastchat.Model.FastChatConstants;
import com.fastchat.Model.LinkMessage;
import com.fastchat.Model.MediaMessage;
import com.fastchat.Model.MessageReceipt;
import com.fastchat.Model.Reaction;
import com.fastchat.Model.ReplyMessage;
import com.fastchat.Model.ReplyMessageG;
import com.fastchat.Model.TextMessage;
import com.fastchat.R;
import com.fastchat.Ui.Reaction.ReactionBottomSheetDialog;
import com.fastchat.chat_interactore.Chat;
import com.fastchat.utils.FileHelper;
import com.fastchat.utils.MediaUtils;
import com.fastchat.utils.PatternUtils;
import com.fastchat.utils.StickyHeaderAdapter;
import com.fastchat.utils.Utils;
import com.fastchat.utils.ZoomIv;
import com.fastchat.widget.AudioVisualizer.audio.AudioView;
import com.fastchat.widget.AudioVisualizer.audio.AudioWaveSeekBar;
import com.fastchat.widget.Avatar;
import com.fastchat.widget.exo.ExoMediaSourceHelper;
import com.fastchat.widget.exo.ExoVideoView;
import com.fastchat.widget.richlinkpreview.RichLinkView;
import com.fastchat.widget.richlinkpreview.ViewListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kotlin.jvm.functions.Function1;


public class MessageAdapter2 extends FirebaseRecyclerAdapter<BaseMessage, RecyclerView.ViewHolder> implements StickyHeaderAdapter<MessageAdapter2.DateItemHolder> {

    private static final int RIGHT_IMAGE_MESSAGE = 56;

    private static final int LEFT_IMAGE_MESSAGE = 89;

    private static final int RIGHT_VIDEO_MESSAGE = 78;

    private static final int LEFT_VIDEO_MESSAGE = 87;

    private static final int RIGHT_AUDIO_MESSAGE = 39;

    private static final int RIGHT_Reply_MESSAGE = 52;

    private static final int LEFT_Reply_MESSAGE = 55;

    private static final int LEFT_AUDIO_MESSAGE = 93;

    private static final int CALL_MESSAGE = 234;

    private static final int LEFT_TEXT_MESSAGE = 1;

    private static final int RIGHT_TEXT_MESSAGE = 2;

    private static final int RIGHT_REPLY_TEXT_MESSAGE = 987;

    private static final int LEFT_REPLY_TEXT_MESSAGE = 789;

    private static final int RIGHT_FILE_MESSAGE = 23;

    private static final int LEFT_FILE_MESSAGE = 25;


    private static final int ACTION_MESSAGE = 99;

    private static final int RIGHT_LINK_MESSAGE = 12;

    private static final int LEFT_LINK_MESSAGE = 13;

    private static final int LEFT_DELETE_MESSAGE = 551;

    private static final int RIGHT_DELETE_MESSAGE = 552;

    private static final int RIGHT_CUSTOM_MESSAGE = 432;

    private static final int LEFT_CUSTOM_MESSAGE = 431;

    public Context context;


    private boolean isLongClickEnabled;

    private List<Integer> selectedItemList = new ArrayList<>();

    public List<BaseMessage> longselectedItemList = new ArrayList<>();


    private MediaPlayer mediaPlayer;

    private int messagePosition = 0;

    private OnMessageLongClick messageLongClick;

    private boolean isUserDetailVisible;

    private String TAG = "MessageAdapter";

    private boolean isSent;

    private boolean isTextMessageClick;

    private boolean isImageMessageClick;
    String userId;
    OnSelectedMessageClick onSelectedMessageClick;
    ReactionBottomSheetDialog reactionBottomSheetDialog;

    public MessageAdapter2(Context context, String userId, FirebaseRecyclerOptions<BaseMessage> options, OnSelectedMessageClick onSelectedMessageClick) {
        super(options);
        this.context = context;
        this.userId = userId;
        this.onSelectedMessageClick = onSelectedMessageClick;
        if (null == mediaPlayer) {
            mediaPlayer = new MediaPlayer();
        }

    }


    @Override
    public int getItemViewType(int position) {
        return getItemViewTypes(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view;
        switch (i) {
            case LEFT_DELETE_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.left_message_item, parent, false);
                view.setTag(LEFT_DELETE_MESSAGE);
                return new DeleteMessageViewHolder(view);
            case RIGHT_DELETE_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.right_message_item, parent, false);
                view.setTag(RIGHT_DELETE_MESSAGE);
                return new DeleteMessageViewHolder(view);
            case LEFT_TEXT_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.left_message_item, parent, false);
                view.setTag(LEFT_TEXT_MESSAGE);
                return new TextMessageViewHolder(view);

            case RIGHT_TEXT_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.right_message_item, parent, false);
                view.setTag(RIGHT_TEXT_MESSAGE);
                return new TextMessageViewHolder(view);

            case LEFT_REPLY_TEXT_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.left_message_item, parent, false);
                view.setTag(LEFT_REPLY_TEXT_MESSAGE);
                return new TextMessageViewHolder(view);

            case RIGHT_REPLY_TEXT_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.right_message_item, parent, false);
                view.setTag(RIGHT_REPLY_TEXT_MESSAGE);
                return new TextMessageViewHolder(view);
            case LEFT_Reply_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.left_reply_message_item, parent, false);
                view.setTag(LEFT_Reply_MESSAGE);
                return new ReplyMessageViewHolder(view);
            case RIGHT_Reply_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.right_reply_message_item, parent, false);
                view.setTag(RIGHT_Reply_MESSAGE);
                return new ReplyMessageViewHolder(view);
            case RIGHT_LINK_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.right_message_link_item, parent, false);
                view.setTag(RIGHT_LINK_MESSAGE);
                return new LinkMessageViewHolder(view);

            case LEFT_LINK_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.left_message_link_item, parent, false);
                view.setTag(LEFT_LINK_MESSAGE);
                return new LinkMessageViewHolder(view);

            case RIGHT_AUDIO_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_audio_layout_right, parent, false);
                view.setTag(RIGHT_AUDIO_MESSAGE);
                return new AudioMessageViewHolder(view);

            case LEFT_AUDIO_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_audio_layout_left, parent, false);
                view.setTag(LEFT_AUDIO_MESSAGE);
                return new AudioMessageViewHolder(view);

            case LEFT_IMAGE_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_left_list_image_item, parent, false);
                view.setTag(LEFT_IMAGE_MESSAGE);
                return new ImageMessageViewHolder(view);
            case RIGHT_IMAGE_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_right_list_image_item, parent, false);
                view.setTag(RIGHT_IMAGE_MESSAGE);
                return new ImageMessageViewHolder(view);
            case LEFT_VIDEO_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_left_list_video_item, parent, false);
                view.setTag(LEFT_VIDEO_MESSAGE);
                return new VideoMessageViewHolder(view);

            case RIGHT_VIDEO_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_right_list_video_item, parent, false);
                view.setTag(RIGHT_VIDEO_MESSAGE);
                return new VideoMessageViewHolder(view);
            case RIGHT_FILE_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_right_file_message, parent, false);
                view.setTag(RIGHT_FILE_MESSAGE);
                return new FileMessageViewHolder(view);

            case LEFT_FILE_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_left_file_message, parent, false);
                view.setTag(LEFT_FILE_MESSAGE);
                return new FileMessageViewHolder(view);
            case ACTION_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_action_message, parent, false);
                view.setTag(ACTION_MESSAGE);
                return new ActionMessageViewHolder(view);

            case CALL_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_action_message, parent, false);
                view.setTag(CALL_MESSAGE);
                return new ActionMessageViewHolder(view);

            case RIGHT_CUSTOM_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.right_message_item, parent, false);
                view.setTag(RIGHT_TEXT_MESSAGE);
                return new CustomMessageViewHolder(view);

            case LEFT_CUSTOM_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.left_message_item, parent, false);
                view.setTag(RIGHT_TEXT_MESSAGE);
                return new CustomMessageViewHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.right_message_item, parent, false);
                view.setTag(-1);
                return new TextMessageViewHolder(view);
        }
    }


    @Override
    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i, @NonNull BaseMessage baseMessage) {

        String key = getRef(i).getKey();


        switch (viewHolder.getItemViewType()) {
            case LEFT_DELETE_MESSAGE:
                ((DeleteMessageViewHolder) viewHolder).ivUser.setVisibility(View.GONE);
            case RIGHT_DELETE_MESSAGE:
                setDeleteData((DeleteMessageViewHolder) viewHolder, i, baseMessage);
                break;
            case LEFT_TEXT_MESSAGE:
            case LEFT_REPLY_TEXT_MESSAGE:
                ((TextMessageViewHolder) viewHolder).ivUser.setVisibility(View.GONE);
            case RIGHT_TEXT_MESSAGE:
            case RIGHT_REPLY_TEXT_MESSAGE:
                setTextData((TextMessageViewHolder) viewHolder, i, baseMessage);
                break;

            case LEFT_Reply_MESSAGE:
            case RIGHT_Reply_MESSAGE:
                setReplyData((ReplyMessageViewHolder) viewHolder, i, baseMessage);
                break;
            case LEFT_LINK_MESSAGE:
            case RIGHT_LINK_MESSAGE:
                setLinkData((LinkMessageViewHolder) viewHolder, i, baseMessage);
                break;
            case LEFT_IMAGE_MESSAGE:
            case RIGHT_IMAGE_MESSAGE:
                setImageData((ImageMessageViewHolder) viewHolder, i, baseMessage);
                break;
            case LEFT_AUDIO_MESSAGE:
            case RIGHT_AUDIO_MESSAGE:
                setAudioData((AudioMessageViewHolder) viewHolder, i, baseMessage);
                break;
            case LEFT_VIDEO_MESSAGE:
            case RIGHT_VIDEO_MESSAGE:
                setVideoData((VideoMessageViewHolder) viewHolder, i, baseMessage);
                break;
            case LEFT_FILE_MESSAGE:
                ((FileMessageViewHolder) viewHolder).ivUser.setVisibility(View.GONE);
            case RIGHT_FILE_MESSAGE:
                setFileData((FileMessageViewHolder) viewHolder, i, baseMessage);
                break;
            case ACTION_MESSAGE:
            case CALL_MESSAGE:
                setActionData((ActionMessageViewHolder) viewHolder, i, baseMessage);
                break;
            case LEFT_CUSTOM_MESSAGE:
                ((CustomMessageViewHolder) viewHolder).ivUser.setVisibility(View.GONE);
            case RIGHT_CUSTOM_MESSAGE:
                setCustomData((CustomMessageViewHolder) viewHolder, i, baseMessage);
                break;


        }
    }

    private void setAudioData(AudioMessageViewHolder viewHolder, int i, BaseMessage baseMessage1) {
        if (baseMessage1 != null && baseMessage1.getDeletedAt() == 0) {
            MediaMessage baseMessage = (MediaMessage) baseMessage1;
            if (!baseMessage.getUserId().equals(userId)) {
                if (baseMessage.getReceiverType().equals(FastChatConstants.RECEIVER_TYPE_USER)) {
                    viewHolder.tvUser.setVisibility(View.GONE);
                    viewHolder.ivUser.setVisibility(View.GONE);
                } else if (baseMessage.getReceiverType().equals(FastChatConstants.RECEIVER_TYPE_GROUP)) {
                    if (isUserDetailVisible) {
                        viewHolder.tvUser.setVisibility(View.VISIBLE);
                        viewHolder.ivUser.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.tvUser.setVisibility(View.GONE);
                        viewHolder.ivUser.setVisibility(View.INVISIBLE);
                    }
                    // setAvatar(viewHolder.ivUser, baseMessage.getSender().getAvatar(), baseMessage.getSender().getName());
                    // viewHolder.tvUser.setText(baseMessage.getSender().getName());
                }
            }
            if (baseMessage.getReplyCount() != 0) {
                viewHolder.tvThreadReplyCount.setVisibility(View.VISIBLE);
                viewHolder.tvThreadReplyCount.setText(baseMessage.getReplyCount() + " Replies");
            } else {
                viewHolder.lvReplyAvatar.setVisibility(View.GONE);
                viewHolder.tvThreadReplyCount.setVisibility(View.GONE);
            }
            viewHolder.tvThreadReplyCount.setOnClickListener(view -> {

            });
            String key = getRef(i).getKey();
            viewHolder.setupVisualizerFxAndUI((MediaMessage) baseMessage);
            FileHelper.makeFileAndShow(viewHolder.itemView.getContext(), (MediaMessage) baseMessage, userId, null, getRef(i).getKey(), viewHolder.img_progress_bar);
            viewHolder.txtProcess();
            // viewHolder.length.setText(Utils.getFileSize(((MediaMessage)baseMessage).getAttachment().getFileSize()));
            // viewHolder.playBtn.setImageResource(R.drawable.ic_play_arrow_black_24dp);


            showMessageTime(viewHolder, baseMessage);

            if (selectedItemList.contains(baseMessage.getId()))
                viewHolder.txtTime.setVisibility(View.VISIBLE);
            else
                viewHolder.txtTime.setVisibility(View.GONE);


            viewHolder.rlMessageBubble.setOnClickListener(view -> {
                setSelectedMessage(baseMessage.getId());
                notifyDataSetChanged();

            });


            ReactionAdapter reactionAdapter = new ReactionAdapter(new ReactionLocalAdapter.Action() {
                @Override
                public void getReaction(Reaction reaction) {
                    FastChat.getFastChat().getChatInteract().deleteReaction(baseMessage.getGroupId(), userId, baseMessage.getReceiverId(), key, "feeling", reaction.getKeyEmoji(), reaction.getUrlEmoji());

                }
            });
            viewHolder.RecyclerView_Reaction.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
            viewHolder.RecyclerView_Reaction.setAdapter(reactionAdapter);
            new Chat().getMessages(baseMessage.getGroupId(), baseMessage.getReceiverId()).child(key).child("reactions").child("reaction").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        String value = snapshot1.child("feeling").getValue(String.class);
                        Reaction reaction = new Reaction(value, "");
                        reactionAdapter.addOnce(reaction);
                        viewHolder.RelativeLayout_Reaction.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            viewHolder.rlMessageBubble.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    setReaction(new ReactionBottomSheetDialog.Action() {
                        @Override
                        public void getReaction(Reaction reaction) {
                            FastChat.getFastChat().getChatInteract().addReaction(baseMessage.getGroupId(), userId, baseMessage.getReceiverId(), key, "feeling", reaction.getKeyEmoji(), reaction.getUrlEmoji());
                            viewHolder.RelativeLayout_Reaction.setVisibility(View.VISIBLE);
                            //   reactionAdapter.addOnce(reaction);
                            reactionBottomSheetDialog.dismiss();
                        }

                        @Override
                        public void deleteMessage(Button button) {
                            if (baseMessage.getUserId().equals(userId)) {
                                button.setVisibility(View.VISIBLE);
                                FastChat.getFastChat().getChatInteract().UpdateStatusMessage(baseMessage.getGroupId(), "", "", key, "deletedAt");
                            } else {
                                button.setVisibility(View.GONE);

                            }
                            reactionBottomSheetDialog.dismiss();
                        }


                    });
                    return true;
                }
            });
        }
    }

    public void stopPlayingAudio() {
        if (mediaPlayer != null)
            mediaPlayer.stop();
    }


    private void setFileData(FileMessageViewHolder viewHolder, int i, BaseMessage v) {
        if (v instanceof MediaMessage) {
            MediaMessage baseMessage = (MediaMessage) v;
            if (baseMessage != null && baseMessage.getDeletedAt() == 0) {
                if (!baseMessage.getUserId().equals(userId)) {
                    if (baseMessage.getReceiverType().equals(FastChatConstants.RECEIVER_TYPE_USER)) {
                        // viewHolder.tvUser.setVisibility(View.GONE);
                        // viewHolder.ivUser.setVisibility(View.GONE);
                    } else if (baseMessage.getReceiverType().equals(FastChatConstants.RECEIVER_TYPE_GROUP)) {
                        if (isUserDetailVisible) {
                            viewHolder.tvUser.setVisibility(View.VISIBLE);
                            viewHolder.ivUser.setVisibility(View.VISIBLE);
                        } else {
                            viewHolder.tvUser.setVisibility(View.GONE);
                            viewHolder.ivUser.setVisibility(View.INVISIBLE);
                        }
                        //   setAvatar(viewHolder.ivUser, baseMessage.getSender().getAvatar(), baseMessage.getSender().getName());
                        //  viewHolder.tvUser.setText(baseMessage.getSender().getName());
                    }
                }
                viewHolder.fileName.setText(baseMessage.getAttachment().getFileName());
                viewHolder.fileExt.setText(baseMessage.getAttachment().getFileExtension());
                int fileSize = baseMessage.getAttachment().getFileSize();
                viewHolder.fileSize.setText(Utils.getFileSize(fileSize));
                viewHolder.lvReplyAvatar.setVisibility(View.GONE);
                if (baseMessage.getReplyCount() != 0) {
                    viewHolder.tvThreadReplyCount.setVisibility(View.VISIBLE);
                    viewHolder.tvThreadReplyCount.setText(baseMessage.getReplyCount() + " Replies");
                } else {
                    viewHolder.lvReplyAvatar.setVisibility(View.GONE);
                    viewHolder.tvThreadReplyCount.setVisibility(View.GONE);
                }
                viewHolder.tvThreadReplyCount.setOnClickListener(view -> {

                });


                showMessageTime(viewHolder, baseMessage);

                if (selectedItemList.contains(baseMessage.getId()))
                    viewHolder.txtTime.setVisibility(View.VISIBLE);
                else
                    viewHolder.txtTime.setVisibility(View.GONE);


                viewHolder.rlMessageBubble.setOnClickListener(view -> {
                    setSelectedMessage(baseMessage.getId());
                    notifyDataSetChanged();

                });
                FileHelper.makeFileAndShow(viewHolder.itemView.getContext(), (MediaMessage) baseMessage, userId, viewHolder.ivIcon, getRef(i).getKey(), viewHolder.img_progress_bar);
                viewHolder.fileName.setOnClickListener(view -> MediaUtils.openFile(baseMessage.getAttachment().getFileUrl(), baseMessage.getAttachment().getFileMimeType(), context));
                viewHolder.rlMessageBubble.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                       /* if (!isLongClickEnabled && !isTextMessageClick) {
                            isImageMessageClick = true;
                            setLongClickSelectedItem(baseMessage);
                            messageLongClick.setLongMessageClick(longselectedItemList);
                            notifyDataSetChanged();
                        }*/
                        return true;
                    }
                });
            }
        }


    }


    private void setImageData(ImageMessageViewHolder viewHolder, int i, BaseMessage v) {
        if (v instanceof MediaMessage) {
            MediaMessage baseMessage = (MediaMessage) v;
            if (!baseMessage.getUserId().equals(userId)) {

                if (baseMessage.getReceiverType().equals(FastChatConstants.RECEIVER_TYPE_USER)) {
                    //  viewHolder.tvUser.setVisibility(View.GONE);
                    //  viewHolder.ivUser.setVisibility(View.GONE);
                } else if (baseMessage.getReceiverType().equals(FastChatConstants.RECEIVER_TYPE_GROUP)) {
                    if (isUserDetailVisible) {
                        viewHolder.tvUser.setVisibility(View.VISIBLE);
                        viewHolder.ivUser.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.tvUser.setVisibility(View.GONE);
                        viewHolder.ivUser.setVisibility(View.INVISIBLE);
                    }
                    // setAvatar(viewHolder.ivUser, baseMessage.getSender().getAvatar(), baseMessage.getSender().getName());
                    //  viewHolder.tvUser.setText(baseMessage.getSender().getName());
                }
            }
            String key = getRef(i).getKey();
            FileHelper.makeFileAndShow(viewHolder.itemView.getContext(), baseMessage, userId, viewHolder.imageView, getRef(i).getKey(), viewHolder.img_progress_bar);
            viewHolder.sensitiveLayout.setVisibility(View.GONE);
            if (baseMessage.getReplyCount() != 0) {
                viewHolder.tvThreadReplyCount.setVisibility(View.VISIBLE);
                viewHolder.tvThreadReplyCount.setText(baseMessage.getReplyCount() + " Replies");
            } else {
                viewHolder.lvReplyAvatar.setVisibility(View.GONE);
                viewHolder.tvThreadReplyCount.setVisibility(View.GONE);
            }
            viewHolder.tvThreadReplyCount.setOnClickListener(view -> {

            });

            showMessageTime(viewHolder, baseMessage);
            if (selectedItemList.contains(baseMessage.getId()))
                viewHolder.txtTime.setVisibility(View.VISIBLE);
            else
                viewHolder.txtTime.setVisibility(View.GONE);


            viewHolder.rlMessageBubble.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    setReaction(new ReactionBottomSheetDialog.Action() {
                        @Override
                        public void getReaction(Reaction reaction) {
                            FastChat.getFastChat().getChatInteract().addReaction(baseMessage.getGroupId(), userId, baseMessage.getReceiverId(), key, "feeling", reaction.getKeyEmoji(), reaction.getUrlEmoji());
                            viewHolder.RelativeLayout_Reaction.setVisibility(View.VISIBLE);
                            //   reactionAdapter.addOnce(reaction);
                            reactionBottomSheetDialog.dismiss();
                        }

                        @Override
                        public void deleteMessage(Button button) {
                            if (baseMessage.getUserId().equals(userId)) {
                                button.setVisibility(View.VISIBLE);
                                FastChat.getFastChat().getChatInteract().UpdateStatusMessage(baseMessage.getGroupId(), "", "", key, "deletedAt");
                            } else {
                                button.setVisibility(View.GONE);

                            }
                            reactionBottomSheetDialog.dismiss();

                        }
                    });
                    return false;
                }
            });
            viewHolder.rlMessageBubble.setOnClickListener(view -> {
                displayImage(baseMessage);
                setSelectedMessage(baseMessage.getId());
                notifyDataSetChanged();

            });

        }

    }

    private void displayImage(BaseMessage baseMessage) {
        Dialog imageDialog = new Dialog(context);
        View messageVw = LayoutInflater.from(context).inflate(R.layout.image_dialog_view, null);
        ZoomIv imageView = messageVw.findViewById(R.id.imageView);
        Glide.with(context).asBitmap().load(((MediaMessage) baseMessage).getAttachment().getFileUrl()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                imageView.setImageBitmap(resource);
            }
        });
        imageDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageDialog.setContentView(messageVw);
        imageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        imageDialog.show();
    }


    private void setVideoData(VideoMessageViewHolder viewHolder, int i, BaseMessage baseMessage1) {
        MediaMessage baseMessage = (MediaMessage) baseMessage1;
        String key = getRef(i).getKey();
        if (!baseMessage.getUserId().equals(userId)) {
            if (baseMessage.getReceiverType().equals(FastChatConstants.RECEIVER_TYPE_USER)) {
                //  viewHolder.tvUser.setVisibility(View.GONE);
                //  viewHolder.ivUser.setVisibility(View.GONE);
            } else if (baseMessage.getReceiverType().equals(FastChatConstants.RECEIVER_TYPE_GROUP)) {
                if (isUserDetailVisible) {
                    viewHolder.tvUser.setVisibility(View.VISIBLE);
                    viewHolder.ivUser.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.tvUser.setVisibility(View.GONE);
                    viewHolder.ivUser.setVisibility(View.INVISIBLE);
                }

            }
        }
        if (((MediaMessage) baseMessage).getAttachment() != null)
            viewHolder.mVideoView.setCacheEnabled(true);

        FileHelper.makeFileAndShow(viewHolder.itemView.getContext(), (MediaMessage) baseMessage, userId, viewHolder.mVideoView, getRef(i).getKey(), viewHolder.img_progress_bar);
        if (baseMessage.getReplyCount() != 0) {
            viewHolder.tvThreadReplyCount.setVisibility(View.VISIBLE);
            viewHolder.tvThreadReplyCount.setText(baseMessage.getReplyCount() + " Replies");
        } else {
            viewHolder.lvReplyAvatar.setVisibility(View.GONE);
            viewHolder.tvThreadReplyCount.setVisibility(View.GONE);
        }
        viewHolder.tvThreadReplyCount.setOnClickListener(view -> {

        });


        ReactionAdapter reactionAdapter = new ReactionAdapter(new ReactionLocalAdapter.Action() {
            @Override
            public void getReaction(Reaction reaction) {
                FastChat.getFastChat().getChatInteract().deleteReaction(baseMessage.getGroupId(), userId, baseMessage.getReceiverId(), key, "feeling", reaction.getKeyEmoji(), reaction.getUrlEmoji());

            }
        });
        viewHolder.RecyclerView_Reaction.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        viewHolder.RecyclerView_Reaction.setAdapter(reactionAdapter);
        new Chat().getMessages(baseMessage.getGroupId(), baseMessage.getReceiverId()).child(key).child("reactions").child("reaction").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String value = snapshot1.child("feeling").getValue(String.class);
                    Reaction reaction = new Reaction(value, "");
                    reactionAdapter.addOnce(reaction);
                    viewHolder.RelativeLayout_Reaction.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        viewHolder.mVideoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setReaction(new ReactionBottomSheetDialog.Action() {
                    @Override
                    public void getReaction(Reaction reaction) {
                        FastChat.getFastChat().getChatInteract().addReaction(baseMessage.getGroupId(), userId, baseMessage.getReceiverId(), key, "feeling", reaction.getKeyEmoji(), reaction.getUrlEmoji());
                        viewHolder.RelativeLayout_Reaction.setVisibility(View.VISIBLE);
                        //   reactionAdapter.addOnce(reaction);
                        reactionBottomSheetDialog.dismiss();
                    }

                    @Override
                    public void deleteMessage(Button button) {
                        if (baseMessage.getUserId().equals(userId)) {
                            button.setVisibility(View.VISIBLE);
                            FastChat.getFastChat().getChatInteract().UpdateStatusMessage(baseMessage.getGroupId(), "", "", key, "deletedAt");
                        } else {
                            button.setVisibility(View.GONE);

                        }
                        reactionBottomSheetDialog.dismiss();
                    }
                });

                // viewHolder.cv_message_container.setOnTouchListener(popup);
                return false;
            }
        });


        showMessageTime(viewHolder, baseMessage);
        if (selectedItemList.contains(baseMessage.getId()))
            viewHolder.txtTime.setVisibility(View.VISIBLE);
        else
            viewHolder.txtTime.setVisibility(View.GONE);


        viewHolder.rlMessageBubble.setOnClickListener(view -> {
            displayImage(baseMessage);
            setSelectedMessage(baseMessage.getId());
            notifyDataSetChanged();

        });

        viewHolder.playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.mVideoView.start();
            }
        });
    }


    private void setDeleteData(DeleteMessageViewHolder viewHolder, int i, BaseMessage baseMessage) {
        if (!baseMessage.getUserId().equals(userId)) {
            if (baseMessage.getReceiverType().equals(FastChatConstants.RECEIVER_TYPE_USER)) {
                viewHolder.tvUser.setVisibility(View.GONE);
                viewHolder.ivUser.setVisibility(View.GONE);
            } else if (baseMessage.getReceiverType().equals(FastChatConstants.RECEIVER_TYPE_GROUP)) {
                if (isUserDetailVisible) {
                    viewHolder.tvUser.setVisibility(View.VISIBLE);
                    viewHolder.ivUser.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.tvUser.setVisibility(View.GONE);
                    viewHolder.ivUser.setVisibility(View.INVISIBLE);
                }
            }
        }
        if (baseMessage.getDeletedAt() != 0) {
            viewHolder.tvThreadReplyCount.setVisibility(View.GONE);
            viewHolder.lvReplyAvatar.setVisibility(View.GONE);
            viewHolder.txtMessage.setText("message deleted");
            viewHolder.txtMessage.setTextColor(context.getResources().getColor(R.color.secondaryTextColor));
            viewHolder.txtMessage.setTypeface(null, Typeface.ITALIC);
        }
        showMessageTime(viewHolder, baseMessage);

        if (selectedItemList.contains(baseMessage.getId()))
            viewHolder.txtTime.setVisibility(View.VISIBLE);
        else
            viewHolder.txtTime.setVisibility(View.GONE);

    }


    private void setActionData(ActionMessageViewHolder viewHolder, int i, BaseMessage baseMessage) {
        if (Utils.isDarkMode(context))
            viewHolder.textView.setTextColor(context.getResources().getColor(R.color.textColorWhite));
        else
            viewHolder.textView.setTextColor(context.getResources().getColor(R.color.primaryTextColor));
        if (baseMessage instanceof Action)
            viewHolder.textView.setText(((Action) baseMessage).getMessage());
        else if (baseMessage instanceof Call) {
            Call call = ((Call) baseMessage);
            boolean isIncoming = false, isVideo = false, isMissed = false;
            String callMessageText = "";
            String callMessageText2 = "";
            if (call.getReceiverType().equals(FastChatConstants.RECEIVER_TYPE_USER)) {
                if (call.getUserId().equals(userId)) {
                    if (call.getCallStatus().equals(FastChatConstants.CALL_STATUS_UNANSWERED)) {
                        callMessageText = context.getResources().getString(R.string.missed_call);
                        isMissed = true;
                    } else if (call.getCallStatus().equals(FastChatConstants.CALL_STATUS_REJECTED)) {
                        callMessageText = context.getResources().getString(R.string.rejected_call);
                    } else
                        callMessageText = context.getResources().getString(R.string.outgoing);
                    isIncoming = false;
                } else {
                    if (call.getCallStatus().equals(FastChatConstants.CALL_STATUS_UNANSWERED)) {
                        callMessageText = context.getResources().getString(R.string.missed_call);
                        isMissed = true;
                    } else if (call.getCallStatus().equals(FastChatConstants.CALL_STATUS_REJECTED)) {
                        callMessageText = context.getResources().getString(R.string.rejected_call);
                    } else
                        callMessageText = context.getResources().getString(R.string.incoming);
                    isIncoming = true;
                }

                if (call.getType().equals(FastChatConstants.CALL_TYPE_VIDEO)) {
                    callMessageText2 = callMessageText + " " + context.getResources().getString(R.string.video_call);
                    isVideo = true;
                } else {
                    callMessageText2 = callMessageText + " " + context.getResources().getString(R.string.audio_call);
                    isVideo = false;
                }
            }

            if (call.getCallStatus().equals(FastChatConstants.CALL_STATUS_ENDED)) {
                viewHolder.textView.setText(callMessageText2 + call.getCallTime() + "" + FastChatConstants.CALL_STATUS_ENDED);
            } else {
                viewHolder.textView.setText(callMessageText2);
            }

        }
    }


    private void showMessageTime(RecyclerView.ViewHolder viewHolder, BaseMessage baseMessage) {

        if (viewHolder instanceof TextMessageViewHolder) {
            setStatusIcon(((TextMessageViewHolder) viewHolder).txtTime, baseMessage);
        } else if (viewHolder instanceof LinkMessageViewHolder) {
            setStatusIcon(((LinkMessageViewHolder) viewHolder).txtTime, baseMessage);
        } else if (viewHolder instanceof ImageMessageViewHolder) {
            setStatusIcon(((ImageMessageViewHolder) viewHolder).txtTime, baseMessage);
        } else if (viewHolder instanceof FileMessageViewHolder) {
            setStatusIcon(((FileMessageViewHolder) viewHolder).txtTime, baseMessage);
        } else if (viewHolder instanceof ReplyMessageViewHolder) {
            setStatusIcon(((ReplyMessageViewHolder) viewHolder).txtTime, baseMessage);
        }

    }


    private void setStatusIcon(TextView txtTime, BaseMessage baseMessage1) {
        if (baseMessage1.getUserId().equals(userId)) {
            if (baseMessage1.getReadAt() != 0) {
                txtTime.setText(Utils.getHeaderDate(baseMessage1.getReadAt()));
                txtTime.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_double_tick, 0);
                txtTime.setCompoundDrawablePadding(10);
            } else if (baseMessage1.getDeliveredAt() != 0) {
                txtTime.setText(Utils.getHeaderDate(baseMessage1.getDeliveredAt()));
                txtTime.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done_all_black_24dp, 0);
                txtTime.setCompoundDrawablePadding(10);
            } else {
                txtTime.setText(Utils.getHeaderDate(baseMessage1.getSentAt()));
                txtTime.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_24, 0);
                txtTime.setCompoundDrawablePadding(10);
            }
        } else {
            txtTime.setText(Utils.getHeaderDate(baseMessage1.getSentAt()));
        }
    }


    private void setTextData(TextMessageViewHolder viewHolder, int i, BaseMessage v) {
        if (v instanceof TextMessage) {
            String key = getRef(i).getKey();
            TextMessage baseMessage = (TextMessage) v;
            if (baseMessage != null) {

                if (baseMessage.getReplyCount() != 0) {
                    viewHolder.tvThreadReplyCount.setVisibility(View.VISIBLE);
                    viewHolder.tvThreadReplyCount.setText(baseMessage.getReplyCount() + " Replies");
                } else {
                    viewHolder.lvReplyAvatar.setVisibility(View.GONE);
                    viewHolder.tvThreadReplyCount.setVisibility(View.GONE);
                }
                viewHolder.tvThreadReplyCount.setOnClickListener(view -> {

                });

                ReactionAdapter reactionAdapter = new ReactionAdapter(new ReactionLocalAdapter.Action() {
                    @Override
                    public void getReaction(Reaction reaction) {
                        FastChat.getFastChat().getChatInteract().deleteReaction(baseMessage.getGroupId(), userId, baseMessage.getReceiverId(), key, "feeling", reaction.getKeyEmoji(), reaction.getUrlEmoji());

                    }
                });
                viewHolder.RecyclerView_Reaction.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                viewHolder.RecyclerView_Reaction.setAdapter(reactionAdapter);
                new Chat().getMessages(baseMessage.getGroupId(), baseMessage.getReceiverId()).child(key).child("reactions").child("reaction").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String value = snapshot1.child("feeling").getValue(String.class);
                            Reaction reaction = new Reaction(value, "");
                            reactionAdapter.addOnce(reaction);
                            viewHolder.RelativeLayout_Reaction.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                viewHolder.txtMessage.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        setReaction(new ReactionBottomSheetDialog.Action() {
                            @Override
                            public void getReaction(Reaction reaction) {
                                FastChat.getFastChat().getChatInteract().addReaction(baseMessage.getGroupId(), userId, baseMessage.getReceiverId(), key, "feeling", reaction.getKeyEmoji(), reaction.getUrlEmoji());
                                viewHolder.RelativeLayout_Reaction.setVisibility(View.VISIBLE);
                                //   reactionAdapter.addOnce(reaction);
                                reactionBottomSheetDialog.dismiss();
                            }

                            @Override
                            public void deleteMessage(Button button) {
                                if (baseMessage.getUserId().equals(userId)) {
                                    button.setVisibility(View.VISIBLE);
                                    FastChat.getFastChat().getChatInteract().UpdateStatusMessage(baseMessage.getGroupId(), "", "", key, "deletedAt");
                                } else {
                                    button.setVisibility(View.GONE);

                                }
                                reactionBottomSheetDialog.dismiss();
                            }

                        });

                        return false;
                    }
                });


                viewHolder.txtMessage.setText(baseMessage.getText());
                if (baseMessage.getUserId().equals(userId)) {
                    PatternUtils.setHyperLinkSupport(context, viewHolder.txtMessage, R.color.textColorWhite);
                    viewHolder.txtMessage.setTextColor(context.getResources().getColor(R.color.textColorWhite));
                } else {
                    PatternUtils.setHyperLinkSupport(context, viewHolder.txtMessage, R.color.textColorWhite);
                    viewHolder.txtMessage.setTextColor(context.getResources().getColor(R.color.primaryTextColor));

                }

                showMessageTime(viewHolder, baseMessage);
                if (getSnapshots().get(getSnapshots().size() - 1).equals(baseMessage)) {
                    selectedItemList.add(baseMessage.getId());
                }
                if (selectedItemList.contains(baseMessage.getId()))
                    viewHolder.txtTime.setVisibility(View.VISIBLE);
                else
                    viewHolder.txtTime.setVisibility(View.GONE);


                viewHolder.cv_message_container.setOnClickListener(view -> {
                    setSelectedMessage(baseMessage.getId());
                    notifyDataSetChanged();

                });

                viewHolder.itemView.setTag(R.string.message, baseMessage);
            }
        }

    }


    private void setLinkData(LinkMessageViewHolder viewHolder, int i, BaseMessage v) {
        if (v instanceof LinkMessage) {
            String key = getRef(i).getKey();
            LinkMessage baseMessage = (LinkMessage) v;
            if (baseMessage != null) {

                if( baseMessage.getReplyCount() != 0) {
                    viewHolder.tvThreadReplyCount.setVisibility(View.VISIBLE);
                    viewHolder.tvThreadReplyCount.setText(baseMessage.getReplyCount() + " Replies");
                } else {
                    viewHolder.lvReplyAvatar.setVisibility(View.GONE);
                    viewHolder.tvThreadReplyCount.setVisibility(View.GONE);
                }
                viewHolder.tvThreadReplyCount.setOnClickListener(view -> {

                });

                ReactionAdapter reactionAdapter = new ReactionAdapter(new ReactionLocalAdapter.Action() {
                    @Override
                    public void getReaction(Reaction reaction) {
                        FastChat.getFastChat().getChatInteract().deleteReaction(baseMessage.getGroupId(), userId, baseMessage.getReceiverId(), key, "feeling", reaction.getKeyEmoji(), reaction.getUrlEmoji());

                    }
                });
                viewHolder.RecyclerView_Reaction.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                viewHolder.RecyclerView_Reaction.setAdapter(reactionAdapter);
                new Chat().getMessages(baseMessage.getGroupId(), baseMessage.getReceiverId()).child(key).child("reactions").child("reaction").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String value = snapshot1.child("feeling").getValue(String.class);
                            Reaction reaction = new Reaction(value, "");
                            reactionAdapter.addOnce(reaction);
                            viewHolder.RelativeLayout_Reaction.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                if (extractLinks(baseMessage.getText()).length>0){
                      viewHolder.richLink.setLink(extractLinks(baseMessage.getText())[0], new ViewListener() {
                    @Override
                    public void onSuccess(boolean status) {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
                }else {
                    viewHolder.richLink.setVisibility(View.GONE);

                }

                viewHolder.cv_message_container.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        setReaction(new ReactionBottomSheetDialog.Action() {
                            @Override
                            public void getReaction(Reaction reaction) {
                                FastChat.getFastChat().getChatInteract().addReaction(baseMessage.getGroupId(), userId, baseMessage.getReceiverId(), key, "feeling", reaction.getKeyEmoji(), reaction.getUrlEmoji());
                                viewHolder.RelativeLayout_Reaction.setVisibility(View.VISIBLE);
                                //   reactionAdapter.addOnce(reaction);
                                reactionBottomSheetDialog.dismiss();
                            }

                            @Override
                            public void deleteMessage(Button button) {
                                if (baseMessage.getUserId().equals(userId)) {
                                    button.setVisibility(View.VISIBLE);
                                    FastChat.getFastChat().getChatInteract().UpdateStatusMessage(baseMessage.getGroupId(), "", "", key, "deletedAt");
                                } else {
                                    button.setVisibility(View.GONE);

                                }
                                reactionBottomSheetDialog.dismiss();
                            }

                        });

                        return false;
                    }
                });


                viewHolder.txtMessage.setText(baseMessage.getText());
                if (baseMessage.getUserId().equals(userId)) {
                    PatternUtils.setHyperLinkSupport(context, viewHolder.txtMessage, R.color.textColorWhite);
                    viewHolder.txtMessage.setTextColor(context.getResources().getColor(R.color.textColorWhite));
                } else {
                    PatternUtils.setHyperLinkSupport(context, viewHolder.txtMessage, R.color.textColorWhite);
                    viewHolder.txtMessage.setTextColor(context.getResources().getColor(R.color.primaryTextColor));

                }

                showMessageTime(viewHolder, baseMessage);
                if (getSnapshots().get(getSnapshots().size() - 1).equals(baseMessage)) {
                    selectedItemList.add(baseMessage.getId());
                }
                if (selectedItemList.contains(baseMessage.getId()))
                    viewHolder.txtTime.setVisibility(View.VISIBLE);
                else
                    viewHolder.txtTime.setVisibility(View.GONE);


                viewHolder.cv_message_container.setOnClickListener(view -> {
                    setSelectedMessage(baseMessage.getId());
                    notifyDataSetChanged();

                });

                viewHolder.itemView.setTag(R.string.message, baseMessage);
            }
        }

    }
    public static String[] extractLinks(String text) {
        List<String> links = new ArrayList<String>();
        Matcher m = Patterns.WEB_URL.matcher(text);
        while (m.find()) {
            String url = m.group();
             links.add(url);
        }

        return links.toArray(new String[links.size()]);
    }
    private void setReplyData(ReplyMessageViewHolder viewHolder, int i, BaseMessage v) {
        if (v instanceof ReplyMessageG) {
            ReplyMessageG baseMessage = (ReplyMessageG) v;
            if (baseMessage != null) {
                String key = getRef(i).getKey();

                if (baseMessage.getReplyCount() != 0) {
                    viewHolder.tvThreadReplyCount.setVisibility(View.VISIBLE);
                    viewHolder.tvThreadReplyCount.setText(baseMessage.getReplyCount() + " Replies");
                } else {
                    viewHolder.lvReplyAvatar.setVisibility(View.GONE);
                    viewHolder.tvThreadReplyCount.setVisibility(View.GONE);
                }
                viewHolder.tvThreadReplyCount.setOnClickListener(view -> {

                });


                ReactionAdapter reactionAdapter = new ReactionAdapter(new ReactionLocalAdapter.Action() {
                    @Override
                    public void getReaction(Reaction reaction) {
                        FastChat.getFastChat().getChatInteract().deleteReaction(baseMessage.getGroupId(), userId, baseMessage.getReceiverId(), key, "feeling", reaction.getKeyEmoji(), reaction.getUrlEmoji());

                    }
                });
                viewHolder.RecyclerView_Reaction.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                viewHolder.RecyclerView_Reaction.setAdapter(reactionAdapter);
                new Chat().getMessages(baseMessage.getGroupId(), baseMessage.getReceiverId()).child(key).child("reactions").child("reaction").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String value = snapshot1.child("feeling").getValue(String.class);
                            Reaction reaction = new Reaction(value, "");
                            reactionAdapter.addOnce(reaction);
                            viewHolder.RelativeLayout_Reaction.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                viewHolder.cv_message_container.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        setReaction(new ReactionBottomSheetDialog.Action() {
                            @Override
                            public void getReaction(Reaction reaction) {
                                FastChat.getFastChat().getChatInteract().addReaction(baseMessage.getGroupId(), userId, baseMessage.getReceiverId(), key, "feeling", reaction.getKeyEmoji(), reaction.getUrlEmoji());
                                viewHolder.RelativeLayout_Reaction.setVisibility(View.VISIBLE);
                                //   reactionAdapter.addOnce(reaction);
                                reactionBottomSheetDialog.dismiss();
                            }

                            @Override
                            public void deleteMessage(Button button) {
                                if (baseMessage.getUserId().equals(userId)) {
                                    button.setVisibility(View.VISIBLE);
                                    FastChat.getFastChat().getChatInteract().UpdateStatusMessage(baseMessage.getGroupId(), "", "", key, "deletedAt");
                                } else {
                                    button.setVisibility(View.GONE);

                                }
                                reactionBottomSheetDialog.dismiss();
                            }


                        });

                        // viewHolder.cv_message_container.setOnTouchListener(popup);
                        return false;
                    }
                });


                viewHolder.cv_message_container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onSelectedMessageClick != null) {
                            onSelectedMessageClick.setMessageClick(baseMessage.getPosition());
                        }

                    }
                });
                viewHolder.txtMessage.setTextSize(16f);
           /* int count = 0;
            CharSequence processed = EmojiCompat.get().process(txtMessage, 0,
                        txtMessage.length() -1, Integer.MAX_VALUE, EmojiCompat.REPLACE_STRATEGY_ALL);
            if (processed instanceof Spannable) {
                Spannable spannable = (Spannable) processed;
                count = spannable.getSpans(0, spannable.length() - 1, EmojiSpan.class).length;
                if (Utils.removeEmojiAndSymbol(txtMessage).trim().length() == 0) {
                    if (count == 1) {
                        viewHolder.txtMessage.setTextSize((int) Utils.dpToPx(context, 32));
                    } else if (count == 2) {
                        viewHolder.txtMessage.setTextSize((int) Utils.dpToPx(context, 24));
                    }
                }
            }
*/
                //  String textMessage=  new TextMessage().getText();
                // TextMessage textMessage=   baseMessage.getReplyMessage();
                //  viewHolder.txtMessage.setText(textMessage.getText());
                if (baseMessage.getText() != null) {
                    viewHolder.reply_txt_message.setText(baseMessage.getText());
                }
                Map<String, Object> value = (Map<String, Object>) baseMessage.getReplyMessage();
                String type = (String) value.get("type");
                if (type != null) {
                    if (type.equals("text")) {
                        viewHolder.txtMessage.setText((String) value.get("text"));
                    } else if (type.equals(FastChatConstants.MESSAGE_TYPE_Reply)) {
                        viewHolder.txtMessage.setText((String) value.get("text"));
                    }
                    else if (type.equals(FastChatConstants.MESSAGE_TYPE_Link)) {
                        viewHolder.txtMessage.setText((String) value.get("text"));
                    }
                    else if (type.equals(FastChatConstants.MESSAGE_TYPE_IMAGE)) {
                        viewHolder.r_image.setVisibility(View.VISIBLE);
                        viewHolder.resend_image.setVisibility(View.VISIBLE);
                        viewHolder.audioWaveSeekBar.setVisibility(View.GONE);
                        Map<String, Object> map = (Map<String, Object>) value.get("attachment");
                        String fileUrl = (String) map.get("fileUrl");
                        RequestOptions options = new RequestOptions()
                                .centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .priority(Priority.HIGH);
                        Glide.with(context)
                                .load(fileUrl)
                                // .placeholder(R.drawable.ic_defaulf_image)
                                //  .error(R.drawable.ic_defaulf_image)
                                .apply(options)
                                .into(viewHolder.resend_image);
                        viewHolder.txtMessage.setText(type);

                    } else if (type.equals(FastChatConstants.MESSAGE_TYPE_VIDEO)) {
                        viewHolder.r_image.setVisibility(View.VISIBLE);
                        viewHolder.resend_image.setVisibility(View.VISIBLE);
                        viewHolder.audioWaveSeekBar.setVisibility(View.GONE);
                        Map<String, Object> map = (Map<String, Object>) value.get("attachment");
                        String fileUrl = (String) map.get("fileUrl");
                        RequestOptions options = new RequestOptions()
                                .centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .priority(Priority.HIGH);
                        Glide.with(context)
                                .load(fileUrl)
                                // .placeholder(R.drawable.ic_defaulf_image)
                                //  .error(R.drawable.ic_defaulf_image)
                                .apply(options)
                                .into(viewHolder.resend_image);
                        viewHolder.txtMessage.setText(type);

                    } else if (type.equals(FastChatConstants.MESSAGE_TYPE_AUDIO)) {
                        viewHolder.resend_image.setVisibility(View.GONE);
                        viewHolder.audioWaveSeekBar.setVisibility(View.GONE);
                        //  viewHolder.audioWaveSeekBar.setWaveform(UUID.randomUUID().toString().getBytes());
                        viewHolder.txtMessage.setText(type);

                    }


                }


                showMessageTime(viewHolder, baseMessage);
                if (getSnapshots().get(getSnapshots().size() - 1).equals(baseMessage)) {
                    selectedItemList.add(baseMessage.getId());
                }
                if (selectedItemList.contains(baseMessage.getId()))
                    viewHolder.txtTime.setVisibility(View.VISIBLE);
                else
                    viewHolder.txtTime.setVisibility(View.GONE);

                viewHolder.rlMessageBubble.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if (!isImageMessageClick) {
                            isLongClickEnabled = true;
                            isTextMessageClick = true;
                            setLongClickSelectedItem(baseMessage);
                            messageLongClick.setLongMessageClick(longselectedItemList);
                            notifyDataSetChanged();
                        }
                        return true;
                    }
                });
                viewHolder.itemView.setTag(R.string.message, baseMessage);
            }
        }

    }


    private void setCustomData(CustomMessageViewHolder viewHolder, int i, BaseMessage baseMessage) {

        if (baseMessage != null) {
            if (!baseMessage.getUserId().equals(userId)) {
                if (baseMessage.getReceiverType().equals(FastChatConstants.RECEIVER_TYPE_USER)) {
                    viewHolder.tvUser.setVisibility(View.GONE);
                    viewHolder.ivUser.setVisibility(View.GONE);
                } else if (baseMessage.getReceiverType().equals(FastChatConstants.RECEIVER_TYPE_GROUP)) {
                    if (isUserDetailVisible) {
                        viewHolder.tvUser.setVisibility(View.VISIBLE);
                        viewHolder.ivUser.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.tvUser.setVisibility(View.GONE);
                        viewHolder.ivUser.setVisibility(View.INVISIBLE);
                    }
                    // setAvatar(viewHolder.ivUser, baseMessage.getSender().getAvatar(), baseMessage.getSender().getName());
                    //  viewHolder.tvUser.setText(baseMessage.getSender().getName());
                }
            }

            viewHolder.txtMessage.setText(context.getResources().getString(R.string.custom_message));
            //viewHolder.txtMessage.setTypeface(fontUtils.getTypeFace(FontUtils.robotoLight));
            if (baseMessage.getUserId().equals(userId))
                viewHolder.txtMessage.setTextColor(context.getResources().getColor(R.color.textColorWhite));
            else
                viewHolder.txtMessage.setTextColor(context.getResources().getColor(R.color.primaryTextColor));

            showMessageTime(viewHolder, baseMessage);
            if (getSnapshots().get(getSnapshots().size() - 1).equals(baseMessage)) {
                selectedItemList.add(baseMessage.getId());
            }
            if (selectedItemList.contains(baseMessage.getId()))
                viewHolder.txtTime.setVisibility(View.VISIBLE);
            else
                viewHolder.txtTime.setVisibility(View.GONE);

            viewHolder.rlMessageBubble.setOnClickListener(view -> {
                setSelectedMessage(baseMessage.getId());
                notifyDataSetChanged();

            });
            viewHolder.itemView.setTag(R.string.message, baseMessage);
        }
    }


    public void setSelectedMessage(Integer id) {
        if (selectedItemList.contains(id))
            selectedItemList.remove(id);
        else
            selectedItemList.add(id);
    }

    public void setLongClickSelectedItem(BaseMessage baseMessage) {


        if (longselectedItemList.contains(baseMessage))
            longselectedItemList.remove(baseMessage);
        else
            longselectedItemList.add(baseMessage);
    }


    @Override
    public long getHeaderId(int var1) {
        BaseMessage baseMessage = getItem(var1);
        return Long.parseLong(Utils.getDateId(baseMessage.getSentAt()));
    }

    @Override
    public DateItemHolder onCreateHeaderViewHolder(ViewGroup var1) {
        View view = LayoutInflater.from(var1.getContext()).inflate(R.layout.cc_message_list_header,
                var1, false);

        return new DateItemHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(DateItemHolder var1, int var2, long var3) {
        BaseMessage baseMessage = getItem(var2);
        Date date = new Date(baseMessage.getSentAt());
        String formattedDate = Utils.getDate(date.getTime());
        var1.txtMessageDate.setBackground(context.getResources().getDrawable(R.drawable.cc_rounded_date_button));
        var1.txtMessageDate.setText(formattedDate);
    }


    private int getItemViewTypes(int position) {
        BaseMessage baseMessage = getItem(position);
        if (baseMessage.getCategory() != null) {
            if (baseMessage.getDeletedAt() == 0) {
                String uid = baseMessage.getUserId();
                if (baseMessage.getCategory().equals(FastChatConstants.CATEGORY_MESSAGE)) {
                    switch (baseMessage.getType()) {
                        case FastChatConstants.MESSAGE_TYPE_TEXT:
                            if (uid.equals(userId)) {
                                if (baseMessage.getMetadata() != null && baseMessage.getMetadata().has("reply"))
                                    return RIGHT_REPLY_TEXT_MESSAGE;
                                else
                                    return RIGHT_TEXT_MESSAGE;
                            } else {
                                if (baseMessage.getMetadata() != null && baseMessage.getMetadata().has("reply"))
                                    return LEFT_REPLY_TEXT_MESSAGE;
                                else
                                    return LEFT_TEXT_MESSAGE;
                            }

                        case FastChatConstants.MESSAGE_TYPE_Link:
                            if (uid.equals(userId)) {
                                return RIGHT_LINK_MESSAGE;
                            } else {
                                return LEFT_LINK_MESSAGE;
                            }
                        case FastChatConstants.MESSAGE_TYPE_AUDIO:
                            if (uid.equals(userId)) {
                                return RIGHT_AUDIO_MESSAGE;
                            } else {
                                return LEFT_AUDIO_MESSAGE;
                            }
                        case FastChatConstants.MESSAGE_TYPE_Reply:
                            if (uid.equals(userId)) {
                                return RIGHT_Reply_MESSAGE;
                            } else {
                                return LEFT_Reply_MESSAGE;
                            }
                        case FastChatConstants.MESSAGE_TYPE_IMAGE:
                            if (uid.equals(userId)) {
                                return RIGHT_IMAGE_MESSAGE;
                            } else {
                                return LEFT_IMAGE_MESSAGE;
                            }
                        case FastChatConstants.MESSAGE_TYPE_VIDEO:
                            if (uid.equals(userId)) {
                                return RIGHT_VIDEO_MESSAGE;
                            } else {
                                return LEFT_VIDEO_MESSAGE;
                            }
                        case FastChatConstants.MESSAGE_TYPE_FILE:
                            if (uid.equals(userId)) {
                                return RIGHT_FILE_MESSAGE;
                            } else {
                                return LEFT_FILE_MESSAGE;
                            }
                        default:
                            return -1;
                    }
                } else {
                    if (baseMessage.getCategory().equals(FastChatConstants.CATEGORY_ACTION)) {
                        return ACTION_MESSAGE;
                    } else if (baseMessage.getCategory().equals(FastChatConstants.CATEGORY_CALL)) {
                        return CALL_MESSAGE;
                    } else if (baseMessage.getCategory().equals(FastChatConstants.CATEGORY_CUSTOM)) {
                        if (uid.equals(userId))
                            return RIGHT_CUSTOM_MESSAGE;
                        else
                            return LEFT_CUSTOM_MESSAGE;
                    }
                }
            } else {
                if (baseMessage.getCategory().equals(FastChatConstants.CATEGORY_MESSAGE)) {
                    if (baseMessage.getUserId().equals(userId)) {
                        return RIGHT_DELETE_MESSAGE;
                    } else {
                        return LEFT_DELETE_MESSAGE;

                    }

                }
            }
        }


        return -1;

    }

    public void setDeliveryReceipts(MessageReceipt messageReceipt) {

        for (int i = getItemCount() - 1; i >= 0; i--) {
            BaseMessage baseMessage = getSnapshots().get(i);
            if (baseMessage.getDeliveredAt() == 0) {
                int index = getSnapshots().indexOf(baseMessage);
                getSnapshots().get(index).setDeliveredAt(messageReceipt.getDeliveredAt());
            }
        }
        notifyDataSetChanged();
    }


    public void setReadReceipts() {
        for (int i = getItemCount() - 1; i >= 0; i--) {
            BaseMessage baseMessage = getSnapshots().get(i);
            if (baseMessage.getReadAt() == 0) {
                int index = getSnapshots().indexOf(baseMessage);
                getSnapshots().get(index).setReadAt(Calendar.getInstance().getTime().getTime());
            }
        }

        notifyDataSetChanged();
    }

    public void setUpdatedMessage(TextMessage baseMessage) {
        if (getSnapshots().contains(baseMessage)) {
            int index = getSnapshots().indexOf(baseMessage);
            getSnapshots().remove(baseMessage);
            getSnapshots().add(index, baseMessage);
            notifyItemChanged(index);
        }
    }

    public void resetList() {
        getSnapshots().clear();
        notifyDataSetChanged();
    }

    public void clearLongClickSelectedItem() {
        isLongClickEnabled = false;
        isTextMessageClick = false;
        isImageMessageClick = false;
        longselectedItemList.clear();
        notifyDataSetChanged();
    }

    public BaseMessage getLastMessage() {
        if (getItemCount() > 0) {
            Log.e(TAG, "getLastMessage: " + getSnapshots().get(getSnapshots().size() - 1));
            return getSnapshots().get(getSnapshots().size() - 1);
        } else
            return null;
    }

    public int getPosition(BaseMessage baseMessage) {
        return getSnapshots().indexOf(baseMessage);
    }

    class ImageMessageViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private CardView cardView;
        private Avatar ivUser;
        public TextView txtTime, tvUser;
        private RelativeLayout rlMessageBubble;
        private TextView tvThreadReplyCount;
        private LinearLayout lvReplyAvatar;

        private RelativeLayout sensitiveLayout;
        AdCircleProgress img_progress_bar;
        private RelativeLayout RelativeLayout_Reaction;
        ImageView Reaction;

        public ImageMessageViewHolder(@NonNull View view) {
            super(view);
            int type = (int) view.getTag();
            imageView = view.findViewById(R.id.go_img_message);
            img_progress_bar = view.findViewById(R.id.img_progress_bar);
            tvUser = view.findViewById(R.id.tv_user);
            cardView = view.findViewById(R.id.cv_image_message_container);
            txtTime = view.findViewById(R.id.txt_time);
            ivUser = view.findViewById(R.id.iv_user);
            rlMessageBubble = view.findViewById(R.id.rl_message);
            RelativeLayout_Reaction = view.findViewById(R.id.RelativeLayout_Reaction);
            Reaction = view.findViewById(R.id.Reaction);
            tvThreadReplyCount = view.findViewById(R.id.thread_reply_count);
            lvReplyAvatar = view.findViewById(R.id.reply_avatar_layout);
            sensitiveLayout = view.findViewById(R.id.sensitive_layout);
        }
    }

    public class ActionMessageViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public ActionMessageViewHolder(@NonNull View view) {
            super(view);
            int type = (int) view.getTag();
            textView = view.findViewById(R.id.go_txt_message);
        }
    }

    class VideoMessageViewHolder extends RecyclerView.ViewHolder {
        private ImageView playBtn;
        private CardView cardView;
        private Avatar ivUser;
        public TextView txtTime, tvUser;
        private RelativeLayout rlMessageBubble, cv_message_container, RelativeLayout_Reaction;
        private TextView tvThreadReplyCount;
        private LinearLayout lvReplyAvatar;
        ExoVideoView mVideoView;
        ExoMediaSourceHelper mHelper;
        AdCircleProgress img_progress_bar;
        RecyclerView RecyclerView_Reaction;

        public VideoMessageViewHolder(@NonNull View view) {
            super(view);
            int type = (int) view.getTag();
            mVideoView = view.findViewById(R.id.player);
            img_progress_bar = view.findViewById(R.id.img_progress_bar);
            playBtn = view.findViewById(R.id.playBtn);
            tvUser = view.findViewById(R.id.tv_user);
            cardView = view.findViewById(R.id.cv_image_message_container);
            txtTime = view.findViewById(R.id.txt_time);
            ivUser = view.findViewById(R.id.iv_user);
            rlMessageBubble = view.findViewById(R.id.rl_message);
            cv_message_container = view.findViewById(R.id.cv_message_container);
            RelativeLayout_Reaction = view.findViewById(R.id.RelativeLayout_Reaction);
            RecyclerView_Reaction = view.findViewById(R.id.RecyclerView_Reaction);
            tvThreadReplyCount = view.findViewById(R.id.thread_reply_count);
            lvReplyAvatar = view.findViewById(R.id.reply_avatar_layout);
            StandardVideoController controller = new StandardVideoController(view.getContext());
            controller.addDefaultControlComponent("custom exo", false);
            controller.setEnableOrientation(true);
            mVideoView.setVideoController(controller);
            mHelper = ExoMediaSourceHelper.getInstance(view.getContext());
        }
    }

    public class FileMessageViewHolder extends RecyclerView.ViewHolder {

        private TextView fileName;
        private TextView fileExt;
        public TextView txtTime;
        private TextView fileSize;
        private TextView tvUser;
        private View view;
        private Avatar ivUser;
        private RelativeLayout rlMessageBubble;
        private TextView tvThreadReplyCount;
        private LinearLayout lvReplyAvatar;
        private ImageView ivIcon;
        AdCircleProgress img_progress_bar;


        FileMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            fileSize = itemView.findViewById(R.id.tvFileSize);
            ivUser = itemView.findViewById(R.id.iv_user);
            tvUser = itemView.findViewById(R.id.tv_user);
            fileExt = itemView.findViewById(R.id.tvFileExtension);
            txtTime = itemView.findViewById(R.id.txt_time);
            fileName = itemView.findViewById(R.id.tvFileName);
            rlMessageBubble = itemView.findViewById(R.id.rl_message);
            tvThreadReplyCount = itemView.findViewById(R.id.thread_reply_count);
            lvReplyAvatar = itemView.findViewById(R.id.reply_avatar_layout);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            img_progress_bar = itemView.findViewById(R.id.img_progress_bar);
            this.view = itemView;
        }
    }

    public class DeleteMessageViewHolder extends RecyclerView.ViewHolder {

        private TextView txtMessage;
        private FrameLayout cardView;
        private View view;
        public TextView txtTime;
        public TextView tvUser;
        private ImageView imgStatus;
        private int type;
        private Avatar ivUser;
        private RelativeLayout rlMessageBubble;
        private TextView tvThreadReplyCount;
        private LinearLayout lvReplyAvatar;

        DeleteMessageViewHolder(@NonNull View view) {
            super(view);
            type = (int) view.getTag();
            tvUser = view.findViewById(R.id.tv_user);
            txtMessage = view.findViewById(R.id.go_txt_message);
            cardView = view.findViewById(R.id.cv_message_container);
            txtTime = view.findViewById(R.id.txt_time);
            imgStatus = view.findViewById(R.id.img_pending);
            ivUser = view.findViewById(R.id.iv_user);
            rlMessageBubble = view.findViewById(R.id.rl_message);
            tvThreadReplyCount = view.findViewById(R.id.thread_reply_count);
            lvReplyAvatar = view.findViewById(R.id.reply_avatar_layout);
            this.view = view;
        }
    }

    public class TextMessageViewHolder extends RecyclerView.ViewHolder {

        private TextView txtMessage;
        private TextView tvThreadReplyCount;
        private FrameLayout cardView;
        private View view;
        public TextView txtTime;
        public TextView tvUser;
        private ImageView imgStatus;
        private int type;
        private Avatar ivUser;
        private RelativeLayout rlMessageBubble;

        private RelativeLayout sentimentVw;

        private RelativeLayout RelativeLayout_Reaction;
        RecyclerView RecyclerView_Reaction;


        private TextView viewSentimentMessage;
        //  private RelativeLayout replyLayout;
        // private TextView replyUser;
        //  private TextView replyMessage;
        private LinearLayout lvReplyAvatar;

        FrameLayout cv_message_container;

        TextMessageViewHolder(@NonNull View view) {
            super(view);

            type = (int) view.getTag();
            tvUser = view.findViewById(R.id.tv_user);
            RecyclerView_Reaction = view.findViewById(R.id.RecyclerView_Reaction);
            txtMessage = view.findViewById(R.id.go_txt_message);
            cardView = view.findViewById(R.id.cv_message_container);
            txtTime = view.findViewById(R.id.txt_time);
            imgStatus = view.findViewById(R.id.img_pending);
            ivUser = view.findViewById(R.id.iv_user);
            rlMessageBubble = view.findViewById(R.id.rl_message);
            cv_message_container = view.findViewById(R.id.cv_message_container);
            RelativeLayout_Reaction = view.findViewById(R.id.RelativeLayout_Reaction);

            //  replyLayout = view.findViewById(R.id.replyLayout);
            //  replyUser = view.findViewById(R.id.reply_user);
            //  replyMessage = view.findViewById(R.id.reply_message);
            tvThreadReplyCount = view.findViewById(R.id.thread_reply_count);
            lvReplyAvatar = view.findViewById(R.id.reply_avatar_layout);
            //  sentimentVw = view.findViewById(R.id.sentiment_layout);
            //  viewSentimentMessage = view.findViewById(R.id.view_sentiment);
            this.view = view;

        }
    }


    public class LinkMessageViewHolder extends RecyclerView.ViewHolder {

        private TextView txtMessage;
        private TextView tvThreadReplyCount;
        private FrameLayout cardView;
        private View view;
        public TextView txtTime;
        public TextView tvUser;
        private ImageView imgStatus;
        private int type;
        private Avatar ivUser;
        private RelativeLayout rlMessageBubble;

        private RelativeLayout sentimentVw;

        private RelativeLayout RelativeLayout_Reaction;
        RecyclerView RecyclerView_Reaction;


        private TextView viewSentimentMessage;
        //  private RelativeLayout replyLayout;
        // private TextView replyUser;
        //  private TextView replyMessage;
        private LinearLayout lvReplyAvatar;

        FrameLayout cv_message_container;
        RichLinkView richLink;

        LinkMessageViewHolder(@NonNull View view) {
            super(view);

            type = (int) view.getTag();
            tvUser = view.findViewById(R.id.tv_user);
            RecyclerView_Reaction = view.findViewById(R.id.RecyclerView_Reaction);
            txtMessage = view.findViewById(R.id.go_txt_message);
            cardView = view.findViewById(R.id.cv_message_container);
            txtTime = view.findViewById(R.id.txt_time);
            imgStatus = view.findViewById(R.id.img_pending);
            ivUser = view.findViewById(R.id.iv_user);
            rlMessageBubble = view.findViewById(R.id.rl_message);
            cv_message_container = view.findViewById(R.id.cv_message_container);
            RelativeLayout_Reaction = view.findViewById(R.id.RelativeLayout_Reaction);
            richLink = view.findViewById(R.id.richLink);

            //  replyLayout = view.findViewById(R.id.replyLayout);
            //  replyUser = view.findViewById(R.id.reply_user);
            //  replyMessage = view.findViewById(R.id.reply_message);
            tvThreadReplyCount = view.findViewById(R.id.thread_reply_count);
            lvReplyAvatar = view.findViewById(R.id.reply_avatar_layout);
            //  sentimentVw = view.findViewById(R.id.sentiment_layout);
            //  viewSentimentMessage = view.findViewById(R.id.view_sentiment);
            this.view = view;

        }
    }


    public class ReplyMessageViewHolder extends RecyclerView.ViewHolder {

        private TextView txtMessage, reply_txt_message;
        private TextView tvThreadReplyCount;
        private View view;
        public TextView txtTime;
        private ImageView imgStatus;
        private int type;
        private Avatar ivUser;
        private RelativeLayout rlMessageBubble;

        private RelativeLayout sentimentVw;

        private RelativeLayout RelativeLayout_Reaction;
        AudioWaveSeekBar audioWaveSeekBar;

        private TextView viewSentimentMessage;
        //  private RelativeLayout replyLayout;
        // private TextView replyUser;
        //  private TextView replyMessage;
        private LinearLayout lvReplyAvatar;

        LinearLayout cv_message_container;
        ImageView Reaction;
        ImageView resend_image;
        FrameLayout r_image;
        RecyclerView RecyclerView_Reaction;

        ReplyMessageViewHolder(@NonNull View view) {
            super(view);

            type = (int) view.getTag();
            Reaction = view.findViewById(R.id.Reaction);
            txtMessage = view.findViewById(R.id.go_txt_message);
            reply_txt_message = view.findViewById(R.id.reply_txt_message);
            resend_image = view.findViewById(R.id.resend_image);
            r_image = view.findViewById(R.id.r_image);

            audioWaveSeekBar = view.findViewById(R.id.seekBar);
            RecyclerView_Reaction = view.findViewById(R.id.RecyclerView_Reaction);


            txtTime = view.findViewById(R.id.txt_time);
            rlMessageBubble = view.findViewById(R.id.rl_message);
            cv_message_container = view.findViewById(R.id.cv_message_container);
            RelativeLayout_Reaction = view.findViewById(R.id.RelativeLayout_Reaction);


            //  replyLayout = view.findViewById(R.id.replyLayout);
            //  replyUser = view.findViewById(R.id.reply_user);
            //  replyMessage = view.findViewById(R.id.reply_message);
            tvThreadReplyCount = view.findViewById(R.id.thread_reply_count);
            lvReplyAvatar = view.findViewById(R.id.reply_avatar_layout);
            //  sentimentVw = view.findViewById(R.id.sentiment_layout);
            //  viewSentimentMessage = view.findViewById(R.id.view_sentiment);
            this.view = view;

        }
    }

    public class CustomMessageViewHolder extends RecyclerView.ViewHolder {

        private TextView txtMessage;
        private RelativeLayout cardView;
        private View view;
        public TextView txtTime;
        public TextView tvUser;
        private ImageView imgStatus;
        private int type;
        private Avatar ivUser;
        private RelativeLayout rlMessageBubble;


        CustomMessageViewHolder(@NonNull View view) {
            super(view);

            type = (int) view.getTag();
            tvUser = view.findViewById(R.id.tv_user);
            txtMessage = view.findViewById(R.id.go_txt_message);
            cardView = view.findViewById(R.id.cv_message_container);
            txtTime = view.findViewById(R.id.txt_time);
            imgStatus = view.findViewById(R.id.img_pending);
            ivUser = view.findViewById(R.id.iv_user);
            rlMessageBubble = view.findViewById(R.id.rl_message);
            this.view = view;
        }
    }

    public class AudioMessageViewHolder extends RecyclerView.ViewHolder {

        private TextView txtProcess;
        private ImageView playBtn;
        private int type;
        private TextView tvUser;
        private Avatar ivUser;
        private RelativeLayout rlMessageBubble;
        private TextView txtTime;
        private TextView tvThreadReplyCount;
        private LinearLayout lvReplyAvatar;
        AudioWaveSeekBar audioRecordView;
        private Runnable timerRunnable;

        private Handler seekHandler = new Handler(Looper.getMainLooper());
        private boolean isPlaying;
        private boolean autoRewind;

        private Timer timer = new Timer();
        RelativeLayout cv_message_container;
        AdCircleProgress img_progress_bar;
        private final AudioView audioView;
        RecyclerView RecyclerView_Reaction;
        private RelativeLayout RelativeLayout_Reaction;

        public AudioMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            type = (int) itemView.getTag();
            img_progress_bar = itemView.findViewById(R.id.img_progress_bar);
            txtProcess = itemView.findViewById(R.id.txtTime);
            playBtn = itemView.findViewById(R.id.playBtn);
            rlMessageBubble = itemView.findViewById(R.id.cv_message_container);
            tvUser = itemView.findViewById(R.id.tv_user);
            ivUser = itemView.findViewById(R.id.iv_user);
            txtTime = itemView.findViewById(R.id.txt_time);
            tvThreadReplyCount = itemView.findViewById(R.id.thread_reply_count);
            lvReplyAvatar = itemView.findViewById(R.id.reply_avatar_layout);
            audioRecordView = itemView.findViewById(R.id.seekBar);
            cv_message_container = itemView.findViewById(R.id.cv_message_container);
            RecyclerView_Reaction = itemView.findViewById(R.id.RecyclerView_Reaction);
            RelativeLayout_Reaction = itemView.findViewById(R.id.RelativeLayout_Reaction);

            audioRecordView.setWaveform(UUID.randomUUID().toString().getBytes());
            audioView = new AudioView(itemView.getContext(), itemView);


        }

        //Our method that sets Vizualizer
        private void setupVisualizerFxAndUI(MediaMessage mediaMessage) {
            audioView.setAudio(mediaMessage, 0);
        }

        public void playCycle() {
            audioRecordView.setProgress(mediaPlayer.getCurrentPosition());
            if (mediaPlayer.isPlaying()) {

                timerRunnable = new Runnable() {
                    @Override
                    public void run() {
                        playCycle();
                    }
                };
                seekHandler.postDelayed(timerRunnable, 1000);
            }
        }

        public void txtProcess() {
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    txtProcess.setText("00:00:00/" + convertSecondsToHMmSs(mp.getDuration() / 1000));
                }
            });

        }

        private void startRecording(String path) {
            playCycle();
            try {
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        audioRecordView.setDuration(mp.getDuration());

                    }
                });

                final int duration = mediaPlayer.getDuration();
                timerRunnable = new Runnable() {
                    @Override
                    public void run() {
                        int pos = mediaPlayer.getCurrentPosition();
                        audioRecordView.setProgress(pos);

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
                    //                audioLength.setText(Utils.convertTimeStampToDurationTime(duration));
                    audioRecordView.setProgress(0);
//                playButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private String convertSecondsToHMmSs(long seconds) {
            long s = seconds % 60;
            long m = (seconds / 60) % 60;
            long h = (seconds / (60 * 60)) % 24;
            return String.format("%02d:%02d:%02d", h, m, s);
        }


    }


    public class DateItemHolder extends RecyclerView.ViewHolder {

        TextView txtMessageDate;

        DateItemHolder(@NonNull View itemView) {
            super(itemView);
            txtMessageDate = itemView.findViewById(R.id.txt_message_date);
        }
    }

    public interface OnMessageLongClick {
        void setLongMessageClick(List<BaseMessage> baseMessage);
    }

    public interface OnSelectedMessageClick {
        void setMessageClick(int postion);
    }

    public void setReaction(ReactionBottomSheetDialog.Action action) {
        reactionBottomSheetDialog = new ReactionBottomSheetDialog(context, action);
        if (FastChat.getUiConfig().isIncludeReactions()) {
            reactionBottomSheetDialog.show();
        }
    }
}



