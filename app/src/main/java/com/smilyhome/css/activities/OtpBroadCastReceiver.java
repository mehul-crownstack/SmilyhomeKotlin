package com.smilyhome.css.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import com.smilyhome.css.activities.interfaces.IAutoReadOtpListener;

public class OtpBroadCastReceiver extends BroadcastReceiver {

    private static IAutoReadOtpListener sOtpListener;
    private static final String TAG = "OtpBroadCastReceiver";

    public void setOtpListener(IAutoReadOtpListener otpListener) {
        sOtpListener = otpListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for (SmsMessage smsMessage : smsMessages) {
            String message = smsMessage.getMessageBody();
            Log.d(TAG, "onReceive: ".concat(message));
            if (Utility.isNotEmpty(message) && message.toLowerCase().contains("smilyhome")) {
                if (message.toLowerCase().startsWith("your smilyhome login otp")) {
                    if (sOtpListener != null) {
                        String[] otpString = message.split(":");
                        if (otpString.length > 1) {
                            sOtpListener.onOtpReceived(otpString[1].trim());
                        }
                    }
                }
            }
        }
    }
}
