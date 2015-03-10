package com.workout.paramp.theworkout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
        db.CreateDb();
        Cursor myC = DataBaseHandler.getInstance(this).myDB.query(true,"focus_data",cols,
                null,null,null,null,null,null);
        myC.moveToFirst();
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        while(!myC.isAfterLast()) {

            Context ctx = getApplicationContext();
            LinearLayout h = new LinearLayout(ctx);
            h.setOrientation(LinearLayout.HORIZONTAL);


            TextView focus_name = new TextView(ctx);
            focus_name.setText(myC.getString(0));
            focus_name.setTextAppearance(getApplicationContext(), R.dimen.abc_text_size_large_material);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            h.setLayoutParams(params);
            focus_name.setLayoutParams(params);
            Button b = new Button(ctx);
            b.setText("X");
            b.setLayoutParams(params);
            h.addView(focus_name);
            h.addView(b);
            l.addView(h,lp);

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = ((TextView)(((LinearLayout)v.getParent()).getChildAt(0))).getText().toString();
                    DataBaseHandler.getInstance(getApplicationContext()).removeFocusArea(s);
                    showFocusAreas();
                }
            });

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
