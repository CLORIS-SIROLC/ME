package Hardware;


import UI.ui;

// 内存
public class Memory {
    
    // 总内存块数大小
    final public static int memory_block_size = 16; 

    // 总内存页面大小（10个基本存储单元）
    final public static int block_page_size = 10; 

    // // 内存链表
    // public ArrayList memory_list; 

    // 位示图(16页,16个物理块，则数组项为16)————页框号（块号）从0开始
    public static int[] bitmap; 

    // 内存数组
    public static int[][] memory_map; 
 
    // 初始化（0————未被占用，1————已被占用）
    public Memory()
    {
        // 位示图初始化
        bitmap = new int[memory_block_size];

        // 内存数组初始化
        memory_map = new int[memory_block_size][block_page_size];

        // 位示图、内存数组全置零
        for(int i = 0; i < memory_block_size; i++)
        {
            bitmap[i] = 0;
            for(int j = 0; j < block_page_size; j++)
            {
                memory_map[i][j] = 0;
            }
        }
    }

    // 返回最小的一个空闲页面号（无空闲区则为-1）
    public  static int Free_Page()
    {
        for(int i = 0 ; i < memory_block_size ; i++)
        {
            // 查找位示图看编号                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
            if(bitmap[i] == 0)return (i);
        }
        return -1;//表示无空闲空间
    }

    // 占用页面
    public static int Set_Page(int blocknum, int pagenum)
    {// blocknum为块号，pagenum为此页面占用的存储单元数量

        // 块号超出总内存块数大小
        if((blocknum + 1) > memory_block_size)return 1; // 占用失败
        // 页面占用存储超出页面大小
        if(pagenum > block_page_size)return 2;      // 占用失败

        // 位示图表示占用
        bitmap[blocknum] = 1;

        switch(blocknum)
        {
            case 0:// 内存0
                {ui.btnNewButton_0.setText("1");   break;}
            case 1:// 内存1
                {ui.btnNewButton_1.setText("1");   break;}
            case 2:// 内存2
                {ui.btnNewButton_2.setText("1");   break;}
            case 3:// 内存3
                {ui.btnNewButton_3.setText("1");   break;}
            case 4:// 内存4
                {ui.btnNewButton_4.setText("1");   break;}
            case 5:// 内存5
                {ui.btnNewButton_5.setText("1");   break;}
            case 6:// 内存6
                {ui.btnNewButton_6.setText("1");   break;}
            case 7:// 内存7
                {ui.btnNewButton_7.setText("1");   break;}
            case 8:// 内存8
                {ui.btnNewButton_8.setText("1");   break;}
            case 9:// 内存9
                {ui.btnNewButton_9.setText("1");   break;}
            case 10:// 内存10
                {ui.btnNewButton_10.setText("1");   break;}
            case 11:// 内存11
                {ui.btnNewButton_11.setText("1");   break;}
            case 12:// 内存12
                {ui.btnNewButton_12.setText("1");   break;}
            case 13:// 内存13
                {ui.btnNewButton_13.setText("1");   break;}
            case 14:// 内存14
                {ui.btnNewButton_14.setText("1");   break;}
            case 15:// 内存15
                {ui.btnNewButton_15.setText("1");   break;}
        }

        // 内存表表示占用
        for(int i = 0; i < pagenum; i++)
        {
            memory_map[blocknum][i]=1;
        }
        return 0;
    }

    // 释放页面
    public static void Empty_Page(int blocknum)
    {
        for(int i = 0; i < block_page_size; i++)
        {
            memory_map[blocknum][i] = 0;
            switch(blocknum)
            {
                case 0:// 内存0
                    {ui.btnNewButton_0.setText("0");   break;}
                case 1:// 内存1
                    {ui.btnNewButton_1.setText("0");   break;}
                case 2:// 内存2
                    {ui.btnNewButton_2.setText("0");   break;}
                case 3:// 内存3
                    {ui.btnNewButton_3.setText("0");   break;}
                case 4:// 内存4
                    {ui.btnNewButton_4.setText("0");   break;}
                case 5:// 内存5
                    {ui.btnNewButton_5.setText("0");   break;}
                case 6:// 内存6
                    {ui.btnNewButton_6.setText("0");   break;}
                case 7:// 内存7
                    {ui.btnNewButton_7.setText("0");   break;}
                case 8:// 内存8
                    {ui.btnNewButton_8.setText("0");   break;}
                case 9:// 内存9
                    {ui.btnNewButton_9.setText("0");   break;}
                case 10:// 内存10
                    {ui.btnNewButton_10.setText("0");   break;}
                case 11:// 内存11
                    {ui.btnNewButton_11.setText("0");   break;}
                case 12:// 内存12
                    {ui.btnNewButton_12.setText("0");   break;}
                case 13:// 内存13
                    {ui.btnNewButton_13.setText("0");   break;}
                case 14:// 内存14
                    {ui.btnNewButton_14.setText("0");   break;}
                case 15:// 内存15
                    {ui.btnNewButton_15.setText("0");   break;}
            }
        }
        bitmap[blocknum] = 0;
    }

    // 打印位示图
    public static void Print_bitmap()
    {
        for(int i = 0; i < memory_block_size; i++)
        {
            //System.out.print(bitmap[i]+"  ");
        }
        //System.out.print('\n');
    }

    // 打印内存数组
    public static void Print_memorymap()
    {
        for(int i = 0; i < memory_block_size; i++)
        {
            for (int j = 0; j < block_page_size; j++)
            {
                //System.out.print(memory_map[i][j]+"  ");
            }
            //System.out.print('\n');
        }
    }
    
    // 空闲盘块数
    public static int Free_block_num()
    {
        int num = 0;
        for(int i = 0 ; i < memory_block_size ; i++)
        {
            // 查找位示图看编号                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
            if(bitmap[i] == 0)num++;
        }
        return num;
    }
    
    // 申请内存
    public static int ASK_Memory(int ins)
    {// ins为PCB含有的总指令数

        // 待存储指令数
        int INS = ins;

        // x为需求的盘块数
        int x = 0;
        if(ins % block_page_size == 0)
        {
            x = (ins / block_page_size);
        }
        else
        {
            x = (ins / block_page_size) + 1;
        }

        // 当内存中需求的盘块数大于已有空闲盘块，不可分配内存
        if((x) > Memory.Free_block_num())return -1;

        int logicaddress = MMU.logic_address_page_now + 1;
        for(int i = 0; i < x ;i++)
        {
            // 逻辑地址页号为老页号+1
            MMU.logic_address_page_now++;

            // 加入页表
            MMU.New_Map(MMU.logic_address_page_now, Free_Page());
            if(INS >= block_page_size)Set_Page(Free_Page(), block_page_size);
            else Set_Page(Free_Page(), INS);

            // 待存储指令数
            INS = INS - block_page_size;
        }
        Print_memorymap();
        return logicaddress; // 返回逻辑地址始址
    }


}
