/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.temp.java.applications.patterns.creational;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingletonDP {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    static class Singleton {
        private static volatile Singleton instance;

        public static Singleton getInstance() {
            if (instance == null) {
                synchronized (Singleton.class) {
                    if (instance == null) {
                        instance = new Singleton();
                    }
                }
            }
            return instance;
        }
    }

    static class CheckSingleton implements Callable<Singleton> {

        @Override
        public Singleton call() {
            return Singleton.getInstance();
        }
    }

    @Test
    public void test() throws ExecutionException, InterruptedException {
        var singleton = Singleton.getInstance();
        var singleton2 = Singleton.getInstance();

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        var singletonT1 = executorService.submit(new CheckSingleton()).get();
        var singletonT2 = executorService.submit(new CheckSingleton()).get();
        executorService.shutdown();

        List.of(singleton2, singletonT1, singletonT2)
                .forEach(
                        s -> Assertions.assertEquals(s, singleton)
                );
    }
}
