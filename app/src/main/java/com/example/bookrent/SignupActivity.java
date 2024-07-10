package com.example.bookrent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookrent.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;
    MyDataBase databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new MyDataBase(this);

        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.signupEmail.getText().toString();
                String password = binding.signupPassword.getText().toString();
                String confirmPassword = binding.signupConfirm.getText().toString();

                if (email.equals("") || password.equals("") || confirmPassword.equals("")) {
                    Toast.makeText(SignupActivity.this, "Toate campurile sunt obligatorii", Toast.LENGTH_SHORT).show();
                } else {
                    if (password.equals(confirmPassword)) {
                        Boolean checkUserEmail = databaseHelper.checkEmail(email);
                        if (!checkUserEmail) {
                            Boolean insert = databaseHelper.insertData(email, password);
                            if (insert) {
                                Toast.makeText(SignupActivity.this, "Inregistrare cu succes", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignupActivity.this, "Inregistrare esuata", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignupActivity.this, "User existent, te rog, logheaza-te", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignupActivity.this, "Parola gresita", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
