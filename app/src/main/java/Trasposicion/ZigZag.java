package Trasposicion;

public class ZigZag {

    public String encode(String text, int level){
        String encodedText = "";
        int size = (level*2) - 2;
        int caracteres =  text.length();
        int sizeOla = caracteres/size;

        for(int i = 0; sizeOla > 1; i++)
        {
            caracteres++;
            sizeOla = caracteres/size;
        }

        for(int i = 0; text.length() < caracteres; i++)
        {
            StringBuilder sb = new StringBuilder();
            encodedText = sb.append("|");
        }

        return encodedText;
    }
}
