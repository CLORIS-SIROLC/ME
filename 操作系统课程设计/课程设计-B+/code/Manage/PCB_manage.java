package Manage;

import java.util.ArrayList;
import java.util.List;

import Hardware.MMU;
import Hardware.Memory;
import Kernel.Clock_thread;
import Kernel.ProcessScheduling_thread;
import Others.OtherString;
import UI.ui;

// 进程管理
public class PCB_manage {

    // 系统PCB表
    public static List<PCB> PCB_table;

    // 系统PCB表的最大表项
    public static final int PCB_table_max_number = 10;






    // 初始化
    public PCB_manage()
    {
        PCB_table = new ArrayList<PCB>();
    }
    
    // 检验系统PCB表是否已满（满了返回T，没满返回F）
    public static boolean isFull()
    {
        if(PCB_table.size() >= PCB_table_max_number)return true;
        else return false;
    }

    // 在PCB表中根据进程ID寻找进程PCB在表中的下标
    public static int Get_Index_By_JobsID(int id)
    {
        int index = -1;
        for(int i = 0 ; i < PCB_table.size(); i++)
        {
            if(PCB_table.get(i).ProID == id)index = i;
        }

        return index;// -1表示没找到
    }




    
    // 进程创建原语
    public static boolean Create_PCB(JCB jcb)
    {   // 返回true表示创建成功
        // 返回false表示创建失败

        // 当内存中需求的盘块数大于已有空闲盘块，不可转换为PCB
        if(((jcb.InstrucNum / Memory.block_page_size) + 1) > Memory.Free_block_num())return false;
        
        // PCB入队（系统PCB表或PCB等待表）
        // 由于不考虑OS内核的内存,不用考虑PCB所占用的内存,因此只要系统PCB表未满,均可以创建
        if(isFull())   
        {// 系统PCB表已满时,不可转换为PCB
            //System.out.println("系统PCB表已满");
            return false;
        }
        else{// 系统PCB表有空余表项时

            // 创建PCB实例(删除对应的JCB实例)
            // PCB申请内存存放指令
            PCB pcb = new PCB(jcb);
            pcb.Priority = 5 ;

            // 加入系统PCB表
            PCB_table.add(pcb);
            if(PCB_table.indexOf(pcb)==-1)
            System.out.println("系统PCB表未装入");

            // 进程PCB状态改为就绪，进程号加入就绪队列
            if(ProcessScheduling_thread.Into_List_Ready(pcb.ProID))
            {// 成功加入就绪队列
                

                // PCB转换为就绪态
                pcb.PSW = PCB.PSW_READY;

                String temp = new String(String.valueOf(Clock_thread.COUNTTIME) + ":[进入就绪队列:"
                    + String.valueOf(pcb.ProID) + ":" + String.valueOf(pcb.InstrucNum) + "]\n");
                //System.out.print(temp);
                OtherString.out_string_1 += temp;
                ui.textArea_running.setText(OtherString.out_string_1);
                ui.textArea_running.setCaretPosition(ui.textArea_running.getText().length());   


                OtherString.out_string_all += temp;
            }
            return true;
        }
    }
    // 进程撤销原语
    public static boolean Cancel_PCB(int id)
    {// id为要撤销的进程的编号
        // 返回true表示创建成功
        // 返回false表示创建失败

        // 进程在系统PCB表中下标index
        int index = Get_Index_By_JobsID(id);
        if(index == -1)return false;    // 没找到

        // 作业/进程调度事件信息输出
        String temp = new String(Clock_thread.COUNTTIME + ":[终止" + id + "]\n");
        OtherString.out_string_1 += temp;
        ui.textArea_running.setText(OtherString.out_string_1);
        ui.textArea_running.setCaretPosition(ui.textArea_running.getText().length());   

        System.out.print(temp);

        // 状态统计信息输出
        temp = new String(Clock_thread.COUNTTIME + ":[" + id + ":" + String.valueOf(PCB_table.get(index).J_InTimes_0) // 作业请求时间
            + "+" + String.valueOf(PCB_table.get(index).J_InTimes_1) // 作业进入时间
            + "+" + String.valueOf(Clock_thread.COUNTTIME - PCB_table.get(index).J_InTimes_1) + "]\n"); // 总运行时间
        OtherString.out_string_2 += temp;
        // System.out.print(temp);

        // 释放进程空间
        // address为进程的逻辑地址始址（页号）,instru为指令条数
        int address = PCB_table.get(index).logic_start_address;
        int instru = PCB_table.get(index).InstrucNum;
        int x = 0;  // 要清除的盘块数
        if(instru % Memory.block_page_size == 0)
        {
            x = (instru / Memory.block_page_size);
        }
        else
        {
            x = (instru / Memory.block_page_size) + 1;
        }
        for(int i = 0 ; i < x; i++)
        {
            Memory.Empty_Page(MMU.Virtual_Translate(address + i));
        }

        // 将进程从系统PCB表删除
        PCB_table.remove(index);
        
        return true;
    }

    // 进程阻塞原语
    public static boolean Block_PCB(int id ,int block_num)
    {// id为阻塞的进程的编号, block_num为阻塞队列编号

        // 进程在系统PCB表中下标index
        int index = Get_Index_By_JobsID(id);
        if((block_num != 1) && (block_num != 2))
        {
            // System.out.println("阻塞队列编号输入错误！！！！！！");
            return false;
        }


        //离开CPU

        // 进程状态（PSW）转换为阻塞态
        PCB_table.get(index).PSW = PCB.PSW_BLOCKED;
        if(block_num == 1)
        {// 进程加入阻塞队列1(键盘输入阻塞队列)
            ProcessScheduling_thread.Into_Blocked_1(id);
        }
        else if(block_num == 2)
        {// 进程加入阻塞队列2(显示器输出阻塞队列)
            ProcessScheduling_thread.Into_Blocked_2(id);
        }

        String temp = new String(Clock_thread.COUNTTIME + ":[阻塞进程:" + String.valueOf(block_num)  // 阻塞队列编号
        + "," + String.valueOf(id) + "]\n");
        OtherString.out_string_1 += temp;
        ui.textArea_running.setText(OtherString.out_string_1);
        ui.textArea_running.setCaretPosition(ui.textArea_running.getText().length());   

        // System.out.print(temp);

        temp = new String(Clock_thread.COUNTTIME + "," + String.valueOf(id));    // 进入时间,进程 ID
        if(block_num == 1)
        {OtherString.out_string_3_1_1.add(temp);}
        else 
        {OtherString.out_string_3_2_1.add(temp);}

        return true;
    }
    // 进程唤醒原语
    public static boolean Wakeup_PCB(int id ,int block_num)
    {// id为要唤醒的进程的编号, block_num为阻塞队列编号
        // 进程在系统PCB表中下标index
        int index = Get_Index_By_JobsID(id);
        
        if((block_num != 1) && (block_num != 2))
        {
            // System.out.println("阻塞队列编号输入错误！！！！！！");
            return false;
        }

        // 进程状态（PSW）转换为就绪态
        PCB_table.get(index).PSW=PCB.PSW_READY;


        // 从阻塞队列出列
        if(block_num == 1)
        {// 进程离开阻塞队列1(键盘输入阻塞队列)
            ProcessScheduling_thread.Out_Blocked_1(id);
        }
        else if(block_num == 2)
        {// 进程离开阻塞队列2(显示器输出阻塞队列)
            ProcessScheduling_thread.Out_Blocked_2(id);
        }

        // 重新加入就绪队列
        ProcessScheduling_thread.Into_List_Ready(id);
        // String temp = new String(Clock_thread.COUNTTIME + ":[重新进入就绪队列:" + String.valueOf(id)    // 进程 ID
        // + "," + (PCB_table.get(index).InstrucNum - PCB_table.get(index).PC) + "]\n");        // 待执行的指令数
        // OtherString.out_string_1 += temp;
        // ui.textArea_running.setText(OtherString.out_string_1);
        // ui.textArea_running.setCaretPosition(ui.textArea_running.getText().length());   

        // System.out.print(temp);

        return true;
    }

}


