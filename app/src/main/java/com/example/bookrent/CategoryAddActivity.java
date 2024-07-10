package com.example.bookrent;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookrent.databinding.ActivityCategoryAddBinding;

public class CategoryAddActivity extends AppCompatActivity {

    private ActivityCategoryAddBinding binding;
    private ProgressDialog progressDialog;
    private String category;
    private BooksDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new BooksDBHelper(this);

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
            addCategoryToDatabase();
        }
    }

    private void addCategoryToDatabase() {
        progressDialog.setMessage("Adding category...");
        progressDialog.show();

        try {
            dbHelper.insertCategory(category);
            progressDialog.dismiss();
            Toast.makeText(CategoryAddActivity.this, "Category added successfully!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(CategoryAddActivity.this, "Failed to add category: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
