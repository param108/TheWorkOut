package com.workout.paramp.theworkout;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class ModifyExercise extends ActionBarActivity {

    String focus_name;
    int focus_id;

    public void addExerciseBtn(View view) {
        EditText etv = (EditText)findViewById(R.id.exercise_name);
        EditText dtv = (EditText)findViewById(R.id.exercise_desc);

        SQLiteDatabase db = DataBaseHandler.getInstance(this).myDB;
        ContentValues values = new ContentValues();
        values.put("name", etv.getText().toString());
        values.put("details", dtv.getText().toString());
        values.put("focus_id",focus_id);
        // will try 3 times
        int i = 0;
        while(i < 3) {
            values.put("exercise_id", DataBaseHandler.getInstance(this).getNextExerciseId());
            try {
                if (DataBaseHandler.getInstance(this).myDB.insert("exercise_data", null, values)<0) {
                    i++;
                    values.remove("exercise_id");
                    continue;
                }
            } catch (Throwable e) {
                i++;
                values.remove("exercise_id");
                continue;
            }
            // success
            break;
        }
        if (i == 3) {
            Toast t = Toast.makeText(getApplicationContext(), "Failed to add this exercise", 10);
            t.show();
            return;
        }
        finish();
    }
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
