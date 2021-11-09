package com.instanitro.instanitro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    EditText emailAddress;
    Button reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        emailAddress=findViewById(R.id.email_field);
        reset=findViewById(R.id.reset_button);
        reset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset_button: {
                resetPassword();
                Toast.makeText(this, "Reset password link sent to your email address.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                break;
            }
        }
    }
    private void resetPassword(){

        final String email_Address=emailAddress.getText().toString().trim();
        if (email_Address.isEmpty()){
            emailAddress.setError("Email address can't be empty.");
            emailAddress.requestFocus();
            return;
        }
        if(Patterns.EMAIL_ADDRESS.matcher(email_Address).matches()){
            FirebaseAuth.getInstance().sendPasswordResetEmail(email_Address);
        }
    }
}
