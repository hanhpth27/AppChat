package com.devt3h.appchat.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devt3h.appchat.R;
import com.devt3h.appchat.model.User;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder>{
    private IUser iUser;

    public UserAdapter(IUser iUser) {
        this.iUser = iUser;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View v = inflater.inflate(R.layout.user_item, viewGroup, false);
        return new UserHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder userHolder, int position) {
        User user = iUser.getUser(position);
        userHolder.tvUsername.setText(user.getName());
        if(!user.getAvatarURL().equals("default")){
            String url = user.getAvatarURL();
            Picasso.get().load(url)
                    .resize(50, 50)
                    .centerCrop()
                    .into(userHolder.imgAvatar);
        }
    }

    @Override
    public int getItemCount() {
        return iUser.getCount();
    }

    public class UserHolder extends RecyclerView.ViewHolder{
        private TextView tvUsername;
        private CircleImageView imgAvatar;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tv_username);
            imgAvatar = itemView.findViewById(R.id.img_avatar);
        }
    }

    public interface IUser{
        int getCount();
        User getUser(int position);
    }
}
