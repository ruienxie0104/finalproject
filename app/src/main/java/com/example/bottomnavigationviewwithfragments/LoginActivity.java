package com.example.bottomnavigationviewwithfragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText,passwordEditText;
    Button loginBtn;
    ProgressBar progressBar;
    TextView createAccountBtnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginBtn = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progress_bar);
        createAccountBtnTextView = findViewById(R.id.create_account_text_view_btn);

        loginBtn.setOnClickListener((v)->loginUser());
        createAccountBtnTextView.setOnClickListener((v)->startActivity(new Intent(LoginActivity.this,CreateAccountActivity.class)));
    }

    void loginUser(){
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        emailEditText.setText("a123@test.com");
        passwordEditText.setText("123456");
        boolean isValidated = validateDate(email,password);
        if(!isValidated){
            return;
        }

        //email = "a123@test.com";
        //password = "123456";

        loginAccountInFirebase(email,password);

    }
    void loginAccountInFirebase(String email,String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if(task.isSuccessful()){
                    if(firebaseAuth.getCurrentUser()!=null){
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));


                    }else{
                        Utility.showToast(LoginActivity.this,"Email有誤，請再次確認");
                    }

                }else{
                    Utility.showToast(LoginActivity.this,task.getException().getLocalizedMessage());
                }

            }
        });

    }

    void changeInProgress(boolean inProgress){
        if(inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }


    boolean validateDate(String email,String password){
        //驗證

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("無效的Email");
            return false;
        }
        if(password.length()<6){
            passwordEditText.setError("密碼字數需大於6");
            return false;
        }
        return true;
    }
}
