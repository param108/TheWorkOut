package com.workout.paramp.theworkout;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.widget.Toast;

import java.net.ContentHandler;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

/**
 * Created by paramp on 3/9/2015.
 */
public class DataBaseHandler {
    private static DataBaseHandler instance = null;
    public SQLiteDatabase myDB = null;
    private static Context localContext;
    protected DataBaseHandler(Context ctx) {
        myDB = ctx.openOrCreateDatabase("MyWorkOut", Context.MODE_PRIVATE, null);
    }

    public static DataBaseHandler getInstance(Context ctx) {
        if (instance == null) {
            instance = new DataBaseHandler(ctx);
        }
        localContext = ctx;
        return instance;
    }

    public void createExerciseTable() {
        SQLiteStatement stmt;
        try {
            stmt = myDB.compileStatement("drop table exercise_data");
            stmt.execute();
        } catch (Exception e) {
            /* ignore a drop error */
        }
        stmt = myDB.compileStatement("Create table exercise_data ( exercise_id int PRIMARY KEY NOT NULL," +
                "focus_id int NOT NULL,"  + // focus for this exercise
                "name char(30) NOT NULL UNIQUE," + // name of exercise
                "details char(150) NOT NULL, FOREIGN KEY(focus_id) REFERENCES focus_data(focus_id))"   // details of exercise, usually a url
        );
        stmt.execute();

    }

    public void CreateDb() {
        try {
            SQLiteStatement stmt = myDB.compileStatement("Create table workouts ( name char(20) PRIMARY KEY NOT NULL," +
                    "workout_id int NOT NULL," +
                    "num_focus int NOT NULL," + // Number of focus areas
                    "total_time int NOT NULL)"  // Time the workout takes in minutes
            );
            stmt.execute();
            stmt = myDB.compileStatement("Create table workout_schema ( workout_id int NOT NULL,"+
                    "seq_no int NOT NULL,"          + // order of focus
                    "focus_id int NOT NULL,"        + // focus_id
                    "sets int NOT NULL)"             // number of sets
            );
            stmt.execute();
            stmt = myDB.compileStatement("Create table workout_data ( created_at DATETIME DEFAULT CURRENT_DATE," +
                    "workout_id int NOT NULL,"+
                    "focus_id int NOT NULL," + // focus area
                    "seq_no int NOT NULL,"   + // order of focus
                    "exercise_id int NOT NULL," + // which exercise
                    "weight int NOT NULL,"      + // representative number for weight
                    "reps int NOT NULL,"        + // number of reps
                    "intensity int NOT NULL,"   + // intensity achieved
                    "remarks char(250) NOT NULL)"       // remarks about the workout
            );
            stmt.execute();
            stmt = myDB.compileStatement("Create table focus_data ( focus_id int PRIMARY KEY NOT NULL," +
                    "name char(30) NOT NULL UNIQUE," + // name of focus area
                    "details char(150) NOT NULL)"   // details of the focus area/muscle group, usually a url
            );
            stmt.execute();

            createExerciseTable();

        } catch (Exception s) {
            // Work out db already exists with necessary tables
            // return the db
            Toast t = Toast.makeText(localContext, "Using existing database",10);
            t.show();

        }

    }

    public void deleteDB() {
        try {
            SQLiteStatement stmt = myDB.compileStatement("Drop table workouts");
            stmt.execute();
            stmt = myDB.compileStatement("Drop table workout_schema");
            stmt.execute();
            stmt = myDB.compileStatement("Drop table workout_data");
            stmt.execute();
            stmt = myDB.compileStatement("Drop table exercise_data");
            stmt.execute();
            stmt = myDB.compileStatement("Drop table focus_data");
            stmt.execute();
        } catch (Exception e) {
            // do nothing
        }
    }

    public int getNextFocusId() {
        try {
            String[] cols={"focus_id"};
            Cursor c = myDB.query(true, "focus_data", cols, null, null, null, null, "focus_id desc", "1");
            int val = 0;
            if (c.getCount() == 0) {
                return 1;
            }
            c.moveToFirst();
            val = c.getInt(0);
            c.close();
            return val+1;
        } catch(Throwable e) {
            return 1;
        }
    }

    public void removeFocusArea(String s) {
        myDB.delete("focus_data","name" + " = ?" , new String[]{s});
    }

    public void removeExercise(String s) {
        myDB.delete("exercise_data","name" + " = ?" , new String[]{s});
    }

}
