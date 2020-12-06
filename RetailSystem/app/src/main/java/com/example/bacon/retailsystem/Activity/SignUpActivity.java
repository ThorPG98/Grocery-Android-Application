package com.example.bacon.retailsystem.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bacon.retailsystem.Model.User;
import com.example.bacon.retailsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private DatabaseReference reference;

    private FirebaseAuth mAuth;
    private TextView tvBackToLogin;
    private EditText etSignUpEmail, etSignUpUsername, etSignUpPassword, etReEnterPassword;
    private Button btnSignUp;
    private ImageView imgShowPass, imgShowRePass;
    private TextInputLayout inputSignUpEmail, inputSignUpUsername, inputSignUpPassword, inputReEnterPassword;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        tvBackToLogin = findViewById(R.id.tv_back_to_login);
        etSignUpEmail = findViewById(R.id.et_sign_up_email);
        etSignUpUsername = findViewById(R.id.et_sign_up_username);
        etSignUpPassword = findViewById(R.id.et_sign_up_password);
        etReEnterPassword = findViewById(R.id.et_re_enter_password);
        btnSignUp = findViewById(R.id.btn_sign_up);
        imgShowPass = findViewById(R.id.img_show_hide_password);
        imgShowRePass = findViewById(R.id.img_show_hide_re_password);
        inputSignUpEmail = findViewById(R.id.input_sign_up_email);
        inputSignUpUsername = findViewById(R.id.input_sign_up_username);
        inputSignUpPassword = findViewById(R.id.input_sign_up_password);
        inputReEnterPassword = findViewById(R.id.input_re_enter_password);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        // Hard coded
        etSignUpEmail.setText("thorpeygen@gmail.com");
        etSignUpUsername.setText("Thor Pey Gen");
        etSignUpPassword.setText("123456");
        etReEnterPassword.setText("123456");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etSignUpEmail.getText().toString();
                String username = etSignUpUsername.getText().toString();
                String password = etSignUpPassword.getText().toString();
                String reEnterPassword = etReEnterPassword.getText().toString();

                Boolean emailResult = emailValidation(email);
                Boolean passwordResult = passwordValidation(password, reEnterPassword);
                Boolean usernameResult = usernameValidation(username);

                String profileImage = "https://firebasestorage.googleapis.com/v0/b/retailsystem-2bae2.appspot.com/o/download.png?alt=media&token=80152179-0dd3-4676-90b0-6b7f3213663c";

                if (emailResult && passwordResult && usernameResult) {

                    registerNewUser(email, password, username, profileImage);
                }
            }
        });

        tvBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean emailValidation(String email){

        if (TextUtils.isEmpty(email)) {
            inputSignUpEmail.setError("Field can't be empty");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputSignUpEmail.setError("Please enter a valid email address");
            return false;
        }
        else {
            inputSignUpEmail.setError(null);
            return true;
        }
    }

    private boolean usernameValidation(String username) {
        if (TextUtils.isEmpty(username)) {
            inputSignUpUsername.setError("Field can't be empty");
            return false;
        }
        else {
            inputSignUpUsername.setError(null);
            return true;
        }
    }

    private boolean passwordValidation(String password, String reEnterPassword) {
        inputSignUpPassword.setError(null);
        inputReEnterPassword.setError(null);

        // If both empty
        if(TextUtils.isEmpty(password ) && TextUtils.isEmpty(reEnterPassword)){
            inputSignUpPassword.setError("Field can't be empty");
            inputReEnterPassword.setError("Field can't be empty");
            return false;
        }
        else if (TextUtils.isEmpty(password)) {
            inputSignUpPassword.setError("Field can't be empty");
            return false;
        }
        else if (TextUtils.isEmpty(reEnterPassword)){
            inputReEnterPassword.setError("Field can't be empty");
            return false;
        }

        if (!password.equals(reEnterPassword)) {
            inputReEnterPassword.setError("Password does not match");
            return false;
        }
        return true;
    }

    private void registerNewUser(final String email, final String password, final String username, final String profileImage) {
        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Processing....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            String id = user.getUid();
                            User userHelper = new User(id, email, password, username, profileImage);
                            reference.child("users").child(id).setValue(userHelper);

                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Register Success!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void showHidePass(View view) {
        if (view.getId()==R.id.img_show_hide_password) {
            if (etSignUpPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                imgShowPass.setImageResource(R.drawable.hide_password);
                etSignUpPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else {
                imgShowPass.setImageResource(R.drawable.show_password);
                etSignUpPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }

        if (view.getId()==R.id.img_show_hide_re_password) {
            if (etReEnterPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                imgShowRePass.setImageResource(R.drawable.hide_password);
                etReEnterPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else {
                imgShowRePass.setImageResource(R.drawable.show_password);
                etReEnterPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }
}
