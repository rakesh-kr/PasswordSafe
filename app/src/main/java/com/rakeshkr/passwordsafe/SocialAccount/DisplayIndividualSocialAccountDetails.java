package com.rakeshkr.passwordsafe.SocialAccount;

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
import com.rakeshkr.passwordsafe.Utility.SecurityActivity;

import java.util.ArrayList;
import java.util.List;


public class DisplayIndividualSocialAccountDetails extends ActionBarActivity implements OnItemClickListener{
    String LOG_CAT="PasswordSafe";
    SecurityActivity securityActivity=new SecurityActivity();
    MyUtility myUtility=new MyUtility();
    AlertDialogManager alert=new AlertDialogManager();
    String seed="hfhgfdtrdhhjhgfytfytdtrdjhyugtff";
    SocialAccountDataSource socialAccountDataSource;
    String title;
    String emailAddress=null,password=null,displayName=null,domainType=null;
    String myEmail=null;
    SocialAccount socialAccount=new SocialAccount();

    String[] displayList={"Display Name","Domain","Email","Password"};
    String[] displayListActualItems;
    ListView lView;
    ListViewAdapter lviewAdapter;

    int sdk= Build.VERSION.SDK_INT;
    int dbId=999999999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_individual_social_account_details);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            title=getIntent().getExtras().getString("title");
            setTitle(title);

        } catch (Exception e) {
            Log.i(LOG_CAT, "Exception while setting back button");
            //Toast.makeText(getApplicationContext(),"Got exception",Toast.LENGTH_LONG).show();
        }
        socialAccountDataSource = new SocialAccountDataSource(this);
        socialAccountDataSource.open();
        if (getIntent().getExtras().getString("dbId")==null){
            dbId=999999999;
        }else{
            dbId=Integer.parseInt(getIntent().getExtras().getString("dbId"));
        }

        socialAccount = socialAccountDataSource.getDataByRowId(dbId);
        emailAddress = socialAccount.getEmail();
        password = socialAccount.getPassword();
        displayName = socialAccount.getDispName();
        domainType = socialAccount.getDomainName();
        List<String> socialActItems = new ArrayList<String>();
        socialActItems.add(displayName);
        socialActItems.add(domainType);
        socialActItems.add(emailAddress);
        socialActItems.add(password);

        displayListActualItems = socialActItems.toArray(new String[socialActItems.size()]);
        lView = (ListView) findViewById(R.id.display_individual_social_detail);
        lviewAdapter = new ListViewAdapter(this, displayList, displayListActualItems);
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
                Intent i=new Intent(getApplicationContext(),ListOfSocialAccountDetails.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
                return true;
            case R.id.action_delete:
                AskOption().show();
                break;
            case R.id.action_edit:
                Intent editIntent=new Intent(getApplicationContext(),UpdateIndividualSocialAccountDetails.class);
                editIntent.putExtra("dbId",String.valueOf(dbId));
                editIntent.putExtra("title",title);
                startActivity(editIntent);
                finish();
            case R.id.action_help:
                return true;
            case R.id.menu_item_share:

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = prepareDataForSharing();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, displayName+"'s Social Account Details");
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
                +"Domain : "+domainType+"\n"
                +"Email Address: "+emailAddress+"\n"
                +"Password: "+password+"\n"
                +"--------------------------\n"
                +"Shared from: "+getApplicationInfo().loadLabel(getPackageManager()).toString();
    }

    private void deleteSocialAccountDetail() {

        if (socialAccountDataSource.deleteRow(dbId)!=0){
            myUtility.createToast(getApplicationContext(),"Deleted successfully");
        }else {
            myUtility.createToast(getApplicationContext(),"Email not present");
        }
        Intent intent=new Intent(getApplicationContext(),ListOfSocialAccountDetails.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(),ListOfSocialAccountDetails.class);
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
