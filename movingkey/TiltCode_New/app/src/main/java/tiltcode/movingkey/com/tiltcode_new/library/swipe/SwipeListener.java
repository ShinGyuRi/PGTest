package tiltcode.movingkey.com.tiltcode_new.library.swipe;

import android.view.MotionEvent;

/**
 * Created by Gyul on 2016-06-28.
 */
public interface SwipeListener {
    void onSwipingLeft(final MotionEvent event);

    void onSwipedLeft(final MotionEvent event);

    void onSwipingRight(final MotionEvent event);

    void onSwipedRight(final MotionEvent event);

    void onSwipingUp(final MotionEvent event);

    void onSwipedUp(final MotionEvent event);

    void onSwipingDown(final MotionEvent event);

    void onSwipedDown(final MotionEvent event);
}
