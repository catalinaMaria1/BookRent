package com.example.bookrent;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Admin extends Fragment {

    private BooksDBHelper dbHelper;
    private EditText editTextTitle, editTextImage, editTextDescription, editTextAuthor, editTextReviews, editTextEraseBook;
    private Button buttonInsertBook, buttonEraseBook;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.activity_books1, container, false);

        dbHelper = new BooksDBHelper(getActivity());

        editTextTitle = v.findViewById(R.id.editTextTitle);
        editTextImage = v.findViewById(R.id.editTextImage);
        editTextDescription = v.findViewById(R.id.editTextDescription);
        editTextAuthor = v.findViewById(R.id.editTextAuthor);
        editTextReviews = v.findViewById(R.id.editTextReviews);
        editTextEraseBook = v.findViewById(R.id.editTextEraseBook);
        buttonInsertBook = v.findViewById(R.id.buttonInsertBook);
        buttonEraseBook = v.findViewById(R.id.buttonEraseBook);

        buttonInsertBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertBook();
            }
        });

        buttonEraseBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eraseBook();
            }
        });

        return v;
    }

    private void insertBook() {
        String title = editTextTitle.getText().toString().trim();
        String image = editTextImage.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String author = editTextAuthor.getText().toString().trim();
        String reviews = editTextReviews.getText().toString().trim();

        if (title.isEmpty()) {
            editTextTitle.setError("Title is required!");
            editTextTitle.requestFocus();
            return;
        }

        dbHelper.insertBook(title, image, description, author, reviews);
        clearFields();
        Toast.makeText(getActivity(), "Book inserted successfully!", Toast.LENGTH_SHORT).show();
    }

    private void eraseBook() {
        String bookName = editTextEraseBook.getText().toString().trim();

        if (bookName.isEmpty()) {
            editTextEraseBook.setError("Book name is required!");
            editTextEraseBook.requestFocus();
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
        editTextTitle.setText("");
        editTextImage.setText("");
        editTextDescription.setText("");
        editTextAuthor.setText("");
        editTextReviews.setText("");
        editTextEraseBook.setText("");
    }
}