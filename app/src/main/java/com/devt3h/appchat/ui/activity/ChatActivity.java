package com.devt3h.appchat.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.devt3h.appchat.R;
import com.devt3h.appchat.adapter.MessageAdapter;
import com.devt3h.appchat.helper.Constants;
import com.devt3h.appchat.helper.Helper;
import com.devt3h.appchat.model.Chat;
import com.devt3h.appchat.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtMessage;
    private ImageButton btnSend, btnCallVoice, btnCallVideo;
    private TextView tvNameOfFriend;
    private RecyclerView rvMessage;
    private FirebaseUser currentUser;
    private DatabaseReference reference;
    private Toolbar toolbar;
    private List<Chat> listChat;
    private MessageAdapter messageAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        inits();
    }

    private void inits(){
        edtMessage = findViewById(R.id.edt_message);
        btnSend = findViewById(R.id.btn_send);
        rvMessage = findViewById(R.id.rv_message);
        tvNameOfFriend = findViewById(R.id.tv_user_chat_name);
        btnCallVoice = findViewById(R.id.btn_call_voice);
        btnCallVideo = findViewById(R.id.btn_call_video);
        btnCallVoice.setOnClickListener(this);
        btnCallVideo.setOnClickListener(this);

        // toolbar
        toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // recycler view message
        LinearLayoutManager messageLayoutManager = new LinearLayoutManager(getApplicationContext());
        messageLayoutManager.setStackFromEnd(true);
        rvMessage.setHasFixedSize(true);
        rvMessage.setLayoutManager(messageLayoutManager);

        // chat
        final String userId = getIntent().getStringExtra("userId");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference(Constants.ARG_USERS).child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                tvNameOfFriend.setText(user.getName());
                readMessage(currentUser.getUid(), userId, user.getAvatarURL());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = edtMessage.getText().toString();
                if(!message.isEmpty()){
                    sendMessage(currentUser.getUid(),userId,message);
                } else {
                    Helper.showToast(getApplicationContext(),"Tin nhắn rỗng");
                }

            }
        });
    }

    private void sendMessage(String senderId, String receiverId, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender_id", senderId);
        hashMap.put("receiver_id", receiverId);
        hashMap.put("message", message);
        hashMap.put("type", "text");
        hashMap.put("seen", false);

        reference.child("Chats").push().setValue(hashMap);
        edtMessage.setText("");
    }
    private void readMessage(final String senderId, final String receiverId, final String avatarURL){
        listChat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listChat.clear();
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    Chat chat = data.getValue(Chat.class);
                    if((senderId.equals(chat.getSender_id()) && receiverId.equals(chat.getReceiver_id()))
                        || (senderId.equals(chat.getReceiver_id()) && receiverId.equals(chat.getSender_id()))){
                        listChat.add(chat);
                    }
                }

                messageAdapter = new MessageAdapter(new MessageAdapter.IMessage() {
                    @Override
                    public int getCount() {
                        return listChat == null ? 0 : listChat.size();
                    }

                    @Override
                    public Chat getChat(int position) {
                        return listChat.get(position);
                    }

                    @Override
                    public String getAvatarURL() {
                        return avatarURL;
                    }
                });
                rvMessage.setAdapter(messageAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View v) {

    }
}
