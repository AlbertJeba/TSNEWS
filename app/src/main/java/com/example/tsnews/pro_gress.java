package com.example.tsnews;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.style.BackgroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import pl.droidsonroids.gif.transforms.CornerRadiusTransform;

public class pro_gress extends Dialog {

    public pro_gress(@NonNull Context context) {
        super(context);
        WindowManager.LayoutParams params=getWindow().getAttributes();
        params.gravity= Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
        View view= LayoutInflater.from(context).inflate(R.layout.pd_loading,null);
        setContentView(view);
    }
}
