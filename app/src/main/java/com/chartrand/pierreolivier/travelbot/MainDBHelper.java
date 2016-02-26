package com.chartrand.pierreolivier.travelbot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class MainDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Account.db";
    private static final int DATABASE_VERSION = 1;
    public static final String PERSON_TABLE_NAME = "accounts";
    public static final String PERSON_COLUMN_ID = "_id";
    public static final String PERSON_COLUMN_NAME = "name";

    public MainDBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + PERSON_TABLE_NAME + "(" +
                        PERSON_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        PERSON_COLUMN_NAME + " TEXT)"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PERSON_TABLE_NAME);
        onCreate(db);
    }
    public boolean insertAccount(String name) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PERSON_COLUMN_NAME, name);

        db.insert(PERSON_TABLE_NAME, null, contentValues);
        return true;
    }
    public Cursor getAccount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + PERSON_TABLE_NAME, null );
        return res;
    }
    public String getAccountName() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + PERSON_TABLE_NAME, null );
        res.moveToFirst();
        if(res.getCount()!=0)
            return res.getString(1);
        else
            return null;
    }

}
