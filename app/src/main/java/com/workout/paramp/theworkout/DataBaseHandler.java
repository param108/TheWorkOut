package com.workout.paramp.theworkout;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.ContentHandler;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Map;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

/**
 * Created by paramp on 3/9/2015.
 */
public class DataBaseHandler {
    private static DataBaseHandler instance = null;
    public SQLiteDatabase myDB = null;
    private static Context localContext;
    private void copyInputStreamToFile( InputStream in, File file ) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void restoreDb() {
        File dbFile = localContext.getDatabasePath("MyWorkOut");
        if (dbFile.exists()) {
            if (myDB != null) {
                myDB.close();
            }
            dbFile.delete();
        }
        try {
            copyInputStreamToFile(localContext.getAssets().open("databases/MyWorkOut.db"),dbFile);
        } catch (IOException io) {
            // just go ahead, we create it below anyway!
        }
        myDB = localContext.openOrCreateDatabase("MyWorkOut", Context.MODE_PRIVATE, null);
    }

    protected DataBaseHandler(Context ctx) {
        /*
         * Before we do anything we check if the database already exists.
         */
        File dbFile = ctx.getDatabasePath("MyWorkOut");
        if (!dbFile.exists()) {
            myDB = ctx.openOrCreateDatabase("MyWorkOut", Context.MODE_PRIVATE, null);
            myDB.close();
            try {
                copyInputStreamToFile(ctx.getAssets().open("databases/MyWorkOut.db"),dbFile);
            } catch (IOException io) {
                // just go ahead, we create it below anyway!
            }
        }

        myDB = ctx.openOrCreateDatabase("MyWorkOut", Context.MODE_PRIVATE, null);
    }

    public static DataBaseHandler getInstance(Context ctx) {
        if (instance == null) {
            instance = new DataBaseHandler(ctx);
        }
        localContext = ctx;
        return instance;
    }

    public void createWorkoutSchemaTable(boolean clear) {
        SQLiteStatement stmt;
        if (clear) {
            try {
                stmt = myDB.compileStatement("drop table workout_schema" );
                stmt.execute();
            } catch (Exception e) {
            /* ignore a drop error */
            }
        }

        stmt = myDB.compileStatement("Create table workout_schema ( workout_id int NOT NULL," +
                "set_id int NOT NULL," + // name of exercise
                "focus_id int NOT NULL," +
                "num_reps_min int NOT NULL," +
                "intensity int NOT NULL," +
                "weight float NOT NULL," +
                "rest_time int NOT NULL," +
                "duration int NOT NULL," +
                "FOREIGN KEY (focus_id) references focus_data(focus_id)," +
                "FOREIGN KEY (workout_id) references workout_names(workout_id)," +
                "PRIMARY KEY(workout_id,set_id))");
        stmt.execute();
    }

    public void createWorkoutDataTable(boolean clear) {
        SQLiteStatement stmt;
        if (clear) {
            try {
                stmt = myDB.compileStatement("drop table workout_data" );
                stmt.execute();
            } catch (Exception e) {
            /* ignore a drop error */
            }
        }

        stmt = myDB.compileStatement("Create table workout_data (date DATETIME DEFAULT CURRENT_DATE," +
                        "start_time DATETIME," +
                        "end_time DATETIME," +
                        "workout_id int NOT NULL,"+
                        "exercise_id int NOT NULL," + // which exercise
                        "set_id int NOT NULL,"   + // order of focus
                        "intensity int NOT NULL,"   + // intensity achieved
                        "weight int NOT NULL,"      + // representative number for weight
                        "num_reps int NOT NULL,"        + // number of reps
                        "rest_time int NOT NULL," +          // remarks about the workout
                        "FOREIGN KEY(workout_id) references workout_names(workout_id)," +
                        "FOREIGN KEY(exercise_id) references exercise_data(exercise_id)," +
                        "FOREIGN KEY(set_id) references workout_schema(set_id)," +
                        "PRIMARY KEY(date, workout_id, set_id))"
        );

        stmt.execute();
    }

    public void createWorkoutPlanTable(boolean clear) {
        SQLiteStatement stmt;
        if (clear) {
            try {
                stmt = myDB.compileStatement("drop table workout_plan" );
                stmt.execute();
            } catch (Exception e) {
            /* ignore a drop error */
            }
        }

        stmt = myDB.compileStatement("Create table workout_plan (date DATETIME DEFAULT CURRENT_DATE," +
                        "workout_id int NOT NULL,"+
                        "exercise_id int NOT NULL," + // which exercise
                        "set_id int NOT NULL,"   + // order of focus
                        "intensity int NOT NULL,"   + // intensity achieved
                        "weight int NOT NULL,"      + // representative number for weight
                        "num_reps int NOT NULL,"        + // number of reps
                        "rest_time int NOT NULL," +          // remarks about the workout
                        "FOREIGN KEY(workout_id) references workout_names(workout_id)," +
                        "FOREIGN KEY(exercise_id) references exercise_data(exercise_id)," +
                        "FOREIGN KEY(set_id) references workout_schema(set_id)," +
                        "PRIMARY KEY(date, workout_id, set_id))"
        );

        stmt.execute();
    }


    public void createWorkoutTables(boolean clear) {
        SQLiteStatement stmt;
        if (clear) {
            try {
                stmt = myDB.compileStatement("drop table workout_data" );
                stmt.execute();
            } catch (Exception e) {
            /* ignore a drop error */
            }

            try {
                stmt = myDB.compileStatement("drop table workout_features" );
                stmt.execute();
            } catch (Exception e) {
            /* ignore a drop error */
            }

            try {
                stmt = myDB.compileStatement("drop table workout_names" );
                stmt.execute();
            } catch (Exception e) {
            /* ignore a drop error */
            }
        }
        stmt = myDB.compileStatement("Create table workout_names ( workout_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name char(30) NOT NULL UNIQUE," + // name of workout
                "details char(150) NOT NULL)"   // description of workout, usually a url
        );
        stmt.execute();

        createWorkoutSchemaTable(clear);

        createWorkoutDataTable(clear);

        createWorkoutPlanTable(clear);
    }

    public void createExerciseTable(boolean clear) {
        SQLiteStatement stmt;
        if (clear) {
            try {
                stmt = myDB.compileStatement("drop table exercise_data" );
                stmt.execute();
            } catch (Exception e) {
            /* ignore a drop error */
            }
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
           createWorkoutTables(false);
           SQLiteStatement stmt = myDB.compileStatement("Create table focus_data ( focus_id int PRIMARY KEY NOT NULL," +
                    "name char(30) NOT NULL UNIQUE," + // name of focus area
                    "details char(150) NOT NULL)"   // details of the focus area/muscle group, usually a url
            );
            stmt.execute();

            createExerciseTable(false);

        } catch (Exception s) {
            // Work out db already exists with necessary tables
            // return the db
            Toast t = Toast.makeText(localContext, "Using existing database",Toast.LENGTH_SHORT);
            t.show();

        }

    }

    public void deleteDB() {
        try {
            SQLiteStatement stmt = myDB.compileStatement("Drop table workout_names");
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

    public void remove_workout(int id) {
        myDB.delete("workout_names","workout_id = ?", new String[]{Integer.toString(id)});
    }

    public int getNextExerciseId() {
        try {
            String[] cols={"exercise_id"};
            Cursor c = myDB.query(true, "exercise_data", cols, null, null, null, null, "exercise_id desc", "1");
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

    public String getWorkoutName(int workout_id) {
        String[] cols={"name"};
        Cursor c = myDB.query(true, "workout_names", cols, "workout_id = ?",
                    new String[] {Integer.toString(workout_id)}, null, null,null,"1");
        if (c.getCount() == 0) {
            return null;
        }
        c.moveToFirst();
        return c.getString(0);
    }

    public ArrayList<String> getWorkoutNames(Map<String,Integer> map) {
        String[] cols={"name","workout_id"};
        ArrayList<String> s = new ArrayList<>();
        Cursor c = myDB.query(true, "workout_names", cols, null,null, null, null,null,null);
        if (c.getCount() == 0) {
            return null;
        }
        c.moveToFirst();
        while(!c.isAfterLast()) {
            s.add(c.getString(0));
            if (map != null) {
                map.put(c.getString(0), c.getInt(1));
            }
            c.moveToNext();
        }
        return s;
    }

    public boolean if_workout_planned() {
        string[] cols={}
        Cursor c = myDB.query(true, "workout_data", cols, null,null, null, null,null,null);

        return true;
    }
}
