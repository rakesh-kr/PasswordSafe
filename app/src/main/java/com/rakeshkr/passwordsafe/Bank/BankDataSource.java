package com.rakeshkr.passwordsafe.Bank;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.rakeshkr.passwordsafe.R;
import com.rakeshkr.passwordsafe.Utility.MySharedPreferences;
import com.rakeshkr.passwordsafe.Utility.SecurityActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BankDataSource{

    public static final String LOG_CAT="PasswordSafe";
    public String seed="hfhgfdtrdhhjhgfytfytdtrdjhyugtff";
    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;
    Context myContext;

    private static final Map<String, String> myMap;
    static
    {
        myMap = new HashMap<String, String>();
        myMap.put("Canara", String.valueOf(R.drawable.ic_canara));
        myMap.put("HDFC", String.valueOf(R.drawable.ic_hdfc));
        myMap.put("AXIS",String.valueOf(R.drawable.ic_axis));
        myMap.put("Karnataka",String.valueOf(R.drawable.ic_karnataka));
        myMap.put("ICICI",String.valueOf(R.drawable.ic_icici));
        myMap.put("Vijaya",String.valueOf(R.drawable.ic_vijaya));
        myMap.put("City Bank",String.valueOf(R.drawable.ic_citibank));
        myMap.put("State Bank of India",String.valueOf(R.drawable.ic_sbi));
        myMap.put("State Bank of Mysore",String.valueOf(R.drawable.ic_sbm));
        myMap.put("Andra Bank",String.valueOf(R.drawable.ic_andra));
        myMap.put("Corporation Bank",String.valueOf(R.drawable.ic_corp));
        myMap.put("Syndicate Bank",String.valueOf(R.drawable.ic_syndicate));
        myMap.put("ING Vysya",String.valueOf(R.drawable.ic_kotak_ing));
        myMap.put("Yes Bank",String.valueOf(R.drawable.ic_yes_bank));
        myMap.put("Others",String.valueOf(R.drawable.ic_bank));

    }

    private static final String[] allColumns = {
            BankDBOpenHelper.COLUMN_ID,
            BankDBOpenHelper.COLUMN_AC,
            BankDBOpenHelper.COLUMN_ATM_CARD_NUM,
            BankDBOpenHelper.COLUMN_ATM_PIN,
            BankDBOpenHelper.COLUMN_BANK_NAME,
            BankDBOpenHelper.COLUMN_DISPLAY_NAME,
            BankDBOpenHelper.COLUMN_EXPIRY_DATE,
            BankDBOpenHelper.COLUMN_IFSC_CODE};

    public BankDataSource(Context context) {
        dbhelper = new BankDBOpenHelper(context);
        myContext=context;
    }

    public void open() {
        Log.i(LOG_CAT, "Bank Database opened");
        database = dbhelper.getWritableDatabase();
    }

    public void close() {
        Log.i(LOG_CAT, "Bank Database closed");
        dbhelper.close();
    }

    public boolean deleteDatabase(Context context){
        return context.deleteDatabase("bank.db");
    }

    public int updatedb(Bank bank,int id){
        int rowsAffected=0;
        String table=BankDBOpenHelper.TABLE_BANK;
        ContentValues values=new ContentValues();
        try {

            SharedPreferences sharedPreferences=myContext.getSharedPreferences(MySharedPreferences.MyPREFERENCES,Context.MODE_PRIVATE);
            String seed=SecurityActivity.decrypt(MySharedPreferences.seed, sharedPreferences.getString(MySharedPreferences.Salt, MySharedPreferences.seed));
            values.put(BankDBOpenHelper.COLUMN_BANK_NAME, bank.getBankName());
            values.put(BankDBOpenHelper.COLUMN_AC, SecurityActivity.encrypt(seed, bank.getAcNum()));
            values.put(BankDBOpenHelper.COLUMN_ATM_CARD_NUM, SecurityActivity.encrypt(seed, bank.getAtmCardNum()));
            values.put(BankDBOpenHelper.COLUMN_ATM_PIN, SecurityActivity.encrypt(seed, bank.getAtmPin()));
            values.put(BankDBOpenHelper.COLUMN_DISPLAY_NAME, bank.getDispName());
            values.put(BankDBOpenHelper.COLUMN_EXPIRY_DATE, SecurityActivity.encrypt(seed, bank.getExpiryDate()));
            values.put(BankDBOpenHelper.COLUMN_IFSC_CODE,bank.getIFSC());
            rowsAffected=database.update(table,values,BankDBOpenHelper.COLUMN_ID+"='"+id+"'",null);
            return rowsAffected;
        }catch (Exception e){
            e.printStackTrace();
        }
        return rowsAffected;
    }

    public Bank addNewRow(Bank bank) {
        ContentValues values = new ContentValues();
        try{
            SharedPreferences sharedPreferences=myContext.getSharedPreferences(MySharedPreferences.MyPREFERENCES,Context.MODE_PRIVATE);
            String seed=SecurityActivity.decrypt(MySharedPreferences.seed, sharedPreferences.getString(MySharedPreferences.Salt, MySharedPreferences.seed));
            values.put(BankDBOpenHelper.COLUMN_BANK_NAME, bank.getBankName());
            values.put(BankDBOpenHelper.COLUMN_AC,SecurityActivity.encrypt(seed, bank.getAcNum()));
            values.put(BankDBOpenHelper.COLUMN_ATM_CARD_NUM, SecurityActivity.encrypt(seed,bank.getAtmCardNum()));
            values.put(BankDBOpenHelper.COLUMN_ATM_PIN, SecurityActivity.encrypt(seed,bank.getAtmPin()));
            values.put(BankDBOpenHelper.COLUMN_DISPLAY_NAME, bank.getDispName());
            values.put(BankDBOpenHelper.COLUMN_EXPIRY_DATE, SecurityActivity.encrypt(seed, bank.getExpiryDate()));
            values.put(BankDBOpenHelper.COLUMN_IFSC_CODE,bank.getIFSC());
            long insertid;
            insertid = database.insert(BankDBOpenHelper.TABLE_BANK, null, values);
            bank.setId(insertid);
        }catch (SQLiteConstraintException e){
            Log.e(LOG_CAT, "Duplicate entry" + bank.getId());
        }catch (Exception e){
            e.printStackTrace();
        }

        return bank;
    }


    /*decrypt before retrieving*/
    public List<Bank> findAll() {
        List<Bank> banks = new ArrayList<Bank>();

        Cursor cursor = database.query(BankDBOpenHelper.TABLE_BANK, allColumns,
                null, null, null, null, null);

        Log.i(LOG_CAT, "Returned " + cursor.getCount() + " rows");
        try {
            SharedPreferences sharedPreferences=myContext.getSharedPreferences(MySharedPreferences.MyPREFERENCES,Context.MODE_PRIVATE);
            String seed=SecurityActivity.decrypt(MySharedPreferences.seed, sharedPreferences.getString(MySharedPreferences.Salt, MySharedPreferences.seed));
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Bank bank = new Bank();
                    bank.setId(cursor.getInt(cursor.getColumnIndex(BankDBOpenHelper.COLUMN_ID)));
                    bank.setBankName(cursor.getString(cursor.getColumnIndex(BankDBOpenHelper.COLUMN_BANK_NAME)));
                    bank.setAcNum(SecurityActivity.decrypt(seed, cursor.getString(cursor.getColumnIndex(BankDBOpenHelper.COLUMN_AC))));
                    bank.setAtmCardNum(SecurityActivity.decrypt(seed, cursor.getString(cursor.getColumnIndex(BankDBOpenHelper.COLUMN_ATM_CARD_NUM))));
                    bank.setAtmPin(SecurityActivity.decrypt(seed, cursor.getString(cursor.getColumnIndex(BankDBOpenHelper.COLUMN_ATM_PIN))));
                    bank.setDispName(cursor.getString(cursor.getColumnIndex(BankDBOpenHelper.COLUMN_DISPLAY_NAME)));
                    bank.setExpiryDate(SecurityActivity.decrypt(seed, cursor.getString(cursor.getColumnIndex(BankDBOpenHelper.COLUMN_EXPIRY_DATE))));
                    bank.setIFSC(cursor.getString(cursor.getColumnIndex(BankDBOpenHelper.COLUMN_IFSC_CODE)));
                    banks.add(bank);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        cursor.close();
        return banks;
    }


    /*decrypt before retrieving
    * retrieves only one row*/
    public Bank getDataByRowId(int id) {
        Bank bank = new Bank();
        Cursor cursor = database.query(BankDBOpenHelper.TABLE_BANK, allColumns,
                null, null, null, null, null);

        Log.i(LOG_CAT, "Returned " + cursor.getCount() + " rows");
        try {
            SharedPreferences sharedPreferences=myContext.getSharedPreferences(MySharedPreferences.MyPREFERENCES,Context.MODE_PRIVATE);
            String seed=SecurityActivity.decrypt(MySharedPreferences.seed,sharedPreferences.getString(MySharedPreferences.Salt,MySharedPreferences.seed));
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    if (cursor.getInt(cursor.getColumnIndex(BankDBOpenHelper.COLUMN_ID))==id){
                        bank.setId(cursor.getInt(cursor.getColumnIndex(BankDBOpenHelper.COLUMN_ID)));
                        bank.setBankName(cursor.getString(cursor.getColumnIndex(BankDBOpenHelper.COLUMN_BANK_NAME)));
                        bank.setAcNum(SecurityActivity.decrypt(seed, cursor.getString(cursor.getColumnIndex(BankDBOpenHelper.COLUMN_AC))));
                        bank.setAtmCardNum(SecurityActivity.decrypt(seed, cursor.getString(cursor.getColumnIndex(BankDBOpenHelper.COLUMN_ATM_CARD_NUM))));
                        bank.setAtmPin(SecurityActivity.decrypt(seed, cursor.getString(cursor.getColumnIndex(BankDBOpenHelper.COLUMN_ATM_PIN))));
                        bank.setDispName(cursor.getString(cursor.getColumnIndex(BankDBOpenHelper.COLUMN_DISPLAY_NAME)));
                        bank.setExpiryDate(SecurityActivity.decrypt(seed, cursor.getString(cursor.getColumnIndex(BankDBOpenHelper.COLUMN_EXPIRY_DATE))));
                        bank.setIFSC(cursor.getString(cursor.getColumnIndex(BankDBOpenHelper.COLUMN_IFSC_CODE)));
                        cursor.close();
                        return bank;
                    }
                }
            }else{
                Log.e(LOG_CAT,"Database empty");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        cursor.close();
        return bank;
    }


    //isDatapresent(value,column name)
    public boolean isDataPresent(String selection_value,String whereClause) {
        String selection=whereClause+" = ? ";
        String[] selectionArgs={selection_value};

        Cursor cursor = database.query(BankDBOpenHelper.TABLE_BANK, allColumns,
                selection, selectionArgs, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }


    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + BankDBOpenHelper.TABLE_BANK;
        Cursor cursor = database.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    // Getting All Contacts
    public Map<String,String> getAllDisplayName() {
        Map <String,String> banksListsInDb = new HashMap<String,String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + BankDBOpenHelper.TABLE_BANK;

        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String name=cursor.getString(cursor.getColumnIndex(BankDBOpenHelper.COLUMN_DISPLAY_NAME));
                String bank_name=cursor.getString(cursor.getColumnIndex(BankDBOpenHelper.COLUMN_BANK_NAME));
                String unique_id=String.valueOf(cursor.getInt(cursor.getColumnIndex(BankDBOpenHelper.COLUMN_ID)));
                String key = unique_id;
                String value=name+"-"+bank_name;
                // Adding contact to list
                banksListsInDb.put(key,value);
            } while (cursor.moveToNext());
        }

        // return contact list
        cursor.close();
        return banksListsInDb;
    }

    public int deleteRow(int dbId){
        String table=BankDBOpenHelper.TABLE_BANK;
        String whereClause=BankDBOpenHelper.COLUMN_ID+" = ? ";
        String[] whereArgs={String.valueOf(dbId)};
        if (isDataPresent(String.valueOf(dbId),BankDBOpenHelper.COLUMN_ID)){
            return database.delete(table,whereClause,whereArgs);
        }else {
            return 0;
        }

    }

    public List<Integer> getListOfImgageId(List<String> list){
        List<Integer> imgIds=new ArrayList<Integer>();
        String selection=BankDBOpenHelper.COLUMN_ID+" = ? ";
        String bankName;
        String[] column={BankDBOpenHelper.COLUMN_BANK_NAME};

        for (Iterator<String> i=list.iterator();i.hasNext();){
            String item=i.next();

            String[] selectionArgs={item};
            Cursor cursor = database.query(BankDBOpenHelper.TABLE_BANK, column,
                    selection, selectionArgs, null, null, null);
            cursor.moveToFirst();
            if (cursor.getCount()==1) {
                bankName=cursor.getString(cursor.getColumnIndex(BankDBOpenHelper.COLUMN_BANK_NAME));
                if (myMap.get(bankName)!=null) {
                    imgIds.add(Integer.parseInt(myMap.get(bankName)));
                }else{
                    imgIds.add(R.drawable.ic_social_media);
                }
            }else{
                //set default image
                imgIds.add(R.drawable.ic_social_media);
            }
            cursor.close();

        }
        return imgIds;
    }

    public Map<String,String> sortData(String sortBy){
        Map <String,String> banksListsInDb = new HashMap<String,String>();
        String selectQuery = "SELECT  "+BankDBOpenHelper.COLUMN_DISPLAY_NAME+", "+BankDBOpenHelper.COLUMN_ID+", "+BankDBOpenHelper.COLUMN_BANK_NAME+" FROM " + BankDBOpenHelper.TABLE_BANK +" ORDER BY "+ sortBy +" ASC";

        Cursor cursor = database.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String name=cursor.getString(cursor.getColumnIndex(BankDBOpenHelper.COLUMN_DISPLAY_NAME));
                String bank_name=cursor.getString(cursor.getColumnIndex(BankDBOpenHelper.COLUMN_BANK_NAME));
                String unique_id=String.valueOf(cursor.getInt(cursor.getColumnIndex(BankDBOpenHelper.COLUMN_ID)));
                String key = unique_id;
                String value=name+"-"+bank_name;
                // Adding contact to list
                banksListsInDb.put(key,value);
            } while (cursor.moveToNext());
        }

        // return contact list
        cursor.close();
        return banksListsInDb;
    }

}
