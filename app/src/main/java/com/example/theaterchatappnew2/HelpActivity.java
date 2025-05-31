package com.example.theaterchatappnew2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

public class HelpActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Button termsBtn = findViewById(R.id.termsButton);
        Button websiteBtn = findViewById(R.id.websiteButton);
        Button settingsBtn = findViewById(R.id.settingsButton);

        termsBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, TermsActivity.class));
        });

        websiteBtn.setOnClickListener(v -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.n-t.gr/")));
        });

        settingsBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
        });


        setupBottomNav();
    }
    @Override
    protected String getBottomNavType() {
        return "home";
    }
}
