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

import com.rakeshkr.passwordsafe.Utility.AlertDialogManager;
import com.rakeshkr.passwordsafe.Utility.MyUtility;
import com.rakeshkr.passwordsafe.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class UpdateIndividualBankDetails extends ActionBarActivity implements OnItemSelectedListener {
    String LOG_CAT="PasswordSafe";
    MyUtility myUtility=new MyUtility();
    AlertDialogManager alert=new AlertDialogManager();
    Spinner bank_name_spinner;
    EditText account_num,atm_card_num,atm_pin,display_name,atm_exp_date_editTxt,ifsc_editTxt;
    String accountNum=null,displayName=null,bankName=null,atmCardNum=null,atmPin=null,atm_exp_date,ifsc_code;
    ImageView password_eye;
    BankDataSource bankDataSource;
    String title;
    int dbId;
    Bank userBankData;
    Calendar myCalendar=Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_individual_bank_details);
        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            title=getIntent().getExtras().getString("title");
            setTitle("Update");
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

        myUtility.createToast(getApplicationContext(),String.valueOf(dbId));

        bank_name_spinner=(Spinner)findViewById(R.id.bank_name_spinner_title);
        //call back listner
        bank_name_spinner.setOnItemSelectedListener(this);
        account_num=(EditText)findViewById(R.id.account_num_editText);
        atm_card_num=(EditText)findViewById(R.id.atm_card_num_editText);
        atm_pin=(EditText)findViewById(R.id.atm_pin_editText);
        display_name=(EditText)findViewById(R.id.display_editText);
        password_eye=(ImageView)findViewById(R.id.password_eye);
        atm_exp_date_editTxt=(EditText)findViewById(R.id.atm_expiry_date_editTxt);
        ifsc_editTxt=(EditText)findViewById(R.id.ifsc_editTxt);
        password_eye.setOnTouchListener(mPasswordVisibleTouchListener);
        List<String> categories=new ArrayList<String>();
        categories=myUtility.bankSpinner(categories);
        ArrayAdapter<String> dataAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        bankDataSource=new BankDataSource(UpdateIndividualBankDetails.this);
        bankDataSource.open();
        bank_name_spinner.setAdapter(dataAdapter);
        if (bankDataSource.isDataPresent(String.valueOf(dbId),BankDBOpenHelper.COLUMN_ID)){
            myUtility.createToast(getApplicationContext(),"data present");
            userBankData = bankDataSource.getDataByRowId(dbId);
            accountNum=userBankData.getAcNum();
            atmCardNum=userBankData.getAtmCardNum();
            atmPin=userBankData.getAtmPin();
            displayName=userBankData.getDispName();
            bankName=userBankData.getBankName();
            atm_exp_date=userBankData.getExpiryDate();
            ifsc_code=userBankData.getIFSC();
            account_num.setText(accountNum);
            atm_card_num.setText(String.valueOf(atmCardNum));
            atm_pin.setText(String.valueOf(atmPin));
            display_name.setText(displayName);
            atm_exp_date_editTxt.setText(atm_exp_date);
            ifsc_editTxt.setText(ifsc_code);
            int bank_name_position=dataAdapter.getPosition(bankName);
            bank_name_spinner.setSelection(bank_name_position);
        }else {
            myUtility.createToast(getApplicationContext(),"data not present");
        }

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        atm_exp_date_editTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(UpdateIndividualBankDetails.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void updateLabel() {
        String myFormat = "MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        atm_exp_date_editTxt.setText(sdf.format(myCalendar.getTime()));
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

    private void updateExistingData(){
        myUtility.createToast(getApplicationContext(),title);
        accountNum=account_num.getText().toString().trim();
        atmCardNum=atm_card_num.getText().toString().trim();
        atmPin=atm_pin.getText().toString().trim();
        displayName=display_name.getText().toString().trim();
        atm_exp_date=atm_exp_date_editTxt.getText().toString().trim();
        bankName=bank_name_spinner.getSelectedItem().toString().trim();
        ifsc_code=ifsc_editTxt.getText().toString().trim();
        if(accountNum.trim().length()>0 && atmCardNum.trim().length()>0 && atmPin.trim().length()>0 && displayName.trim().length()>0 && atm_exp_date.trim().length()>0 && ifsc_code.length()>0){
            int retVal=update();
            if (retVal>0){
                myUtility.createToast(getApplicationContext(),"Updated successfully");
                Intent intent=new Intent(getApplicationContext(),ListOfBankDetails.class);
                startActivity(intent);
                finish();
            }else if (retVal<=0){
                alert.showAlertDialog(UpdateIndividualBankDetails.this,"Error","Data not updated",false);
            }
        }else {
            alert.showAlertDialog(UpdateIndividualBankDetails.this,"Empty Fields","Some fields are empty!!",false);
        }

    }


    private int update() {
        int retVal=0;
        Bank bank = new Bank();
        bank.setBankName(bankName);
        bank.setAcNum(accountNum);
        bank.setAtmCardNum(atmCardNum);
        bank.setAtmPin(atmPin);
        bank.setDispName(displayName);
        bank.setExpiryDate(atm_exp_date);
        bank.setIFSC(ifsc_code);
        try {
             retVal= bankDataSource.updatedb(bank,dbId);
            Log.i(LOG_CAT, "new row added created with id " + bank.getId());
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
                Intent i=new Intent(getApplicationContext(),ListOfBankDetails.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
                return true;
            case R.id.action_done:
                updateExistingData();
                break;
            case R.id.action_cancel:
                myUtility.createToast(getApplicationContext(),"Nothing Updated...");
                Intent intent=new Intent(getApplicationContext(),ListOfBankDetails.class);
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
        account_num.setText("");
        atm_card_num.setText("");
        atm_pin.setText("");
        display_name.setText("");
        atm_exp_date_editTxt.setText("");
        ifsc_editTxt.setText("");
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(),ListOfBankDetails.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
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
