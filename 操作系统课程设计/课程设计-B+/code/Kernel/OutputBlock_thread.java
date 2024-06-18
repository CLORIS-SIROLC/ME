package Kernel;

import Manage.PCB_manage;

// 仿真 I/O 中断————————阻塞队列的唤醒线程——————外设输出变量造成进程进入阻塞态
public class OutputBlock_thread implements Runnable{

	public volatile static boolean flag = true;
    @Override
    public void run() {
        
        
        while(flag)
        {
            Kernel.lock.lock();  // 加锁
            try
            {
                Kernel.outputCondition.await();    // 休眠线程output
                
                CHECK_OUTPUT();

                Kernel.clkCondition.signal();       // 唤醒线程clk

            }
            catch(Exception e){e.printStackTrace();}
            finally
            {
                Kernel.lock.unlock();       // 把锁释放掉
            }
        }
    }
    

    // 检查是否有需要出列的阻塞队列2
    private synchronized static boolean CHECK_OUTPUT()
    {
        if(ProcessScheduling_thread.List_Blocked_2.isEmpty() )// 队列为空
        {
        }
        else// 队列非空
        {
            // 遍历阻塞队列，检查是否有可以出列的
            for(int i = 0 ; i <ProcessScheduling_thread.List_Blocked_2.size() ; i++)
            {
                int id = ProcessScheduling_thread.List_Blocked_2.get(i).ProID;// 进程编号
                int time = ProcessScheduling_thread.List_Blocked_2.get(i).BqTimes2; // 进入就绪队列的时间

                // 满足条件的出队
                if(Clock_thread.COUNTTIME >= (time + 2))
                {
                    PCB_manage.Wakeup_PCB(id ,2);
                    // System.out.println("Output出列");
                    break;
                }
            }

        }
        return false;
    }
}
