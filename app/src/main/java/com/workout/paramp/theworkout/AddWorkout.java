package com.workout.paramp.theworkout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;


public class AddWorkout extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);
    }

    public void onResume() {
        super.onResume();
        EditText f_desc = (EditText) this.findViewById(R.id.workout_desc);
        EditText f_name = (EditText) this.findViewById(R.id.workout_name);

        f_desc.setText("");
        f_name.setText("");
    }

    public void addWorkout(View view) {
        EditText f_desc = (EditText) this.findViewById(R.id.workout_desc);
        EditText f_name = (EditText) this.findViewById(R.id.workout_name);
        ContentValues values = new ContentValues();
        values.put("name",f_name.getText().toString());
        values.put("details",f_desc.getText().toString());
        // will try 3 times
        int i = 0;
        while(i < 3) {
            try {
                if (DataBaseHandler.getInstance(this).myDB.insert("workout_names", null, values)<0) {
                    i++;
                    continue;
                }
            } catch (Throwable e) {
                i++;
                continue;
            }
            // success
            break;
        }
        if (i == 3) {
            Toast t = Toast.makeText(this, "Failed to add workout",10);
            t.show();
            return;
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_workout, menu);
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
