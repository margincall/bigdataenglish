package com.example.pelly.bigdataenglish;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class VocaDatabase extends SQLiteOpenHelper {
    private static VocaDatabase mInstance = null;
    public static final String DB_PATH = "/data/data/com.example.pelly.bigdataenglish/databases/";
    public static final String DB_NAME = "voca.db";

    private Context mContext;

    private VocaDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory,
                                int version) {
        super(context, name, factory, version);
        mContext = context;
        Log.v("v", "VocaDatabase : created or opened database(full constructor) : " + name);
    }

    private VocaDatabase(Context context) {
        super(context, DB_NAME, null, 1);
        mContext = context;
        Log.v("v", "VocaDatabase : created or opened database : " + DB_NAME);
    }

    /**
     * Static method for getting singleton instance
     *
     * @param context : application context
     * @return : singleton instance
     */
    public static VocaDatabase getInstance(Context context) {
        if (mInstance == null) {
            Log.v("v", "trying to create instance of db");
            mInstance = new VocaDatabase(context);
        }
        return mInstance;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public void createDataBase() throws IOException {

        if (dbExist()) {
            Log.v("v", "db exists");
            // By calling this method here onUpgrade will be called on a writeable database,
            // but only if the version number has been bumped
            this.getWritableDatabase();
        }

        if (!dbExist()) {
            close();
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean dbExist() {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {
            //database does't exist yet.
        }

        if (checkDB != null)
            checkDB.close();

        return checkDB != null;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = mContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

        Log.v("v", "VocaDatabase : db copy complete");

    }

    @Override
    synchronized public void close() {
        super.close();
        if (mInstance != null) {
        Log.v("v", "VocaDatabase : closing the database");
            mInstance = null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS 'voca'"
                + "('_id' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , "
                + "'word' VARCHAR(50), 'pronunciation' VARCHAR(50), "
                + "'meaning' BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS brands");
        mContext.deleteDatabase(DB_NAME);
    }

}