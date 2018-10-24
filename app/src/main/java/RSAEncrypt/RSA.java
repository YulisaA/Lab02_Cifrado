package RSAEncrypt;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

public class RSA {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    static boolean coprimos(int a, int b) {
        boolean verify = false;
        if(mcd(a,b) == 1) {
            verify = true;
        }
        return verify;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    static int ENumber(int phi) {

        boolean eFound = false;
        int count = 2;
        int eResult = 0;
        while(!eFound && count <phi ) {
            boolean isCoprimo = coprimos(phi, count);
            if(isCoprimo) {
                boolean verify = isPrime(count);
                if(verify && count>10) {
                    eResult = count;
                    eFound = true;
                }
            }
            count++;
        }
        return eResult;
    }

    static int mcd(int a, int b) {
        if(b == 0) {
            return a;
        }else {
            return mcd(b, a%b);
        }


    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static String [] generateKeys(String pValue, String qValue)
    {
        //key[0] = public key
        //key[1] = private key
        String [] keys = new String[2];
        Integer p = Integer.parseInt(pValue);
        Integer q = Integer.parseInt(qValue);
        Integer n = p * q;
        Integer phiInitial = (p-1)*(q-1);
        BigInteger phi = new BigInteger(String.valueOf(phiInitial));

        int e = ENumber(phiInitial);
        BigInteger x = new BigInteger(String.valueOf(e));
        BigInteger d = x.modInverse(new BigInteger(String.valueOf(phi)));

        keys[0] = n + "," + e;
        keys[1] =  n + "," + d;
        return keys;
    }

    private static Boolean isPrime (int number)
    {
        int count = 2;
        boolean isPrimeNumber = true;

        while ((isPrimeNumber) && (count!=number)){
            if (number % count == 0)
                isPrimeNumber = false;
            count++;
        }

        return isPrimeNumber;
    }

    public static int encrypt(int plainText, String publicKey) throws UnsupportedEncodingException {
        int result = 0;
        String [] key = publicKey.split(",");
        BigInteger n = new BigInteger(key[0]);
        BigInteger e = new BigInteger(key[1]);

        BigInteger number = new BigInteger(String.valueOf(plainText));
        result = number.modPow(e, n).intValue();

        return result;
    }

    public static String decrypt(int encryptedText, String privateKey) throws UnsupportedEncodingException {
        int result = 0;
        String [] key = privateKey.split(",");
        BigInteger n = new BigInteger(key[0]);
        BigInteger d = new BigInteger(key[1]);

        BigInteger number = new BigInteger(String.valueOf(encryptedText));
        result = number.modPow(d, n).intValue();

        String resultString = (new String(Character.toChars(result)));
        return resultString;
    }

}
