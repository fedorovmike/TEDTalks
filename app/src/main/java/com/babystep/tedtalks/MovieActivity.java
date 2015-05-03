package com.babystep.tedtalks;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.VideoView;


public class MovieActivity extends ActionBarActivity {

    private static final String LOG_TAG = MovieActivity.class.getSimpleName();
    private static final String ITEM_EXTRA = "item";

    public static Intent getStartIntent(Context context, RssParser.Item item) {
        Intent intent = new Intent(context, MovieActivity.class);
        intent.putExtra(ITEM_EXTRA, item);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        VideoView mVideoView  = (VideoView)findViewById(R.id.videoView);

        RssParser.Item item = (RssParser.Item) getIntent().getSerializableExtra(ITEM_EXTRA);
        if (item != null) {
            Uri uri = Uri.parse(item.mediaContentURL);
            mVideoView.setMediaController(new MediaController(this));
            mVideoView.setVideoURI(uri);
            mVideoView.requestFocus();
            mVideoView.setZOrderOnTop(true);
            mVideoView.start();
        }
    }

}
