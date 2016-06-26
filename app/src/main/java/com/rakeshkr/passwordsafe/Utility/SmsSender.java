package com.rakeshkr.passwordsafe.Utility;

import android.content.Context;
import android.telephony.SmsManager;

//Max sms length is 140 chars
public class SmsSender {
    Context myContext;
    String msg,toPhNum;
    public SmsSender(Context context) {
        myContext=context;
    }
    public String getMsg(){
        return msg;
    }

    public void setMsg(String message){
        msg=message.trim();
    }

    public String getToPhNum(){
        return toPhNum;
    }

    public void setToPhNum(String phNum){
        toPhNum=phNum.trim();
    }

    public boolean sendMsg(){
        SmsManager smsManager=SmsManager.getDefault();
        if (msg.length()>0){
            smsManager.sendTextMessage(toPhNum,null,msg,null,null);
            return true;
        }else{
            return false;
        }
    }
}
