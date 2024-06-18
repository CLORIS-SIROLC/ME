package Hardware;

import Kernel.ProcessScheduling_thread;
import Manage.PCB;
import Manage.PCB_manage;

// CPU类————用于执行指令
public class CPU {

    // 程序计数器（PC）
    public static int PC_num;

    // 指令寄存器（IR）
    public static int IR_num;

    // 状态寄存器（PSW）————保存CPU的状态信息
    public static int PSW;
    static public final int CPU_PSW_Systemstate = 1;  //为提高代码可读性，则令系统态为（1）
    static public final int CPU_PSW_Userstate = 0;  //为提高代码可读性，则令用户态为（0）

    // CPU中现在运行的PCB的ID
    public static int process_running_ID;

    // 指令逻辑地址
    public static int ins_Address;

    // 是否空闲
    public static boolean isFree = true;

    // 1类指令执行了一半(true)
    public static boolean isRUNNING_1 = false;
    // 2类指令执行了一半
    public static boolean isRUNNING_2 = false;
    // 3类指令执行了一半
    public static boolean isRUNNING_3 = false;


   //封装为 CPU 类的方法，供进程切换、CPU

    // //  CPU现场保护函数
    // void CPU_PRO()
    // {

    // }       


    // // CPU现场恢复函数
    // // mPCB为要执行的进程
    // void CPU_REC(PCB mPCB)
    // {
    //     this.process_running = mPCB;
    // }       

    //初始化
    public CPU()
    {
        // 操作系统状态初始设置为用户态
        PSW = CPU_PSW_Userstate;

        // 初始为空闲
        isFree = true;
    }
    
    // 进程分配CPU
    public static boolean CPU_IN(int ID)
    {
        int index = PCB_manage.Get_Index_By_JobsID(ID);

        // 离开就绪队列
        ProcessScheduling_thread.Out_List_Ready(ID);

        // PCB转换为运行态
        PCB_manage.PCB_table.get(index).PSW = PCB.PSW_RUNNING;


        // CPU　部分
        isFree = false;     // 不空闲

        if(PCB_manage.PCB_table.get(index).PC == PCB_manage.PCB_table.get(index).InstrucNum)
        {
            // 撤销进程
            PCB_manage.Cancel_PCB(ID);
            isFree = true;     // 不空闲
            return false;
        }
        // 程序计数器（PC）正要执行的指令编号(从0开始)
        PC_num = PCB_manage.PCB_table.get(index).PC;

        // 指令寄存器（IR）下一条
        IR_num = PCB_manage.PCB_table.get(index).IR;

        // 状态寄存器（PSW）————保存CPU的状态信息
        switch(PCB_manage.PCB_table.get(index).instructions.get(PC_num).Instruc_State)
        {
            case 0:
            {
                PSW = CPU_PSW_Userstate;// 状态寄存器（PSW）————用户态
                break;
            }
            case 1:
            {
                PSW = CPU_PSW_Userstate;// 状态寄存器（PSW）————用户态
                break;
            }
            case 2:
            {
                PSW = CPU_PSW_Systemstate;// 状态寄存器（PSW）————系统态
                break;
            }
            case 3:
            {
                PSW = CPU_PSW_Systemstate;// 状态寄存器（PSW）————系统态
                break;
            }
            default:
            {       
                // System.out.println("指令种类越界！！");
                return false;

            }

        }
        // CPU中现在运行的PCB
        process_running_ID = PCB_manage.PCB_table.get(index).ProID;

        // 指令逻辑地址
        ins_Address = PCB_manage.PCB_table.get(index).logic_start_address * 10 +  PC_num ;

        return true;    //装入成功

    }

    // 进程普通离开CPU__(进入就绪队列)
    public static boolean CPU_OUT(int ID)
    {
        int index = PCB_manage.Get_Index_By_JobsID(ID);

        isFree = true;     // 不空闲

        // PCB转换为就绪态
        PCB_manage.PCB_table.get(index).PSW = PCB.PSW_READY;

        // PCB优先级
        if(PCB_manage.PCB_table.get(index).Priority > 3)
        {
            PCB_manage.PCB_table.get(index).Priority -- ;
        }

        // 进入就绪队列
        ProcessScheduling_thread.Into_List_Ready(ID);

        return true;
    }


    // 判断CPU执行PCB这条指令的时间是否足够
    public static int Time_is_enough()
    {// -1————时间片已经用完
    // 1———— 可以开始执行
    // 0———— 指令继续执行
    // 2————系统出错
    // 3————来不及执行

        // 当前剩余时间片小于等于0
        if(ProcessScheduling_thread.Times <= 0)
        {   //时间片已经用完
            // System.out.println("时间片已经用完");
            return -1;
        }
        // 即将执行的指令种类
        switch(PCB_manage.PCB_table.get(PCB_manage.Get_Index_By_JobsID(process_running_ID)).instructions.get(PC_num).Instruc_State)
        {
            case 0: // 指令种类为0
            {
                if(ProcessScheduling_thread.Times >= 1)
                {
                    return 1;
                }
                else 
                {
                    // System.out.println("时间片已经用完");
                    return -1;
                }
            }
            case 1:
            {
                if(isRUNNING_1 == true)  //1类指令正在执行（已经执行了一半）
                {   
                    if(ProcessScheduling_thread.Times >= 1)//当前时间片还有至少1s
                    {
                        return 0;
                    }
                    else
                    {
                       //  System.out.println("系统错误！！！！！！！！！！！！！！");
                        return 2;
                    }                    
                }
                else//1类指令还没开始执行
                {
                    if(ProcessScheduling_thread.Times >= 2)//当前时间片还有至少2s
                    {
                        return 1;
                    }
                    else
                    {
                        return 3;
                    }  
                }
            }
            case 2:
            {
                if(isRUNNING_2 == true)  //2类指令正在执行（已经执行了一半）
                {   
                    if(ProcessScheduling_thread.Times >= 1)//当前时间片还有至少1s
                    {
                        // System.out.println("继续执行2类指令");
                        return 0;
                    } else
                    {
                        // System.out.println("系统错误！！！！！！！！！！！！！！");
                        return 2;
                    }  
                }else//2类指令还没开始执行
                {
                    if(ProcessScheduling_thread.Times >= 2)//当前时间片还有至少2s
                    {
                        // System.out.println("可以执行2类指令");
                        return 1;
                    }
                    else
                    {
                        // .out.println("来不及执行2类指令了");
                        return 3;
                    }  
                }
            }
            case 3:
            {
                if(isRUNNING_3 == true)  //3类指令正在执行（已经执行了一半）
                {   
                    if(ProcessScheduling_thread.Times >= 1)//当前时间片还有至少1s
                    {
                        // System.out.println("继续执行3类指令");
                        return 0;
                    } else
                    {
                        // System.out.println("系统错误！！！！！！！！！！！！！！");
                        return 2;
                    }  
                }else//3类指令还没开始执行
                {
                    if(ProcessScheduling_thread.Times >= 2)//当前时间片还有至少2s
                    {
                        // System.out.println("可以执行3类指令");
                        return 1;
                    }
                    else
                    {
                        // System.out.println("来不及执行3类指令了");
                        return 3;
                    }  
                }
            }
    
         }
        return 1;
    }






}
