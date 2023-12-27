package com.example.foodcarboncalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class RegisterActivity extends AppCompatActivity {

    private EditText email, password, confirmPassword;
    Button register;
    TextView alreadyAccount;
    FirebaseAuth mAuth;
    ProgressDialog mLoadingBar;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.name_input);
        password = findViewById(R.id.password_input);
        confirmPassword = findViewById(R.id.confirmPassword_input);

        register = findViewById(R.id.register_button);
        alreadyAccount = findViewById(R.id.alreadyAccount_text);
        mAuth = FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(this);

        /*TextView btn = findViewById(R.id.alreadyAccount_text);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });*/

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AtemptRegistartion();
            }
        });

        alreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);//intent for go to other class
                startActivity(intent);
            }
        });
    }

    private void AtemptRegistartion() {
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();
        String confirmPasswordText = confirmPassword.getText().toString();

        if(emailText.isEmpty() || !emailText.toLowerCase().contains("@gmail")){
            showError(email, "Email is not valid");
        } else if (passwordText.isEmpty() || passwordText.length()<6) {
            showError(password, "Password must be greated than 6 latter");
        } else if (!confirmPasswordText.equals(passwordText)) {
            showError(confirmPassword, "Password did not match!");
        }else {
            mLoadingBar.setTitle("Registration");
            mLoadingBar.setMessage("Please wait, while your credentials");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();
            mAuth.createUserWithEmailAndPassword(emailText,passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        mLoadingBar.dismiss();
                        Toast.makeText(RegisterActivity.this, "Registration is sucessful", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterActivity.this, SetupActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    }
                    else{
                        mLoadingBar.dismiss();
                        Toast.makeText(RegisterActivity.this, "Registration is failed", Toast.LENGTH_LONG).show();

                    }
                }
            });

        }
    }

    private void showError(EditText field, String text) {
        field.setError(text);
        field.requestFocus();

    }
}