package com.example.anti2110.chatapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersViewHolder extends RecyclerView.ViewHolder {

    View view;

    public UsersViewHolder(View itemView) {
        super(itemView);
        view = itemView;
    }
    public void setName(String name){
        TextView userNameView = view.findViewById(R.id.user_single_name);
        userNameView.setText(name);
    }
    public void setUserStatus(String status){
        TextView userStatusView = view.findViewById(R.id.user_single_status);
        userStatusView.setText(status);
    }
    public void setUserImage(String thumb_image, Context context) {
        CircleImageView userImageView = (CircleImageView) view.findViewById(R.id.user_single_image);
        Picasso.get().load(thumb_image).placeholder(R.drawable.avatar).into(userImageView);
    }
}
