// ContactActivity.java
package com.example.theaterchatappnew2;

import android.os.Bundle;

public class ContactActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        setupBottomNav();
    }

    @Override
    protected String getBottomNavType() {
        return "";  // ή "home" αν θες να χρωματιστεί
    }
}
