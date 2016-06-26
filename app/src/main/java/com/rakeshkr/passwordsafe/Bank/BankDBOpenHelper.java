package com.rakeshkr.passwordsafe.Bank;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BankDBOpenHelper extends SQLiteOpenHelper {

    private static final String LOGTAG = "PasswordSafe";

    private static final String DATABASE_NAME = "bank.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_BANK = "bankDetails";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_BANK_NAME = "bankName";
    public static final String COLUMN_AC = "accountNum";
    public static final String COLUMN_ATM_CARD_NUM = "atmCardNum";
    public static final String COLUMN_ATM_PIN = "atmPin";
    public static final String COLUMN_DISPLAY_NAME = "displayName";
    public static final String COLUMN_EXPIRY_DATE="expiryDate";
    public static final String COLUMN_IFSC_CODE="ifsc";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_BANK + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    COLUMN_BANK_NAME + " TEXT NOT NULL, " +
                    COLUMN_AC + " TEXT NOT NULL, " +
                    COLUMN_ATM_CARD_NUM + " TEXT NOT NULL, " +
                    COLUMN_ATM_PIN + " TEXT NOT NULL, " +
                    COLUMN_DISPLAY_NAME + " TEXT NOT NULL, " +
                    COLUMN_IFSC_CODE + " TEXT NOT NULL, " +
                    COLUMN_EXPIRY_DATE + " TEXT NOT NULL"+
                    ")";


    public BankDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        Log.i(LOGTAG, "Table has been created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BANK);
        onCreate(db);
    }

}
