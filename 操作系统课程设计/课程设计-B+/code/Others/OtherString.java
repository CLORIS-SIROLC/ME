package Others;

import java.util.ArrayList;
import java.util.List;

public class OtherString {

    // 进程调度事件的输出字符串
    public static String out_string_1;

    // 状态统计信息的输出字符串
    public static String out_string_2;

    // 缓冲区处理事件的输出字符串
    public static String out_string_3;
    // 记录阻塞队列1相关信息（开头）
    public final static String out_string_3_1 = new String("BB1:[阻塞队列1,键盘输入:");
    public static List<String> out_string_3_1_1;
    // 记录阻塞队列2相关信息（开头）
    public final static String out_string_3_2 = new String("BB2:[阻塞队列2,屏幕显示:");
    public static List<String> out_string_3_2_1;
    //进入时间,进程 ID
    
    // 就绪队列字符串（就绪队列现状输出）
    public static String out_string_4;
    // 阻塞队列1字符串（阻塞队列现状输出）
    public static String out_string_5_1;
    // 阻塞队列2字符串（阻塞队列现状输出）
    public static String out_string_5_2;

    // 用于IO文本框显示
    public static String output_string;

    // 文件地址2（input）
    public static String fileway2_1;
    public static String fileway2_2;
    public static String fileway2_3;
    public static String fileway2_4;




    // 所有事件的输出字符串
    public static String out_string_all;

    public OtherString()
    {
        out_string_1 = new String("");
        out_string_2 = new String("");
        out_string_3 = new String("");  
        out_string_3_1_1 = new ArrayList<String>();    
        out_string_3_2_1 = new ArrayList<String>();    
        out_string_all = new String("");

        fileway2_1 = new String("input2\\jobs-input.txt");
        fileway2_2 = new String("input2\\");
        fileway2_3 = new String("output2");
        fileway2_4 = new String("input2");

        out_string_4 = new String("");  
        out_string_5_1 = new String("");  
        out_string_5_2 = new String("");  

        output_string = new String("");
    }
}
