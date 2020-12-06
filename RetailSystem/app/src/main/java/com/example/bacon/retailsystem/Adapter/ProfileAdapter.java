package com.example.bacon.retailsystem.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bacon.retailsystem.R;

public class ProfileAdapter extends BaseAdapter {

    private Context mContext;
    private String[] data;
    private int[] icon;

    public ProfileAdapter(Context context, String[] data1, int[] iconId) {
        mContext = context;
        data = data1;
        icon = iconId;
    }

    public int getCount() {
        return data.length;
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row;
        row = inflater.inflate(R.layout.profile_row_item, parent, false);
        TextView title;
        ImageView i1;
        i1 = row.findViewById(R.id.lv_img);
        title = row.findViewById(R.id.lv_text);
        title.setText(data[position]);
        i1.setImageResource(icon[position]);

        return (row);
    }
}
