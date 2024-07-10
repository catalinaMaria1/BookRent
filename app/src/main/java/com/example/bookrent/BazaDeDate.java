package com.example.bookrent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.EditText;
import android.widget.Button;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class BazaDeDate extends SQLiteOpenHelper {
    EditText email, password;
    Button login, register;

    public BazaDeDate(@Nullable Context context) {
        super(context, "Signup.db", null ,1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDatabase) {
        MyDatabase.execSQL("create Table allusers(email TEXT primary key, password TEXT)");

    }
    @Override
    public void onUpgrade(SQLiteDatabase MyDatabase, int i, int i1){
MyDatabase.execSQL("drop Table if exists allusers");
        }
    public Boolean insertData(String email, String password){
        SQLiteDatabase MyDatabase =this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        long result=MyDatabase.insert("allusers", null,contentValues);
            if(result==-1)
                {return false;}
            else
                {return true;}

        }
        public Boolean checkEmail(String email)
        {
            SQLiteDatabase MyDatabase = this.getWritableDatabase();
            Cursor cursor= MyDatabase.rawQuery("Select * from allusers where email = ?", new String[]{email});
            if (cursor.getCount() >0){return true;}
            else {return false;}

        }
        public Boolean checkEmailPassword( String email, String password)
        { SQLiteDatabase MyDatabase = this.getWritableDatabase();
            Cursor cursor= MyDatabase.rawQuery("Select * from allusers where email = ? and password = ?", new String[]{email, password});
            if(cursor.getCount()>0)
            {return true;}
            else
            {return false;}


        }
}