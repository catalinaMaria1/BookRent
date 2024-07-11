package com.example.bookrent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Books1 extends AppCompatActivity {

    private BooksDBHelper dbHelper;
    private EditText editTextTitle, editTextImage, editTextDescription, editTextAuthor, editTextReviews;
    private Button buttonInsertBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books1);


        dbHelper = new BooksDBHelper(this);


        editTextTitle = findViewById(R.id.editTextTitle);
        editTextImage = findViewById(R.id.editTextImage);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextAuthor = findViewById(R.id.editTextAuthor);
        editTextReviews = findViewById(R.id.editTextReviews);
        buttonInsertBook = findViewById(R.id.buttonInsertBook);

        buttonInsertBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertBook();
            }
        });
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
        Toast.makeText(this, "Book inserted successfully!", Toast.LENGTH_SHORT).show();
    }

    private void clearFields() {
        editTextTitle.setText("");
        editTextImage.setText("");
        editTextDescription.setText("");
        editTextAuthor.setText("");
        editTextReviews.setText("");
    }
}
