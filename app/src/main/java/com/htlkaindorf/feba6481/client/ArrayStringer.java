package com.htlkaindorf.feba6481.client;

/**
 * Created by feba6481 on 15.01.16.
 */
public class ArrayStringer {
    public static String getString(int[] array) {
        String out = array[0] + "";
        for (int i = 1; i < array.length; i++) {
            out += "|" + array[i];
        }
        return out;
    }
}
