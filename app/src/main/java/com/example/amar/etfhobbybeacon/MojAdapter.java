package com.example.amar.etfhobbybeacon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class MojAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<NearbyMResponse> mDataSource;

    public MojAdapter(Context context, ArrayList<NearbyMResponse> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.list_item, parent, false);

        TextView listText= (TextView)rowView.findViewById(R.id.list_text);
        NearbyMResponse response=(NearbyMResponse)getItem(position);
        String text="Korisnik: "+response.username+", podudaranje: "+response.percentage*100+"%";
        listText.setText(text);


        return rowView;
    }
}
