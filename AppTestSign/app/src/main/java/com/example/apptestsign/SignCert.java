package com.example.apptestsign;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignCert {

    private static String TAG = "SignCert";

    public static boolean checkAppSignature(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);

            Signature signature = packageInfo.signatures[0];

            MessageDigest mDigest = MessageDigest.getInstance("SHA");

            mDigest.update(signature.toByteArray());

            final String curSign = Base64.encodeToString(mDigest.digest(),
                    Base64.DEFAULT).trim();

            Log.d(TAG, "checkAppSignature: " +  curSign);

        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
