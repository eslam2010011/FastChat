package com.fastchat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.core.graphics.drawable.DrawableCompat;

import com.fastchat.R;


public class BadgeCount extends LinearLayout {

    private TextView tvCount;   //Used to display count

    private int count;      //Used to store value of count

    private float countSize;    //Used to store size of count

    private int countColor;     //Used to store color of count

    private int countBackgroundColor;      //Used to store background color of count.

    public BadgeCount(Context context) {
        super(context);
        initViewComponent(context, null, -1);
    }

    public BadgeCount(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewComponent(context, attrs, -1);
    }

    public BadgeCount(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViewComponent(context, attrs, defStyleAttr);
    }

    /**
     * This method is used to initialize the view present in component and set the value if it is
     * available.
     *
     * @param context
     * @param attributeSet
     * @param defStyleAttr
     */
    private void initViewComponent(Context context, AttributeSet attributeSet, int defStyleAttr) {

        View view = View.inflate(context, R.layout.cc_badge_count, null);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.BadgeCount,
                0, 0);
        count = a.getInt(R.styleable.BadgeCount_count, 0);
        countSize = a.getDimension(R.styleable.BadgeCount_count_size, 12);
        countColor = a.getColor(R.styleable.BadgeCount_count_color, Color.WHITE);
        countBackgroundColor=a.getColor(R.styleable.BadgeCount_count_background_color,getResources().getColor(R.color.colorPrimary));


        addView(view);

          if (count==0){
              setVisibility(INVISIBLE);
          }else {
              setVisibility(VISIBLE);
          }

        tvCount = view.findViewById(R.id.tvSetCount);
        tvCount.setBackground(getResources().getDrawable(R.drawable.count_background));
        tvCount.setTextSize(countSize);
        tvCount.setTextColor(countColor);
        tvCount.setText(String.valueOf(count));
        setCountBackground(countBackgroundColor);

    }

    /**
     * This method is used to set background color of count.
     * @param color is an object of Color.class . It is used as color for background.
     */
    public void setCountBackground(@ColorInt int color) {
        Drawable unwrappedDrawable = tvCount.getBackground();
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable,color);
    }

    /**
     * This method is used to set color of count i.e integer.
     * @param color is an object of Color.class. It is used as color of text in tvCount (TextView)
     */
    public void setCountColor(@ColorInt int color) {
        tvCount.setTextColor(color);
    }

    /**
     * This method is used to set size of text present in tvCount(TextView) i.e count size;
     * @param size
     */
    public void setCountSize(float size) {
        tvCount.setTextSize(size);

    }

    /**
     * This method is used to set count in tvCount(TextView). If count is 0 then tvCount is invisible
     * If count is more than 1, then it will display the value. The limit of the count shown will be 999.
     *
     * @param count is an Integer which is set in tvCount(TextView).
     */
    public void setCount(int count) {
        this.count = count;
        if (count == 0)
            setVisibility(GONE);
        else
            setVisibility(View.VISIBLE);
        if (count<999)
            tvCount.setText(String.valueOf(count));
        else
            tvCount.setText("999+");
    }


}
