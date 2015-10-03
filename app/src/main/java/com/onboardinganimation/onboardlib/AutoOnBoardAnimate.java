package com.onboardinganimation.onboardlib;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.util.ArrayList;

/**
 * Created by Nilesh Kumar Tiwari on 7/13/2015.
 */
public class AutoOnBoardAnimate {

    private static volatile int startX, startY, endX, endY;
    private static volatile float stepX, stepY;

    public enum Position {START, END}

    ;

    /**
     * calculates the x value
     *
     * @param positionVal
     * @param startX
     * @param endX
     * @param positionOffset
     * @return
     */
    public static float calculateStepX(Position positionVal, float startX, float endX, float positionOffset) {

        float stepX = 0;

        switch (positionVal) {
            case START:

                if (endX == startX) {
                    //keep at same level
                    stepX = endX;
                } else if (startX <= endX) {
                    //move right
                    stepX = endX * positionOffset;
                } else {
                    //move left
                    stepX = startX - ((startX - endX) * positionOffset);
                }

                break;
            case END:

                if (endX == startX) {
                    //keep at same level
                    stepX = endX;
                } else if (endX < startX) {
                    //move right
                    stepX = endX + ((startX - endX) * positionOffset);
                } else {
                    //move left
                    stepX = endX - ((endX - startX) * positionOffset);
                }

                break;
        }
        return stepX;
    }

    /**
     * calculates the y value
     *
     * @param positionVal
     * @param startY
     * @param endY
     * @param positionOffset
     * @return
     */
    public static float calculateStepY(Position positionVal, float startY, float endY, float positionOffset) {
        float stepY = 0;

        switch (positionVal) {
            case START:

                if (startY == endY) {
                    //keep at same level
                    stepY = endY;
                } else if (startY < endY) {
                    //move down
                    stepY = endY * positionOffset;
                } else {
                    //move up
                    stepY = startY - ((startY - endY) * positionOffset);
                }
                break;
            case END:

                if (startY == endY) {
                    //keep at same level
                    stepY = endY;
                } else if (startY < endY) {
                    //move down
                    stepY = endY + ((startY - endY) * positionOffset);
                } else {
                    //move up
                    stepY = endY - ((endY - startY) * positionOffset);
                }

                break;
        }
        return stepY;
    }

    /**
     * animates the movable views
     *
     * @param positionVal
     * @param movable
     * @param positionOffset - should be 0 to 1 for translating from start to end and -1 to 0 from end to start
     * @return
     */
    public static Position animate(Position positionVal, ArrayList<MovableView> movable,
                                   float positionOffset) {
        if (movable != null) {

            for (int i = 0; i < movable.size(); i++) {

                startX = (int) movable.get(i).getStartX();
                startY = (int) movable.get(i).getStartY();

                endX = (int) movable.get(i).getEndX();
                endY = (int) movable.get(i).getEndY();

                switch (positionVal) {
                    case START:
                        stepX = AutoOnBoardAnimate.calculateStepX(positionVal, startX, endX, positionOffset);
                        stepY = AutoOnBoardAnimate.calculateStepY(positionVal, startY, endY, positionOffset);
                        movable.get(i).setX(stepX);
                        movable.get(i).setY(stepY);
                        movable.get(i).setAlpha(positionOffset);
                        if (movable.get(i).getX() == endX) {
                            positionVal = AutoOnBoardAnimate.Position.END;
                        }
                        break;
                    case END:
                        stepX = AutoOnBoardAnimate.calculateStepX(positionVal, startX, endX, Math.abs(positionOffset - 1));
                        stepY = AutoOnBoardAnimate.calculateStepY(positionVal, startY, endY, Math.abs(positionOffset - 1));
                        movable.get(i).setX(stepX);
                        movable.get(i).setY(stepY);
                        movable.get(i).setAlpha(positionOffset);
                        if (movable.get(i).getX() == startX) {
                            positionVal = AutoOnBoardAnimate.Position.START;
                        }
                        break;
                }

                movable.get(i).invalidate();
            }

        }
        return positionVal;
    }

    /**
     * finds and create an array of movable views.
     *
     * @param v
     * @return
     */
    public static ArrayList<MovableView> init(View v) {

        ArrayList<MovableView> movable = new ArrayList<>();
        View child = null;
        movable.clear();
        ViewGroup rootView = (ViewGroup) v.getRootView();
        for (int i = 0; i < rootView.getChildCount(); i++) {
            child = rootView.getChildAt(i);
            if (child instanceof MovableView) {
                movable.add((MovableView) child);
            }
        }
        return movable;
    }

    /**
     * animates movable view of first fragment from its start to end position
     * @param movable
     * @param v
     */
    public static void initialAnimation(final ArrayList<MovableView> movable, View v) {

        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (TutorialActivity.isActivityCreated) {

                    for (int i = 0; i < movable.size(); i++) {
                        movable.get(i).setUpXAndY();
                        movable.get(i).setVisibility(View.INVISIBLE);
                        int endX = (int) movable.get(i).getEndX();
                        int endY = (int) movable.get(i).getEndY();
                        int startX = (int) movable.get(i).getStartX();
                        int startY = (int) movable.get(i).getStartY();
                        movable.get(i).setX(startX);
                        movable.get(i).setY(startY);

                        movable.get(i).setAlpha(0);
                        movable.get(i).setVisibility(View.VISIBLE);
                        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(movable.get(i), "x", endX).setDuration(1000);
                        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(movable.get(i), "y", endY).setDuration(1000);
                        ObjectAnimator objectAnimatorAplha = ObjectAnimator.ofFloat(movable.get(i), "alpha", 1).setDuration(1000);
                        objectAnimatorX.start();
                        objectAnimatorY.start();
                        objectAnimatorAplha.start();
                    }

                    TutorialActivity.isActivityCreated = false;
                }
            }
        });
    }
}
