package SDESAlgorithm;

public class SDES {

    static String [] [] SOMatrix = new String [4] [4];
    static String [] [] S1Matrix = new String [4] [4];

    static byte[] Key1 = new byte[8];
    static byte[] Key2 = new byte[8];

    //SBoxes
    public static void fillSO()
    {
        SOMatrix [0][0] = "01";
        SOMatrix [0][1] = "00";
        SOMatrix [0][2] = "11";
        SOMatrix [0][3] = "10";

        SOMatrix [1][0] = "11";
        SOMatrix [1][1] = "10";
        SOMatrix [1][2] = "01";
        SOMatrix [1][3] = "00";

        SOMatrix [2][0] = "00";
        SOMatrix [2][1] = "10";
        SOMatrix [2][2] = "01";
        SOMatrix [2][3] = "11";

        SOMatrix [3][0] = "11";
        SOMatrix [3][1] = "01";
        SOMatrix [3][2] = "11";
        SOMatrix [3][3] = "10";
    }

    public static void fillS1()
    {
        S1Matrix [0][0] = "00";
        S1Matrix [0][1] = "01";
        S1Matrix [0][2] = "10";
        S1Matrix [0][3] = "11";

        S1Matrix [1][0] = "10";
        S1Matrix [1][1] = "00";
        S1Matrix [1][2] = "01";
        S1Matrix [1][3] = "11";

        S1Matrix [2][0] = "11";
        S1Matrix [2][1] = "00";
        S1Matrix [2][2] = "01";
        S1Matrix [2][3] = "00";

        S1Matrix [3][0] = "10";
        S1Matrix [3][1] = "01";
        S1Matrix [3][2] = "00";
        S1Matrix [3][3] = "11";
    }

    public static void generateKeys(String key) {
        byte [] Key;
        Key = P10(key.getBytes());
        byte[] P10ResultPart1 = new byte[5];
        byte[] P10ResultPart2 = new byte[5];

        for(int i = 0; i < P10ResultPart1.length; i++)
        {
            P10ResultPart1[i] = Key[i];
            P10ResultPart2[i] = Key[5 + i];
        }

        //LS1 5 bytes each one
        byte[] LS1ResultPart1;
        byte[] LS1ResultPart2;

        LS1ResultPart1 = LS1(P10ResultPart1);
        LS1ResultPart2 = LS1(P10ResultPart2);

        byte [] resultLS1 = new byte[10];
        for(int i = 0; i < (resultLS1.length)/2; i++)
        {
            resultLS1[i] = LS1ResultPart1[i];
            resultLS1[5+i] = LS1ResultPart2[i];
        }
        //Obtain Key1
        Key1 = P8(resultLS1);

        byte [] LS2ResultPart1;
        byte [] LS2ResultPart2;

        LS2ResultPart1 = LS2(LS1ResultPart1);
        LS2ResultPart2 = LS2(LS1ResultPart2);

        byte [] resultLS2 = new byte[10];
        for(int i = 0; i < (resultLS2.length)/2; i++)
        {
            resultLS2[i] = LS2ResultPart1[i];
            resultLS2[5+i] = LS2ResultPart2[i];
        }
        //Obtain Key2
        Key2 = P8(resultLS2);
    }

    public static byte [] P10(byte [] key)
    {
        byte [] P10result = new byte[10];
        P10result[0] = key[2];
        P10result[1] = key[4];
        P10result[2] = key[0];
        P10result[3] = key[7];
        P10result[4] = key[6];
        P10result[5] = key[1];
        P10result[6] = key[8];
        P10result[7] = key[5];
        P10result[8] = key[9];
        P10result[9] = key[3];

        return P10result;
    }

    public static byte [] P8(byte [] key)
    {
        byte [] P8result = new byte[8];
        P8result[0] = key[3];
        P8result[1] = key[5];
        P8result[2] = key[1];
        P8result[3] = key[6];
        P8result[4] = key[0];
        P8result[5] = key[4];
        P8result[6] = key[9];
        P8result[7] = key[7];

        return P8result;
    }

    public static byte [] P4(byte [] key)
    {
        byte [] P4result = new byte[4];
        P4result[0] = key[3];
        P4result[1] = key[1];
        P4result[2] = key[0];
        P4result[3] = key[2];

        return P4result;
    }

    public static byte [] EP(byte [] key)
    {
        byte [] EPresult = new byte[8];
        EPresult[0] = key[3];
        EPresult[1] = key[2];
        EPresult[2] = key[0];
        EPresult[3] = key[1];
        EPresult[4] = key[2];
        EPresult[5] = key[1];
        EPresult[6] = key[3];
        EPresult[7] = key[0];

        return EPresult;
    }

    public static byte [] IP(byte [] key)
    {
        byte [] P8result = new byte[8];
        P8result[0] = key[2];
        P8result[1] = key[6];
        P8result[2] = key[1];
        P8result[3] = key[5];
        P8result[4] = key[3];
        P8result[5] = key[0];
        P8result[6] = key[7];
        P8result[7] = key[4];

        return P8result;
    }

    public static byte [] IPInverse(byte [] array1, byte [] array2)
    {
        byte[] array = new byte[8];
        for(int i = 0; i< (array.length)/2; i++)
        {
            array[i] = array1[i];
            array[4+i] = array2[i];
        }
        byte [] IP1result = new byte[8];
        IP1result[0] = array[5];
        IP1result[1] = array[2];
        IP1result[2] = array[0];
        IP1result[3] = array[4];
        IP1result[4] = array[7];
        IP1result[5] = array[3];
        IP1result[6] = array[1];
        IP1result[7] = array[6];

        return IP1result;
    }

    public static byte [] LS1(byte [] key)
    {
        byte [] LSresult = new byte[5];
        LSresult[0] = key[4];
        LSresult[1] = key[0];
        LSresult[2] = key[1];
        LSresult[3] = key[2];
        LSresult[4] = key[3];

        return LSresult;
    }

    public static byte [] LS2(byte [] key)
    {
        byte [] LSresult = new byte[5];
        LSresult[0] = key[3];
        LSresult[1] = key[4];
        LSresult[2] = key[0];
        LSresult[3] = key[1];
        LSresult[4] = key[2];

        return LSresult;
    }

    public static byte[] XOR(byte[] array1, byte[] array2)
    {
        byte[] XORresult = new byte[array1.length];
        byte[] result;

        for(int i = 0; i < XORresult.length; i++){
            if(array1[i] == array2[i])
            {
                result = "0".getBytes();
                XORresult[i] = result[0];
            }
            else{
                result = "1".getBytes();
                XORresult[i] = result[0];
            }
        }

        return XORresult;
    }

    public static String FK_Function(byte[] IPResult, byte[] Key1, byte[] Key2)
    {
        byte[] IPResultPart1 = new byte[4];
        byte[] IPResultPart2 = new byte[4];

        //Initial permutation
        for(int j = 0; j < IPResultPart1.length; j++)
        {
            IPResultPart1[j] = IPResult[j];
            IPResultPart2[j] = IPResult[4+j];
        }

        //Expand and permute
        byte[] EPresult;
        EPresult = EP(IPResultPart2);

        //XOR: expand and permute and Key1
        byte[] XORresult;
        XORresult = XOR(EPresult, Key1);

        byte[] XORResultPart1 = new byte[4];
        byte[] XORResultPart2 = new byte[4];
        for(int j = 0; j < XORResultPart1.length; j++)
        {
            XORResultPart1[j] = XORresult[j];
            XORResultPart2[j] = XORresult[4+j];
        }
        //Obtain position to use SBOXES
        byte[] bit1 = new byte[1];
        byte[] bit2 = new byte[1];
        byte[] bit3 = new byte[1];
        byte[] bit4 = new byte[1];
        bit1[0] = XORResultPart1[0];
        bit2[0] = XORResultPart1[1];
        bit3[0] = XORResultPart1[2];
        bit4[0] = XORResultPart1[3];
        //Obtain row and column
        String binaryrow = new String(bit1,0) + new String(bit4, 0);
        String binarycolumn = new String(bit2,0) + new String(bit3, 0);

        //Convert binary column to decimal to use the boxes
        int row = Integer.parseInt(binaryrow,2);
        int column = Integer.parseInt(binarycolumn,2);
        //Obtain SO Matrix value
        String S0Result = SOMatrix[row][column];

        bit1[0] = XORResultPart2[0];
        bit2[0] = XORResultPart2[1];
        bit3[0] = XORResultPart2[2];
        bit4[0] = XORResultPart2[3];
        binaryrow = new String(bit1,0) + new String(bit4, 0);
        binarycolumn = new String(bit2,0) + new String(bit3, 0);

        row = Integer.parseInt(binaryrow,2);
        column = Integer.parseInt(binarycolumn,2);
        //Obtain S1Matrix value
        String S1Result = S1Matrix[row][column];

        byte[] SBResult;
        SBResult = (S0Result + S1Result).getBytes();

        byte[] P4Result;
        P4Result = P4(SBResult);

        byte[] XORResult;
        XORResult = XOR(IPResultPart1, P4Result);

        //S W I T C H, switch values for next operations
        byte[] EPResult;
        EPResult = EP(XORResult);

        byte [] XORresultKey2;
        XORresultKey2 = XOR(EPResult, Key2);

        byte[] XORPart1 = new byte[4];
        byte[] XORPart2 = new byte[4];
        for(int j = 0; j < XORPart1.length; j++)
        {
            XORPart1[j] = XORresultKey2[j];
            XORPart2[j] = XORresultKey2[4+j];
        }

        bit1[0] = XORPart1[0];
        bit2[0] = XORPart1[1];
        bit3[0] = XORPart1[2];
        bit4[0] = XORPart1[3];
        binaryrow = new String(bit1,0) + new String(bit4, 0);
        binarycolumn = new String(bit2,0) + new String(bit3, 0);

        row = Integer.parseInt(binaryrow,2);
        column = Integer.parseInt(binarycolumn,2);
        //Obtain SOMatrix value
        String S0Result2 = SOMatrix[row][column];

        bit1[0] = XORPart2[0];
        bit2[0] = XORPart2[1];
        bit3[0] = XORPart2[2];
        bit4[0] = XORPart2[3];
        binaryrow = new String(bit1,0) + new String(bit4, 0);
        binarycolumn = new String(bit2,0) + new String(bit3, 0);

        row = Integer.parseInt(binaryrow,2);
        column = Integer.parseInt(binarycolumn,2);
        //Obtain S1Matrix value
        String S1Result2 = S1Matrix[row][column];

        byte[] SBResult2 = new byte[4];
        SBResult2 = (S0Result2 + S1Result2).getBytes();

        byte[] P4result = new byte[4];
        P4result = P4(SBResult2);

        byte[] lastXOR = new byte[4];
        lastXOR = XOR(IPResultPart2, P4result);

        return new String(lastXOR, 0) + "-" + new String(XORResult, 0);
    }

    public static String encrypt(int c) {
        fillSO();
        fillS1();

        byte[] actualCharacter;
        String binary = "";
        byte[] IPinverse;
        String result = "";

            binary = Integer.toBinaryString(c);
            while(binary.length() < 8)
            {
                binary = "0" + binary;
            }
            //Get bytes to init
            actualCharacter = binary.getBytes();

            byte[] IPResult;
            IPResult = IP(actualCharacter);

            //Obtain lastXOR and XORResult
            String[] fk = FK_Function(IPResult, Key1, Key2).split("-");
            char[] functionPart1 = fk[0].toCharArray();
            char[] functionPart2 = fk[1].toCharArray();
            byte[] lastXOR = new byte[4];
            byte[] XORResult = new byte[4];
            byte[] position1;
            byte[] position2;
            String actualfk1 = "";
            String actualfk2 = "";

            for(int j = 0; j < lastXOR.length; j++)
            {
                actualfk1 = functionPart1[j] + "";
                actualfk2 = functionPart2[j] + "";
                position1 = actualfk1.getBytes();
                position2 = actualfk2.getBytes();
                lastXOR[j] = position1[0];
                XORResult[j] = position2[0];
            }

            //Inverse Permutation
            IPinverse = IPInverse(lastXOR, XORResult);

            //Obtain ASCII of the actual binary result
            int ascii = Integer.parseInt(new String(IPinverse, 0),2);

            //Obtain character of the ASCII
            result = (new String(Character.toChars(ascii)));

        return result;
    }

    public static String decrypt(int c) {
        fillSO();
        fillS1();

        byte[] actualCharacter;
        String binary = "";
        byte[] IPinverse;
        String result = "";
        //Obtain binary
        binary = Integer.toBinaryString(c);
        while(binary.length() < 8)
        {
            binary = "0" + binary;
        }
            //GetBytes for the actualCharacter
            actualCharacter = binary.getBytes();
            byte[] IPResult = new byte[8];
            IPResult = IP(actualCharacter);

            //Now Key2 is used first
            String[] fk = FK_Function(IPResult, Key2, Key1).split("-");
            char[] functionPart1 = fk[0].toCharArray();
            char[] functionPart2 = fk[1].toCharArray();
            byte[] lastXOR = new byte[4];
            byte[] XORResult = new byte[4];
            byte[] position1 = new byte[1];
            byte[] position2 = new byte[1];
            String actualfk1 = "";
            String actualfk2 = "";

            for(int j = 0; j < lastXOR.length; j++)
            {
                actualfk1 = functionPart1[j] + "";
                actualfk2 = functionPart2[j] + "";
                position1 = actualfk1.getBytes();
                position2 = actualfk2.getBytes();
                lastXOR[j] = position1[0];
                XORResult[j] = position2[0];
            }

            IPinverse = IPInverse(lastXOR, XORResult);
            //Obtain ascii of the actual value
            int ascii = Integer.parseInt(new String(IPinverse, 0),2);

            //Obtain character of the ASCII
            result = (new String(Character.toChars(ascii)));

        return result;
    }

}
