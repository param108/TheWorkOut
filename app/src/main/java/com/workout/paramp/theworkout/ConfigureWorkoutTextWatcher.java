package com.workout.paramp.theworkout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

/**
 * Created by paramp on 4/29/2015.
 */
public class ConfigureWorkoutTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        WMutableInt oldval = new WMutableInt(val.value);
        try {
            val.value = Integer.parseInt(s.toString());
        } catch(Exception e){
            // do nothing
            val.value = oldval.value;
            return;
        }
        modified.value = true;
        Log.i("workout", "updating value : "+ val.value);

    }

    private WMutableInt val;
    private WMutableBoolean modified;
    public ConfigureWorkoutTextWatcher(WMutableInt val, WMutableBoolean modified) {
        this.val = val;
        if (this.val == val) {
            Log.i("workout", "They are same");
        }
        this.modified = modified;
    }
}
