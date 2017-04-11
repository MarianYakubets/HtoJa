package com.htoja.mifik.htoja.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.htoja.mifik.htoja.R;
import com.htoja.mifik.htoja.view.SquareTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mi on 3/2/2017.
 */
public class SetupGroupsFragment extends Fragment {
    private GridView gridView;
    private ImageAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle bundle) {
        return inflater.inflate(R.layout.fragment_setup_groups, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (adapter == null) {
            List<String> names = new ArrayList<>(2);
            names.add("ТВАРИНИ");
            names.add("МІСТА");
            names.add("КРАЇНИ");
            names.add("ФІЛЬМИ");
            names.add("ЛЮДИ");
            names.add("ІСТОРІЯ");
            names.add("РОСЛИНИ");
            adapter = new SetupGroupsFragment.ImageAdapter(getContext(), names);
        }
        gridView = (GridView) getActivity().findViewById(R.id.grid);
        gridView.setAdapter(adapter);
    }


    public class ImageAdapter extends BaseAdapter {
        private final LayoutInflater layoutInflater;
        private List<String> groups;

        public ImageAdapter(Context c, List<String> groups) {
            this.groups = groups;
            layoutInflater = LayoutInflater.from(c);
        }

        public int getCount() {
            return groups.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = layoutInflater.inflate(R.layout.group_item, null);
            SquareTextView txt = (SquareTextView) convertView.findViewById(R.id.tv_name);
            txt.setText(groups.get(position));
            return convertView;
        }
    }
}