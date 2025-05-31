package com.example.theaterchatappnew2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_DARK_THEME = "dark_theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load theme before super.onCreate
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean darkTheme = prefs.getBoolean(KEY_DARK_THEME, false);
        AppCompatDelegate.setDefaultNightMode(
                darkTheme ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // At the top of onCreate() after setContentView(...)
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            startActivity(new Intent(this, HelpActivity.class));
            finish();
        });

        EditText usernameEditText = findViewById(R.id.usernameEditText);
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> {
            String newUsername = usernameEditText.getText().toString();
            prefs.edit().putString(KEY_USERNAME, newUsername).apply();
            Intent intent = new Intent(this, HelpActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });


        Switch themeSwitch = findViewById(R.id.themeSwitch);

        // Load saved username and theme
        usernameEditText.setText(prefs.getString(KEY_USERNAME, ""));
        themeSwitch.setChecked(darkTheme);

        // Save username on change
        usernameEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                prefs.edit().putString(KEY_USERNAME, usernameEditText.getText().toString()).apply();
            }
        });

        // Save theme and apply on switch
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean(KEY_DARK_THEME, isChecked).apply();
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finishAffinity(); // closes all activities
        });


    }


}