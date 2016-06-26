package com.rakeshkr.passwordsafe.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;


public class MySharedPreferences {
    public static final String MyPREFERENCES = "Credentials" ;
    public static String Name="myUserNameKey";
    public static String Password="myPasswordKey";
    public static String Email="myEmailKey";
    public static String PasswordLastModified="myPasswordLastModified";
    public static String Mobile="myMobileNumber";
    public static String Attempts="numOfAttempts";
    public static String SecretKeyGenerated="mySecretKeyGenareted";

    public static final String MyAPPINFO="APPInfo";
    public static String ISFIRSTTIME="isFirstTime";

    public static final String NOTIFICATIONPREFERENCES="NotificationPreference";
    public static String Vibration="vibrationSettings";
    public static String Sound="soundSettings";
    public static String ExpiryDate="expiryDate";
    public static String SmsOtp="sendOtpViaSms";
    public static String Salt="mySalt";
    public static String seed="hfhgfdtrdhhjhgfytfytdtrdjhyugtff";
    public static String EncryptionAlgorythm="myEncryptionAlgorythm";


    public boolean verifyPassword(String variable,Context context){

        String password=null;
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);
        try {
            password = SecurityActivity.decrypt(seed, sharedPreferences.getString(Password, null));
            return password.equals(variable);

        }catch (Exception e){
            Toast.makeText(context,"Exception while decrypting password",Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public boolean isOtpToMobileEnabled(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences(NOTIFICATIONPREFERENCES,Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean(SmsOtp,false)){
            return true;
        }else {
            return false;
        }
    }
}
