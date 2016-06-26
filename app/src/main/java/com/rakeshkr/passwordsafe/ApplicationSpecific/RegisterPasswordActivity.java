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

public class RegisterPasswordActivity extends Activity {
    String userName;
    String intentString;
    String[] intentArray;
    MyUtility myUtility=new MyUtility();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_password);

        intentString=getIntent().getExtras().getString("intentString");
        if (intentString != null) {
            intentArray=intentString.split(" ");
        }
        userName=intentArray[0];
        TextView step,hey_user;
        step=(TextView)findViewById(R.id.step);
        step.setText("Step 2/5");

        hey_user=(TextView)findViewById(R.id.hey_user);
        hey_user.setText("Hey " + userName + ",");

        final EditText passwordEditTxt,confirmPasswordEditTxt;
        passwordEditTxt=(EditText)findViewById(R.id.password_editText);
        confirmPasswordEditTxt=(EditText)findViewById(R.id.confirm_password_editText);

        Button nextButton,backButton;
        nextButton=(Button)findViewById(R.id.next);
        backButton=(Button)findViewById(R.id.back);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password=passwordEditTxt.getText().toString().trim();
                String confirmPassword=confirmPasswordEditTxt.getText().toString().trim();
                if (password.length()>0) {
                    if (password.equals(confirmPassword)) {
                        Intent intent = new Intent(getApplicationContext(), RegisterEmailActivity.class);
                        intent.putExtra("intentString", userName + " " + password);
                        startActivity(intent);
                        myUtility.finishTask(RegisterPasswordActivity.this);
                    }else {
                        myUtility.createToast(getApplicationContext(),"Password and confirm password mis match");
                    }
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
        Intent intent=new Intent(getApplicationContext(),RegisterUsernameActivity.class);
        startActivity(intent);
        myUtility.finishTask(RegisterPasswordActivity.this);
    }

    @Override
    public void onBackPressed() {
        goBack();
    }
}
