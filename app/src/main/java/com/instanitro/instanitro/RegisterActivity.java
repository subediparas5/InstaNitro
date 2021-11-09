package com.instanitro.instanitro;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    public FirebaseAuth mAuth;
    EditText FirstName,LastName, Email, Password,ConfirmPassword;
    Button back,Register;
    FirebaseAuth.AuthStateListener mAuthListner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        Register=findViewById(R.id.register_submit_button);
        back= findViewById(R.id.sign_in_button);
        Email= findViewById(R.id.register_email);
        Password= findViewById(R.id.register_password_field);
        FirstName= findViewById(R.id.register_fname_field);
        LastName= findViewById(R.id.register_lname_field);
        ConfirmPassword= findViewById(R.id.confirm_password_field);
        findViewById(R.id.register_submit_button).setOnClickListener(this);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        mAuthListner=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mAuth.getCurrentUser()!=null){
                    finish();
                    Intent login = new Intent(RegisterActivity.this, DashboardActivity.class);
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
            Intent register = new Intent(RegisterActivity.this, DashboardActivity.class);
            register.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(register);
        }
    }
    private void registerUser() {
        final String email = Email.getText().toString().trim();
        final String password = Password.getText().toString().trim();
        String confirmPassword=ConfirmPassword.getText().toString().trim();
        final String firstName =FirstName.getText().toString().trim();
        final String lastName =LastName.getText().toString().trim();
        if(firstName.isEmpty()){
            FirstName.setError("Please enter your first name");
            FirstName.requestFocus();
            return;
        }
        if(lastName.isEmpty()){
            LastName.setError("Please enter your first name");
            LastName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            Email.setError("Email is required");
            Email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Email.setError("Please enter a valid email address.");
            Email.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            Password.setError("Password is required.");
            Password.requestFocus();
            return;
        }
        if (password.length()<6){
            Password.setError("Minimum length of password is 6 digits.");
            Password.requestFocus();
            return;
        }
        if(!password.equals(confirmPassword)){
            ConfirmPassword.setError("Password and Confirm Password didn't match.");
            ConfirmPassword.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        String UID=mAuth.getUid();
                                                        User user = new User(firstName, lastName, email);
                                                        FirebaseDatabase.getInstance().getReference("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(getApplicationContext(), "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                                                    finish();
                                                                    Intent register = new Intent(RegisterActivity.this, DashboardActivity.class);
                                                                    register.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                    startActivity(register);
                                                                }
                                                            }
                                                        });
                                                    } else {
                                                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                                            Toast.makeText(getApplicationContext(), "User with same email address is already registered.", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }
                                            });
                                        }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_in_button:{
                startActivity(new Intent(this,LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;}
            case R.id.register_submit_button:
                registerUser();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
