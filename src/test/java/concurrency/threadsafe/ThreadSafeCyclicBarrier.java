package concurrency.threadsafe;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.*;

public class ThreadSafeCyclicBarrier {

    @Test
    void useCyclicBarrier() throws InterruptedException {

        // Given
        // A list of interger
        List<Integer> inputList = List.of(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);
        List<Integer> inputList2 = List.of(4,5,6);
        List<Integer> inputList3 = List.of(7,8,9,10,11);
        List<Integer> inputList4 = List.of(12,13,14,15);

        // L'objectif ici est de garantir que l'output produit sur la console sera composé de 4X "SUM =>" puis 4X "SUM -100 =>"
        /* Ex :
        SUM => 15
        SUM => 120
        SUM => 54
        SUM => 45
        SUM - 100 => -46
        SUM - 100 => 20
        SUM - 100 => -55
        SUM - 100 => -85
         */

        ExecutorService executorService = null;
        CyclicBarrier cyclicBarrier = null; //TODO

        try{
            executorService = Executors.newFixedThreadPool(4);
            executorService.execute(createTask(inputList, 158));
            executorService.execute(createTask(inputList2, 4878));
            executorService.execute(createTask(inputList3, 477));
            executorService.execute(createTask(inputList4, 78));
        } finally {
            executorService.shutdown();
        }

        executorService.awaitTermination(15, TimeUnit.SECONDS);
    }

    private Runnable createTask(List<Integer> integers, Integer randomThreadSleep ) { //TODO
        return () ->  {
            try {
                Thread.sleep(randomThreadSleep);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


            int sum = integers.stream().mapToInt(x -> x).sum();
            System.out.println("SUM => " + sum);

            //TODO


            int minusHundred = sum - 100;
            System.out.println("SUM - 100 => " + minusHundred);
        };
    }


}
