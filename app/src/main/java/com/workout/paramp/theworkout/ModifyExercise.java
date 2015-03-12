package com.workout.paramp.theworkout;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class ModifyExercise extends ActionBarActivity {

    String focus_name;
    int focus_id;
    private void populateFocusData() {
        Intent i = getIntent();
        focus_name = i.getStringExtra("TheWorkout.focus_name");
        focus_id = i.getIntExtra("TheWorkout.focus_id",1);
        TextView tv = (TextView)findViewById(R.id.exercise_focus);
        tv.setText("Focus: "+ focus_name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_exercise);
        populateFocusData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_modify_exercise, menu);
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
