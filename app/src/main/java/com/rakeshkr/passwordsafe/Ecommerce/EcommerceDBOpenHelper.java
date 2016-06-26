package com.rakeshkr.passwordsafe.Ecommerce;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class EcommerceDBOpenHelper extends SQLiteOpenHelper {

    private static final String LOGTAG = "PasswordSafe";

    private static final String DATABASE_NAME = "e_commerce.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_ECOM = "eCommerceTable";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_DISPLAY_NAME = "displayName";
    public static final String COLUMN_SELLER_NAME= "sellerName";
    public static final String COLUMN_WEBSITE= "websiteName";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_ECOM + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    COLUMN_EMAIL+ " TEXT NOT NULL, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL, " +
                    COLUMN_WEBSITE + " TEXT NOT NULL, " +
                    COLUMN_SELLER_NAME + " TEXT NOT NULL, " +
                    COLUMN_DISPLAY_NAME + " TEXT NOT NULL" +
                    ")";


    public EcommerceDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        Log.i(LOGTAG, "E commerce table has been created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ECOM);
        onCreate(db);
    }

}
