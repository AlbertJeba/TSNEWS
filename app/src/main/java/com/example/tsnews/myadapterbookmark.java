package com.example.tsnews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static android.text.format.DateUtils.MINUTE_IN_MILLIS;
import static android.text.format.DateUtils.getRelativeTimeSpanString;

public class myadapterbookmark extends FirebaseRecyclerAdapter<model, myadapterbookmark.myviewholder>
{
    DatabaseReference databaseReference,fav_item_ref;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    Boolean favchecker=false;

    public myadapterbookmark(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }



    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, int position, @NonNull final model model) {
        holder.header.setText(model.getHeader());
        String time=calculateTimeAgo(model.getTime());
        holder.time.setText(time);
        final String postkey = getRef(position).getKey();

        Glide.with(holder.img1.getContext()).load(model.getImage()).into(holder.img1);
        holder.header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(holder.img1.getContext(),news_desc.class);
                intent.putExtra("linkvalue",model.getLink());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.img1.getContext().startActivity(intent);
            }
        });

        String header=getItem(position).getHeader();
        String img=getItem(position).getImage();
        String url=getItem(position).getLink();
        String tim=getItem(position).getTime();

    }
    private String calculateTimeAgo(String time) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        try {
            long time1 = sdf.parse(time).getTime();
            long now = System.currentTimeMillis();
            CharSequence ago =
                    getRelativeTimeSpanString(time1, now, MINUTE_IN_MILLIS);
            return ago+"";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_bookmark,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        ImageView img1;
        TextView header, link,time;
        CardView card;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            img1=itemView.findViewById(R.id.img_bm);
            header=itemView.findViewById(R.id.header_bm);
            link=itemView.findViewById(R.id.header_bm);
            time=itemView.findViewById(R.id.time_bm);
            card=itemView.findViewById(R.id.card);
        }
    }



}
