package co6;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;

public class PassengerCountScan {
    public static long[] inclusiveScan(long[] counts) {
        Objects.requireNonNull(counts, "Passenger counts cannot be null.");
        int length = counts.length;
        if (length == 0) return new long[0];
        for (long count : counts)
            if (count < 0) throw new IllegalArgumentException("Passenger counts cannot be negative.");

        int paddedLength = 1;
        while (paddedLength < length) {
            if (paddedLength > Integer.MAX_VALUE / 2)
                throw new IllegalArgumentException("Too many intervals to scan.");
            paddedLength *= 2;
        }

        long[] tree = Arrays.copyOf(counts, paddedLength);
        for (int stride = 1; stride < paddedLength; stride *= 2) {
            int blocks = paddedLength / (stride * 2);
            int levelStride = stride;
            IntStream.range(0, blocks).parallel().forEach(block -> {
                int index = (block + 1) * levelStride * 2 - 1;
                tree[index] = Math.addExact(tree[index], tree[index - levelStride]);
            });
        }

        tree[paddedLength - 1] = 0;
        for (int stride = paddedLength / 2; stride >= 1; stride /= 2) {
            int blocks = paddedLength / (stride * 2);
            int levelStride = stride;
            IntStream.range(0, blocks).parallel().forEach(block -> {
                int index = (block + 1) * levelStride * 2 - 1;
                long leftTotal = tree[index - levelStride];
                tree[index - levelStride] = tree[index];
                tree[index] = Math.addExact(tree[index], leftTotal);
            });
        }

        long[] inclusive = new long[length];
        for (int index = 0; index < length; index++)
            inclusive[index] = Math.addExact(tree[index], counts[index]);
        return inclusive;
    }
}