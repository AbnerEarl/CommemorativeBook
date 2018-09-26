package com.example.frank.commemorativebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


import com.example.frank.commemorativebook.view.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xxx on 2016/6/29.
 */
public class MyPhotoActivity extends AppCompatActivity {

    private List<String> mTitles = Arrays.asList("选择照片");
    private List<MyPhotoFragment> mFragments = new ArrayList<>();

    private ViewPagerIndicator mIndicator;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_photo);

        mIndicator = (ViewPagerIndicator) findViewById(R.id.viewPagerIndicator);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        mIndicator.setShowTabs(1);
        mIndicator.setTitles(mTitles);

        for (int i = 0; i < mTitles.size(); i++) {
            mFragments.add(MyPhotoFragment.newInstance(new ArrayList<String>()));
        }
        mViewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        mIndicator.setViewPager(mViewPager, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFragments.get(mIndicator.getCurrPosition()).onActivityResult(requestCode,resultCode,data);
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mTitles.size();
        }
    }
}
