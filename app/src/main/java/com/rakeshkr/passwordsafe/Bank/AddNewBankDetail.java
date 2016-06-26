package com.rakeshkr.passwordsafe.Bank;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.rakeshkr.passwordsafe.ApplicationSpecific.SelectionActivity;
import com.rakeshkr.passwordsafe.R;
import com.rakeshkr.passwordsafe.Utility.AlertDialogManager;
import com.rakeshkr.passwordsafe.Utility.MyUtility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class AddNewBankDetail extends ActionBarActivity implements OnItemSelectedListener{
    String LOG_CAT="PasswordSafe";
    MyUtility myUtility=new MyUtility();
    AlertDialogManager alert=new AlertDialogManager();
    Spinner bank_name_spinner;
    EditText account_num,atm_card_num,atm_pin,display_name,atm_card_date_editTxt,ifsc_editTxt;
    String bankName=null;
    String accountNum=null,atmCardNum=null,atmPin=null,displayName=null,atm_card_exp_date=null,ifsc_code=null;
    BankDataSource bankDataSource;
    ImageView password_eye;
    Calendar myCalendar=Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bank_account_detail);
        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (Exception e){
            Log.i(LOG_CAT, "Exception while setting back button");
        }

        bank_name_spinner=(Spinner)findViewById(R.id.bank_name_spinner_title);
        //call back listner
        if (bank_name_spinner != null) {
            bank_name_spinner.setOnItemSelectedListener(this);
        }
        account_num=(EditText)findViewById(R.id.account_num_editText);
        atm_card_num=(EditText)findViewById(R.id.atm_card_num_editText);
        atm_pin=(EditText)findViewById(R.id.atm_pin_editText);
        display_name=(EditText)findViewById(R.id.display_editText);
        password_eye=(ImageView)findViewById(R.id.password_eye);
        atm_card_date_editTxt=(EditText)findViewById(R.id.atm_card_date_editTxt);
        ifsc_editTxt=(EditText)findViewById(R.id.ifsc_editTxt);
        password_eye.setOnTouchListener(mPasswordVisibleTouchListener);
        // Spinner Drop down elements
        List<String> categories=new ArrayList<String>();
        categories=myUtility.bankSpinner(categories);
        ArrayAdapter<String> dataAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        bankDataSource=new BankDataSource(AddNewBankDetail.this);

        bank_name_spinner.setAdapter(dataAdapter);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };


        atm_card_date_editTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddNewBankDetail.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        atm_card_date_editTxt.setText(sdf.format(myCalendar.getTime()));
    }


    private View.OnTouchListener mPasswordVisibleTouchListener=new View.OnTouchListener(){
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final boolean isOutsideView = event.getX() < 0 ||
                    event.getX() > v.getWidth() ||
                    event.getY() < 0 ||
                    event.getY() > v.getHeight();

            // change input type will reset cursor position, so we want to save it
            final int cursor = atm_pin.getSelectionStart();

            if (isOutsideView || MotionEvent.ACTION_UP == event.getAction())
                atm_pin.setInputType( InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD);
            else
                atm_pin.setInputType( InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

            atm_pin.setSelection(cursor);
            return true;
        }
    };

    private void addNewData() {
        accountNum=account_num.getText().toString().trim();
        atmCardNum=atm_card_num.getText().toString();
        atmPin=atm_pin.getText().toString().trim();
        displayName=display_name.getText().toString();
        atm_card_exp_date=atm_card_date_editTxt.getText().toString();
        bankName=bank_name_spinner.getSelectedItem().toString();
        ifsc_code=ifsc_editTxt.getText().toString().trim();

        if(accountNum.trim().length()>0 && atmCardNum.trim().length()>0 && atmPin.trim().length()>0 && displayName.trim().length()>0 && atm_card_exp_date.trim().length()>0 && ifsc_code.length()>0){
            bankDataSource.open();
            if((bankDataSource.isDataPresent(accountNum,BankDBOpenHelper.COLUMN_AC))) {
                alert.showAlertDialog(AddNewBankDetail.this,"Data Present","Sorry "+accountNum+" A/C number already present",false);
            }else if(bankDataSource.isDataPresent(atmCardNum,BankDBOpenHelper.COLUMN_ATM_CARD_NUM)) {
                alert.showAlertDialog(AddNewBankDetail.this,"Data Present","Sorry "+atmCardNum+" ATM card number already present",false);
            }else if(bankDataSource.isDataPresent(displayName,BankDBOpenHelper.COLUMN_DISPLAY_NAME) || displayName.contains("-")) {
                alert.showAlertDialog(AddNewBankDetail.this,"Data Present","Sorry Unique display name "+displayName+" already present or '-' special char not allowed",false);
            }else if(bankDataSource.isDataPresent(atm_card_exp_date,BankDBOpenHelper.COLUMN_EXPIRY_DATE)){

            }else {
                add();
                myUtility.createToast(getApplicationContext(),"Successfully added");
                Intent intent=new Intent(getApplicationContext(),ListOfBankDetails.class);
                startActivity(intent);
                finish();
            }
        } else {
            alert.showAlertDialog(AddNewBankDetail.this, "Empty Fields", "All fields are mandatory", false);
        }
       bankDataSource.close();
    }

    private void add() {
        Bank bank = new Bank();
        bank.setBankName(bankName);
        bank.setAcNum(accountNum);
        bank.setAtmCardNum(atmCardNum);
        bank.setAtmPin(atmPin);
        bank.setDispName(displayName);
        bank.setExpiryDate(atm_card_exp_date);
        bank.setIFSC(ifsc_code);
        try {
            bank = bankDataSource.addNewRow(bank);
            Log.i(LOG_CAT, "new row added created with id " + bank.getId());
        }catch (SQLiteConstraintException e){
            myUtility.createToast(getApplicationContext(),"Data Already present");
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        account_num.setText("");
        atm_card_num.setText("");
        atm_pin.setText("");
        display_name.setText("");
        ifsc_editTxt.setText("");
    }


    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(),SelectionActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        bankName = parent.getItemAtPosition(position).toString();

    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
    @Override
    protected void onResume() {
        super.onResume();
        bankDataSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bankDataSource.close();
    }
}
