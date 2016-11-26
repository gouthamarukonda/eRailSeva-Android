package com.example.avinash.erailseva;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    static final String url = "https://erailseva.herokuapp.com";
    //This is our tablayout
    private TabLayout tabLayout1;

    //This is our viewPager
    private ViewPager viewPager1;

    static final String COOKIES_HEADER = "Set-Cookie";
    static CookieManager msCookieManager = new CookieManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        //Initializing the tablayout
        tabLayout1 = (TabLayout) findViewById(R.id.tabLayout1);

        //Adding the tabs using addTab() method
        tabLayout1.addTab(tabLayout1.newTab().setText("LOG IN"));
        tabLayout1.addTab(tabLayout1.newTab().setText("SIGN UP"));
        tabLayout1.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager1 = (ViewPager) findViewById(R.id.pager1);

        //Creating our pager adapter
        Pager1 adapter = new Pager1(getSupportFragmentManager(), tabLayout1.getTabCount());

        //Adding adapter to pager
        viewPager1.setAdapter(adapter);
        viewPager1.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout1));

        //Adding onTabSelectedListener to swipe views
        tabLayout1.setOnTabSelectedListener(this);
    }
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager1.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
