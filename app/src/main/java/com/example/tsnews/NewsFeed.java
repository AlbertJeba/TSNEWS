package com.example.tsnews;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tsnews.databinding.ActivityNewsFeedBinding;
import com.google.firebase.auth.FirebaseAuth;

public class NewsFeed extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNewsFeedBinding binding;
    ImageView profile_photo;
    TextView u_name,u_email;
    Button logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewsFeedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarNewsFeed.toolbar);
        //Stat-off Profile details from Google Sign-in
        NavigationView navigationView = binding.navView;
        profile_photo=navigationView.getHeaderView(0).findViewById(R.id.user_profile_photo);
        u_name=navigationView.getHeaderView(0).findViewById(R.id.user_name);
        u_email=navigationView.getHeaderView(0).findViewById(R.id.user_email_id);
        //logout=navigationView.getMenu().findItem(R.id.Logout);
        navigationView.getMenu().findItem(R.id.Logout).setOnMenuItemClickListener(menuItem -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),SignInActivity.class));
            // [START auth_fui_signout]
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // ...
                        }
                    });
            finish();
            return true;
        });
        GoogleSignInAccount account= GoogleSignIn.getLastSignedInAccount(this);
        u_name.setText(account.getDisplayName());
        u_email.setText(account.getEmail());
       Glide.with(this).load(account.getPhotoUrl()).into(profile_photo);
       /* logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),SignInActivity.class));
                finish();
            }
        });
8?
        //End-off Profile details from Google Sign-in
        /*binding.appBarNewsFeed.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = binding.drawerLayout;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_news_feed);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.news_feed, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_news_feed);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}