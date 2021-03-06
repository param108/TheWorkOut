package com.workout.paramp.theworkout;

import android.util.MutableBoolean;
import android.util.MutableFloat;
import android.util.MutableInt;

import java.util.Date;

/**
 * Created by paramp on 4/28/2015.
 */
public class WorkOutData {
    public WMutableInt set_id;
    public WMutableInt focus_id;
    public WMutableInt num_reps_min;
    public WMutableInt intensity;
    public WMutableFloat weight;
    public WMutableInt rest_time;
    public WMutableInt duration;
    public WMutableBoolean modified;

    public boolean lt (WorkOutData w1) {
        return (set_id.value < w1.set_id.value);
    }
    public boolean gt (WorkOutData w1) {
        return (set_id.value < w1.set_id.value);
    }
    public boolean eq (WorkOutData w1) {
        return (set_id.value == w1.set_id.value);
    }

    public WorkOutData(int set_id, int focus_id, int num_reps_min,
                       int intensity, float weight, int rest_time, int duration) {
        this.set_id = new WMutableInt(set_id);
        this.focus_id = new WMutableInt(focus_id);
        this.num_reps_min = new WMutableInt(num_reps_min);
        this.intensity = new WMutableInt(intensity);
        this.weight = new WMutableFloat(weight);
        this.rest_time = new WMutableInt(rest_time);
        this.duration = new WMutableInt(duration);
        this.modified = new WMutableBoolean(false);
    }


}

