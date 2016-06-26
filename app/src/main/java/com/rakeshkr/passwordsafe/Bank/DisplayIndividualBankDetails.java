package com.rakeshkr.passwordsafe.Bank;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.rakeshkr.passwordsafe.Utility.AlertDialogManager;
import com.rakeshkr.passwordsafe.Utility.ListViewAdapter;
import com.rakeshkr.passwordsafe.Utility.MyUtility;
import com.rakeshkr.passwordsafe.R;

import java.util.ArrayList;
import java.util.List;


public class DisplayIndividualBankDetails extends ActionBarActivity implements OnItemClickListener{
    String LOG_CAT="PasswordSafe";
    MyUtility myUtility=new MyUtility();
    AlertDialogManager alert=new AlertDialogManager();
    //String seed="hfhgfdtrdhhjhgfytfytdtrdjhyugtff";
    BankDataSource bankDataSource;
    String title;
    String bankName=null,accountNum=null,atmCard=null,atmPin=null,displayName=null,atm_exp_date=null,ifsc_code=null;
    int dbId;
    Bank bank=new Bank();
    String[] displayList={"Display Name","Bank name","Account Number","ATM Card Number","ATM expiry date","ATM Pin","IFSC"};
    String[] displayListActualItems;
    ListView lView;
    ListViewAdapter lviewAdapter;
    int sdk= Build.VERSION.SDK_INT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_individual_bank_details);
        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            title=getIntent().getExtras().getString("title");
            setTitle(title);
        }catch (Exception e){
            Log.i(LOG_CAT, "Exception while setting back button");
        }
        bankDataSource=new BankDataSource(this);
        bankDataSource.open();
        if (getIntent().getExtras().getString("dbId")==null){
            dbId=999999999;
        }else{
            dbId=Integer.parseInt(getIntent().getExtras().getString("dbId"));
        }

        bank=bankDataSource.getDataByRowId(dbId);
        bankName=bank.getBankName();
        accountNum=bank.getAcNum();
        atmCard=bank.getAtmCardNum();
        atmPin=bank.getAtmPin();
        displayName=bank.getDispName();
        atm_exp_date=bank.getExpiryDate();
        ifsc_code=bank.getIFSC();
        List<String> bankItems=new ArrayList<String>();
        bankItems.add(displayName);
        bankItems.add(bankName);
        bankItems.add(accountNum);
        bankItems.add(atmCard);
        bankItems.add(atm_exp_date);
        bankItems.add(atmPin);
        bankItems.add(ifsc_code);


        displayListActualItems=bankItems.toArray(new String[bankItems.size()]);

        lView=(ListView)findViewById(R.id.display_individual_bank_detail);
        lviewAdapter=new ListViewAdapter(this,displayList,displayListActualItems);
        lView.setAdapter(lviewAdapter);
        lView.setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
        if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {

            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(displayListActualItems[position]);
            myUtility.createToast(getApplicationContext(),displayListActualItems[position]+" Copied to clipboard");

        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Clip",displayListActualItems[position]);
            myUtility.createToast(getApplicationContext(),displayListActualItems[position]+" copied to clipboard");
            clipboard.setPrimaryClip(clip);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display_menu_bank_social, menu);
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
            case R.id.action_delete:
                AskOption().show();
                break;
            case R.id.action_edit:
                Intent editIntent=new Intent(getApplicationContext(),UpdateIndividualBankDetails.class);
                editIntent.putExtra("dbId",String.valueOf(dbId));
                editIntent.putExtra("title",title);
                startActivity(editIntent);
                finish();
            case R.id.action_help:
                break;
            case R.id.menu_item_share:

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = prepareDataForSharing();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, displayName+"'s Bank Details");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String prepareDataForSharing() {
        return "\n-------------------------\n"
                +"Bank Name: "+bankName+"\n"
                +"Account No: "+accountNum+"\n"
                +"ATM CARD No: "+atmCard+"\n"
                +"ATM Pin: "+atmPin+"\n"
                +"ATM Card Exp Date: "+atm_exp_date+"\n"
                +"Bank IFSC Code: "+ifsc_code+"\n"
                +"--------------------------\n"
                +"Shared from: "+getApplicationInfo().loadLabel(getPackageManager()).toString();
    }

    private void deleteBankDetail() {

        if (bankDataSource.deleteRow(dbId)!=0){
            myUtility.createToast(getApplicationContext(),"Deleted successfully");
        }else {
            myUtility.createToast(getApplicationContext(),"Account not present");
        }
        Intent intent=new Intent(getApplicationContext(),ListOfBankDetails.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(),ListOfBankDetails.class);
        startActivity(i);
        finish();
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

    private AlertDialog AskOption(){
        return new AlertDialog.Builder(this)
                .setTitle("Delete "+displayName)
                .setMessage("Are you sure you want to delete?")

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteBankDetail();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        myUtility.createToast(getApplicationContext(), "Did not deleted "+displayName);
                        dialog.dismiss();
                    }
                })
                .create();

    }
}
