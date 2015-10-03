package com.onboardinganimation.sample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onboardinganimation.R;
import com.onboardinganimation.onboardlib.TutorialFragment;


/**
 * Created by Nilesh Kumar Tiwari on 7/31/2015.
 */
public class TestFragment extends TutorialFragment {
    private static TestFragment testFragmentInstance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.test_fragment, container, false);
        super.initOnBoardFragment(v);
        return v;
    }

    public static TestFragment newInstance() {

        if(testFragmentInstance ==null) {
            testFragmentInstance = new TestFragment();
        }

        return testFragmentInstance;
    }

}
