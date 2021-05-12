package com.example.pxisjavaver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class PxProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView navHeadName, navHeadEmail;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_px_profile);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navHeadName = (TextView) headerView.findViewById(R.id.NavHeadName);
        navHeadEmail = (TextView) headerView.findViewById(R.id.NavHeadEmail);

        String fullName = SharedPrefManager.getInstance(this).getKeyPxuFname() + " "
                + SharedPrefManager.getInstance(this).getKeyPxuMname() + " "
                + SharedPrefManager.getInstance(this).getKeyPxuLname();
        navHeadName.setText(fullName);
        navHeadEmail.setText(SharedPrefManager.getInstance(this).getKeyPxuEmail());
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new pxProfFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_profile);
        }



//        FNameTV = (TextView) findViewById(R.id.PFNamTxtV);
//        MNameTV = (TextView) findViewById(R.id.PMNamTxtV);
//        LNameTV = (TextView) findViewById(R.id.PLNameTxtV);
//        GenderTV = (TextView) findViewById(R.id.PGendTxtV);
//        BDateTV = (TextView) findViewById(R.id.PBDateTxtV);
//        AddTV = (TextView) findViewById(R.id.PAddTxtV);
//        EmailTV = (TextView) findViewById(R.id.PEmailTxtV);
//
//        FNameTV.setText(SharedPrefManager.getInstance(this).getKeyPxuFname());
//        MNameTV.setText(SharedPrefManager.getInstance(this).getKeyPxuMname());
//        LNameTV .setText(SharedPrefManager.getInstance(this).getKeyPxuLname());
//        GenderTV.setText(SharedPrefManager.getInstance(this).getKeyPxuGender());
//        BDateTV.setText(SharedPrefManager.getInstance(this).getKeyPxuBdate());
//        AddTV.setText(SharedPrefManager.getInstance(this).getKeyPxuAddress());
//        EmailTV.setText(SharedPrefManager.getInstance(this).getKeyPxuEmail());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new pxProfFragment()).commit();
                break;
            case R.id.nav_medications:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new pxMedsFragment()).commit();
                break;
            case R.id.nav_CheckupHistory:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new pxCheckupFragment()).commit();
                break;
            case R.id.nav_appointments:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new pxAptmtFragment()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                            PxProfileActivity.this.finish();
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
}
