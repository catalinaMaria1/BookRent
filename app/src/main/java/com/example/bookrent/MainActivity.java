package com.example.bookrent;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Button visitButton;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    public static User user=new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        visitButton = findViewById(R.id.visitButton);
        visitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user !=null && user.getId()==1) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new Admin())
                            .addToBackStack(null)
                            .commit();
                } else {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new HomeFragment())
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        checkLoginStatus();  // Call the method to check login status and navigate accordingly.
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.nav_login:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new LoginFragment())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.nav_register:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SignupFragment())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.nav_account:
                try{
                    if(user.getId() !=-1)
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new AccountFragment())
                                .addToBackStack(null)
                                .commit();
                    else{
                        Toast.makeText(this, "Not Logged In!", Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new LoginFragment())
                                .addToBackStack(null)
                                .commit();
                    }
                }
                catch (Exception e){

                    Toast.makeText(this, "Not Logged In!", Toast.LENGTH_SHORT).show();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new LoginFragment())
                            .addToBackStack(null)
                            .commit();
                }
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void checkLoginStatus() {
        if (user!=null) {
            if (Objects.equals(user.getId(), 1)) {
                // Redirect to the Books1 activity if the user is an admin.
                Intent intent = new Intent(MainActivity.this, Books1.class);
                startActivity(intent);
                finish();
            } else {
                // Redirect to HomeFragment if the user is not an admin.
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment())
                        .addToBackStack(null)
                        .commit();
            }
        }
    }
}