package com.htoja.mifik.htoja.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.htoja.mifik.htoja.R;
import com.htoja.mifik.htoja.control.TeamGameManager;
import com.htoja.mifik.htoja.data.TeamsSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class TeamsActivity extends AppCompatActivity {

    private ListView listView;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        listView = (ListView) findViewById(R.id.list);

        adapter = new ListAdapter(TeamsActivity.this, R.layout.team_item, new ArrayList<String>());
        listView.setAdapter(adapter);

        clickAdd(null);
        clickAdd(null);
    }

    public void clickAdd(View view) {
        adapter.add("КОМАНДА " + (adapter.getCount() + 1));
    }

    public void clickPlay(View view) {
        TeamGameManager.getInstance().startNewSet(adapter.getData());
        Intent i = new Intent(getApplicationContext(), GameActivity.class);
        startActivity(i);
    }

    private class ListAdapter extends ArrayAdapter<String> {
        private final List<String> data;
        private final Context context;

        public ListAdapter(Context context, int resource, List<String> data) {
            super(context, resource, data);
            this.data = data;
            this.context = context;
        }

        public List<String> getData() {
            return data;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.team_item, null);
                viewHolder = new ViewHolder();
                viewHolder.text = (TextView) convertView
                        .findViewById(R.id.childTextView);
                viewHolder.button = (ImageButton) convertView
                        .findViewById(R.id.childButton);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final String temp = getItem(position);
            viewHolder.text.setText(temp);
            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.size() > 2) {
                        data.remove(position);
                        notifyDataSetChanged();
                    }
                }
            });

            return convertView;
        }

        public class ViewHolder {
            TextView text;
            ImageButton button;
        }
    }


}
