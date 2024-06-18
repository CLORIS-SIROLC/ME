package Kernel;

import Manage.PCB_manage;

// 仿真 I/O 中断————————阻塞队列的唤醒线程——————外设输入变量造成进程进入阻塞态
public class InputBlock_thread implements Runnable{

    
	public volatile static boolean flag = true;

    @Override
    public void run() {
        
        
        while(flag)
        {
            Kernel.lock.lock();  // 加锁
            try
            {
                Kernel.inputCondition.await();    // 休眠线程input

                CHECK_INPUT();      // 检查是否有需要出列的阻塞队列1
                
                Kernel.outputCondition.signal();  // 唤醒线程output

            }
            catch(Exception e){e.printStackTrace();}
            finally
            {
                Kernel.lock.unlock();       // 把锁释放掉
            }
        }
    }


    // 检查是否有需要出列的阻塞队列1
    private synchronized static boolean CHECK_INPUT()
    {
        if(ProcessScheduling_thread.List_Blocked_1.isEmpty() )// 队列为空
        {
        }
        else// 队列非空
        {
            // 遍历阻塞队列，检查是否有可以出列的
            for(int i = 0 ; i <ProcessScheduling_thread.List_Blocked_1.size() ; i++)
            {
                int id = ProcessScheduling_thread.List_Blocked_1.get(i).ProID;// 进程编号
                int time = ProcessScheduling_thread.List_Blocked_1.get(i).BqTimes1; // 进入就绪队列的时间

                // 满足条件的出队
                if(Clock_thread.COUNTTIME >= (time + 2))
                {
                    PCB_manage.Wakeup_PCB(id ,1);
                    // System.out.println("Input出列");
                    break;
                }
            }

        }
        return false;
    }
}
