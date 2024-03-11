package com.github.coderodde.util;

import java.util.Arrays;

/**
 * This class provides the method for sorting {@code int} arrays using 
 * least-significant digit (LSD) radix sort.
 */
public final class LSDRadixsort {
    
    /**
     * The number of counters in the counter array.
     */
    private static final int NUMBER_OF_COUNTERS = 256;
    
    /**
     * Sorts the entire {@code int} array into ascending order.
     * 
     * @param array the array to sort. 
     */
    public static void sort(int[] array) {
        sort(array, 0, array.length);
    }
    
    /**
     * Sorts the range {@code array[fromIndex ... toIndex - 1]} into ascending
     * order.
     * 
     * @param array     the array holding the range.
     * @param fromIndex the starting, inclusive index of the sorting range.
     * @param toIndex   the ending, exclusive index of the sorting range. 
     */
    public static void sort(int[] array, int fromIndex, int toIndex) {
        checkRangeIndices(array.length,
                          fromIndex,
                          toIndex);
        
        int rangeLength = toIndex - fromIndex;
        
        if (rangeLength < 2) {
            // Trivially sorted:
            return;
        }
        
        // buffer and counterMap are allocated only once for the sake of 
        // performance:
        int[] buffer = new int[rangeLength];
        int[] counterMap = new int[NUMBER_OF_COUNTERS];
        
        // Spawn sorting:
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
        // Sort first by least-significant bytes, then by second 
        // least-significant, and finally by third least-signficant byte:
        for (int byteIndex = 0; byteIndex != 3; byteIndex++) {
            countingSortImpl(array, 
                             buffer, 
                             counterMap, 
                             byteIndex,
                             fromIndex,
                             toIndex);
        }
        
        // Deal with the signed data:
        countingSortImplSigned(array, 
                               buffer, 
                               counterMap,
                               fromIndex,
                               toIndex);
    }
    
    /**
     * Performs the counting sort on {@code array[fromIndex ... toIndex - 1]}.
     * 
     * @param array      the array to sort.
     * @param buffer     the buffer array.
     * @param counterMap the counter array. We reuse this in order not to
     *                   allocate it everytime we call this method.
     * @param byteIndex  the index of the byte that serves as the sorting key.
     * @param fromIndex  the starting, inclusive index of the sorting range.
     * @param toIndex    the ending, exclusive index of the sorting range.
     */
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
        
        // Build the buffer array (which will end up sorted):
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
    
    /**
     * Sorts the {@code array[fromIndex ... toIndex - 1]} by most significant 
     * bytes that contain the sign bits.
     * 
     * @param array      the array to sort.
     * @param buffer     the buffer array.
     * @param counterMap the counter map. We pass this array in order not to 
     *                   create it in this method.
     * @param fromIndex  the starting, inclusive index of the sorting range.
     * @param toIndex    the ending, exclusive index of the sorting range.
     */
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
    
    /**
     * Extracts the counter array index from the integer datum.
     * 
     * @param datum     the integer key.
     * @param byteIndex the index of the byte of the key to consider.
     * @return          the index into counter array.
     */
    private static int extractCounterIndex(int datum, int byteIndex) {
        // Shift so that the target byte is the leftmost byte and set to zero
        // all the remaining bits:
        return (datum >>> (byteIndex * 8)) & 0xff;
    }
    
    /**
     * Extracts the counter array index from the integer datum. Considers only
     * the most significant byte that contains the sign bit. The sign bit is 
     * flipped in order to put the datum in correct location in the counter 
     * array.
     * 
     * @param datum the integer key.
     * @return      the index into counter array. 
     */
    private static int extractCounterIndexSigned(int datum) {
        // We use xor ^ operator in order to flip the bit index 7 (8th bit from
        // the least significant end):
        return (datum >>> 24) ^ 0b1000_0000;
    }
    
    /**
     * Checks that the specified sorting range is reasonable.
     * 
     * @param arrayLength the total length of the target array.
     * @param fromIndex   the starting, inclusive index of the sorting range.
     * @param toIndex     the ending, exclusive index of the sorting range.
     */
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
