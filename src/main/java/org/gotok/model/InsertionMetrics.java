package org.gotok.model;

public class InsertionMetrics {
    private long sum;
    private long count;

    public synchronized void update(long value) {
        sum += value;
        count++;
    }

    public double getAverageLatency() {
        return count == 0 ? 0 : (double) sum / (double) count;
    }
}
