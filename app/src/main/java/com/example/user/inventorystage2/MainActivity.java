package com.example.user.inventorystage2;

import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.user.inventorystage2.Data.InventoryContract;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //set fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddProductFragment addProductFrag = new AddProductFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, addProductFrag)
                        .addToBackStack(null)
                        .commit();
            }
        });

        //set drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Add product list fragment as the default fragment
        if (savedInstanceState == null) {
            ProductListFragment productListFrag = new ProductListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, productListFrag)
                    .addToBackStack(null)
                    .commit();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            System.exit(0);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_listProduct: {
                ProductListFragment productListFrag = new ProductListFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, productListFrag)
                        .addToBackStack(null)
                        .commit();
                break;
            }

            case R.id.nav_addProduct: {
                AddProductFragment addProductFrag = new AddProductFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, addProductFrag)
                        .addToBackStack(null)
                        .commit();
                break;
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void decrease(long id, int quantity) {
        Uri currentProductUri =
                ContentUris.withAppendedId(InventoryContract.ProductEntry.CONTENT_URI, id);

        if (quantity > 0) {
            quantity = quantity - 1;
            ContentValues values = new ContentValues();
            values.put(InventoryContract.ProductEntry._ID, id);
            values.put(InventoryContract.ProductEntry.QUANTITY, quantity);
            getContentResolver().update(currentProductUri, values, null, null);

        } else {
            Toast.makeText(this, getString(R.string.quantity_cannot_be_negative), Toast.LENGTH_SHORT).show();
        }
    }
}