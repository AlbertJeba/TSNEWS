package com.example.tsnews;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static android.text.format.DateUtils.*;

public class myadapter extends FirebaseRecyclerAdapter<model, myadapter.myviewholder>
{

    public myadapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, int position, @NonNull final model model) {
        holder.header.setText(model.getHeader());
        String time=calculateTimeAgo(model.getTime());
        holder.time.setText(time);
        Glide.with(holder.img1.getContext()).load(model.getImage()).into(holder.img1);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(holder.img1.getContext(),news_desc.class);
                intent.putExtra("linkvalue",model.getLink());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.img1.getContext().startActivity(intent);
            }
        });

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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        ImageView img1;
        TextView header, link,time;
        CardView card;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            img1=itemView.findViewById(R.id.img1);
            header=itemView.findViewById(R.id.header);
            link=itemView.findViewById(R.id.header);
            time=itemView.findViewById(R.id.time);
            card=itemView.findViewById(R.id.card);
        }
    }
}