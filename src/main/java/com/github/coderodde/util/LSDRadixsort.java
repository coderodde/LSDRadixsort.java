package com.github.coderodde.util;

import java.util.Arrays;

public final class LSDRadixsort {
    
    private static final int NUMBER_OF_COUNTERS = 256;
    
    public static void sort(int[] array) {
        sort(array, 0, array.length);
    }
    
    public static void sort(int[] array, int fromIndex, int toIndex) {
        checkRangeIndices(array.length,
                          fromIndex,
                          toIndex);
        
        int rangeLength = toIndex - fromIndex;
        
        if (rangeLength < 2) {
            // Trivially sorted:
            return;
        }
        
        int[] buffer = new int[rangeLength];
        int[] counterMap = new int[NUMBER_OF_COUNTERS];
        
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
        for (int i = 1; i != NUMBER_OF_COUNTERS; i++) {
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
                         fromIndex, 
                         buffer.length);
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
        for (int i = 1; i != NUMBER_OF_COUNTERS; i++) {
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
                         fromIndex, 
                         buffer.length);
    }
    
    private static int extractCounterIndex(int datum, int byteIndex) {
        return (datum >>> (byteIndex * 8)) & 0xff;
    }
    
    private static int extractCounterIndexSigned(int datum) {
        // We use xor ^ operator in order to flip the bit index 7 (8th bit from
        // the least significant end):
        return (datum >>> 24) ^ 0b1000_0000;
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
