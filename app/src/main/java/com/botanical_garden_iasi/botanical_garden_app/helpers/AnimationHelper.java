package com.botanical_garden_iasi.botanical_garden_app.helpers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

public class AnimationHelper {
    public static void setVisibilityAnimated(View view, int visibility, int duration) {
        if (view.getAnimation() == null) {
            if (visibility == View.GONE) {
                if (view.getVisibility() != View.GONE) {
                    view.animate()
                            .scaleX(0.1f)
                            .scaleY(0.1f)
                            .alpha(0.0f)
                            .setDuration(duration)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    view.clearAnimation();
                                    view.setVisibility(View.GONE);
                                }
                            });
                }
            } else if (visibility == View.VISIBLE) {
                if (view.getVisibility() != View.VISIBLE) {
                    view.setVisibility(View.VISIBLE);
                    view.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .alpha(1.0f)
                            .setDuration(duration)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    view.clearAnimation();
                                    view.setVisibility(View.VISIBLE);
                                }
                            });
                }

            } else {
                if (view.getVisibility() != View.INVISIBLE) {
                    view.animate()
                            .alpha(0.0f)
                            .setDuration(duration)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    view.clearAnimation();
                                    view.setVisibility(View.INVISIBLE);
                                }
                            });
                }
            }
        }

    }
}
