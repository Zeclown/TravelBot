package com.chartrand.pierreolivier.travelbot;

import android.app.ListFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class EmailListFragment extends ListFragment {
    ArrayList<Letter> mailList;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        EmailAdapter adapter = new EmailAdapter(getActivity(),new ArrayList<Letter>());
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO implement some logic
    }
    private class EmailAdapter extends ArrayAdapter<Letter> {
        public EmailAdapter(Context context, ArrayList<Letter> letter) {
            super(context, 0, letter);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Letter letter = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
            }
            // Lookup view for data population
            TextView tvName = (TextView) convertView.findViewById(android.R.id.text1);
            TextView tvHome = (TextView) convertView.findViewById(android.R.id.text2);
            // Populate the data into the template view using the data object
            tvName.setText(letter.getDateSent());
            tvHome.setText(letter.getDateSent());
            // Return the completed view to render on screen
            return convertView;
        }
    }

}