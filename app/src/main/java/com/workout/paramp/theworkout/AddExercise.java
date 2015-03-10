package com.workout.paramp.theworkout;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class AddExercise extends ActionBarActivity {

    Map<String,Integer> focus_map;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);
        populateSpinner();
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
