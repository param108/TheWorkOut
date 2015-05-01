package com.workout.paramp.theworkout;

import java.util.Date;

public class WorkOutPlan {
    public Date date;
    public Date start_time;
    public WMutableInt exercise_id;
    public WMutableInt set_id;
    public WMutableInt intensity;
    public WMutableFloat weight;
    public WMutableInt num_reps_min;
    public WMutableInt rest_time;
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

    public WorkOutPlan(Date date, Date start_time, int exercise_id, int set_id,
                       int intensity, float weight, int num_reps_min, int rest_time) {
        this.date = date;
        this.start_time = start_time;
        this.exercise_id = new WMutableInt(exercise_id);
        this.set_id = new WMutableInt(set_id);
        this.intensity = new WMutableInt(intensity);
        this.weight = new WMutableFloat(weight);
        this.num_reps_min = new WMutableInt(num_reps_min);
        this.rest_time = new WMutableInt(rest_time);
        this.modified = new WMutableBoolean(false);
    }


}
