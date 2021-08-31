package com.fastchat.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Patterns;
import android.widget.TextView;

import com.fastchat.R;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PatternUtils {
    public static void setHyperLinkSupport(Context context, TextView txtMessage, int Color) {
        new PatternBuilder().
                addPattern(Pattern.compile("(^|[\\s.:;?\\-\\]<\\(])" +
                                "((https?://|www\\.|pic\\.)[-\\w;/?:@&=+$\\|\\_.!~*\\|'()\\[\\]%#,☺]+[\\w/#](\\(\\))?)" +
                                "(?=$|[\\s',\\|\\(\\).:;?\\-\\[\\]>\\)])"),
                        context.getResources().getColor(Color),
                        new PatternBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                if (!text.trim().contains("http")) {
                                    text = "http://" + text;
                                }
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(text.trim()));
                                context.startActivity(Intent.createChooser(intent, "Url"));
                            }
                        }).into(txtMessage);
        new PatternBuilder().
                addPattern(Patterns.PHONE, context.getResources().getColor(Color),
                        new PatternBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(text));
                                intent.setData(Uri.parse("tel:" + text));
                                context.startActivity(Intent.createChooser(intent, "Dial"));
                            }
                        }).into(txtMessage);
        new PatternBuilder().
                addPattern(Pattern.compile("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}"),
                        context.getResources().getColor(Color),
                        new PatternBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + text));
                                intent.putExtra(Intent.EXTRA_EMAIL, text);
                                context.startActivity(Intent.createChooser(intent, "Mail"));
                            }
                        }).into(txtMessage);
    }


}