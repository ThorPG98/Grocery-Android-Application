package com.example.bacon.retailsystem.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bacon.retailsystem.Model.User;
import com.example.bacon.retailsystem.Prevalent.Prevalent;
import com.example.bacon.retailsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView tvSignUp, tvForgetPassword, tvQuit;
    private EditText etEmail, etPassword;
    private Button btnSignIn;
    private ImageView imgShowPass;
    private TextInputLayout inputLoginEmail, inputLoginPassword;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvSignUp = findViewById(R.id.tv_login_sign_up);
        tvForgetPassword = findViewById(R.id.tv_login_forget_password);
        tvQuit = findViewById(R.id.tv_login_quit);
        etEmail = findViewById(R.id.et_login_email);
        etPassword = findViewById(R.id.et_login_password);
        btnSignIn = findViewById(R.id.btn_sign_in);
        imgShowPass = findViewById(R.id.img_login_show_hide_password);
        inputLoginEmail = findViewById(R.id.input_login_email);
        inputLoginPassword = findViewById(R.id.input_login_password);

        mAuth = FirebaseAuth.getInstance();

        //Hard coded
        etEmail.setText("thorpeygen@gmail.com");
        etPassword.setText("123456");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                Boolean emailResult = emailValidation(email);
                Boolean passwordResult = passwordValidation(password);

                if (emailResult && passwordResult){
                    signInUser(email,password);
                }
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                finish();
            }
        });

        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
                finish();
            }
        });

        tvQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }

    private boolean emailValidation(String email){

        if (TextUtils.isEmpty(email)) {
            inputLoginEmail.setError("Field can't be empty");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputLoginEmail.setError("Please enter a valid email address");
            return false;
        }
        else {
            inputLoginEmail.setError(null);
            return true;
        }
    }

    private boolean passwordValidation(String password) {

        if (TextUtils.isEmpty(password)) {
            inputLoginPassword.setError("Field can't be empty");
            return false;
        }
        else {
            inputLoginPassword.setError(null);
            return true;
        }
    }

    private void signInUser(String email, final String password) {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Processing....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    FirebaseUser user = task.getResult().getUser();
                    Prevalent.currentOnlineUser = new User();

                    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference ref = database.child("users");

                    ref.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Prevalent.currentOnlineUser = dataSnapshot.getValue(User.class);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                        // Failed to read value
                            Log.w("LOGIN", "Failed to read value.", error.toException());
                        }
                    });

                    progressDialog.dismiss();
                    finish();
                    startActivity(new Intent(LoginActivity.this, HomePageNav.class));
                }
                else{
                    progressDialog.dismiss();
                    task.getException().printStackTrace();;
                    Toast.makeText(LoginActivity.this, "Login Failed! Please Try Again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showHidePass(View view) {
        if (view.getId()==R.id.img_login_show_hide_password) {
            if (etPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                imgShowPass.setImageResource(R.drawable.hide_password);
                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else {
                imgShowPass.setImageResource(R.drawable.show_password);
                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }
}
