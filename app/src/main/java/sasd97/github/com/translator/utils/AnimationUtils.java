package sasd97.github.com.translator.utils;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

/**
 * Created by alexander on 17/04/2017.
 */

public class AnimationUtils {

    private AnimationUtils() {}

    private static AnimationSet obtainFadeAnimation(final View v,
                                                    float alphaStart, float alphaFinish,
                                                    float startY, float finishY,
                                                    long duration, final int finalVisibility) {
        AnimationSet set = new AnimationSet(true);
        set.setInterpolator(new AccelerateDecelerateInterpolator());

        Animation alpha = new AlphaAnimation(alphaStart, alphaFinish);
        alpha.setDuration(duration);

        Animation translate = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, startY, Animation.RELATIVE_TO_SELF, finishY
        );
        translate.setDuration(duration);

        set.addAnimation(alpha);
        set.addAnimation(translate);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(finalVisibility);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        v.startAnimation(set);
        return set;
    }

    public static AnimationSet fadeOut(final View view) {
        return obtainFadeAnimation(view, 1.0f, 0.0f, 0.0f, 0.0f, 500L, View.INVISIBLE);
    }

    public static AnimationSet fadeIn(final View view) {
        return obtainFadeAnimation(view, 0.0f, 1.0f, 1.0f, 0.0f, 500L, View.VISIBLE);
    }
}
