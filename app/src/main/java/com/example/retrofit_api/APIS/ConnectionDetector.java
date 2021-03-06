package com.example.retrofit_api.APIS;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import com.example.retrofit_api.R;

public class ConnectionDetector {
    private static AlertDialog alert = null;
    private static AlertDialog.Builder builder = null;

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected();
    }

    private static boolean checkInternetConnection(Context mContext) {
        ConnectivityManager connection = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connection != null) {
            if (connection.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                    connection.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                    connection.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED ||
                    connection.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING) {
                return true;
            } else if (connection.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||
                    connection.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
                return false;
            } else {
                return false;
            }
        }
        return false;
    }

    public static boolean internetCheck(Context context, boolean showDialog) {
        if (checkInternetConnection(context)) {
            return true;
        }
        if (showDialog) {
            showAlertDialog(context, context.getString(R.string.msg_no_internet_title), context.getString(R.string.msg_no_internet), false);
        }
        return false;
    }

    public static void showAlertDialog(final Context context, String pTitle, final String pMsg, Boolean status) {
        try {
            if (alert != null && alert.isShowing())
                return;

            builder = new AlertDialog.Builder(context);
            builder.setTitle(pTitle);
            builder.setMessage(pMsg);
            builder.setCancelable(false);
            builder.setPositiveButton(context.getString(R.string.msg_goto_settings),
                    (dialog, which) -> {
                        dialog.dismiss();
                        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        context.startActivity(intent);
                    });
            alert = builder.create();
            if (!alert.isShowing())
                alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}