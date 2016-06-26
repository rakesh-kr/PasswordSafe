package com.rakeshkr.passwordsafe.ApplicationSpecific;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rakeshkr.passwordsafe.R;
import com.rakeshkr.passwordsafe.Utility.AlertDialogManager;
import com.rakeshkr.passwordsafe.Utility.MySharedPreferences;
import com.rakeshkr.passwordsafe.Utility.MyUtility;
import com.rakeshkr.passwordsafe.Utility.SecurityActivity;
import com.rakeshkr.passwordsafe.Utility.SmsSender;


public class ChangeEmailActivity extends ActionBarActivity{
    String LOG_CAT="PasswordSafe";
    EditText passwordEditTxt,newEmailEditTxt;
    Button change_email_button;
    String enteredPassword=null,oldPassword=null,emailAddress=null,oldEmailAddress="";
    SharedPreferences sharedPreferences;

    AlertDialogManager alert=new AlertDialogManager();
    private String seed="hfhgfdtrdhhjhgfytfytdtrdjhyugtff";
    SecurityActivity securityActivity=new SecurityActivity();
    MyUtility myUtility=new MyUtility();
    MySharedPreferences mySharedPreferences=new MySharedPreferences();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_email_activity);
        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (Exception e){
            Log.i(LOG_CAT, "Exception while setting back button");
        }
        passwordEditTxt=(EditText)findViewById(R.id.password_editText);
        newEmailEditTxt=(EditText)findViewById(R.id.new_email_editTxt);
        change_email_button=(Button)findViewById(R.id.change_email);
        sharedPreferences = getSharedPreferences(MySharedPreferences.MyPREFERENCES, MODE_PRIVATE);
        oldEmailAddress=sharedPreferences.getString(MySharedPreferences.Email,"");
        if (!oldEmailAddress.equals("")){
            newEmailEditTxt.setText(oldEmailAddress);
        }



        change_email_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredPassword = passwordEditTxt.getText().toString().trim();
                emailAddress = newEmailEditTxt.getText().toString().trim();
                try {
                    if (enteredPassword.length() > 0 && emailAddress.length() > 0) {
                        oldPassword = SecurityActivity.decrypt(seed, sharedPreferences.getString(MySharedPreferences.Password, null));
                        try {
                            if (oldPassword != null && oldPassword.equals(enteredPassword) && !emailAddress.equals(oldEmailAddress)) {
                                passwordEditTxt.setError(null);
                                sharedPreferences = addKeyValuePair(MySharedPreferences.Email, emailAddress, sharedPreferences, false);
                                String[] toArr = {oldEmailAddress};
                                String msg="Recently your email address " + oldEmailAddress + " is changed to " + emailAddress + " from your Password safe application.\n" +"If it is you then kindly ignore this mail";
                                myUtility.composeMailSend(getApplicationContext(),toArr,"Email Address change, Do not reply to this mail...",msg);
                                if (mySharedPreferences.isOtpToMobileEnabled(getApplicationContext())){
                                    SmsSender smsSender=new SmsSender(getApplicationContext());
                                    try {
                                        smsSender.setToPhNum(SecurityActivity.decrypt(seed,sharedPreferences.getString(MySharedPreferences.Mobile,"Please add")));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    String newMsg="Email id changed. New Email id for PasswordSafe Application is "+emailAddress;
                                    smsSender.setMsg(newMsg);
                                    if (smsSender.sendMsg()){
                                        myUtility.createToast(getApplicationContext(),"SMS sent to "+smsSender.getToPhNum());
                                    }
                                }

                                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                                startActivity(intent);
                                myUtility.finishTask(ChangeEmailActivity.this);
                            } else if (!oldPassword.equals(enteredPassword)) {
                                myUtility.createToast(getApplicationContext(), "incorrect password");
                                passwordEditTxt.setFocusable(true);
                                passwordEditTxt.setError("error");
                            } else if (emailAddress.equals(oldEmailAddress)) {
                                myUtility.createToast(getApplicationContext(), "Old email and new email are same");
                                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                                startActivity(intent);
                                myUtility.finishTask(ChangeEmailActivity.this);
                            }
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
                    } else {
                        //if edit text fields empty
                        alert.showAlertDialog(ChangeEmailActivity.this,"Empty fields","Some fields are missing",false);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent i=new Intent(getApplicationContext(),SettingsActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                myUtility.finishTask(ChangeEmailActivity.this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private SharedPreferences addKeyValuePair(String key, String value,SharedPreferences sharedpreferences,boolean storeEncrypted) {

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
        Intent i=new Intent(getApplicationContext(),SettingsActivity.class);
        startActivity(i);
        myUtility.finishTask(ChangeEmailActivity.this);
    }
}
