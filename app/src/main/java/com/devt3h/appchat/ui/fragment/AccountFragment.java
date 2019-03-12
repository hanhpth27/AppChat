package com.devt3h.appchat.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.devt3h.appchat.R;
import com.devt3h.appchat.helper.Constants;
import com.devt3h.appchat.helper.Helper;
import com.devt3h.appchat.model.AccountUser;
import com.devt3h.appchat.model.Friend;
import com.devt3h.appchat.ui.activity.SettingActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends Fragment implements View.OnClickListener {
    private TextView tvUserName, tvStatus, tvBirthday, tvCountry, tvCareer;
    private ImageView imgCover, imgAvatar;
    private TextView tvPost, tvFollowers, tvEdit;

    private DatabaseReference rDatabase;
    private String idCurrent;

    List<String> listId ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        intit(v);

        rDatabase.child(Constants.ARG_USERS).child(idCurrent).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AccountUser accountUser = dataSnapshot.getValue(AccountUser.class);
                updateUI(accountUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        countFriend(idCurrent);
        return v;
    }

    private void updateUI(AccountUser accountUser) {
        tvUserName.setText(accountUser.getName());
        tvBirthday.setText(accountUser.getBirthday());
        tvCareer.setText(accountUser.getCareer());
        tvCountry.setText(accountUser.getCountry());
        if(!accountUser.getStatus().equals(Constants.KEY_DEFAULT)){
            tvStatus.setText(accountUser.getStatus());
        }

        String avatarURL = accountUser.getAvatarURL();
        String coverURL = accountUser.getCoverURL();
        if(!avatarURL.equals(Constants.KEY_DEFAULT)){
            Glide.with(getContext())
                    .load(avatarURL)
                    .override(getResources().getDimensionPixelSize(R.dimen.size_image), getResources().getDimensionPixelSize(R.dimen.size_image))
                    .centerCrop()
                    .into(imgAvatar);

        }

        if(!coverURL.equals(Constants.KEY_DEFAULT)){
            Glide.with(getContext())
                    .load(coverURL)
                    .centerCrop()
                    .into(imgCover);
        }
    }

    private void countFriend(String id) {
        rDatabase.child(Constants.ARG_FRIENDS).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Friend friend = dataSnapshot.getValue(Friend.class);
                if(friend!=null){
                    if (friend.getSender_id().equals(id) && friend.getStatus().equals(Constants.KEY_ACCEPTED)) {
                        listId.add(friend.getReceiver_id());
                    } else if (friend.getReceiver_id().equals(id) && friend.getStatus().equals(Constants.KEY_ACCEPTED)) {
                        listId.add(friend.getReceiver_id());
                    }

                    int count = listId.size();
                    tvFollowers.setText(count + " \nFollowers");
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

        tvEdit.setOnClickListener(this);
    }

    private void intit(View v) {
        tvUserName = v.findViewById(R.id.tv_username);
        tvBirthday = v.findViewById(R.id.tv_birthday);
        tvStatus = v.findViewById(R.id.tv_status);
        tvCareer = v.findViewById(R.id.tv_career);
        tvCountry = v.findViewById(R.id.tv_country);
        tvPost = v.findViewById(R.id.tv_post);
        tvFollowers = v.findViewById(R.id.tv_followers);
        tvEdit = v.findViewById(R.id.tv_edit_profile);
        imgCover = v.findViewById(R.id.img_cover);
        imgAvatar = v.findViewById(R.id.img_avatar);

        rDatabase = FirebaseDatabase.getInstance().getReference();

        idCurrent = FirebaseAuth.getInstance().getCurrentUser().getUid();
        listId = new ArrayList<>();
    }

    @Override
    public void onClick(View view) {
        Intent iEdit = new Intent(getActivity(), SettingActivity.class);
        startActivity(iEdit);
    }
}
