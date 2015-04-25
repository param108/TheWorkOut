package com.workout.paramp.theworkout;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class AdminActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin, menu);
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

    public void on_admin_focus_area_click(View view) {
        Intent in = new Intent(getApplicationContext(),ShowFocusArea.class);
        startActivity(in);

    }

    public void on_admin_exercise_click(View view) {
        Intent in = new Intent(getApplicationContext(),AddExercise.class);
        startActivity(in);

    }

    public void on_admin_workout_click(View view) {
        Intent in = new Intent(getApplicationContext(),AddWorkout.class);
        startActivity(in);
    }

    public void on_admin_restore_db_click(View view) {
        DataBaseHandler db = DataBaseHandler.getInstance(getApplicationContext());
        db.restoreDb();
    }
}
