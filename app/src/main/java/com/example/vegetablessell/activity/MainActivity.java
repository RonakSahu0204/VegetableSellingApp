package com.example.vegetablessell.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vegetablessell.R;
import com.example.vegetablessell.fragment.UserHomeFragment;
import com.example.vegetablessell.utils.SessionManagement;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;

    private TextView mHeaderUserName, mHeaderUserEmail;

    private SessionManagement session;

    String accountType = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = new SessionManagement(this);
        toolbar = findViewById(R.id.toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        View header = navigationView.getHeaderView(0);

        mHeaderUserName = header.findViewById(R.id.headerUserName);
        mHeaderUserEmail = header.findViewById(R.id.headerUserEmail);

        try {
            JSONObject userdata = new JSONObject(session.getSession());

            mHeaderUserName.setText(userdata.getString("username"));
            mHeaderUserEmail.setText(userdata.getString("email"));
            accountType = userdata.getString("accountType");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if("customer".equalsIgnoreCase(accountType)) {

            UserHomeFragment userHomeFragment = new UserHomeFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.xyz, userHomeFragment).commit();

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        if(null == session.getSession() || "" == session.getSession()) {
            navigationView.getMenu().findItem(R.id.logout).setVisible(false);
        }

        try {
            JSONObject userdata = new JSONObject(session.getSession());
            accountType = userdata.getString("accountType");

            if("customer".equals(accountType)) {
                navigationView.getMenu().findItem(R.id.overall_invoice).setVisible(false);
                navigationView.getMenu().findItem(R.id.localitywise).setVisible(false);
                navigationView.getMenu().findItem(R.id.userwise).setVisible(false);
                navigationView.getMenu().findItem(R.id.overall_invoice).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_log).setVisible(false);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if(id == R.id.logout) {
            session.closeSession();
            Toast.makeText(this, "Logged out successfully!!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}