package com.rakeshkr.passwordsafe.ApplicationSpecific;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.rakeshkr.passwordsafe.R;
import com.rakeshkr.passwordsafe.Utility.MyUtility;

public class WelcomeActivity extends Activity {
    MyUtility myUtility=new MyUtility();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title_show);

        final TextView appNameTxtView,tagNameTxtView;
        final Button login_button,signup_button;
        appNameTxtView=(TextView)findViewById(R.id.appName);
        tagNameTxtView=(TextView)findViewById(R.id.tagline);

        login_button=(Button)findViewById(R.id.login);
        signup_button=(Button)findViewById(R.id.signup);


        Animation animation= AnimationUtils.loadAnimation(this,R.anim.left_to_right);
        appNameTxtView.startAnimation(animation);

        Animation animation_tagLine=AnimationUtils.loadAnimation(WelcomeActivity.this,R.anim.left_to_right);
        tagNameTxtView.startAnimation(animation_tagLine);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation animation_login = AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.fade_in);
                login_button.startAnimation(animation_login);

                Animation animation_signup = AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.fade_in);
                signup_button.startAnimation(animation_signup);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LockActivity.class);
                startActivity(intent);
                myUtility.finishTask(WelcomeActivity.this);
            }
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),RegisterUsernameActivity.class);
                startActivity(intent);
                myUtility.finishTask(WelcomeActivity.this);
            }
        });
    }
}
