package com.rakeshkr.passwordsafe.ApplicationSpecific;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rakeshkr.passwordsafe.Bank.AddNewBankDetail;
import com.rakeshkr.passwordsafe.Bank.BankDataSource;
import com.rakeshkr.passwordsafe.Bank.ListOfBankDetails;
import com.rakeshkr.passwordsafe.Ecommerce.AddNewEcommerceDetail;
import com.rakeshkr.passwordsafe.Ecommerce.EcommerceDataSource;
import com.rakeshkr.passwordsafe.Ecommerce.ListOfEcommerceAccountDetails;
import com.rakeshkr.passwordsafe.R;
import com.rakeshkr.passwordsafe.SocialAccount.AddNewSocialAccountDetail;
import com.rakeshkr.passwordsafe.SocialAccount.ListOfSocialAccountDetails;
import com.rakeshkr.passwordsafe.SocialAccount.SocialAccountDataSource;
import com.rakeshkr.passwordsafe.Utility.DataObject;
import com.rakeshkr.passwordsafe.Utility.MyRecyclerViewAdapter;
import com.rakeshkr.passwordsafe.Utility.MySharedPreferences;
import com.rakeshkr.passwordsafe.Utility.MyUtility;

import java.util.ArrayList;


public class SelectionActivity extends AppCompatActivity
{
    //Defining Variables
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    MyUtility myUtility=new MyUtility();
    BankDataSource bankDataSource;
    SocialAccountDataSource socialAccountDataSource;
    EcommerceDataSource ecommerceDataSource;
    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };
    int bankAccountCount=0,socialAccountCount=0,ecommerceAccountCount=0;
    private boolean open=false;

    Button bank_button,social_button,e_button;

    private String mActivityTitle;
    private FloatingActionButton fab_main;
    FloatingActionButton bank_fab;
    FloatingActionButton social_fab;
    FloatingActionButton ecom_fab;


    //Animations
    Animation show_fab_1;
    Animation hide_fab_1;
    Animation show_fab_2;
    Animation hide_fab_2;
    Animation show_fab_3;
    Animation hide_fab_3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection_activity_layout);
        setup_nav_bar();

        mActivityTitle = getTitle().toString();
        fab_main = (FloatingActionButton) findViewById(R.id.add_id);
        //Floating Action Buttons
        bank_fab = (FloatingActionButton) findViewById(R.id.bank_fab);
        social_fab = (FloatingActionButton) findViewById(R.id.social_fab);
        ecom_fab = (FloatingActionButton) findViewById(R.id.ecom_fab);

        //Animations
        show_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.bank_button_show);
        hide_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.bank_button_hide);
        show_fab_2 = AnimationUtils.loadAnimation(getApplication(), R.anim.social_button_show);
        hide_fab_2 = AnimationUtils.loadAnimation(getApplication(), R.anim.social_button_hide);
        show_fab_3 = AnimationUtils.loadAnimation(getApplication(), R.anim.ecom_button_show);
        hide_fab_3 = AnimationUtils.loadAnimation(getApplication(), R.anim.ecom_button_hide);

        bankDataSource=new BankDataSource(this);
        bankDataSource.open();
        socialAccountDataSource=new SocialAccountDataSource(this);
        socialAccountDataSource.open();
        ecommerceDataSource=new EcommerceDataSource(this);
        ecommerceDataSource.open();

        bankAccountCount=bankDataSource.getRowCount();
        socialAccountCount=socialAccountDataSource.getRowCount();
        ecommerceAccountCount=ecommerceDataSource.getRowCount();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
        }
        mAdapter = new MyRecyclerViewAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);



        bank_button=(Button)findViewById(R.id.bank_button);
        social_button=(Button)findViewById(R.id.social_button);
        e_button=(Button)findViewById(R.id.e_button);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();
                Intent selectionIntent;
                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()){
                    case R.id.bank_detail_id:
                        selectionIntent = new Intent(getApplicationContext(), ListOfBankDetails.class);
                        startActivity(selectionIntent);
                        myUtility.finishTask(SelectionActivity.this);
                        return true;
                    case R.id.social_account_id:
                        selectionIntent = new Intent(getApplicationContext(), ListOfSocialAccountDetails.class);
                        startActivity(selectionIntent);
                        myUtility.finishTask(SelectionActivity.this);
                        return true;
                    case R.id.ecom_details_id:
                        selectionIntent=new Intent(getApplicationContext(), ListOfEcommerceAccountDetails.class);
                        startActivity(selectionIntent);
                        myUtility.finishTask(SelectionActivity.this);
                        return true;
                    case R.id.home:
                        selectionIntent=new Intent(getApplicationContext(), SelectionActivity.class);
                        startActivity(selectionIntent);
                        myUtility.finishTask(SelectionActivity.this);
                        return true;
                    case R.id.setting_id:
                        selectionIntent=new Intent(getApplicationContext(),SettingsActivity.class);
                        startActivity(selectionIntent);
                        myUtility.finishTask(SelectionActivity.this);
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });


        //fab clicks
        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!open) {
                    //Display FAB menu
                    expandFAB();
                    open = true;
                } else {
                    //Close FAB menu
                    hideFAB();
                    open = false;
                }
            }
        });

        bank_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectionIntent;
                selectionIntent = new Intent(getApplicationContext(), AddNewBankDetail.class);
                startActivity(selectionIntent);
                myUtility.finishTask(SelectionActivity.this);
            }
        });

        social_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectionIntent;
                selectionIntent = new Intent(getApplicationContext(), AddNewSocialAccountDetail.class);
                startActivity(selectionIntent);
                myUtility.finishTask(SelectionActivity.this);
            }
        });

        ecom_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectionIntent;
                selectionIntent = new Intent(getApplicationContext(), AddNewEcommerceDetail.class);
                startActivity(selectionIntent);
                myUtility.finishTask(SelectionActivity.this);
            }
        });

        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setItemIconTintList(null);

    }

    private void setup_nav_bar() {
        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle(R.string.app_name);
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


    private void myItemClickMethod(int position) {
        Intent selectionIntent;
        switch (position) {
            case 0:
                selectionIntent = new Intent(getApplicationContext(), ListOfBankDetails.class);
                startActivity(selectionIntent);
                myUtility.finishTask(SelectionActivity.this);
                break;
            case 1:
                selectionIntent = new Intent(getApplicationContext(), ListOfSocialAccountDetails.class);
                startActivity(selectionIntent);
                myUtility.finishTask(SelectionActivity.this);
                break;
            case 2:
                selectionIntent=new Intent(getApplicationContext(), ListOfEcommerceAccountDetails.class);
                startActivity(selectionIntent);
                myUtility.finishTask(SelectionActivity.this);
                break;
            case 3:
                selectionIntent=new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(selectionIntent);
                myUtility.finishTask(SelectionActivity.this);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        bankDataSource.open();
        socialAccountDataSource.open();
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                myItemClickMethod(position);
            }
        });
    }

    private ArrayList<DataObject> getDataSet() {
        ArrayList results = new ArrayList<DataObject>();
        String[] selectionItems={"List of Bank Details","List of Social Account Details","List of E-Commerce Accounts","Settings"};
        String[] countList={"0","0","0"," "};
        countList[0]=String.valueOf(bankAccountCount);
        countList[1]=String.valueOf(socialAccountCount);
        countList[2]=String.valueOf(ecommerceAccountCount);
        Integer[] imgIds={R.drawable.ic_rupee,
                R.drawable.ic_social_media,
                R.drawable.ic_ecommerce,
                R.drawable.ic_settings_selection};
        for (int i=0;i<selectionItems.length;i++) {
            DataObject obj = new DataObject(selectionItems[i],imgIds[i],countList[i]);
            results.add(obj);
        }
        return results;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_settings:
                Intent i=new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(i);
                myUtility.finishTask(SelectionActivity.this);
                break;
            case R.id.action_help:
                break;
            case R.id.action_logout:
                //myUtility.logout(getApplicationContext());
                Intent intent=new Intent(getApplicationContext(), LockActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                myUtility.createToast(getApplicationContext(), "Logged out successfully");
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                finish();
                break;

        }
        // Activate the navigation drawer toggle
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();
        bankDataSource.close();
        socialAccountDataSource.close();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (mHandler != null) { mHandler.removeCallbacks(mRunnable); }
    }

    @Override
    public void onBackPressed() {
        if (isNavDrawerOpen()){
            closeNavDrawer();
        }
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        myUtility.createToast(this, "Please click BACK again to exit");
        mHandler.postDelayed(mRunnable, 2000);
    }

    protected boolean isNavDrawerOpen() {
        return drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    protected void closeNavDrawer() {
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }


    private void expandFAB() {

        //Floating Action Button 1
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) bank_fab.getLayoutParams();
        layoutParams.rightMargin += (int) (bank_fab.getWidth() * 0.25);
        layoutParams.bottomMargin += (int) (bank_fab.getHeight() * 3.9);
        bank_fab.setLayoutParams(layoutParams);
        bank_fab.startAnimation(show_fab_1);
        bank_fab.setClickable(true);

        //Floating Action Button 2
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) social_fab.getLayoutParams();
        layoutParams2.rightMargin += (int) (social_fab.getWidth() * 0.25);
        layoutParams2.bottomMargin += (int) (social_fab.getHeight() * 2.7);
        social_fab.setLayoutParams(layoutParams2);
        social_fab.startAnimation(show_fab_2);
        social_fab.setClickable(true);

        //Floating Action Button 3
        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) ecom_fab.getLayoutParams();
        layoutParams3.rightMargin += (int) (ecom_fab.getWidth() * 0.25);
        layoutParams3.bottomMargin += (int) (ecom_fab.getHeight() * 1.5);
        ecom_fab.setLayoutParams(layoutParams3);
        ecom_fab.startAnimation(show_fab_3);
        ecom_fab.setClickable(true);
        fab_main.setImageResource(R.drawable.ic_clear);

    }

    private void hideFAB() {

        //Floating Action Button 1
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) bank_fab.getLayoutParams();
        layoutParams.rightMargin -= (int) (bank_fab.getWidth() * 0.25);
        layoutParams.bottomMargin -= (int) (bank_fab.getHeight() * 3.9);
        bank_fab.setLayoutParams(layoutParams);
        bank_fab.startAnimation(hide_fab_1);
        bank_fab.setClickable(false);

        //Floating Action Button 2
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) social_fab.getLayoutParams();
        layoutParams2.rightMargin -= (int) (social_fab.getWidth() * 0.25);
        layoutParams2.bottomMargin -= (int) (social_fab.getHeight() * 2.7);
        social_fab.setLayoutParams(layoutParams2);
        social_fab.startAnimation(hide_fab_2);
        social_fab.setClickable(false);

        //Floating Action Button 3
        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) ecom_fab.getLayoutParams();
        layoutParams3.rightMargin -= (int) (ecom_fab.getWidth() * 0.25);
        layoutParams3.bottomMargin -= (int) (ecom_fab.getHeight() * 1.5);
        ecom_fab.setLayoutParams(layoutParams3);
        ecom_fab.startAnimation(hide_fab_3);
        ecom_fab.setClickable(false);
        fab_main.setImageResource(R.drawable.ic_add);
    }



}
