package Kernel;


import Manage.JCB_manage;
import Manage.PCB_manage;
import Others.OtherString;
import UI.ui;

public class JobIn_thread implements Runnable{
    // 作业请求的中断仿真————————作业请求查询线程类

    
	public volatile static boolean flag = true;

    // 作业请求判读函数
    // 有作业请求————返回true   没有作业请求————返回false
    private synchronized static boolean CheckJob()
    {
        boolean x = false;
        if(Clock_thread.COUNTTIME%10==0) // 变量计时到十秒了
        {
            while(JCB_manage.JCB_list_todo.isEmpty() != true)// 保证在todo列表非空的前提下判断
            {
                if(JCB_manage.JCB_list_todo.get(JCB_manage.Get_Earliest_Arrival_Time_JCB_todo()).InTimes_0 > Clock_thread.COUNTTIME)
                {// 新作业请求的时间未到达
                     break;
                }
                else // 新作业请求的时间到达
                {
                    String temp = new String(Clock_thread.COUNTTIME + ":[新增作业:" + JCB_manage.JCB_list_todo.get(JCB_manage.Get_Earliest_Arrival_Time_JCB_todo()).JobsID
                    + "," + JCB_manage.JCB_list_todo.get(JCB_manage.Get_Earliest_Arrival_Time_JCB_todo()).InTimes_0 
                    + "," + JCB_manage.JCB_list_todo.get(JCB_manage.Get_Earliest_Arrival_Time_JCB_todo()).InstrucNum + "]\n");
//                    System.out.print(temp);
                    OtherString.out_string_1 += temp;
                    ui.textArea_running.setText(OtherString.out_string_1);
                    ui.textArea_running.setCaretPosition(ui.textArea_running.getText().length());   
    
                    OtherString.out_string_all += temp;
                    
                    // 尝试将作业JCB转化为PCB
                    if(PCB_manage.Create_PCB(JCB_manage.JCB_list_todo.get(JCB_manage.Get_Earliest_Arrival_Time_JCB_todo())))
                    {// 尝试成功
                    }
                    else    // 尝试失败
                    {
                        // System.out.print("尝试失败！！！\n");
                        // 此作业转到doing列表(表示已到达且等待PCB)
                        JCB_manage.Todo_Translate_Doing(JCB_manage.JCB_list_todo.get(JCB_manage.Get_Earliest_Arrival_Time_JCB_todo()).JobsID);
                    }
                    x = true;

                }
            }
        }
        return x;
    } 


    @Override
    public void run() {
        while(flag){
        while(true)
        {
            Kernel.lock.lock();     // lock up（上锁）
            try{
                //初始休眠，保持各个进程同步，由别人唤醒
                Kernel.jitCondition.await();    // 休眠线程jit

                if(CheckJob())
                {
                    //System.out.println(Clock_thread.COUNTTIME+":[JIT run]");
                }

                Kernel.pstCondition.signal();   // 唤醒线程pst
            }
            catch(Exception e){e.printStackTrace();}
            finally{
                Kernel.lock.unlock();   // unlock（关锁）
            }
        }
    }
    }

    // 实时添加作业
   public void job_IN()
   {
        JCB_manage.Add_new_JCB_todo();

   }
}