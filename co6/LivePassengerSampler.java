package co6;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class LivePassengerSampler {
    private final int capacity;
    private final Random random;
    private final List<String> reservoir = new ArrayList<>();
    private long eventsSeen;

    public LivePassengerSampler(int capacity) {
        this(capacity, new Random());
    }

    public LivePassengerSampler(int capacity, long seed) {
        this(capacity, new Random(seed));
    }

    private LivePassengerSampler(int capacity, Random random) {
        if (capacity < 0) throw new IllegalArgumentException("Sample size cannot be negative.");
        this.capacity = capacity;
        this.random = Objects.requireNonNull(random);
    }

    public void accept(String event) {
        if (event == null || event.isBlank()) throw new IllegalArgumentException("Passenger event cannot be blank.");
        eventsSeen = Math.incrementExact(eventsSeen);
        if (reservoir.size() < capacity) {
            reservoir.add(event);
            return;
        }
        if (capacity == 0) return;
        long position = random.nextLong(eventsSeen);
        if (position < capacity) reservoir.set((int) position, event);
    }

    public long eventsSeen() {
        return eventsSeen;
    }

    public List<String> sample() {
        return new ArrayList<>(reservoir);
    }
}