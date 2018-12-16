package com.aboruo.jvmtools.testcode;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Description
 * <p>
 * deadlock test
 * </p>
 * DATE 2018/12/16.
 * @author aboruo.
 */
public class DeadLockTest extends Thread {
    protected Object myTransport;
    /**
     * myIncome = salary or reward
     */
    private static ReentrantLock car = new ReentrantLock();
    private static ReentrantLock plane = new ReentrantLock();
    public DeadLockTest() {
    }
    public DeadLockTest(Object myTransport) {
        this.myTransport = myTransport;
        if (myTransport == car) {
            this.setName("byCar");
        }
        if (myTransport == plane) {
            this.setName("byPlane");
        }
    }
    @Override
    public void run() {
        if (this.myTransport == car) {
            try {
                car.lockInterruptibly(); //占用car锁
                try {
                    Thread.sleep(500); //等待plane 启动
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                plane.lockInterruptibly(); //去占有plane锁
                System.out.println("myTransport to plane has passed");
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("myTransport to plane is killed");
            } finally {
                if(plane.isHeldByCurrentThread())
                    plane.unlock();
                if(car.isHeldByCurrentThread())
                    car.unlock();
            }
        }
        if (this.myTransport == plane) {
            try {
                plane.lockInterruptibly(); // 占用plane锁
                try {
                    Thread.sleep(500); //等待car 启动
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                car.lockInterruptibly(); //去占用car锁
                System.out.println("myTransport to car has passed");
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("myTransport to car is killed");
            } finally {
                if (car.isHeldByCurrentThread())
                    car.unlock();
                if (plane.isHeldByCurrentThread())
                    plane.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        //这2个线程死锁
        DeadLockTest carTrans = new DeadLockTest(car);
        DeadLockTest planeTrans = new DeadLockTest(plane);
        carTrans.start();
        planeTrans.start();
        Thread.sleep(1000);
    }
}
