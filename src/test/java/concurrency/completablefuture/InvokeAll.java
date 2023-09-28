package concurrency.completablefuture;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class InvokeAll {

    Callable<String> callable = () -> {
        Thread.sleep(2000);
        return String.valueOf(Thread.currentThread().getName());
    };

    Supplier<String> supplier = () -> {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return String.valueOf(Thread.currentThread().getName());
    };



    @Test
    void invokeAllWithFuture() throws InterruptedException {

        ExecutorService executorService = null;
        try {
            executorService = Executors.newFixedThreadPool(5);
            List<Future<String>> futures = executorService.invokeAll(List.of(callable, callable));
            futures.stream().forEach(f -> {
                String result = null;
                try {
                    result = f.get();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(result);
            });
        } finally {
            executorService.shutdown();
        }

        executorService.awaitTermination(15, TimeUnit.SECONDS);
        Assertions.assertTrue(true);
    }

    @Test
    void invokeAllWithCompletableFuture() throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        CompletableFuture<String> firstCompletable = CompletableFuture.supplyAsync(supplier, executorService);
        CompletableFuture<String> secondCompletable = CompletableFuture.supplyAsync(supplier, executorService);
        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(firstCompletable, secondCompletable);
        completableFuture.get();
        System.out.println(firstCompletable.get());
        System.out.println(secondCompletable.get());

    }

}
