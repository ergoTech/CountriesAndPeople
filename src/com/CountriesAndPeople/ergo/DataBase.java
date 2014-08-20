package com.CountriesAndPeople.ergo;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.sql.SQLException;

public class DataBase {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_AREA = "area";
    public static final String KEY_POP = "population";

    private static final String DATABASE_NAME = "Countrybd2";
    private static final String DATABASE_TABLE = "countryTable";
    private static final int DATABASE_VERSION = 1;

    private DbHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;

    public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_COUNTRY, KEY_AREA, KEY_POP};

    public Cursor getData() {
        String where = null;
        Cursor c = ourDatabase.query(true, DATABASE_TABLE, ALL_KEYS, where, null, null, null, null, null);

        return c;
    }

    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" +
                            KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            KEY_COUNTRY + " TEXT NOT NULL, " +
                            KEY_AREA + " TEXT NOT NULL, " +
                            KEY_POP + " TEXT NOT NULL)"
            );
            commonData("Russia", "143,5", "17,098,000", db);
            commonData("Canada", "34,88", "9,984,000", db);
            commonData("China", "1,351", "9,706,000", db);
            commonData("United States", "313,9", "9,629,000", db);
            commonData("Brazil", "198,7", "8,514,000", db);
            commonData("Australia", "22,68", "7,692,000", db);
            commonData("India", "1,237,000", "3,166,000", db);
            commonData("Argentina", "41,09", "2,780,000", db);
            commonData("Kazakhstan", "16,8", "2,724,000", db);
            commonData("Mexico", "120,8", "1,964,000", db);
            commonData("Peru", "29,99", "1,285,000", db);
            commonData("Colombia", "47,7", "1,141,000", db);
            commonData("Egypt", "80,72", "1,002,000", db);
            commonData("Turkey", "74", "783,000", db);
            commonData("Chile", "17,46", "756,000", db);
            commonData("France", "65,7", "640,000", db);
            commonData("Ukraine", "47", "603,000", db);
            commonData("Madagascar", "22,29", "587,000", db);
            commonData("Spain", "47,27" , "505,000", db);
            commonData("Sweden", "9.64", "450,000", db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXIST " + DATABASE_TABLE);
            onCreate(db);
        }

        private void commonData (String name, String area, String pop , SQLiteDatabase db) {
            ContentValues cv = new ContentValues();
            cv.put(KEY_COUNTRY, name);
            cv.put(KEY_AREA, area);
            cv.put(KEY_POP, pop);
            db.insert(DATABASE_TABLE, null, cv );
        }
    }

    public DataBase(Context c) {
        ourContext = c;
    }

    public DataBase open() throws SQLException {
        ourHelper = new DbHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        ourHelper.close();
    }

    public long createEntry(String name, String area, String pop) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_COUNTRY, name);
        cv.put(KEY_AREA, area);
        cv.put(KEY_POP, pop);
        return ourDatabase.insert(DATABASE_TABLE, null, cv);
    }

    public boolean deleteRow(String name) {
        return ourDatabase.delete(DATABASE_TABLE, KEY_COUNTRY + "=?", new String[]{name}) != 0;
    }


}
