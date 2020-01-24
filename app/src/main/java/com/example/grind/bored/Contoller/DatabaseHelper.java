package com.example.grind.bored.Contoller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// Une BD en SQLITE pour enregistrer les films favoris
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "favorite_movies";


    public DatabaseHelper(Context context) {
        super(context, "favorite_movies.db", null, 1 );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE favorite_movies (ID INTEGER PRIMARY KEY AUTOINCREMENT,Name TEXT UNIQUE)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP IF TABLE EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public void addData(String item)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name",item);
        this.getWritableDatabase().insertOrThrow("favorite_movies","",contentValues);
    }

    public Cursor getData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME;
        Cursor data = db.rawQuery(query,null);
        return data;
    }

    public void deleteData(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM favorite_movies WHERE Name = '"+name+"'";
        db.execSQL(query);
    }
}
