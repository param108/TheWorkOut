package com.workout.paramp.theworkout;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class PlayWorkOut extends ActionBarActivity {

    public void plan_workout_clicked(View view) {
        Intent in = new Intent(getApplicationContext(),ChooseWorkout.class);
        in.putExtra("is_plan", true);
        startActivity(in);
    }

    public void play_workout_clicked(View view) {
        DataBaseHandler db = DataBaseHandler.getInstance(getApplicationContext());
        Intent in;
        if (db.if_workout_planned()) {
            in = new Intent(getApplicationContext(), StartWorkout.class);
            in.putExtra("is_plan",false);
            startActivity(in);
        } else {
            in = new Intent(getApplicationContext(), ChooseWorkout.class);
            in.putExtra("is_plan",false);
            startActivity(in);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_work_out);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_play_work_out, menu);
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
