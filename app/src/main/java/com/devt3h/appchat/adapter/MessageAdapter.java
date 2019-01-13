package com.devt3h.appchat.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.devt3h.appchat.R;
import com.devt3h.appchat.helper.Constants;
import com.devt3h.appchat.model.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private IMessage iMessage;
    private FirebaseUser currentUser;
    private Context context;

    public MessageAdapter(IMessage iMessage){
        this.iMessage = iMessage;

    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view;
        if(getItemViewType(i) == Constants.TYPE_MESSAGE_LEFT) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_message_left, viewGroup, false);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_message_right,viewGroup,false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, int i) {
        Chat chat = iMessage.getChat(i);

        messageViewHolder.tvMessage.setText(chat.getMessage());
        if(!iMessage.getAvatarURL().equals("default") && getItemViewType(i) == Constants.TYPE_MESSAGE_LEFT){
            Glide.with(context).load(iMessage.getAvatarURL())
                    .into(messageViewHolder.ivAvatarChat);
        }

    }

    @Override
    public int getItemCount() {
        return iMessage.getCount();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivAvatarChat;
        private TextView tvMessage;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatarChat = itemView.findViewById(R.id.iv_avatar_chat);
            tvMessage = itemView.findViewById(R.id.tv_message);
        }
    }

    public interface IMessage{
        int getCount();
        Chat getChat(int position);
        String getAvatarURL();

    }

    @Override
    public int getItemViewType(int position) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(iMessage.getChat(position).getSender_id().equals(currentUser.getUid()))
            return Constants.TYPE_MESSAGE_RIGHT;
        else
            return Constants.TYPE_MESSAGE_LEFT;

    }
}
