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
import com.devt3h.appchat.model.Friend;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AddFriendRequestFragment extends Fragment {
    private UserAdapter userAdapter;
    private List<Friend> requestFriendList;
    private List<String> listId;
    private RecyclerView rvRequestFriend;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_request_friend, container, false);

        rvRequestFriend = v.findViewById(R.id.rv_request_friend);
        rvRequestFriend.setLayoutManager(new LinearLayoutManager(container.getContext()));

        requestFriendList = new ArrayList<>();
        listId = new ArrayList<>();

        readRequestFriend();
        return v;
    }

    private void readRequestFriend() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    }
}
