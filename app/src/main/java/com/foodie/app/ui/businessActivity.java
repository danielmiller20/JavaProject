package com.foodie.app.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.foodie.app.R;
import com.foodie.app.entities.Business;
import com.foodie.app.ui.view_adapters.BusinessRecyclerViewAdapter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class BusinessActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    BusinessRecyclerViewAdapter businessRecyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.setTitle("");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        List<Business> businessList = loadDemoData();


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.businessRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        businessRecyclerViewAdapter = new BusinessRecyclerViewAdapter(businessList,getApplicationContext());

        recyclerView.setAdapter(businessRecyclerViewAdapter);

        //businessRecyclerViewAdapter.loadNewData(businessList);

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
        getMenuInflater().inflate(R.menu.business, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        //inflater.inflate(R.menu.actions, popup.getMenu());
        popup.show();
    }

    public List<Business> loadDemoData(){

        List<Business> businessList = new ArrayList<>();
        Business demo;


        try {

            String name1 = "Burgeranch ";

            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.burgeranch_logo);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] logo1 = stream.toByteArray();

            demo = new Business(name1,"Derech Agudat Sport Beitar 1, Jerusalem, 9695235","0543051733","Burgeranch@burgeranch.co.il","burgeranch.co.il",1,logo1);
            businessList.add(demo);

            String name2 = "McDonald's ";

            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.mcdonalds_logo);
            stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] logo2 = stream.toByteArray();

            demo = new Business(name2,"Sderot Yitshak Rabin 10, Jerusalem, 1234558","0543051733","McDonald@mcdonald.com","mcdonald.com",2,logo2);
            businessList.add(demo);

            String name3 = "Duda Lapizza ";

            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.duda_lapizza_logo);
            stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] logo3 = stream.toByteArray();

            demo = new Business(name3,"Sderot Hatsvi 5, Jerusalem, 6546185","0543051733","duda@lapizza.com","duda-lapizza.com",3,logo3);
            businessList.add(demo);


            String name4 = "Pizza Hut ";

            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pizza_hut_logo);
            stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] logo4 = stream.toByteArray();

            demo = new Business(name4,"Nayot 9, Jerusalem, 6546185","0543051733","pizza@pizza-hut.com","pizza-hut.com",4,logo4);
            businessList.add(demo);


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }
        return businessList;
    }

}
