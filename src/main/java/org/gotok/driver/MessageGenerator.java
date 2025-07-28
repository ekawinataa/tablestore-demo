package org.gotok.driver;

import com.google.protobuf.InvalidProtocolBufferException;
import net.datafaker.Faker;
import org.gotok.model.InsertionMetrics;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

public class MessageGenerator {

    private final int batchSize;
    private final Consumer<Map<String, byte[]>> messageConsumer;
    private final ScheduledExecutorService executorService;
    private final Faker faker;

    public MessageGenerator(int batchSize,
                            int parallelism,
                            Consumer<Map<String, byte[]>> messageConsumer) {
        this.batchSize = batchSize;
        this.messageConsumer = messageConsumer;
        this.executorService = Executors.newScheduledThreadPool(parallelism);
        this.faker = new Faker();
    }

    public void generate() throws InvalidProtocolBufferException, InterruptedException {
        InsertionMetrics insertionMetrics = new InsertionMetrics();
        for (int i = 0; i < batchSize; i++) {
            executorService.submit(new DynamicMessageTask(messageConsumer, insertionMetrics));
            Thread.sleep(5);
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            System.out.println("Waiting for all tasks to complete...");
            Thread.sleep(1000); // Wait for all tasks to complete
        }
        System.out.println("All tasks completed successfully. Average latency: " +
                insertionMetrics.getAverageLatency() + " ms");
    }

    private static class DynamicMessageTask implements Runnable {
        private final Consumer<Map<String, byte[]>> messageConsumer;
        private final InsertionMetrics insertionMetrics;

        public DynamicMessageTask(Consumer<Map<String, byte[]>> messageConsumer,
                                  InsertionMetrics insertionMetrics) {
            this.messageConsumer = messageConsumer;
            this.insertionMetrics = insertionMetrics;
        }

        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            Map<String, byte[]> values = new HashMap<>();
            values.put("id", "testID".getBytes());
            values.put("value", "testValue".getBytes());
            messageConsumer.accept(values);
            long endTime = System.currentTimeMillis();
            insertionMetrics.update(endTime - startTime);
        }
    }
}
