package com.kj.kevin.hitsmusic;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

/**
 * Created by Kevin on 2018/6/1.
 */

public class HitsMusic extends Application {
    private static ConnectivityManager connManager;

    public static boolean isMobileNetworkAvailable(Context context) {
        if(null == connManager){
            connManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isAvailable()) {
            return true;
        } else {
            return false;
        }
    }

    public static void showNetworkNotAvailableDialog(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.network_not_available_title))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(context.getString(R.string.network_not_available_message))
                .setPositiveButton(context.getText(R.string.ok), new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        context.startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(true);

        builder.create().show();
    }
}
