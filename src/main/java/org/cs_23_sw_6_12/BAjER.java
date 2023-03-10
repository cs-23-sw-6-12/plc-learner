package org.cs_23_sw_6_12;

public class BAjER {
    public final static char[] resetCode =  new char[]{1};


    public static char[] setup(int inputSize, int outputSize){
        return new char[]{2, (char) inputSize, (char) outputSize};
    }


    public static char[] step(byte[] input){
        String s = new String(new byte[]{0}) + new String(input);
        return s.toCharArray();
    }
}
