package com.rakeshkr.passwordsafe.ApplicationSpecific;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rakeshkr.passwordsafe.Utility.AlertDialogManager;
import com.rakeshkr.passwordsafe.Utility.MySharedPreferences;
import com.rakeshkr.passwordsafe.Utility.MyUtility;
import com.rakeshkr.passwordsafe.R;
import com.rakeshkr.passwordsafe.Utility.SecurityActivity;


public class ValidatePin extends Activity implements OnClickListener{
    EditText txtPassword,email;
    private String seed="hfhgfdtrdhhjhgfytfytdtrdjhyugtff";
    AlertDialogManager alert=new AlertDialogManager();
    TextView appNameTxtView;
    SharedPreferences sharedpreferences,sharedPreferences_app_info;
    Button verify;
    MyUtility myUtility=new MyUtility();
    Button one,two,three,four,five,six,seven,eight,nine,zero;
    ImageButton backspace;
    Vibrator vibrator;
    String myPassword="";
    Animation shakeAppName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            ValidatePin.this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();
        }
        setContentView(R.layout.validate_pin);

        txtPassword = (EditText) findViewById(R.id.password);
        verify=(Button) findViewById(R.id.verify);
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

        shakeAppName= AnimationUtils.loadAnimation(this,R.anim.shake_text);
        shakeAppName.setDuration(1500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    appNameTxtView.startAnimation(shakeAppName);
            }
        },5000);

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
                    Intent intent=new Intent();
                    setResult(200, intent);
                    finish();

                } else {
                    // username / password doesn't match
                    vibrator.vibrate(500);
                    myUtility.shakeEditText(ValidatePin.this,txtPassword);
                    myUtility.createToast(ValidatePin.this,"Invalid pin");
                }
            }else {
                myUtility.createToast(getApplicationContext(),"stored password is null");
            }
        }else {
            // user didn't entered username or password
            // Show alert asking him to enter the details
            alert.showAlertDialog(ValidatePin.this, "Login failed..", "Username/Password/Email is missing", false);
        }
        txtPassword.setText("");
        myPassword="";
    }



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
        ValidatePin.this.finish();
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
