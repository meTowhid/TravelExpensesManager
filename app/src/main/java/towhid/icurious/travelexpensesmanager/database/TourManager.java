package towhid.icurious.travelexpensesmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import towhid.icurious.travelexpensesmanager.dataModel.Tour;

public class TourManager {
    private DatabaseHelper mHelper;
    private SQLiteDatabase mDatabase;
    private String[] columns = {
            DatabaseHelper.COL_ID,
            DatabaseHelper.COL_TITLE,
            DatabaseHelper.COL_DESCRIPTION,
            DatabaseHelper.COL_GOING_DATE,
            DatabaseHelper.COL_RETURN_DATE};

    public TourManager(Context context) {
        mHelper = new DatabaseHelper(context);
    }

    private void openDB() {
        mDatabase = mHelper.getWritableDatabase();
    }

    private void closeDB() {
        mHelper.close();
        mDatabase.close();
    }

    public boolean createTour(Tour tour) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COL_TITLE, tour.getTitle());
        cv.put(DatabaseHelper.COL_DESCRIPTION, tour.getDescription());
        cv.put(DatabaseHelper.COL_GOING_DATE, tour.getGoingDate());
        cv.put(DatabaseHelper.COL_RETURN_DATE, tour.getReturnDate());

        openDB();
        long inserted = mDatabase.insert(DatabaseHelper.TABLE_TOUR, null, cv);
        closeDB();
        return inserted > 0;
    }

    public Tour readTour(int id) { // get single contact from database
        Tour tour = new Tour();
        openDB();
        Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_TOUR,
                columns,
                DatabaseHelper.COL_ID + " =? ",
                new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            tour.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_ID)));
            tour.setTitle(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TITLE)));
            tour.setDescription(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_DESCRIPTION)));
            tour.setGoingDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_GOING_DATE)));
            tour.setReturnDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_RETURN_DATE)));
            cursor.close();
        }
        closeDB();
        return tour;
    }

    ArrayList<Tour> readTour() { // get list of tours from database
        ArrayList<Tour> list = new ArrayList<>();
        Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_TOUR, columns, null, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Tour tour = new Tour();
                tour.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_ID)));
                tour.setTitle(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TITLE)));
                tour.setDescription(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_DESCRIPTION)));
                tour.setGoingDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_GOING_DATE)));
                tour.setReturnDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_RETURN_DATE)));
                list.add(tour);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public boolean updateTour(int id, Tour tour) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COL_TITLE, tour.getTitle());
        cv.put(DatabaseHelper.COL_DESCRIPTION, tour.getDescription());
        cv.put(DatabaseHelper.COL_GOING_DATE, tour.getGoingDate());
        cv.put(DatabaseHelper.COL_RETURN_DATE, tour.getReturnDate());

        openDB();
        int updated = mDatabase.update(DatabaseHelper.TABLE_TOUR, cv, DatabaseHelper.COL_ID + " = " + id, null);
        closeDB();
        return updated > 0;
    }

    public boolean deleteTour(int id) {
        openDB();
        int deleted = mDatabase.delete(DatabaseHelper.TABLE_TOUR, DatabaseHelper.COL_ID + " = " + id, null);
        closeDB();
        return deleted > 0;
    }

}
