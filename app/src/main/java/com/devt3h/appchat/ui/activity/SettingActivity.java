package com.devt3h.appchat.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.devt3h.appchat.R;
import com.devt3h.appchat.helper.Constants;
import com.devt3h.appchat.helper.Helper;
import com.devt3h.appchat.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity{
    private final static int GALLERY_PICK = 1;

    private CircleImageView imgAvatar;
    private EditText edtUsername, edtEmail, edtPassword;
    private Button btnChange;
    private Toolbar toolbar;
    private Uri linkIAvartar;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private StorageReference sReference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        inits();
        sReference = FirebaseStorage.getInstance().getReference().child(Constants.STG_IMAGE);
        firebaseUser = mAuth.getCurrentUser();
        String userId = firebaseUser.getUid();
        //edtEmail.setHint(firebaseUser.getEmail());
        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.ARG_USERS).child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(!user.getAvatarURL().equals(Constants.KEY_DEFAULT)){
                    String url = user.getAvatarURL();
                    Picasso.get().load(url)
                            .resize(250, 250)
                            .centerCrop()
                            .into(imgAvatar);
                }
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

                if(!newPassword.isEmpty() && newPassword.length() < 6){
                    Helper.showToast(SettingActivity.this,"Mật khẩu mới phải lớn hơn 6 ký tự");
                }else {
                    changeProfile(newUserName,newEmail,newPassword, linkIAvartar);
                    finish();
                }
            }
        });

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_PICK);

//                CropImage.activity()
//                        .setGuidelines(CropImageView.Guidelines.ON)
//                        .setAspectRatio(1, 1)
//                        .start(CropImageActivity.this);
            }
        });
    }

    private void changeProfile(String newUserName, String newEmail, String newPassword, Uri linkIAvartar) {
        if(!newEmail.isEmpty())  firebaseUser.updateEmail(newEmail);
        if(!newPassword.isEmpty()) firebaseUser.updatePassword(newPassword);
        if(linkIAvartar!=null){
             String userId = firebaseUser.getUid();
                final StorageReference filePath = sReference.child(userId +".jpg");

            UploadTask uploadTask = filePath.putFile(linkIAvartar);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        databaseReference.child(Constants.KEY_AVATAR_URL).setValue(downloadUri.toString());
                        Helper.showToast(SettingActivity.this, getResources().getString(R.string.setting_successeful));
                    } else {
                        // Handle failures
                        // ...
                        Helper.showToast(SettingActivity.this, getResources().getString(R.string.setting_error));
                    }
                }
            });
        }

        if(!newUserName.isEmpty()) databaseReference.child(Constants.KEY_USER_NAME).setValue(newUserName);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK && requestCode == GALLERY_PICK){
            CropImage.activity(data.getData())
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                linkIAvartar = result.getUri();
                imgAvatar.setImageURI(linkIAvartar);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void inits(){
        imgAvatar = findViewById(R.id.img_avatar);
        edtUsername = findViewById(R.id.edt_name);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        btnChange = findViewById(R.id.btn_change_profile);

        toolbar = findViewById(R.id.setting_account);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.setting_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mAuth = FirebaseAuth.getInstance();

    }
}
