package com.rakeshkr.passwordsafe.SocialAccount;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import com.rakeshkr.passwordsafe.Utility.AlertDialogManager;
import com.rakeshkr.passwordsafe.ApplicationSpecific.SelectionActivity;
import com.rakeshkr.passwordsafe.Utility.MyUtility;
import com.rakeshkr.passwordsafe.R;
import com.rakeshkr.passwordsafe.Utility.SecurityActivity;

import java.util.ArrayList;
import java.util.List;


public class AddNewSocialAccountDetail extends ActionBarActivity implements OnItemSelectedListener {
    String LOG_CAT="PasswordSafe";
    SecurityActivity securityActivity=new SecurityActivity();
    MyUtility myUtility=new MyUtility();
    AlertDialogManager alert=new AlertDialogManager();
    String seed="hfhgfdtrdhhjhgfytfytdtrdjhyugtff";
    Spinner mail_domain_spinner;
    EditText emailAddress_editTxt,password_editTxt,confirmPassword_editTxt,displayName_editTxt;
    String mail_type=null;
    String emailAddress=null,password=null,confirmPassword=null,dispName=null,domain=null;
    ImageView password_eye,confirm_password_eye;
    SocialAccountDataSource socialAccountDataSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_social_account_detail);
        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (Exception e){
            Log.i(LOG_CAT, "Exception while setting back button");
        }

        emailAddress_editTxt=(EditText)findViewById(R.id.email_address_editText);
        password_editTxt=(EditText)findViewById(R.id.password_editText);
        confirmPassword_editTxt=(EditText)findViewById(R.id.confirm_password_editText);
        displayName_editTxt=(EditText)findViewById(R.id.display_editText);
        mail_domain_spinner=(Spinner)findViewById(R.id.type_name_spinner_title);
        confirm_password_eye=(ImageView)findViewById(R.id.confirm_password_eye);
        password_eye=(ImageView)findViewById(R.id.password_eye);
        password_eye.setOnTouchListener(mPasswordVisibleTouchListener);
        confirm_password_eye.setOnTouchListener(mConfirmPasswordVisibleTouchListener);
        //call back listner
        mail_domain_spinner.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories=myUtility.socialAccountSpinner(categories);
        ArrayAdapter<String> dataAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mail_domain_spinner.setAdapter(dataAdapter);

        socialAccountDataSource=new SocialAccountDataSource(AddNewSocialAccountDetail.this);

    }


    private View.OnTouchListener mPasswordVisibleTouchListener=new View.OnTouchListener(){
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final boolean isOutsideView = event.getX() < 0 ||
                    event.getX() > v.getWidth() ||
                    event.getY() < 0 ||
                    event.getY() > v.getHeight();

            // change input type will reset cursor position, so we want to save it
            final int cursor = password_editTxt.getSelectionStart();

            if (isOutsideView || MotionEvent.ACTION_UP == event.getAction())
                password_editTxt.setInputType( InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD);
            else
                password_editTxt.setInputType( InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

            password_editTxt.setSelection(cursor);
            return true;
        }
    };

    private View.OnTouchListener mConfirmPasswordVisibleTouchListener=new View.OnTouchListener(){
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final boolean isOutsideView = event.getX() < 0 ||
                    event.getX() > v.getWidth() ||
                    event.getY() < 0 ||
                    event.getY() > v.getHeight();

            // change input type will reset cursor position, so we want to save it
            final int cursor = confirmPassword_editTxt.getSelectionStart();

            if (isOutsideView || MotionEvent.ACTION_UP == event.getAction())
                confirmPassword_editTxt.setInputType( InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD);
            else
                confirmPassword_editTxt.setInputType( InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

            confirmPassword_editTxt.setSelection(cursor);
            return true;
        }
    };

    private void add() {
        SocialAccount socialAccount=new SocialAccount();
        socialAccount.setEmail(emailAddress);
        socialAccount.setPassword(password);
        socialAccount.setDispName(dispName);
        socialAccount.setDomainName(domain);
        try {
            socialAccount = socialAccountDataSource.addNewRow(socialAccount);
            Log.i(LOG_CAT, "new row added created with email id " + socialAccount.getEmail());
        }catch (SQLiteConstraintException e){
            myUtility.createToast(getApplicationContext(),"Data Already present");
        }


    }

    private void addNewData(){
        emailAddress=emailAddress_editTxt.getText().toString().trim();
        password=password_editTxt.getText().toString().trim();
        confirmPassword=confirmPassword_editTxt.getText().toString().trim();
        dispName=displayName_editTxt.getText().toString().trim();
        domain=mail_domain_spinner.getSelectedItem().toString();

        if (emailAddress.length()>0&&password.length()>0 && confirmPassword.length()>0 && dispName.length()>0&&domain!=null){
            socialAccountDataSource.open();
            if (password.equals(confirmPassword)){
                if (socialAccountDataSource.isDataPresent(emailAddress,SocialAccountDBOpenHelper.COLUMN_EMAIL)){
                    alert.showAlertDialog(AddNewSocialAccountDetail.this,"Data Present",emailAddress+" already present",false);
                }else if (socialAccountDataSource.isDataPresent(dispName,SocialAccountDBOpenHelper.COLUMN_DISPLAY_NAME)){
                    alert.showAlertDialog(AddNewSocialAccountDetail.this,"Data Present",dispName+" already present",false);
                }else{
                    add();
                    myUtility.createToast(getApplicationContext(),"Successfully added");
                    Intent intent=new Intent(getApplicationContext(),ListOfSocialAccountDetails.class);
                    startActivity(intent);
                    finish();
                }

            }else if (!password.equals(confirmPassword)){
                alert.showAlertDialog(AddNewSocialAccountDetail.this,"Password Mismatch","Password and confirm password mismatch",false);
            }else {
                alert.showAlertDialog(AddNewSocialAccountDetail.this,"OOPS!","Some Unknown error",false);
            }

        }else {
            alert.showAlertDialog(AddNewSocialAccountDetail.this,"Empty Fields","All fields are mandatory",false);
        }

        socialAccountDataSource.close();
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
                Intent i=new Intent(getApplicationContext(),SelectionActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
                return true;
            case R.id.action_done:
                addNewData();
                break;
            case R.id.action_cancel:
                myUtility.createToast(getApplicationContext(),"Nothing added...");
                Intent intent=new Intent(getApplicationContext(),SelectionActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.action_clear:
                clearEditTexts();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearEditTexts() {
        emailAddress_editTxt.setText("");
        password_editTxt.setText("");
        confirmPassword_editTxt.setText("");
        displayName_editTxt.setText("");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        mail_type = parent.getItemAtPosition(position).toString();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(),SelectionActivity.class);
        startActivity(i);
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
        socialAccountDataSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        socialAccountDataSource.close();
    }
}
