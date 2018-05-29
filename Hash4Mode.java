package SkipJack;

import java.util.ArrayList;
import java.util.Arrays;

public class Hash4Mode {
    public static byte[] HashFunction(byte[] text, SkipJack cipher, String h0) {
        byte[] sB = h0.getBytes();
        byte[] sBF = new byte[text.length];
        int j = 0, k = 0;
        while (k < text.length) {
            if (j!=8){
                sBF[k]=sB[j];
                j++;
                k++;
            }else{
                j=0;
                sBF[k] = sB[j];
                k++;
                j++;
            }
        }
        byte[]  h = sBF;
        ArrayList<byte[]> blockedText = toBlocks(text);
        for (int i=0; i<blockedText.size(); i++){
            // используется 4 схема безопасного хэширования
            byte[] miXorH = xor(blockedText.get(i), h);
            byte[] mi = blockedText.get(i);
            h = xor(cipher.encrypt(miXorH), mi);
        }
        return h;
    }

    public static ArrayList<byte[]> toBlocks(byte[] text){
        int zero = 0;
        ArrayList<byte[]> list = new ArrayList();
        byte[] bufferArray = new byte[8];
        int j = 0;
        for (int i = 0; i < text.length; i++){
            if (j!=8){
                bufferArray[j] = text[i];
                j++;
            }else{
                list.add(Arrays.copyOf(bufferArray, 8));
                j=0;
                bufferArray[j] = text[i];
                j++;
            }
        }
        if (j!=8){
            for (int i = j; i<bufferArray.length; i++){
                bufferArray[i] = (byte)zero;
            }
            list.add(Arrays.copyOf(bufferArray, 8));
        } else if(j==8){
            list.add(Arrays.copyOf(bufferArray, 8));
        }
        return list;
    }

    public static byte[] xor(byte[] array1, byte[] array2) {
        byte[] buffer = new byte[8];
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = (byte) ((array1[i] ^ array2[i]));
        }
        return buffer;
    }
}
