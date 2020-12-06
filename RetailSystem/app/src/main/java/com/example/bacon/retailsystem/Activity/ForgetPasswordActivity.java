package com.example.bacon.retailsystem.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bacon.retailsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    public FirebaseAuth mAuth;
    private EditText etResetPasswordEmail;
    private TextView tvSignUp, tvBackToLogin;
    private Button btnSendResetPassword;
    private TextInputLayout inputResetPasswordEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        etResetPasswordEmail = findViewById(R.id.et_reset_password_email);
        tvSignUp = findViewById(R.id.tv_forget_password_sign_up);
        tvBackToLogin = findViewById(R.id.tv_forget_password_back_to_login);
        btnSendResetPassword = findViewById(R.id.btn_send_reset);
        inputResetPasswordEmail = findViewById(R.id.input_reset_password_email);

        mAuth = FirebaseAuth.getInstance();

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgetPasswordActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tvBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSendResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = etResetPasswordEmail.getText().toString();

                if (TextUtils.isEmpty(userEmail)) {
                    inputResetPasswordEmail.setError("Field can't be Empty");
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                    inputResetPasswordEmail.setError("Please enter a valid email address");
                }
                else {
                    mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgetPasswordActivity.this, "Please check your EMAIL Account!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
                                finish();
                            }
                            else {
                                String message = task.getException().getMessage();
                                Toast.makeText(ForgetPasswordActivity.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
