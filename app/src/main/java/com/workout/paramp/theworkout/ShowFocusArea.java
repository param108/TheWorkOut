package com.workout.paramp.theworkout;

import android.app.Activity;
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
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ShowFocusArea extends ActionBarActivity {

    public void onPause() {
        super.onPause();
        //LinearLayout l = (LinearLayout)findViewById(R.id.focus_areas_data);
        //l.removeAllViewsInLayout();
    }

    public void showFocusAreas() {
        LinearLayout l = (LinearLayout)findViewById(R.id.focus_areas_data);
        l.removeAllViewsInLayout();

        String[] cols={"name","details"};
        DataBaseHandler db = DataBaseHandler.getInstance(this);
        Cursor myC = DataBaseHandler.getInstance(this).myDB.query(true,"focus_data",cols,
                null,null,null,null,null,null);
        myC.moveToFirst();
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        while(!myC.isAfterLast()) {
            LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.exercise_list,l, false);
            TextView ex_tv = (TextView) rowView.findViewById(R.id.textView);
            ex_tv.setText(myC.getString(0));
            Button b = (Button) rowView.findViewById(R.id.button);

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = ((TextView)(((RelativeLayout)v.getParent()).getChildAt(0))).getText().toString();
                    DataBaseHandler.getInstance(getApplicationContext()).removeFocusArea(s);
                    showFocusAreas();
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
        showFocusAreas();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_focus_area);
    }

    public void onNewClick(View v) {
        Intent i = new Intent(this,AddFocusArea.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_focus_area, menu);
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
