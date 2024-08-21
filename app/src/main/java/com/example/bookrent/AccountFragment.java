package com.example.bookrent;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;

import static com.example.bookrent.MainActivity.user;

public class AccountFragment extends Fragment {

    private ActivityResultLauncher<Intent> imagePickLauncher;
    private ImageView profilePic;
    private Uri selectedImageUri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Register the image picker launcher
        imagePickLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            selectedImageUri = data.getData();
                            profilePic.setImageURI(selectedImageUri);

                            // Save the image URI to the database
                            MyDataBase db = new MyDataBase(getContext());
                            db.updateProfilePicUri(String.valueOf(user.getId()), selectedImageUri.toString());
                        }
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_account, container, false);

        // Initialize views
        TextView emailTextView = view.findViewById(R.id.textView2);
        Button settings = view.findViewById(R.id.account_settings);
        Button cart = view.findViewById(R.id.account_cart);
        Button wallet = view.findViewById(R.id.account_wallet);
        Button logout = view.findViewById(R.id.account_logout);
        TextView moneyText = view.findViewById(R.id.moneyAmount);
        profilePic = view.findViewById(R.id.imageView4);

        // Set user details if logged in
        try {
            emailTextView.setText(user.getEmail());
            moneyText.setText(String.valueOf(user.getAmount()) + " RON");
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error, not Logged in", Toast.LENGTH_SHORT).show();
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).addToBackStack(null).commit();
        }

        // Set profile picture from the database
        MyDataBase db = new MyDataBase(getContext());
        String profilePicUri = db.getProfilePicUri(String.valueOf(user.getId()));
        if (profilePicUri != null && !profilePicUri.isEmpty()) {
            profilePic.setImageURI(Uri.parse(profilePicUri));
        }

        // Set onClickListeners for buttons
        settings.setOnClickListener(v ->
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).addToBackStack(null).commit()
        );

        wallet.setOnClickListener(v ->
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new WalletFragment()).addToBackStack(null).commit()
        );

        cart.setOnClickListener(v ->
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new CartFragment()).addToBackStack(null).commit()
        );

        logout.setOnClickListener(v -> {
            user = new User(); // Log out the user

            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            if (getActivity() != null) {
                getActivity().finish();
            }
        });

        // Set click listener for profile picture
        profilePic.setOnClickListener(v -> {
            ImagePicker.with(this)
                    .cropSquare()
                    .compress(512)
                    .maxResultSize(512, 512)
                    .createIntent(intent -> {
                        imagePickLauncher.launch(intent);
                        return null;
                    });
        });

        return view;
    }
}
