package com.rakeshkr.passwordsafe.SocialAccount;


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

public class SocialAccountDataSource {

    public static final String LOG_CAT="PasswordSafe";
    //public String seed="hfhgfdtrdhhjhgfytfytdtrdjhyugtff";
    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;
    Context myContext;

    private static final Map<String, String> myMap;
    static
    {
        myMap = new HashMap<String, String>();
        myMap.put("GMAIL", String.valueOf(R.drawable.ic_gmail));
        myMap.put("YAHOO", String.valueOf(R.drawable.ic_yahoo));
        myMap.put("Facebook",String.valueOf(R.drawable.ic_facebook));
        myMap.put("MICROSOFT",String.valueOf(R.drawable.ic_msn));
        myMap.put("Others",String.valueOf(R.drawable.ic_social_media));
    }

    private static final String[] allColumns = {
            SocialAccountDBOpenHelper.COLUMN_ID,
            SocialAccountDBOpenHelper.COLUMN_EMAIL,
            SocialAccountDBOpenHelper.COLUMN_PASSWORD,
            SocialAccountDBOpenHelper.COLUMN_EMAIL_DOMAIN,
            SocialAccountDBOpenHelper.COLUMN_DISPLAY_NAME};

    public SocialAccountDataSource(Context context) {
        dbhelper = new SocialAccountDBOpenHelper(context);
        myContext=context;
    }

    public void open() {
        Log.i(LOG_CAT, "Social a/c Database opened");
        database = dbhelper.getWritableDatabase();
    }

    public void close() {
        Log.i(LOG_CAT, "Social a/c Database closed");
        dbhelper.close();
    }

    public boolean deleteDatabase(Context context){
        return context.deleteDatabase("social_account.db");
    }

    public int updatedb(SocialAccount socialAccount,int id){
        int rowsAffected=0;
        String table=SocialAccountDBOpenHelper.TABLE_SOCIAL;
        ContentValues values=new ContentValues();
        try {
            SharedPreferences sharedPreferences=myContext.getSharedPreferences(MySharedPreferences.MyPREFERENCES,Context.MODE_PRIVATE);
            String seed=SecurityActivity.decrypt(MySharedPreferences.seed, sharedPreferences.getString(MySharedPreferences.Salt, MySharedPreferences.seed));
            values.put(SocialAccountDBOpenHelper.COLUMN_EMAIL, socialAccount.getEmail());
            values.put(SocialAccountDBOpenHelper.COLUMN_PASSWORD, SecurityActivity.encrypt(seed, socialAccount.getPassword()));
            values.put(SocialAccountDBOpenHelper.COLUMN_DISPLAY_NAME, socialAccount.getDispName());
            values.put(SocialAccountDBOpenHelper.COLUMN_EMAIL_DOMAIN, socialAccount.getDomainName());
            rowsAffected=database.update(table,values,SocialAccountDBOpenHelper.COLUMN_ID+"='"+id+"'",null);
            return rowsAffected;
        }catch (Exception e){
            e.printStackTrace();
        }
        return rowsAffected;
    }

    public SocialAccount addNewRow(SocialAccount socialAccount) {
        ContentValues values = new ContentValues();
        try{
            SharedPreferences sharedPreferences=myContext.getSharedPreferences(MySharedPreferences.MyPREFERENCES,Context.MODE_PRIVATE);
            String seed=SecurityActivity.decrypt(MySharedPreferences.seed, sharedPreferences.getString(MySharedPreferences.Salt, MySharedPreferences.seed));
            values.put(SocialAccountDBOpenHelper.COLUMN_EMAIL, socialAccount.getEmail());
            values.put(SocialAccountDBOpenHelper.COLUMN_PASSWORD, SecurityActivity.encrypt(seed, socialAccount.getPassword()));
            values.put(SocialAccountDBOpenHelper.COLUMN_DISPLAY_NAME, socialAccount.getDispName());
            values.put(SocialAccountDBOpenHelper.COLUMN_EMAIL_DOMAIN, socialAccount.getDomainName());
        long insertid = database.insert(SocialAccountDBOpenHelper.TABLE_SOCIAL, null, values);
            socialAccount.setId(insertid);
        }catch (SQLiteConstraintException e){
            Log.e(LOG_CAT, "Duplicate entry" + socialAccount.getEmail());
        }catch (Exception e){
            e.printStackTrace();
        }

        return socialAccount;
    }

    public List<SocialAccount> findAll() {
        List<SocialAccount> socialAccounts = new ArrayList<SocialAccount>();
        Cursor cursor=null;
        try {
            cursor = database.query(SocialAccountDBOpenHelper.TABLE_SOCIAL, allColumns,
                    null, null, null, null, null);
            SharedPreferences sharedPreferences=myContext.getSharedPreferences(MySharedPreferences.MyPREFERENCES,Context.MODE_PRIVATE);
            String seed=SecurityActivity.decrypt(MySharedPreferences.seed, sharedPreferences.getString(MySharedPreferences.Salt, MySharedPreferences.seed));
            Log.i(LOG_CAT, "Returned " + cursor.getCount() + " rows");
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    SocialAccount socialAccount = new SocialAccount();
                    socialAccount.setId(cursor.getInt(cursor.getColumnIndex(SocialAccountDBOpenHelper.COLUMN_ID)));
                    socialAccount.setEmail(cursor.getString(cursor.getColumnIndex(SocialAccountDBOpenHelper.COLUMN_EMAIL)));
                    socialAccount.setPassword(SecurityActivity.decrypt(seed, cursor.getString(cursor.getColumnIndex(SocialAccountDBOpenHelper.COLUMN_PASSWORD))));
                    socialAccount.setDispName(cursor.getString(cursor.getColumnIndex(SocialAccountDBOpenHelper.COLUMN_DISPLAY_NAME)));
                    socialAccount.setDomainName(cursor.getString(cursor.getColumnIndex(SocialAccountDBOpenHelper.COLUMN_EMAIL_DOMAIN)));
                    socialAccounts.add(socialAccount);
                }
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return socialAccounts;
    }


    /*decrypt before retrieving
    * retrieves only one row*/
    public SocialAccount getDataByEmailAddress(String email) {
        SocialAccount socialAccount = new SocialAccount();
        Cursor cursor = database.query(SocialAccountDBOpenHelper.TABLE_SOCIAL, allColumns,
                null, null, null, null, null);

        Log.i(LOG_CAT, "Returned " + cursor.getCount() + " rows");
        try {
            SharedPreferences sharedPreferences=myContext.getSharedPreferences(MySharedPreferences.MyPREFERENCES,Context.MODE_PRIVATE);
            String seed=SecurityActivity.decrypt(MySharedPreferences.seed, sharedPreferences.getString(MySharedPreferences.Salt, MySharedPreferences.seed));
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    if (cursor.getString(cursor.getColumnIndex(SocialAccountDBOpenHelper.COLUMN_EMAIL)).equals(email)){
                        socialAccount.setEmail(cursor.getString(cursor.getColumnIndex(SocialAccountDBOpenHelper.COLUMN_EMAIL)));
                        socialAccount.setPassword(SecurityActivity.decrypt(seed, cursor.getString(cursor.getColumnIndex(SocialAccountDBOpenHelper.COLUMN_PASSWORD))));
                        socialAccount.setDispName(cursor.getString(cursor.getColumnIndex(SocialAccountDBOpenHelper.COLUMN_DISPLAY_NAME)));
                        socialAccount.setDomainName(cursor.getString(cursor.getColumnIndex(SocialAccountDBOpenHelper.COLUMN_EMAIL_DOMAIN)));
                        cursor.close();
                        return socialAccount;
                    }
                }
            }else{
                Log.e(LOG_CAT,"Database empty");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        cursor.close();
        return socialAccount;
    }

    public SocialAccount getDataByRowId(int id) {
        SocialAccount socialAccount = new SocialAccount();
        Cursor cursor = database.query(SocialAccountDBOpenHelper.TABLE_SOCIAL, allColumns,
                null, null, null, null, null);

        Log.i(LOG_CAT, "Returned " + cursor.getCount() + " rows");
        try {
            SharedPreferences sharedPreferences=myContext.getSharedPreferences(MySharedPreferences.MyPREFERENCES,Context.MODE_PRIVATE);
            String seed=SecurityActivity.decrypt(MySharedPreferences.seed, sharedPreferences.getString(MySharedPreferences.Salt, MySharedPreferences.seed));
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    if (cursor.getInt(cursor.getColumnIndex(SocialAccountDBOpenHelper.COLUMN_ID))==id){
                        socialAccount.setEmail(cursor.getString(cursor.getColumnIndex(SocialAccountDBOpenHelper.COLUMN_EMAIL)));
                        socialAccount.setPassword(SecurityActivity.decrypt(seed, cursor.getString(cursor.getColumnIndex(SocialAccountDBOpenHelper.COLUMN_PASSWORD))));
                        socialAccount.setDispName(cursor.getString(cursor.getColumnIndex(SocialAccountDBOpenHelper.COLUMN_DISPLAY_NAME)));
                        socialAccount.setDomainName(cursor.getString(cursor.getColumnIndex(SocialAccountDBOpenHelper.COLUMN_EMAIL_DOMAIN)));
                        cursor.close();
                        return socialAccount;
                    }
                }
            }else{
                Log.e(LOG_CAT,"Database empty");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        cursor.close();
        return socialAccount;
    }


    public boolean isDataPresent(String value,String whereClause) {
        String selection=whereClause+" = ? ";
        String[] selectionArgs={value};

        Cursor cursor = database.query(SocialAccountDBOpenHelper.TABLE_SOCIAL, allColumns,
                selection, selectionArgs, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    // Getting All data
    public List<String> getAllDisplayName() {
        List<String> accountsListsInDb = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SocialAccountDBOpenHelper.TABLE_SOCIAL;

        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String name=cursor.getString(cursor.getColumnIndex(SocialAccountDBOpenHelper.COLUMN_DISPLAY_NAME));
                // Adding contact to list
                accountsListsInDb.add(name);
            } while (cursor.moveToNext());
        }

        // return contact list
        cursor.close();
        return accountsListsInDb;
    }

    // Getting All Contacts
    public Map<String,String> getAllEmails() {
        Map <String,String> accountsListsInDb = new HashMap<String,String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SocialAccountDBOpenHelper.TABLE_SOCIAL;

        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String myemail=cursor.getString(cursor.getColumnIndex(SocialAccountDBOpenHelper.COLUMN_EMAIL));
                String unique_id=String.valueOf(cursor.getInt(cursor.getColumnIndex(SocialAccountDBOpenHelper.COLUMN_ID)));
                String key = unique_id;
                // Adding contact to list
                accountsListsInDb.put(key,myemail);
            } while (cursor.moveToNext());
        }

        // return contact list
        cursor.close();
        return accountsListsInDb;
    }

    public int deleteRow(int id){
        String table=SocialAccountDBOpenHelper.TABLE_SOCIAL;
        String whereClause=SocialAccountDBOpenHelper.COLUMN_ID+" = ? ";
        String[] whereArgs={String.valueOf(id)};

        if (isDataPresent(String.valueOf(id),SocialAccountDBOpenHelper.COLUMN_ID)){
            return database.delete(table,whereClause,whereArgs);
        }else {
            return 0;
        }

    }

    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + SocialAccountDBOpenHelper.TABLE_SOCIAL;
        Cursor cursor = database.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public List<Integer> getListOfImgageId(List<String> list){
        List<Integer> imgIds=new ArrayList<Integer>();
        String selection=SocialAccountDBOpenHelper.COLUMN_ID+" = ? ";
        String domainName;
        String[] column={SocialAccountDBOpenHelper.COLUMN_EMAIL_DOMAIN};

        for (Iterator<String> i=list.iterator();i.hasNext();){
            String item=i.next();

            String[] selectionArgs={item};
            Cursor cursor = database.query(SocialAccountDBOpenHelper.TABLE_SOCIAL, column,
                    selection, selectionArgs, null, null, null);
            cursor.moveToFirst();
            //int count=cursor.getCount();
            if (cursor.getCount()==1) {
                domainName=cursor.getString(cursor.getColumnIndex(SocialAccountDBOpenHelper.COLUMN_EMAIL_DOMAIN));
                if (myMap.get(domainName)!=null) {
                    imgIds.add(Integer.parseInt(myMap.get(domainName)));
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
}
