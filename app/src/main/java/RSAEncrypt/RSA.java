package RSAEncrypt;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

public class RSA {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    static boolean coprimes(int number1, int number2) {
        boolean verify = false;
        //Verify if numbers are coprimes
        if(mcd(number1,number2) == 1) {
            verify = true;
        }
        return verify;
    }

    //Verify if a number is prime
    private static Boolean isPrime (int number)
    {
        int count = 2;
        boolean isPrimeNumber = true;

        //If number can be divided by a number different than itself and 1, then return false
        while ((isPrimeNumber) && (count!=number)){
            if (number % count == 0)
                isPrimeNumber = false;
            count++;
        }
        return isPrimeNumber;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    static int ENumber(int phi) {
        boolean eFound = false;
        int count = 2;
        int eResult = 0;
        while(count < phi && !eFound) {
            //Obtain e number that is coprimo with phi and count
            boolean isCoprimo = coprimes(phi, count);
            if(isCoprimo) {
                boolean verify = isPrime(count);
                if(verify && count > 10) {
                    eResult = count;
                    eFound = true;
                }
            }
            count++;
        }
        return eResult;
    }

    static int mcd(int phi, int count) {
        if(count == 0) {
            return phi;
        }else {
            //a mod b until MCD is or not 1
            return mcd(count, phi%count);
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

        //Obtain number e
        int e = ENumber(phiInitial);
        BigInteger eToBigInteger = new BigInteger(String.valueOf(e));
        //Obtain d by doing a mod inverse with e and phi
        BigInteger d = eToBigInteger.modInverse(new BigInteger(String.valueOf(phi)));

        //public key
        keys[0] = n + "," + e;
        //private key
        keys[1] =  n + "," + d;
        return keys;
    }

    public static int encrypt(int plainText, String publicKey) throws UnsupportedEncodingException {
        int result = 0;
        //Obtain public key
        String [] key = publicKey.split(",");
        BigInteger n = new BigInteger(key[0]);
        BigInteger e = new BigInteger(key[1]);

        //Obtain plainText (ascii)
        BigInteger number = new BigInteger(String.valueOf(plainText));
        //The result is PLAINTEXTpow(e) mod n
        result = number.modPow(e, n).intValue();
        return result;
    }

    public static String decrypt(int encryptedText, String privateKey) throws UnsupportedEncodingException {
        int result = 0;
        //Obtain private key
        String [] key = privateKey.split(",");
        BigInteger n = new BigInteger(key[0]);
        BigInteger d = new BigInteger(key[1]);

        //Obtain encryptedText
        BigInteger number = new BigInteger(String.valueOf(encryptedText));
        //The result is CYPHERTEXTpow(d) mod n
        //Result will contain the caracter in ASCII
        result = number.modPow(d, n).intValue();
        //Obtain value of the ascii number
        String resultString = (new String(Character.toChars(result)));
        return resultString;
    }

}
