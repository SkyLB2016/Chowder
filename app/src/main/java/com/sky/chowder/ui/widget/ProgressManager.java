package com.sky.chowder.ui.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.sky.chowder.R;

/**
 * Created by sky on 2017/6/21.
 */
public class ProgressManager {
    private void progressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("main");
        progressDialog.setMessage("main");
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setMax(100);
        progressDialog.incrementProgressBy(50);
        progressDialog.setIndeterminate(false);
        progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

}
