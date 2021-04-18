package com.atiqur.rgbled.utils;

public class HelperUtils {

    public static byte[] toBytes(char command, int parameter, int digits) {
        byte[] result = new byte[(digits + 3)];
        result[0] = (byte) command;
        for (int i = digits; i > 0; i--) {
            int square = (int) Math.pow(10.0d, (double) (i - 1));
            result[(digits - i) + 1] = (byte) ((parameter / square) + 48);
            parameter -= (parameter / square) * square;
        }
        result[digits + 1] = 13;
        result[digits + 2] = 10;
        return result;
    }

    public static byte[] toBytesFloat(char command, float parameter, int digits) {
        byte[] result = new byte[(digits + 3)];
        StringBuilder temp = new StringBuilder(String.valueOf(parameter));
        while(temp.length()<=digits){
            temp.append("0");
        }
        //Log.d("Print", temp.toString());
        result[0] = (byte) command;
        for(int i=0;i<digits;i++){
            result[i+1] = (byte)temp.charAt(i);
        }
        result[digits+1] = 13;
        result[digits+2] = 10;
        //Log.d("Print", Arrays.toString(result));
        return result;
    }
    public static int map(int x, int in_min, int in_max, int out_min, int out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
}
