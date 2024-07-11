package com.example.bookrent;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DisplayBooksActivity extends AppCompatActivity implements BooksAdapter.OnBookDeleteClickListener {

    private RecyclerView recyclerViewBooks;
    private BooksAdapter booksAdapter;
    private BooksDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_books);

        recyclerViewBooks = findViewById(R.id.recyclerViewBooks);
        recyclerViewBooks.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new BooksDBHelper(this);
        ArrayList<Book> booksList = dbHelper.getAllBooks();

        booksAdapter = new BooksAdapter(this, booksList, this);
        recyclerViewBooks.setAdapter(booksAdapter);
    }

    @Override
    public void onDeleteClick(Book book) {
        dbHelper.eraseBook(book.getTitle());
        booksAdapter.updateBooks(dbHelper.getAllBooks());
    }
}
