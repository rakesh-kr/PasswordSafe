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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.rakeshkr.passwordsafe.Utility.AlertDialogManager;
import com.rakeshkr.passwordsafe.Utility.MyUtility;
import com.rakeshkr.passwordsafe.R;
import com.rakeshkr.passwordsafe.Utility.SecurityActivity;

import java.util.ArrayList;
import java.util.List;


public class UpdateIndividualSocialAccountDetails extends ActionBarActivity implements OnItemSelectedListener {
    String LOG_CAT="PasswordSafe";
    SecurityActivity securityActivity=new SecurityActivity();
    MyUtility myUtility=new MyUtility();
    AlertDialogManager alert=new AlertDialogManager();
    String seed="hfhgfdtrdhhjhgfytfytdtrdjhyugtff";
    Spinner social_account_spinner;
    EditText emailEditTxt,passwordEditTxt,displayNameEditTxt,confirmPasswordEditTxt;
    String emailAddress=null,password=null,displayName=null,confirmPassword=null,domain=null;
    SocialAccountDataSource socialAccountDataSource;
    String title,myEmail;
    SocialAccount socialAccount;
    ImageView password_eye,confirm_password_eye;
    int dbId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_individual_social_account_details);
        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            title=getIntent().getExtras().getString("title");
            setTitle("Update "+title);
        }catch (NullPointerException e){
            Log.e(LOG_CAT, "Exception while setting title");
        }catch (Exception e){
            Log.e(LOG_CAT, "Exception while setting back button");
        }

        if (getIntent().getExtras().getString("dbId")==null){
            dbId=999999999;
        }else{
            dbId=Integer.parseInt(getIntent().getExtras().getString("dbId"));
        }
        social_account_spinner=(Spinner)findViewById(R.id.type_name_spinner_title);
        //call back listner
        social_account_spinner.setOnItemSelectedListener(this);
        emailEditTxt=(EditText)findViewById(R.id.email_address_editText);
        passwordEditTxt=(EditText)findViewById(R.id.password_editText);
        displayNameEditTxt=(EditText)findViewById(R.id.display_editText);
        confirmPasswordEditTxt=(EditText)findViewById(R.id.confirm_password_editText);

        confirm_password_eye=(ImageView)findViewById(R.id.confirm_password_eye);
        password_eye=(ImageView)findViewById(R.id.password_eye);
        password_eye.setOnTouchListener(mPasswordVisibleTouchListener);
        confirm_password_eye.setOnTouchListener(mConfirmPasswordVisibleTouchListener);

        List<String> categories=new ArrayList<String>();
        categories=myUtility.socialAccountSpinner(categories);
        ArrayAdapter<String> dataAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        socialAccountDataSource=new SocialAccountDataSource(UpdateIndividualSocialAccountDetails.this);
        socialAccountDataSource.open();
        social_account_spinner.setAdapter(dataAdapter);

        if (socialAccountDataSource.isDataPresent(String.valueOf(dbId),SocialAccountDBOpenHelper.COLUMN_ID)){
            myUtility.createToast(getApplicationContext(),"data present");
            socialAccount = socialAccountDataSource.getDataByRowId(dbId);
            emailAddress=socialAccount.getEmail();
            password=socialAccount.getPassword();
            displayName=socialAccount.getDispName();
            domain=socialAccount.getDomainName();
            //need get account type

            emailEditTxt.setText(emailAddress);
            passwordEditTxt.setText(password);
            displayNameEditTxt.setText(displayName);
            int position=dataAdapter.getPosition(domain);
            social_account_spinner.setSelection(position);

        }else {
            myUtility.createToast(getApplicationContext(),"data not present");
        }


    }

    private View.OnTouchListener mPasswordVisibleTouchListener=new View.OnTouchListener(){
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final boolean isOutsideView = event.getX() < 0 ||
                    event.getX() > v.getWidth() ||
                    event.getY() < 0 ||
                    event.getY() > v.getHeight();

            // change input type will reset cursor position, so we want to save it
            final int cursor = passwordEditTxt.getSelectionStart();

            if (isOutsideView || MotionEvent.ACTION_UP == event.getAction())
                passwordEditTxt.setInputType( InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD);
            else
                passwordEditTxt.setInputType( InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

            passwordEditTxt.setSelection(cursor);
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
            final int cursor = confirmPasswordEditTxt.getSelectionStart();

            if (isOutsideView || MotionEvent.ACTION_UP == event.getAction())
                confirmPasswordEditTxt.setInputType( InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD);
            else
                confirmPasswordEditTxt.setInputType( InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

            confirmPasswordEditTxt.setSelection(cursor);
            return true;
        }
    };

        private int update() {
            int retVal=0;
            SocialAccount socialAccountt=new SocialAccount();
            socialAccountt.setEmail(emailAddress);
            socialAccountt.setPassword(password);
            socialAccountt.setDispName(displayName);
            socialAccountt.setDomainName(domain);
            try {
                retVal= socialAccountDataSource.updatedb(socialAccountt,dbId);
                if(retVal>0){
                    Log.i(LOG_CAT, "row updated with email id " + socialAccountt.getEmail());
                }

            }catch (SQLiteConstraintException e){
                myUtility.createToast(getApplicationContext(),"Data Already present");
            }
            return retVal;
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
                Intent i=new Intent(getApplicationContext(),ListOfSocialAccountDetails.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
                break;
            case R.id.action_done:
                updateExistingData();
                break;
            case R.id.action_cancel:
                myUtility.createToast(getApplicationContext(),"Nothing Updated...");
                Intent intent=new Intent(getApplicationContext(),ListOfSocialAccountDetails.class);
                startActivity(intent);
                finish();
                break;
            case R.id.action_clear:
                clearEditText();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearEditText() {
        emailEditTxt.setText("");
        passwordEditTxt.setText("");
        confirmPasswordEditTxt.setText("");
        displayNameEditTxt.setText("");
    }

    private void updateExistingData() {
        myUtility.createToast(getApplicationContext(), title);
        emailAddress = emailEditTxt.getText().toString();
        password = passwordEditTxt.getText().toString();
        confirmPassword = confirmPasswordEditTxt.getText().toString();
        displayName = displayNameEditTxt.getText().toString();
        domain=social_account_spinner.getSelectedItem().toString();
        if (emailAddress.trim().length() > 0 && password.trim().length() > 0 && confirmPassword.trim().length() > 0 && displayName.trim().length() > 0) {
            if (password.equals(confirmPassword)) {
                int retVal = update();
                if (retVal > 0) {
                    myUtility.createToast(getApplicationContext(), "Updated successfully");
                    Intent intent = new Intent(getApplicationContext(), ListOfSocialAccountDetails.class);
                    startActivity(intent);
                    finish();
                } else if (retVal <= 0) {
                    alert.showAlertDialog(UpdateIndividualSocialAccountDetails.this, "Error", "Data not updated", false);
                }
            }else {
                alert.showAlertDialog(UpdateIndividualSocialAccountDetails.this, "Error", "Password and confirm password mismatch", false);
            }
        } else {
            alert.showAlertDialog(UpdateIndividualSocialAccountDetails.this, "Empty Fields", "Some fields are empty!!", false);
        }
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(),ListOfSocialAccountDetails.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        myEmail = parent.getItemAtPosition(position).toString();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
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
