package com.example.pxisjavaver;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;

public class DrProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView navHeadName, navHeadEmail;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dr_profile);
        Toolbar toolbar = findViewById(R.id.dr_toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.dr_nav_view);
        View headerView = navigationView.getHeaderView(0);
        navHeadName = (TextView) headerView.findViewById(R.id.drHeaderName);
        navHeadEmail = (TextView) headerView.findViewById(R.id.drNavHeaderEmail);
        drawer = findViewById(R.id.dr_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        String fullName = SharedPrefManager.getInstance(this).getKeyPxuFname() + " "
                + SharedPrefManager.getInstance(this).getKeyPxuMname() + " "
                + SharedPrefManager.getInstance(this).getKeyPxuLname();
        navHeadName.setText(fullName);
        navHeadEmail.setText(SharedPrefManager.getInstance(this).getKeyPxuEmail());

        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.dr_fragment_container,
                    new drProfFragment()).commit();
            navigationView.setCheckedItem(R.id.dr_nav_profile);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            DrProfileActivity.this.finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuLogout:
                SharedPrefManager.getInstance(this).logout();
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.dr_nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.dr_fragment_container,
                        new drProfFragment()).commit();
                break;
            case R.id.dr_nav_appointments:
                getSupportFragmentManager().beginTransaction().replace(R.id.dr_fragment_container,
                        new drAptmtFRagment()).commit();
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
