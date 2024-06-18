package Hardware;

import java.util.Map;
import java.util.HashMap;

// 内存管理单元
public class MMU {
    //用于维护页表与地址转换

    //页面大小
        //(可存放指令条数)
    final public static int page_size_num = 10;
        //(页面大小，单位B)
    final public static int page_size_B = 1000;
    // 一条指令的大小（单位：B）
    final public static int ins_size_B = 100;


    // 页表存储（页号与块号的一一对应关系）
    public static Map<Integer,Integer> page_table = new HashMap<>();

    // 当前分配的逻辑地址页号的最后一个(从0开始依次往后分配)
    public static int logic_address_page_now = -1;


    // MMU初始化
    public MMU()
    {
        page_table = new HashMap<>();       //初始化页表
        logic_address_page_now = -1;
    }

    // 新增页表项————只是单纯加入页表
    public static int New_Map(int virtual_page, int physical_page)
    {// 页号virtual_page     块号physical_page
        
        // 查询该物理页面是否已被占用
        if(Memory.bitmap[physical_page]!=0)return -1;

        // 将页号与块号的对应关系存入页表
        page_table.put(virtual_page, physical_page);

        if (page_table.size() > Memory.memory_block_size)
        {// 页表项大于等于16
            //System.out.println("[内存不足]页表项已满16条");
            page_table.remove(virtual_page, physical_page);
            return 1;
        }
        else
        {// 页表项小于16
            //System.out.println("[页表新增]页号：" + virtual_page + " 段号：" + physical_page);
            return 0;
        }

    }

    // 查找————虚拟页序号转化为物理块序号
    public static int Virtual_Translate(int virtual_page)
    {// 页号virtual_page    

        // 块号physical_page
        int physical_page = -1;

        if(page_table.containsKey(virtual_page))
        {// 能找到虚拟地址的页号对应的物理地址的块号
            physical_page = page_table.get(virtual_page);
        }
        return physical_page;
    }

    // 打印页表
    public void Print_Page()
    {
        // System.out.println("[页表打印]");
        System.out.print(page_table);
    }

    // 转换————指令的逻辑地址转换为物理地址
    public static int Address_Transfer(int logic_add)
    {
        // a为虚拟地址的页号
        int a = logic_add / page_size_num;
         
        // b为虚拟地址的页面偏移
        int b = logic_add % page_size_num;

        // in_a为页号对应的物理地址页框号
        int in_a = Virtual_Translate(a);

        //指令的物理地址
        return (in_a * 1024 + b * 100);

    }

}
