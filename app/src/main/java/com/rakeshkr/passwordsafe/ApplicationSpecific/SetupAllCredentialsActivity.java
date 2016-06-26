package com.rakeshkr.passwordsafe.ApplicationSpecific;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.TextView;

import com.rakeshkr.passwordsafe.R;
import com.rakeshkr.passwordsafe.Utility.MySharedPreferences;
import com.rakeshkr.passwordsafe.Utility.MyUtility;
import com.rakeshkr.passwordsafe.Utility.SecurityActivity;

public class SetupAllCredentialsActivity extends Activity {
    String intentString,userName,password,email,mobile,notFirstTime,curTime="",algo,salt;
    String[] intentArray;
    SharedPreferences sharedPreferences,sharedPreferences_app_info;
    MyUtility myUtility=new MyUtility();
    CheckBox username_ckb,password_ckb,email_ckb,mobile_ckb,algo_ckb,salt_ckb;
    TextView all_done;
    int done=1;
    Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup_all_credentials);
        intentString=getIntent().getExtras().getString("intentString");
        if (intentString != null) {
            intentArray=intentString.split(" ");
        }
        userName=intentArray[0];
        password=intentArray[1];
        email=intentArray[2];
        mobile=intentArray[3];
        algo=intentArray[4];
        salt=intentArray[5];
        myUtility.createToast(SetupAllCredentialsActivity.this,algo+" "+salt);

        all_done=(TextView)findViewById(R.id.all_setup_done);
        sharedPreferences = getSharedPreferences(MySharedPreferences.MyPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences_app_info=getSharedPreferences(MySharedPreferences.MyAPPINFO, Context.MODE_PRIVATE);
        notFirstTime=sharedPreferences_app_info.getString(MySharedPreferences.ISFIRSTTIME, null);


        curTime=myUtility.getDateTime("Registration");

        username_ckb=(CheckBox)findViewById(R.id.username_ckb);
        password_ckb=(CheckBox)findViewById(R.id.master_password_ckb);
        email_ckb=(CheckBox)findViewById(R.id.email_ckb);
        mobile_ckb=(CheckBox)findViewById(R.id.mobile_ckb);
        algo_ckb=(CheckBox)findViewById(R.id.algo_ckb);
        salt_ckb=(CheckBox)findViewById(R.id.salt_ckb);

        animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.right_to_left);
        username_ckb.startAnimation(animation);
        password_ckb.startAnimation(animation);
        email_ckb.startAnimation(animation);
        mobile_ckb.startAnimation(animation);
        algo_ckb.startAnimation(animation);
        salt_ckb.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                setupDisplay(MySharedPreferences.Name, userName, sharedPreferences, false, username_ckb);
                setupDisplay(MySharedPreferences.Password,password,sharedPreferences,true,password_ckb);
                setupDisplay(MySharedPreferences.Email, email, sharedPreferences, false, email_ckb);
                setupDisplay(MySharedPreferences.Mobile, mobile, sharedPreferences, true, mobile_ckb);
                setupDisplay(MySharedPreferences.EncryptionAlgorythm, algo, sharedPreferences, true, algo_ckb);
                setupDisplay(MySharedPreferences.Salt, salt, sharedPreferences, true, salt_ckb);
            }
        }, 10000);


        addKeyValuePair(MySharedPreferences.PasswordLastModified, curTime, sharedPreferences, false);
        if (addKeyValuePair(MySharedPreferences.ISFIRSTTIME, "TRUE", sharedPreferences_app_info, false)){
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (done == 1) {
                        all_done.setVisibility(View.VISIBLE);
                    } else {
                        all_done.setText("Some Error while setting up things..!");
                        all_done.setVisibility(View.VISIBLE);
                        all_done.setTextColor(Color.RED);
                    }
                    sendMail();
                    Intent success = new Intent(getApplicationContext(), LockActivity.class);
                    startActivity(success);
                    myUtility.finishTask(SetupAllCredentialsActivity.this);
                }
            }, 10000);


        }



    }

    private void sendMail() {
        try {
            String[] toArr = {email};
            String sub="Thank you for Registering with PasswordSafe App!!";
            String body="\nHi "+userName+","
                    +"\nYour Application credentials are as follows:\n"
                    + "\nUserName: " + userName
                    + "\nPassword: " + password
                    + "\nEmail address: " + email
                    + "\nMobile Number: " + mobile
                    + "\nAlgorythm: " + algo
                    + "\nSalt Text: " + salt;
            if (myUtility.composeMailSend(getApplicationContext(),toArr,sub,body)) {
                CreateNotificationActivity createNotificationActivity = new CreateNotificationActivity(getApplicationContext());
                createNotificationActivity.newNonClickableNotification("Profile updated", "Your application profile updated by " + userName);

            }else {
                myUtility.createToast(getApplicationContext(), "Unable to send mail to "+email);
            }
        }catch (Exception e){
            e.printStackTrace();

        }
    }

    private void setupDisplay(final String key,final String value,final SharedPreferences sharedPreferences,final boolean storedEncrypted,final CheckBox ckb){
                if (addKeyValuePair(key,value,sharedPreferences,storedEncrypted)){
                    ckb.setEnabled(true);
                    ckb.setChecked(true);
                    done=done*1;
                }else {
                    ckb.setChecked(false);
                    done=0;
                }


    }

    private boolean addKeyValuePair(String key, String value,SharedPreferences sharedpreferences,boolean storeEncrypted) {
        String seed=MySharedPreferences.seed;
        SharedPreferences.Editor editor=sharedpreferences.edit();

        try {
            if(storeEncrypted) {
                value = SecurityActivity.encrypt(seed, value);
            }
            editor.putString(key, value);
            editor.commit();

            return (sharedpreferences.getString(key," ")).equals(value);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
