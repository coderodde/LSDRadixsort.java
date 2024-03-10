package com.github.coderodde.util;

import java.util.Arrays;

public final class LSDRadixsort {
    
    public static void sort(int[] array) {
        int[] buffer = new int[array.length];
        int[] counterMap = new int[256];
        sortImpl(array, buffer, counterMap);
    }
    
    private static void sortImpl(int[] array, int[] buffer, int[] counterMap) {
        for (int byteIndex = 3; byteIndex != 0; byteIndex--) {
            countingSortImpl(array, 
                             buffer, 
                             counterMap, 
                             byteIndex);
        }
        
        countingSortImplSigned(array, 
                               buffer, 
                               counterMap);
    }
    
    private static void countingSortImpl(int[] array,
                                         int[] buffer,
                                         int[] counterMap,
                                         int byteIndex) {
        Arrays.fill(counterMap, 0);
        
        // Count the elements:
        for (int i = 0, sz = array.length; i != sz; i++) {
            counterMap[extractCounterIndex(array[i], byteIndex)]++;
        }

        // Make the counter map accummulative:
        for (int i = 1, sz = array.length; i != sz; i++) {
            counterMap[i] += counterMap[i - 1];
        }
        
        // Build the output array:
        for (int i = array.length - 1; i != -1; i--) {
            int index = extractCounterIndex(array[i], byteIndex);
            buffer[counterMap[index]-- - 1] = array[i];
        }
        
        // Just copy the buffer to the array:
        System.arraycopy(buffer, 
                         0, 
                         array,
                         0, 
                         array.length);
    }
    
    private static void countingSortImplSigned(int[] array,
                                               int[] buffer,
                                               int[] counterMap) {
        Arrays.fill(counterMap, 0);
        
        // Count the elements:
        for (int i = 0, sz = array.length; i != sz; i++) {
            counterMap[extractCounterIndexSigned(array[i])]++;
        }

        // Make the counter map accummulative:
        for (int i = 1, sz = array.length; i != sz; i++) {
            counterMap[i] += counterMap[i - 1];
        }
        
        // Build the output array:
        for (int i = array.length - 1; i != -1; i--) {
            int index = extractCounterIndexSigned(array[i]);
            buffer[counterMap[index]-- - 1] = array[i];
        }
        
        // Just copy the buffer to the array:
        System.arraycopy(buffer, 
                         0, 
                         array,
                         0, 
                         array.length);
    }
    
    private static int extractCounterIndex(int datum, int byteIndex) {
        int ret = (datum >>> (byteIndex * 8)) & 0xff;
        return ret;
    }
    
    private static int extractCounterIndexSigned(int datum) {
        datum >>>= 24;
        int ret = datum > 127 ? (datum & 0b0111_1111) : (datum | 0b1000_0000);
        return ret;
    }
}
