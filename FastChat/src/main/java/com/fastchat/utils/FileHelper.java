package com.fastchat.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

 import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fastchat.Core.FastChat;
import com.fastchat.Model.FastChatConstants;
import com.fastchat.Model.MediaMessage;
import com.fastchat.Model.User;
import com.fastchat.R;
import com.fastchat.chat_interactore.Chat;
import com.fastchat.utils.FirebaseUpload.FileUploadResult;
import com.fastchat.utils.FirebaseUpload.ParseFileUtils;
import com.fastchat.widget.adprogressbarlib.AdCircleProgress;
import com.fastchat.widget.exo.ExoVideoView;
import com.google.android.gms.common.util.SharedPreferencesUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import static com.fastchat.utils.FirebaseUpload.FirebaseUploadHandler.generateRandomName;
import static com.fastchat.utils.FirebaseUpload.FirebaseUploadHandler.storage;


public class FileHelper {

    public static void makeFileAndShow(Context context, MediaMessage mediaMessage, String Uid, View view, String key, AdCircleProgress adCircleProgress) {
        if (mediaMessage.getUserId().equals(Uid) && mediaMessage.getAttachment().getFileUrl() == null) {
            adCircleProgress.setMax(100);
            final String fullName = generateRandomName();
            String uid = Uid;
            StorageReference filesRef = storage().getReference().child("chat").child(mediaMessage.getReceiverType()).child(uid);
            StorageReference fileRef = filesRef.child(fullName);
            final FileUploadResult result = new FileUploadResult();
            UploadTask uploadTask = null;
            try {
                uploadTask = fileRef.putBytes(ParseFileUtils.readFileToByteArray(new File(mediaMessage.getAttachment().getFilePath())));
            } catch (IOException e) {
                e.printStackTrace();
            }
            uploadTask.addOnProgressListener(taskSnapshot -> {
                adCircleProgress.setVisibility(View.VISIBLE);
                result.progress.set(taskSnapshot.getTotalByteCount(), taskSnapshot.getBytesTransferred());
                result.progress1 = (int) ((int) (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                adCircleProgress.setAdProgress((int) ((int) (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount()));
                if ((int) ((int) (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount()) == 100) {
                    adCircleProgress.setVisibility(View.GONE);
                }
            }).addOnSuccessListener(taskSnapshot -> {
                fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    result.progress.set(taskSnapshot.getTotalByteCount(), taskSnapshot.getTotalByteCount());
                    new Chat().updateFile(mediaMessage.getGroupId(),key, mediaMessage.getUserId(), mediaMessage.getReceiverId(), uri.toString());
                });
            });
            if (view != null) {
                if (mediaMessage.getType().equals(FastChatConstants.MESSAGE_TYPE_IMAGE)) {
                    if (view instanceof ImageView) {
                        ImageView imageView = (ImageView) view;
                        RequestOptions options = new RequestOptions()
                                .centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                //   .skipMemoryCache(true)
                                .priority(Priority.HIGH);
                        Glide.with(context)
                                .load(mediaMessage.getAttachment().getFilePath())
                                //  .placeholder(R.drawable.ic_defaulf_image)
                                //  .error(R.drawable.ic_defaulf_image)
                                .apply(options)
                                .into(imageView);
                    }

                }
                if (mediaMessage.getType().equals(FastChatConstants.MESSAGE_TYPE_VIDEO)) {
                    if (view instanceof ExoVideoView) {
                        ExoVideoView videoView = (ExoVideoView) view;
                        videoView.setUrl(mediaMessage.getAttachment().getFilePath());

                    }

                }
                if (mediaMessage.getType().equals(FastChatConstants.MESSAGE_TYPE_FILE)) {
                    if (view instanceof ImageView) {
                        ImageView imageView = (ImageView) view;
                        imageView.setOnClickListener(view1 -> MediaUtils.openFile(mediaMessage.getAttachment().getFilePath(), mediaMessage.getAttachment().getFileMimeType(), context));

                    }

                }

            }


        } else {
            if (view != null) {
                if (mediaMessage.getType().equals(FastChatConstants.MESSAGE_TYPE_IMAGE)) {
                    if (view instanceof ImageView) {
                        ImageView imageView = (ImageView) view;
                        if (mediaMessage.getAttachment().getFileUrl() != null) {
                            RequestOptions options = new RequestOptions()
                                    .centerCrop()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .priority(Priority.HIGH);
                            Glide.with(context)
                                    .load(mediaMessage.getAttachment().getFileUrl())
                                    // .placeholder(R.drawable.ic_defaulf_image)
                                    //  .error(R.drawable.ic_defaulf_image)
                                    .apply(options)
                                    .into(imageView);
                        }


                    }

                }
                if (mediaMessage.getType().equals(FastChatConstants.MESSAGE_TYPE_VIDEO)) {
                    if (view instanceof ExoVideoView) {
                        ExoVideoView videoView = (ExoVideoView) view;
                        if (mediaMessage.getAttachment().getFileUrl() != null) {
                            videoView.setUrl(mediaMessage.getAttachment().getFileUrl());
                        }

                    }

                }

                if (mediaMessage.getType().equals(FastChatConstants.MESSAGE_TYPE_FILE)) {
                    if (view instanceof ImageView) {
                        ImageView imageView = (ImageView) view;
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setOnClickListener(view1 -> MediaUtils.openFile(mediaMessage.getAttachment().getFilePath(), mediaMessage.getAttachment().getFileMimeType(), context));

                    }

                }
            }

        }


    }


}