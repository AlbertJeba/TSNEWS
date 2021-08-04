package com.example.tsnews.ui.slideshow;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.tsnews.R;
import com.example.tsnews.databinding.FragmentSlideshowBinding;
import com.example.tsnews.model;
import com.example.tsnews.no_internet;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;
    Button send;
    TextInputEditText name, email, message;
    String current_user;
    GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button button = root.findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDetail();
            }
        });

       // name = root.findViewById(R.id.Your_name);
        //email = root.findViewById(R.id.Your_Email_id);
        message = root.findViewById(R.id.Your_message_box);
        send = root.findViewById(R.id.submit_btn);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        current_user = user.getUid();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processinsert();
            }
        });
        return root;
    }

    private void processinsert() {
        String name_ =account.getDisplayName() ;
        String email_ = account.getEmail();
        String message_ = message.getText().toString();
        SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa");
        String format = s.format(new Date());
       /* if (TextUtils.isEmpty(name_)) {

            name.setError(" Name required");
            return;
        }
        if (TextUtils.isEmpty(email_)) {

            email.setError(" Email ID required");
            return;
        }*/
        if (TextUtils.isEmpty(message_)) {

            message.setError("Your message required");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("Name",name_);
        map.put("Email",email_);
        map.put("Message", message.getText().toString());
        map.put("UID", current_user);
        map.put("Time",format);

        FirebaseDatabase.getInstance().getReference().child("Messages").child(current_user).push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        message.setText("");


                        Toast.makeText(getContext(), "Message send", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Could not send", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void updateDetail() {
        Intent intent = new Intent(getContext(), no_internet.class);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}