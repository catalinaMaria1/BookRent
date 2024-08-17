package com.example.bookrent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;


import java.util.ArrayList;

public class MyDataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Signup.db";
    private static final int DATABASE_VERSION = 2;

    // Tabel utilizatori
    private static final String TABLE_USERS = "allusers";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_CART = "cartBooksId";
    private static final String COLUMN_BOOKS_OWNED = "BooksId";

    public MyDataBase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                "ID" + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_AMOUNT + " REAL, "+
                COLUMN_CART+ " TEXT,"+
                COLUMN_BOOKS_OWNED+ " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public void insertData(String email, String password) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_EMAIL, email);
            contentValues.put(COLUMN_PASSWORD, password);
            contentValues.put(COLUMN_AMOUNT, 0.0);
            contentValues.put(COLUMN_CART,"");
            contentValues.put(COLUMN_BOOKS_OWNED,"");

            db.insert(TABLE_USERS, null, contentValues);

        }
    }


    public boolean insertLast(String UserId,String bookId){
        try(SQLiteDatabase db=this.getWritableDatabase()) {
            ContentValues contentValues = new ContentValues();
            Cursor cursor= db.rawQuery("SELECT * FROM "+ TABLE_USERS + " WHERE ID = ?", new String[]{UserId});
            String currList="";
            boolean found=false;
            if(cursor.getCount()>0 && cursor!=null){cursor.moveToFirst();
                currList=cursor.getString(4);
                String[] tokens=currList.trim().split("\\s+");
                for(int i=0;i<tokens.length;i++){
                    if(tokens[i].equals(bookId))
                        found=true;
                }
                if(!found){
                    contentValues.put(COLUMN_CART,currList+" "+bookId);
                }
                else{
                    contentValues.put(COLUMN_CART,currList);
                }
            }
            else{
                contentValues.put(COLUMN_CART,bookId);
            }
            db.update(TABLE_USERS, contentValues, "ID = ?",new String[]{UserId});
            return found;

        }
    }

    public void clearCart(String UserId){
        try(SQLiteDatabase db=this.getWritableDatabase()) {
            ContentValues contentValues = new ContentValues();
            String currList="";
            contentValues.put(COLUMN_CART,currList);
            db.update(TABLE_USERS, contentValues, "ID + ?",new String[]{UserId});

        }
    }

    public void removeFromCart(String UserId,int index){
        try(SQLiteDatabase db=this.getWritableDatabase()) {
            ContentValues contentValues = new ContentValues();
            Cursor cursor= db.rawQuery("SELECT * FROM "+ TABLE_USERS + " WHERE ID = ?", new String[]{UserId});
            cursor.moveToFirst();
            String currList="";
            currList=cursor.getString(4);
            if(!currList.isEmpty()){
                String[] tokenList=currList.trim().split("\\s+");
                if(tokenList.length==1){
                    currList="";
                }
                else {
                    String aux="";
                    for(int i=0;i<tokenList.length;i++){
                        if(i==index)continue;
                        aux=aux+" "+tokenList[i];
                    }
                    currList=aux;

                }
                contentValues.put(COLUMN_CART,currList);
                db.update(TABLE_USERS, contentValues, "ID + ?",new String[]{UserId});

            }

        }
    }

    public String[] getBooksOwned(String UserId){
        try (SQLiteDatabase db = this.getReadableDatabase()){
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE ID = ?", new String[]{UserId});

            try
            {
                cursor.moveToFirst();
                String data=cursor.getString(5);
                String[] ids=data.trim().split("\\s+");
                return ids;
            }
            catch (Exception e){
                String[] er={"not found!"};
                return er;
            }
        }

    }

    public boolean addBooksOwned(String UserId, ArrayList<Book> cart){
        String[] booksAlreadyOwned=getBooksOwned(UserId);
        String s="";
        for(int i=0;i<booksAlreadyOwned.length;i++){s=s+" "+booksAlreadyOwned[i];}

        try{
            for(Book book:cart){
                s=s+" "+String.valueOf(book.getId());

            }
            SQLiteDatabase db=this.getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put(COLUMN_BOOKS_OWNED,s);
            db.update(TABLE_USERS,contentValues,"ID = ?",new String[]{UserId});
            return true;
        }
        catch (Exception e) {Log.e("bookError",e.getMessage());return false;}

    }

    public String[] getList(String UserId){
        try (SQLiteDatabase db = this.getReadableDatabase()){
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE ID = ?", new String[]{UserId});

            try
            {
                cursor.moveToFirst();
                String data=cursor.getString(4);
                String[] ids=data.trim().split("\\s+");
                return ids;
            }
            catch (Exception e){
                String[] er={"not found!"};
                return er;
            }
        }

    }

    public boolean checkEmail(String email) {
        try (SQLiteDatabase db = this.getReadableDatabase()){
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email});
            return cursor.getCount() > 0;
        }
    }

    public User checkEmailPassword(String email, String password) {
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?", new String[]{email, password})) {
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