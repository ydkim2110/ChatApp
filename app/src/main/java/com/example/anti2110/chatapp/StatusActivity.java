package com.example.anti2110.chatapp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private EditText editTextStatus;
    private Button saveBtn;

    private DatabaseReference dbRef;
    private FirebaseUser currentUser;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = currentUser.getUid();
        dbRef = FirebaseDatabase.getInstance().getReference().child("ChatUsers").child(currentUid);

        toolbar = (Toolbar) findViewById(R.id.status_app_bar);
        toolbar.setTitle("Account Status");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String statusValue = getIntent().getStringExtra("statusValue");
        editTextStatus = (EditText) findViewById(R.id.status_editText);
        editTextStatus.setText(statusValue);

        saveBtn = (Button) findViewById(R.id.status_save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(StatusActivity.this);
                progressDialog.setTitle("Status Changes");
                progressDialog.setMessage("Please wait... ");
                progressDialog.show();

                String newStatus = editTextStatus.getText().toString();

                dbRef.child("status").setValue(newStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(StatusActivity.this, "상태메시지가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(StatusActivity.this, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
}
