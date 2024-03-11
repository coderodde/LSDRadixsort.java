package com.github.coderodde.util.demo;

import com.github.coderodde.util.LSDRadixsort;
import java.util.Arrays;
import java.util.Random;

public class LSDRadixsortDemo {
    
    private static final int LENGTH = 100_000_000;
    private static final int PREFIX_SUFFIX_EXCLUSION_RANGE_LENGTH = 0;
    
    public static void main(String[] args) {
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        
        System.out.printf("Seed = %d.\n", seed);
        
        long startTime = System.currentTimeMillis();
        int[] array1 = createRandomIntegerArray(LENGTH, random);
        int[] array2 = array1.clone();
        long endTime = System.currentTimeMillis();
        
        int fromIndex = 
                random.nextInt(PREFIX_SUFFIX_EXCLUSION_RANGE_LENGTH + 1);
        
        int toIndex = LENGTH - random.nextInt(
                        PREFIX_SUFFIX_EXCLUSION_RANGE_LENGTH + 1);
        
        System.out.printf("Built demo arrays in %d milliseconds.\n",
                          endTime - startTime);
        
        startTime = System.currentTimeMillis();
        LSDRadixsort.sort(array1, fromIndex, toIndex);
        endTime = System.currentTimeMillis();
        long durationRadixsort = endTime - startTime;
        
        System.out.printf("LSDRaidxsort took %d milliseconds.\n", 
                          durationRadixsort);
        
        startTime = System.currentTimeMillis();
        Arrays.sort(array2, fromIndex, toIndex);
        endTime = System.currentTimeMillis();
        long durationArraysSort = endTime - startTime;
        
        System.out.printf("Arrays.sort took %d milliseconds.\n", 
                          durationArraysSort);
        
        System.out.printf("Arrays agree: %b.\n",
                          Arrays.equals(array1,
                                        array2));
        
        float ratio = (float) durationRadixsort / (float) durationArraysSort;
        
        System.out.println(
                String.format(
                        "Time ratio: %.3f.\n", ratio)
                        .replace(',', '.'));
    }
    
    private static int[] createRandomIntegerArray(int length, Random random) {
        int[] array = new int[length];
        
        for (int i = 0; i < length; i++) {
            array[i] = random.nextInt();
        }
        
        return array;
    }
}