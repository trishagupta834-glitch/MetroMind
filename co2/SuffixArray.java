package co2;

import java.util.*;

public class SuffixArray {
    public static int[] build(String text) {
        Integer[] a = new Integer[text.length()];
        for (int i = 0; i < a.length; i++) a[i] = i;
        Arrays.sort(a, (i, j) -> text.substring(i).compareTo(text.substring(j)));
        int[] result = new int[a.length];
        for (int i = 0; i < a.length; i++) result[i] = a[i];
        return result;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(build("banana")));
    }
}
