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

public class RegisterUsernameActivity extends Activity{
    MyUtility myUtility=new MyUtility();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_username);
        TextView step;
        step=(TextView)findViewById(R.id.step);
        step.setText("Step 1/5");

        final EditText usernameEditTxt;
        usernameEditTxt=(EditText)findViewById(R.id.user_name_editTxt);

        Button nextButton,backButton;
        nextButton=(Button)findViewById(R.id.next);
        backButton=(Button)findViewById(R.id.back);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName=usernameEditTxt.getText().toString().trim();
                if (userName.length()>0 ) {
                    if (!userName.contains(" ")) {
                        Intent intent = new Intent(getApplicationContext(), RegisterPasswordActivity.class);
                        intent.putExtra("intentString", userName);
                        startActivity(intent);
                        myUtility.finishTask(RegisterUsernameActivity.this);
                    }else {
                        myUtility.createToast(getApplicationContext(),"username cannot contain \" \"(space) character");
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
        Intent intent=new Intent(getApplicationContext(),WelcomeActivity.class);
        startActivity(intent);
        myUtility.finishTask(RegisterUsernameActivity.this);
    }

    @Override
    public void onBackPressed() {
        goBack();
    }
}
