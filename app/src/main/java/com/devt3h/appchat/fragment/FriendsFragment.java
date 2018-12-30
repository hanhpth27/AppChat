package com.devt3h.appchat.fragment;

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
import com.devt3h.appchat.adapter.FriendAdapter;
import com.devt3h.appchat.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {
    private RecyclerView recyclerView;
    private FriendAdapter friendAdapter;
    private List<User> friends;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends,container,false);

        inits(view);
        loadFriends();
        return view;
    }

    private void inits(View view) {
        recyclerView = view.findViewById(R.id.recycle_view_friend);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        friends = new ArrayList<>();

    }
    private void loadFriends(){
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                friends.clear();

                for(DataSnapshot data: dataSnapshot.getChildren()){
                    User friend = data.getValue(User.class);
                    if(!currentUser.getUid().equals(friend.getId()) && friend != null)
                        friends.add(friend);
                }

                friendAdapter = new FriendAdapter(getContext(),friends);
                recyclerView.setAdapter(friendAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
