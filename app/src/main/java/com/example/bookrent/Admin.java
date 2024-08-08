 package com.example.bookrent;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bookrent.databinding.ActivityAccountBinding;
import com.example.bookrent.databinding.ActivityBooks1Binding;

public class Admin extends Fragment {

    private BooksDBHelper dbHelper;
    ActivityBooks1Binding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= ActivityBooks1Binding.inflate(inflater,container,false);

        dbHelper = new BooksDBHelper(getActivity());


        binding.buttonInsertBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertBook();
            }
        });

        binding.buttonEraseBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eraseBook();
            }
        });

        return binding.getRoot();
    }

    private void insertBook() {
        String title = binding.editTextTitle.getText().toString().trim();
        String image = binding.editTextImage.getText().toString().trim();
        String description = binding.editTextDescription.getText().toString().trim();
        String author = binding.editTextAuthor.getText().toString().trim();
        String reviews = binding.editTextReviews.getText().toString().trim();
        Double price = Double.valueOf(binding.editTextPrice.getText().toString().trim());

        if (title.isEmpty()) {
            binding.editTextTitle.setError("Title is required!");
            binding.editTextTitle.requestFocus();
            return;
        }

        dbHelper.insertBook(title,
                image,
                description,
                author,
                reviews,
                price);
        clearFields();
        Toast.makeText(getActivity(), "Book inserted successfully!", Toast.LENGTH_SHORT).show();
    }

    private void eraseBook() {
        String bookName = binding.editTextEraseBook.getText().toString().trim();

        if (bookName.isEmpty()) {
            binding.editTextEraseBook.setError("Book name is required!");
            binding.editTextEraseBook.requestFocus();
            return;
        }

        boolean result = dbHelper.eraseBook(bookName);

        if (result) {
            Toast.makeText(getActivity(), "Failed to erase book. Book not found", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Book erased successfully!.", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        binding.editTextTitle.setText("");
        binding.editTextImage.setText("");
        binding.editTextDescription.setText("");
        binding.editTextReviews.setText("");
        binding.editTextAuthor.setText("");
        binding.editTextEraseBook.setText(""); // Clear erase book text field as well
    }
}