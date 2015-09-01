package com.example.okos.tvnow;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class GuiaTiVi {

    private static final String GUIATIVI_API_KEY = "2cc1644953a9534ebafc7825c781dfea";
    private static final String GUIATIVI_SECRET_KEY = "c195056fd5e9dd1a6dd917543f74d64d";
    private static final String url_base = "http://api.guiativi.es/";
    private static final String version_api = "v2";

    public static final String getRequest( String method, String operation )
            throws NoSuchAlgorithmException,
            InvalidKeyException, UnsupportedEncodingException {

        SecretKeySpec keySpec =
                new SecretKeySpec(GUIATIVI_SECRET_KEY.getBytes(), "HmacMD5");
        Mac mac = Mac.getInstance("HmacMD5");
        mac.init(keySpec);

        byte[] hmacBytes =
                mac.doFinal((method + operation).getBytes("UTF-8"));
        StringBuffer buf = new StringBuffer(hmacBytes.length * 2);

        for (byte b: hmacBytes)
            buf.append(Integer.toHexString(b + 0x800).substring(1));

        String hmac = buf.toString();

        return url_base + version_api + "/" + hmac + "/" + GUIATIVI_API_KEY + "/" + operation;
    }



    //borrar
   /*
   public static void main(String[] args) throws UnsupportedEncodingException,
	InvalidKeyException, NoSuchAlgorithmException {

	   String s = getRequest("GET","schedule/neox");
	   System.out.println(s);


   }
   */

}