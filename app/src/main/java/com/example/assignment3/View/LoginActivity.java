package com.example.assignment3.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.assignment2.R;
import com.example.assignment2.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseAuth userAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        userAuth = FirebaseAuth.getInstance();

        setContentView(binding.getRoot());

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checks if inputs are blank, then passes them to sign in user
                String givenEmail = String.valueOf(binding.inputEmail.getText());
                String givenPass = String.valueOf(binding.inputPass.getText());
                if(!givenEmail.isBlank() && !givenPass.isBlank()) {
                    signInUser(givenEmail, givenPass);
                }else{
                    Toast.makeText(getApplicationContext(), "Email and/or Password Fields Can't Be Blank!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.toRegisterPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(toRegister);
                finish();
            }
        });

    }

    private void signInUser(String email, String password){
        // attempts to sign user in, then goes to search Page
        userAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.i("test","Signed In Successful");

                            Intent goodSignIn = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(goodSignIn);
                            finish();
                        }else{
                            Log.i("test","Couldn't Sign User In. Er: "+task.getException());
                            Toast.makeText(getApplicationContext(), "Incorrect Email and/or Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}