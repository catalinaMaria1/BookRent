package com.example.bookrent;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bookrent.databinding.ActivitySignupBinding;

public class SignupFragment extends Fragment {

    ActivitySignupBinding binding;
    MyDataBase databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=ActivitySignupBinding.inflate(inflater, container, false);


        databaseHelper = new MyDataBase(getActivity());

        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.signupEmail.getText().toString();
                String password = binding.signupPassword.getText().toString();
                String confirmPassword = binding.signupConfirm.getText().toString();
                System.out.println("LOGGLED");

                if (email.equals("") || password.equals("") || confirmPassword.equals("")) {
                    Toast.makeText(getActivity(), "Toate campurile sunt obligatorii", Toast.LENGTH_SHORT).show();
                } else {
                    if (password.equals(confirmPassword)) {
                        Boolean checkUserEmail = databaseHelper.checkEmail(email);
                        if (!checkUserEmail) {
                            Boolean insert = databaseHelper.insertData(email, password);
                            if (insert) {
                                Toast.makeText(getActivity(), "Inregistrare cu succes", Toast.LENGTH_SHORT).show(); Intent intent = new Intent(getActivity(), LoginFragment.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getActivity(), "Inregistrare esuata", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "User existent, te rog, logheaza-te", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Parola gresita", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).addToBackStack(null).commit();
            }
        });

        return binding.getRoot();
    }
}