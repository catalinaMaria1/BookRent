package com.example.bookrent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Settings extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSave;
    private MyDataBase myDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        // Initialize UI elements
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSave = findViewById(R.id.buttonSave);
        myDataBase = new MyDataBase(this);

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up the save button click listener
        buttonSave.setOnClickListener(v -> {
            String newEmail = editTextEmail.getText().toString().trim();
            String newPassword = editTextPassword.getText().toString().trim();

            // Validate inputs
            if (newEmail.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(Settings.this, "Please fill out both fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                boolean emailExists = myDataBase.checkEmail(newEmail);
                if (emailExists) {
                    boolean updateSuccessful = myDataBase.updateData(newEmail, newPassword);
                    if (updateSuccessful) {
                        Toast.makeText(Settings.this, "Account updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Settings.this, "Error updating account", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Settings.this, "Email does not exist", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(Settings.this, "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
