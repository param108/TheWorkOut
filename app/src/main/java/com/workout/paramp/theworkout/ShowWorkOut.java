package com.workout.paramp.theworkout;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;


public class ShowWorkOut extends ActionBarActivity {
    Map workout_id_map;
    protected void show_workouts() {
        LinearLayout l = (LinearLayout)findViewById(R.id.workouts_linear_layout);
        l.removeAllViewsInLayout();
        workout_id_map.clear();

        String[] cols={"workout_id", "name","details"};
        DataBaseHandler db = DataBaseHandler.getInstance(this);
        Cursor myC = DataBaseHandler.getInstance(this).myDB.query(true,"workout_names",cols,
                null,null,null,null,null,null);
        myC.moveToFirst();
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        while(!myC.isAfterLast()) {
            workout_id_map.put(myC.getString(1),myC.getInt(0));
            LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.workout_list,l, false);
            TextView ex_tv = (TextView) rowView.findViewById(R.id.textView);
            ex_tv.setText(myC.getString(1));
            Button b = (Button) rowView.findViewById(R.id.del_button);
            Button b2 = (Button) rowView.findViewById(R.id.mod_button);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = ((TextView)(((RelativeLayout)v.getParent()).getChildAt(0))).getText().toString();
                    DataBaseHandler.getInstance(getApplicationContext()).remove_workout((int)workout_id_map.get(s));
                    show_workouts();
                }
            });

            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = ((TextView)(((RelativeLayout)v.getParent()).getChildAt(0))).getText().toString();
                    /* Start the modifyWorkout activity */
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
            l.addView(rowView);
            myC.moveToNext();
        }
        myC.close();
    }

    public void onResume() {
        super.onResume();
        show_workouts();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_work_out);
        workout_id_map = new HashMap();
        show_workouts();
    }

    public void on_new_workout(View v) {
        Intent in = new Intent(getApplicationContext(),AddWorkout.class);
        startActivity(in);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_work_out, menu);
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
