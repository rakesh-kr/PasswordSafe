package com.rakeshkr.passwordsafe.ApplicationSpecific;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rakeshkr.passwordsafe.R;
import com.rakeshkr.passwordsafe.Utility.MySharedPreferences;
import com.rakeshkr.passwordsafe.Utility.MyUtility;


public class ApplicationLocked extends ActionBarActivity{
    int numOfAttemps;
    EditText secretKey_editTxt;
    TextView lockInfo_txtView,warning_txtView;
    Button unlockBtn;
    String info;

    SharedPreferences sharedPreferences,sharedPreferences_notification_info;
    Vibrator vibrator;
    String enteredSectretKey,genaratedSecretKey;

    MyUtility myUtility=new MyUtility();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
        }
        setContentView(R.layout.account_locked);
        if (getIntent().getExtras().getString("attempts")==null){
            numOfAttemps=0;
        }else{
            numOfAttemps=getIntent().getExtras().getInt("attempts");
        }

        secretKey_editTxt=(EditText)findViewById(R.id.secretKey);
        lockInfo_txtView=(TextView)findViewById(R.id.lock_info);
        unlockBtn=(Button)findViewById(R.id.unlock);
        sharedPreferences=getSharedPreferences(MySharedPreferences.MyPREFERENCES, Context.MODE_PRIVATE);
        warning_txtView=(TextView)findViewById(R.id.warning);
        //blinking effect
        myUtility.blinkTextView(warning_txtView);


        info="Enter secret key sent to your "+sharedPreferences.getString(MySharedPreferences.Email,"Email Address");
        lockInfo_txtView.setText(info);
        sharedPreferences_notification_info=getSharedPreferences(MySharedPreferences.NOTIFICATIONPREFERENCES,Context.MODE_PRIVATE);
        vibrator=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        unlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enteredSectretKey=secretKey_editTxt.getText().toString().trim();
                genaratedSecretKey=sharedPreferences.getString(MySharedPreferences.SecretKeyGenerated,"not genarated");
                if (!genaratedSecretKey.equals("not genarated") && genaratedSecretKey.equals(enteredSectretKey)){
                    myUtility.createToast(getApplicationContext(),"Successfully unlocked");
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putInt(MySharedPreferences.Attempts, 0);
                    editor.commit();
                    Intent unlocked=new Intent(getApplicationContext(),LockActivity.class);
                    startActivity(unlocked);
                    finish();
                }else {
                    myUtility.createToast(getApplicationContext(),"Invalid secret key");
                    myUtility.shakeEditText(ApplicationLocked.this, secretKey_editTxt);
                    if (sharedPreferences_notification_info.getBoolean(MySharedPreferences.Vibration,false)) {
                        vibrator.vibrate(500);
                    }
                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                ApplicationLocked.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        ApplicationLocked.this.finish();
    }
}
