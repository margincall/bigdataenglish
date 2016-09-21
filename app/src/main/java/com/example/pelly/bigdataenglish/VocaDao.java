package com.example.pelly.bigdataenglish;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by RyuCH on 2016-09-22.
 */
public class VocaDao {

    VocaDatabase mHelper;
    SQLiteDatabase db;

    public VocaDao(Context context) {
        mHelper = VocaDatabase.getInstance(context);
    }

    public void update(String set, String condition) {
        String sql = "UPDATE voca SET "
                + set
                + " "
                + condition
                + ";";

        db = mHelper.getWritableDatabase();
        if (db == null) {
            Log.d("d", "VocaDao : db null");
            mHelper.close();
            return;
        }

        db.execSQL(sql);
    }

    public String getBrandName(int brandId) {
        ArrayList<String> names = getString("brand", "WHERE _id = " + brandId);
        if (names == null || names.size() == 0)
            return "";
        else
            return names.get(0);
    }

    public ArrayList<Integer> getInt(String column, String condition) {
        String sql = "SELECT "
                + column
                + " FROM brands "
                + condition
                + ";";

        Cursor cursor = readQuery(sql);
        if (cursor == null)
            return new ArrayList<Integer>();

        ArrayList<Integer> result = new ArrayList<Integer>();
        while (cursor.moveToNext()) {
            result.add(cursor.getInt(0));
        }

        cursor.close();

        return result;
    }

    public ArrayList<String> getString(String column, String condition) {
        String sql = "SELECT "
                + column
                + " FROM voca "
                + condition
                + ";";

        Cursor cursor = readQuery(sql);
        if (cursor == null)
            return new ArrayList<String>();

        ArrayList<String> result = new ArrayList<String>();
        while (cursor.moveToNext()) {
            result.add(cursor.getString(0));
        }

        cursor.close();

        return result;
    }

    private Cursor readQuery(String query) {
        db = mHelper.getReadableDatabase();
        if (db == null) {
            Log.d("d", "VocaDao : db null");
            return null;
        }
        return db.rawQuery(query, null);
    }

}
