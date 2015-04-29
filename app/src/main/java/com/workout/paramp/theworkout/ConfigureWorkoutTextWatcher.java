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
        WMutableInt oldval = null;
        WMutableFloat float_oldval = null;

        if (isFloat) {
            float_oldval = new WMutableFloat(valfloat.value);
        } else {
            oldval = new WMutableInt(val.value);
        }

        try {
            if (isFloat) {
                valfloat.value = Float.parseFloat(s.toString());
            } else {
                val.value = Integer.parseInt(s.toString());
            }
        } catch(Exception e){
            // do nothing
            if (isFloat) {
                valfloat.value = float_oldval.value;
            } else {
                val.value = oldval.value;
            }
            return;
        }
        modified.value = true;

    }

    private WMutableInt val;
    private WMutableFloat valfloat;
    private WMutableBoolean modified;
    boolean isFloat;
    public ConfigureWorkoutTextWatcher(WMutableInt val, WMutableBoolean modified) {
        isFloat = false;
        this.val = val;
        this.modified = modified;
    }

    public ConfigureWorkoutTextWatcher(WMutableFloat val, WMutableBoolean modified) {
        isFloat = true;
        this.valfloat = val;
        this.modified = modified;
    }
}
