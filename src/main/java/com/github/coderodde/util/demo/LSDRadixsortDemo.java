package com.github.coderodde.util.demo;

import com.github.coderodde.util.LsdRadixsort;
import java.util.Arrays;
import java.util.Random;

public class LSDRadixsortDemo {
    
    private static final int LENGTH = 100_000_000;
    private static final int PREFIX_SUFFIX_EXCLUSION_RANGE_LENGTH = 0;
    
    public static void main(String[] args) {
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        
        int fromIndex = 
                random.nextInt(PREFIX_SUFFIX_EXCLUSION_RANGE_LENGTH + 1);
        
        int toIndex = LENGTH - random.nextInt(
                        PREFIX_SUFFIX_EXCLUSION_RANGE_LENGTH + 1);
        
        System.out.printf("Seed = %d.\n", seed);
        
        ////// int array:
        
        System.out.println("--- int arrays ---");
        
        long startTime = System.currentTimeMillis();
        int[] intArray1 = createRandomIntegerArray(LENGTH, random);
        int[] intArray2 = intArray1.clone();
        long endTime = System.currentTimeMillis();
        
        System.out.printf("Built demo int arrays in %d milliseconds.\n", 
                          endTime - startTime);
        
        startTime = System.currentTimeMillis();
        LsdRadixsort.sort(intArray1, fromIndex, toIndex);
        endTime = System.currentTimeMillis();
        long durationRadixsort = endTime - startTime;
        
        System.out.printf("LSDRadixsort took %d milliseconds.\n", 
                          durationRadixsort);
        
        startTime = System.currentTimeMillis();
        Arrays.sort(intArray2, fromIndex, toIndex);
        endTime = System.currentTimeMillis();
        long durationArraysSort = endTime - startTime;
        
        System.out.printf("Arrays.sort took %d milliseconds.\n", 
                          durationArraysSort);
        
        System.out.printf("Arrays agree: %b.\n",
                          Arrays.equals(intArray1,
                                        intArray2));
        
        float ratio = (float) durationRadixsort / (float) durationArraysSort;
        
        System.out.println(
                String.format(
                        "Time ratio: %.3f.\n", ratio)
                        .replace(',', '.'));
        
        intArray1 = null;
        intArray2 = null;
        System.gc();
        
        ////// long array:
        
        System.out.println("--- long arrays ---");
        
        startTime = System.currentTimeMillis();
        long[] longArray1 = createRandomLongIntegerArray(LENGTH, random);
        long[] longArray2 = longArray1.clone();
        endTime = System.currentTimeMillis();
        
        System.out.printf("Built demo long arrays in %d milliseconds.\n",
                          endTime - startTime);
        
        startTime = System.currentTimeMillis();
        LsdRadixsort.sort(longArray1, fromIndex, toIndex);
        endTime = System.currentTimeMillis();
        durationRadixsort = endTime - startTime;
        
        System.out.printf("LSDRadixsort took %d milliseconds.\n", 
                          durationRadixsort);
        
        startTime = System.currentTimeMillis();
        Arrays.sort(longArray2, fromIndex, toIndex);
        endTime = System.currentTimeMillis();
        durationArraysSort = endTime - startTime;
        
        System.out.printf("Arrays.sort took %d milliseconds.\n", 
                          durationArraysSort);
        
        System.out.printf("Arrays agree: %b.\n",
                          Arrays.equals(longArray1,
                                        longArray2));
        
        ratio = (float) durationRadixsort / (float) durationArraysSort;
        
        System.out.println(
                String.format(
                        "Time ratio: %.3f.\n", ratio)
                        .replace(',', '.'));
        
        longArray1 = null;
        longArray2 = null;
        System.gc();
    }
    
    private static short[] createRandomShortArray(int length, Random random) {
        short[] array = new short[length];
        
        for (int i = 0; i < length; i++) {
            array[i] = (short) random.nextInt();
        }
        
        return array;
    }
    
    private static int[] createRandomIntegerArray(int length, Random random) {
        int[] array = new int[length];
        
        for (int i = 0; i < length; i++) {
            array[i] = random.nextInt();
        }
        
        return array;
    }
    
    private static long[] createRandomLongIntegerArray(int length, 
                                                       Random random) {
        long[] array = new long[length];
        
        for (int i = 0; i < length; i++) {
            array[i] = random.nextInt();
        }
        
        return array;
    }
}