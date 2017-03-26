package com.htoja.mifik.htoja.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.htoja.mifik.htoja.R;
import com.htoja.mifik.htoja.control.TeamGameManager;
import com.htoja.mifik.htoja.utils.Storage;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }

    @Override
    protected void onPostResume() {
        if (!TeamGameManager.getInstance().hasStarted()) {
            findViewById(R.id.btContinue).setVisibility(View.GONE);
        }
        super.onPostResume();
    }

    public void startSingle(View view) {
        Intent i = new Intent(getApplicationContext(), ResultActivity.class);
        i.putExtra("SHOW_NEXT_TEAM", true);
        startActivity(i);
    }

    public void startTeams(View view) {
        if (TeamGameManager.getInstance().hasStarted()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Нова Гра");
            builder.setMessage("Ви точно хочете розпочати нову гру? Прогрес поточної буде припинено.");
            builder.setPositiveButton("Так", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    TeamGameManager.getInstance().end(getBaseContext());
                    Intent i = new Intent(getApplicationContext(), SetupActivity.class);
                    startActivity(i);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Ні", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        } else {
            Intent i = new Intent(getApplicationContext(), SetupActivity.class);
            startActivity(i);
        }
    }
}
