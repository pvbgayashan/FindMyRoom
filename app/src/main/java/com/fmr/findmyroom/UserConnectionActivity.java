package com.fmr.findmyroom;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;

public class UserConnectionActivity extends AppCompatActivity {

    private SectionsPageAdapter mSectionsPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_connection);

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        ViewPager mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    public void setupViewPager(ViewPager viewPager) {
        mSectionsPageAdapter.addFragment(new LoginFragment(), "LOGIN");
        mSectionsPageAdapter.addFragment(new SignUpFragment(), "SIGN UP");

        viewPager.setAdapter(mSectionsPageAdapter);
    }
}
