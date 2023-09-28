package concurrency.threadsafe;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadSafeAtomic {


    @Test
    void doItWithSynchronized() throws InterruptedException {


        // Given
        // - 1 objet partagé entre 2 threads "Counter" qui enregsitre l'information de comptage
        ThreadSafeAtomic.Counter counter = new ThreadSafeAtomic.Counter();

        // - 2 runnables qui partage la ressource "Counter"
        Runnable countRunnable1 = () -> {
            for(int i=0; i<1000000; i++){
                counter.incrementAndGet();
            }
            System.out.println(counter.get());
        };

        Runnable countRunnable2 = () -> {
            for(int i=0; i<1000000; i++){
                counter.incrementAndGet();
            }
            System.out.println(counter.get());
        };

        // Objectif : faire en sorte via l'utilisation d'une classe "Atomic" que "Counter.count" soit bien égale à 2 000 000 à la fin

        ExecutorService executorService = null;

        try{
            executorService = Executors.newFixedThreadPool(2);
            executorService.execute(countRunnable1);
            executorService.execute(countRunnable2);
        } finally {
            executorService.shutdown();
        }

        executorService.awaitTermination(15, TimeUnit.SECONDS);

        Assertions.assertEquals(2000000, counter.get());




    }

    class Counter {


        private int count = 0;

        public int incrementAndGet(){
            return count++;
        }

        public int get(){
            return count;
        }


        //        private AtomicInteger count = new AtomicInteger(0);
//
//        public int incrementAndGet(){
//            return count.getAndIncrement();
//        }
//
//        public int get(){
//            return count.get();
//        }

    }

}
