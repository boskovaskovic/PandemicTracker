package db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;

@Database(entities = State.class, exportSchema = true, version = 1)
public abstract class StateDatabase extends RoomDatabase {

    private static final String db_name = "state_db";
    private static StateDatabase instance;

    public static synchronized StateDatabase getInstance(final Context context) {

        if (instance == null)
            instance = Room.databaseBuilder(context.getApplicationContext(), StateDatabase.class, db_name)
                    .fallbackToDestructiveMigration()
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadExecutor().execute(new Runnable() {
                                @Override
                                public void run() {
                                    loadDatabase(getInstance(context));
                                }
                            });

                        }
                    })
                    .build();
        return instance;


    }

    private static void loadDatabase(StateDatabase db) {


        db.stateDao().addState(new State("Alabama", "AL"));
        db.stateDao().addState(new State("Alaska", "AK"));
        db.stateDao().addState(new State("Arizona", "AZ"));
        db.stateDao().addState(new State("Arkansas", "AR"));
        db.stateDao().addState(new State("California", "CA"));
        db.stateDao().addState(new State("Colorado", "CO"));
        db.stateDao().addState(new State("Connecticut", "CT"));
        db.stateDao().addState(new State("Delaware", "DE"));
        db.stateDao().addState(new State("District of Columbia", "DC"));
        db.stateDao().addState(new State("Florida", "FL"));

        db.stateDao().addState(new State("Georgia", "GA"));
        db.stateDao().addState(new State("Hawaii", "HI"));
        db.stateDao().addState(new State("Idaho", "ID"));
        db.stateDao().addState(new State("Illinois", "IL"));
        db.stateDao().addState(new State("Indiana", "IN"));
        db.stateDao().addState(new State("Iowa", "IA"));
        db.stateDao().addState(new State("Kansas", "KS"));
        db.stateDao().addState(new State("Kentucky", "KY"));
        db.stateDao().addState(new State("Louisiana", "LA"));
        db.stateDao().addState(new State("Maine", "ME"));

        db.stateDao().addState(new State("Maryland", "MD"));
        db.stateDao().addState(new State("Massachusetts", "MA"));
        db.stateDao().addState(new State("Michigan", "MI"));
        db.stateDao().addState(new State("Minnesota", "MN"));
        db.stateDao().addState(new State("Mississippi", "MS"));
        db.stateDao().addState(new State("Missouri", "MO"));
        db.stateDao().addState(new State("Montana", "MT"));
        db.stateDao().addState(new State("Nebraska", "NE"));
        db.stateDao().addState(new State("Nevada", "NV"));
        db.stateDao().addState(new State("New Hampshire", "NH"));


        db.stateDao().addState(new State("New Jersey", "NJ"));
        db.stateDao().addState(new State("New Mexico", "NM"));
        db.stateDao().addState(new State("New York", "NY"));
        db.stateDao().addState(new State("North Carolina", "NC"));
        db.stateDao().addState(new State("North Dakota", "ND"));
        db.stateDao().addState(new State("Ohio", "OH"));
        db.stateDao().addState(new State("Oklahoma", "OK"));
        db.stateDao().addState(new State("Oregon", "OR"));
        db.stateDao().addState(new State("Pennsylvania", "PA"));
        db.stateDao().addState(new State("Rhode Island", "RI"));


        db.stateDao().addState(new State("South Carolina", "SC"));
        db.stateDao().addState(new State("South Dakota", "SD"));
        db.stateDao().addState(new State("Tennessee", "TN"));
        db.stateDao().addState(new State("Texas", "TX"));
        db.stateDao().addState(new State("Utah", "UT"));
        db.stateDao().addState(new State("Vermont", "VT"));
        db.stateDao().addState(new State("Virginia", "VA"));
        db.stateDao().addState(new State("Washington", "WA"));
        db.stateDao().addState(new State("West Virginia", "WV"));
        db.stateDao().addState(new State("Wisconsin", "WI"));
        db.stateDao().addState(new State("Wyoming", "WY"));


    }

    public abstract StateDao stateDao();


}
