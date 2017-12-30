package com.example.user.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.waimai.R;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by user on 2017/12/19.
 */
public class DialogUtil {
    private static View view;
    private static Dialog mLoadingDialog;
    private static AVLoadingIndicatorView avLoadingIndicatorView;
    private static TextView loadingText;

    public static void showDialogForLoading(Context context, String msg) {
        view = LayoutInflater.from(context).inflate(R.layout.layout_loading_dialog, null);
        loadingText = (TextView) view.findViewById(R.id.id_tv_loading_dialog_text);
        avLoadingIndicatorView = (AVLoadingIndicatorView) view.findViewById(R.id.AVLoadingIndicatorView);
        loadingText.setText(msg);
        mLoadingDialog = new Dialog(context, R.style.loading_dialog_style);
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        mLoadingDialog.show();
        avLoadingIndicatorView.show();
        mLoadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    mLoadingDialog.hide();
                    return true;
                }
                return false;
            }
        });

    }

    public static void stop() {
        mLoadingDialog.dismiss();
        avLoadingIndicatorView.hide();
    }
}
