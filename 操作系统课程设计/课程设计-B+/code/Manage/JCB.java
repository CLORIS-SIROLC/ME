package Manage;

//JCB类的设计
public class JCB {
    
    // 作业序号ID
    public int JobsID;

    // 作业请求时间，单位为秒
    public int InTimes_0;

    // 作业进入时间，单位为秒
    public int InTimes_1;

    // 作业包含的程序指令数目
    public int InstrucNum;

    // // 作业的优先级
    // private int Priority;

    // // 作业所占内存大小
    // private int MemorySize;

    // 所有指令
    public INSTRUCTION[] instructions;

    // 构造函数
    public JCB(int ID, int intimes, int insnum)
    {
        JobsID = ID;
        InTimes_0 = intimes;
        InstrucNum = insnum;
        InTimes_1 = 0;
    }

    //打印JCB的方法
    public void Print_JCB()
    {
        // System.out.println("JobsID:" + JobsID + "  InTimes:" + InTimes_0 +"  InstrucNum:" + InstrucNum);
    }
}


//分别设置了用来读取和修改数据成员的接口