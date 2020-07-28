package com.example.appnotification;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    DatabaseReference reference;
    Button btRegister;
    TextView username,email,password,repass;
    FirebaseAuth Auth;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        reference = FirebaseDatabase.getInstance().getReference();
        Auth = FirebaseAuth.getInstance();
        anhxa();
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtUsername = username.getText().toString();
                String txtemail = email.getText().toString();
                String txtpassword = password.getText().toString();
                String txtrepass = repass.getText().toString();

                if(TextUtils.isEmpty(txtUsername) || TextUtils.isEmpty(txtemail) || TextUtils.isEmpty(txtpassword) || TextUtils.isEmpty(txtrepass)){
                    Toast.makeText(RegisterActivity.this, "All fileds are required!", Toast.LENGTH_SHORT).show();
                }else if(txtpassword.length() < 6){
                    Toast.makeText(RegisterActivity.this, "Password must be at least six characters!", Toast.LENGTH_SHORT).show();
                }else if(!(txtpassword.equalsIgnoreCase(txtrepass))){
                    Toast.makeText(RegisterActivity.this, "Check your password!", Toast.LENGTH_SHORT).show();
                }else {
                    register(txtUsername,txtemail,txtpassword);
                }
            }
        });


    }
    public void register(final String username, String email, String password){
        Auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser FBUser = Auth.getCurrentUser();
                    String userid = FBUser.getUid();

                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id",userid);
                    hashMap.put("username", username);
                    hashMap.put("imageURL", "default");
                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Register seccessfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            }
                        }
                    });
                }else {
                    Toast.makeText(RegisterActivity.this, "You can't register with this email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void anhxa(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btRegister = findViewById(R.id.btRegister);
        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        repass = findViewById(R.id.repass);


    }
}
