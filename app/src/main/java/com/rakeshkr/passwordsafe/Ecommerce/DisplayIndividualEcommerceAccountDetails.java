package com.rakeshkr.passwordsafe.Ecommerce;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.rakeshkr.passwordsafe.R;
import com.rakeshkr.passwordsafe.Utility.ListViewAdapter;
import com.rakeshkr.passwordsafe.Utility.MyUtility;

import java.util.ArrayList;
import java.util.List;


public class DisplayIndividualEcommerceAccountDetails extends ActionBarActivity implements OnItemClickListener{
    String LOG_CAT="PasswordSafe";
    MyUtility myUtility=new MyUtility();

    EcommerceDataSource ecommerceDataSource;
    String title;
    String emailAddress=null,password=null,displayName=null,seller=null,website=null;

    EcommerceAccount ecommerceAccount=new EcommerceAccount();

    String[] displayList={"Display Name","Email","Password","Seller","Website"};
    String[] displayListActualItems;
    ListView lView;
    ListViewAdapter lviewAdapter;

    int sdk= Build.VERSION.SDK_INT;
    int dbId=999999999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_individual_ecommerce_account_details);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            title=getIntent().getExtras().getString("title");
            setTitle(title);

        } catch (Exception e) {
            Log.i(LOG_CAT, "Exception while setting back button");
        }
        ecommerceDataSource = new EcommerceDataSource(this);
        ecommerceDataSource.open();
        try {
            if (getIntent().getExtras().getString("dbId") == null) {
                dbId = 999999999;
            } else {
                dbId = Integer.parseInt(getIntent().getExtras().getString("dbId"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        ecommerceAccount = ecommerceDataSource.getDataByRowId(dbId);
        emailAddress = ecommerceAccount.getEmail();
        password = ecommerceAccount.getPassword();
        displayName = ecommerceAccount.getDispName();
        seller = ecommerceAccount.getSellerName();
        website=ecommerceAccount.getWebsiteName();
        List<String> ecommerceActItems = new ArrayList<String>();
        ecommerceActItems.add(displayName);
        ecommerceActItems.add(emailAddress);
        ecommerceActItems.add(password);
        ecommerceActItems.add(seller);
        ecommerceActItems.add(website);

        displayListActualItems = ecommerceActItems.toArray(new String[ecommerceActItems.size()]);
        lView = (ListView) findViewById(R.id.display_individual_ecommerce_details);
        lviewAdapter = new ListViewAdapter(this, displayList, displayListActualItems);
        lView.setAdapter(lviewAdapter);
        lView.setOnItemClickListener(this);


    }
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
        if(sdk < Build.VERSION_CODES.HONEYCOMB) {

            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(displayListActualItems[position]);
            myUtility.createToast(getApplicationContext(),displayListActualItems[position]+" Copied to clipboard");

        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Clip",displayListActualItems[position]);
            myUtility.createToast(getApplicationContext(),displayListActualItems[position]+" copied to clipboard");
            clipboard.setPrimaryClip(clip);
        }
            if (displayList[position].equals("Website")){
                String url="http://"+website;
                Intent i=new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(Intent.createChooser(i, "Open with"));
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
                Intent i=new Intent(getApplicationContext(),ListOfEcommerceAccountDetails.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
                return true;
            case R.id.action_delete:
                AskOption().show();
                break;
            case R.id.action_edit:
                Intent editIntent=new Intent(getApplicationContext(),UpdateIndividualEcommerceAccountDetails.class);
                editIntent.putExtra("dbId",String.valueOf(dbId));
                editIntent.putExtra("title",title);
                startActivity(editIntent);
                finish();
            case R.id.action_help:
                return true;
            case R.id.menu_item_share:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = prepareDataForSharing();
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, displayName+"'s "+seller+"account Details");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String prepareDataForSharing() {
        return "\n-------------------------\n"
                +displayName+"'s "+seller+" account Details\n"
                +"Seller : "+seller+"\n"
                +"Email Address: "+emailAddress+"\n"
                +"Password: "+password+"\n"
                +"Website: "+website+"\n"
                +"--------------------------\n"
                +"Shared from: "+getApplicationInfo().loadLabel(getPackageManager()).toString();
    }

    private void deleteSocialAccountDetail() {
        if (ecommerceDataSource.deleteRow(dbId)!=0){
            myUtility.createToast(getApplicationContext(),"Deleted successfully");
        }else {
            myUtility.createToast(getApplicationContext(),"Email not present");
        }
        Intent intent=new Intent(getApplicationContext(),ListOfEcommerceAccountDetails.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(),ListOfEcommerceAccountDetails.class);
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

    private AlertDialog AskOption(){
        return new AlertDialog.Builder(this)
                .setTitle("Delete "+displayName)
                .setMessage("Are you sure you want to delete?")

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteSocialAccountDetail();
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
