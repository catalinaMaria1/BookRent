package com.example.bookrent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Books1 extends AppCompatActivity {

    private BooksDBHelper dbHelper;
    private EditText editTextTitle, editTextImage, editTextDescription, editTextAuthor, editTextReviews, editTextPrice, editTextEraseBook;
    private Button buttonInsertBook, buttonEraseBook;

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
        editTextPrice = findViewById(R.id.editTextPrice); // Added EditText for price
        editTextEraseBook = findViewById(R.id.editTextEraseBook);
        buttonInsertBook = findViewById(R.id.buttonInsertBook);
        buttonEraseBook = findViewById(R.id.buttonEraseBook);

        buttonInsertBook.setOnClickListener(v -> insertBook());

        buttonEraseBook.setOnClickListener(v -> eraseBook());
    }

    private void insertBook() {
        String title = editTextTitle.getText().toString().trim();
        String image = editTextImage.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String author = editTextAuthor.getText().toString().trim();
        String reviews = editTextReviews.getText().toString().trim();
        String priceString = editTextPrice.getText().toString().trim();
        double price = 0.0;

        if (!priceString.isEmpty()) {
            try {
                price = Double.parseDouble(priceString);
            } catch (NumberFormatException e) {
                editTextPrice.setError("Invalid price format");
                editTextPrice.requestFocus();
                return;
            }
        }

        if (title.isEmpty()) {
            editTextTitle.setError("Title is required!");
            editTextTitle.requestFocus();
            return;
        }

        dbHelper.insertBook(title, image, description, author, reviews, price);
        clearFields();
        Toast.makeText(this, "Book inserted successfully!", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Failed to erase book. Book not found", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Book erased successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        editTextTitle.setText("");
        editTextImage.setText("");
        editTextDescription.setText("");
        editTextAuthor.setText("");
        editTextReviews.setText("");
        editTextPrice.setText(""); // Clear price field
        editTextEraseBook.setText(""); // Clear erase book text field as well
    }
}
