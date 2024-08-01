package com.example.bookrent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button visitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        visitButton = findViewById(R.id.visitButton);

        visitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Home.class);
                startActivity(intent);
            }
        });

        checkLoginStatus();
    }

    private void checkLoginStatus() {
        boolean isLoggedIn = false;
        if (isLoggedIn) {
            Intent intent = new Intent(MainActivity.this, Books1.class);
            startActivity(intent);
            finish();
        }
    }
}
