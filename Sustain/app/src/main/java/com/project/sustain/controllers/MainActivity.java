package com.project.sustain.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.sustain.R;


public class MainActivity extends AppCompatActivity {
    private Button btnLogout;
    private FirebaseAuth auth;
    private FirebaseUser mUser;
    private Toolbar mToolbar;
    public static final int PROFILE_CHANGE_REQ = 1000;

    private Button subWtrRep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        mUser = auth.getCurrentUser();

        setContentView(R.layout.activity_main);

        //add Toolbar as ActionBar with menu
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        this.setSupportActionBar(mToolbar);

        if (mUser != null) {

            if (mUser.getDisplayName() != null) {
                setToolbarTitle(mUser.getDisplayName());
            } else {
                setToolbarTitle(mUser.getEmail());
            }
        }

        btnLogout = (Button) findViewById(R.id.buttonLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        subWtrRep = (Button) findViewById(R.id.subRep);

        subWtrRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, WaterRptSubmitActivity.class), 5000);
            }
        });
    }

    /**
     * Sets the text in the toolbar to the text provided.,
     * @param name The name to put into the toolbar title.
     */
    private void setToolbarTitle(String name) {
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle(name);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_profile:
                // User chose the "Edit Profile" action, show the user profile settings UI...
                startActivityForResult(new Intent(MainActivity.this, EditProfileActivity.class),
                        PROFILE_CHANGE_REQ);
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PROFILE_CHANGE_REQ) {
            if (resultCode == RESULT_OK) {
                Log.d("EditResult", "Got result OK");
                setToolbarTitle(data.getStringExtra("displayName"));
            }
        }
    }
}