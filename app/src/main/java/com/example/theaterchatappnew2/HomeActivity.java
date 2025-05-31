package com.example.theaterchatappnew2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Button chatbotButton = findViewById(R.id.chatbotButton);
        chatbotButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
        });
        Button contactButton = findViewById(R.id.contactUsButton);
        contactButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ContactActivity.class);
            startActivity(intent);
        });

        Button helpButton = findViewById(R.id.helpButton);
        helpButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, HelpActivity.class);
            startActivity(intent);
        });

        // In HomeActivity.java, inside onCreate after setContentView(...)
        ImageView infoIcon = findViewById(R.id.infoIcon);
        infoIcon.setOnClickListener(v -> {
            String versionName = "0.1.5";
            try {
                versionName = getPackageManager()
                        .getPackageInfo(getPackageName(), 0).versionName;
            } catch (Exception e) {
                versionName = "N/A";
            }
            String message = "App version: " + versionName + "\nMade by: Theaiter INC.";
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("About")
                    .setMessage(message)
                    .setPositiveButton("OK", null)
                    .show();
        });

        setupBottomNav();

        // TODO: Implement Contact Us
    }
    @Override
    protected String getBottomNavType() {
        return "home";
    }
}
