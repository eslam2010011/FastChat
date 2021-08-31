package com.fastchat.widget;

import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.fastchat.Model.BaseMessage;

public abstract class ComposeActionListener {

    public void onMoreActionClicked(ImageView moreIcon) {}

    public void onCameraActionClicked(ImageView cameraIcon) {}

    public void onGalleryActionClicked(ImageView galleryIcon) {}

    public void onAudioActionClicked(ImageView audioIcon) {}

    public void onFileActionClicked(ImageView fileIcon) {}

    public void onEmojiActionClicked(ImageView emojiIcon) {}

    public void onSendActionClicked(EditText editText ) {}

    public void onVoiceNoteComplete(String string) {}

    public void onReplyComplete(BaseMessage baseMessage,EditText editText ,boolean Reply) {}

    public void onLinkComplete(BaseMessage baseMessage,EditText editText ,boolean Reply) {}

    public abstract void  beforeTextChanged(CharSequence charSequence, int i, int i1, int i2);

    public abstract void onTextChanged(CharSequence charSequence, int i, int i1, int i2);

    public abstract void afterTextChanged(Editable editable);

    public abstract void IsTyping(boolean IsTyping);


    public void getCameraActionView(ImageView cameraIcon) {
        cameraIcon.setVisibility(View.VISIBLE);
    }

    public void getGalleryActionView(ImageView galleryIcon) {
        galleryIcon.setVisibility(View.VISIBLE);
    }

    public void getFileActionView(ImageView fileIcon) {
        fileIcon.setVisibility(View.VISIBLE);
    }

}
