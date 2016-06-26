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

public class RegisterMobileActivity extends Activity {
    String intentString;
    String[] intentArray;
    String userName,password,email;
    MyUtility myUtility=new MyUtility();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_mobile);
        try {
            intentString = getIntent().getExtras().getString("intentString");
            if (intentString.length()>0) {
                intentArray = intentString.split(" ");
            }
            userName=intentArray[0];
            password=intentArray[1];
            email=intentArray[2];
        }catch (Exception e){
            e.printStackTrace();
        }
        myUtility.createToast(RegisterMobileActivity.this,intentString);


        TextView step,hey_user;
        step=(TextView)findViewById(R.id.step);
        step.setText("Step 4/5");

        hey_user=(TextView)findViewById(R.id.hey_user);
        hey_user.setText("Hey "+userName+",");

        final EditText mobileEditTxt;
        mobileEditTxt=(EditText)findViewById(R.id.mobile_editText);

        Button nextButton,backButton;
        nextButton=(Button)findViewById(R.id.next);
        backButton=(Button)findViewById(R.id.back);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile=mobileEditTxt.getText().toString().trim();
                if (mobile.length()>0) {
                    Intent intent = new Intent(getApplicationContext(), RegisterAlgoAndSaltActivity.class);
                    intent.putExtra("intentString", userName + " " +password+" "+email+" " +mobile);
                    startActivity(intent);
                    myUtility.finishTask(RegisterMobileActivity.this);
                }else {
                    MyUtility myUtility=new MyUtility();
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
        Intent intent=new Intent(getApplicationContext(),RegisterEmailActivity.class);
        intent.putExtra("intentString", userName + " " +password+" "+email);
        startActivity(intent);
        myUtility.finishTask(RegisterMobileActivity.this);
    }

    @Override
    public void onBackPressed() {
        goBack();
    }
}
