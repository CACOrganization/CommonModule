package com.caccommonmodule.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.provider.Settings.Secure;

public class CreateUdidUtil {

    private static final String ENCODE = "puorg104";

    public static String getUdId(Context context) {

//        String udid = "";
//        
//        AccountManager accountManager = AccountManager.get(context);
//        
//        Account[] accounts = accountManager.getAccountsByType("com.google");
//        
//        if(accounts != null && accounts.length > 0){
//            
//            udid = MD5.hash(accounts[0] + ENCODE);
//        }else{
//            udid = MD5.hash("test104test@gmail.com" + ENCODE);
//        }
//        
//        return udid;
        
        String udid = "";

        AccountManager accountManager = AccountManager.get(context);

        Account[] accounts = accountManager.getAccountsByType("com.google");

        if (android.os.Build.VERSION.SDK_INT >= 9) {
            udid = android.os.Build.SERIAL;
        } else {
            udid = Secure.getString(context.getContentResolver(),
                    Secure.ANDROID_ID);
        }

        if(udid == null)
            udid = "";
        if (accounts != null && accounts.length > 0 && udid.length() < 0) {
            udid = MD5.hash(accounts[0] + udid + ENCODE);
        } else {
            udid = MD5.hash("test104@gmail.com_" + udid + ENCODE);
        }

        return udid;
    }

    public static boolean checkUdId(Context context, String deviceId) {
        boolean bAuthSuccess = false;

        AccountManager accountManager = AccountManager.get(context);

        Account[] accounts = accountManager.getAccountsByType("com.google");

        if (accounts != null && accounts.length > 0) {
            String checkUdid = MD5.hash(accounts[0] + ENCODE);

            if (checkUdid.equals(deviceId)) {
                bAuthSuccess = true;
            }
        } else {
            bAuthSuccess = true;
        }

        return bAuthSuccess;
    }
}
