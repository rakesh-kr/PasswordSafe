package com.rakeshkr.passwordsafe.Ecommerce;


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

public class EcommerceDataSource {

    public static final String LOG_CAT="PasswordSafe";
    //public String seed="hfhgfdtrdhhjhgfytfytdtrdjhyugtff";
    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;
    Context myContext;
    private static final Map<String, String> myMap;
    static
    {
        myMap = new HashMap<String, String>();
        myMap.put("Flipkart", String.valueOf(R.drawable.ic_flipkart));
        myMap.put("Myntra", String.valueOf(R.drawable.ic_myntra));
        myMap.put("Jabong",String.valueOf(R.drawable.ic_jabong));
        myMap.put("Amazon",String.valueOf(R.drawable.ic_amazon));
        myMap.put("SnapDeal",String.valueOf(R.drawable.ic_snapdeal));
        myMap.put("Freecharge",String.valueOf(R.drawable.ic_freecharge));
        myMap.put("Paytm",String.valueOf(R.drawable.ic_paytm));
        myMap.put("Others",String.valueOf(R.drawable.ic_ecommerce));
        myMap.put("ShopClues",String.valueOf(R.drawable.ic_shopclues));
    }

    private static final String[] allColumns = {
            EcommerceDBOpenHelper.COLUMN_ID,
            EcommerceDBOpenHelper.COLUMN_EMAIL,
            EcommerceDBOpenHelper.COLUMN_PASSWORD,
            EcommerceDBOpenHelper.COLUMN_WEBSITE,
            EcommerceDBOpenHelper.COLUMN_SELLER_NAME,
            EcommerceDBOpenHelper.COLUMN_DISPLAY_NAME};

    public EcommerceDataSource(Context context) {
        dbhelper = new EcommerceDBOpenHelper(context);
        myContext=context;
    }

    public void open() {
        Log.i(LOG_CAT, "eCommerce a/c Database opened");
        database = dbhelper.getWritableDatabase();
    }

    public void close() {
        Log.i(LOG_CAT, "Ecommerce a/c Database closed");
        dbhelper.close();
    }

    public boolean deleteDatabase(Context context){
        return context.deleteDatabase("e_commerce.db");
    }

    public int updatedb(EcommerceAccount ecommerceAccount,int id){
        int rowsAffected=0;
        String table=EcommerceDBOpenHelper.TABLE_ECOM;
        ContentValues values=new ContentValues();

        try {
            SharedPreferences sharedPreferences=myContext.getSharedPreferences(MySharedPreferences.MyPREFERENCES,Context.MODE_PRIVATE);
            String seed=SecurityActivity.decrypt(MySharedPreferences.seed, sharedPreferences.getString(MySharedPreferences.Salt, MySharedPreferences.seed));
            values.put(EcommerceDBOpenHelper.COLUMN_EMAIL, ecommerceAccount.getEmail());
            values.put(EcommerceDBOpenHelper.COLUMN_PASSWORD, SecurityActivity.encrypt(seed, ecommerceAccount.getPassword()));
            values.put(EcommerceDBOpenHelper.COLUMN_DISPLAY_NAME, ecommerceAccount.getDispName());
            values.put(EcommerceDBOpenHelper.COLUMN_WEBSITE, ecommerceAccount.getWebsiteName());
            values.put(EcommerceDBOpenHelper.COLUMN_SELLER_NAME,ecommerceAccount.getSellerName());
            rowsAffected=database.update(table,values,EcommerceDBOpenHelper.COLUMN_ID+"='"+id+"'",null);
            return rowsAffected;
        }catch (Exception e){
            e.printStackTrace();
        }
        return rowsAffected;
    }

    public EcommerceAccount addNewRow(EcommerceAccount ecommerceAccount) {
        ContentValues values = new ContentValues();
        try{
            SharedPreferences sharedPreferences=myContext.getSharedPreferences(MySharedPreferences.MyPREFERENCES,Context.MODE_PRIVATE);
            String seed=SecurityActivity.decrypt(MySharedPreferences.seed, sharedPreferences.getString(MySharedPreferences.Salt, MySharedPreferences.seed));
            values.put(EcommerceDBOpenHelper.COLUMN_EMAIL, ecommerceAccount.getEmail());
            values.put(EcommerceDBOpenHelper.COLUMN_PASSWORD, SecurityActivity.encrypt(seed, ecommerceAccount.getPassword()));
            values.put(EcommerceDBOpenHelper.COLUMN_WEBSITE, ecommerceAccount.getWebsiteName());
            values.put(EcommerceDBOpenHelper.COLUMN_SELLER_NAME,ecommerceAccount.getSellerName());
            values.put(EcommerceDBOpenHelper.COLUMN_DISPLAY_NAME, ecommerceAccount.getDispName());
        long insertid = database.insert(EcommerceDBOpenHelper.TABLE_ECOM, null, values);
            ecommerceAccount.setId(insertid);
        }catch (SQLiteConstraintException e){
            Log.e(LOG_CAT, "Duplicate entry" + ecommerceAccount.getEmail());
        }catch (Exception e){
            e.printStackTrace();
        }

        return ecommerceAccount;
    }

    public List<EcommerceAccount> findAll() {
        List<EcommerceAccount> ecommerceAccounts = new ArrayList<EcommerceAccount>();
        Cursor cursor=null;
        try {
            cursor = database.query(EcommerceDBOpenHelper.TABLE_ECOM, allColumns,
                    null, null, null, null, null);
            SharedPreferences sharedPreferences=myContext.getSharedPreferences(MySharedPreferences.MyPREFERENCES,Context.MODE_PRIVATE);
            String seed=SecurityActivity.decrypt(MySharedPreferences.seed, sharedPreferences.getString(MySharedPreferences.Salt, MySharedPreferences.seed));
            Log.i(LOG_CAT, "Returned " + cursor.getCount() + " rows");
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    EcommerceAccount ecommerceAccount = new EcommerceAccount();
                    ecommerceAccount.setId(cursor.getInt(cursor.getColumnIndex(EcommerceDBOpenHelper.COLUMN_ID)));
                    ecommerceAccount.setEmail(cursor.getString(cursor.getColumnIndex(EcommerceDBOpenHelper.COLUMN_EMAIL)));
                    ecommerceAccount.setPassword(SecurityActivity.decrypt(seed, cursor.getString(cursor.getColumnIndex(EcommerceDBOpenHelper.COLUMN_PASSWORD))));
                    ecommerceAccount.setDispName(cursor.getString(cursor.getColumnIndex(EcommerceDBOpenHelper.COLUMN_DISPLAY_NAME)));
                    ecommerceAccount.setWebsiteName(cursor.getString(cursor.getColumnIndex(EcommerceDBOpenHelper.COLUMN_WEBSITE)));
                    ecommerceAccount.setSellerName(cursor.getString(cursor.getColumnIndex(EcommerceDBOpenHelper.COLUMN_SELLER_NAME)));
                    ecommerceAccounts.add(ecommerceAccount);
                }
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return ecommerceAccounts;
    }


    /*decrypt before retrieving
    * retrieves only one row*/
    public EcommerceAccount getDataByEmailAddress(String email) {
        EcommerceAccount ecommerceAccount = new EcommerceAccount();
        Cursor cursor = database.query(EcommerceDBOpenHelper.TABLE_ECOM, allColumns,
                null, null, null, null, null);

        Log.i(LOG_CAT, "Returned " + cursor.getCount() + " rows");
        try {
            SharedPreferences sharedPreferences=myContext.getSharedPreferences(MySharedPreferences.MyPREFERENCES,Context.MODE_PRIVATE);
            String seed=SecurityActivity.decrypt(MySharedPreferences.seed, sharedPreferences.getString(MySharedPreferences.Salt, MySharedPreferences.seed));
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    if (cursor.getString(cursor.getColumnIndex(EcommerceDBOpenHelper.COLUMN_EMAIL)).equals(email)){
                        ecommerceAccount.setEmail(cursor.getString(cursor.getColumnIndex(EcommerceDBOpenHelper.COLUMN_EMAIL)));
                        ecommerceAccount.setPassword(SecurityActivity.decrypt(seed, cursor.getString(cursor.getColumnIndex(EcommerceDBOpenHelper.COLUMN_PASSWORD))));
                        ecommerceAccount.setDispName(cursor.getString(cursor.getColumnIndex(EcommerceDBOpenHelper.COLUMN_DISPLAY_NAME)));
                        ecommerceAccount.setWebsiteName(cursor.getString(cursor.getColumnIndex(EcommerceDBOpenHelper.COLUMN_WEBSITE)));
                        ecommerceAccount.setSellerName(cursor.getString(cursor.getColumnIndex(EcommerceDBOpenHelper.COLUMN_SELLER_NAME)));
                        cursor.close();
                        return ecommerceAccount;
                    }
                }
            }else{
                Log.e(LOG_CAT,"Database empty");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        cursor.close();
        return ecommerceAccount;
    }

    public EcommerceAccount getDataByRowId(int id) {
        EcommerceAccount ecommerceAccount = new EcommerceAccount();
        Cursor cursor = database.query(EcommerceDBOpenHelper.TABLE_ECOM, allColumns,
                null, null, null, null, null);

        Log.i(LOG_CAT, "Returned " + cursor.getCount() + " rows");
        try {
            SharedPreferences sharedPreferences=myContext.getSharedPreferences(MySharedPreferences.MyPREFERENCES,Context.MODE_PRIVATE);
            String seed=SecurityActivity.decrypt(MySharedPreferences.seed, sharedPreferences.getString(MySharedPreferences.Salt, MySharedPreferences.seed));
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    if (cursor.getInt(cursor.getColumnIndex(EcommerceDBOpenHelper.COLUMN_ID))==id){
                        ecommerceAccount.setEmail(cursor.getString(cursor.getColumnIndex(EcommerceDBOpenHelper.COLUMN_EMAIL)));
                        ecommerceAccount.setPassword(SecurityActivity.decrypt(seed, cursor.getString(cursor.getColumnIndex(EcommerceDBOpenHelper.COLUMN_PASSWORD))));
                        ecommerceAccount.setDispName(cursor.getString(cursor.getColumnIndex(EcommerceDBOpenHelper.COLUMN_DISPLAY_NAME)));
                        ecommerceAccount.setWebsiteName(cursor.getString(cursor.getColumnIndex(EcommerceDBOpenHelper.COLUMN_WEBSITE)));
                        ecommerceAccount.setSellerName(cursor.getString(cursor.getColumnIndex(EcommerceDBOpenHelper.COLUMN_SELLER_NAME)));
                        cursor.close();
                        return ecommerceAccount;
                    }
                }
            }else{
                Log.e(LOG_CAT,"Database empty");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        cursor.close();
        return ecommerceAccount;
    }


    public boolean isDataPresent(String value,String whereClause) {
        List<EcommerceAccount> banks = new ArrayList<EcommerceAccount>();
        String selection=whereClause+" = ? ";
        String[] selectionArgs={value};
        Cursor cursor = database.query(EcommerceDBOpenHelper.TABLE_ECOM, allColumns,
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
        String selectQuery = "SELECT  * FROM " + EcommerceDBOpenHelper.TABLE_ECOM;

        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String name=cursor.getString(cursor.getColumnIndex(EcommerceDBOpenHelper.COLUMN_DISPLAY_NAME));
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
        String selectQuery = "SELECT  * FROM " + EcommerceDBOpenHelper.TABLE_ECOM;

        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String myemail=cursor.getString(cursor.getColumnIndex(EcommerceDBOpenHelper.COLUMN_EMAIL));
                String unique_id=String.valueOf(cursor.getInt(cursor.getColumnIndex(EcommerceDBOpenHelper.COLUMN_ID)));
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
        String table=EcommerceDBOpenHelper.TABLE_ECOM;
        String whereClause=EcommerceDBOpenHelper.COLUMN_ID+" = ? ";
        String[] whereArgs={String.valueOf(id)};

        if (isDataPresent(String.valueOf(id),EcommerceDBOpenHelper.COLUMN_ID)){
            return database.delete(table,whereClause,whereArgs);
        }else {
            return 0;
        }

    }

    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + EcommerceDBOpenHelper.TABLE_ECOM;
        Cursor cursor = database.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public List<Integer> getListOfImgageId(List<String> list){
        List<Integer> imgIds=new ArrayList<Integer>();
        String selection=EcommerceDBOpenHelper.COLUMN_ID+" = ? ";
        String sellerName;
        String[] column={EcommerceDBOpenHelper.COLUMN_SELLER_NAME};

        for (Iterator<String> i=list.iterator();i.hasNext();){
            String item=i.next();
            String[] selectionArgs={item};
            Cursor cursor = database.query(EcommerceDBOpenHelper.TABLE_ECOM, column,
                    selection, selectionArgs, null, null, null);
            cursor.moveToFirst();
            //int count=cursor.getCount();
            if (cursor.getCount()==1) {
                sellerName=cursor.getString(cursor.getColumnIndex(EcommerceDBOpenHelper.COLUMN_SELLER_NAME));
                if (myMap.get(sellerName)!=null) {
                    imgIds.add(Integer.parseInt(myMap.get(sellerName)));
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
