package com.example.productivitytracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "stopwatch_history.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "stopwatch_history";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DURATION = "duration";

    private static DataBaseHelper instance;

    public static synchronized DataBaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DataBaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_DURATION + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades here
    }


    public void saveEntry(DataModel data) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues content = new ContentValues();

        content.put(COLUMN_DESCRIPTION, data.getDescription());
        content.put(COLUMN_DURATION, data.getDuration());

        db.insert(TABLE_NAME, null, content);
        db.close();
    }


    public String getTotalProductiveHours() {
        System.out.println("getTotalProductiveHours called");
        SQLiteDatabase db = getReadableDatabase();
        int totalProductiveMilliseconds = 0;

        try (Cursor cursor = db.rawQuery("SELECT SUM(duration) FROM " + TABLE_NAME, null)) {
            if (cursor.moveToFirst()) {
                totalProductiveMilliseconds = cursor.getInt(0);
            }
        } finally {
            db.close();
        }

        // Convert milliseconds to hours and minutes
        long hours = TimeUnit.MILLISECONDS.toHours(totalProductiveMilliseconds);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(totalProductiveMilliseconds) % 60;

        // Format the result as HH:MM
        String formattedTime = String.format("%02d:%02d", hours, minutes);

        System.out.println("totalProductiveHours: " + formattedTime);

        return formattedTime;
    }

    public List<DataModel> getHistory() {

        try (SQLiteDatabase db = getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null)) {
            ArrayList<DataModel> history = null;
            if (cursor.moveToFirst()) {
                do {
                    DataModel dataModel = new DataModel(
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DURATION))
                    );

                    if (history == null) {
                        history = new ArrayList<DataModel>(List.of(dataModel));
                    } else {
                        history.add(dataModel);
                    }
                } while (cursor.moveToNext());
            }
            return new ArrayList<DataModel>(history);
        }
    }
}

