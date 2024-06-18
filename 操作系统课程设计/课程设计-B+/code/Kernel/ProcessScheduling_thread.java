package Kernel;

import java.util.ArrayList;
import java.util.List;

import Hardware.CPU;
import Hardware.MMU;
import Manage.JCB_manage;
import Manage.PCB_manage;
import Others.OtherString;
import UI.ui;

//进程调度模块
public class ProcessScheduling_thread implements Runnable{
    // 进程调度的中断仿真————————进程调度算法线程类

	public volatile static boolean flag = true;

    // 时间片（初始值）最大值
    public static int Times_first = 3;   // 单位：秒（s）

    // 静态时间片
    public static int Times = 3;   // 单位：秒（s）


    // 就绪队列
    public static List<mReady> List_Ready;
    // 当前就绪队列中PCB数量
    public static int Ready_num = 0;
    // 就绪队列信息表项
    public static class mReady{

        // 位置编号（RqNum）————从1开始
		public int RqNum;

        // 进入就绪队列时间（RqTimes）
		public int RqTimes;

        // 进程编号（ProID）
        public int ProID;

        // 初始化
        public mReady(int a, int b, int c) {
            RqNum = a;
            RqTimes = b;
            ProID = c;
        }
    }

    // 阻塞队列1(键盘输入阻塞队列)
    public static List<mBlocked_1> List_Blocked_1;
    // 当前阻塞队列1中PCB数量
    public static int Blocked_1_num = 0;
    // 阻塞队列1中最早进入的进程PCB的编号ProID
    public static int Blocked_1_first_id = -1;
    // 阻塞队列1(键盘输入阻塞队列)信息表项
    public static class mBlocked_1{
        // 位置编号（BqNum1）————从1开始
		public int BqNum1;

        // 进程进入阻塞队列时间（BqTimes1）
		public int BqTimes1;

        // 进程编号（ProID）
        public int ProID;
        
        // 初始化
        public mBlocked_1(int a, int b, int c) {
            BqNum1 = a;
            BqTimes1 = b;
            ProID = c;
        }
    }

    // 阻塞队列2(显示器输出阻塞队列)
    public static List<mBlocked_2> List_Blocked_2;
    // 当前阻塞队列2中PCB数量
    public static int Blocked_2_num = 0;
    // 阻塞队列2中最早进入的进程PCB的编号ProID
    public static int Blocked_2_first_id = -1;
    // 阻塞队列2(显示器输出阻塞队列)信息表项
    public static class mBlocked_2{
        // 位置编号（BqNum2）————从1开始
		public int BqNum2;

        // 进程进入阻塞队列时间（BqTimes2）
		public int BqTimes2;

        // 进程编号（ProID）
        public int ProID;

        // 初始化
        public mBlocked_2(int a, int b, int c) {
            BqNum2 = a;
            BqTimes2 = b;
            ProID = c;
        }
    }






    // 调度时长设置函数
    private synchronized static void SetSchedulingTime()
    {
        // pri 为当前占用CPU 的进程的优先级
        int pri = PCB_manage.PCB_table.get(PCB_manage.Get_Index_By_JobsID(CPU.process_running_ID)).Priority;
        switch(pri)
        {
            case 5:
            {
                Times_first = 3;
                break;
            }
            case 4:
            {
                Times_first = 4;
                break;
            }
            case 3:
            {
                Times_first = 5;
                break;
            }
            default:
            {
                Times_first = 3;
            }

        }

    }

    // 进程调度函数（时间片轮转）
    private synchronized static boolean ProcessScheduling()
    {
        if(ui.rdbtnNewRadioButton_SJP.isSelected())     // 当选择时间片调度算法时
        {
            // 决定进入CPU的进程
            if(Get_ReadyID_By_Time() == -1)
            {
                // System.out.println("找不到ID !!");
                return false;
            }
            CPU.CPU_IN(Get_ReadyID_By_Time());                
            // System.out.println("进程调度");
            return true;// 调度成功，新进程占用CPU
        }
        else     // 多级反馈队列调度算法时
        {
            // 决定进入CPU的进程
            if(Get_ReadyID_By_Time_and_Priority() == -1)
            {
                // System.out.println("找不到ID !!");
                return false;
            }

            // 进程进入CPU
            CPU.CPU_IN(Get_ReadyID_By_Time_and_Priority());            
            // System.out.println("进程调度");

            // 调度时长设置函数
            SetSchedulingTime();
            return true;// 调度成功，新进程占用CPU
        }
    }

    @Override
    public void run() {
        while(flag){
        while(true)
        {
            Kernel.lock.lock();  // 加锁
            try
            {
                Kernel.pstCondition.await();    // 休眠线程pst

                // 更新ui上各个队列信息
                Check_empty_Ready();
                Check_empty_Blocked_1();
                Check_empty_Blocked_2();
                
                if(CPU.isFree)  // 此时CPU空闲，时间片（或许）为满
                {
                    if(!List_Ready.isEmpty())    // 就绪队列不为空（有进程等待执行）
                    {
                        if(ProcessScheduling())    // 进程调度————选择合适的进程占用CPU
                        {// 调度成功，新进程占用CPU     
                            // 设定CPU繁忙
                            CPU.isFree = false;
                            // 重置时间片
                            Times = Times_first;

                            // 进程运行
                            switch(CPU.Time_is_enough())// 时间片是否足够
                            {
                                // case -1:    // 时间片已经用完
                                // {
                                //     // 不存在此种情况
                                //     break;
                                // }
                                // case 0:     // 指令继续执行（1、2、3这类占用2s的指令）
                                // {
                                //     // 不存在此种情况
                                //     break;
                                // }
                                case 1:      // 可以开始执行（0、1、2、3类）
                                {// 即将执行的指令
                                    switch(PCB_manage.PCB_table.get(PCB_manage.Get_Index_By_JobsID(CPU.process_running_ID)).instructions.get(CPU.PC_num).Instruc_State)
                                    {
                                        case 0: // 0类指令，耗时1s
                                        {
                                            CPU.isFree = false;

                                            // 输出
                                            String temp = new String(Clock_thread.COUNTTIME + ":[运行进程:" + CPU.process_running_ID    // 进程ID
                                            + ":" + String.valueOf(CPU.PC_num + 1)     // 指令段编号
                                            + "," + String.valueOf(0) // 指令类型编号
                                            + "," + CPU.ins_Address  // 逻辑地址
                                            + "," + String.valueOf(MMU.Address_Transfer(CPU.ins_Address)) +"]\n");
                                            OtherString.out_string_1 += temp;
                                            ui.textArea_running.setText(OtherString.out_string_1);
                                            ui.textArea_running.setCaretPosition(ui.textArea_running.getText().length());   

                                            // 对于PCB的更改（执行完了修改）
                                            PCB_manage.PCB_table.get(PCB_manage.Get_Index_By_JobsID(CPU.process_running_ID)).PC++;
                                            PCB_manage.PCB_table.get(PCB_manage.Get_Index_By_JobsID(CPU.process_running_ID)).IR++;

                                            // 对于CPU的更改
                                            CPU.PSW = CPU.CPU_PSW_Userstate;    //转换为用户态

                                            // 对于时间片的更改 
                                            Times--;

                                            // 此CPU全部执行完了
                                            if(PCB_manage.PCB_table.get(PCB_manage.Get_Index_By_JobsID(CPU.process_running_ID)).PC
                                            == PCB_manage.PCB_table.get(PCB_manage.Get_Index_By_JobsID(CPU.process_running_ID)).InstrucNum)
                                            {
                                                //PCB撤销
                                                PCB_manage.Cancel_PCB(CPU.process_running_ID);
                                                CPU.isFree = true;
                                                break;
                                            }

                                            // 指令执行完后,判断情况
                                            if(Times == 0)// 时间片用完
                                            {
                                                // 进程离开CPU
                                                CPU.CPU_OUT(CPU.process_running_ID);
                                                CPU.isFree = true;
                                            } 
                                            else // 还有时间
                                            {// 下一条 指令类型判断
                                                switch(PCB_manage.PCB_table.get(PCB_manage.Get_Index_By_JobsID(CPU.process_running_ID)).instructions.get(CPU.PC_num).Instruc_State)
                                                {
                                                    case 0:// 指令类型为0
                                                    {
                                                        CPU.isFree = false;
                                                        CPU.IR_num++;
                                                        CPU.PC_num++;
                                                        CPU.ins_Address++;
                                                        break;
                                                    }
                                                    case 1:// 指令类型为1
                                                    {
                                                        if(Times < 2)// 时间片不够用
                                                        {
                                                            // 进程离开CPU
                                                            CPU.CPU_OUT(CPU.process_running_ID);
                                                            CPU.isFree = true;
                                                            CPU.isRUNNING_1 = false;
                                                        }
                                                        else // 刚好够用
                                                        {
                                                            CPU.isRUNNING_1 = false;
                                                            CPU.isFree = false;
                                                            CPU.IR_num++;
                                                            CPU.PC_num++;
                                                            CPU.ins_Address++;
                                                        }
                                                        break;
                                                    }
                                                    case 2:// 指令类型为2
                                                    {
                                                        if(Times < 2)// 时间片不够用
                                                        {
                                                            // 进程离开CPU
                                                            CPU.CPU_OUT(CPU.process_running_ID);
                                                            CPU.isFree = true;
                                                            CPU.isRUNNING_2 = false;
                                                        }
                                                        else // 刚好够用
                                                        {
                                                            CPU.isRUNNING_2 = false;
                                                            CPU.isFree = false;
                                                            CPU.IR_num++;
                                                            CPU.PC_num++;
                                                            CPU.ins_Address++;
                                                        }
                                                        break;
                                                    }
                                                    case 3:// 指令类型为3
                                                    {
                                                        if(Times < 2)// 时间片不够用
                                                        {
                                                            // 进程离开CPU
                                                            CPU.CPU_OUT(CPU.process_running_ID);
                                                            CPU.isFree = true;
                                                            CPU.isRUNNING_3 = false;
                                                        }
                                                        else // 刚好够用
                                                        {
                                                            CPU.isRUNNING_3 = false;
                                                            CPU.isFree = false;
                                                            CPU.IR_num++;
                                                            CPU.PC_num++;
                                                            CPU.ins_Address++;
                                                        }
                                                        break;
                                                    }
                                                }
                                            }
                                            break;

                                        }
                                        case 1: //1类指令的第1s
                                        {
                                            // 先执行

                                            // 对于CPU的更改
                                            CPU.isFree = false;
                                            CPU.PSW = CPU.CPU_PSW_Userstate;    //转换为用户态
                                            CPU.isRUNNING_1 = true;

                                            // 对于时间片的更改 
                                            Times--;

                                            // 输出
                                            String temp = new String(Clock_thread.COUNTTIME + ":[运行进程:" + CPU.process_running_ID    // 进程ID
                                            + ":" + String.valueOf(CPU.PC_num + 1)     // 指令段编号
                                            + "," + String.valueOf(1) // 指令类型编号
                                            + "," + CPU.ins_Address  // 逻辑地址
                                            + "," + String.valueOf(MMU.Address_Transfer(CPU.ins_Address)) +"]\n");  // 物理地址
                                            OtherString.out_string_1 += temp;
                                            ui.textArea_running.setText(OtherString.out_string_1);
                                            ui.textArea_running.setCaretPosition(ui.textArea_running.getText().length());   

                                            // 执行完后,判断情况
                                            if(Times == 0)// 时间片用完
                                            {
                                                //不存在此种情况
                                                // System.out.println("系统错误");
                                            } else //时间片未用完，应该继续执行1类指令
                                            {
                                                CPU.isFree = false;
                                                // 指令1运行位 置1
                                                CPU.isRUNNING_1 = true;
                                            }
                                            break;
                                        }
                                        case 2: //2类指令(Input)的第1s
                                        {
                                            // 先执行
                                            // 对于CPU的更改
                                            CPU.isFree = false;
                                            CPU.PSW = CPU.CPU_PSW_Systemstate;    //转换为系统态
                                            CPU.isRUNNING_2 = true;

                                            // 对于时间片的更改 
                                            Times--;

                                            // 输出
                                            String temp = new String(Clock_thread.COUNTTIME + ":[运行进程:" + CPU.process_running_ID    // 进程ID
                                            + ":" + String.valueOf(CPU.PC_num + 1)     // 指令段编号
                                            + "," + String.valueOf(2) // 指令类型编号
                                            + "," + CPU.ins_Address  // 逻辑地址
                                            + "," + String.valueOf(MMU.Address_Transfer(CPU.ins_Address)) +"]\n");  // 物理地址
                                            OtherString.out_string_1 += temp;
                                            ui.textArea_running.setText(OtherString.out_string_1);
                                            ui.textArea_running.setCaretPosition(ui.textArea_running.getText().length());

                                            temp = new String(String.valueOf(Clock_thread.COUNTTIME + 1) + ":进程"+ CPU.process_running_ID +" Input事件发生\n");
                                            OtherString.output_string += temp;
                                            ui.textArea_io.setText(OtherString.output_string);
                                            ui.textArea_io.setCaretPosition(ui.textArea_io.getText().length());
                   

                                            // 执行完后,判断情况
                                            if(Times == 0)  // 时间片用完
                                            {
                                                //不存在此种情况
                                                // System.out.println("系统错误");
                                            } 
                                            else    //时间片未用完，应该继续执行2类指令
                                            {
                                                CPU.isFree = false;
                                                // 指令2运行位 置1
                                                CPU.isRUNNING_2 = true;
                                            }
                                            break;

                                        }
                                        case 3:// 3类指令(Output)的第1s
                                        {
                                            // 先执行
                                            // 对于CPU的更改
                                            CPU.isFree = false;
                                            CPU.PSW = CPU.CPU_PSW_Systemstate;    //转换为系统态
                                            CPU.isRUNNING_3 = true;

                                            // 对于时间片的更改 
                                            Times--;

                                            // 输出
                                            String temp = new String(Clock_thread.COUNTTIME + ":[运行进程:" + CPU.process_running_ID    // 进程ID
                                            + ":" + String.valueOf(CPU.PC_num + 1)     // 指令段编号
                                            + "," + String.valueOf(3) // 指令类型编号
                                            + "," + CPU.ins_Address  // 逻辑地址
                                            + "," + String.valueOf(MMU.Address_Transfer(CPU.ins_Address)) +"]\n");  // 物理地址
                                            OtherString.out_string_1 += temp;
                                            ui.textArea_running.setText(OtherString.out_string_1);
                                            ui.textArea_running.setCaretPosition(ui.textArea_running.getText().length());

                                            temp = new String(String.valueOf(Clock_thread.COUNTTIME + 1) + ":进程"+ CPU.process_running_ID +" Output事件发生\n");
                                            OtherString.output_string += temp;
                                            ui.textArea_io.setText(OtherString.output_string);
                                            ui.textArea_io.setCaretPosition(ui.textArea_io.getText().length());
                   

                                            // 执行完后,判断情况
                                            if(Times == 0)// 时间片用完
                                            {
                                                //不存在此种情况
                                                // System.out.println("系统错误");
                                            } else //时间片未用完，应该继续执行3类指令
                                            {
                                                CPU.isFree = false;
                                                // 指令3运行位 置1
                                                CPU.isRUNNING_3 = true;
                                            }
                                            break;
                                        }
                                        default:
                                        {
                                           //  System.out.println("系统错误");
                                            break;
                                        }
                                    }
                                    break;     
                                }
                                case 2:      // 系统错误
                                {
                                    System.out.println("系统错误");
                                    break;     
                                }
                                case 3:      // 来不及执行（2、3、4这类占用2s的指令）
                                {

                                    break;     
                                }
                            }
                        }
                    }
                    else // 就绪队列为空
                    {// 是否有可以进入就绪队列的
                        // 没有可以进入就绪队列的
                        // 判断是否需要进程暂停
                        if(List_Blocked_1.isEmpty() && List_Blocked_2.isEmpty())    // 阻塞队列均为空
                        {
                            if(JCB_manage.JCB_list_doing.isEmpty() && JCB_manage.JCB_list_todo.isEmpty())// JCB全为空
                            {
                                // 标志位全置false，无法继续执行
                                JobIn_thread.flag = false;
                                ProcessScheduling_thread.flag = false;
                                Clock_thread.flag = false;
                                InputBlock_thread.flag = false;
                                OutputBlock_thread.flag = false;

                                // 输出总结
                                String temp = new String (ui.textArea_running.getText() + "\n\n状态统计信息:\n" 
                                + OtherString.out_string_2);
                                if(OtherString.fileway2_4.equals("input2"))// 才存在阻塞队列和指令
                                {
                                    temp += OtherString.out_string_3_1;
                                    for(int i = 0 ; i < OtherString.out_string_3_1_1.size() ; i = i+2)
                                    {
                                        temp += OtherString.out_string_3_1_1.get(i);
                                        if(i != OtherString.out_string_3_1_1.size()-1)temp +='/';
                                    }
                                    temp = temp.substring(0,temp.length() - 1);
                                    temp += "]\n";
                                    temp += OtherString.out_string_3_2;
                                    for(int i = 0 ; i < OtherString.out_string_3_2_1.size() ; i = i +2)
                                    {
                                        temp += OtherString.out_string_3_2_1.get(i);
                                        if(i != OtherString.out_string_3_2_1.size()-1)temp +='/';
                                    }
                                    temp = temp.substring(0,temp.length() - 1);
                                    temp += "]\n";
                                }
                                ui.textArea_running.setText(temp);
                                ui.textArea_running.setCaretPosition(ui.textArea_running.getText().length());   

                                
                                // 更新ui上各个队列信息
                                Check_empty_Ready();
                                Check_empty_Blocked_1();
                                Check_empty_Blocked_2();

                            }
                        }
                        else
                        {
                            Times = Times_first;
                        }
                    }   
                    if(CPU.isFree && Times == Times_first)  //若此时依然空闲
                    {
                        String temp = new String(Clock_thread.COUNTTIME + ":[CPU 空闲]\n");
                        OtherString.out_string_1 += temp;
                        ui.textArea_running.setText(OtherString.out_string_1);
                        ui.textArea_running.setCaretPosition(ui.textArea_running.getText().length());   
                        //System.out.print(temp);
                    }
                }
                else    // CPU不空闲，说明有进程占用PCB
                {// 判断剩余时间是否可以执行指令
                    switch(CPU.Time_is_enough())//时间片是否足够
                    {
                        case -1:    //时间片已经用完
                        {
                            break;
                        }
                        case 0:     // 指令继续执行
                        {
                            switch(PCB_manage.PCB_table.get(PCB_manage.Get_Index_By_JobsID(CPU.process_running_ID)).instructions.get(CPU.PC_num).Instruc_State)
                            {
                                case 0:
                                {
                                    // 不可能出现这种情况
                                    // System.out.println("系统错误");
                                    break;
                                }
                                case 1: // 1类指令的后1s
                                {
                                    // 执行

                                    // 对于CPU的更改
                                    CPU.isFree = false;
                                    CPU.PSW = CPU.CPU_PSW_Userstate;    //转换为用户态
                                    CPU.isRUNNING_1 = true;

                                    
                                    // 对于PCB的更改（执行完了修改）
                                    PCB_manage.PCB_table.get(PCB_manage.Get_Index_By_JobsID(CPU.process_running_ID)).PC++;
                                    PCB_manage.PCB_table.get(PCB_manage.Get_Index_By_JobsID(CPU.process_running_ID)).IR++;

                                    // 对于时间片的更改 
                                    Times--;

                                    // 输出
                                    String temp = new String(Clock_thread.COUNTTIME + ":[运行进程:" + CPU.process_running_ID    // 进程ID
                                    + ":" + String.valueOf(CPU.PC_num + 1)     // 指令段编号
                                    + "," + String.valueOf(1) // 指令类型编号
                                    + "," + CPU.ins_Address  // 逻辑地址
                                            + "," + String.valueOf(MMU.Address_Transfer(CPU.ins_Address)) +"]\n");
                                    OtherString.out_string_1 += temp;
                                    ui.textArea_running.setText(OtherString.out_string_1);
                                    ui.textArea_running.setCaretPosition(ui.textArea_running.getText().length()); 

                                        CPU.isRUNNING_1 = false;
                                            
                                    // 此PCB全部执行完了
                                    if(PCB_manage.PCB_table.get(PCB_manage.Get_Index_By_JobsID(CPU.process_running_ID)).PC
                                        >= PCB_manage.PCB_table.get(PCB_manage.Get_Index_By_JobsID(CPU.process_running_ID)).InstrucNum)
                                    {//PCB撤销
                                        PCB_manage.Cancel_PCB(CPU.process_running_ID);
                                        CPU.isFree = true;
                                        CPU.isRUNNING_1 = false;
                                        break;
                                    }
                                    else// 此PCB还有指令待执行
                                    {
                                        // 执行完后,判断情况
                                        if(Times == 0)// 时间片用完
                                        {
                                            // 进程离开CPU
                                            CPU.CPU_OUT(CPU.process_running_ID);
                                            CPU.isFree = true;
                                            CPU.isRUNNING_1 = false;
                                            break;
                                        } 
                                        else // 还有时间
                                        {// 下一条 指令类型判断
                                            switch(PCB_manage.PCB_table.get(PCB_manage.Get_Index_By_JobsID(CPU.process_running_ID)).instructions.get(CPU.PC_num + 1).Instruc_State)
                                            {
                                                case 0:// 指令类型为0
                                                {
                                                    CPU.isFree = false;
                                                    // 指令1运行位 置0
                                                    CPU.isRUNNING_1 = false;
                                                    CPU.ins_Address++;
                                                    CPU.IR_num++;
                                                    CPU.PC_num++;
                                                    break;
                                                }
                                                case 1:// 指令类型为1
                                                {
                                                    if(Times < 2)  //时间不够
                                                    {
                                                        // 进程离开CPU
                                                        CPU.CPU_OUT(CPU.process_running_ID);
                                                        CPU.isFree = true;
                                                        CPU.isRUNNING_1 = false;
                                                        break;
                                                    }
                                                    else //时间够用
                                                    {
                                                        CPU.isFree = false;
                                                        // 指令1运行位 置0
                                                        CPU.isRUNNING_1 = false;
                                                        CPU.ins_Address++;
                                                        CPU.IR_num++;
                                                        CPU.PC_num++;
                                                        break;
                                                    }
                                                }
                                                case 2:
                                                {
                                                    if(Times < 2)  //时间不够
                                                    {
                                                        // 进程离开CPU
                                                        CPU.CPU_OUT(CPU.process_running_ID);
                                                        CPU.isFree = true;
                                                        CPU.isRUNNING_2 = false;
                                                        break;
                                                    }
                                                    else //时间够用
                                                    {
                                                        CPU.isFree = false;
                                                        // 指令2运行位 置0
                                                        CPU.isRUNNING_2 = false;
                                                        CPU.ins_Address++;
                                                        CPU.IR_num++;
                                                        CPU.PC_num++;
                                                        break;
                                                    }
                                                }
                                                case 3:
                                                {
                                                    if(Times < 2)  //时间不够
                                                    {
                                                        // 进程离开CPU
                                                        CPU.CPU_OUT(CPU.process_running_ID);
                                                        CPU.isFree = true;
                                                        CPU.isRUNNING_3 = false;
                                                        break;
                                                    }
                                                    else //时间够用
                                                    {
                                                        CPU.isFree = false;
                                                        // 指令3运行位 置0
                                                        CPU.isRUNNING_3 = false;
                                                        CPU.ins_Address++;
                                                        CPU.IR_num++;
                                                        CPU.PC_num++;
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    break;
                                }
                                case 2: // 2类指令的后1s
                                {
                                    // 执行

                                    // 对于CPU的更改
                                    CPU.isFree = false;
                                    CPU.PSW = CPU.CPU_PSW_Systemstate;    //转换为系统态
                                    CPU.isRUNNING_2 = true;
                                    
                                    // 对于PCB的更改（执行完了修改）
                                    PCB_manage.PCB_table.get(PCB_manage.Get_Index_By_JobsID(CPU.process_running_ID)).PC++;
                                    PCB_manage.PCB_table.get(PCB_manage.Get_Index_By_JobsID(CPU.process_running_ID)).IR++;
                                    
                                    // PCB优先级修改
                                    if(PCB_manage.PCB_table.get(PCB_manage.Get_Index_By_JobsID(CPU.process_running_ID)).Priority > 3)
                                    {
                                        PCB_manage.PCB_table.get(PCB_manage.Get_Index_By_JobsID(CPU.process_running_ID)).Priority -- ;
                                    }

                                    // 对于时间片的更改 
                                    Times--;

                                    String temp = new String(Clock_thread.COUNTTIME + "," + String.valueOf(CPU.process_running_ID));
                                    // 进程阻塞1字符串修改
                                    OtherString.out_string_3_1_1.add(temp);

                                    // 阻塞进程
                                    PCB_manage.Block_PCB(CPU.process_running_ID,1);

                                    CPU.isFree = true;
                                    CPU.isRUNNING_2 = false;
                                    break;
                                }
                                case 3:// 3类指令的后1s
                                {
                                    // 执行

                                    // 对于CPU的更改
                                    CPU.isFree = false;
                                    CPU.PSW = CPU.CPU_PSW_Systemstate;    //转换为系统态
                                    CPU.isRUNNING_3 = true;
                                    
                                    // 对于PCB的更改（执行完了修改）
                                    PCB_manage.PCB_table.get(PCB_manage.Get_Index_By_JobsID(CPU.process_running_ID)).PC++;
                                    PCB_manage.PCB_table.get(PCB_manage.Get_Index_By_JobsID(CPU.process_running_ID)).IR++;
                                    
                                    // PCB优先级修改
                                    if(PCB_manage.PCB_table.get(PCB_manage.Get_Index_By_JobsID(CPU.process_running_ID)).Priority > 3)
                                    {
                                        PCB_manage.PCB_table.get(PCB_manage.Get_Index_By_JobsID(CPU.process_running_ID)).Priority-- ;
                                    }

                                    // 对于时间片的更改 
                                    Times--;

                                    String temp = new String(Clock_thread.COUNTTIME + "," + String.valueOf(CPU.process_running_ID));
                                    // 进程阻塞2字符串修改
                                    OtherString.out_string_3_2_1.add(temp);

                                    // 阻塞进程
                                    PCB_manage.Block_PCB(CPU.process_running_ID,2);

                                    CPU.isFree = true;
                                    CPU.isRUNNING_3 = false;
                                    break;
                                }
                            }

                            break;
                        }
                        case 1:  // 可以开始执行（0、1、2、3类）
                        {// 即将执行的指令
                            switch(PCB_manage.PCB_table.get(PCB_manage.Get_Index_By_JobsID(CPU.process_running_ID)).instructions.get(CPU.PC_num).Instruc_State)
                            {
                                case 0: // 0类指令，耗时1s
                                {
                                    CPU.isFree = false;
                                    // 输出
                                    String temp = new String(Clock_thread.COUNTTIME + ":[运行进程:" + CPU.process_running_ID    // 进程ID
                                    + ":" + String.valueOf(CPU.PC_num + 1)     // 指令段编号
                                    + "," + String.valueOf(PCB_manage.PCB_table.get(PCB_manage.Get_Index_By_JobsID(CPU.process_running_ID)).instructions.get(CPU.PC_num).Instruc_State) // 指令类型编号
                                    + "," + CPU.ins_Address  // 逻辑地址
                                    + "," + String.valueOf(MMU.Address_Transfer(CPU.ins_Address)) +"]\n");  // 物理地址
                                    OtherString.out_string_1 += temp;
                                    ui.textArea_running.setText(OtherString.out_string_1);
                                    ui.textArea_running.setCaretPosition(ui.textArea_running.getText().length());   

                                    // 对于PCB的更改
                                    PCB_manage.PCB_table.get(PCB_manage.Get_Index_By_JobsID(CPU.process_running_ID)).PC++;
                                    PCB_manage.PCB_table.get(PCB_manage.Get_Index_By_JobsID(CPU.process_running_ID)).IR++;

                                    // 对于CPU的更改
                                    CPU.PSW = CPU.CPU_PSW_Userstate;    //转换为用户态
                                    // 对于时间片的更改 
                                    Times--;
    
                                    // 此进程全部执行完了
                                    if(PCB_manage.PCB_table.get(PCB_manage.Get_Index_By_JobsID(CPU.process_running_ID)).PC
                                     == PCB_manage.PCB_table.get(PCB_manage.Get_Index_By_JobsID(CPU.process_running_ID)).InstrucNum)
                                    {
                                        //PCB撤销
                                        PCB_manage.Cancel_PCB(CPU.process_running_ID);

                                        // 设置CPU空闲
                                        CPU.isFree = true;
                                        break;
                                    }

                                    //判断情况
                                    if(Times == 0)// 时间片用完
                                    {
                                        // 进程离开CPU
                                        CPU.CPU_OUT(CPU.process_running_ID);
                                        // 设置CPU空闲
                                        CPU.isFree = true;
                                    }
                                    else //时间片未用完
                                    {
                                        switch(PCB_manage.PCB_table.get(PCB_manage.Get_Index_By_JobsID(CPU.process_running_ID)).instructions.get(CPU.PC_num).Instruc_State)
                                        {
                                            case 0:// 指令类型为0
                                            {
                                                CPU.isFree = false;
                                                CPU.IR_num++;
                                                CPU.PC_num++;
                                                CPU.ins_Address++;
                                                break;
                                            }
                                            case 1:// 指令类型为1
                                            {
                                                if(Times < 2)// 时间片不够用
                                                {
                                                    // 进程离开CPU
                                                    CPU.CPU_OUT(CPU.process_running_ID);
                                                    CPU.isFree = true;
                                                    CPU.isRUNNING_1 = false;
                                                    break;
                                                }
                                                else //够用
                                                {
                                                    CPU.isRUNNING_1 = false;
                                                    CPU.isFree = false;
                                                    CPU.IR_num++;
                                                    CPU.PC_num++;
                                                    CPU.ins_Address++;
                                                    break;
                                                }
                                            }
                                            case 2:// 指令类型为2
                                            {
                                                if(Times < 2)// 时间片不够用
                                                {
                                                    // 进程离开CPU
                                                    CPU.CPU_OUT(CPU.process_running_ID);
                                                    CPU.isFree = true;
                                                    CPU.isRUNNING_2 = false;
                                                    break;
                                                }
                                                else //够用
                                                {
                                                    CPU.isFree = false;
                                                    CPU.IR_num++;
                                                    CPU.PC_num++;
                                                    CPU.isRUNNING_2 = false;
                                                    CPU.ins_Address++;
                                                    break;
                                                }
                                            }
                                            case 3:
                                            {
                                                if(Times < 2)// 时间片不够用
                                                {
                                                    // 进程离开CPU
                                                    CPU.CPU_OUT(CPU.process_running_ID);
                                                    CPU.isFree = true;
                                                    CPU.isRUNNING_3 = false;
                                                    break;
                                                }
                                                else //够用
                                                {
                                                    CPU.isFree = false;
                                                    CPU.IR_num++;
                                                    CPU.PC_num++;
                                                    CPU.isRUNNING_3 = false;
                                                    CPU.ins_Address++;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    break;

                                }
                                case 1: // 1类指令，耗时2s
                                {// 先执行
                                    // 对于CPU的更改
                                    CPU.isFree = false;
                                    CPU.PSW = CPU.CPU_PSW_Userstate;    //转换为用户态
                                    CPU.isRUNNING_1 = true;

                                    // 对于时间片的更改 
                                    Times--;

                                    // 输出
                                    String temp = new String(Clock_thread.COUNTTIME + ":[运行进程:" + CPU.process_running_ID    // 进程ID
                                    + ":" + String.valueOf(CPU.PC_num + 1)     // 指令段编号
                                    + "," + String.valueOf(1) // 指令类型编号
                                    + "," + CPU.ins_Address  // 逻辑地址
                                            + "," + String.valueOf(MMU.Address_Transfer(CPU.ins_Address)) +"]\n");
                                    OtherString.out_string_1 += temp;
                                    ui.textArea_running.setText(OtherString.out_string_1);
                                    ui.textArea_running.setCaretPosition(ui.textArea_running.getText().length());   

                                    // 执行完后,判断情况
                                    if(Times == 0)// 时间片用完
                                    {
                                        // System.out.println("系统错误");
                                    } 
                                    else //时间片未用完，应该继续执行1类指令
                                    {
                                        CPU.isFree = false;
                                        // 指令1运行位 置1
                                        CPU.isRUNNING_1 = true;
                                    }
                                    break;
                                }
                                case 2: //2类指令(Input)的第1s
                                {
                                    // 先执行
                                    // 对于CPU的更改
                                    CPU.isFree = false;
                                    CPU.PSW = CPU.CPU_PSW_Systemstate;    //转换为系统态
                                    CPU.isRUNNING_2 = true;

                                    // 对于时间片的更改 
                                   Times--;

                                    // 输出
                                    String temp = new String(Clock_thread.COUNTTIME + ":[运行进程:" + CPU.process_running_ID    // 进程ID
                                    + ":" + String.valueOf(CPU.PC_num + 1)     // 指令段编号
                                    + "," + String.valueOf(2) // 指令类型编号
                                    + "," + CPU.ins_Address  // 逻辑地址
                                    + "," + String.valueOf(MMU.Address_Transfer(CPU.ins_Address)) +"]\n");  // 物理地址
                                    OtherString.out_string_1 += temp;
                                    ui.textArea_running.setText(OtherString.out_string_1);
                                    ui.textArea_running.setCaretPosition(ui.textArea_running.getText().length());

                                    temp = new String(String.valueOf(Clock_thread.COUNTTIME + 1) + ":进程"+ CPU.process_running_ID +" Input事件发生\n");
                                    OtherString.output_string += temp;
                                    ui.textArea_io.setText(OtherString.output_string);
                                    ui.textArea_io.setCaretPosition(ui.textArea_io.getText().length());
                   

                                    // 执行完后,判断情况
                                    if(Times == 0)  // 时间片用完
                                    {
                                         //不存在此种情况
                                        // System.out.println("系统错误");
                                    } 
                                    else    //时间片未用完，应该继续执行2类指令
                                    {
                                        CPU.isFree = false;
                                        // 指令2运行位 置1
                                        CPU.isRUNNING_2 = true;
                                    }
                                    break;

                                }
                                case 3:// 3类指令(Output)的第1s
                                {
                                    // 先执行
                                    // 对于CPU的更改
                                    CPU.isFree = false;
                                    CPU.PSW = CPU.CPU_PSW_Systemstate;    //转换为系统态
                                    CPU.isRUNNING_3 = true;

                                    // 对于时间片的更改 
                                    Times--;

                                    // 输出
                                    String temp = new String(Clock_thread.COUNTTIME + ":[运行进程:" + CPU.process_running_ID    // 进程ID
                                    + ":" + String.valueOf(CPU.PC_num + 1)     // 指令段编号
                                    + "," + String.valueOf(3) // 指令类型编号
                                    + "," + CPU.ins_Address  // 逻辑地址
                                   + "," + String.valueOf(MMU.Address_Transfer(CPU.ins_Address)) +"]\n");  // 物理地址
                                   OtherString.out_string_1 += temp;
                                    ui.textArea_running.setText(OtherString.out_string_1);
                                    ui.textArea_running.setCaretPosition(ui.textArea_running.getText().length());

                                    temp = new String(String.valueOf(Clock_thread.COUNTTIME + 1) + ":进程"+ CPU.process_running_ID +" Output事件发生\n");
                                    OtherString.output_string += temp;
                                    ui.textArea_io.setText(OtherString.output_string);
                                    ui.textArea_io.setCaretPosition(ui.textArea_io.getText().length());
                   

                                    // 执行完后,判断情况
                                    if(Times == 0)// 时间片用完
                                    {
                                        //不存在此种情况
                                        // System.out.println("系统错误");
                                    } else //时间片未用完，应该继续执行3类指令
                                    {
                                        CPU.isFree = false;
                                        // 指令3运行位 置1
                                        CPU.isRUNNING_3 = true;
                                    }
                                    break;
                                }
                            
                            }
                          
                            break;
                        }
                        case 2: // 系统出错
                        {
                            // System.out.println("系统出错！！！！！！！！！！！");
                            break;
                        }
                        case 3: //来不及执行123类指令（仅有1s时）
                        {
                            switch(PCB_manage.PCB_table.get(PCB_manage.Get_Index_By_JobsID(CPU.process_running_ID)).instructions.get(CPU.PC_num).Instruc_State)
                            {
                                case 0:
                                {
                                    // 不存在此种情况
                                    // System.out.println("系统错误!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                    break;
                                }
                                case 1:
                                {//直接出队
                                    // 进程离开CPU
                                    Clock_thread.COUNTTIME--;
                                    // System.out.println("系统错误!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                    CPU.CPU_OUT(CPU.process_running_ID);
                                    CPU.isFree = true;
                                    CPU.isRUNNING_1 = false;
                                    break;
                                }
                                case 2:
                                {
                                    // // 进程离开CPU
                                    Clock_thread.COUNTTIME--;
                                    // System.out.println("系统错误!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                    CPU.CPU_OUT(CPU.process_running_ID);
                                    CPU.isFree = true;
                                    CPU.isRUNNING_2 = false;
                                    break;
                                }
                                case 3:
                                {
                                    //进程离开CPU
                                    Clock_thread.COUNTTIME--;
                                    // System.out.println("系统错误!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                    CPU.CPU_OUT(CPU.process_running_ID);
                                    CPU.isFree = true;
                                    CPU.isRUNNING_3 = false;
                                    break;
                                }
                                default:
                                {
                                    // System.out.println("");
                                    break;
                                }

                            }
                            break;
                        }
                    }


                }

                    


                
                // //System.out.println(Clock_thread.COUNTTIME+":[PST run]");

                // 更新ui上各个队列信息
                Check_empty_Ready();
                Check_empty_Blocked_1();
                Check_empty_Blocked_2();

                
                Kernel.inputCondition.signal();     // 唤醒线程input
            }
            catch(Exception e){e.printStackTrace();}
            finally
            {
                Kernel.lock.lock();       // 把锁释放掉
            }
        }
    }
    }
    
    



    // 初始化
    public ProcessScheduling_thread()
    {    // 就绪队列
        List_Ready = new ArrayList<mReady>();
        Ready_num = 0;

        // 阻塞队列1(键盘输入阻塞队列)
        List_Blocked_1 = new ArrayList<mBlocked_1>();
        Blocked_1_num = 0;

        // 阻塞队列2(显示器输出阻塞队列)
        List_Blocked_2 = new ArrayList<mBlocked_2>();
        Blocked_2_num = 0;

        // 初始静态时间片
        Times = Times_first;

    }

    // 进程加入就绪队列
    public static boolean Into_List_Ready(int id)
    {
        // id为进程pcb中的进程号
        int index = PCB_manage.Get_Index_By_JobsID(id);

        if(PCB_manage.PCB_table.get(index).PC != 0)
        {
            String temp = new String(Clock_thread.COUNTTIME + ":[重新进入就绪队列:" + String.valueOf(id)    // 进程 ID
            + "," + (PCB_manage.PCB_table.get(index).InstrucNum - PCB_manage.PCB_table.get(index).PC) + "]\n");        // 待执行的指令数
            OtherString.out_string_1 += temp;
            ui.textArea_running.setText(OtherString.out_string_1);
            ui.textArea_running.setCaretPosition(ui.textArea_running.getText().length());   
            
        }

        
        mReady x = new mReady(Ready_num+1, Clock_thread.COUNTTIME, id);
        List_Ready.add(x);

        // 当前就绪队列中PCB数量
        Ready_num++;
        if(Get_ReadyIndex_By_JobsID(id) == -1)return false;
        return true;
    }
    // 进程离开就绪队列
    public static boolean Out_List_Ready(int id)
    {// i为进程pcb中的进程号
        if(List_Ready.size() == 0)// 就绪队列为空时
        {
            return false; // 离开失败
        }

        // index为进程在就绪队列中的下标
        int index = Get_ReadyIndex_By_JobsID(id);

        // 当前就绪队列中PCB数量
        Ready_num--;

        // 出队
        List_Ready.remove(index);
        // System.out.println("出队");
        return true;
    }
    // 在就绪队列中根据进程ID寻找进程PCB在就绪队列中的下标
    public static int Get_ReadyIndex_By_JobsID(int id)
    {
        int index = -1;
        for(int i = 0 ; i < List_Ready.size(); i++)
        {
            if(List_Ready.get(i).ProID == id)index = i;
        }
        return index;// -1表示没找到
    }
    // 在就绪队列中根据进程入队时间最早寻找进程PCB的ID
    public static int Get_ReadyID_By_Time()
    {
        int index = -1;
        int max = 9999;
        if(List_Ready.size() == 0)return -1;
        for(int i = 0 ; i < List_Ready.size(); i++)
        {
            if(List_Ready.get(i).RqTimes < max)
            {
                index = i;
                max = List_Ready.get(i).RqTimes;
            }
        }
        return List_Ready.get(index).ProID;// -1表示没找到
    }
    // 在就绪队列中根据优先级与入队时间寻找进程PCB的ID
    public static int Get_ReadyID_By_Time_and_Priority()
    {
        int index = -1;
        int priority = 0;       //优先级为5，4，3，时间片对应3，4，5s
        int max = 9999;     // 入队时间

        //先获取队列中的最高优先级
        for(int i = 0 ; i < List_Ready.size() ; i++)
        {
            // pri 为当前进程的优先级
            int pri = PCB_manage.PCB_table.get(PCB_manage.Get_Index_By_JobsID(List_Ready.get(i).ProID)).Priority;
            if(pri > priority)priority = pri;
        }
        // 此时队列中的最高优先级为priority

        //再找该优先级中入队时间最早的
        for(int i = 0 ; i < List_Ready.size(); i++)
        {
            // pri 为当前进程的优先级
            int pri = PCB_manage.PCB_table.get(PCB_manage.Get_Index_By_JobsID(List_Ready.get(i).ProID)).Priority;

            //小于当前队列中最高优先级
            if(pri < priority)continue;
            else
            {
                if(List_Ready.get(i).RqTimes < max)
                {
                    index = i;
                    max = List_Ready.get(i).RqTimes;
                }
            }
        }
        return List_Ready.get(index).ProID;// -1表示没找到
    }
    // 就绪队列现状
    public static boolean Check_empty_Ready()
    {
        if(List_Ready.size() == 0)
        {
            ui.textArea_ready.setText("");
            return false;
        }
        else
        {
            OtherString.out_string_4 = new String();
            String temp = new String();
            for(int i = 0; i < List_Ready.size() ; i++)
            {
                temp = new String("进程 " + List_Ready.get(i).ProID + '\n');
                OtherString.out_string_4 += temp;
            }
            ui.textArea_ready.setText(OtherString.out_string_4);
            return true;
        }
    }


    // 进程加入阻塞队列1(键盘输入阻塞队列)
    public static boolean Into_Blocked_1(int id)
    {// i为进程pcb中的进程号
        mBlocked_1 x = new mBlocked_1(Blocked_1_num+1, Clock_thread.COUNTTIME, id);
        List_Blocked_1.add(x);

        // 当前队列中PCB数量
        Blocked_1_num++;
        return true;
    }
    // 进程离开阻塞队列1(键盘输入阻塞队列)
    public static boolean Out_Blocked_1(int id)
    {// i为进程pcb中的进程号
        if(List_Blocked_1.size() == 0)// 队列为空时
        {
            return false; // 离开失败
        }

        // index为进程在阻塞队列1(键盘输入阻塞队列)中的下标
        int index = Get_Blocked_1_Index_By_JobsID(id);

        // 当前队列中PCB数量
        Blocked_1_num--;

        // 出队
        List_Blocked_1.remove(index);
        return true;
    }
    // 在阻塞队列1中根据进程ID寻找进程PCB在阻塞队列1(键盘输入阻塞队列)中的下标
    public static int Get_Blocked_1_Index_By_JobsID(int id)
    {
        int index = -1;
        for(int i = 0 ; i < List_Blocked_1.size(); i++)
        {
            if(List_Blocked_1.get(i).ProID == id)index = i;
        }
        return index;// -1表示没找到
    }
    // 阻塞队列1(键盘输入阻塞队列)现状
    public static boolean Check_empty_Blocked_1()
    {
        OtherString.out_string_5_1 = new String();
        if(List_Blocked_1.size() == 0)
        {
            ui.textArea_block_1.setText("");
            return false;
        }
        else
        {
            String temp = new String();
            for(int i = 0; i < List_Blocked_1.size() ; i++)
            {
                temp = new String("进程 " + List_Blocked_1.get(i).ProID + '\n');
                OtherString.out_string_5_1 += temp;
            }
            ui.textArea_block_1.setText(OtherString.out_string_5_1);
            return true;
        }
    }


    // 进程加入阻塞队列2(显示器输出阻塞队列)
    public static boolean Into_Blocked_2(int id)
    {// i为进程pcb中的进程号
        mBlocked_2 x = new mBlocked_2(Blocked_2_num+1, Clock_thread.COUNTTIME, id);
        List_Blocked_2.add(x);

        // 当前队列中PCB数量
        Blocked_2_num++;
        return true;
    }
    // 进程离开阻塞队列2(显示器输出阻塞队列)
    public static boolean Out_Blocked_2(int id)
    {// i为进程pcb中的进程号
        if(List_Blocked_2.size() == 0)// 队列为空时
        {
            return false; // 离开失败
        }

        // index为进程在阻塞队列2(显示器输出阻塞队列)中的下标
        int index = Get_Blocked_2_Index_By_JobsID(id);

        // 当前队列中PCB数量
        Blocked_2_num--;

        // 出队
        List_Blocked_2.remove(index);
        return true;
    }
    // 在阻塞队列2中根据进程ID寻找进程PCB在阻塞队列2(显示器输出阻塞队列)中的下标
    public static int Get_Blocked_2_Index_By_JobsID(int id)
    {
        int index = -1;
        for(int i = 0 ; i < List_Blocked_2.size(); i++)
        {
            if(List_Blocked_2.get(i).ProID == id)index = i;
        }
        return index;// -1表示没找到
    }
     // 阻塞队列2(显示器输出阻塞队列)现状
    public static boolean Check_empty_Blocked_2()
    {
        if(List_Blocked_2.size() == 0)
        {
            ui.textArea_block_2.setText("");
            return false;
        }
        else
        {
            OtherString.out_string_5_2 = new String();
            String temp = new String();
            for(int i = 0; i < List_Blocked_2.size() ; i++)
            {
                temp = new String("进程 " + List_Blocked_2.get(i).ProID + '\n');
                OtherString.out_string_5_2 += temp;
            }
            ui.textArea_block_2.setText(OtherString.out_string_5_2);
            return true;
        }
    }


    // 离开CPU的时候改指令数！！！

    // 将进程装入CPU
    public static boolean Into_CPU(int id)
    {// i为要装入CPU的进程pcb中的进程号

        // 进程在PCB表中的下标index1
        int index1 = PCB_manage.Get_Index_By_JobsID(id);
        if(index1 == -1)
        {
            // System.out.println("该进程不存在于系统PCB表中!!!!!!!!!!!!!!");
            return false;
        }

        // 进程在就绪队列中的下标不存在
        if(Get_ReadyIndex_By_JobsID(id) == -1)
        {
            // System.out.println("该进程不存在于就绪队列表中!!!!!!!!!!!!!!");
            return false;
        }

        // 从就绪队列中出列
        Out_List_Ready(id);
        
        // 装入CPU
        return true;
    }
 
}


