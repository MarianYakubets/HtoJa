package com.htoja.mifik.htoja.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.htoja.mifik.htoja.R;
import com.htoja.mifik.htoja.activity.GameActivity;
import com.htoja.mifik.htoja.control.TeamGameManager;

import java.util.ArrayList;

/**
 * Created by mi on 3/2/2017.
 */
public class RoundResultFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle bundle) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getActivity().getIntent().getExtras();
        ArrayList<String> skip = bundle.getStringArrayList(GameActivity.SKIP);
        ArrayList<String> correct = bundle.getStringArrayList(GameActivity.CORRECT);

        StringBuilder sb = new StringBuilder();
        sb.append("Кінець раунду: \n" + TeamGameManager.getInstance().getCurrentTeam());

        TeamGameManager.getInstance().addCurrentTeamPoints(correct.size() - skip.size());
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        TextView tvResult = (TextView) view.findViewById(R.id.tvResult);
        tvName.setText(sb.toString());

        if (correct != null && skip != null) {
            tvResult.setText(String.valueOf(correct.size() - skip.size()) + "\n БАЛІВ");
        }

        super.onViewCreated(view, savedInstanceState);

    }
}