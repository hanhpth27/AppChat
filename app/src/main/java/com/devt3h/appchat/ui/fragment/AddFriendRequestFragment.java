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
import com.devt3h.appchat.adapter.RequestFriendAdapter;
import com.devt3h.appchat.adapter.UserAdapter;
import com.devt3h.appchat.helper.Constants;
import com.devt3h.appchat.helper.Helper;
import com.devt3h.appchat.model.Friend;
import com.devt3h.appchat.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddFriendRequestFragment extends Fragment {
    private RequestFriendAdapter userAdapter;
    private List<User> requestFriendList;
    private List<String> listId;
    private RecyclerView rvRequestFriend;
    private DatabaseReference databaseReference;
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
        databaseReference = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String userId = firebaseUser.getUid();

        databaseReference.child(Constants.ARG_FRIENDS).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Friend friend = dataSnapshot.getValue(Friend.class);
                if(friend!=null && friend.getStatus().equals(Constants.KEY_PENDING)){
                    if(friend.getReceiver_id().equals(userId)){
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

        databaseReference.child(Constants.ARG_USERS).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);

                if(listId!=null){
                    for(int i=0; i<listId.size(); i++){
                        if(user.getId().equals(listId.get(i))){
                            requestFriendList.add(user);
                            listId.remove(i);
                        }
                    }
                }

                userAdapter = new RequestFriendAdapter(new RequestFriendAdapter.IRequestFriend() {
                    @Override
                    public int getCount() {
                        if(requestFriendList==null) return 0;
                        return requestFriendList.size();
                    }

                    @Override
                    public User getFriend(int position) {
                        return requestFriendList.get(position);
                    }

                    @Override
                    public void acceptRequestFriend(int postion) {
                        acceptRequest(postion);
                        Helper.showToast(getContext(), getResources().getString(R.string.accept_successeful));
                    }

                    @Override
                    public void declineRequestFriend(int position) {
                        declineRequest(position);
                        Helper.showToast(getContext(), getResources().getString(R.string.decline_done));
                    }
                });

                rvRequestFriend.setAdapter(userAdapter);
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

    private void acceptRequest(final int position){
        final User user = requestFriendList.get(position);
        databaseReference.child(Constants.ARG_FRIENDS).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String sender_id = dataSnapshot.child("sender_id").getValue().toString();

                if(user.getId().equals(sender_id)){
                    databaseReference.child(Constants.ARG_FRIENDS).child(dataSnapshot.getKey()).child(Constants.KEY_STATUS)
                            .setValue(Constants.KEY_ACCEPTED);
                    requestFriendList.remove(position);
                    userAdapter.notifyDataSetChanged();
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
    }

    private void declineRequest(final int position){
        final User user = requestFriendList.get(position);
        databaseReference.child(Constants.ARG_FRIENDS).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String sender_id = dataSnapshot.child("sender_id").getValue().toString();

                if(user.getId().equals(sender_id)){
                    databaseReference.child(Constants.ARG_FRIENDS).child(dataSnapshot.getKey()).removeValue();
                    requestFriendList.remove(position);
                    userAdapter.notifyDataSetChanged();
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
    }
}
