package com.fastchat.widget.typing;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
 import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.FloatRange;

public class DisappearDotView extends DotView {
    private static final String TAG = DisappearDotView.class.getSimpleName();
    private Paint paint = new Paint();
    private int centerX;
    private int centerY;
    private float radius;
    private boolean isAnimatingDisappear = false;

    private AnimatorSet animatorDisappearSet;
    private AnimatorSet animatorAppearSet;
    private float scale = 1F;

    public DisappearDotView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(dotColor);
        canvas.drawCircle(centerX, centerY, radius * scale, paint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        radius = (Math.min(getWidth(), getHeight()) / 2) / 1.2F;    // Save space for overshoot interpolator.
    }

    @Override
    protected void init() {
        paint = new Paint();
    }

    @Override
    public void startDotAnimation() {
        stopDotAnimation();
        if (isAnimatingDisappear) {
            isAnimatingDisappear = false;
            if (animatorAppearSet == null) {
                animatorAppearSet = new AnimatorSet();

                ValueAnimator appearAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), dotSecondColor, dotFirstColor);
                appearAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        dotColor = (int) animator.getAnimatedValue();
                        invalidate();
                    }
                });
                appearAnimator.setDuration(animationTotalDuration);
                appearAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

                ValueAnimator growAnimator = ValueAnimator.ofFloat(0F, 1F);
                growAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        scale = (float) animator.getAnimatedValue();
                    }
                });
                growAnimator.setDuration(animationTotalDuration);
                growAnimator.setInterpolator(new OvershootInterpolator());
                animatorAppearSet.playTogether(appearAnimator, growAnimator);
            }
            animatorAppearSet.start();
        } else {
            isAnimatingDisappear = true;
            if (animatorDisappearSet == null) {
                animatorDisappearSet = new AnimatorSet();
                ValueAnimator disappearAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), dotFirstColor, dotSecondColor);
                disappearAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        dotColor = (int) animator.getAnimatedValue();
                        invalidate();
                    }
                });


                disappearAnimator.setDuration(animationTotalDuration);
                disappearAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

                ValueAnimator shrinkAnimator = ValueAnimator.ofFloat(1F, 0F);
                shrinkAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        scale = (float) animator.getAnimatedValue();
                    }
                });
                shrinkAnimator.setDuration(animationTotalDuration);
                shrinkAnimator.setInterpolator(new AccelerateInterpolator());

                animatorDisappearSet.playTogether(disappearAnimator, shrinkAnimator);
            }
            animatorDisappearSet.start();
        }
    }

    @Override
    public void stopDotAnimation() {
        if (animatorDisappearSet != null) {
            animatorDisappearSet.isStarted();
            animatorDisappearSet.cancel();
        }
        if (animatorAppearSet != null) {
            animatorAppearSet.isStarted();
            animatorAppearSet.cancel();
        }
    }

    @Override
    public boolean isAnimating() {
        if (animatorDisappearSet != null && animatorDisappearSet.isStarted()) {
            return true;
        }

        if (animatorAppearSet != null && animatorAppearSet.isStarted()) {
            return true;
        }

        return false;
    }

    @Override
    protected void setMaxCompressRatio(@FloatRange(from = 0.0, to = 1.0) float compressRatio) {
        // not needed
    }
}
