package com.workout.paramp.theworkout;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AddExercise extends ActionBarActivity {

    Map<String,Integer> focus_map;
    Map<String,Integer> exercise_map;
    private void populateExercises() {
        exercise_map.clear();
        SQLiteDatabase db = DataBaseHandler.getInstance(this).myDB;
        String[] cols = {"name","exercise_id"};
        Cursor c = db.query(true,"exercise_data",cols,null,null,null,null,null,null);
        if (c.getCount() == 0) {
            c.close();
            return;
        }

        LinearLayout l = (LinearLayout)findViewById(R.id.ex_lin_layout);
        c.moveToFirst();
        while(!c.isAfterLast()) {
            exercise_map.put(c.getString(0),c.getInt(1));
            LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.exercise_list,l, false);
            TextView ex_tv = (TextView) rowView.findViewById(R.id.textView);
            ex_tv.setText(c.getString(0));
            Button b = (Button) rowView.findViewById(R.id.button);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String a = ((TextView)(((RelativeLayout)v.getParent()).getChildAt(0))).getText().toString();
                    DataBaseHandler.getInstance(getApplicationContext()).removeExercise(a);
                    populateExercises();
                }
            });
            ex_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                     * add code here to go to modify screen
                     */
                }
            });
            c.moveToNext();
        }
        c.close();
    }
    private void populateSpinner() {

        /*
         * Lets clean it up
         */
        focus_map.clear();

        SQLiteDatabase db = DataBaseHandler.getInstance(this).myDB;
        String[] cols = {"focus_id", "name"};
        Cursor c = db.query(true, "focus_data", cols, null, null, null, null, null, null);

        if (c.getCount() == 0) {
            c.close();
            return;
        }

        ArrayList<String> focus_names = new ArrayList<String>();
        c.moveToFirst();
        while (!c.isAfterLast()) {
            focus_map.put(c.getString(1), c.getInt(0));
            focus_names.add(c.getString(1));
            c.moveToNext();
        }
        c.close();

        Spinner spinner = (Spinner) findViewById(R.id.focus_spinner);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                focus_names);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        focus_map = new HashMap<String,Integer>();
        exercise_map = new HashMap<String,Integer>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);
        populateSpinner();
        populateExercises();
        Button b = (Button)findViewById(R.id.add_exercise);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_exercise, menu);
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
