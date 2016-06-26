package com.rakeshkr.passwordsafe.Ecommerce;

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

import com.rakeshkr.passwordsafe.ApplicationSpecific.SelectionActivity;
import com.rakeshkr.passwordsafe.R;
import com.rakeshkr.passwordsafe.Utility.AlertDialogManager;
import com.rakeshkr.passwordsafe.Utility.MyUtility;

import java.util.ArrayList;
import java.util.List;


public class AddNewEcommerceDetail extends ActionBarActivity implements OnItemSelectedListener {
    String LOG_CAT="PasswordSafe";
    //SecurityActivity securityActivity=new SecurityActivity();
    MyUtility myUtility=new MyUtility();
    AlertDialogManager alert=new AlertDialogManager();
    //String seed="hfhgfdtrdhhjhgfytfytdtrdjhyugtff";
    Spinner seller_spinner;
    EditText emailAddress_editTxt,password_editTxt,confirmPassword_editTxt,displayName_editTxt,webite_editTxt;
    String seller_type=null;
    String emailAddress=null,password=null,confirmPassword=null,dispName=null,seller=null,website=null;
    ImageView password_eye,confirm_password_eye;
    EcommerceDataSource ecommerceDataSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_ecom_details);
        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (Exception e){
            Log.i(LOG_CAT, "Exception while setting back button");
        }

        emailAddress_editTxt=(EditText)findViewById(R.id.email_address_editText);
        password_editTxt=(EditText)findViewById(R.id.password_editText);
        confirmPassword_editTxt=(EditText)findViewById(R.id.confirm_password_editText);
        displayName_editTxt=(EditText)findViewById(R.id.display_editText);
        seller_spinner=(Spinner)findViewById(R.id.seller_name_spinner_title);
        confirm_password_eye=(ImageView)findViewById(R.id.confirm_password_eye);
        webite_editTxt=(EditText)findViewById(R.id.website_editTxt);
        password_eye=(ImageView)findViewById(R.id.password_eye);
        password_eye.setOnTouchListener(mPasswordVisibleTouchListener);
        confirm_password_eye.setOnTouchListener(mConfirmPasswordVisibleTouchListener);
        //call back listner
        seller_spinner.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories=myUtility.sellerSpinnerItems(categories);
        ArrayAdapter<String> dataAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seller_spinner.setAdapter(dataAdapter);

        ecommerceDataSource=new EcommerceDataSource(AddNewEcommerceDetail.this);

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
        EcommerceAccount ecommerceAccount=new EcommerceAccount();
        ecommerceAccount.setEmail(emailAddress);
        ecommerceAccount.setPassword(password);
        ecommerceAccount.setDispName(dispName);
        ecommerceAccount.setWebsiteName(website);
        ecommerceAccount.setSellerName(seller);
        try {
            ecommerceAccount = ecommerceDataSource.addNewRow(ecommerceAccount);
            Log.i(LOG_CAT, "new row added created with email id " + ecommerceAccount.getEmail());
        }catch (SQLiteConstraintException e){
            myUtility.createToast(getApplicationContext(),"Data Already present");
        }


    }

    private void addNewData(){
        emailAddress=emailAddress_editTxt.getText().toString().trim();
        password=password_editTxt.getText().toString().trim();
        confirmPassword=confirmPassword_editTxt.getText().toString().trim();
        dispName=displayName_editTxt.getText().toString().trim();
        seller=seller_spinner.getSelectedItem().toString();
        website=webite_editTxt.getText().toString().trim();

        if (emailAddress.length()>0&&password.length()>0 && confirmPassword.length()>0 && dispName.length()>0&&seller!=null&&website.length()>0){
            ecommerceDataSource.open();
            if (password.equals(confirmPassword)){
                if (ecommerceDataSource.isDataPresent(dispName,EcommerceDBOpenHelper.COLUMN_DISPLAY_NAME)){
                    alert.showAlertDialog(AddNewEcommerceDetail.this,"Data Present",dispName+" already present",false);
                }else{
                    add();
                    myUtility.createToast(getApplicationContext(),"Successfully added");
                    Intent intent=new Intent(getApplicationContext(),ListOfEcommerceAccountDetails.class);
                    startActivity(intent);
                    finish();
                }

            }else if (!password.equals(confirmPassword)){
                alert.showAlertDialog(AddNewEcommerceDetail.this,"Password Mismatch","Password and confirm password mismatch",false);
            }else {
                alert.showAlertDialog(AddNewEcommerceDetail.this,"OOPS!","Some Unknown error",false);
            }

        }else {
            alert.showAlertDialog(AddNewEcommerceDetail.this,"Empty Fields","All fields are mandatory",false);
        }

        ecommerceDataSource.close();
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
        webite_editTxt.setText("");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        seller_type = parent.getItemAtPosition(position).toString();
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
        ecommerceDataSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ecommerceDataSource.close();
    }
}
