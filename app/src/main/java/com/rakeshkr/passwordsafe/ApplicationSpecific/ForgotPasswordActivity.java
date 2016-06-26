package com.rakeshkr.passwordsafe.ApplicationSpecific;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rakeshkr.passwordsafe.Utility.AlertDialogManager;
import com.rakeshkr.passwordsafe.Utility.MySharedPreferences;
import com.rakeshkr.passwordsafe.Utility.MyUtility;
import com.rakeshkr.passwordsafe.R;
import com.rakeshkr.passwordsafe.Utility.SecurityActivity;

import java.util.Random;

public class ForgotPasswordActivity extends ActionBarActivity {

    String LOG_CAT="PasswordSafe";
    MyUtility myUtility=new MyUtility();
    EditText emailTxt;
    Button sendBtn;
    public static String registeredEmail=null;
    SharedPreferences sharedPreferences;
    AlertDialogManager alert=new AlertDialogManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_activity);
        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (Exception e){
            Log.i(LOG_CAT, "Exception while setting back button");
        }

        emailTxt=(EditText)findViewById(R.id.email_edtext);
        sendBtn=(Button)findViewById(R.id.send_button);


        sharedPreferences = getSharedPreferences(MySharedPreferences.MyPREFERENCES, Context.MODE_PRIVATE);
        String receiverMail= sharedPreferences.getString(MySharedPreferences.Email,null);
        final String receiverUserName=sharedPreferences.getString(MySharedPreferences.Name,null);
        if(receiverMail==null){
            emailTxt.setText("Not Registered");
        }else{
            emailTxt.setText(receiverMail);
        }
        emailTxt.setEnabled(false);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registeredEmail=emailTxt.getText().toString();
                if(registeredEmail.equals("")||registeredEmail.equals("Not Registered")){
                    alert.showAlertDialog(ForgotPasswordActivity.this,"Error","Email cannot be empty/ not registered with the app",false);
                }else{
                    String[] toArr = {registeredEmail};
                    Random randomPass= new Random();
                    int i=Math.abs(randomPass.nextInt(Integer.MAX_VALUE));
                    String newPassword=String.valueOf(i);
                    String body="Do not reply this email.\n" +
                            "Your newPassword is "+newPassword+"\n" +
                            "Please change the password using above new Password";
                    try{
                        if(myUtility.composeMailSend(getApplicationContext(),toArr,"Password Reset mail",body)){
                            sharedPreferences=updateKeyValuePair(MySharedPreferences.Name, receiverUserName, sharedPreferences,false);
                            sharedPreferences=updateKeyValuePair(MySharedPreferences.Password, newPassword, sharedPreferences,true);
                            sharedPreferences=updateKeyValuePair(MySharedPreferences.PasswordLastModified,myUtility.getDateTime("Forgot password module"),sharedPreferences,false);
                            Toast.makeText(ForgotPasswordActivity.this, "Email sent to "+registeredEmail, Toast.LENGTH_LONG).show();
                            myUtility.createToast(getApplicationContext(), "Please Login with new Password");
                            Intent goToChangePassword=new Intent(getApplicationContext(),LockActivity.class);
                            startActivity(goToChangePassword);
                            myUtility.finishTask(ForgotPasswordActivity.this);
                        }else{
                            Toast.makeText(ForgotPasswordActivity.this, "Email was not sent", Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e){
                        Log.e(LOG_CAT, "Could not send email", e);
                        Toast.makeText(ForgotPasswordActivity.this, "Could not send email", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    public SharedPreferences updateKeyValuePair(String key,String value,SharedPreferences sharedPreferences,boolean storeEncrypted){
        String seed="hfhgfdtrdhhjhgfytfytdtrdjhyugtff";
        SharedPreferences.Editor editor=sharedPreferences.edit();
        try{
            if(storeEncrypted){
                value = SecurityActivity.encrypt(seed, value);
            }
            editor.putString(key,value);
            editor.apply();

        }catch (Exception e){
            e.printStackTrace();
        }

        return sharedPreferences;
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(),LockActivity.class);
        startActivity(i);
        myUtility.finishTask(ForgotPasswordActivity.this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent i=new Intent(getApplicationContext(),LockActivity.class);
                startActivity(i);
                myUtility.finishTask(ForgotPasswordActivity.this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
