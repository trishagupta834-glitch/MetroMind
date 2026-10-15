package co6;

import java.util.Random;
import java.util.Objects;

public class PassengerDemandRanking {
    public enum Metric {
        PASSENGERS,
        ARRIVAL_TIME,
        DELAY_MINUTES
    }

    public static class ServiceRecord {
        public final String service;
        public final long passengers;
        public final long sortValue;
        public final String displayValue;

        public ServiceRecord(String service, long passengers) {
            this(service, passengers, passengers, Long.toString(passengers));
        }

        public ServiceRecord(String service, long sortValue, long passengers, String displayValue) {
            if (service == null || service.isBlank()) throw new IllegalArgumentException("Service name cannot be blank.");
            if (sortValue < 0 || passengers < 0) throw new IllegalArgumentException("Service values cannot be negative.");
            this.service = service.trim();
            this.passengers = passengers;
            this.sortValue = sortValue;
            this.displayValue = Objects.requireNonNull(displayValue);
        }
    }

    public static void sortByDemand(ServiceRecord[] records) {
        sort(records, Metric.PASSENGERS);
    }

    public static void sort(ServiceRecord[] records, Metric metric) {
        Objects.requireNonNull(records, "Service records cannot be null.");
        Objects.requireNonNull(metric, "Sort metric cannot be null.");
        for (ServiceRecord record : records) Objects.requireNonNull(record, "A service record cannot be null.");
        quickSort(records, 0, records.length - 1, new Random(), metric);
    }

    private static void quickSort(ServiceRecord[] records, int low, int high, Random random, Metric metric) {
        if (low >= high) return;
        int pivot = low + random.nextInt(high - low + 1);
        swap(records, pivot, high);
        int boundary = low;
        for (int i = low; i < high; i++)
            if (comesBefore(records[i], records[high], metric)) swap(records, boundary++, i);
        swap(records, boundary, high);
        quickSort(records, low, boundary - 1, random, metric);
        quickSort(records, boundary + 1, high, random, metric);
    }

    private static boolean comesBefore(ServiceRecord first, ServiceRecord second, Metric metric) {
        if (first.sortValue != second.sortValue) {
            return metric == Metric.ARRIVAL_TIME
                    ? first.sortValue < second.sortValue
                    : first.sortValue > second.sortValue;
        }
        int caseInsensitiveOrder = first.service.compareToIgnoreCase(second.service);
        return caseInsensitiveOrder == 0
            ? first.service.compareTo(second.service) < 0
            : caseInsensitiveOrder < 0;
    }

    private static void swap(ServiceRecord[] records, int first, int second) {
        ServiceRecord value = records[first];
        records[first] = records[second];
        records[second] = value;
    }
}