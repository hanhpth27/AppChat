package com.devt3h.appchat.ui.activity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.devt3h.appchat.R;
import com.devt3h.appchat.adapter.UserAdapter;
import com.devt3h.appchat.helper.Constants;
import com.devt3h.appchat.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FindFriendActivity extends AppCompatActivity {
    private UserAdapter adapter;
    private RecyclerView rvUser;
    private List<User> userList;
    private Toolbar toolbar;
    private EditText edtKey;
    private ImageView imgSearch;

    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);

        inits();
        imgSearch.setOnClickListener(new View.OnClickListener() {
            String key = edtKey.getText().toString();
            @Override
            public void onClick(View view) {
                searchFriend(key);
            }
        });
    }

    private void inits() {
        edtKey = findViewById(R.id.edt_key);
        imgSearch = findViewById(R.id.btn_search);

        rvUser = findViewById(R.id.rv_find_user);
        rvUser.setLayoutManager(new LinearLayoutManager(this));

        toolbar = findViewById(R.id.toolbar_friend);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.find_friend_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.ARG_USERS);
        userList = new ArrayList<>();
    }

    private void searchFriend(String key){
        Query query = mDatabase.orderByChild(Constants.KEY_USER_NAME)
                .startAt(key).endAt(key + "\uf8ff");

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                userList.clear();
                if (adapter!=null) adapter.notifyDataSetChanged();
                User user = dataSnapshot.getValue(User.class);
                if(user!=null) userList.add(user);

                adapter = new UserAdapter(new UserAdapter.IUser() {
                    @Override
                    public int getCount() {
                        if (userList== null) return 0;
                        return userList.size();
                    }

                    @Override
                    public User getUser(int position) {
                        return userList.get(position);
                    }
                });

                rvUser.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
