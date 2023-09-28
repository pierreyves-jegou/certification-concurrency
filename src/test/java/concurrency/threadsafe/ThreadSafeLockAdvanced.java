package concurrency.threadsafe;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ThreadSafeLockAdvanced {

    @Test
    void makeItWorks() throws InterruptedException {

        Calculation[] calculations = {
                new Calculation(Calculation.ADDITION, 5),
                new Calculation(Calculation.ADDITION, 10),
                new Calculation(Calculation.SUBTRACTION, 3),
                new Calculation(Calculation.SUBTRACTION, 2),
                new Calculation(Calculation.ADDITION, 10)
        };

        Calculator calculator = new Calculator();

        Runnable countRunnable1 = () -> {
            Stream.iterate(0, i -> i + 1)
                    .limit(100)
                    .forEach(x -> {
                        calculator.calculate(calculations);
                        System.out.println(calculator.getResult());
                    });
        };

        Runnable countRunnable2 = () -> {
            Stream.iterate(0, i -> i + 1)
                    .limit(100)
                    .forEach(x -> {
                        calculator.calculate(calculations);
                        System.out.println(calculator.getResult());
                    });
        };

        ExecutorService executorService = null;

        try{
            executorService = Executors.newFixedThreadPool(2);
            executorService.execute(countRunnable1);
            executorService.execute(countRunnable2);
        } finally {
            executorService.shutdown();
        }

        executorService.awaitTermination(15, TimeUnit.SECONDS);


    }


    class Calculator {

        private double result = 0l;

        public void add(double value) {
            this.result += value;
        }

        public void sub(double value) {
            this.result -= value;
        }

        public double getResult() {
            return result;
        }

        public void calculate(Calculation... calculations) {

            for (Calculation calculation : calculations) {
                switch (calculation.type) {
                    case Calculation.ADDITION:
                        add(calculation.value);
                        break;
                    case Calculation.SUBTRACTION:
                        sub(calculation.value);
                        break;
                }
            }
        }
    }

    class Calculation {

        public static final int UNSPECIFIED = -1;
        public static final int ADDITION = 0;
        public static final int SUBTRACTION = 1;

        int type = UNSPECIFIED;
        private double value;

        public Calculation(int type, double value) {
            this.type = type;
            this.value = value;
        }
    }


}
