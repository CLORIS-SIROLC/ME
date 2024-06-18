package Kernel;
public class Clock_thread implements Runnable {
    // 系统时钟中断仿真————————时钟线程类

    //初始时间的静态变量
    public static int COUNTTIME = 0; // 单位：秒（s）

	public volatile static boolean flag = true;

    // 模拟时钟     对 COUNTTIME 变量计时操作
    private synchronized static void TIME_COUNT()
    {
        // System.out.println(COUNTTIME+":[CLK run]");
        COUNTTIME++;
        try {
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void run() {
        // 休眠，使每个初始线程同步开始
            try{
                Thread.sleep(200);     
            }
            catch(InterruptedException e){e.printStackTrace();}

            while(flag)
            {
                Kernel.lock.lock();  // 加锁
                try
                {
                    Kernel.jitCondition.signal();  // 唤醒线程jit
                    Kernel.clkCondition.await();    // 休眠线程clk
                    
                    TIME_COUNT();

                }
                catch(Exception e){e.printStackTrace();}
                finally
                {
                    Kernel.lock.unlock();       // 把锁释放掉
                }
            }
        

   

    }

   
}