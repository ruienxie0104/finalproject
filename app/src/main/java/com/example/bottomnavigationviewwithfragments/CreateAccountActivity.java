package com.example.bottomnavigationviewwithfragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Member;
import java.util.regex.Pattern;

public class CreateAccountActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText, name, studentid;
    Button createAccountBtn;
    ProgressBar progressBar;
    TextView loginBtnTextView;
    FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        name = findViewById(R.id.studentname);
        studentid = findViewById(R.id.studentid);

        createAccountBtn = findViewById(R.id.create_account_btn);
        progressBar = findViewById(R.id.progress_bar);
        loginBtnTextView = findViewById(R.id.login_text_view_btn);
        ref = FirebaseDatabase.getInstance().getReference("Account");
        auth = FirebaseAuth.getInstance();

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namedata = name.getText().toString();
                String id = studentid.getText().toString();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    emailEditText.setError("未填寫信箱");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    passwordEditText.setError("未填寫密碼");
                    return;
                }
                if (password.length() < 6) {
                    passwordEditText.setError("密碼需多於6碼");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                auth.createUserWithEmailAndPassword(email, password).
                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(CreateAccountActivity.this, "已建立帳戶", Toast.LENGTH_SHORT).show();
                                    user_data data = new user_data(namedata, id);
                                    Log.i("test", auth.getCurrentUser().getUid());
                                    ref.child(auth.getCurrentUser().getUid()).setValue(data);
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                } else {
                                    Toast.makeText(CreateAccountActivity.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
        loginBtnTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(CreateAccountActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

}
