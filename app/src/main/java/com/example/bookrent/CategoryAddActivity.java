package com.example.bookrent;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookrent.databinding.ActivityCategoryAddBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CategoryAddActivity extends AppCompatActivity {

    private static final String TAG = "CategoryAddActivity";
    private ActivityCategoryAddBinding binding;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    private void validateData() {
        category = binding.categoryEt.getText().toString().trim();
        if (TextUtils.isEmpty(category)) {
            Toast.makeText(this, "Please enter category!", Toast.LENGTH_SHORT).show();
        } else {
            addCategoryFirebase();
        }
    }

    private void addCategoryFirebase() {
        if (firebaseAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Please log in to add a category", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Adding category...");
        progressDialog.show();

        long timestamp = System.currentTimeMillis();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", "" + timestamp);
        hashMap.put("category", "" + category);
        hashMap.put("timestamp", "" + timestamp);
        hashMap.put("uid", "" + firebaseAuth.getUid());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        Log.d(TAG, "Database reference: " + ref.toString());

        ref.child("" + timestamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(CategoryAddActivity.this, "Category added successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(CategoryAddActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error adding category", e);
                    }
                });
    }
}
