package com.example.rohit.expensemanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private Button btnLogin;
    private TextView mForgot;
    private TextView mRegister;
    private Dialog dialog;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login();
        mAuth = FirebaseAuth.getInstance();

    }
    public void login()
    {
        mEmail = findViewById(R.id.login_email);
        mPassword = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.Login_btn);
        mForgot = findViewById(R.id.forgot_txt);
        mRegister = findViewById(R.id.register_txt);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_email = mEmail.getText().toString();
                String str_pass = mPassword.getText().toString();

                if(TextUtils.isEmpty(str_email))
                {
                    mEmail.setError("Email cannot be empty");
                    return;
                }
                if(TextUtils.isEmpty(str_pass))
                {
                    mEmail.setError("Password cannot be empty");
                    return;
                }
                setDialog(true,true);

                mAuth.signInWithEmailAndPassword(str_email,str_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            setDialog(false,false);
                            Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        }
                        else
                        {
                            setDialog(false,false);
                            Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ResetActivity.class));
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));
            }
        });
    }

    private void setDialog(boolean show,boolean cre){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.progress);
        if(cre)
            dialog = builder.create();
        if (show)
            dialog.show();
        else
            dialog.dismiss();
    }
}
