package com.onboardinganimation.onboardlib;

import android.support.v4.app.Fragment;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Nilesh Kumar Tiwari on 8/19/2015.
 */
public class TutorialFragment extends Fragment implements TutorialViewAnimator {

    private ArrayList<MovableView> movable;
    private AutoOnBoardAnimate.Position positionVal;
    public  boolean isFirstFragment;

    @Override
    public void animate(float positionOffset) {
        AutoOnBoardAnimate.animate(positionVal, movable, positionOffset);
    }

    @Override
    public void addToInitialAnimation() {
    }

    protected void initOnBoardFragment(View v){
        movable = AutoOnBoardAnimate.init(v);
        positionVal = AutoOnBoardAnimate.Position.START;
        if(isFirstFragment){
            autoAnimate(v);
        }
    }

    protected void autoAnimate(View v){
        AutoOnBoardAnimate.initialAnimation(movable, v);
        addToInitialAnimation();
    }

    public void setIsFirstFragment(boolean isFirstFragment) {
        this.isFirstFragment = isFirstFragment;
    }
}
