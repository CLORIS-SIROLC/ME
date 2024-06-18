package Manage;

// 指令类的设计
public class INSTRUCTION {
    
// 指令段编号（Instruc_ID）：作业创建进程以后，进程所执行用户程序段指令序号，从 1 开始计数。
public int Instruc_ID;

// 指令类型
public int Instruc_State;
// 0 表示用户态计算操作语句。执行该指令需要运行时间 InRunTimes=1s。
// 1 表示用户态计算操作函数。执行该指令进程转向执行函数指令，需要 InRunTimes=2s。
// 2 表示键盘输入变量语句。发生系统调用，CPU 进行模式切换，运行进程进入阻塞态；
    // 值守的键盘操作输入模块接收到输入变量或输出变量内容，InRunTimes=2s 后完成输入，
    // 产生硬件终端信息号，阻塞队列 1 的队头节点出队，进入就绪队列；InputBlock_thread 类在 2s
    // 以后自动唤醒该进程；会通过缓冲区进行数据输入输出；
// 3 屏幕输出显示语句。发生系统调用，CPU 进行模式切换，运行进程进入阻塞态；
    // 值守的屏幕显示模块输出变量内容，InRunTimes=2s 后完成显示，产生硬件终端信息号，
    // 阻塞队列 2 的队头节点出队，进入就绪队列；OutputBlock_thread 类在 2s 以后自动唤醒该进程；

// 指令访问的逻辑地址
public int L_Address;

// 指令运行时间
public int InRunTimes;

//初始化
public INSTRUCTION(int id , int state)
{
    // 指令段编号
    Instruc_ID = id;

    // 指令类型
    Instruc_State = state;

    // 指令访问的逻辑地址暂时设为0
    L_Address = 0;

    // 指令运行时间暂时随便设置
    switch(state)
    {
        case 0:
        {
            InRunTimes = 1; 
            break;
        }
        case 1:
        {
            InRunTimes = 2; 
            break;
        }
        case 2:
        {
            InRunTimes = 2; 
            break;
        }
        case 3:
        {
            InRunTimes = 2; 
            break;
        }
        default :
        {
            InRunTimes = -1; 
            // System.out.println("ERROR!指令类型与规定不符（越界）");
            break;
        }
    }

}

    
}
