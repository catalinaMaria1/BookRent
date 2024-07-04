package com.example.bookrent;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.EditText;
import android. widget.Button;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class BazaDeDate extends SQLiteOpenHelper {
    EditText email, password;
    Button login, register;

    public BazaDeDate(@Nullable Context context) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1){

        /*
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_page);
        email=(EditText) findViewById(R.id.emailEditText);*/
    }

}