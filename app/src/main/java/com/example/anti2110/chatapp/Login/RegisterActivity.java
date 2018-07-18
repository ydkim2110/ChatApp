package com.example.anti2110.chatapp.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.anti2110.chatapp.MainActivity;
import com.example.anti2110.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText registerName, registerEmail, registerPassword;
    private Button registerBtn;

    private FirebaseAuth auth;
    private DatabaseReference dbRef;


    private Toolbar toolbar;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        toolbar = (Toolbar) findViewById(R.id.register_toolbar);
        toolbar.setTitle("Create Account");
        setSupportActionBar(toolbar);
        // 뒤로가기 구현
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);

        registerName = (EditText) findViewById(R.id.register_name);
        registerEmail = (EditText) findViewById(R.id.register_email);
        registerPassword = (EditText) findViewById(R.id.register_password);
        registerBtn = (Button) findViewById(R.id.register_btn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = registerName.getText().toString();
                String email = registerEmail.getText().toString();
                String password = registerPassword.getText().toString();

                if(!TextUtils.isEmpty(name) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {
                    progressDialog.setTitle("Registering User");
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    registerUser(name, email, password);
                } else {
                    Toast.makeText(RegisterActivity.this, "모든 항목을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }

            private void registerUser(final String name, String email, String password) {
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {

                                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                    String uid = currentUser.getUid();

                                    dbRef = FirebaseDatabase.getInstance().getReference().child("ChatUsers").child(uid);

                                    Map<String, String> userMap = new HashMap<>();
                                    userMap.put("name", name);
                                    userMap.put("status", "Hi there!");
                                    userMap.put("image", "default");
                                    userMap.put("thumb_image", "default");

                                    dbRef.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                progressDialog.dismiss();
                                                Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(mainIntent);
                                                finish();
                                            }
                                        }
                                    });

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(RegisterActivity.this, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }

}
