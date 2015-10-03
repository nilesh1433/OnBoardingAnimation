package com.onboardinganimation.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import com.onboardinganimation.R;
import com.onboardinganimation.onboardlib.TutorialActivity;


/**
 * The launchpad activity for this sample project. This activity launches other activities that
 * demonstrate implementations of common animations.
 */
public class MainActivity extends TutorialActivity {

    ViewPager pager;
    private MyPagerAdapter pagerAdapter;
    int previousPosition;
    public static boolean isActivityCreated;
    private LinearLayout linearLayout;
    private static final int PAGE_COUNT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager = (ViewPager) findViewById(R.id.viewPager);
        linearLayout = (LinearLayout) findViewById(R.id.indicatorHolder);
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        super.setUpOnBoardAnimation(pager, linearLayout);

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {
                case 0:
                    return TestFragment.newInstance();
                case 1:
                    return TestFragmentTwo.newInstance();
                default:
                    return TestFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

    }

}
