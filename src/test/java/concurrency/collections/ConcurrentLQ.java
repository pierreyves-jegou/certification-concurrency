package concurrency.collections;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class ConcurrentLQ {

    @Test
    void queueMeConcurrently() throws InterruptedException {

        Queue<Integer> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
        //Queue<Integer> concurrentLinkedQueue = new ArrayDeque<>();


        ExecutorService executorService = null;

        try {
            executorService = Executors.newFixedThreadPool(3);
            executorService.submit(createWritingTask(concurrentLinkedQueue));
            executorService.submit(createWritingTask(concurrentLinkedQueue));
            executorService.submit(createWritingTask(concurrentLinkedQueue));
        } finally {
            executorService.shutdown();
        }


        executorService.awaitTermination(15, TimeUnit.SECONDS);

        int sum = concurrentLinkedQueue.stream()
                .mapToInt(x -> x)
                .sum();
        Assertions.assertEquals(4950 * 3, sum);
    }

    private Runnable createWritingTask(Queue<Integer> concurrentLinkedQueue) {
        return () -> {
            Stream.iterate(0, i -> i + 1).limit(100)
                    .forEach(x -> concurrentLinkedQueue.add(x));
        };

    }
}
