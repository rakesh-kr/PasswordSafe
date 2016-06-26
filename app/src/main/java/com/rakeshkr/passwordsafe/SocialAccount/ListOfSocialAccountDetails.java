package com.rakeshkr.passwordsafe.SocialAccount;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rakeshkr.passwordsafe.ApplicationSpecific.SelectionActivity;
import com.rakeshkr.passwordsafe.ApplicationSpecific.SettingsActivity;
import com.rakeshkr.passwordsafe.Bank.ListOfBankDetails;
import com.rakeshkr.passwordsafe.Ecommerce.ListOfEcommerceAccountDetails;
import com.rakeshkr.passwordsafe.R;
import com.rakeshkr.passwordsafe.Utility.ListViewAdapterForImage;
import com.rakeshkr.passwordsafe.Utility.MySharedPreferences;
import com.rakeshkr.passwordsafe.Utility.MyUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ListOfSocialAccountDetails extends AppCompatActivity implements OnItemClickListener {
    String LOG_CAT="PasswordSafe";
    MyUtility myUtility=new MyUtility();
    SocialAccountDataSource socialAccountDataSource;

    ListView lView;
    ListViewAdapterForImage lviewAdapter;
    final List<String> valueList = new ArrayList<String>();
    final List<String> keyList = new ArrayList<String>();
    String[] socialAccountArrayKeyList,socialAccountArrayValueList;

    String[] socialAccountArrayList;
    Integer[] imgIdsList;
    Map<String,String> socialAccounts = new HashMap<String,String>();

    //Defining Variables
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_social_account_activity);
        setup_nav_bar();
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();
                Intent selectionIntent;
                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    case R.id.bank_detail_id:
                        selectionIntent = new Intent(getApplicationContext(), ListOfBankDetails.class);
                        startActivity(selectionIntent);
                        myUtility.finishTask(ListOfSocialAccountDetails.this);
                        return true;
                    case R.id.social_account_id:
                        selectionIntent = new Intent(getApplicationContext(), ListOfSocialAccountDetails.class);
                        startActivity(selectionIntent);
                        myUtility.finishTask(ListOfSocialAccountDetails.this);
                        return true;
                    case R.id.ecom_details_id:
                        selectionIntent = new Intent(getApplicationContext(), ListOfEcommerceAccountDetails.class);
                        startActivity(selectionIntent);
                        myUtility.finishTask(ListOfSocialAccountDetails.this);
                        return true;
                    case R.id.home:
                        selectionIntent = new Intent(getApplicationContext(), SelectionActivity.class);
                        startActivity(selectionIntent);
                        myUtility.finishTask(ListOfSocialAccountDetails.this);
                        return true;
                    case R.id.setting_id:
                        selectionIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(selectionIntent);
                        myUtility.finishTask(ListOfSocialAccountDetails.this);
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });
        navigationView.getMenu().getItem(2).setChecked(true);
        navigationView.setItemIconTintList(null);
        socialAccountDataSource = new SocialAccountDataSource(this);
        socialAccountDataSource.open();

        socialAccounts = socialAccountDataSource.getAllEmails();
        if (socialAccounts.isEmpty()){
            LinearLayout linearLayout=(LinearLayout)findViewById(R.id.list_of_social_account_id);

            TextView textView2 = new TextView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;
            layoutParams.setMargins(10, 150, 10, 10); // (left, top, right, bottom)
            textView2.setLayoutParams(layoutParams);
            textView2.setText("No Records found :(");
            textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            textView2.setBackgroundColor(0xffffdbdb); // hex color 0xAARRGGBB
            linearLayout.addView(textView2);
        }else {
            setListView();
        }


    }

    private void setListView() {
        for(String key : socialAccounts.keySet()){
            String value = socialAccounts.get(key);
            valueList.add(value);
            keyList.add(key);
        }
        socialAccountArrayKeyList=keyList.toArray(new String[keyList.size()]);
        socialAccountArrayValueList=valueList.toArray(new String[valueList.size()]);

        imgIdsList=(socialAccountDataSource.getListOfImgageId(keyList)).toArray(new Integer[keyList.size()]);
        lView = (ListView) findViewById(R.id.list_of_social_ac);
        lviewAdapter = new ListViewAdapterForImage(this, socialAccountArrayValueList, imgIdsList);
        lView.setAdapter(lviewAdapter);
        lView.setOnItemClickListener(this);
        registerForContextMenu(lView);
    }

    public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long id) {
        final String item = (String) adapterView.getItemAtPosition(position);
        String dbId=keyList.get(position);
        Intent viewIntent=new Intent(getApplicationContext(),DisplayIndividualSocialAccountDetails.class);
        viewIntent.putExtra("dbId", dbId);
        viewIntent.putExtra("title", valueList.get(position));
        startActivity(viewIntent);
        finish();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        MenuInflater inflater = getMenuInflater();
        menu.setHeaderTitle((String) socialAccountArrayValueList[info.position]);
        menu.setHeaderIcon(R.drawable.ic_info);
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String databaseId=(String)socialAccountArrayKeyList[info.position];
        String title=(String)socialAccountArrayValueList[info.position];
        switch (item.getItemId()) {
            case R.id.edit:
                Intent editIntent=new Intent(getApplicationContext(),UpdateIndividualSocialAccountDetails.class);
                editIntent.putExtra("dbId",databaseId);
                editIntent.putExtra("title",title);
                startActivity(editIntent);
                finish();
                return true;
            case R.id.delete:
                AskOption(Integer.parseInt(databaseId)).show();
                return true;
            case R.id.view:
                Intent viewIntent=new Intent(getApplicationContext(),DisplayIndividualSocialAccountDetails.class);
                viewIntent.putExtra("dbId",databaseId);
                viewIntent.putExtra("title",title);
                startActivity(viewIntent);
                finish();
                return true;
            default:
                return super.onContextItemSelected(item);

        }
    }

    private void deleteSocialAccountDetail(int dbid) {
        if (socialAccountDataSource.deleteRow(dbid)!=0){
            myUtility.createToast(getApplicationContext(),"Deleted successfully");
        }else {
            myUtility.createToast(getApplicationContext(),"Account not present");
        }
        Intent intent=new Intent(getApplicationContext(),ListOfSocialAccountDetails.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings_bank_social_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                Intent settingsIntent=new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(settingsIntent);
                finish();
                break;
            case R.id.action_help:
                break;
            case R.id.action_add_to_db:
                Intent addIntent=new Intent(getApplicationContext(),AddNewSocialAccountDetail.class);
                startActivity(addIntent);
                finish();
                break;
            default:
                break;
        }

        // Activate the navigation drawer toggle
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
        if (isNavDrawerOpen()){
            closeNavDrawer();
        }else {
            Intent i = new Intent(getApplicationContext(), SelectionActivity.class);
            startActivity(i);
            myUtility.finishTask(ListOfSocialAccountDetails.this);
        }
    }
    protected boolean isNavDrawerOpen() {
        return drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    protected void closeNavDrawer() {
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
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

    private AlertDialog AskOption(final int id){
        return new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete?")

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteSocialAccountDetail(id);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        myUtility.createToast(getApplicationContext(), "Did not deleted");
                        dialog.dismiss();
                    }
                })
                .create();

    }

    private void setup_nav_bar() {
        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

        }
        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navList);
        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout != null) {
            drawerLayout.setDrawerListener(actionBarDrawerToggle);
        }

        SharedPreferences sharedPreferences=getSharedPreferences(MySharedPreferences.MyPREFERENCES, MODE_PRIVATE);
        String username=sharedPreferences.getString(MySharedPreferences.Name, "User not found!");
        String email=sharedPreferences.getString(MySharedPreferences.Email,"Not specified!");
        View header=navigationView.getHeaderView(0);
        TextView username_textView=(TextView)header.findViewById(R.id.username);
        if (username_textView != null) {
            username_textView.setText(username);
        }
        TextView email_textView=(TextView)header.findViewById(R.id.email);
        if (email_textView != null) {
            email_textView.setText(email);
        }


        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.openDrawer, R.string.closeDrawer){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);

            }
        };
        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

    }

}
