package com.rakeshkr.passwordsafe.ApplicationSpecific;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

import com.rakeshkr.passwordsafe.R;
import com.rakeshkr.passwordsafe.Utility.MySharedPreferences;
import com.rakeshkr.passwordsafe.Utility.MyUtility;


public class NotificationSettings extends ActionBarActivity{
    String LOG_CAT="PasswordSafe";

    Switch vibrationSwitch,soundSwitch,smsOtpSwitch;
    SharedPreferences sharedPreferences;
    TextView zero_TextView,max_expiryDate_TextView;
    MyUtility myUtility=new MyUtility();
    NumberPicker np;
    int max_expiry_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_setting);
        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (Exception e){
            Log.i(LOG_CAT, "Exception while setting back button");
        }
        vibrationSwitch=(Switch)findViewById(R.id.vibration);
        soundSwitch=(Switch)findViewById(R.id.sound);
        smsOtpSwitch=(Switch)findViewById(R.id.sms_otp_switch);
        zero_TextView=(TextView)findViewById(R.id.zero_textView);
        max_expiryDate_TextView=(TextView)findViewById(R.id.password_expiry_date);
        sharedPreferences=getSharedPreferences(MySharedPreferences.NOTIFICATIONPREFERENCES, Context.MODE_PRIVATE);

        setStatusOfSwitch(MySharedPreferences.Vibration,vibrationSwitch);
        setStatusOfSwitch(MySharedPreferences.Sound,soundSwitch);
        setStatusOfSwitch(MySharedPreferences.SmsOtp,smsOtpSwitch);
        setExpiryDate();


        vibrationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                modifySetting(isChecked, MySharedPreferences.Vibration);
                if (sharedPreferences.getBoolean(MySharedPreferences.Vibration, false)) {
                    myUtility.createToast(getApplicationContext(), "Vibration is ON");
                } else {
                    myUtility.createToast(getApplicationContext(), "Vibration is OFF");
                }
            }
        });

        soundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                modifySetting(isChecked,MySharedPreferences.Sound);
            }
        });

        zero_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpMyWindow(zero_TextView.getText().toString()).show();

            }
        });

        max_expiryDate_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpMyWindow(zero_TextView.getText().toString()).show();
            }
        });

        smsOtpSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AlertDialog dialog = AskOption();
                    dialog.show();
                }
                modifySetting(isChecked,MySharedPreferences.SmsOtp);
            }
        });
    }


    private AlertDialog AskOption(){
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                .setTitle("Warning..!")
                .setMessage("Enabling this option may incur SMS cost based on your SMS pack! ")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;
    }


    private AlertDialog popUpMyWindow(String storedValue) {
        LayoutInflater numberPicker_layout=LayoutInflater.from(NotificationSettings.this);
        View numberPickerView =numberPicker_layout.inflate(R.layout.picket_dialog_for_expiry_date_remiander, null);
        np=(NumberPicker)numberPickerView.findViewById(R.id.numberPicker);
        np.setMaxValue(60);
        np.setMinValue(1);
        np.setWrapSelectorWheel(true);
        if (!storedValue.equals("Not Set")){
            np.setValue(Integer.parseInt(storedValue));
        }
        max_expiry_date=np.getValue();
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                max_expiry_date = newVal;
            }
        });
        return new AlertDialog.Builder(this)
                .setView(numberPickerView)
                .setTitle("Set expiry date")
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //need below line to support edit filed updatation
                        np.clearFocus();
                        max_expiry_date=np.getValue();
                        modifyExpiryDate(max_expiry_date);
                        zero_TextView.setText(String.valueOf(max_expiry_date));
                        myUtility.createToast(getApplicationContext(), "expiry date set to " + String.valueOf(max_expiry_date));

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        myUtility.createToast(getApplicationContext(), "Nothing is set");
                        dialog.dismiss();
                    }
                })
                .create();
    }

    private void modifyExpiryDate(int expiryDate) {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt(MySharedPreferences.ExpiryDate,expiryDate);
        editor.commit();

    }

    private void setStatusOfSwitch(String key,Switch mySwitch) {
        if (sharedPreferences.getBoolean(key,false)){
            mySwitch.setChecked(true);
        }else {
            mySwitch.setChecked(false);
        }
    }

    private void setExpiryDate() {
        int date=sharedPreferences.getInt(MySharedPreferences.ExpiryDate, 0);
        if (date!=0){
            zero_TextView.setText(String.valueOf(date));
        }else{
            zero_TextView.setText("Not Set");
        }
    }

    private void modifySetting(boolean isOn,String key) {

        SharedPreferences.Editor editor=sharedPreferences.edit();
        if (isOn){
            editor.putBoolean(key, true);
        }else {
            editor.putBoolean(key, false);
        }
        editor.commit();

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent i=new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(i);
                myUtility.finishTask(NotificationSettings.this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(),SettingsActivity.class);
        startActivity(i);
        myUtility.finishTask(NotificationSettings.this);
    }
}
