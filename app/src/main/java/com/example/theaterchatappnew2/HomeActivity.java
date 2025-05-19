package com.example.theaterchatappnew2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

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

        setupBottomNav();

        // TODO: Implement Contact Us
    }
    @Override
    protected String getBottomNavType() {
        return "home";
    }
}
