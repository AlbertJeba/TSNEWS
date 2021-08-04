package com.example.tsnews.ui.home;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tsnews.R;
import com.example.tsnews.databinding.FragmentHomeBinding;
import com.example.tsnews.model;
import com.example.tsnews.myadapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shashank.sony.fancytoastlib.FancyToast;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    RecyclerView recview;
    myadapter adapter;
    DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle mDrawerToggle;

    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

       /* final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        //FB Reference
        FirebaseRecyclerOptions<model> options =
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("news").orderByChild("tsid").limitToLast(100000), model.class)
                        .build();
        //end of FB Reference
        // Add the following lines to create RecyclerView
        //getting recycler view and adapter
        recview = root.findViewById(R.id.recview);
        recview.setHasFixedSize(true);
        recview.setLayoutManager(new LinearLayoutManager(root.getContext()));
        adapter = new myadapter(options);
        recview.setAdapter(adapter);
        //end of getting recycler view and adapter
        //swipeRefreshLayout
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isNetworkAvailable()) {

                    FancyToast.makeText(getContext(), "No Internet Connection", FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();

                } else if (isNetworkAvailable()) {

                    //Toast.makeText(MainActivity.this,"Welcome", Toast.LENGTH_LONG).show();
                }
                //internet check end

                // This method performs the actual data-refresh operation.
                // The method calls setRefreshing(false) when it's finished.
                // myUpdateOperation();
                binding.swipeRefreshLayout.setRefreshing(false);
            }
        });
        //end of swipeRefreshLayout
        return root;

    }

    //check network connection
    public boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {


            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {

                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {

                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {

                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}