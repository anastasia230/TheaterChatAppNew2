package com.example.theaterchatappnew2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class HelpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Button termsBtn = findViewById(R.id.termsButton);
        Button websiteBtn = findViewById(R.id.websiteButton);
        Button settingsBtn = findViewById(R.id.settingsButton);

        termsBtn.setOnClickListener(v -> {
            // Navigate to Terms & Conditions activity or webpage
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://example.com/terms")));
        });

        websiteBtn.setOnClickListener(v -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.n-t.gr/")));
        });

        settingsBtn.setOnClickListener(v -> {
            // Optional: open a settings activity
        });
    }
}
