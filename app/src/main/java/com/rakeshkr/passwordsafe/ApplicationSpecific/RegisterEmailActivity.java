package com.rakeshkr.passwordsafe.ApplicationSpecific;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rakeshkr.passwordsafe.R;
import com.rakeshkr.passwordsafe.Utility.MyUtility;

public class RegisterEmailActivity extends Activity {
    String intentString;
    String[] intentArray;
    String userName,password;
    MyUtility myUtility=new MyUtility();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_email);
        try {
            intentString = getIntent().getExtras().getString("intentString");
            if (intentString.length()>0) {
                intentArray = intentString.split(" ");
            }
            userName=intentArray[0];
            password=intentArray[1];
        }catch (Exception e){
            e.printStackTrace();
        }



        TextView step,hey_user;
        step=(TextView)findViewById(R.id.step);
        step.setText("Step 3/5");

        hey_user=(TextView)findViewById(R.id.hey_user);
        hey_user.setText("Hey "+userName+",");

        final EditText emailEditTxt;
        emailEditTxt=(EditText)findViewById(R.id.email_address_editText);

        Button nextButton,backButton;
        nextButton=(Button)findViewById(R.id.next);
        backButton=(Button)findViewById(R.id.back);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=emailEditTxt.getText().toString().trim();
                if (email.length()>0) {
                    Intent intent = new Intent(getApplicationContext(), RegisterMobileActivity.class);
                    intent.putExtra("intentString",userName+" "+password+" "+email);
                    startActivity(intent);
                    finish();
                }else {
                    myUtility.createToast(getApplicationContext(),"Field cannot be empty!");
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }

    private void goBack() {
        Intent intent=new Intent(getApplicationContext(),RegisterPasswordActivity.class);
        myUtility.createToast(RegisterEmailActivity.this,intentString);
        intent.putExtra("intentString", userName + " " +password);
        startActivity(intent);
        myUtility.finishTask(RegisterEmailActivity.this);
    }

    @Override
    public void onBackPressed() {
        goBack();
    }
}
