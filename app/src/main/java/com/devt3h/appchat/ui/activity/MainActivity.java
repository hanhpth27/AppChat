package com.devt3h.appchat.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.devt3h.appchat.R;
import com.devt3h.appchat.adapter.ViewPagerAdapter;
import com.devt3h.appchat.ui.fragment.AccountFragment;
import com.devt3h.appchat.ui.fragment.AddFriendRequestFragment;
import com.devt3h.appchat.ui.fragment.ChatsFragment;
import com.devt3h.appchat.ui.fragment.FriendsFragment;
import com.devt3h.appchat.ui.fragment.NewsFeedFragment;
import com.devt3h.appchat.ui.fragment.NotificationFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    public static final int OPEN_CAMERA = 1;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private EditText edtSearch;
    private ImageView imgCamera, imgLogout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        inits();
        imgLogout.setOnClickListener(view -> {
            logOutUser();
        });

        imgCamera.setOnClickListener(view -> {
            Intent iCamera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(iCamera, OPEN_CAMERA);
        });

        //showActivitySearch();
    }

    private void showActivitySearch() {
        edtSearch.setOnKeyListener((v, keyCode, event) -> {
            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                Intent searchIntent = new Intent(MainActivity.this, FindFriendActivity.class);
                startActivity(searchIntent);
                return true;
            }
            return false;
        });
    }

    private void inits() {
//        toolbar = findViewById(R.id.main_app_bar);
//        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        edtSearch = findViewById(R.id.btn_search);
        imgCamera = findViewById(R.id.img_camera);
        imgLogout = findViewById(R.id.img_logout);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new NewsFeedFragment());
        viewPagerAdapter.addFragment(new AddFriendRequestFragment());
        viewPagerAdapter.addFragment(new ChatsFragment());
        viewPagerAdapter.addFragment(new NotificationFragment());
        viewPagerAdapter.addFragment(new AccountFragment());
//        viewPagerAdapter.addFragment(new FriendsFragment());

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        initTabLayout();
    }

    private void initTabLayout(){
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_newsfeed_selected);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_friend);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_mess);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_notification);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_account);

        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#ffffff"));

        tabLayout.getTabAt(0).select();
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position){
                    case 0:
                        tab.setIcon(R.drawable.ic_newsfeed_selected);
                        break;
                    case 1:
                        tab.setIcon(R.drawable.ic_friend_selected);
                        break;
                    case 2:
                        tab.setIcon(R.drawable.ic_mess_selected);
                        break;
                    case 3:
                        tab.setIcon(R.drawable.ic_notification_selected);
                        break;
                    case 4:
                        tab.setIcon(R.drawable.ic_account_selected);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position){
                    case 0:
                        tab.setIcon(R.drawable.ic_newsfeed);
                        break;
                    case 1:
                        tab.setIcon(R.drawable.ic_friend);
                        break;
                    case 2:
                        tab.setIcon(R.drawable.ic_mess);
                        break;
                    case 3:
                        tab.setIcon(R.drawable.ic_notification);
                        break;
                    case 4:
                        tab.setIcon(R.drawable.ic_account);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.btn_log_out:
                mAuth.signOut();
                logOutUser();
                break;
            default:
                break;
        }
        return true;
    }

    private void logOutUser() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
