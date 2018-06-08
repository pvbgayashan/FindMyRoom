package com.fmr.findmyroom.user_management;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;

import com.fmr.findmyroom.R;

public class UserConnectionActivity extends AppCompatActivity {

    private SectionsPageAdapter mSectionsPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_connection);

        // set the toolbar
        Toolbar userConnToolbar = findViewById(R.id.userConnectionToolbar);
        userConnToolbar.setTitle("User Management");
        setSupportActionBar(userConnToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        ViewPager mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void setupViewPager(ViewPager viewPager) {
        mSectionsPageAdapter.addFragment(new LoginFragment(), "LOGIN");
        mSectionsPageAdapter.addFragment(new SignUpFragment(), "SIGN UP");

        viewPager.setAdapter(mSectionsPageAdapter);
    }
}
