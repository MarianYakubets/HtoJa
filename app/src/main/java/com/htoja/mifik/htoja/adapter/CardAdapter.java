package com.htoja.mifik.htoja.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.htoja.mifik.htoja.R;

/**
 * Created by marian on 26.03.17.
 */

public class CardAdapter extends ArrayAdapter<String> {
    private  final int layout;
    public CardAdapter(Context context, int layout) {
        super(context, 0);
        this.layout = layout;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        ViewHolder holder;

        if (contentView == null) {
            contentView = View.inflate(getContext(), layout, null);
            holder = new ViewHolder(contentView);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }

        holder.textView.setText(getItem(position).toUpperCase());

        return contentView;
    }

    private static class ViewHolder {
        public TextView textView;

        public ViewHolder(View view) {
            this.textView = (TextView) view.findViewById(R.id.item_card_stack_text);
        }
    }

}
