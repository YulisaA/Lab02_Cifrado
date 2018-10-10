package Trasposicion;

public class ZigZag {

    //Encrypt message with certain level
    public static String encrypt(String text, int level){
        StringBuilder encodedText = new StringBuilder();
        encodedText = encodedText.append(text);
        StringBuilder sb = new StringBuilder();

        if(level == 1 || level == 0)
        {
            return text;
        }
        int sizeWave = (level*2) - 2;
        double caracteres =  text.length();
        double numberWaves = caracteres/sizeWave;

        //Add Split if waves are not completed yet
        while(verifyInt(numberWaves) != 1)
        {
            caracteres++;
            numberWaves = caracteres/sizeWave;
            encodedText = encodedText.append("|");
        }
        //Level 1 will return the original message
        if (level == 1)
        {
            return encodedText.toString();
        }

        for (int i = 1; i <= level; i++) {
            //top and base rows
            if (i == 1 || i == level) {
                //j will have the position of the top and the base rows
                for (int j = i-1; j < encodedText.toString().length(); j = j + sizeWave) {
                    sb.append(encodedText.toString().charAt(j));
                }
                //center rows
            } else {
                //actualPosition will have the position of the character to add
                int actualPosition = i - 1;
                //Flag that indicates the start of a wave
                boolean isSameWave = false;
                //initial character of actual wave
                int initialCharacter = 2 * (level-i);
                //End character of actual wave
                int endCharacter = sizeWave - initialCharacter;

                while (actualPosition < encodedText.toString().length()) {
                    //Concatenate actual character
                    sb.append(encodedText.toString().charAt(actualPosition));
                    if (!isSameWave)
                    {
                        //Obtain position of the actual row
                        actualPosition = actualPosition + initialCharacter;
                        isSameWave = true;
                    }
                    else{
                        //Obtain position of the actual row
                        actualPosition = actualPosition + endCharacter;
                        isSameWave = false;
                    }
                }
            }
        }
        return sb.toString();
    }

    //Verify if #waves is Integer
    public static int verifyInt(double number) {
        if (number % 1 == 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
