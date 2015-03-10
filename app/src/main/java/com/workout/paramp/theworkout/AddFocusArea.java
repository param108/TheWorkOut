package com.workout.paramp.theworkout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.sql.DatabaseMetaData;


public class AddFocusArea extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_focus_area);
    }

    public void onResume() {
        super.onResume();
        EditText f_desc = (EditText) this.findViewById(R.id.focus_desc);
        EditText f_name = (EditText) this.findViewById(R.id.focus_name);

        f_desc.setText("");
        f_name.setText("");
    }

    public void addFocusArea(View view) {
        EditText f_desc = (EditText) this.findViewById(R.id.focus_desc);
        EditText f_name = (EditText) this.findViewById(R.id.focus_name);
        ContentValues values = new ContentValues();
        values.put("name",f_name.getText().toString());
        values.put("details",f_desc.getText().toString());
        // will try 3 times
        int i = 0;
        while(i < 3) {
            values.put("focus_id", DataBaseHandler.getInstance(this).getNextFocusId());
            try {
                if (DataBaseHandler.getInstance(this).myDB.insert("focus_data", null, values)<0) {
                    i++;
                    values.remove("focus_id");
                    continue;
                }
            } catch (Throwable e) {
                i++;
                values.remove("focus_id");
                continue;
            }
            // success
            break;
        }
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_focus_area, menu);
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
