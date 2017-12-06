package com.android.bentabasura.benta_basura.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.android.bentabasura.benta_basura.R;

/**
 * Created by Lowe on 12/4/2017.
 */

public class Admin_Navigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Menu navMenu;
    private Intent manageUsers,manageNews,manageCraftTrash,manageTips;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_navigation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navMenu = navigationView.getMenu();
        navigationView.setNavigationItemSelectedListener(this);

        manageUsers = new Intent(this,Admin_ManageUsers.class);
        manageNews = new Intent(this,Admin_ManageNews.class);
        manageTips = new Intent(this,Admin_ManageTips.class);
        manageCraftTrash = new Intent(this,Admin_ManageTrashCraft.class);
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.manage:
                if(navMenu.findItem(R.id.manage).getTitle().equals("Manage                                        +")) {
                    navMenu.findItem(R.id.manage_crafted_raw).setVisible(true);
                    navMenu.findItem(R.id.manage_users).setVisible(true);
                    navMenu.findItem(R.id.manage_news).setVisible(true);
                    navMenu.findItem(R.id.manage_tips).setVisible(true);
                    navMenu.findItem(R.id.manage).setTitle("Manage                                        -");
                }
                else{
                    navMenu.findItem(R.id.manage_crafted_raw).setVisible(false);
                    navMenu.findItem(R.id.manage_users).setVisible(false);
                    navMenu.findItem(R.id.manage_news).setVisible(false);
                    navMenu.findItem(R.id.manage_tips).setVisible(false);
                    navMenu.findItem(R.id.manage).setTitle("Manage                                        +");
                }
                break;
            case R.id.manage_users:
                startActivity(manageUsers);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.manage_crafted_raw:
                startActivity(manageCraftTrash);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.manage_news:
                startActivity(manageNews);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.manage_tips:
                startActivity(manageTips);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.logout:
                //logout();
                break;
        }
        return true;
    }
}
