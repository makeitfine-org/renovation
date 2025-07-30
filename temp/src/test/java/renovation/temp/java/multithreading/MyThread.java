/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.temp.java.multithreading;

public class MyThread {

    private static final Object LOCK = new Object();

    static class MyRunnable implements Runnable {

        @Override
        public void run() {
//            System.out.println("RUNNING"); // RUNNING
//            try {
            synchronized (LOCK) {
//                    LOCK.wait();
                while (true) {
                    try {
                        System.out.println(">>> " + Thread.currentThread().getName());
                        LOCK.wait();
                        System.out.println("!>>> " + Thread.currentThread().getName());
                        Thread.sleep(10000);
                        break;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            System.out.println(Thread.currentThread().getName()+": FINISHED");
//            }
//            catch (InterruptedException e) {
//                System.out.println(e);
//            }
        }
    }


    //            @Test
//    void test() throws InterruptedException {
    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(new MyRunnable());
        System.out.println(t1.getState()); // NEW
        t1.start();
        System.out.println(t1.getState()); // RUNNABLE
        Thread.sleep(100);
        System.out.println(t1.getState()); // TIMED_WAITING
        Thread t2 = new Thread(new MyRunnable());
//        t2.setDaemon(true);
        t2.start();
        Thread.sleep(100);
        System.out.println("t2: " + t2.getState()); // TIMED_WAITING
//        System.out.println(t1.getState()); // TERMINATED
        synchronized (LOCK) {
//            System.out.println("t2: " + t2.getState()); // TIMED_WAITING
            LOCK.notifyAll();
        }
        t1.interrupt();
//        t2.interrupt();
//        t1.join();
        System.out.println(t1.getState()); // TERMINATED
//        System.out.println(Thread.currentThread().getState()); // TERMINATED

    }
}
