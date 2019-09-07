package com.example.rohit.expensemanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {
    private EditText rEmail;
    private EditText rPassword;
    private Button btnReg;
    private TextView rSignin;
    private ProgressDialog mDiaglog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        registration();
        mAuth = FirebaseAuth.getInstance();
        mDiaglog = new ProgressDialog(this);
    }
    public void registration()
    {
        rEmail = findViewById(R.id.reg_email);
        rPassword = findViewById(R.id.reg_password);
        btnReg = findViewById(R.id.Registration_btn);
        rSignin = findViewById(R.id.txt_signin);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_email = rEmail.getText().toString();
                String str_pass = rPassword.getText().toString();

                if(TextUtils.isEmpty(str_email))
                {
                    rEmail.setError("Email cannot be empty");
                    return;
                }
                if(TextUtils.isEmpty(str_pass))
                {
                    rEmail.setError("Password cannot be empty");
                    return;
                }
                mDiaglog.setMessage("Please Wait....");
                mAuth.createUserWithEmailAndPassword(str_email,str_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            mDiaglog.dismiss();
                            Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));

                        }
                        else
                        {
                            mDiaglog.dismiss();
                            Toast.makeText(getApplicationContext(),"Registration Failed",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

        rSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }
}
