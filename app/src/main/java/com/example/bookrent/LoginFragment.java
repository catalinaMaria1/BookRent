package com.example.bookrent;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bookrent.databinding.ActivityLoginBinding;
import com.example.bookrent.databinding.ActivitySignupBinding;
import static com.example.bookrent.MainActivity.user;

import java.util.Objects;

public class LoginFragment extends Fragment {
    ActivityLoginBinding binding;
    MyDataBase databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding  = ActivityLoginBinding.inflate(inflater, container, false);

        databaseHelper = new MyDataBase(getActivity());

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.emailEditText.getText().toString().trim();
                String password = binding.passwordEditText.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getActivity(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
                } else {
                    User checkCredentials = databaseHelper.checkEmailPassword(email, password);
                    if (checkCredentials.getId() !=  -1 ) {
                        Toast.makeText(getActivity(), "Login Successfully", Toast.LENGTH_SHORT).show();
                        MainActivity.user.setId(checkCredentials.getId());
                        MainActivity.user.setEmail(checkCredentials.getEmail());
                        MainActivity.user.setAmount(checkCredentials.getAmount());
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new SignupFragment()).addToBackStack(null).commit();
            }
        });
        return binding.getRoot();
    }
}