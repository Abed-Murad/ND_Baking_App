package com.am.nd_baking_app.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import com.am.nd_baking_app.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;

import androidx.core.content.ContextCompat;

public class FUNC {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public static void makeSnackBar(Context context, View view, String text, boolean error) {
        final Snackbar snackbar;
        snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(context, error ? R.color.colorError : R.color.colorInfo));
        TextView textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text); //Get reference of snackbar textview
        textView.setMaxLines(3);
        snackbar.show();
    }


    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }


    public static String getRecipeImage(int recipeId) {
        switch (recipeId) {
            case 1:
                return "https://images-gmi-pmc.edge-generalmills.com/0d8ad1b2-0411-4b37-8cdf-9b7d0334d42a.jpg";
            case 2:
                return "https://images-gmi-pmc.edge-generalmills.com/c95a0455-70d0-4667-bc17-acfaf2894210.jpg";

            case 3:
                return "https://d2gk7xgygi98cy.cloudfront.net/14-3-large.jpg";

            case 4:
                return "https://assets.epicurious.com/photos/579f8ec93a12dd9d56024065/2:1/w_1260%2Ch_630/blueberry-cheesecake.jpg";


        }
        return null;
    }

}
