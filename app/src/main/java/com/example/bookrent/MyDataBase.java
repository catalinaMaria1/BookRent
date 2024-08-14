package com.example.bookrent;

import static android.app.PendingIntent.getActivity;
import static com.example.bookrent.AESCrypt.encrypt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyDataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Signup.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_USERS = "allusers";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_AMOUNT= "amount";

    private static final String TABLE_CART_BOOKS = "cart_books";
    private static final String COLUMN_BOOK_ID = "book_id";
    private static final String COLUMN_BOOK_TITLE = "title";
    private static final String COLUMN_BOOK_AUTHOR = "author";
    private static final String COLUMN_BOOK_IMAGE = "image";
    private static final String COLUMN_BOOK_DESCRIPTION = "description";
    private static final String COLUMN_BOOK_REVIEWS = "reviews";
    private static final String COLUMN_BOOK_PRICE = "price";

    public MyDataBase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                "ID" + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, "+
                COLUMN_AMOUNT + " REAL)"
        );

        db.execSQL("CREATE TABLE " + TABLE_CART_BOOKS + " (" +
                COLUMN_BOOK_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_BOOK_TITLE + " TEXT, " +
                COLUMN_BOOK_AUTHOR + " TEXT, " +
                COLUMN_BOOK_IMAGE + " TEXT, " +
                COLUMN_BOOK_DESCRIPTION + " TEXT, " +
                COLUMN_BOOK_REVIEWS + " TEXT, " +
                COLUMN_BOOK_PRICE + " REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("CREATE TABLE " + TABLE_CART_BOOKS + " (" +
                    COLUMN_BOOK_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_BOOK_TITLE + " TEXT, " +
                    COLUMN_BOOK_AUTHOR + " TEXT, " +
                    COLUMN_BOOK_IMAGE + " TEXT, " +
                    COLUMN_BOOK_DESCRIPTION + " TEXT, " +
                    COLUMN_BOOK_REVIEWS + " TEXT, " +
                    COLUMN_BOOK_PRICE + " REAL)");
        }
    }

    public User insertData(String email, String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        try {
            password = encrypt(password);
        } catch (Exception e) {
            throw new RuntimeException("Encryption error", e);
        }
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_EMAIL, email);
            contentValues.put(COLUMN_PASSWORD, password);
            contentValues.put(COLUMN_AMOUNT, 0.0);
            long result = db.insert(TABLE_USERS, null, contentValues);
            User data=new User();
            if(result != -1){
                Cursor cursor=db.rawQuery("SELECT * FROM "+ TABLE_USERS+ " WHERE "+ COLUMN_EMAIL + " = ?", new String[]{email});
                if(cursor.moveToFirst()){
                    data.setId(cursor.getInt(0));
                    data.setEmail(cursor.getString(1));
                    data.setAmount(cursor.getFloat(3));
                }

            }
            return data;
        }
    }

    public boolean checkEmail(String email) {
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email})) {
            return cursor.getCount() > 0;
        }
    }

    public User checkEmailPassword(String email, @NonNull String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        try {
            password = encrypt(password);
        } catch (Exception e) {
            throw new RuntimeException("Encryption error", e);
        }
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?", new String[]{email, password})) {
            User data=new User();
            data.setId(-1);
            if(cursor.moveToFirst()){
                Log.d("CURSOR",cursor.toString());
                data.setId(cursor.getInt(0));
                data.setEmail(cursor.getString(1));
                data.setAmount(cursor.getFloat(3));
                Log.d("CURSORUL",data.toString());
            }

            return data;
        }
    }
    public void updateMoney(String id, float amount){
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_AMOUNT, amount);

            db.update(TABLE_USERS, contentValues, "ID = ?",new String[]{(id)});
        }

    }

    public boolean updateData(String email, String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        try {
            password = encrypt(password);
        } catch (Exception e) {
            throw new RuntimeException("Encryption error", e);
        }
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_PASSWORD, password);
            int result = db.update(TABLE_USERS, contentValues, COLUMN_EMAIL + " = ?", new String[]{email});
            return result > 0;
        }
    }
    public void addBookToCart(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_BOOK_ID, book.getId());
        contentValues.put(COLUMN_BOOK_TITLE, book.getTitle());
        contentValues.put(COLUMN_BOOK_AUTHOR, book.getAuthor());
        contentValues.put(COLUMN_BOOK_IMAGE, book.getImage());
        contentValues.put(COLUMN_BOOK_DESCRIPTION, book.getDescription());
        contentValues.put(COLUMN_BOOK_REVIEWS, book.getReviews());
        contentValues.put(COLUMN_BOOK_PRICE, book.getPrice());

        db.insert(TABLE_CART_BOOKS, null, contentValues);
        db.close();
    }
}