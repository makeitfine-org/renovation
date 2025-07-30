/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.temp.java.multithreading;

class Counter {
    private int count = 0; // shared resource

    public synchronized void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }
}

public class MyThread2 extends Thread {
    private Counter counter;

    public MyThread2(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            counter.increment();
        }
    }

    public static void main(String[] args) {
        Counter counter = new Counter();
        MyThread2 t1 = new MyThread2(counter);
        MyThread2 t2 = new MyThread2(counter);
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        }catch (Exception e){

        }
        System.out.println(counter.getCount()); // Expected: 2000, Actual will be random <= 2000
    }
}
