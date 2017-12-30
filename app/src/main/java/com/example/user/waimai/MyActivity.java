package com.example.user.waimai;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.roger.match.library.MatchTextView;
import com.roger.match.library.MatchButton;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import com.example.user.util.MatchDialog;
import tyrantgit.explosionfield.ExplosionField;

public class MyActivity extends AppCompatActivity {
    private SeekBar mSeekBar;
    private MatchTextView mMatchTextView;
    MatchButton matchButton;
    private ExplosionField mExplosionField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

       mMatchTextView = (MatchTextView)findViewById(R.id.matchtv);
         matchButton = (MatchButton)findViewById(R.id.matchbt);
        // the following are default settings
       // mMatchTextView.setText("EL BY");
       // mMatchTextView.setTextSize(30);
        //mMatchTextView.setTextColor(Color.BLACK);
// setProgress  float 0-1
        //mMatchTextView.setProgress(0.5f);

        matchButton.setTextColor(Color.BLACK);

        mSeekBar = (SeekBar) findViewById(R.id.mSeekBar);
        mSeekBar.setProgress(50);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mMatchTextView.setProgress(progress * 1f / 100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        matchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MatchDialog matchDialog = new MatchDialog();
                getSupportFragmentManager().beginTransaction().add(matchDialog, "LXD").commit();
            }
        });

        mExplosionField = ExplosionField.attach2Window(this);
        addListener(findViewById(R.id.activity_my));
    }

    private void addListener(View root) {
        if (root instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) root;
            for (int i = 0; i < parent.getChildCount(); i++) {
                addListener(parent.getChildAt(i));
            }
        } else {
            root.setClickable(true);
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mExplosionField.explode(v);
                    v.setOnClickListener(null);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_reset) {
            View root = findViewById(R.id.activity_my);
            reset(root);
            addListener(root);
            mExplosionField.clear();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void reset(View root) {
        if (root instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) root;
            for (int i = 0; i < parent.getChildCount(); i++) {
                reset(parent.getChildAt(i));
            }
        } else {
            root.setScaleX(1);
            root.setScaleY(1);
            root.setAlpha(1);
        }
    }
}
