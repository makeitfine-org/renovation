/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.common;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CheckBlockingQueueTest {

    private BlockingQueue<Integer> q = new LinkedBlockingQueue<>(1);

    @AfterEach
    void init() {
        q.clear();
    }

    @Test
    void test() throws InterruptedException {
        q.offer(1);
        q.offer(11);
        q.poll(60, TimeUnit.SECONDS);
        q.poll(1, TimeUnit.SECONDS);
        q.offer(2);
        assertEquals(2, q.poll());
        assertEquals(null, q.poll());
        assertEquals(null, q.poll());
    }
}
