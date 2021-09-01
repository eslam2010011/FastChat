package com.fastchat.Ui.Chat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fastchat.Core.FastChat;
import com.fastchat.Model.BaseMessage;
import com.fastchat.Model.Call;
import com.fastchat.Model.FastChatConstants;
import com.fastchat.Model.LinkMessage;
import com.fastchat.Model.MediaMessage;
import com.fastchat.Model.ReplyMessage;
import com.fastchat.Model.ReplyMessageG;
import com.fastchat.Model.TextMessage;
import com.fastchat.Model.User;
import com.fastchat.R;
import com.fastchat.adapter.ConversationListAdapter;
import com.fastchat.adapter.MessageAdapter2;
import com.fastchat.chat_interactore.Chat;
import com.fastchat.chat_interactore.ChatInteract;
import com.fastchat.utils.KeyBoardUtils;
import com.fastchat.utils.MediaUtils;
import com.fastchat.utils.StickyHeaderDecoration;
import com.fastchat.utils.TypingStat;
import com.fastchat.utils.Utils;
import com.fastchat.widget.Avatar;
import com.fastchat.widget.ComposeActionListener;
import com.fastchat.widget.ComposeBox;
import com.fastchat.widget.audio_record_view.AudioRecordView;
import com.fastchat.widget.swipetoreply.ISwipeControllerActions;
import com.fastchat.widget.swipetoreply.SwipeController;
 import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static com.fastchat.Core.FastChat.getContext;

public class fragmentChat extends Fragment {
    AudioRecordView audioRecordView;
    private long time;
    private ArrayList<String> returnValue = new ArrayList<>();
    MessageAdapter2 messageAdapter2;
    private LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerViewMessages;
    StickyHeaderDecoration stickyHeaderDecoration;
    String receiverId, userId;
    private String audioFileNameWithPath;
    LinearLayout LinearLayoutTyping, layoutchat;
    ImageView voice_call, video_call, back;
    TextView info;
    TextView Typing;


    ComposeBox composeBox;
    public int position;
    Avatar avatar;
    TextView tv_user, tv_tiem;
    String groupId;
    private ChildEventListener messagesChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            if (dataSnapshot.exists()) {
                BaseMessage baseMessage = dataSnapshot.getValue(BaseMessage.class);
                if (baseMessage != null && baseMessage.getCategory() != null) {
                    if (userId.equals(baseMessage.getUserId())) {
                        FastChat.getFastChat().getChatInteract().UpdateStatusMessage(baseMessage.getGroupId(), userId, receiverId, dataSnapshot.getKey(), "readByMeAt");
                        FastChat.getFastChat().getChatInteract().UpdateStatusMessage(baseMessage.getGroupId(), userId, receiverId, dataSnapshot.getKey(), "deliveredToMeAt");
                    } else {
                        FastChat.getFastChat().getChatInteract().UpdateStatusMessage(baseMessage.getGroupId(), userId, receiverId, dataSnapshot.getKey(), "deliveredAt");
                        FastChat.getFastChat().getChatInteract().UpdateStatusMessage(baseMessage.getGroupId(), userId, receiverId, dataSnapshot.getKey(), "readAt");
                        scrollToBottom();

                    }
                }

            }


        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleArguments();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmentchat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewComponent(view);
        UiConfig();

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        initMessageAdapter();
        KeyBoardUtils.setKeyboardVisibilityListener(getActivity(), (View) recyclerViewMessages.getParent(), keyboardVisible -> {
            if (keyboardVisible) {
                // scrollToBottom();
                composeBox.imageViewSend.setVisibility(View.VISIBLE);
                composeBox.imageViewAudio.setVisibility(GONE);
            } else {
                composeBox.imageViewSend.setVisibility(GONE);
                composeBox.imageViewAudio.setVisibility(View.VISIBLE);
            }
        });

        SwipeController controller = new SwipeController(getActivity(), new ISwipeControllerActions() {
            @Override
            public void onSwipePerformed(int position) {
                fragmentChat.this.position = position;
                MediaUtils.vibrate(getActivity());
                composeBox.reply(true, messageAdapter2.getSnapshots().get(position));
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(controller);
        itemTouchHelper.attachToRecyclerView(recyclerViewMessages);
        setComposeBoxListener();
        isTyping();
        setUser();
        FastChat.getFastChat().getChatInteract().GetMessage(50, groupId, receiverId).addChildEventListener(messagesChildEventListener);
    }

    private void initViewComponent(View view) {
        composeBox = view.findViewById(R.id.labeled);
        Typing = view.findViewById(R.id.typing);
        recyclerViewMessages = view.findViewById(R.id.recyclerViewMessages);
        layoutchat = view.findViewById(R.id.layoutchat);
        LinearLayoutTyping = view.findViewById(R.id.LinearLayoutTyping);
        avatar = view.findViewById(R.id.iv_user);
        tv_user = view.findViewById(R.id.tv_user);
        video_call = view.findViewById(R.id.video_call);
        voice_call = view.findViewById(R.id.voice_call);
        info = view.findViewById(R.id.info);
        back = view.findViewById(R.id.back);
        tv_tiem = view.findViewById(R.id.tv_tiem);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        video_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FastPermissions.with(getActivity())
//                        .request(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)
//                        .ifNecessary()
//                        .withRationaleDialog(getString(R.string.ConversationActivity_signal_needs_the_microphone_and_camera_permissions_in_order_to_call_s),
//                                R.drawable.ic_baseline_mic_24,
//                                R.drawable.ic_videocam_black_24dp)
//                        .withPermanentDenialDialog(getString(R.string.ConversationActivity_signal_needs_the_microphone_and_camera_permissions_in_order_to_call_s))
//                        .onAllGranted(() -> {
//                            FastChat.getFastChat().getChatInteract().sendMessageCall(groupId, FastChatConstants.MESSAGE_TYPE_VIDEO, receiverId, userId, new Chat.CallbackListener() {
//                                @Override
//                                public void onSuccess(BaseMessage textMessage) {
//                                    MediaUtils.playSendSound(getActivity(), R.raw.outgoing_message);
//                                    SendNotification(receiverId, (Call) textMessage);
//                                    DatabaseReference databaseReference = FastChat.getFastChat().getChatInteract().getUser(receiverId);
//                                    ValueEventListener valueEventListener = new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                            User user1 = snapshot.getValue(User.class);
//                                            if ((user1 != null ? user1.getUserName() : null) != null) {
//                                                if (FastChat.getUiConfig().getOnChatListener() != null) {
//                                                    FastChat.getUiConfig().getOnChatListener().onVideo(user1, (Call) textMessage);
//                                                }
//
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError error) {
//
//                                        }
//                                    };
//                                    databaseReference.addListenerForSingleValueEvent(valueEventListener);
//                                }
//                            });
//
//                        })
//                        .execute();


            }
        });
        voice_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FastPermissions.with(getActivity())
//                        .request(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)
//                        .ifNecessary()
//                        .withRationaleDialog(getString(R.string.ConversationActivity_signal_needs_the_microphone_and_camera_permissions_in_order_to_call_s),
//                                R.drawable.ic_baseline_mic_24,
//                                R.drawable.ic_videocam_black_24dp)
//                        .withPermanentDenialDialog(getString(R.string.ConversationActivity_signal_needs_the_microphone_and_camera_permissions_in_order_to_call_s))
//                        .onAllGranted(() -> {
//                            //SendNotificationCall();
//                            FastChat.getFastChat().getChatInteract().sendMessageCall(groupId, FastChatConstants.MESSAGE_TYPE_VIDEO, receiverId, userId, new Chat.CallbackListener() {
//                                @Override
//                                public void onSuccess(BaseMessage textMessage) {
//                                    MediaUtils.playSendSound(getActivity(), R.raw.outgoing_message);
//                                    SendNotification(receiverId, (Call) textMessage);
//                                    //SendNotification((Call) textMessage);
//                                    DatabaseReference databaseReference = FastChat.getFastChat().getChatInteract().getUser(receiverId);
//                                    ValueEventListener valueEventListener = new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                            User user1 = snapshot.getValue(User.class);
//                                            if ((user1 != null ? user1.getUserName() : null) != null) {
//                                                if (FastChat.getUiConfig().getOnChatListener() != null) {
//                                                    FastChat.getUiConfig().getOnChatListener().onVoice(user1, (Call) textMessage);
//                                                }
//
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError error) {
//
//                                        }
//                                    };
//                                    databaseReference.addListenerForSingleValueEvent(valueEventListener);
//                                }
//                            });
//
//                        })
//                        .execute();


            }
        });


    }

    private void setComposeBoxListener() {
        composeBox.setComposeBoxListener(new ComposeActionListener() {


                                             @Override
                                             public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                             }

                                             @Override
                                             public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                                 if (charSequence.toString().trim().length() == 0) {
                                                     composeBox.imageViewSend.setVisibility(GONE);
                                                     composeBox.imageViewAudio.setVisibility(View.VISIBLE);
                                                 } else {
                                                     composeBox.imageViewAudio.setVisibility(GONE);
                                                     composeBox.imageViewSend.setVisibility(View.VISIBLE);
                                                 }
                                             }

                                             @Override
                                             public void afterTextChanged(Editable editable) {


                                             }

                                             @Override
                                             public void IsTyping(boolean IsTyping) {
                                                 FastChat.getFastChat().getChatInteract().addTyping(userId, receiverId, null, "IsTyping", IsTyping);
                                             }

                                             @Override
                                             public void onLinkComplete(BaseMessage baseMessage, EditText editText, boolean Reply) {
                                                 super.onLinkComplete(baseMessage, editText, Reply);
                                                 String message = editText.getText().toString().trim();
                                                 editText.setText("");
                                                 FastChat.getFastChat().getChatInteract().sendMessageLink(groupId, message, receiverId, userId, new Chat.CallbackListener() {
                                                     @Override
                                                     public void onSuccess(BaseMessage textMessage) {
                                                         MediaUtils.playSendSound(getActivity(), R.raw.outgoing_message);
                                                         scrollToBottom();
                                                         SendNotification(receiverId, (LinkMessage) textMessage);
                                                     }
                                                 });

                                             }

                                             @Override
                                             public void onSendActionClicked(EditText editText) {
                                                 String message = editText.getText().toString().trim();
                                                 editText.setText("");
                                                 FastChat.getFastChat().getChatInteract().sendMessage(groupId, message, receiverId, userId, new Chat.CallbackListener() {
                                                     @Override
                                                     public void onSuccess(BaseMessage textMessage) {
                                                         MediaUtils.playSendSound(getActivity(), R.raw.outgoing_message);
                                                         scrollToBottom();
                                                         SendNotification(receiverId, (TextMessage) textMessage);
                                                     }
                                                 });


                                             }

                                             @Override
                                             public void onReplyComplete(BaseMessage baseMessage, EditText editText, boolean Reply) {
                                                 super.onReplyComplete(baseMessage, editText, Reply);
                                                 if (Reply) {
                                                     String message = editText.getText().toString().trim();
                                                     editText.setText("");
                                                     FastChat.getFastChat().getChatInteract().sendReplyMessage(groupId, messageAdapter2.getSnapshots().get(position), position, message, receiverId, userId, new Chat.CallbackListener() {
                                                         @Override
                                                         public void onSuccess(BaseMessage textMessage) {
                                                             composeBox.reply(false, messageAdapter2.getSnapshots().get(position));
                                                             MediaUtils.playSendSound(getActivity(), R.raw.outgoing_message);
                                                             scrollToBottom();
                                                             SendNotification(receiverId, (ReplyMessage) textMessage);
                                                             fragmentChat.this.position = -1;

                                                         }
                                                     });
                                                 } else {
                                                     fragmentChat.this.position = -1;
                                                 }

                                             }

                                             @Override
                                             public void onMoreActionClicked(ImageView moreIcon) {
                                                 super.onMoreActionClicked(moreIcon);
                                             }

                                             @Override
                                             public void onCameraActionClicked(ImageView cameraIcon) {
                                                 super.onCameraActionClicked(cameraIcon);
                                             }

                                             @Override
                                             public void onGalleryActionClicked(ImageView galleryIcon) {
                                                 Options options = Options.init()
                                                         .setRequestCode(100)                                           //Request code for activity results
                                                         .setCount(1)
                                                         .setPreSelectedUrls(returnValue)                 //Number of images to restict selection count
                                                         .setFrontfacing(false)                                         //Front Facing camera on start
                                                         .setExcludeVideos(false)
                                                         //Option to exclude videos
                                                         .setVideoDurationLimitinSeconds(30)                            //Duration for video recording
                                                         .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                                                         .setPath("/pix/images");//Custom Path For media Storage
                                                 Pix.start(fragmentChat.this, options);
                                             }

                                             @Override
                                             public void onAudioActionClicked(ImageView audioIcon) {
                                                 super.onAudioActionClicked(audioIcon);
                                             }

                                             @Override
                                             public void onFileActionClicked(ImageView fileIcon) {
                                                 super.onFileActionClicked(fileIcon);
                                             }

                                             @Override
                                             public void onEmojiActionClicked(ImageView emojiIcon) {
                                                 super.onEmojiActionClicked(emojiIcon);
                                             }

                                             @Override
                                             public void onVoiceNoteComplete(String string) {
                                                 if (string != null) {
                                                     FastChat.getFastChat().getChatInteract().sendMediaMessage(groupId, new File(string), FastChatConstants.MESSAGE_TYPE_AUDIO, receiverId, userId, new Chat.CallbackListener() {
                                                         @Override
                                                         public void onSuccess(BaseMessage textMessage) {
                                                             MediaUtils.playSendSound(getActivity(), R.raw.outgoing_message);
                                                             scrollToBottom();
                                                             SendNotification(receiverId, (MediaMessage) textMessage);
                                                         }
                                                     });
                                                     //sendMediaMessage(audioFile, CometChatConstants.MESSAGE_TYPE_AUDIO);
                                                 }
                                             }


                                         }

        );
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
                }
                if (new File(returnValue.get(0).toString()).getAbsolutePath().endsWith("mp4")) {
                    if (FastChat.getUiConfig().isIncludeVideo()){
                        FastChat.getFastChat().getChatInteract().sendMediaMessage(groupId, new File(returnValue.get(0).toString()), FastChatConstants.MESSAGE_TYPE_VIDEO, receiverId, userId, new Chat.CallbackListener() {
                            @Override
                            public void onSuccess(BaseMessage textMessage) {
                                MediaUtils.playSendSound(getActivity(), R.raw.outgoing_message);
                                scrollToBottom();
                                SendNotification(receiverId, (MediaMessage) textMessage);
                            }
                        });
                    }

                } else {
                    FastChat.getFastChat().getChatInteract().sendMediaMessage(groupId, new File(returnValue.get(0).toString()), FastChatConstants.MESSAGE_TYPE_IMAGE, receiverId, userId, new Chat.CallbackListener() {
                        @Override
                        public void onSuccess(BaseMessage textMessage) {
                            MediaUtils.playSendSound(getActivity(), R.raw.outgoing_message);
                            scrollToBottom();
                            SendNotification(receiverId, (MediaMessage) textMessage);
                        }
                    });
                }


            }
        } else {
          /*  if (requestCode == 200) {
                if (resultCode == RESULT_OK && data != null) {
                    dataList = data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS);

                    try {
                        String path = ContentUriUtils.INSTANCE.getFilePath(context, dataList.get(0));
                        sendMediaMessage(new File(path), CometChatConstants.MESSAGE_TYPE_FILE);

                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
*/
        }

    }

    private void handleArguments() {
        if (getArguments() != null) {
            userId = getArguments().getString("userId");
            receiverId = getArguments().getString("receiverId");

            groupId = getArguments().getString("groupId");
            if (groupId != null) {
                groupId = getArguments().getString("groupId");
            } else {
                groupId = ChatInteract.setOneToOneChat(userId, receiverId);
            }
        }
    }

    private void initMessageAdapter() {
        FirebaseRecyclerOptions<BaseMessage> options =
                new FirebaseRecyclerOptions.Builder<BaseMessage>()
                        .setQuery(FastChat.getFastChat().getChatInteract().GetMessage(50, groupId, receiverId), new SnapshotParser<BaseMessage>() {
                            @NonNull
                            @Override
                            public BaseMessage parseSnapshot(@NonNull DataSnapshot snapshot) {
                                BaseMessage university = snapshot.getValue(BaseMessage.class);
                                if (university != null) {
                                    if (university.getCategory() != null) {
                                        if (university.getCategory().equals(FastChatConstants.CATEGORY_MESSAGE)) {
                                            if (university.getType().equals("text")) {
                                                Log.d("snapshot", university.getType());
                                                return snapshot.getValue(TextMessage.class);

                                            } else if (university.getType().equals(FastChatConstants.MESSAGE_TYPE_Reply)) {
                                                return snapshot.getValue(ReplyMessageG.class);
                                            } else if (university.getType().equals(FastChatConstants.MESSAGE_TYPE_Link)) {
                                                return snapshot.getValue(LinkMessage.class);
                                            } else {
                                                MediaMessage mediaMessage = snapshot.getValue(MediaMessage.class);
                                                if (mediaMessage != null) {
                                                    return mediaMessage;
                                                }
                                            }


                                        } else if (university.getCategory().equals(FastChatConstants.CATEGORY_CALL)) {
                                            Call call = snapshot.getValue(Call.class);
                                            if (call != null) {
                                                return call;
                                            }

                                        }
                                    }

                                }


                                return snapshot.getValue(TextMessage.class);
                            }
                        })
                        .build();
        messageAdapter2 = new MessageAdapter2(getActivity(), userId, options, new MessageAdapter2.OnSelectedMessageClick() {
            @Override
            public void setMessageClick(int postion) {
                linearLayoutManager.scrollToPosition(postion);
                //  RecyclerView.ViewHolder viewHolder = recyclerViewMessages.findViewHolderForItemId(position);
                // View view = viewHolder.itemView;
            }
        });
        Log.v("messageAdapter2", options.getSnapshots() + "");
        recyclerViewMessages.setLayoutManager(linearLayoutManager);
        recyclerViewMessages.setAdapter(messageAdapter2);
        recyclerViewMessages.setHasFixedSize(true);
        stickyHeaderDecoration = new StickyHeaderDecoration(messageAdapter2);
        recyclerViewMessages.addItemDecoration(stickyHeaderDecoration, 0);
        messageAdapter2.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = messageAdapter2.getItemCount();
                int lastVisiblePosition =
                        linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    linearLayoutManager.scrollToPosition(positionStart);
                }
            }
        });

       /* rvChatListView.setOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) { // when we have reached end of RecyclerView this event fired
                loadMoreData();
            }
        });*/
        scrollToBottom();

    }

    @Override
    public void onStart() {
        super.onStart();
        messageAdapter2.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        messageAdapter2.stopListening();
    }

    private void scrollToBottom() {
        if (messageAdapter2 != null && messageAdapter2.getItemCount() > 0) {
            recyclerViewMessages.scrollToPosition(messageAdapter2.getItemCount() - 1);

        }
    }


    public void UiConfig() {
        if (FastChat.getUiConfig().getColorGeneral() != null) {
            video_call.setColorFilter(Color.parseColor(FastChat.getUiConfig().getColorGeneral()),
                    PorterDuff.Mode.SRC_IN);
            voice_call.setColorFilter(Color.parseColor(FastChat.getUiConfig().getColorGeneral()),
                    PorterDuff.Mode.SRC_IN);
            back.setColorFilter(Color.parseColor(FastChat.getUiConfig().getColorGeneral()),
                    PorterDuff.Mode.SRC_IN);
            info.getBackground().setColorFilter(Color.parseColor(FastChat.getUiConfig().getColorGeneral()),
                    PorterDuff.Mode.SRC_IN);
            composeBox.ivMic.setColorFilter(Color.parseColor(FastChat.getUiConfig().getColorGeneral()),
                    PorterDuff.Mode.SRC_IN);
            composeBox.ivSend.setColorFilter(Color.parseColor(FastChat.getUiConfig().getColorGeneral()),
                    PorterDuff.Mode.SRC_IN);
        }
        if (FastChat.getUiConfig().getBackgroundChat() == -1) {
        } else {
            layoutchat.setBackgroundResource(FastChat.getUiConfig().getBackgroundChat());
        }
        if (FastChat.getUiConfig().isButtonVideo()) {
            video_call.setVisibility(View.VISIBLE);
        } else {
            video_call.setVisibility(View.INVISIBLE);

        }
        if (FastChat.getUiConfig().isButtonVoice()) {
            voice_call.setVisibility(View.VISIBLE);
        } else {
            voice_call.setVisibility(View.INVISIBLE);

        }
        if (FastChat.getUiConfig().isInfoView()) {
            info.setVisibility(View.VISIBLE);
        } else {
            info.setVisibility(View.INVISIBLE);

        }
        if (FastChat.getUiConfig().isBottomView()) {
            composeBox.setVisibility(View.VISIBLE);
        } else {
            composeBox.setVisibility(GONE);

        }


    }

    public void SendNotification(String userId, BaseMessage baseMessage) {
        FastChat.getFastChat().getChatInteract().getUser(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user1 = snapshot.getValue(User.class);
                if (user1 != null && user1.getToken() != null) {
                    if (FastChat.getFastChat().getNotificationContractor() != null) {
                        FastChat.getFastChat().getNotificationContractor().sendNotification(user1.getToken(), user1, baseMessage);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       // FastPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    public void setUser() {
        FastChat.getFastChat().getChatInteract().getUser(receiverId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user1 = snapshot.getValue(User.class);
                if ((user1 != null ? user1.getUserName() : null) != null) {
                    avatar.setInitials(user1.getUserName());
                    tv_user.setText(user1.getUserName());
                    if (user1.getLastSeenAt() != null) {
                        if (user1.getLastSeenAt() == -1) {
                            tv_tiem.setText("Online Now");
                        } else {
                            tv_tiem.setText(Utils.getTimeAgo(user1.getLastSeenAt()));

                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void isTyping() {
        FastChat.getFastChat().getChatInteract().GetTyping(receiverId, userId).addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("IsTyping")) {
                    boolean IsTyping = dataSnapshot.child("IsTyping").getValue(boolean.class);
                    if (IsTyping) {
                        Typing.setVisibility(View.VISIBLE);
                        tv_tiem.setVisibility(GONE);
                        Typing.setText("typing...");
                        //  tv_tiem.setText(TypingStat.getStatString(getActivity(), 1));
                        //  tv_tiem.setTextColor(R.color.colorPrimary);
                        //  LinearLayoutTyping.setVisibility(View.VISIBLE);
                    } else {
                        //  LinearLayoutTyping.setVisibility(View.GONE);
                        Typing.setVisibility(GONE);
                        tv_tiem.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

