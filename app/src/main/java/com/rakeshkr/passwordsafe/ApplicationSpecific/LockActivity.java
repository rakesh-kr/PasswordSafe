package com.rakeshkr.passwordsafe.ApplicationSpecific;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.rakeshkr.passwordsafe.R;
import com.rakeshkr.passwordsafe.Utility.AlertDialogManager;
import com.rakeshkr.passwordsafe.Utility.MySharedPreferences;
import com.rakeshkr.passwordsafe.Utility.MyUtility;
import com.rakeshkr.passwordsafe.Utility.SecurityActivity;
import com.rakeshkr.passwordsafe.Utility.SmsSender;

import java.util.Random;


public class LockActivity extends Activity implements OnClickListener{
    EditText txtPassword,email;
    private String seed;
    AlertDialogManager alert=new AlertDialogManager();

    String LOG_CAT="PasswordSafe";
    TextView forgot_passwordTxt,newUser_txt,appNameTxtView;
    SharedPreferences sharedpreferences,sharedPreferences_app_info,sharedPreferences_notification_info;
    Button verify;
    MyUtility myUtility=new MyUtility();
    ImageView email_correct_img;
    Button registerPopupBtn,notNowPopupBtn;
    Button one,two,three,four,five,six,seven,eight,nine,zero;
    ImageButton backspace;
    Vibrator vibrator;
    String myPassword="";
    Animation shakeAppName;

    int numOfAttempts=0;

    MySharedPreferences mySharedPreferences=new MySharedPreferences();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            LockActivity.this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();
        }
        setContentView(R.layout.activity_lock);
        seed=MySharedPreferences.seed;
        txtPassword = (EditText) findViewById(R.id.password);
        newUser_txt=(TextView)findViewById(R.id.new_user);
        verify=(Button) findViewById(R.id.verify);
        forgot_passwordTxt=(TextView)findViewById(R.id.forgot_password);
        txtPassword.setFocusable(false);
        txtPassword.setEnabled(false);
        vibrator=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        one=(Button)findViewById(R.id.one);
        two=(Button)findViewById(R.id.two);
        three=(Button)findViewById(R.id.three);
        four=(Button)findViewById(R.id.four);
        five=(Button)findViewById(R.id.five);
        six=(Button)findViewById(R.id.six);
        seven=(Button)findViewById(R.id.seven);
        eight=(Button)findViewById(R.id.eight);
        nine=(Button)findViewById(R.id.nine);
        zero=(Button)findViewById(R.id.zero);
        backspace=(ImageButton)findViewById(R.id.back);

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);
        nine.setOnClickListener(this);
        zero.setOnClickListener(this);
        backspace.setOnClickListener(this);
        verify.setOnClickListener(this);

        appNameTxtView=(TextView)findViewById(R.id.appName);

        sharedpreferences = getSharedPreferences(MySharedPreferences.MyPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences_app_info=getSharedPreferences(MySharedPreferences.MyAPPINFO,Context.MODE_PRIVATE);
        final String is_not_first=sharedPreferences_app_info.getString(MySharedPreferences.ISFIRSTTIME,null);
        sharedPreferences_notification_info=getSharedPreferences(MySharedPreferences.NOTIFICATIONPREFERENCES,Context.MODE_PRIVATE);

        if (sharedpreferences.getInt(MySharedPreferences.Attempts,0)==3){
            Intent gotoIntent=new Intent(getApplicationContext(),ApplicationLocked.class);
            gotoIntent.putExtra("attempts",sharedpreferences.getInt(MySharedPreferences.Attempts,0));
            startActivity(gotoIntent);
            finish();
        }

        if(is_not_first==null){
            SharedPreferences.Editor editor=sharedPreferences_notification_info.edit();
            if (!sharedPreferences_notification_info.getBoolean(MySharedPreferences.Vibration,false)){
                editor.putBoolean(MySharedPreferences.Vibration,true);
            }
            if (!sharedPreferences_notification_info.getBoolean(MySharedPreferences.Sound,false)){
                editor.putBoolean(MySharedPreferences.Sound,true);
            }
            if (!sharedPreferences_notification_info.getBoolean(MySharedPreferences.SmsOtp,true)){
                editor.putBoolean(MySharedPreferences.SmsOtp,false);
            }
            editor.commit();
            Intent intent=new Intent(getApplicationContext(),WelcomeActivity.class);
            startActivity(intent);
            finish();

            newUser_txt.setVisibility(View.VISIBLE);
            forgot_passwordTxt.setVisibility(View.INVISIBLE);
        }else if(is_not_first.equals("TRUE")){
            forgot_passwordTxt.setVisibility(View.VISIBLE);
            newUser_txt.setVisibility(View.INVISIBLE);
        }

        shakeAppName= AnimationUtils.loadAnimation(this,R.anim.shake_text);
        shakeAppName.setDuration(1500);
        appNameTxtView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                appNameTxtView.startAnimation(shakeAppName);
            }
        });



    }


    private void login_button_click(){
        String password = txtPassword.getText().toString();
        if (password.trim().length() > 0) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            String storedPassword = sharedpreferences.getString(MySharedPreferences.Password, null);
            if (storedPassword!=null) {
                try {
                    storedPassword = SecurityActivity.decrypt(seed, sharedpreferences.getString(MySharedPreferences.Password, null));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "caught exception in decrypting password", Toast.LENGTH_LONG).show();
                }

                if (password.equals(storedPassword)) {
                    // Staring MainActivity
                    Intent i = new Intent(getApplicationContext(), SelectionActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();

                } else {
                    // username / password doesn't match
                    if (sharedPreferences_notification_info.getBoolean(MySharedPreferences.Vibration,false)) {
                        vibrator.vibrate(500);
                    }
                    myUtility.shakeEditText(LockActivity.this, txtPassword);

                    numOfAttempts+=1;
                    myUtility.createToast(LockActivity.this, "Invalid pin, Attempts left : "+String.valueOf((3-numOfAttempts)));
                    if (numOfAttempts==3){
                        Random randomPass= new Random();
                        int i=Math.abs(randomPass.nextInt(Integer.MAX_VALUE));
                        sendMail(i);
                        if (mySharedPreferences.isOtpToMobileEnabled(getApplicationContext())){
                            SmsSender smsSender=new SmsSender(getApplicationContext());
                            try {
                                smsSender.setToPhNum(SecurityActivity.decrypt(seed,sharedpreferences.getString(MySharedPreferences.Mobile,"Please add")));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            smsSender.setMsg("Use " + String.valueOf(i) + " to unlock Your PasswordSafe application");
                            smsSender.sendMsg();
                        }else {
                            myUtility.createToast(LockActivity.this,"otp not sent");
                        }
                        Intent gotoIntent=new Intent(getApplicationContext(),ApplicationLocked.class);
                        gotoIntent.putExtra("attempts", numOfAttempts);
                        startActivity(gotoIntent);
                        finish();

                    }
                }
            }else {
                vibrator.vibrate(500);
                initiatePopupWindow();
            }
        }else {

            alert.showAlertDialog(LockActivity.this, "Login failed..", "Username/Password/Email is missing", false);
        }
        txtPassword.setText("");
        myPassword="";
    }

    private void sendMail(int i) {
        String[] toArr = {sharedpreferences.getString(MySharedPreferences.Email,"app.safetreasure.rakesh@gmail.com")};

        String secretKey=String.valueOf(i);
        String body="Use \""+secretKey+"\" to unlock the application.\nIf 3 attempts were not made by you then your application is tried to login by others.\nSo please change the password soon";
        try{
            if(myUtility.composeMailSend(getApplicationContext(),toArr,"3 Login attempts failed",body)){
                SharedPreferences.Editor editor=sharedpreferences.edit();
                editor.putInt(MySharedPreferences.Attempts, numOfAttempts);
                editor.putString(MySharedPreferences.SecretKeyGenerated, secretKey);
                editor.commit();
                //locked
            }else{
                Toast.makeText(LockActivity.this, "Email was not sent", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Log.e(LOG_CAT, "Could not send email", e);
            Toast.makeText(LockActivity.this, "Could not send email", Toast.LENGTH_LONG).show();
        }
    }


    private PopupWindow pwindo;
    private void initiatePopupWindow() {
        try {
            // We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) LockActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.register_first_time,
                    (ViewGroup) findViewById(R.id.popup_view_id));
            pwindo = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT , true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
            pwindo.setFocusable(true);
            notNowPopupBtn=(Button)layout.findViewById(R.id.not_now);
            registerPopupBtn = (Button) layout.findViewById(R.id.register_btn);
            registerPopupBtn.setOnClickListener(register_button_click_listener);
            notNowPopupBtn.setOnClickListener(notNow_button_click_listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private OnClickListener notNow_button_click_listener=new OnClickListener() {
        @Override
        public void onClick(View view) {
            pwindo.dismiss();
        }
    };

    private OnClickListener register_button_click_listener = new OnClickListener() {
        public void onClick(View v) {
            newUserRegister(v);

        }
    };


    public void forgotPassword(View v){
        Intent intent=new Intent(getApplicationContext(),ForgotPasswordActivity.class);
        startActivity(intent);
        myUtility.finishTask(LockActivity.this);
    }


    public void newUserRegister(View v){
        Intent intent=new Intent(getApplicationContext(),RegisterFirstTime.class);
        startActivity(intent);
        myUtility.finishTask(LockActivity.this);
    }

    private final TextWatcher myEmailWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            email.setError(null);
            email.setError("beforeTextChanged");
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            email.setError(null);
            email.setError("onTextChanged");
        }

        @Override
        public void afterTextChanged(Editable editable) {
            email.setError(null);
            if (myUtility.isValidEmail(email.getText().toString().trim())){
                email.setError(null);

                email_correct_img.setVisibility(View.VISIBLE);
            }else {

                email.setError("invalid EMAIL");
                email_correct_img.setVisibility(View.INVISIBLE);
            }

        }
    };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lock_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        AlertDialog diaBox=AskOption();
        diaBox.show();
    }

    private AlertDialog AskOption(){
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit?")

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        myUtility.finishTask(LockActivity.this);
                        LockActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(pwindo!= null)
            pwindo.dismiss();
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.one:
                myPassword=myPassword+"1";
                txtPassword.setText(myPassword);
                break;
            case R.id.two:
                myPassword=myPassword+"2";
                txtPassword.setText(myPassword);
                break;
            case R.id.three:
                myPassword=myPassword+"3";
                txtPassword.setText(myPassword);
                break;
            case R.id.four:
                myPassword=myPassword+"4";
                txtPassword.setText(myPassword);
                break;
            case R.id.five:
                myPassword=myPassword+"5";
                txtPassword.setText(myPassword);
                break;
            case R.id.six:
                myPassword=myPassword+"6";
                txtPassword.setText(myPassword);
                break;
            case R.id.seven:
                myPassword=myPassword+"7";
                txtPassword.setText(myPassword);
                break;
            case R.id.eight:
                myPassword=myPassword+"8";
                txtPassword.setText(myPassword);
                break;
            case R.id.nine:
                myPassword=myPassword+"9";
                txtPassword.setText(myPassword);
                break;
            case R.id.zero:
                myPassword=myPassword+"0";
                txtPassword.setText(myPassword);
                break;
            case R.id.back:
                String temp=txtPassword.getText().toString().trim();
                if (temp.length()>0){
                    temp=temp.substring(0,temp.length()-1);
                    myPassword=temp;
                    txtPassword.setText(myPassword);
                }
                break;
            case R.id.verify:
                login_button_click();
                break;
            default:
                break;

        }
    }
}
