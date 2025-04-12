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
import com.example.assignment2.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;

    private FirebaseAuth userAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // binding for easy access to Views
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        userAuth = FirebaseAuth.getInstance();

        binding.toLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // goes back to login
                Intent toLoginIntObj = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(toLoginIntObj);
                finish();
            }
        });

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validates inputs with regex patterns, then passes to registerUser function
                String newEmail = String.valueOf(binding.emailInput.getText());
                String newPass = String.valueOf(binding.passInput.getText());
                if(passAndEmailAreValid(newEmail, newPass)) {
                    registerUser(newEmail, newPass);
                }else{
                    Toast.makeText(getApplicationContext(), "Email and/or Password Have Improper Structure(s)!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void registerUser(String email, String password){
        userAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // attempt to create a new user with given inputs, then goes to login Page
                        if(task.isSuccessful()){
                            Log.i("test","New User has been created!");
                            Toast.makeText(getApplicationContext(), "New User Created!", Toast.LENGTH_SHORT).show();

                            Intent intentObj = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intentObj);
                            finish();
                        }else{
                            Log.i("test","User couldn't be created. Er: "+task.getException());
                            Toast.makeText(getApplicationContext(), "Email and/or Password Don't Meet Requirements", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean passAndEmailAreValid(String email, String pass){
        // email pattern checks for a @ symbol in email and if it's blank
        boolean isValidEmailOrBlank = Pattern.compile("^(.+)@(\\S+)$").matcher(email).matches() && !email.isBlank();
        // password pattern checks if password is at least 6 character, 1 uppercase letter, 1 lowercase and 1 digit
        boolean isValidPass = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{6,}$").matcher(pass).matches();
        return isValidEmailOrBlank && isValidPass;
    }
}