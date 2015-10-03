package com.onboardinganimation.onboardlib;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onboardinganimation.R;

public class TutorialActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private LinearLayout mIndicatorHolder;
    private int mPageCount;
    public static boolean isActivityCreated;
    private int mLastPosition;
    private boolean mPageChanged;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isActivityCreated = true;
    }

    public void setUpOnBoardAnimation(ViewPager viewPager, LinearLayout indicatorHolder){
        mViewPager = viewPager;
        checkError();
        mPagerAdapter = (FragmentPagerAdapter) mViewPager.getAdapter();
        checkFragmentError();
        ((TutorialFragment)mPagerAdapter.getItem(0)).setIsFirstFragment(true);
        mIndicatorHolder = indicatorHolder;
        mPageCount = mPagerAdapter.getCount();
        mViewPager.setOnPageChangeListener(this);
        if(mIndicatorHolder != null) {
            makeIndicator();
        }
    }

    private void checkError(){
        if(mViewPager == null){
            throw new Error("View pager not found. Call setUpAutoOnBoardAnimation after getting the view pager reference");
        }
        if(mViewPager.getAdapter() == null){
            throw new Error("View pager adapter not found. Call setUpAutoOnBoardAnimation after setting adapter to view pager");
        }
    }

    private void checkFragmentError(){
        if(!(mPagerAdapter instanceof FragmentPagerAdapter)){
            throw new Error("Adapter should be an instance of FragmentPagerAdapter");
        }
        for(int i=0; i<mViewPager.getAdapter().getCount(); i++) {
            if (!(mPagerAdapter.getItem(i) instanceof TutorialViewAnimator)){
                throw new Error("Fragment at position "+ (i+1) +" should be an instance of CustomViewAnimator");
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.d("TAG", position+" "+positionOffset+" "+mLastPosition);
        updateColor(position, positionOffset);
        ((TutorialViewAnimator) mPagerAdapter.getItem(position)).animate(1 - positionOffset);
        ((TutorialViewAnimator) mPagerAdapter.getItem(position + 1)).animate(positionOffset);
    }


    private void updateColor(int position, float positionOffset){
        if(positionOffset>0.9) {
            position+=1;
        }
        mIndicatorHolder.findViewWithTag(position).setBackgroundColor(getResources().getColor(R.color.brand_color));
        for(int i=0; i<mPageCount; i++){
            if(i!=position) {
                mIndicatorHolder.findViewWithTag(i).setBackgroundColor(Color.GRAY);
            }
        }
    }

    private void makeIndicator() {
        for (int i = 0; i < mPageCount; i++) {
            TextView tv = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(15, 5, 15, 5);
            tv.setLayoutParams(params);
            tv.setTag(i);
            tv.setWidth(25);
            tv.setHeight(25);
            mIndicatorHolder.addView(tv);
        }
    }

    @Override
    public void onPageSelected(int position) {
        mLastPosition = position;
        mPageChanged = true;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
