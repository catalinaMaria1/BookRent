package com.example.bookrent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddBookActivity extends AppCompatActivity {

    private BooksDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new BooksDBHelper(this);
        addSampleBooks();
    }

    private void addSampleBooks() {
        // Adăugați cărți folosind metoda insertBook din BooksDBHelper
        dbHelper.insertBook("Harry Potter", "https://example.com/harry_potter.jpg",
                "A magical adventure", "J.K. Rowling", "Great book!");
        dbHelper.insertBook("Lord of the Rings", "https://example.com/lotr.jpg",
                "Epic fantasy novel", "J.R.R. Tolkien", "One ring to rule them all.");
        dbHelper.insertBook("To Kill a Mockingbird", "https://example.com/to_kill_a_mockingbird.jpg",
                "Classic novel", "Harper Lee", "Powerful message.");

        // Afișare mesaj de confirmare
        Toast.makeText(this, "Cărți adăugate în baza de date", Toast.LENGTH_SHORT).show();
    }
}
