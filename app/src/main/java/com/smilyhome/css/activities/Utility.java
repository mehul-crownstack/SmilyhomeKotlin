package com.smilyhome.css.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Build;
import android.text.Html;
import android.util.Base64;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class Utility {

    public static boolean isEmpty(String text) {
        return (text == null || text.isEmpty());
    }

    public static boolean isNotEmpty(String text) {
        return !isEmpty(text);
    }

    public static boolean isEmpty(TextView view) {
        return (view == null || "".equals(view.getText().toString()) || view.getText().toString().isEmpty());
    }

    public static boolean isEmpty(List list) {
        return (list == null || list.isEmpty());
    }

    public static boolean isNotEmpty(List list) {
        return !(isEmpty(list));
    }

    public static String toCamelCase(String str) {
        if (isEmpty(str)) {
            return "";
        }
        final StringBuilder ret = new StringBuilder(str.length());
        for (final String word : str.split(" ")) {
            if (!word.isEmpty()) {
                ret.append(Character.toUpperCase(word.charAt(0)));
                ret.append(word.substring(1).toLowerCase());
            }
            if (!(ret.length() == str.length())) {
                ret.append(" ");
            }
        }
        return ret.toString();
    }

    public static String getAmountInCurrencyFormat(String amountStr) {
        if (isEmpty(amountStr)) {
            return "";
        }
        double amount = Double.parseDouble(amountStr);
        if (amount < 0) {
            amount *= -1;
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String retVal = NumberFormat.getCurrencyInstance(new Locale("en", "in")).format(Double.parseDouble(decimalFormat.format(amount)));
        return retVal.substring(2);
    }

    public static String setCalendarPadding(int pInput) {
        String lValue;
        if (pInput < 10) {
            lValue = "0" + pInput;
        } else {
            lValue = String.valueOf(pInput);
        }
        return lValue;
    }

    public static long generateRandomNumber() {
        StringBuilder sb = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 1; i < 10; i++) {
            int next;
            next = random.nextInt(10);
            if (i == 1) {
                while (next == 0) {
                    next = random.nextInt(10);
                }
            }
            sb.append(next);
        }
        return new BigInteger(sb.toString()).longValue();
    }

    public static void writeHtmlCode(String msg, TextView textView) {
        if (isEmpty(msg)) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(msg, Html.FROM_HTML_MODE_COMPACT));
        } else {
            textView.setText(Html.fromHtml(msg));
        }
    }

    public static String encodeBitmapToBase64(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.WEBP, 100, baos);
            byte[] b = baos.toByteArray();
            return Base64.encodeToString(b, Base64.DEFAULT);
        }
        return "";
    }

    public static Bitmap getImageInBitmap(String imageStr) {
        byte[] b = Base64.decode(imageStr, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    public static void writeStrikeOffText(TextView textView) {
        if (isEmpty(textView)) {
            return;
        }
        textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

}
