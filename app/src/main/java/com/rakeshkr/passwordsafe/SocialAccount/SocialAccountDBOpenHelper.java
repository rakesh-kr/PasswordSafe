package com.rakeshkr.passwordsafe.SocialAccount;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SocialAccountDBOpenHelper extends SQLiteOpenHelper {

    private static final String LOGTAG = "PasswordSafe";

    private static final String DATABASE_NAME = "social_account.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_SOCIAL = "socialAccountDetails";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_DISPLAY_NAME = "displayName";
    public static final String COLUMN_EMAIL_DOMAIN= "emailDomain";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_SOCIAL + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    COLUMN_EMAIL+ " TEXT NOT NULL, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL, " +
                    COLUMN_EMAIL_DOMAIN + " TEXT NOT NULL, " +
                    COLUMN_DISPLAY_NAME + " TEXT NOT NULL" +
                    ")";


    public SocialAccountDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        Log.i(LOGTAG, "Table has been created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOCIAL);
        onCreate(db);
    }

}
