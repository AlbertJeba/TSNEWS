package com.example.tsnews.ui.gallery;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.Toast;


import androidx.annotation.NonNull;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tsnews.R;
import com.example.tsnews.databinding.FragmentGalleryBinding;
import com.example.tsnews.model;

import com.example.tsnews.myadapterbookmark;

import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;


public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;
    RecyclerView recview1;
    DatabaseReference databaseReference, fav_item_ref, fav_ref;
    myadapterbookmark adapterbookmark;
    private View mEmptyView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /* final TextView textView = binding.textGallery;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String cuid = user.getUid();
        FirebaseRecyclerOptions<model> options =
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("favourites").child(cuid).orderByChild("tsid").limitToLast(100000), model.class)
                        .build();


        // Add the following lines to create RecyclerView
        recview1 = root.findViewById(R.id.recview_bookmark);
        recview1.setHasFixedSize(true);
        recview1.setLayoutManager(new LinearLayoutManager(root.getContext()));
        adapterbookmark = new myadapterbookmark(options);
        recview1.setAdapter(adapterbookmark);
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        CardView bk = binding.cardBk;
        adapterbookmark.notifyDataSetChanged();
        //Swipe to refresh start
        binding.swipeRefreshLayoutBookmark.setOnRefreshListener(() -> {


            if (!isNetworkAvailable()) {

                FancyToast.makeText(getContext(), "No Internet Connection", FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();

            } else if (isNetworkAvailable()) {

                //Toast.makeText(MainActivity.this,"Welcome", Toast.LENGTH_LONG).show();

            }
            //internet check end
            adapterbookmark.notifyDataSetChanged();
            // This method performs the actual data-refresh operation.
            // The method calls setRefreshing(false) when it's finished.
            // myUpdateOperation();
            binding.swipeRefreshLayoutBookmark.setRefreshing(false);
        });


        mDatabaseRef.child("favourites").child(cuid).orderByChild("tsid").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //Toast.makeText(getContext(), "data exists", Toast.LENGTH_SHORT).show();
                    bk.setVisibility(View.GONE);

                } else {
                    Toast.makeText(getContext(), "No data exists", Toast.LENGTH_SHORT).show();
                    bk.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return root;
    }


    //
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
        adapterbookmark.startListening();
        System.out.println(adapterbookmark.getItemCount());
    }

    @Override
    public void onStop() {
        super.onStop();
        adapterbookmark.stopListening();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}