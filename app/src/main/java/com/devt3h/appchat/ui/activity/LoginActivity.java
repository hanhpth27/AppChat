package com.devt3h.appchat.ui.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.devt3h.appchat.R;
import com.devt3h.appchat.helper.Helper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtEmail, edtPassword;
    private Button btnLogin, btnRegister, btnForgotPassword;
    private FirebaseAuth mAuth;
    private ProgressDialog progressLoginDialog;
    private Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        inits();
    }

    private void inits() {
        progressLoginDialog = new ProgressDialog(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
        btnForgotPassword = findViewById(R.id.btn_forgot_password);
        btnRegister = findViewById(R.id.btn_register);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnForgotPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                loginUser();
                break;
            case R.id.btn_register:
                openRegisterActivity();
                break;
            case R.id.btn_forgot_password:
                resetPassword();
                break;
            default:
                break;
        }
    }

    private void resetPassword() {
        final Dialog dialog = new Dialog(this, R.style.Theme_Dialog);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_reset_password);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

        dialog.setCanceledOnTouchOutside(true);

        EditText edtEmail = dialog.findViewById(R.id.edt_email);
        Button btnConform = dialog.findViewById(R.id.btn_submit);
        btnConform.setOnClickListener(view -> {
            String email = edtEmail.getText().toString();
            if(email.isEmpty()){
                Helper.showToast(this, "Please enter your email");
            }else{
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Helper.showToast(LoginActivity.this, "Check email to reset your password!");
                        }else {
                            Helper.showToast(LoginActivity.this, "Fail to send reset password email!");
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    private void openRegisterActivity() {
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
        //finish();
    }

    private void loginUser() {
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        if(email.isEmpty() || password.isEmpty()){
            Helper.showToast(LoginActivity.this,"Điền đầy đủ email và mật khẩu");
        }else {
            progressLoginDialog.setTitle("Loginning Your Account");
            progressLoginDialog.setMessage("Please Wait ... ");
            progressLoginDialog.show();
            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }else {
                                Helper.showToast(LoginActivity.this,"Lỗi, thử lại sau");
                            }
                            progressLoginDialog.dismiss();
                        }
                    });
        }
    }
}
