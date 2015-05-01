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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ChooseWorkout extends ActionBarActivity {
    ArrayList<String> workout_names;
    Map<String, Integer> workout_map;
    ArrayAdapter<String> adapter;
    ArrayList<String> days;

    static final int TODAY = 0;
    static final int TOMORROW = 1;
    int which_day = TODAY;
    boolean is_plan = false;

    private void populate_spinner(Spinner sp) {
        days = new ArrayList<>();
        days.add("Today");
        days.add("Tomorrow");
        sp.setVisibility(View.VISIBLE);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, days);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    which_day = TODAY;
                } else {
                    which_day = TOMORROW;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_workout);
        DataBaseHandler db = DataBaseHandler.getInstance(getApplicationContext());

        is_plan = getIntent().getBooleanExtra("is_plan", false);
        workout_map = new HashMap<>();
        workout_names = db.getWorkoutNames(workout_map);
        populate_buttons();
        Spinner s = (Spinner)findViewById(R.id.choose_workout_date_spinner);
        if (is_plan) {
            populate_spinner(s);
        } else {
            s.setVisibility(View.GONE);
        }
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
                    Button me = (Button)v;
                    if (is_plan) {
                        in = new Intent(getApplicationContext(), PlanWorkout.class);
                        in.putExtra("date",which_day); // will tell the date to populate.
                    } else {
                        in = new Intent(getApplicationContext(), StartWorkout.class);
                    }
                    in.putExtra("workout_id",workout_map.get(me.getText().toString()));
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
