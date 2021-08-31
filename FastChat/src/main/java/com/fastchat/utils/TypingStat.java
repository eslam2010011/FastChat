package com.fastchat.utils;

import android.content.Context;

import com.fastchat.R;


//indicates typing state if user is typing or recording or doing nothing
public class TypingStat {
    public static final int NOT_TYPING = 0;
    public static final int TYPING = 1;
    public static final int RECORDING = 2;

    public static String getStatString(Context context, int stat) {
        switch (stat) {
            case TypingStat.NOT_TYPING:
                return "";
            case TypingStat.TYPING:
                return context.getResources().getString(R.string.typing);
            case TypingStat.RECORDING:
                return context.getResources().getString(R.string.recording);

            default:
                return "";
        }
    }
}
