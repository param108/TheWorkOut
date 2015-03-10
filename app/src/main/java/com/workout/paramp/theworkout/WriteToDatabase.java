package com.workout.paramp.theworkout;

import android.app.ActionBar;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.TimeUtils;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class WriteToDatabase extends ActionBarActivity {


    class WorkoutDetails {
        String name;
        int workout_id;
        int num_focus;
    };

    private ArrayList<WorkoutDetails> workouts;
    private void getWorkoutDetails()
    {
        int num_entries;
        Cursor myCursor=DataBaseHandler.getInstance(this).myDB.query(true, "workouts", new String[]{"name", "workout_id", "num_focus"},
                null, null, null, null, "workout_id", null);
        myCursor.moveToFirst();

        num_entries = myCursor.getCount();

        workouts = new ArrayList<WorkoutDetails>();

        if (num_entries == 0) {
            myCursor.close();
            return;
        }

        while (!myCursor.isAfterLast()) {
            WorkoutDetails w = new WorkoutDetails();
            w.name = myCursor.getString(0);
            w.workout_id = myCursor.getInt(1);
            w.num_focus = myCursor.getInt(2);
            workouts.add(w);
            myCursor.moveToNext();
        }
        myCursor.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_to_database);
        TextView tv = (TextView)findViewById(R.id.date_value);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        tv.setText(sdf.format(c.getTime()));

        DataBaseHandler.getInstance(this).CreateDb();
        getWorkoutDetails();

        if (workouts.isEmpty()) {
            LinearLayout w = (LinearLayout)findViewById(R.id.workouts_layout);
            Context ctx = getApplicationContext();
            TextView nothingHere = new TextView(ctx);
            nothingHere.setText("Nothing here yet");
            nothingHere.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            w.addView(nothingHere);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_write_to_database, menu);
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
