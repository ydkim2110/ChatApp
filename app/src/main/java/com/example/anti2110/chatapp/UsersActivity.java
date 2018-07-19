package com.example.anti2110.chatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.anti2110.chatapp.Model.Users;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UsersActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView usersList;

    private FirebaseRecyclerAdapter<Users, UsersViewHolder> adapter;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        toolbar = (Toolbar) findViewById(R.id.users_app_bar);
        toolbar.setTitle("All Users");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbRef = FirebaseDatabase.getInstance().getReference().child("ChatUsers");

        usersList =(RecyclerView) findViewById(R.id.users_list);
        usersList.setHasFixedSize(true);
        usersList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
          adapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(
                  Users.class,
                  R.layout.user_single_layout,
                  UsersViewHolder.class,
                  dbRef
          ) {
              @Override
              protected void populateViewHolder(UsersViewHolder viewHolder, Users model, int position) {
                    viewHolder.setName(model.getName());
                    viewHolder.setUserStatus(model.getStatus());
                    viewHolder.setUserImage(model.getThumb_image(), getApplicationContext());

                    final String userId = getRef(position).getKey();

                    viewHolder.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent profileIntent = new Intent(UsersActivity.this, ProfileActivity.class);
                            profileIntent.putExtra("userId", userId);
                            startActivity(profileIntent);
                        }
                    });
              }
          };
          adapter.notifyDataSetChanged();
          usersList.setAdapter(adapter);
    }

}
