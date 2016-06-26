package com.rakeshkr.passwordsafe.ApplicationSpecific;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.rakeshkr.passwordsafe.Utility.AlertDialogManager;
import com.rakeshkr.passwordsafe.Utility.MySharedPreferences;
import com.rakeshkr.passwordsafe.Utility.MyUtility;
import com.rakeshkr.passwordsafe.R;
import com.rakeshkr.passwordsafe.Utility.SecurityActivity;

public class ChangePasswordActivity extends ActionBarActivity {
    String LOG_CAT="PasswordSafe";

    SecurityActivity securityActivity=new SecurityActivity();
    private String seed="hfhgfdtrdhhjhgfytfytdtrdjhyugtff";
    SharedPreferences sharedpreferences,sharedPreferences_app_info;
    AlertDialogManager alert=new AlertDialogManager();
    Drawable originalEditText;
    EditText oldPassword_editTxt,newPassword_editTxt,confirmPassword_editTxt;
    Button change_password_btn;
    CheckBox show_password_ckb;
    MyUtility myUtility=new MyUtility();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (Exception e){
            Log.i(LOG_CAT, "Exception while setting back button");
        }

        oldPassword_editTxt=(EditText)findViewById(R.id.old_password_editText);
        newPassword_editTxt=(EditText)findViewById(R.id.new_password_editText);
        confirmPassword_editTxt=(EditText)findViewById(R.id.confirm_password_editText);
        originalEditText=confirmPassword_editTxt.getBackground();
        change_password_btn=(Button)findViewById(R.id.change_password);
        show_password_ckb=(CheckBox)findViewById(R.id.show_password);

        confirmPassword_editTxt.addTextChangedListener(confirmPasswordWatcher);

        sharedPreferences_app_info=getSharedPreferences(MySharedPreferences.MyAPPINFO, Context.MODE_PRIVATE);
        final String is_not_first=sharedPreferences_app_info.getString(MySharedPreferences.ISFIRSTTIME, null);
        if(is_not_first==null){
            Toast.makeText(getApplicationContext(),"Your are not a registered User",Toast.LENGTH_LONG).show();
            Intent registerFirst=new Intent(getApplicationContext(),LockActivity.class);
            startActivity(registerFirst);
            myUtility.finishTask(ChangePasswordActivity.this);
        }


        change_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPassword=oldPassword_editTxt.getText().toString().trim();
                String newPassword=newPassword_editTxt.getText().toString();
                String confirmPassword=confirmPassword_editTxt.getText().toString();
                if(oldPassword.trim().length()>0 && newPassword.trim().length()>0 && confirmPassword.trim().length()>0){
                    sharedpreferences=getSharedPreferences(MySharedPreferences.MyPREFERENCES,Context.MODE_PRIVATE);
                    String storedPassword=sharedpreferences.getString(MySharedPreferences.Password,null);
                    try{
                        storedPassword=SecurityActivity.decrypt(seed,storedPassword);
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"exception while decrypting password in change password class",Toast.LENGTH_LONG).show();
                    }
                    if(storedPassword.equals(oldPassword)){
                        if(newPassword.equals(confirmPassword) && !newPassword.equals(oldPassword)){
                            sharedpreferences=updateKeyValuePair(MySharedPreferences.Password,newPassword,sharedpreferences,true);
                            sharedpreferences=updateKeyValuePair(MySharedPreferences.PasswordLastModified,myUtility.getDateTime(sharedpreferences.getString(MySharedPreferences.Name,"")),sharedpreferences,false);
                            CreateNotificationActivity createNotificationActivity=new CreateNotificationActivity(getApplicationContext());
                            createNotificationActivity.newClickableNotification("Password Change", "Login with new password " + newPassword);
                            Intent i=new Intent(getApplicationContext(),LockActivity.class);
                            startActivity(i);
                            myUtility.finishTask(ChangePasswordActivity.this);
                        }else if(newPassword.equals(oldPassword)){
                            oldPassword_editTxt.setError("matches with new password");
                            newPassword_editTxt.setError("matches with old password");
                            Toast.makeText(getApplicationContext(),"New Password and old password cannot be same",Toast.LENGTH_LONG).show();

                        }else if(!newPassword.equals(confirmPassword)){
                            newPassword_editTxt.setError("Do not match with confirm password");
                            confirmPassword_editTxt.setError("Do not match with new password");
                            Toast.makeText(getApplicationContext(),"New Password and confirm password do not match",Toast.LENGTH_LONG).show();
                        }

                    }else if(!storedPassword.equals(oldPassword)){
                        Toast.makeText(getApplicationContext(),"Enter correct old password",Toast.LENGTH_LONG).show();
                    }
                }else{
                    alert.showAlertDialog(ChangePasswordActivity.this,"Error", "Fields cannot be blank", false);
                    oldPassword_editTxt.setError("Cannot be blank");
                    newPassword_editTxt.setError("Cannot be blank");
                    confirmPassword_editTxt.setError("Cannot be blank");
                }

            }
        });

        show_password_ckb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (!isChecked) {
                    // show password
                    oldPassword_editTxt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    newPassword_editTxt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    confirmPassword_editTxt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    oldPassword_editTxt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    newPassword_editTxt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    confirmPassword_editTxt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
    }

    private final TextWatcher confirmPasswordWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            confirmPassword_editTxt.setError(null);
            oldPassword_editTxt.setError(null);
            newPassword_editTxt.setError(null);
            confirmPassword_editTxt.setError("beforeTextChanged");
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            confirmPassword_editTxt.setError(null);
            oldPassword_editTxt.setError(null);
            newPassword_editTxt.setError(null);

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (confirmPassword_editTxt.getText().toString().trim().equals(newPassword_editTxt.getText().toString().trim())){

            }else {

            }
        }
    };

    public SharedPreferences updateKeyValuePair(String key,String value,SharedPreferences sharedPreferences,boolean storeEncrypted){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        try{
            if(storeEncrypted){
                value= SecurityActivity.encrypt(seed, value);
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
        Intent i=new Intent(getApplicationContext(),SettingsActivity.class);
        startActivity(i);
        myUtility.finishTask(ChangePasswordActivity.this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent i=new Intent(getApplicationContext(),SettingsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                myUtility.finishTask(ChangePasswordActivity.this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
