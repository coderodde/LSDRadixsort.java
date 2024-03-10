package com.github.coderodde.util.demo;

import com.github.coderodde.util.LSDRadixsort;
import java.util.Arrays;

public class LSDRadixsortDemo {
    
    public static void main(String[] args) {
        int[] array1 = { -1, -2 };
        int[] array2 = array1.clone();
        
        LSDRadixsort.sort(array1);
        Arrays.sort(array2);
        
        System.out.printf("LSDRaidxsort returned: %s.\n", 
                          Arrays.toString(array1));
        
        System.out.printf("Arrays.sort returned: %s.\n", 
                          Arrays.toString(array2));
        
        System.out.printf("Arrays agree: %b.\n", Arrays.equals(array1, array2));
    }
}