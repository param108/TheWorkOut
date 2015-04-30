package com.workout.paramp.theworkout;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ChooseWorkout extends ActionBarActivity {
    ArrayList<String> workout_names;
    Map<String, Integer> workout_map;
    boolean is_plan = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_workout);
        DataBaseHandler db = DataBaseHandler.getInstance(getApplicationContext());

        is_plan = getIntent().getBooleanExtra("is_plan", false);
        workout_map = new HashMap<>();
        workout_names = db.getWorkoutNames(workout_map);
        populate_buttons();
    }

    private void populate_buttons() {
        LinearLayout l = (LinearLayout) findViewById(R.id.choose_workout_layout);
        l.removeAllViewsInLayout();
        for (String s : workout_names) {
            Button b = new Button(getApplicationContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT );
            b.setLayoutParams(lp);
            b.setText(s);
            l.addView(b);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in;
                    if (!is_plan) {
                        in = new Intent(getApplicationContext(), StartWorkout.class);
                    } else {
                        in = new Intent(getApplicationContext(), PlanWorkout.class);
                    }
                    in.putExtra("is_plan",is_plan);
                    startActivity(in);
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_workout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
