package com.example.apptestsign;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class SignCert {

    private static String TAG = "SignCert";

    public static boolean checkAppSignature(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);

            // 签名信息
            Signature signature = packageInfo.signatures[0];

            MessageDigest mDigest = MessageDigest.getInstance("SHA");

            mDigest.update(signature.toByteArray());

            //
//            final String curSign = Base64.encodeToString(mDigest.digest(),
//                    Base64.DEFAULT).trim();
//            Log.d(TAG, "checkAppSignature: Base64 signature " + curSign);

            // 读取证书
            byte[] cert = signature.toByteArray();
            InputStream input = new ByteArrayInputStream(cert);
            CertificateFactory cf = null;
            try {
                cf = CertificateFactory.getInstance("X509");
            } catch (CertificateException e) {
                e.printStackTrace();
            }
            X509Certificate c = null;
            try {
                c = (X509Certificate) cf.generateCertificate(input);
            } catch (CertificateException e) {
                e.printStackTrace();
            }

            // 计算证书SHA1
            String hexString = null;
            try {
                MessageDigest md = MessageDigest.getInstance("SHA1");
                byte[] publicKey = md.digest(c.getEncoded());
                hexString = byte2HexFormatted(publicKey);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (CertificateEncodingException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "checkAppSignature: hex " + hexString);
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // 字节转16进制
    private static String byte2HexFormatted(byte[] arr) {
        StringBuilder str = new StringBuilder(arr.length * 2);
        for (int i = 0; i < arr.length; i++) {
            String h = Integer.toHexString(arr[i]);
            int l = h.length();
            if (l == 1) {
                h = "0" + h;
            }
            if (l > 2) {
                h = h.substring(l - 2, l);
            }
            str.append(h.toUpperCase());
            if (i < (arr.length - 1)) {
                str.append(':');
            }
        }
        return str.toString();
    }
}
