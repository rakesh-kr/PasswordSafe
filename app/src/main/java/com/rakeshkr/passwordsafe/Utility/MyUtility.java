package com.rakeshkr.passwordsafe.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rakeshkr.passwordsafe.ApplicationSpecific.LockActivity;
import com.rakeshkr.passwordsafe.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MyUtility extends Activity {

    public static final String senderMail = "app.safetreasure.rakesh@gmail.com";
    public static final String senderPassword = "jesus_362508312498";

    public void createToast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    public List<String> bankSpinner(List<String> categories){
        categories.add("Canara");
        categories.add("HDFC");
        categories.add("AXIS");
        categories.add("Karnataka");
        categories.add("ICICI");
        categories.add("Vijaya");
        categories.add("City Bank");
        categories.add("State Bank of India");
        categories.add("State Bank of Mysore");
        categories.add("Andra Bank");
        categories.add("Corporation Bank");
        categories.add("Syndicate Bank");
        categories.add("ING Vysya");
        categories.add("Yes Bank");
        categories.add("Others");

        return categories;
    }

    public List<String> algoSpinner(List<String> algorythms){
        algorythms.add("AES");

        return algorythms;
    }

    public List<String> socialAccountSpinner(List<String> categories){
        categories.add("GMAIL");
        categories.add("YAHOO");
        categories.add("MICROSOFT");
        categories.add("REDIFF");
        categories.add("Facebook");
        categories.add("Others");

        return categories;
    }

    public List<String> sellerSpinnerItems(List<String> categories){
        categories.add("Flipkart");
        categories.add("Amazon");
        categories.add("Myntra");
        categories.add("SnapDeal");
        categories.add("Jabong");
        categories.add("ShopClues");
        categories.add("Freecharge");
        categories.add("Paytm");
        categories.add("Others");
        return categories;
    }

    public String checkPasswordStrength(String password){
        int length=password.trim().length();
        if (length<=0 || length>=16){
            return "not allowed";
        }else if(length<4 && length>0){
            return "weak";
        }else if (length>=4 && length<7){
            return "moderate";
        }else if (length>=7 && length<=10){
            return "strong";
        }else if (length>=11 && length<=15){
            return "very strong";
        }
        return "error";
    }

    // validating email id
    public boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public String getDateTime(String whomModified){
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");

        return formatter.format(calendar.getTime())+" by "+whomModified;
    }

    public EditText disableEditText(EditText editText){
        editText.setEnabled(false);
        return editText;
    }

    public EditText enableEditText(EditText editText){
        editText.setFocusable(true);
        editText.setEnabled(true);
        return editText;
    }

    public void shakeEditText(Context context,EditText editText){
        Animation shake= AnimationUtils.loadAnimation(context,R.anim.shake);
        editText.startAnimation(shake);
    }

    public void logout(Context context){
        Intent intent=new Intent(context, LockActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    public void blinkTextView(TextView textView){
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        textView.startAnimation(anim);
    }

    public boolean composeMailSend(Context context,String[] to,String subject,String body){
        boolean sent=false;
        GmailSender m = new GmailSender(senderMail, senderPassword);
        m.setFrom(senderMail);
        m.setTo(to);
        m.setSubject(subject);
        m.setBody(body);
        try {
            sent=m.send();
            if (sent) {
                createToast(context, "Mail sent...!");
            } else {
                createToast(context, "Failed to send mail :(");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return sent;
    }

    public void finishTask(Activity activity){
        activity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        activity.finish();
    }



}