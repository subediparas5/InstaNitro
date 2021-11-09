package com.instanitro.instanitro;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton register;
    Button login;
    EditText Email,Password;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    FirebaseAuth.AuthStateListener mAuthListner;
    TextView forgotPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth=FirebaseAuth.getInstance();
        forgotPassword=findViewById(R.id.forgot_password_button);
        forgotPassword.setOnClickListener(this);
        register = findViewById(R.id.register_button);
        findViewById(R.id.register_button).setOnClickListener(this);
        login=findViewById(R.id.sign_in_button);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        Email = findViewById(R.id.email_field);
        Password=findViewById(R.id.password_field);
        mAuthListner=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mAuth.getCurrentUser()!=null){
                    finish();
                    Intent login = new Intent(LoginActivity.this, DashboardActivity.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(login);}
            }
        };
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
        if(mAuth.getCurrentUser()!=null){
            finish();
            Intent register = new Intent(LoginActivity.this, DashboardActivity.class);
            register.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(register);
        }
    }
    private void signIn(){

        final String email = Email.getText().toString().trim();
        final String password = Password.getText().toString().trim();
        if (email.isEmpty()) {
            Email.setError("Username or phone number is required");
            Email.requestFocus();
            return;
        }
        if (!(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            Email.setError("Please enter a valid email address.");
            Email.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            Password.setError("Password is required.");
            Password.requestFocus();
            return;
        }
        if(Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "User Logged in", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent login = new Intent(LoginActivity.this, DashboardActivity.class);
                        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(login);
                    } else
                        Toast.makeText(LoginActivity.this,"Email address and password did not match",Toast.LENGTH_SHORT).show();
                }
            });
            }
        }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_button: {
                finish();
                startActivity(new Intent(this, RegisterActivity.class));
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
                break;
            }
            case R.id.sign_in_button:{
                signIn();
                break;
            }
            case R.id.forgot_password_button:{
                finish();
                startActivity(new Intent(this,ForgotPasswordActivity.class));
            }
        }
    }
    }