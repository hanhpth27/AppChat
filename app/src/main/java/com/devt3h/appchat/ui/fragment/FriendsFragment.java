package com.devt3h.appchat.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devt3h.appchat.R;
import com.devt3h.appchat.adapter.UserAdapter;
import com.devt3h.appchat.helper.Constants;
import com.devt3h.appchat.model.Friend;
import com.devt3h.appchat.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {
    private RecyclerView rvUsers;
    private UserAdapter adapter;
    private List<User> friendList;
    private List<String> listId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_users, container, false);
        rvUsers = v.findViewById(R.id.rv_users);
        rvUsers.setLayoutManager(new LinearLayoutManager(container.getContext()));
        friendList = new ArrayList<>();
        listId = new ArrayList<>();

        readUser();
        return v;
    }

    private void readUser() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // read data form table Friend
        databaseReference.child(Constants.ARG_FRIENDS).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Friend friend = dataSnapshot.getValue(Friend.class);
                String userId = user.getUid();
                if(user!=null){
                    if(friend.getSender_id().equals(user.getUid()) && friend.getStatus().equals(Constants.KEY_ACCEPTED)){
                        listId.add(friend.getReceiver_id());

                    }else if(friend.getReceiver_id().equals(user.getUid()) && friend.getStatus().equals(Constants.KEY_ACCEPTED)){
                        listId.add(friend.getSender_id());
                    }
                }
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

        // read data from table Users with userId of table Friend
        databaseReference.child(Constants.ARG_USERS).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);

                if(listId!=null){
                    for(int i=0; i<listId.size(); i++){
                        if(user.getId().equals(listId.get(i))){
                            friendList.add(user);
                            listId.remove(i);
                        }
                    }
                }

                adapter = new UserAdapter(new UserAdapter.IUser() {
                    @Override
                    public int getCount() {
                        if(friendList==null) return 0;
                        return friendList.size();
                    }

                    @Override
                    public User getUser(int position) {
                        return friendList.get(position);
                    }
                }, true);

                rvUsers.setAdapter(adapter);
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
