package com.htoja.mifik.htoja.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.htoja.mifik.htoja.R;
import com.htoja.mifik.htoja.data.Vocabulary;
import com.htoja.mifik.htoja.view.SquareTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by mi on 3/2/2017.
 */
public class SetupCategoriesFragment extends Fragment {
    private GridView gridView;
    private ImageAdapter adapter;
    private Button btn;

    private LinkedList<String> colors = new LinkedList<>(Arrays.asList("#E57373", "#F44336", "#3F51B5", "#3F51B5",
            "#009688", "#009688", "#8BC34A", "#CDDC39", "#CDDC39", "#FFC107", "#FF9800", "#E65100", "#FF5722", "#607D8B"));
    private TextView tvCategories;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle bundle) {
        return inflater.inflate(R.layout.fragment_setup_categories, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Collections.shuffle(colors, new Random(System.nanoTime()));
        if (adapter == null) {
            adapter = new SetupCategoriesFragment.ImageAdapter(getContext(),
                    Vocabulary.getInstance().getCategories());
        }
        gridView = (GridView) getActivity().findViewById(R.id.grid);
        btn = (Button) getActivity().findViewById(R.id.btNext);
        tvCategories = (TextView) getActivity().findViewById(R.id.tvCategories);
        updatePlayBtn();
        tvCategories.setText(getActivity().getResources().getString(R.string.num_categories) + " " + adapter.selected.size());

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = adapter.getItem(position);
                if (adapter.isSelected(position)) {
                    adapter.selected.remove(item);
                } else {
                    adapter.selected.add(item);
                }
                adapter.notifyDataSetChanged();
                updatePlayBtn();
                tvCategories.setText(getActivity().getResources().getString(R.string.num_categories) + " " + adapter.selected.size());
            }
        });
    }

    private void updatePlayBtn() {
        if (adapter.selected.size() > 0) {
            btn.setClickable(true);
            btn.setVisibility(View.VISIBLE);
        } else {
            btn.setClickable(false);
            btn.setVisibility(View.INVISIBLE);
        }
    }

    public List<String> getCategories() {
        return adapter.selected;
    }

    private class ImageAdapter extends BaseAdapter {
        private final LayoutInflater layoutInflater;
        private List<String> categories;
        private List<String> selected = new ArrayList<>();

        ImageAdapter(Context c, List<String> categories) {
            this.categories = categories;
            layoutInflater = LayoutInflater.from(c);
        }

        public int getCount() {
            return categories.size();
        }

        public String getItem(int position) {
            return categories.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = layoutInflater.inflate(R.layout.group_item, null);
            SquareTextView txt = (SquareTextView) convertView.findViewById(R.id.tv_name);
            txt.setBackgroundColor(Color.parseColor(colors.get(position)));
            String name = categories.get(position);
            txt.setText(name.toUpperCase() + "\n\n" + String.valueOf(Vocabulary.getInstance().getSizeOfCategory(name)));
            if (isSelected(position)) {
                convertView.setBackground(getActivity().getResources().getDrawable(R.drawable.group_card_selected));
            }
            return convertView;
        }

        boolean isSelected(int position) {
            return selected.contains(adapter.getItem(position));
        }
    }
}