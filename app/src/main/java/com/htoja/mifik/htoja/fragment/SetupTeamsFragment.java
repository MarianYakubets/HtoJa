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
import android.widget.Toast;

import com.htoja.mifik.htoja.R;
import com.htoja.mifik.htoja.control.TeamGameManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by mi on 3/2/2017.
 */
public class SetupTeamsFragment extends Fragment {
    private SetupTeamsFragment.ListAdapter adapter;

    private LinkedList<String> colors = new LinkedList<>(Arrays.asList("сині", "фіолетові", "рожеві", "червоні", "жовті",
            "чорні", "білі", "коричневі", "зелені", "малинові",
            "салатові", "кремові", "помаранчеві", "сірі", "оливкові"));

    private LinkedList<String> creatures = new LinkedList<>(Arrays.asList("гнови", "ельфи", "монголи", "вікінги", "вампіри",
            "динозаври", "репери", "блогери", "інженери", "термінатори",
            "поні", "індіанці", "санти", "монахи", "гопніки"));
    private final String SPACE = " ";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle bundle) {
        return inflater.inflate(R.layout.fragment_setup_teams, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Collections.shuffle(colors, new Random(System.nanoTime()));
        Collections.shuffle(creatures, new Random(System.nanoTime()));
        if (adapter == null) {
            List<String> names = new ArrayList<>();
            adapter = new SetupTeamsFragment.ListAdapter(getContext(), R.layout.team_item, names);
            clickAdd(null);
            clickAdd(null);
        }
        ListView listView = (ListView) getActivity().findViewById(R.id.list);
        listView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        if (getView() != null) {
            adapter.notifyDataSetChanged();
            getView().requestLayout();
        }
        super.onStart();
    }

    public void clickAdd(View view) {
        if (adapter.getCount() == 15) {
            Toast.makeText(getContext(), "Максимум 15 команд", Toast.LENGTH_SHORT).show();
            return;
        }
        adapter.add(colors.poll() + SPACE + creatures.poll());
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
                        String string = data.get(position);
                        String[] split = string.split(SPACE);

                        colors.addLast(split[0]);
                        creatures.addLast(split[1]);

                        data.remove(position);
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Мінімум 2 команди", Toast.LENGTH_SHORT).show();
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