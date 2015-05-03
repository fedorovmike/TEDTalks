package com.babystep.tedtalks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mike on 03.05.2015.
 */
public class ItemAdapter extends ArrayAdapter<RssParser.Item> {

    private static final String LOG_TAG = ItemAdapter.class.getSimpleName();

    public ItemAdapter(Context context, int resource, List<RssParser.Item> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RssParser.Item item = getItem(position);

        View v = convertView;
        ViewHolder holder;

        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.list_item, null);
            // cache view fields into the holder
            holder = new ViewHolder();
            holder.ivThumb = (ImageView) v.findViewById(R.id.ivThumb);
            holder.tvTitle = (TextView) v.findViewById(R.id.tvTitle);
            holder.tvPubDate = (TextView) v.findViewById(R.id.tvPubDate);
            holder.tvDesc = (TextView) v.findViewById(R.id.tvDescription);
            holder.tvDuration = (TextView) v.findViewById(R.id.tvDuration);
            // associate the holder with the view for later lookup
            v.setTag(holder);
        }
        else {
            // view already exists, get the holder instance from the view
            holder = (ViewHolder) v.getTag();
        }

        holder.tvTitle.setText(item.title);
        holder.tvPubDate.setText(item.pubDate);
        holder.tvDesc.setText(item.description);
        holder.tvDuration.setText(item.Duration);
        Picasso.with(getContext()).load(item.mediaThumbnailURL).into(holder.ivThumb);
        return v;
    }

    public static class ViewHolder {
        public ImageView ivThumb;
        public TextView tvTitle;
        public TextView tvPubDate;
        public TextView tvDesc;
        public TextView tvDuration;
    }
}
