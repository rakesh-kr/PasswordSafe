package com.rakeshkr.passwordsafe.ApplicationSpecific;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.rakeshkr.passwordsafe.Bank.BankDataSource;
import com.rakeshkr.passwordsafe.Ecommerce.EcommerceDataSource;
import com.rakeshkr.passwordsafe.R;
import com.rakeshkr.passwordsafe.Utility.MySharedPreferences;
import com.rakeshkr.passwordsafe.Utility.MyUtility;
import com.rakeshkr.passwordsafe.SocialAccount.SocialAccountDataSource;
import com.rakeshkr.passwordsafe.Utility.SecurityActivity;


public class ApplicationSummaryActivity extends ActionBarActivity{
    String LOG_CAT="PasswordSafe";
    EditText nameEditTxt,emailAddressEditTxt;
    TextView bankCountTxtView,socialCountTxtView,lastModifiedTxtView,ecommerceCountTxtView;
    EditText mobileNumEditTxt;
    String name=null,emailAddress=null,bankCount=null,socialCount=null,lastModified=null,ecommerceCount=null,mobileNum=null;
    String seed="hfhgfdtrdhhjhgfytfytdtrdjhyugtff";
    MyUtility myUtility=new MyUtility();

    SharedPreferences sharedPreferences;

    BankDataSource bankDataSource;
    SocialAccountDataSource socialAccountDataSource;
    EcommerceDataSource ecommerceDataSource;

    boolean isDoneButtonEnabled=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application_summary);
        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (Exception e){
            Log.i(LOG_CAT, "Exception while setting back button");
        }

        nameEditTxt=(EditText)findViewById(R.id.name_editTxt);
        emailAddressEditTxt=(EditText)findViewById(R.id.email_address_editText);
        bankCountTxtView=(TextView)findViewById(R.id.display_no_of_bank_detail);
        socialCountTxtView=(TextView)findViewById(R.id.display_no_of_social_detail);
        ecommerceCountTxtView=(TextView)findViewById(R.id.display_no_of_ecommerce_detail);
        lastModifiedTxtView=(TextView)findViewById(R.id.display_last_modified_date);
        mobileNumEditTxt=(EditText)findViewById(R.id.mobile_number_editTxt);

        bankDataSource=new BankDataSource(ApplicationSummaryActivity.this);
        socialAccountDataSource=new SocialAccountDataSource(ApplicationSummaryActivity.this);
        ecommerceDataSource=new EcommerceDataSource(ApplicationSummaryActivity.this);
        bankDataSource.open();
        socialAccountDataSource.open();
        ecommerceDataSource.open();
        sharedPreferences=getSharedPreferences(MySharedPreferences.MyPREFERENCES, MODE_PRIVATE);
        emailAddress=sharedPreferences.getString(MySharedPreferences.Email, null);
        name=sharedPreferences.getString(MySharedPreferences.Name, null);
        try {
            mobileNum=SecurityActivity.decrypt(seed,sharedPreferences.getString(MySharedPreferences.Mobile,""));
        }catch (Exception e){
            e.printStackTrace();
        }

        bankCount=String.valueOf(bankDataSource.getRowCount());
        socialCount=String.valueOf(socialAccountDataSource.getRowCount());
        ecommerceCount=String.valueOf(ecommerceDataSource.getRowCount());
        lastModified=sharedPreferences.getString(MySharedPreferences.PasswordLastModified, null);

        emailAddressEditTxt.setText(emailAddress);
        nameEditTxt.setText(name);
        bankCountTxtView.setText(bankCount);
        socialCountTxtView.setText(socialCount);
        ecommerceCountTxtView.setText(ecommerceCount);
        lastModifiedTxtView.setText(lastModified);
        mobileNumEditTxt.setText(mobileNum);

        bankDataSource.close();
        socialAccountDataSource.close();
        ecommerceDataSource.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent i=new Intent(getApplicationContext(),SettingsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                myUtility.finishTask(ApplicationSummaryActivity.this);
                return true;
            case R.id.action_edit:
                editMode();
                return true;
            case R.id.action_done:
                disableMode();
                updateValues();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateValues() {
        try{
            String oldEmail;
            oldEmail=emailAddress;
            name=nameEditTxt.getText().toString().trim();
            emailAddress=emailAddressEditTxt.getText().toString().trim();
            mobileNum=mobileNumEditTxt.getText().toString().trim();
            if (name.length()>0 && emailAddress.length()>0 && mobileNum.length()>0) {
                sharedPreferences = addKeyValuePair(MySharedPreferences.Name, name, sharedPreferences, false);
                sharedPreferences = addKeyValuePair(MySharedPreferences.Mobile, mobileNum, sharedPreferences, true);
                sharedPreferences = addKeyValuePair(MySharedPreferences.Email, emailAddress, sharedPreferences, false);
                myUtility.createToast(getApplicationContext(),"Updated profile information");
                String[] toArr = {oldEmail};
                if (!oldEmail.equals(emailAddress)) {
                    myUtility.composeMailSend(getApplicationContext(), toArr, "Email Address change, Do not reply to this mail...", "Recently your email address " + oldEmail + " is changed to " + emailAddress + " from your Password safe application.\n" + "If it is you then kindly ignore this mail");
                }
            }else {
                myUtility.createToast(getApplicationContext(),"Some fields are empty");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void disableMode() {
        myUtility.disableEditText(emailAddressEditTxt);
        myUtility.disableEditText(mobileNumEditTxt);
        myUtility.disableEditText(nameEditTxt);
    }

    private SharedPreferences addKeyValuePair(String key, String value,SharedPreferences sharedpreferences,boolean storeEncrypted) {
        String seed="hfhgfdtrdhhjhgfytfytdtrdjhyugtff";
        SharedPreferences.Editor editor=sharedpreferences.edit();

        try {
            if(storeEncrypted) {
                value = SecurityActivity.encrypt(seed, value);

            }
            editor.putString(key, value);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return sharedpreferences;
    }

    private void editMode() {
        if (!emailAddressEditTxt.isEnabled()) {
            myUtility.enableEditText(emailAddressEditTxt);
        }
        if (!mobileNumEditTxt.isEnabled()){
            myUtility.enableEditText(mobileNumEditTxt);
        }
        if (!nameEditTxt.isEnabled()){
            myUtility.enableEditText(nameEditTxt);
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(),SettingsActivity.class);
        startActivity(intent);
        myUtility.finishTask(ApplicationSummaryActivity.this);
    }
}
