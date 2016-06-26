package com.rakeshkr.passwordsafe.ApplicationSpecific;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.rakeshkr.passwordsafe.Bank.BankDataSource;
import com.rakeshkr.passwordsafe.Ecommerce.EcommerceDataSource;
import com.rakeshkr.passwordsafe.R;
import com.rakeshkr.passwordsafe.SocialAccount.SocialAccountDataSource;
import com.rakeshkr.passwordsafe.Utility.MySharedPreferences;
import com.rakeshkr.passwordsafe.Utility.MyUtility;

public class SettingsActivity extends ActionBarActivity {
    String[] settingItems={"Dashboard","Change Password","Change Email","Clear Bank Details","Clear Social Account Details","Clear E-Commerce details","Notification Settings"};
    String LOG_CAT="PasswordSafe";
    MyUtility myUtility=new MyUtility();
    BankDataSource bankDataSource;
    String password=null;
    SocialAccountDataSource socialAccountDataSource;
    EcommerceDataSource ecommerceDataSource;
    MySharedPreferences mySharedPreferences=new MySharedPreferences();

    private String seed;
    //SecurityActivity securityActivity=new SecurityActivity();
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);


        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (Exception e){
            Log.i(LOG_CAT, "Exception while setting back button");
        }
        seed=MySharedPreferences.seed;
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.setting_list_view,settingItems);

        ListView mySettingList=(ListView) findViewById(R.id.settingList);
        mySettingList.setAdapter(adapter);

        mySettingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent settingIntent;
                switch (position) {
                    case 0:
                        //application profile
                        settingIntent=new Intent(getApplicationContext(),ApplicationSummaryActivity.class);
                        startActivity(settingIntent);
                        myUtility.finishTask(SettingsActivity.this);
                        break;
                    case 1:
                        //change password
                        settingIntent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                        startActivity(settingIntent);
                        myUtility.finishTask(SettingsActivity.this);
                        break;
                    case 2:
                        settingIntent=new Intent(getApplicationContext(),ChangeEmailActivity.class);
                        startActivity(settingIntent);
                        myUtility.finishTask(SettingsActivity.this);
                        break;
                    case 3:
                        //delete bank database
                        AskOption("Bank").show();
                        break;
                    case 4:
                        //delete social account database
                        AskOption("Social").show();
                        break;
                    case 5:
                        AskOption("Ecommerce").show();
                        break;
                    case 6:
                        settingIntent=new Intent(getApplicationContext(),NotificationSettings.class);
                        startActivity(settingIntent);
                        myUtility.finishTask(SettingsActivity.this);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void deleteBankDatabase(String myPassword) {
        boolean password_ok=mySharedPreferences.verifyPassword(myPassword,getApplicationContext());
        if (password_ok) {
            bankDataSource = new BankDataSource(SettingsActivity.this);
            if (bankDataSource.deleteDatabase(getApplicationContext())) {
                myUtility.createToast(getApplicationContext(), "Bank details cleared");
                Log.i(LOG_CAT, "Bank details deleted");
            } else {
                myUtility.createToast(getApplicationContext(), "Bank details are absent");
            }
        }else {
            myUtility.createToast(getApplicationContext(),"Wrong password");
        }
    }

    private void deleteSocialDatabase(String myPassword) {
        boolean password_ok=mySharedPreferences.verifyPassword(myPassword,getApplicationContext());
        if (password_ok) {
            socialAccountDataSource = new SocialAccountDataSource(SettingsActivity.this);
            if (socialAccountDataSource.deleteDatabase(getApplicationContext())) {
                myUtility.createToast(getApplicationContext(), "Social Account details cleared");
                Log.i(LOG_CAT, "Social Account details cleared");
            } else {
                myUtility.createToast(getApplicationContext(), "Social Account details are absent");
            }
        }else{
            myUtility.createToast(getApplicationContext(),"Wrong password");
        }
    }

    private void deleteEcommerceDatabase(String myPassword){
        boolean password_ok=mySharedPreferences.verifyPassword(myPassword,getApplicationContext());
        if (password_ok) {
            ecommerceDataSource = new EcommerceDataSource(SettingsActivity.this);
            if (ecommerceDataSource.deleteDatabase(getApplicationContext())) {
                myUtility.createToast(getApplicationContext(), "Ecommerce Account details cleared");
                Log.i(LOG_CAT, "Social Account database deleted");
            } else {
                myUtility.createToast(getApplicationContext(), "Ecommerce Account details are absebt");
            }
        }else{
            myUtility.createToast(getApplicationContext(),"Wrong password");
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent i=new Intent(getApplicationContext(),SelectionActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                myUtility.finishTask(SettingsActivity.this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(),SelectionActivity.class);
        startActivity(i);
        myUtility.finishTask(SettingsActivity.this);
    }

    private AlertDialog AskOption(final String whichDataBase){
        LayoutInflater prompt_layout=LayoutInflater.from(SettingsActivity.this);
        View promptsView =prompt_layout.inflate(R.layout.prompt, null);
        final EditText prompt_password=(EditText)promptsView.findViewById(R.id.prompt_password);

        return new AlertDialog.Builder(this)
                .setView(promptsView)
                .setTitle("Warning")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        password = prompt_password.getText().toString();
                        switch (whichDataBase) {
                            case "Bank":
                                deleteBankDatabase(password);
                                break;
                            case "Social":
                                deleteSocialDatabase(password);
                                break;
                            case "Ecommerce":
                                deleteEcommerceDatabase(password);
                                break;
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        myUtility.createToast(getApplicationContext(), "Noting is done");
                        dialog.dismiss();
                    }
                })
                .create();

    }

}
