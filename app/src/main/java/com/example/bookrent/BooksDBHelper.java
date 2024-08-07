package com.example.bookrent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class BooksDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "books.db";
    private static final int DATABASE_VERSION = 4;

    public static final String TABLE_NAME_BOOKS = "books";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_REVIEWS = "reviews";

    private static final String SQL_CREATE_BOOKS_TABLE =
            "CREATE TABLE " + TABLE_NAME_BOOKS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_IMAGE + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_AUTHOR + " TEXT, " +
                    COLUMN_REVIEWS + " TEXT)";

    public BooksDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_BOOKS_TABLE);


        insertInitialBooksIfEmpty(db);
    }

    private void insertInitialBooks(SQLiteDatabase db) {
        Log.d("BooksDBHelper", "Inserting initial books");
        insertBook(db, "Book Title ", "image_url_1", "Description 1", "Author 1", "Reviews 1");
        insertBook(db, "Book Title 2", "image_url_2", "Description 2", "Author 2", "Reviews 2");
        insertBook(db, "Book Title 3", "image_url_3", "Description 3", "Author 3", "Reviews 3");
        insertBook(db,
                "Cat timp infloresc lamaii",
                "android.resource://com.example.bookrent/drawable/cat_timp_infloresc_lamaii",
                "Description 1",
                "Author 1",
                "Reviews 1"
        );
    }

    private void insertInitialBooksIfEmpty(SQLiteDatabase db) {
        Cursor cursor = db.query(TABLE_NAME_BOOKS, null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() == 0) {
            insertInitialBooks(db);
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("BooksDBHelper", "Upgrading database from version " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_BOOKS);
        onCreate(db);
    }

    public void insertBook(SQLiteDatabase db, String title, String image, String description, String author, String reviews) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_IMAGE, image);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_AUTHOR, author);
        values.put(COLUMN_REVIEWS, reviews);

        db.insert(TABLE_NAME_BOOKS, null, values);
    }


    public void insertBook(String title, String image, String description, String author, String reviews) {
        SQLiteDatabase db = this.getWritableDatabase();
        insertBook(db, title, image, description, author, reviews);
        db.close();
    }

    public ArrayList<Book> getAllBooks() {
        ArrayList<Book> booksList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_BOOKS, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
                String image = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
                String author = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUTHOR));
                String reviews = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REVIEWS));

                booksList.add(new Book(id, title, image, description, author, reviews));
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();

        return booksList;
    }

    public boolean eraseBook(String bookName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_TITLE + " LIKE ?";
        String[] selectionArgs = { bookName };

        db.delete(TABLE_NAME_BOOKS, selection, selectionArgs);
        db.close();
        return true;
    }
}