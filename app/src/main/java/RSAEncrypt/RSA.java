package RSAEncrypt;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;

public class RSA {

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

        int randomInteger = getRandomNumber(1, phiInitial);
        while(!isPrime(randomInteger))
        {
            randomInteger = getRandomNumber(1, phiInitial);
        }

        BigInteger e = new BigInteger(String.valueOf(randomInteger));
        BigInteger d = e.modInverse(phi);

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static int getRandomNumber(int min, int max){
        return ThreadLocalRandom.current().nextInt(min, max);
    }
}
