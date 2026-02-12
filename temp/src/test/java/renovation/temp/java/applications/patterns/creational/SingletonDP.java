/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.temp.java.applications.patterns.creational;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class SingletonDP {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    static class Singleton {
        private static volatile Singleton instance;

        public static Singleton getInstance() {
            if (instance == null) {
                synchronized (Singleton.class) {
                    if (instance == null) {
                        log.info("Thread of creation: {}", Thread.currentThread().getName());
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
        // When
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        // And
        var futureT1 = executorService.submit(new CheckSingleton());
        var futureT2 = executorService.submit(new CheckSingleton());
        var singleton = Singleton.getInstance();

        // Then
        List.of(futureT2.get(), futureT1.get())
                .forEach(
                        s -> Assertions.assertSame(s, singleton)
                );

        executorService.shutdown();
    }
}
