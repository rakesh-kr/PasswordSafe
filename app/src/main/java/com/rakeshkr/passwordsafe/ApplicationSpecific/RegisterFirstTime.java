package com.rakeshkr.passwordsafe.ApplicationSpecific;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.rakeshkr.passwordsafe.Utility.AlertDialogManager;
import com.rakeshkr.passwordsafe.Utility.GmailSender;
import com.rakeshkr.passwordsafe.Utility.MySharedPreferences;
import com.rakeshkr.passwordsafe.Utility.MyUtility;
import com.rakeshkr.passwordsafe.R;
import com.rakeshkr.passwordsafe.Utility.SecurityActivity;

public class RegisterFirstTime extends ActionBarActivity {
    String LOG_CAT="PasswordSafe";
    EditText passwordEditTxt,confirmPasswordEditTxt,emailEditTxt,userNameEditText,mobileNumEditText;
    String loginPassword=null,confirmPassword=null,emailAddress=null,notFirstTime=null,curTime="",username=null,mobileNum="";
    SharedPreferences sharedPreferences,sharedPreferences_app_info;

    AlertDialogManager alert=new AlertDialogManager();

    MyUtility myUtility=new MyUtility();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_first_activity);
        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (Exception e){
            Log.i(LOG_CAT, "Exception while setting back button");
            //Toast.makeText(getApplicationContext(),"Got exception",Toast.LENGTH_LONG).show();
        }
        passwordEditTxt=(EditText)findViewById(R.id.password_editText);
        confirmPasswordEditTxt=(EditText)findViewById(R.id.confirm_password_editText);
        emailEditTxt=(EditText)findViewById(R.id.email_address_editText);
        userNameEditText=(EditText)findViewById(R.id.user_name_editTxt);
        mobileNumEditText=(EditText)findViewById(R.id.mobile_editText);
        sharedPreferences = getSharedPreferences(MySharedPreferences.MyPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences_app_info=getSharedPreferences(MySharedPreferences.MyAPPINFO,Context.MODE_PRIVATE);
        notFirstTime=sharedPreferences_app_info.getString(MySharedPreferences.ISFIRSTTIME, null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_new_bank_social_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent i=new Intent(getApplicationContext(),LockActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                myUtility.finishTask(RegisterFirstTime.this);
                return true;
            case R.id.action_cancel:
                Intent intent=new Intent(getApplicationContext(),LockActivity.class);
                startActivity(intent);
                myUtility.finishTask(RegisterFirstTime.this);
                return true;
            case R.id.action_done:
                addLoginDetails();
                return true;
            case R.id.action_clear:
                clearEditTexts();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearEditTexts() {
        passwordEditTxt.setText("");
        confirmPasswordEditTxt.setText("");
        emailEditTxt.setText("");
        mobileNumEditText.setText("");
    }

    private void addLoginDetails() {
        loginPassword=passwordEditTxt.getText().toString().trim();
        confirmPassword=confirmPasswordEditTxt.getText().toString().trim();
        emailAddress=emailEditTxt.getText().toString().trim();
        curTime=myUtility.getDateTime("Registration");
        username=userNameEditText.getText().toString().trim();
        mobileNum=mobileNumEditText.getText().toString().trim();
        if (loginPassword.trim().length()>0 && confirmPassword.trim().length()>0 && emailAddress.trim().length()>0 && username.length()>0){
            if (loginPassword.equals(confirmPassword)){
                sharedPreferences=addKeyValuePair(MySharedPreferences.Name,username,sharedPreferences,false);
                sharedPreferences = addKeyValuePair(MySharedPreferences.Password, loginPassword, sharedPreferences,true);
                sharedPreferences = addKeyValuePair(MySharedPreferences.Email, emailAddress, sharedPreferences,false);
                sharedPreferences = addKeyValuePair(MySharedPreferences.PasswordLastModified,curTime,sharedPreferences,false);
                if (mobileNum.trim().length()<=0) {
                    mobileNum="Please add";
                }
                sharedPreferences = addKeyValuePair(MySharedPreferences.Mobile, mobileNum, sharedPreferences, true);
                sharedPreferences_app_info = addKeyValuePair(MySharedPreferences.ISFIRSTTIME, "TRUE", sharedPreferences_app_info,false);
                sendMail();
                Intent success=new Intent(getApplicationContext(),LockActivity.class);
                startActivity(success);
                myUtility.finishTask(RegisterFirstTime.this);
            }else {
                alert.showAlertDialog(RegisterFirstTime.this, "Password Mismatch", "Password and confirm password mis match", false);
            }
        }else {
            alert.showAlertDialog(RegisterFirstTime.this, "Login failed..", "Username/Password/Email is missing", false);
        }
    }

    private void sendMail() {
        String senderMail="app.safetreasure.rakesh@gmail.com";
        String senderPassword="jesus_362508312498";
        try {
            GmailSender m = new GmailSender(senderMail, senderPassword);
            String[] toArr = {emailAddress};
            m.setTo(toArr);
            m.setFrom(senderMail);
            m.setSubject("Thank you for Registering with PasswordSafe App!!");
            m.setBody("\nHi "+username
                    +"\nYour Application credentials are as follows:\n"
                    + "\nUserName: " + username
                    + "\nPassword: " + loginPassword
                    + "\nEmail address: " + emailAddress
                    + "\nMobile Number: " + mobileNum);
            if (m.send()) {
                CreateNotificationActivity createNotificationActivity = new CreateNotificationActivity(getApplicationContext());
                createNotificationActivity.newNonClickableNotification("Profile updated","Your application profile updated by "+username);
            }else {
                myUtility.createToast(getApplicationContext(), "Unable to send mail to "+emailAddress);
            }
        }catch (Exception e){
            e.printStackTrace();

        }
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

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(),LockActivity.class);
        startActivity(intent);
        myUtility.finishTask(RegisterFirstTime.this);
    }

}


