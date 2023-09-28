package concurrency.starting;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.function.Function;
import java.util.stream.Stream;

public class StartThreadTest {

    @Test
    void startARunnable(){

        // Given
        Runnable runnable = () -> {
            Stream.generate(Math::random)
                    .limit(1_000)
                    .forEach(System.out::println);
        };

        // L'objectif ici est de créer et démarrer un thread qui executera le "Runnable"
        //TODO
        new Thread(runnable).start();
        System.out.println("YYYYYYYYYYYYYYYYYYYYOP");
        Assertions.assertTrue(true);
    }

    @Test
    void startACallable() throws ExecutionException, InterruptedException {
        //Given
        Callable<Integer> callable = () -> {
            Integer sum = Stream.iterate(0,  i -> i < 1_000_000, i -> i + 1)
                    .mapToInt(Integer::valueOf)
                    .sum();
            return sum;
        };


        // L'objectif ici est de lancer un Thread avec un "Callable". A la difference du "Runnable",
        // le "Callable" permettra de récupérer le résultat du traitement du Thread
        // Tips : Il faut passer par l'objet intermédiare "FutureTask" pour pouvoir lancer un Thread avec un "Callable"
        FutureTask<Integer> future = null; //TODO
        // TODO lancer le traitement

        Integer sum = future.get();
        Assertions.assertEquals(1783293664, sum);

    }

}
