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
import com.devt3h.appchat.model.User;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {
    private Context context;
    private List<User> users;

    public FriendAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item,viewGroup,false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder userViewHolder, int i) {
        User user = users.get(i);
        userViewHolder.tvName.setText(user.getName());

        if(user.getAvatarURL().equals("default"))
            userViewHolder.ivAvatar.setImageResource(R.drawable.user_icon);
        else {
            Glide.with(context).load(user.getAvatarURL())
                    .into(userViewHolder.ivAvatar);
        }

    }

    @Override
    public int getItemCount() {
        if(users == null)
            return 0;
        else return users.size();
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivAvatar;
        private TextView tvName;
        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvName = itemView.findViewById(R.id.tv_name);
        }
    }
}
