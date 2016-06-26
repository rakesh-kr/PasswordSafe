package com.rakeshkr.passwordsafe.ApplicationSpecific;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import com.rakeshkr.passwordsafe.R;
import com.rakeshkr.passwordsafe.Utility.MySharedPreferences;
import com.rakeshkr.passwordsafe.Utility.MyUtility;

import java.util.ArrayList;
import java.util.List;

public class RegisterAlgoAndSaltActivity extends Activity implements OnItemSelectedListener {
    MyUtility myUtility=new MyUtility();
    String[] intentArray;
    String intentString;
    String userName;
    String password;
    String email;
    String mobile;
    Spinner algoSpinner;
    String algorithm,salt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_algo_salt);
        TextView step;
        step=(TextView)findViewById(R.id.step);
        step.setText("Step 5/5");
        try {
            intentString = getIntent().getExtras().getString("intentString");
            if (intentString.length()>0) {
                intentArray = intentString.split(" ");
            }
            userName=intentArray[0];
            password=intentArray[1];
            email=intentArray[2];
            mobile=intentArray[3];
        }catch (Exception e){
            e.printStackTrace();
        }
        myUtility.createToast(RegisterAlgoAndSaltActivity.this,intentString);
        final EditText saltEditTxt;
        saltEditTxt=(EditText)findViewById(R.id.salt_editTxt_id);
        saltEditTxt.setText(MySharedPreferences.seed);

        algoSpinner=(Spinner)findViewById(R.id.algo_spinner_id);
        algoSpinner.setOnItemSelectedListener(this);
        Button nextButton,backButton;
        nextButton=(Button)findViewById(R.id.next);
        backButton=(Button)findViewById(R.id.back);

        List<String> algos=new ArrayList<String>();
        algos=myUtility.algoSpinner(algos);

        ArrayAdapter<String> dataAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,algos);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        algoSpinner.setAdapter(dataAdapter);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salt=saltEditTxt.getText().toString().trim();
                algorithm=algoSpinner.getSelectedItem().toString();
                if (salt.length()!=32){
                    myUtility.createToast(RegisterAlgoAndSaltActivity.this,"Salt length should be 32");
                }else {

                    Intent intent = new Intent(getApplicationContext(), SetupAllCredentialsActivity.class);
                    intent.putExtra("intentString",userName + " " +password + " "+email+ " "+mobile +" "+algorithm+" "+salt);
                    startActivity(intent);
                    finish();
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
        Intent intent=new Intent(getApplicationContext(),RegisterMobileActivity.class);
        intent.putExtra("intentString", userName + " " +password + " "+email);
        startActivity(intent);
        myUtility.finishTask(RegisterAlgoAndSaltActivity.this);
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        algorithm=parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Todo
    }
}
