package com.htoja.mifik.htoja.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.htoja.mifik.htoja.R;
import com.htoja.mifik.htoja.control.TeamGameManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mi on 3/2/2017.
 */
public class SetupTeamsFragment extends Fragment {
    private ListView listView;
    private SetupTeamsFragment.ListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle bundle) {
        return inflater.inflate(R.layout.fragment_setup_teams, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (adapter == null) {
            adapter = new SetupTeamsFragment.ListAdapter(getContext(), R.layout.team_item, new ArrayList<String>());
            listView = (ListView) getActivity().findViewById(R.id.list);
            listView.setAdapter(adapter);

            clickAdd(null);
            clickAdd(null);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    public void clickAdd(View view) {
        adapter.add("КОМАНДА " + (adapter.getCount() + 1));
    }

    public List<String> getTeams() {
        return adapter.getData();
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
                        .findViewById(R.id.tvTeamName);
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