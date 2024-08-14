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
    private static final int DATABASE_VERSION = 5;

    public static final String TABLE_NAME_BOOKS = "books";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_REVIEWS = "reviews";
    public static final String COLUMN_IN_CART = "in_cart";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_CATEGORY = "category";

    private static final String SQL_CREATE_BOOKS_TABLE =
            "CREATE TABLE " + TABLE_NAME_BOOKS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT," +
                    COLUMN_IMAGE + " TEXT," +
                    COLUMN_DESCRIPTION + " TEXT," +
                    COLUMN_AUTHOR + " TEXT," +
                    COLUMN_REVIEWS + " TEXT," +
                    COLUMN_IN_CART + " INTEGER DEFAULT 0," +
                    COLUMN_PRICE + " REAL DEFAULT 0," +
                    COLUMN_CATEGORY + " TEXT" + ");";

    public BooksDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_BOOKS_TABLE);
        insertInitialBooksIfEmpty(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("BooksDBHelper", "Upgrading database from version " + oldVersion + " to " + newVersion);
        if (oldVersion < 5) {
            db.execSQL("ALTER TABLE " + TABLE_NAME_BOOKS + " ADD COLUMN " + COLUMN_PRICE + " REAL DEFAULT 0");
        }

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

    private void insertInitialBooks(SQLiteDatabase db) {
        Log.d("BooksDBHelper", "Inserting initial books");
        insertBook(db, "Book Title 1", "image_url_1", "Description 1", "Author 1", "Reviews 1", 10.0, "Category");
        insertBook(db, "Wilder", "android.resource://com.example.bookrent/drawable/wilder", "Rebecca Yarros, autoare de bestsellere #1 New York Times și a fenomenului Fourth Wing, invită cititorii într-o croazieră tumultuoasă de nouă luni, pe parcursul căreia protagoniștii se luptă din răsputeri să nu-i ia valul.", "Rebecca Yarros", "5/5", 39.99,"Fiction");
        insertBook(db, "Twisted Hate", "android.resource://com.example.bookrent/drawable/twisted", "Superb si arogant, Josh Chen se pregateste sa devina medic. Pana acum, n-a intalnit nicio fata care sa-i reziste — cu exceptia afurisitei de Jules Ambrose. Inca de cand s-au cunoscut, frumoasa roscata i-a fost ca un ghimpe in coasta, dar in acelasi timp Jules il obsedeaza asa cum nimeni alta n-a reusit.", "Ana Huang", "4/5", 37.45,"Romance");
        insertBook(db, "Cat timp infloresc lamaii", "android.resource://com.example.bookrent/drawable/cat_timp_infloresc_lamaii", "Description 1", "Author 1", "Reviews 1", 25.0,"Category");
    }

    public void insertBook(SQLiteDatabase db, String title, String image, String description, String author, String reviews, double price, String category) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_IMAGE, image);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_AUTHOR, author);
        values.put(COLUMN_REVIEWS, reviews);
        values.put(COLUMN_IN_CART, 0);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_CATEGORY,category);
        db.insert(TABLE_NAME_BOOKS, null, values);
    }

    public void insertBook(String title, String image, String description, String author, String reviews, double price, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        insertBook(db, title, image, description, author, reviews, price,category);
    }

    public void addBookToCart(int bookId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IN_CART, 1); // Mark as in cart
        db.update(TABLE_NAME_BOOKS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(bookId)});
    }

    public void removeBookFromCart(int bookId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IN_CART, 0); // Mark as not in cart
        db.update(TABLE_NAME_BOOKS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(bookId)});
    }

    public ArrayList<Book> getBooksInCart() {
        ArrayList<Book> booksList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_BOOKS, null, COLUMN_IN_CART + " = ?", new String[]{"1"}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
                String image = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
                String author = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUTHOR));
                String reviews = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REVIEWS));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE));
                String category=cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY));

                booksList.add(new Book(id, title, image, description, author, reviews, price,category));
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }


        return booksList;
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
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE));
                String category=cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY));

                booksList.add(new Book(id, title, image, description, author, reviews, price,category));
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }


        return booksList;
    }

    public boolean eraseBook(String bookName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_TITLE + " LIKE ?";
        String[] selectionArgs = { bookName };

        db.delete(TABLE_NAME_BOOKS, selection, selectionArgs);
        return true;
    }
}