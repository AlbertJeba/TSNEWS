package com.example.tsnews;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.TimeZone;

import static android.text.format.DateUtils.*;

public class myadapter extends FirebaseRecyclerAdapter<model, myadapter.myviewholder> {

    ImageButton fav_btn;
    DatabaseReference databaseReference, fav_item_ref, fav_ref, news;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    Boolean favchecker = false;


    public myadapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, int position, @NonNull final model model) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuserid = user.getUid();
        final String postkey = getRef(position).getKey();

        // FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        holder.header.setText(model.getHeader());
        String time = calculateTimeAgo(model.getTime());
        holder.time.setText(time);
        Glide.with(holder.img1.getContext()).load(model.getImage()).into(holder.img1);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.img1.getContext(), news_desc.class);
                intent.putExtra("linkvalue", model.getLink());
                intent.putExtra("thumbnail", model.getImage());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.img1.getContext().startActivity(intent);
            }
        });

        fav_ref = database.getReference("favourites");
        fav_item_ref = database.getReference("favouriteList").child(currentuserid);
        news = database.getReference("news");
        String header = getItem(position).getHeader();
        String img = getItem(position).getImage();
        String url = getItem(position).getLink();
        String tim = getItem(position).getTime();
        ImageView gg = holder.img1;

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri bmpUri = getLocalBitmapUri(gg);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/*");
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                String body = "Download Tech Snicks";
                if (bmpUri != null) {
                    // Construct a ShareIntent with link to image

                    intent.putExtra(Intent.EXTRA_STREAM, bmpUri);

                }
                intent.putExtra(Intent.EXTRA_TEXT, header + "\n\nRead more -" + url);
                intent = Intent.createChooser(intent, "Share News");
                v.getContext().startActivity(intent);
            }
        });
        holder.favouriteChecker(postkey);
        holder.fav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favchecker = true;
                fav_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (favchecker.equals(true)) {
                            if (snapshot.child(postkey).hasChild(currentuserid)) {
                                fav_ref.child(postkey).child(currentuserid).removeValue();
                                //  fav_ref.child(currentuserid).child(postkey).removeValue();
                                delete(tim);
                                favchecker = false;
                            } else {
                                fav_ref.child(postkey).child(currentuserid).setValue(true);
                                model.setHeader(header);
                                model.setImage(img);
                                model.setLink(url);
                                model.setTime(tim);

                                String id = fav_ref.push().getKey();
                                fav_item_ref.child(currentuserid).child(id).setValue(model);
                                favchecker = false;
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

            }
        });

    }

    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    private String calculateTimeAgo(String time) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        try {

            long time1=0;
            time1 = Objects.requireNonNull(sdf.parse(time)).getTime();
            long now = System.currentTimeMillis();
            CharSequence ago =
                    getRelativeTimeSpanString(time1, now, MINUTE_IN_MILLIS);
            return ago+"";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentuser = user.getUid();

    void delete(String tim) {
        Query query = fav_item_ref.child(currentuser).orderByChild("time").equalTo(tim);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    dataSnapshot1.getRef().removeValue();
                    //Toast.makeText(fav_btn.getContext(), "deleted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow, parent, false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder {
        ImageView img1;
        TextView header, link, time;
        CardView card;
        ImageButton fav_btn, share;
        LinearLayout linearLayout;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            img1 = itemView.findViewById(R.id.img1);
            header = itemView.findViewById(R.id.header);
            link = itemView.findViewById(R.id.header);
            time = itemView.findViewById(R.id.time);
            card = itemView.findViewById(R.id.card);
            fav_btn = itemView.findViewById(R.id.not_bookmark_icon);
            linearLayout = itemView.findViewById(R.id.linear_card);
            share = itemView.findViewById(R.id.share_btn);
        }


        public void favouriteChecker(String postkey) {

            fav_item_ref = database.getReference("favourites");

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = user.getUid();

            fav_ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if (snapshot.child(postkey).hasChild(uid)) {
                        fav_btn.setImageResource(R.drawable.ic_baseline_turned_in_24);
                    } else {
                        fav_btn.setImageResource(R.drawable.ic_baseline_turned_in_not_24);

                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
    }
}