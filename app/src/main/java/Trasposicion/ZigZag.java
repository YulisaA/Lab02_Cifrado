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

    //Decrypt message with certain level
    public static String decrypt(String text, int level){
        int sizeWave = (level * 2) - 2;
        //In case level is high and is not possible create even 1 wave
        int numberOfWavesTest = text.length()/sizeWave;
        StringBuilder encodedText = new StringBuilder();
        encodedText = encodedText.append(text);

        if(level == 1 || level == 0)
        {
            return text;
        }
        //Add character '|' just in case the level is too high
        while(numberOfWavesTest == 0)
        {
            encodedText = encodedText.append('|');
            numberOfWavesTest = encodedText.length()/sizeWave;
        }
        int numberOfWaves = encodedText.length()/sizeWave;
        int blockSize = 2*numberOfWaves;
        StringBuilder result = new StringBuilder();
        String textForBlocks = "";

        //Array that contains the top and bases of the waves
        String [] topAndBases = new String[2];

        topAndBases[0] = encodedText.substring(0, numberOfWaves);
        topAndBases[1] = encodedText.substring(encodedText.length() - numberOfWaves, encodedText.length());

        //If level is 2, it will not be blocks, just tops and bases
        if(level == 2)
        {
            for(int i = 0; i < numberOfWaves; i++)
            {
                result.append(topAndBases[0].charAt(i));
                result.append(topAndBases[1].charAt(i));

            }
            return result.toString();
        }

        StringBuilder sb = new StringBuilder(encodedText);
        sb.setLength(encodedText.length() - numberOfWaves);
        //Obtains the text for blocks because the topsAndWaves are already on an array.
        textForBlocks = sb.toString().replaceFirst(topAndBases[0], "");
        //This array will contain the blocks
        String [] blocks = new String[textForBlocks.length()/blockSize];
        int actual = 0;
        for(int i = 0; i <= textForBlocks.length() - blockSize; i = i + blockSize)
        {
            //Fill array with blocks
            blocks[actual] = textForBlocks.substring(i, i + blockSize);
            actual++;
        }
        int position = 0;
        //This cicle will repeat until numberOfWaves is finished
        for(int i = 0; i < numberOfWaves; i++)
        {
            //Concatenate the actual character of the TOP
            result.append(topAndBases[0].charAt(i));

            for(int j = 0; j < blocks.length; j++)
            {
                //Concatenate the actual character of EVERY BLOCK
                result.append(blocks[j].charAt(position));
            }
            position++;
            //Concatenate the actual character of the BASE
            result.append(topAndBases[1].charAt(i));

            for(int j = blocks.length - 1; j >= 0; j--)
            {
                //Concatenate the actual character of EVERY BLOCK, but now UPSIDE DOWN (REVERSE)
                result.append(blocks[j].charAt(position));
            }
            position++;
        }

        return result.toString();
    }
}
