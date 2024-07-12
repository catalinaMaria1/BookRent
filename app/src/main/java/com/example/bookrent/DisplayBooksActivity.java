package com.example.bookrent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DisplayBooksActivity extends AppCompatActivity {

    private RecyclerView recyclerViewBooks;
    private BooksAdapter booksAdapter;
    private BooksDBHelper dbHelper;
    private EditText editTextEraseBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_books);

        recyclerViewBooks = findViewById(R.id.recyclerViewBooks);
        recyclerViewBooks.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new BooksDBHelper(this);
        ArrayList<Book> booksList = dbHelper.getAllBooks();

        booksAdapter = new BooksAdapter(this, booksList);
        recyclerViewBooks.setAdapter(booksAdapter);




    }
}
