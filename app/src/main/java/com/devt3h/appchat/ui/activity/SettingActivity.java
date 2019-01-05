package com.devt3h.appchat.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.devt3h.appchat.R;
import com.devt3h.appchat.helper.Constants;
import com.devt3h.appchat.helper.Helper;
import com.devt3h.appchat.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity{
    private CircleImageView imgAvatar;
    private EditText edtUsername, edtEmail, edtPassword;
    private Button btnChange;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        inits();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String userId = firebaseUser.getUid();
        edtEmail.setText(firebaseUser.getEmail());
        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.ARG_USERS).child(userId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user!=null) edtUsername.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newUserName = edtUsername.getText().toString();
                String newEmail = edtEmail.getText().toString();
                String newPassword = edtPassword.getText().toString();

                if(newUserName.isEmpty() || newEmail.isEmpty()){
                    Helper.showToast(SettingActivity.this,"Bạn phải điền đầy đủ thông tin");
                }else if(!newPassword.isEmpty() && newPassword.length() < 6){
                    Helper.showToast(SettingActivity.this,"Mật khẩu mới phải lớn hơn 6 ký tự");
                }else {
                    changeProfile(newUserName,newEmail,newPassword);
                    finish();
                }
            }
        });
    }

    private void changeProfile(String newUserName, String newEmail, String newPassword) {

    }


    private void inits() {
        imgAvatar = findViewById(R.id.img_avatar);
        edtUsername = findViewById(R.id.edt_name);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        btnChange = findViewById(R.id.btn_change_profile);

        mAuth = FirebaseAuth.getInstance();

    }
}
