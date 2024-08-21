package com.example.bookrent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyDataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Signup.db";
    private static final int DATABASE_VERSION = 3;

    private static final String TABLE_USERS = "allusers";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_CART = "cartBooksId";
    private static final String COLUMN_BOOKS_OWNED = "BooksId";
    private static final String COLUMN_PROFILE_PIC = "profilePic";

    public MyDataBase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_AMOUNT + " REAL, " +
                COLUMN_CART + " TEXT, " +
                COLUMN_BOOKS_OWNED + " TEXT, " +
                COLUMN_PROFILE_PIC + " TEXT" +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {  // If the old version is less than 3, add the profilePic column
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_PROFILE_PIC + " TEXT");
        }
    }

    public void insertData(String email, String password) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_EMAIL, email);
            contentValues.put(COLUMN_PASSWORD, password);
            contentValues.put(COLUMN_AMOUNT, 0.0);
            contentValues.put(COLUMN_CART,"");
            contentValues.put(COLUMN_BOOKS_OWNED,"");
            contentValues.put(COLUMN_PROFILE_PIC, "");

            db.insert(TABLE_USERS, null, contentValues);
        }
    }

    public boolean insertLast(String UserId, String bookId) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues contentValues = new ContentValues();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE ID = ?", new String[]{UserId});
            String currList = "";
            boolean found = false;
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                currList = cursor.getString(4);
                String[] tokens = currList.trim().split("\\s+");
                for (String token : tokens) {
                    if (token.equals(bookId))
                        found = true;
                }
                if (!found) {
                    contentValues.put(COLUMN_CART, currList + " " + bookId);
                } else {
                    contentValues.put(COLUMN_CART, currList);
                }
            } else {
                contentValues.put(COLUMN_CART, bookId);
            }
            db.update(TABLE_USERS, contentValues, "ID = ?", new String[]{UserId});
            return found;
        }
    }

    public void clearCart(String UserId) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_CART, "");
            db.update(TABLE_USERS, contentValues, "ID = ?", new String[]{UserId});
        }
    }

    public void removeFromCart(String UserId, int index) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues contentValues = new ContentValues();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE ID = ?", new String[]{UserId});
            cursor.moveToFirst();
            String currList = cursor.getString(4);
            if (!currList.isEmpty()) {
                String[] tokenList = currList.trim().split("\\s+");
                if (tokenList.length == 1) {
                    currList = "";
                } else {
                    StringBuilder aux = new StringBuilder();
                    for (int i = 0; i < tokenList.length; i++) {
                        if (i == index) continue;
                        aux.append(" ").append(tokenList[i]);
                    }
                    currList = aux.toString();
                }
                contentValues.put(COLUMN_CART, currList);
                db.update(TABLE_USERS, contentValues, "ID = ?", new String[]{UserId});
            }
        }
    }

    public String[] getBooksOwned(String UserId) {
        try (SQLiteDatabase db = this.getReadableDatabase()) {
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE ID = ?", new String[]{UserId});
            cursor.moveToFirst();
            String data = cursor.getString(5);
            return data.trim().split("\\s+");
        } catch (Exception e) {
            return new String[]{"not found!"};
        }
    }

    public boolean addBooksOwned(String UserId, ArrayList<Book> cart) {
        String[] booksAlreadyOwned = getBooksOwned(UserId);
        StringBuilder s = new StringBuilder();
        for (String bookId : booksAlreadyOwned) s.append(" ").append(bookId);

        try {
            for (Book book : cart) {
                s.append(" ").append(book.getId());
            }
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_BOOKS_OWNED, s.toString());
            db.update(TABLE_USERS, contentValues, "ID = ?", new String[]{UserId});
            return true;
        } catch (Exception e) {
            Log.e("bookError", e.getMessage());
            return false;
        }
    }

    public String[] getList(String UserId) {
        try (SQLiteDatabase db = this.getReadableDatabase()) {
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE ID = ?", new String[]{UserId});
            cursor.moveToFirst();
            String data = cursor.getString(4);
            return data.trim().split("\\s+");
        } catch (Exception e) {
            return new String[]{"not found!"};
        }
    }

    public boolean checkEmail(String email) {
        try (SQLiteDatabase db = this.getReadableDatabase()) {
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email});
            return cursor.getCount() > 0;
        }
    }

    public void updateProfilePicUri(String userId, String uri) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_PROFILE_PIC, uri);

            int rowsUpdated = db.update(TABLE_USERS, contentValues, "ID = ?", new String[]{userId});

            if (rowsUpdated == 0) {
                Log.e("Database", "No rows updated, check if the user ID exists.");
            }
        }
    }

    public String getProfilePicUri(String userId) {
        try (SQLiteDatabase db = this.getReadableDatabase()) {
            Cursor cursor = db.rawQuery("SELECT " + COLUMN_PROFILE_PIC + " FROM " + TABLE_USERS + " WHERE ID = ?", new String[]{userId});
            if (cursor.moveToFirst()) {
                return cursor.getString(0);
            } else {
                return null;
            }
        }
    }

    public User checkEmailPassword(String email, String password) {
        try (SQLiteDatabase db = this.getReadableDatabase()) {
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?", new String[]{email, password});
            User data = new User();
            data.setId(-1);
            if (cursor.moveToFirst()) {
                data.setId(cursor.getInt(0));
                data.setEmail(cursor.getString(1));
                data.setAmount(cursor.getFloat(3));
            }
            return data;
        }
    }

    public void updateMoney(String id, float amount) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_AMOUNT, amount);
            db.update(TABLE_USERS, contentValues, "ID = ?", new String[]{id});
        }
    }

    public boolean updateData(String email, String password) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_PASSWORD, password);
            int result = db.update(TABLE_USERS, contentValues, COLUMN_EMAIL + " = ?", new String[]{email});
            return result > 0;
        }
    }
}
