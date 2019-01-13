package com.devt3h.appchat.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.devt3h.appchat.R;
import com.devt3h.appchat.helper.Constants;
import com.devt3h.appchat.helper.Helper;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UnfriendFragment extends Fragment implements View.OnClickListener {
    private Button btnUnfriend;
    private String key;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(Constants.ARG_FRIENDS);
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_unfriend, container, false);
        btnUnfriend = v.findViewById(R.id.btn_unfriend);

        key = getArguments().getString("key");

        btnUnfriend.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        unfriend(key);
        getActivity().finish();
    }

    private void unfriend(String key) {
        Task task = mDatabase.child(key).removeValue();
        if(task.isSuccessful()){
            Helper.showToast(getContext(), getResources().getString(R.string.unfriend_done));
        }else {
            Helper.showToast(getContext(), getResources().getString(R.string.setting_error));
        }
    }
}
