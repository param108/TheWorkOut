package com.workout.paramp.theworkout;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class PlanWorkout extends ActionBarActivity {
    Date date;
    String dateString;
    // focus_id to exercise list adapter for spinner
    Map<Integer,ArrayAdapter<String> > exercise_adapter_map;

    //Exercise name to exercise_id
    Map<String, Integer> exercise_map;

    private ArrayAdapter<String> populate_exercises(int focus_id) {
        SQLiteDatabase db = DataBaseHandler.getInstance(getApplicationContext()).myDB;
        ArrayList<String> exercise_names = new ArrayList<>();

        if (exercise_adapter_map.containsKey(focus_id)) {
            return exercise_adapter_map.get(focus_id);
        }

        String[] cols = {"exercise_id", "name"};
        Cursor c = db.query(true, "exercise_data",cols,"focus_id = ?",
                            new String[] {Integer.toString(focus_id)},null,null,null,null);

        c.moveToFirst();
        while (!c.isAfterLast()) {
            exercise_map.put(c.getString(1), c.getInt(0));
            exercise_names.add(c.getString(1));
            c.moveToNext();
        }
        c.close();

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                exercise_names);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exercise_adapter_map.put(focus_id, adapter);
        return adapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_workout);
        exercise_adapter_map = new HashMap<>();
        exercise_map = new HashMap<>();
        setupDate(getIntent().getIntExtra("date", ChooseWorkout.TODAY));
        setupWorkoutName(getIntent().getIntExtra("workout_id", -1));
        populate_schema_data();
        populate_view();
    }

    /*
     * First see if already there use it to create workout_plan, if no, use schema to generate
     * the rows in the db and the workout_plan array.
     */
    private void get_workout_plan() {

    }
    /*
     * Needs workout_plan data to be populated
     */
    private void populate_view() {

    }

    ArrayList<WorkOutData> workout_schema;
    private void populate_schema_data() {
        workout_schema = new ArrayList<>();
        DataBaseHandler db = DataBaseHandler.getInstance(getApplicationContext());
        String[] cols={"set_id","focus_id","num_reps_min", "intensity","weight","rest_time","duration" };
        Cursor myC = DataBaseHandler.getInstance(this).myDB.query(true,"workout_schema",cols,"workout_id = ?",
                new String[]{Integer.toString(workout_id)},null, null, "set_id",null);
        myC.moveToFirst();
        while(!myC.isAfterLast()) {
            WorkOutData wdata = new WorkOutData(myC.getInt(0),
                    myC.getInt(1),
                    myC.getInt(2),
                    myC.getInt(3),
                    myC.getFloat(4),
                    myC.getInt(5),
                    myC.getInt(6));
            workout_schema.add(wdata);
            myC.moveToNext();
        }
        myC.close();
    }

    int workout_id;
    String workout_name;
    private void setupWorkoutName(int workout_id) {
        DataBaseHandler db = DataBaseHandler.getInstance(getApplicationContext());
        this.workout_id = workout_id;
        workout_name = db.getWorkoutName(workout_id);
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }

    private String getDateString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MM-yyyy", Locale.getDefault());
        return dateFormat.format(date);
    }

    private void setupDate(int isToday) {
        Calendar calendar = Calendar.getInstance();
        date = calendar.getTime();

        if (isToday == ChooseWorkout.TOMORROW) {

            calendar.add(Calendar.DAY_OF_YEAR, 1);
            date = calendar.getTime();
        }

        dateString = getDateString();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_plan_workout, menu);
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
