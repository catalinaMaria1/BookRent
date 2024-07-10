package com.example.bookrent;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BooksDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "books.db";
    private static final int DATABASE_VERSION = 2; // Update version to force database upgrade

    // Books table
    public static final String TABLE_NAME_BOOKS = "books";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_REVIEWS = "reviews";

    // Categories table
    public static final String TABLE_NAME_CATEGORIES = "categories";
    public static final String COLUMN_CATEGORY_ID = "_id";
    public static final String COLUMN_CATEGORY_NAME = "category";

    private static final String SQL_CREATE_BOOKS_TABLE =
            "CREATE TABLE " + TABLE_NAME_BOOKS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_IMAGE + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_AUTHOR + " TEXT, " +
                    COLUMN_REVIEWS + " TEXT)";

    private static final String SQL_CREATE_CATEGORIES_TABLE =
            "CREATE TABLE " + TABLE_NAME_CATEGORIES + " (" +
                    COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CATEGORY_NAME + " TEXT)";

    public BooksDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_BOOKS_TABLE);
        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_BOOKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CATEGORIES);
        onCreate(db);
    }

    // Method to insert a book into the database
    public void insertBook(String title, String image, String description, String author, String reviews) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_IMAGE, image);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_AUTHOR, author);
        values.put(COLUMN_REVIEWS, reviews);

        db.insert(TABLE_NAME_BOOKS, null, values);
        db.close();
    }

    // Method to insert a category into the database
    public void insertCategory(String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, category);

        db.insert(TABLE_NAME_CATEGORIES, null, values);
        db.close();
    }
}
