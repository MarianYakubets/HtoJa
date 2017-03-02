package com.htoja.mifik.htoja.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.htoja.mifik.htoja.R;
import com.htoja.mifik.htoja.control.TeamGameManager;

/**
 * Created by mi on 3/2/2017.
 */
public class NextTeamFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle bundle) {
        return inflater.inflate(R.layout.fragment_next_team, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        StringBuilder sb = new StringBuilder();
        sb.append("НАСТУПНА КОМАНДА: \n" + TeamGameManager.getInstance().nextTeam());

        TextView resultView = (TextView) view.findViewById(R.id.tvResult);
        resultView.setText(sb.toString());
        super.onViewCreated(view, savedInstanceState);

    }
}