package com.babystep.tedtalks;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private List<RssParser.Item> items = new ArrayList<>();
    private ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new ItemAdapter(this, 0, items);

        ListView listView = (ListView) findViewById(R.id.lvItems);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);

        new GetFeedTask().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(MovieActivity.getStartIntent(this, adapter.getItem(position)));
    }

    private class GetFeedTask extends AsyncTask<Void, Void, List<RssParser.Item>> {

        @Override
        protected List<RssParser.Item> doInBackground(Void... params) {
            try {
                Request request = new Request.Builder().url("http://feeds2.feedburner.com/tedtalks_video/").build();

                Response response = new OkHttpClient().newCall(request).execute();
                List<RssParser.Item> items = null;
                try {
                    items = new RssParser().parse(response.body().byteStream());

                    String dd = items.get(0).title;
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
                return items;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<RssParser.Item> items) {
            if (items != null) {
                adapter.clear();
                adapter.addAll(items);
            }
        }
    }

}
