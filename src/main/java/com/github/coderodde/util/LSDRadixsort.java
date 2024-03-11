package com.github.coderodde.util;

import java.util.Arrays;

public final class LSDRadixsort {
    
    public static void sort(int[] array) {
        sort(array, 0, array.length);
    }
    
    public static void sort(int[] array, int fromIndex, int toIndex) {
        checkRangeIndices(array.length,
                          fromIndex,
                          toIndex);
        
        int[] buffer = new int[array.length];
        int[] counterMap = new int[256];
        
        sortImpl(array,
                 buffer, 
                 counterMap,
                 fromIndex,
                 toIndex);
    }
    
    private static void sortImpl(int[] array,
                                 int[] buffer, 
                                 int[] counterMap,
                                 int fromIndex,
                                 int toIndex) {
        for (int byteIndex = 0; byteIndex != 3; byteIndex++) {
            countingSortImpl(array, 
                             buffer, 
                             counterMap, 
                             byteIndex,
                             fromIndex,
                             toIndex);
        }
        
        countingSortImplSigned(array, 
                               buffer, 
                               counterMap,
                               fromIndex,
                               toIndex);
    }
    
    private static void countingSortImpl(int[] array,
                                         int[] buffer,
                                         int[] counterMap,
                                         int byteIndex,
                                         int fromIndex,
                                         int toIndex) {
        Arrays.fill(counterMap, 0);
        
        // Count the elements:
        for (int i = fromIndex; i != toIndex; i++) {
            counterMap[extractCounterIndex(array[i], byteIndex)]++;
        }

        // Make the counter map accummulative:
        for (int i = 1; i != 256; i++) {
            counterMap[i] += counterMap[i - 1];
        }
        
        // Build the output array:
        for (int i = toIndex - 1; i >= fromIndex; i--) {
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
                                               int[] counterMap,
                                               int fromIndex,
                                               int toIndex) {
        Arrays.fill(counterMap, 0);
        
        // Count the elements:
        for (int i = fromIndex; i != toIndex; i++) {
            counterMap[extractCounterIndexSigned(array[i])]++;
        }

        // Make the counter map accummulative:
        for (int i = 1; i != 256; i++) {
            counterMap[i] += counterMap[i - 1];
        }
        
        // Build the output array:
        for (int i = toIndex - 1; i >= fromIndex; i--) {
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
        return (datum >>> (byteIndex * 8)) & 0xff;
    }
    
    private static int extractCounterIndexSigned(int datum) {
        datum >>>= 24;
        return datum > 127 ? (datum & 0b0111_1111) : 
                             (datum | 0b1000_0000);
    }
    
    private static void checkRangeIndices(int arrayLength, 
                                          int fromIndex,
                                          int toIndex) {
        if (fromIndex < 0) {
            throw new IllegalArgumentException(
                    String.format(
                            "fromIndex(%d) is negative. Must be at least 0.", 
                            fromIndex));
        }
        
        if (toIndex > arrayLength) {
            throw new IllegalArgumentException(
                    String.format(
                            "toIndex(%d) is too large. Must be at most %d.",
                            toIndex, 
                            arrayLength));
        }
        
        if (fromIndex > toIndex) {
            throw new IllegalArgumentException(
                    String.format(
                            "toIndex(%d) > fromIndex(%d).", 
                            toIndex,
                            fromIndex));
        }
    }
}
