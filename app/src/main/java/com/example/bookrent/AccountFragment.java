package com.example.bookrent;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static com.example.bookrent.MainActivity.isLog;
import static com.example.bookrent.MainActivity.username;

public class AccountFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_account, container, false);

        TextView emailTextView = view.findViewById(R.id.textView2);
        Button settings = view.findViewById(R.id.account_settings);
        Button cart = view.findViewById(R.id.account_cart);
        Button wallet = view.findViewById(R.id.account_wallet);
        Button logout = view.findViewById(R.id.account_logout);

        emailTextView.setText(username);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).addToBackStack(null).commit();
            }
        });

        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new WalletFragment()).addToBackStack(null).commit();
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new CartFragment()).addToBackStack(null).commit();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear login state
                isLog = false;
                username = null;

                // Navigate to home screen
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                // Optionally, finish current activity if needed
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });

        return view;
    }
}
