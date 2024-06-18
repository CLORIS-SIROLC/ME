package Kernel;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Kernel {
    // 可重入锁
    public static ReentrantLock lock = new ReentrantLock();

    //挂起不满足条件的线程（等待），直到另一个线程发出信号时才被唤醒
    public static Condition clkCondition = lock.newCondition();
    public static Condition jitCondition = lock.newCondition();
    public static Condition pstCondition = lock.newCondition();
    public static Condition inputCondition = lock.newCondition();
    public static Condition outputCondition = lock.newCondition();

}
