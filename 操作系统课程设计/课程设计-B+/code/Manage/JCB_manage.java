package Manage;

import java.util.List;
import Kernel.Clock_thread;
import Others.OtherString;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;

// 作业管理
public class JCB_manage {
    
    // 待处理作业JCB队列
    public static List<JCB> JCB_list_todo;

    // 处理中作业JCB队列(等待分配PCB的作业队列)
    public static List<JCB> JCB_list_doing;

    // 下一个作业进程应该有的作业编号
    public static int User_next_new_JobsID = 1;

    // 初始化JCB_manage类，自动读取初始作业，进入待处理作业JCB队列
    public JCB_manage(String file_way)
    {
        file_way = OtherString.fileway2_1;
        // 初始化待处理作业JCB队列、处理中作业JCB队列
        JCB_list_todo = new ArrayList<JCB>();
        JCB_list_doing = new ArrayList<JCB>();

        // 读取作业文件 file_way 即jobs-input.txt中的作业数据进//待处理作业JCB队列
        File file = new File(OtherString.fileway2_1);
        BufferedReader reader = null;
        String[] x;
        // 一次读一个字符
        try 
        {
            // 以行为单位读取文件内容，一次读一整行
            reader = new BufferedReader(new FileReader(file));
            String temp = null;
            while ((temp = reader.readLine()) != null) {
                x = temp.split(",");
                JCB temp_jcb = new JCB(Integer.parseInt(x[0]), Integer.parseInt(x[1]), Integer.parseInt(x[2]));
                JCB_list_todo.add(temp_jcb);

                // 下一个作业进程应该有的作业编号+1
                User_next_new_JobsID++;
            }
        }
        catch (Exception e) {e.printStackTrace();}
    }

    // 打印待处理作业JCB队列JCB_list_todo
    public static void Print_JCB_list_todo()
    {
        for (int i=0;i<JCB_list_todo.size();i++)
        {
            JCB_list_todo.get(i).Print_JCB();
        }

    }

    // 打印处理中作业JCB队列JCB_list_doing
    public static void Print_JCB_list_doing()
    {
        for (int i=0;i<JCB_list_doing.size();i++)
        {
            JCB_list_doing.get(i).Print_JCB();
        }

    }

    // 从列表中获取某作业ID（JobsID）对应的JCB在队列中的索引
    public static int Get_Index_By_JobsID(int ID,int x)
    {
        // x==0则为待处理作业JCB队列JCB_list_todo
        // x==1则为待处理作业JCB队列JCB_list_doing
        int key = 0;
        if(x==0)
        {
             for (int i=0 ; i < JCB_list_todo.size() ; i++)
            {
                if (JCB_list_todo.get(i).JobsID == ID)
                {
                    key = 1;
                    return i;
                }
            }
        }
        else if(x==1){
            for (int i=0 ; i < JCB_list_doing.size() ; i++)
            {
                if (JCB_list_doing.get(i).JobsID == ID)
                {
                    key = 1;
                    return i;
                }
            }
        } 
        if (key == 0)System.out.println("未查询到作业ID:" + ID + "对应的JCB在队列中的索引");
        return -1;  // 表示没有查询到
    }

    // 将一个JCB从待处理作业JCB队列（todo）转移到处理中作业JCB队列（doing）
    public static void Todo_Translate_Doing(int id)
    {// i为该JCB的作业ID（JobsID）

        // index为该作业在待处理作业JCB队列JCB_list_todo中的索引
        int index=Get_Index_By_JobsID(id, 0);

        // 将此JCB的作业进入时间InTimes_1设置为当前时间
        JCB_list_todo.get(index).InTimes_1 = Clock_thread.COUNTTIME;

        // 将该JCB存入处理中作业JCB队列JCB_list_doing中
        JCB_list_doing.add(JCB_list_todo.get(index));

        // 从待处理作业JCB队列JCB_list_todo中除去该JCB
        JCB_list_todo.remove(index);
    }

    // 获取待处理作业JCB队列JCB_list_todo中作业请求时间InTimes最小的JCB在队列中的索引
    public static int Get_Earliest_Arrival_Time_JCB_todo()
    {
        // 初始最小值
        int MIN = 99999;

        // 初始索引
        int index = -1;

        for (int i=0 ; i < JCB_list_todo.size() ; i++)
        {
            if(JCB_list_todo.get(i).InTimes_0 < MIN)
            {
                index = i;
                MIN = JCB_list_todo.get(i).InTimes_0;
            }
        }
        return index;  // -1 即为没有找到
    }

    // 新建JCB加入待处理作业JCB队列JCB_list_todo
    public static void Add_new_JCB_todo()
    {
        // 作业请求时间intimes
        int intimes_0 = Clock_thread.COUNTTIME;
        
        // 作业包含的程序指令数目insnum
        int insnum = 0;

        // 随机数实例
        SecureRandom secureRandom;
        try 
        {
            secureRandom = SecureRandom.getInstance("SHA1PRNG");
            // 使用系统时钟做种子
            secureRandom.setSeed(System.currentTimeMillis());
            // 随机设置此作业中的指令条数insnum，范围为（10-20）条
            insnum = 10 + secureRandom.nextInt(11);
        } 
        catch (NoSuchAlgorithmException e) {e.printStackTrace();}

        // 加入对应队列
        JCB x = new JCB(User_next_new_JobsID, intimes_0, insnum);
        JCB_list_todo.add(x);

        // 创建新作业自己的程序段指令文件 “x.txt”
        String filename = new String(String.valueOf(User_next_new_JobsID) + ".txt");
        try{
            File file = new File(OtherString.fileway2_2+ filename);
            if (file.createNewFile()) 
            {
              System.out.println("创建新作业: " + file.getName());
            }
        }
        catch(Exception e){e.printStackTrace();}

        //将 指令内容 写入新作业自己的程序段指令文件 “x.txt”中
        String temp;
        BufferedWriter wri = null;
        try {                         
            secureRandom = SecureRandom.getInstance("SHA1PRNG");
            // 使用系统时钟做种子
            secureRandom.setSeed(System.currentTimeMillis());      

            //指令内容按行写入
            wri = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(OtherString.fileway2_2 + filename, true))); 
            for(int i = 0; i < insnum; i++) 
            {
                if(i<insnum-1)
                {
                    temp = String.valueOf(i + 1) + ',' + String.valueOf(secureRandom.nextInt(4)) + '\n';      
                }
                else
                {
                    temp = String.valueOf(i + 1) + ',' + String.valueOf(secureRandom.nextInt(4));    
                }
                wri.write(temp);        
            }                                                                    
        }
        catch (Exception e) {e.printStackTrace();} 
        finally {   
            try{wri.close();} 
            catch (IOException e) {e.printStackTrace();}    
        }

        //将新加入的作业信息写入文件input2\jobs-input.txt
        temp = new String("\r" + String.valueOf(User_next_new_JobsID) + ',' 
            + String.valueOf(intimes_0) + ',' + String.valueOf(insnum));
        BufferedWriter writer = null;
        try {                                                                        
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(OtherString.fileway2_1, true)));                              
            writer.write(temp);                                                      
        }
        catch (Exception e) {e.printStackTrace();} 
        finally {   
            try{writer.close();} 
            catch (IOException e) {e.printStackTrace();}    
        }

        // 下一个作业进程应该有的作业编号+1
        User_next_new_JobsID++;
    }

}
