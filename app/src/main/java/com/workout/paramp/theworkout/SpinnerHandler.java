package com.workout.paramp.theworkout;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

interface focus_id_map_intf {
    public int get_focus_id_from_idx(int idx);
}
/**
 * Created by paramp on 4/29/2015.
 */
public class SpinnerHandler implements AdapterView.OnItemSelectedListener {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        modified.value = true;
        val.value = map_intf.get_focus_id_from_idx(position);
        Log.i("workout", "choosing spinner " + val.value);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private WMutableInt val;
    private WMutableBoolean modified;
    private focus_id_map_intf map_intf;
    public SpinnerHandler(WMutableInt val, WMutableBoolean modified, focus_id_map_intf map_intf) {
        this.val = val;
        this.modified = modified;
        this.map_intf = map_intf;
    }
}
