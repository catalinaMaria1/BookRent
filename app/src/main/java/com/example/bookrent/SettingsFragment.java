package com.example.bookrent;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsFragment extends Fragment {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSave;
    private MyDataBase myDataBase;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.activity_settings, container, false);


        EdgeToEdge.enable(getActivity());

        // Initialize UI elements
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        buttonSave = view.findViewById(R.id.buttonSave);
        myDataBase = new MyDataBase(getActivity());

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
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
                Toast.makeText(getActivity(), "Please fill out both fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                boolean emailExists = myDataBase.checkEmail(newEmail);
                if (emailExists) {
                    boolean updateSuccessful = myDataBase.updateData(newEmail, newPassword);
                    if (updateSuccessful) {
                        Toast.makeText(getActivity(), "Account updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Error updating account", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Email does not exist", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}