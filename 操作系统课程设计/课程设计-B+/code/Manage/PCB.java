package Manage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import Hardware.MMU;
import Hardware.Memory;
import Kernel.Clock_thread;
import Others.OtherString;
import UI.ui;

//进程控制块PCB类的设计
public class PCB {

    // 进程编号（值分别为 1,2,3,4,5,6，。。）
    public int ProID = 0;

    // 进程优先级（Priority）————随机生成[1-5]整数优先数，优先数越小，优先级越大
    public int Priority = 0;

    // 作业请求时间，单位为秒
    public int J_InTimes_0;

    // 作业进入时间，单位为秒
    public int J_InTimes_1;

    // 进程创建时间（InTimes）————由仿真时钟开始计时
    public int InTimes = 0;

    // 进程结束时间（EndTimes）————显示仿真时钟的时间
    public int EndTimes = 0;

    // 进程状态（PSW）————保存该进程当前状态：新建（0）、就绪（1）、运行（2）、阻塞（3）、挂起（4）、终止（5）
    public int PSW = 0;
    // 为了方便调用，增加程序可读性，则：
    public static final int PSW_NEW = 0;  // 新建
    public static final int PSW_READY = 1;  // 就绪
    public static final int PSW_RUNNING = 2;    // 运行
    public static final int PSW_BLOCKED = 3;  // 阻塞
   // public static final int PSW_SUSPEND = 4;  // 挂起
    public static final int PSW_EXIT = 5;  // 终止

    // 进程包含的指令数目（InstrucNum）
    public int InstrucNum = 0;

    // 程序计数器信息（PC）正在执行的指令编号————初始为0
    public int PC = 1;

    // 指令寄存器信息（IR）下一条将执行的指令编号————初始为1，最终为0
    public int IR = 2;

    // 指令集
    public List<INSTRUCTION> instructions;

    // ★进程运行时间（RunTimes）；统计记录进程开始运行时间、时长，时间由仿真时钟提供
    public int RunTimes = 0;

    // ★进程周转时间（TurnTimes）；
    public int TurnTimes = 0;

    // 进程的逻辑地址始(页号)
    public int logic_start_address;





    // PCB类实例的初始化（新建）
    public PCB(JCB jcb)
    {
        if(JCB_manage.JCB_list_todo.indexOf(jcb)==-1)System.out.println("不存在该JCB！");
        // 进程ProID编号沿用作业编号JobsID
        ProID = jcb.JobsID;

        // 进程创建时间InTimes为仿真时钟当前时间
        InTimes = Clock_thread.COUNTTIME;

        // 作业请求时间，单位为秒
        J_InTimes_0 = jcb.InTimes_0;

        jcb.InTimes_1 = Clock_thread.COUNTTIME;
        
        // 作业进入时间，单位为秒
        J_InTimes_1 = jcb.InTimes_1;

        // 进程优先级Priority————随机生成[1-5]整数优先数，优先数越小，优先级越大
        try{
            // 随机数实例
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            // 使用系统时钟做种子
            secureRandom.setSeed(System.currentTimeMillis());
            Priority = 1 + secureRandom.nextInt(5);
        }
        catch(Exception e){e.printStackTrace();}
        Priority = 5;   // 为了多级反馈队列算法，全部设为5

        // 进程状态PSW————新建（0）
        PSW = PSW_NEW;

        // 程序计数器信息PC正在执行的指令编号为0（新建）
        PC = 0;

        // 指令寄存器信息IR,下一条将执行的指令编号为1
        IR = 1;
        
        // ★进程已运行时间RunTimes
        RunTimes = 0;

        // ★进程周转时间TurnTimes
        TurnTimes = 0;

        // 进程结束时间EndTimes———— -1表示暂未结束
        EndTimes = -1;

        // 进程包含的指令数目InstrucNum
        InstrucNum = jcb.InstrucNum;

        //申请内存
        logic_start_address = Memory.ASK_Memory(InstrucNum);

        // 指令的导入
        int num = 0;
        instructions = new ArrayList<INSTRUCTION>();
        String filename = new String(String.valueOf(jcb.JobsID) + ".txt");
        File file = new File(OtherString.fileway2_2 + filename);
        BufferedReader reader = null;
        String[] x;             
        try 
        {
            // 指令集的载入（读取文件）
            reader = new BufferedReader(new FileReader(file));
            String temp = null;
            while ((temp = reader.readLine()) != null) {
                temp = temp.replace(" " , "");  // 避免空格影响字符串格式
                x = temp.split(",");
                INSTRUCTION temp_ins = new INSTRUCTION(Integer.parseInt(x[0]), Integer.parseInt(x[1]));
                //获取指令的地址
                temp_ins.L_Address = logic_start_address * MMU.page_size_B + num * MMU.ins_size_B;
                // System.out.println(temp_ins.L_Address);
                num++;
                instructions.add(temp_ins);
                if(num >= InstrucNum){break;}   // 避免多余的空行干扰指令集的读取
            }
        }
        catch (Exception e) {e.printStackTrace();}   
        

        String temp = new String(String.valueOf(Clock_thread.COUNTTIME) + ":[创建进程:" 
            + String.valueOf(ProID) + "," + String.valueOf(MMU.Virtual_Translate(logic_start_address) * 10) 
            + ",首次适应动态分区内存分配]\n");
        //System.out.print(temp);
        OtherString.out_string_1 += temp;
        ui.textArea_running.setText(OtherString.out_string_1);
        ui.textArea_running.setCaretPosition(ui.textArea_running.getText().length());   
        OtherString.out_string_all += temp;

        // 把已经转化为PCB的JCB从todo列表或doing列表删除
        if(JCB_manage.Get_Index_By_JobsID(jcb.JobsID,0) != -1)
        {// 存在于todo表
            JCB_manage.JCB_list_todo.remove(JCB_manage.Get_Index_By_JobsID(jcb.JobsID,0)); 
        }
        else if(JCB_manage.Get_Index_By_JobsID(jcb.JobsID,1) != -1)
        {// 存在于doing表
            JCB_manage.JCB_list_doing.remove(JCB_manage.Get_Index_By_JobsID(jcb.JobsID,1)); 
        }              
    }

}