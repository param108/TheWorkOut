package com.workout.paramp.theworkout;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.MutableBoolean;
import android.util.MutableInt;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class ConfigureWorkout extends ActionBarActivity {
    Map<Integer,String> focus_map;
    Map<String, Integer> rev_focus_map;

    int workout_id;
    String workout_name;
    ArrayAdapter<String> adapter;
    ArrayList<String> focus_names;
    ArrayList<Integer> seen_setids;
    LinkedList<WorkOutData> workout_data;
    LinearLayout data_layout;

    private int get_spinner_index(int focus_idx) {
        return focus_names.indexOf(focus_map.get(focus_idx));
    }

    public int focus_id_from_string(String s) {
        return rev_focus_map.get(s);
    }

    private void populate_focus_areas() {
        SQLiteDatabase db = DataBaseHandler.getInstance(getApplicationContext()).myDB;
        focus_map.clear();
        focus_names.clear();
        String[] cols = {"focus_id", "name"};
        Cursor c = db.query(true, "focus_data", cols, null, null, null, null, null, null);

        if (c.getCount() == 0) {
            c.close();
            return;
        }


        c.moveToFirst();
        while (!c.isAfterLast()) {
            focus_map.put(c.getInt(0), c.getString(1));
            rev_focus_map.put(c.getString(1),c.getInt(0));
            focus_names.add(c.getString(1));
            c.moveToNext();
        }
        c.close();

// Create an ArrayAdapter using the string array and a default spinner layout
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                focus_names);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void renumber_sets() {
        int idx = 1;
        for (WorkOutData w : workout_data) {
            w.set_id.value = idx;
            idx++;
            w.modified.value = true;
        }
    }

    private void delete_set(int set_val) {
        int idx = 0;
        for (WorkOutData w : workout_data) {
            if (w.set_id.value == set_val) {
                break;
            }
            idx++;
        }
        workout_data.remove(idx);
        renumber_sets();
    }

    private void add_set(int set_val) {
        WorkOutData w = new WorkOutData(0,0,0,0,0,0,0);
        int idx = 0;
        for (WorkOutData nw : workout_data) {
            if (nw.set_id.value == set_val) {
                break;
            }
            idx++;
        }
        workout_data.add(idx+1,w);
        renumber_sets();
    }

    private void scroll_view(View v) {
        ScrollView sv = (ScrollView)findViewById(R.id.the_scroll_view);
        sv.scrollTo(0,v.getTop());
    }

   class map_idx_to_focus_id implements focus_id_map_intf {
       @Override
       public int get_focus_id_from_idx(int idx) {
           int ret = focus_id_from_string(focus_names.get(idx));
           return ret;
       }
   }

    private void populate_view() {
        data_layout.removeAllViewsInLayout();
        int idx = 0;
        for (WorkOutData w :workout_data) {
            Log.i("workout","w.intensity"+w.intensity.value);
            Spinner spinner;
            LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.workout_mod_list, data_layout, false);

            spinner = (Spinner) rowView.findViewById(R.id.focus_area_spinner);
            spinner.setAdapter(adapter);

            spinner.setSelection(get_spinner_index(w.focus_id.value), true);
            spinner.setOnItemSelectedListener(new SpinnerHandler(w.focus_id,w.modified, new map_idx_to_focus_id() ));
            Button add_btn = (Button) rowView.findViewById(R.id.add_button);
            add_btn.setText(Integer.toString(w.set_id.value) + ".");

            RelativeLayout info_layout = (RelativeLayout) rowView.findViewById(R.id.work_out_mod_info_layout);
            info_layout.setVisibility(View.GONE);

            EditText reps_edit = (EditText) rowView.findViewById(R.id.work_out_mod_reps_edit_text);
            EditText intensity_edit = (EditText) rowView.findViewById(R.id.work_out_mod_intensity_edit_text);
            EditText duration_edit = (EditText) rowView.findViewById(R.id.work_out_mod_duration_edit_text);
            EditText rest_edit = (EditText) rowView.findViewById(R.id.work_out_mod_rest_edit_text);
            EditText weight_edit = (EditText) rowView.findViewById(R.id.work_out_mod_weight_edit_text);

            reps_edit.setText(Integer.toString(w.num_reps_min.value));
            intensity_edit.setText(Integer.toString(w.intensity.value));
            rest_edit.setText(Integer.toString(w.rest_time.value));
            duration_edit.setText(Integer.toString(w.duration.value));
            weight_edit.setText(Integer.toString(w.weight.value));

            configure_info_layout_edit_text(reps_edit, w.num_reps_min, w.modified );
            configure_info_layout_edit_text(intensity_edit, w.intensity, w.modified);
            configure_info_layout_edit_text(rest_edit, w.rest_time, w.modified );
            configure_info_layout_edit_text(duration_edit, w.duration, w.modified );
            configure_info_layout_edit_text(weight_edit, w.weight, w.modified );

            Button open_btn = (Button) rowView.findViewById(R.id.open_button);
            open_btn.setText(">");

            Button del_btn = (Button) rowView.findViewById(R.id.del_button);

            del_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /* delete only in data */
                    RelativeLayout topline = (RelativeLayout) v.getParent();
                    Button add = (Button) topline.findViewById(R.id.add_button);
                    String set_id_str = add.getText().toString();
                    set_id_str = set_id_str.substring(0,set_id_str.length()-1);
                    int set_to_del = Integer.parseInt(set_id_str);
                    if (set_to_del < 1) {
                        Toast t = Toast.makeText(getApplicationContext(),"Failed to del "+ set_to_del + " " + set_id_str,Toast.LENGTH_LONG);
                        t.show();
                    } else {
                        delete_set(set_to_del);
                    }
                    populate_view();
                }
            });
            open_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                     * add code here to go to modify screen
                     */
                    RelativeLayout data_layout = (RelativeLayout) v.getParent().getParent();
                    RelativeLayout info_layout = (RelativeLayout) data_layout.getChildAt(1);
                    Button _btn = (Button) v;
                    if (info_layout.getVisibility() == View.VISIBLE) {
                        info_layout.setVisibility(View.GONE);
                        _btn.setText(">");
                    } else {
                        info_layout.setVisibility(View.VISIBLE);
                        _btn.setText("v");
                    }
                }
            });

            add_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                     * add code here to go to modify screen
                     */
                    RelativeLayout topline = (RelativeLayout) v.getParent();
                    Button add = (Button) topline.findViewById(R.id.add_button);
                    String set_id_str = add.getText().toString();
                    set_id_str = set_id_str.substring(0,set_id_str.length()-1);
                    add_set(Integer.parseInt(set_id_str));
                    populate_view();
                }
            });
            data_layout.addView(rowView);
            idx++;
        }
    }

    private void configure_info_layout_edit_text(EditText t_edit, WMutableInt val, WMutableBoolean modified) {
        ConfigureWorkoutTextWatcher ctw = new ConfigureWorkoutTextWatcher(val, modified);
        t_edit.addTextChangedListener(ctw);
    }

    private void populate_data() {
        workout_data.clear();
        seen_setids.clear();
        DataBaseHandler db = DataBaseHandler.getInstance(getApplicationContext());
        workout_name = db.getWorkoutName(workout_id);
        TextView title_view = (TextView)findViewById(R.id.configure_workout_title);
        title_view.setText(workout_name);



        populate_focus_areas(); //set the adapter for the dropdown

        String[] cols={"set_id","focus_id","num_reps_min", "intensity","weight","rest_time","duration" };
        Cursor myC = DataBaseHandler.getInstance(this).myDB.query(true,"workout_schema",cols,"workout_id = ?",new String[]{Integer.toString(workout_id)},null, null, "set_id",null);
        myC.moveToFirst();
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        while(!myC.isAfterLast()) {
            seen_setids.add(myC.getInt(0));
            WorkOutData wdata = new WorkOutData(myC.getInt(0),
                    myC.getInt(1),
                    myC.getInt(2),
                    myC.getInt(3),
                    myC.getInt(4),
                    myC.getInt(5),
                    myC.getInt(6));
            workout_data.add(wdata);
            myC.moveToNext();
        }
        myC.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_workout);
        workout_id = this.getIntent().getIntExtra("workout_id", -1);
        DataBaseHandler db = DataBaseHandler.getInstance(getApplicationContext());

        if (workout_id == -1) {
            Toast t = Toast.makeText(getApplicationContext(),"Failed to open workout", Toast.LENGTH_SHORT);
            finish();
        }
        focus_map = new HashMap<>();
        rev_focus_map = new HashMap<>();
        focus_names = new ArrayList<String>();
        workout_data = new LinkedList<>();
        seen_setids = new ArrayList<>();
        data_layout = (LinearLayout)findViewById(R.id.configure_workout_data);
        populate_data();
        populate_view();
    }

    public void configure_workout_add_set_clicked(View v) {
        WorkOutData w = new WorkOutData(0,0,0,0,0,0,0);
        workout_data.add(w);
        renumber_sets();
        populate_view();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_configure_workout, menu);
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
