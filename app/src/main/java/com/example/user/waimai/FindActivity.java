package com.example.user.waimai;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.example.user.adapter.TextTagsAdapter;
import com.moxun.tagcloudlib.view.TagCloudView;

public class FindActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        TagCloudView tagCloudView = (TagCloudView) findViewById(R.id.tag_cloud);
        tagCloudView.setBackgroundColor(Color.WHITE);

        TextTagsAdapter tagsAdapter = new TextTagsAdapter(new String[13]);
        tagCloudView.setAdapter(tagsAdapter);
    }
}
