package UI;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import Hardware.Memory;
import Kernel.Clock_thread;
import Kernel.InputBlock_thread;
import Kernel.JobIn_thread;
import Kernel.OutputBlock_thread;
import Kernel.ProcessScheduling_thread;
import Manage.JCB_manage;
import Manage.PCB_manage;
import Others.OtherString;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;




// 界面设计
public class ui  extends JFrame {

    
    Thread mclock = new Thread(new Clock_thread());
    Thread jobin = new Thread(new JobIn_thread());
    Thread sched = new Thread(new ProcessScheduling_thread());
    Thread input = new Thread(new InputBlock_thread());
    Thread output = new Thread(new OutputBlock_thread());

    static public int key1 = 0; // 表示未开始计时
    static public int key2 = 0; // 表示未选择运行方式1————SJP
    static public int key3 = 0; // 表示未选择运行方式2————DJFK
    static public int key4 = 0; // 表示未打开文件

    public static void main(String args[])
    {
        OtherString otherstring = new OtherString();
        Memory memory = new Memory();
        JCB_manage jcb_manage = new JCB_manage(OtherString.fileway2_1);
        PCB_manage pcb_manage = new PCB_manage();

        // 界面
        try {
            ui frame = new ui();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        

    }

    public static JPanel contentPane;// 总框架页面

    public static JButton btnNewButton_OPEN_File;   // 打开文件按钮
    public static JButton btnNewButton_STORE_File;// 另存为按钮
    public static JButton btnNewButton_OUT;     // 退出按钮
    public static JButton btnNewButton_START;   // 开始按钮
    public static JButton btnNewButton_STOP;    // 暂停按钮
    public static JButton btnNewButton_NewWork;// 作业新增按钮

    public static JRadioButton rdbtnNewRadioButton_DJFK;    // 多级反馈队列选择按钮
    public static JRadioButton rdbtnNewRadioButton_SJP;     // 时间片轮转法选择按钮

    public static TextArea textArea_running;    // 运行记录
    public static TextArea textArea_io;     // IO记录
    public static TextArea textArea_ready;   // 就绪队列记录
    public static TextArea textArea_block_1;    // 阻塞队列1记录
    public static TextArea textArea_block_2;    // 阻塞队列2记录

    public static JButton btnNewButton_0;   // 内存0
    public static JButton btnNewButton_1;   // 内存1
    public static JButton btnNewButton_2;   // 内存2
    public static JButton btnNewButton_3;   // 内存3
    public static JButton btnNewButton_4;   // 内存4
    public static JButton btnNewButton_5;   // 内存5
    public static JButton btnNewButton_6;   // 内存6
    public static JButton btnNewButton_7;   // 内存7
    public static JButton btnNewButton_8;   // 内存8
    public static JButton btnNewButton_9;   // 内存9
    public static JButton btnNewButton_10;   // 内存10
    public static JButton btnNewButton_11;   // 内存11
    public static JButton btnNewButton_12;   // 内存12
    public static JButton btnNewButton_13;   // 内存13
    public static JButton btnNewButton_14;   // 内存14
    public static JButton btnNewButton_15;   // 内存15

    public ui() {
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setBounds(350, 50, 900, 900);
            contentPane = new JPanel();
            contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

            setContentPane(contentPane);
            contentPane.setLayout(new BorderLayout(0, 0));
            
            JPanel panel_menu = new JPanel();
            panel_menu.setBackground(new Color(173, 198, 218));
            contentPane.add(panel_menu, BorderLayout.NORTH);
            panel_menu.setLayout(new GridLayout(1, 5, 0, 0));
            
            btnNewButton_OPEN_File = new JButton("打 开 文 件");
            btnNewButton_OPEN_File.setFont(new Font("黑体", Font.BOLD, 17));
            panel_menu.add(btnNewButton_OPEN_File);
            btnNewButton_OPEN_File.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) 
            {                
				JOptionPane.showMessageDialog(null,"请选择合适的jobs-input.txt文件", "", JOptionPane.INFORMATION_MESSAGE);
                JFileChooser chooser = new JFileChooser(OtherString.fileway2_4);
                    chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                    chooser.showDialog(new JLabel(), "选择");
                    // File file = chooser.getSelectedFile();
                    if(chooser.getSelectedFile().getParentFile().getName().equals("input2"))
                    {
                        OtherString.fileway2_1 = new String("input2\\jobs-input.txt");
                        OtherString.fileway2_2 = new String("input2\\");
                        OtherString.fileway2_3 = new String("output2");
                        OtherString.fileway2_4 = new String("input2");
                    }
                    else if(chooser.getSelectedFile().getParentFile().getName().equals("input1"))
                    {
                        OtherString.fileway2_1 = new String("input1\\jobs-input.txt");
                        OtherString.fileway2_2 = new String("input1\\");
                        OtherString.fileway2_3 = new String("output1");
                        OtherString.fileway2_4 = new String("input1");
                    }
                key4 = 1;
            }
            });

            btnNewButton_STORE_File = new JButton("另 存 为");
            btnNewButton_STORE_File.setFont(new Font("黑体", Font.BOLD, 17));
            panel_menu.add(btnNewButton_STORE_File);
            btnNewButton_STORE_File.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) 
            {                
                String defaultFileName = new String();
                if(rdbtnNewRadioButton_SJP.isSelected())defaultFileName = new String("ProcessResults-xxx-SJP.txt");
                else defaultFileName = new String("ProcessResults-xxx-DJFK.txt");
                //弹出文件选择框
				JFileChooser chooser = new JFileChooser(OtherString.fileway2_3);
                chooser.setSelectedFile(new File(defaultFileName)); //设置默认文件名
                chooser.setFileFilter(new FileNameExtensionFilter("TXT","txt"));
				//下面的方法将阻塞，直到【用户按下保存按钮且“文件名”文本框不为空】或【用户按下取消按钮】
				int option = chooser.showSaveDialog(null);
				if(option==JFileChooser.APPROVE_OPTION)
                {	//假如用户选择了保存
					File file = chooser.getSelectedFile();
					String filename = chooser.getName(file);	//从文件名输入框中获取文件名
					try {
						BufferedWriter outBuffer = new BufferedWriter( new FileWriter(file));
                        OtherString.out_string_all = new String("作业/进程调度事件：\n" + textArea_running.getText());
						outBuffer.write(OtherString.out_string_all);
						outBuffer.flush();//刷新，保证缓冲区内容写入
						outBuffer.close();		
					} catch (IOException ex) {System.err.println("IO异常");ex.printStackTrace();}	
					JOptionPane.showMessageDialog(null,"存储完毕！", "", JOptionPane.INFORMATION_MESSAGE);
		        }
			}
		});
        
            
            btnNewButton_OUT = new JButton("退 出");
            btnNewButton_OUT.setFont(new Font("黑体", Font.BOLD, 17));
            panel_menu.add(btnNewButton_OUT);
            btnNewButton_OUT.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) 
            { 
				JOptionPane.showMessageDialog(null,"提示：窗口即将关闭！", "", JOptionPane.INFORMATION_MESSAGE);
				System.exit(1);
			}
		    });
        
            JPanel panel_ontime = new JPanel();
            panel_ontime.setSize(new Dimension(500, 900));
            panel_ontime.setMaximumSize(new Dimension(500, 900));
            panel_ontime.setBorder(new EmptyBorder(12, 12, 12, 12));
            panel_ontime.setBackground(new Color(173, 198, 218));
            contentPane.add(panel_ontime, BorderLayout.CENTER);
            panel_ontime.setLayout(new GridLayout(2, 2, 0, 0));
            
            Panel panel = new Panel();
            panel_ontime.add(panel);
            panel.setLayout(new BorderLayout(0, 0));
            
            JLabel lblNewLabel_1 = new JLabel("运行记录");
            lblNewLabel_1.setFont(new Font("楷体", Font.BOLD, 17));
            panel.add(lblNewLabel_1, BorderLayout.NORTH);
            
            textArea_running = new TextArea();
            textArea_running.setFont(new Font("Dialog", Font.PLAIN, 17));
            panel.add(textArea_running, BorderLayout.CENTER);
            textArea_running.setEditable(false);
            textArea_running.setBackground(new Color(255, 255, 255));

            Panel panel_2 = new Panel();
            panel_ontime.add(panel_2);
            panel_2.setLayout(new GridLayout(2, 1, 0, 0));
            
            Panel panel_7 = new Panel();
            panel_2.add(panel_7);
            panel_7.setLayout(new BorderLayout(0, 0));
            
            JLabel lblNewLabel_2_1 = new JLabel("Input/Output");
            lblNewLabel_2_1.setVerticalAlignment(SwingConstants.TOP);
            lblNewLabel_2_1.setFont(new Font("楷体", Font.BOLD, 17));
            panel_7.add(lblNewLabel_2_1, BorderLayout.NORTH);
            
            textArea_io = new TextArea();
            textArea_io.setFont(new Font("Dialog", Font.PLAIN, 17));
            panel_7.add(textArea_io);
            textArea_io.setEditable(false);
            textArea_io.setBackground(new Color(255, 255, 255));
            
            Panel panel_8 = new Panel();
            panel_2.add(panel_8);
            panel_8.setLayout(new BorderLayout(0, 0));
            
            JLabel lblNewLabel_2_1_1 = new JLabel("就绪队列");
            lblNewLabel_2_1_1.setVerticalAlignment(SwingConstants.TOP);
            lblNewLabel_2_1_1.setFont(new Font("楷体", Font.BOLD, 17));
            panel_8.add(lblNewLabel_2_1_1, BorderLayout.NORTH);
            
            textArea_ready = new TextArea();
            textArea_ready.setFont(new Font("Dialog", Font.PLAIN, 17));
            panel_8.add(textArea_ready, BorderLayout.CENTER);
            textArea_ready.setEditable(false);
            textArea_ready.setBackground(new Color(255, 255, 255));
            
            Panel panel_1 = new Panel();
            panel_ontime.add(panel_1);
            panel_1.setLayout(new GridLayout(2, 1, 0, 0));
            
            JPanel panel_9 = new JPanel();
            panel_9.setBackground(new Color(173, 198, 218));
            panel_1.add(panel_9);
            panel_9.setLayout(new GridLayout(2, 1, 0, 0));
            
            rdbtnNewRadioButton_SJP = new JRadioButton("时间片轮转法");
            rdbtnNewRadioButton_SJP.setBackground(new Color(173, 198, 218));
            rdbtnNewRadioButton_SJP.setFont(new Font("黑体", Font.BOLD, 20));
            panel_9.add(rdbtnNewRadioButton_SJP);
            rdbtnNewRadioButton_SJP.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                if(rdbtnNewRadioButton_DJFK.isSelected())
                {
					JOptionPane.showMessageDialog(null,"只可以选择一种！", "", JOptionPane.INFORMATION_MESSAGE);
                    rdbtnNewRadioButton_SJP.setSelected(false);
                    key2 = 0;
                }
                else 
                {
                    if(rdbtnNewRadioButton_SJP.isSelected())key2 = 1;
                }
                
            }
            });
            
            rdbtnNewRadioButton_DJFK = new JRadioButton("多级反馈队列");
            rdbtnNewRadioButton_DJFK.setFont(new Font("黑体", Font.BOLD, 20));
            rdbtnNewRadioButton_DJFK.setBackground(new Color(173, 198, 218));
            panel_9.add(rdbtnNewRadioButton_DJFK);
            rdbtnNewRadioButton_DJFK.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                if(rdbtnNewRadioButton_SJP.isSelected())
                {
					JOptionPane.showMessageDialog(null,"只可以选择一种！", "", JOptionPane.INFORMATION_MESSAGE);
                    rdbtnNewRadioButton_DJFK.setSelected(false);
                    key3 = 0;

                }
                else 
                {
                    if(rdbtnNewRadioButton_DJFK.isSelected())key3 = 1;
                }
            }
            });


            JPanel panel_10 = new JPanel();
            panel_10.setBackground(new Color(173, 198, 218));
            panel_1.add(panel_10);
            panel_10.setLayout(new GridLayout(2, 2, 0, 0));
            
            btnNewButton_START = new JButton("开 始");
            btnNewButton_START.setFont(new Font("黑体", Font.BOLD, 25));
            panel_10.add(btnNewButton_START);
            btnNewButton_START.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                if(key2 == 1 || key3 == 1)
                {    if(mclock.isAlive())
                    {
                        // mclock.start();
                    }             
                    else
                    {
                        mclock = new Thread(new Clock_thread());
                        mclock.start();
                    }   
                    if(jobin.isAlive())
                    {
                        // jobin.start();
                    }             
                    else
                    {
                        jobin = new Thread(new JobIn_thread());
                        jobin.start();
                    }                 
                    if(sched.isAlive())
                    {
                        // sched.start();
                    }             
                    else
                    {
                        sched = new Thread(new ProcessScheduling_thread());
                        sched.start();
                    } 
                    if(input.isAlive())
                    {
                        // input.start();
                    }             
                    else
                    {
                        input = new Thread(new InputBlock_thread());
                        input.start();
                    } 
                    if(output.isAlive())
                    {
                        // output.start();
                    }             
                    else
                    {
                        output = new Thread(new OutputBlock_thread());
                        output.start();
                    } 

                    Clock_thread.flag = true;
                    JobIn_thread.flag = true;
                    ProcessScheduling_thread.flag = true;
                    InputBlock_thread.flag = true;
                    OutputBlock_thread.flag = true;
                    key1 = 1; //开始计时
                }
                else
                {
				    JOptionPane.showMessageDialog(null,"提示：请先选择调度算法", "", JOptionPane.INFORMATION_MESSAGE);
                }
                
            }
            });

            JPanel panel_11 = new JPanel();
            panel_11.setBackground(new Color(173, 198, 218));
            panel_10.add(panel_11);
            panel_11.setLayout(new BorderLayout(0, 0));

            btnNewButton_NewWork = new JButton("作业新增");
            btnNewButton_NewWork.setFont(new Font("黑体", Font.BOLD, 25));
            panel_11.add(btnNewButton_NewWork);

            btnNewButton_NewWork.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) 
            {                
                if(key1==1)
                {
                    JCB_manage.Add_new_JCB_todo();
				    JOptionPane.showMessageDialog(null,"提示：第" + Clock_thread.COUNTTIME + "s新增一个作业", "", JOptionPane.INFORMATION_MESSAGE);
                }
                else
                {
				    JOptionPane.showMessageDialog(null,"提示：作业新增失败，请在运行时新增。", "", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            });
            
            JPanel panel_12 = new JPanel();
            panel_12.setBackground(new Color(173, 198, 218));
            panel_10.add(panel_12);
            
            btnNewButton_STOP = new JButton("暂 停");
            btnNewButton_STOP.setFont(new Font("黑体", Font.BOLD, 25));
            panel_10.add(btnNewButton_STOP);
            btnNewButton_STOP.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) 
            {         
                Clock_thread.flag = false;
                JobIn_thread.flag = false;
                ProcessScheduling_thread.flag = false;
                InputBlock_thread.flag = false;
                OutputBlock_thread.flag = false;
                key1 = 0; // 计时结束
            }
            });
            
            Panel panel_4 = new Panel();
            panel_ontime.add(panel_4);
            panel_4.setLayout(new GridLayout(0, 1, 0, 0));
            
            Panel panel_5 = new Panel();
            panel_4.add(panel_5);
            panel_5.setLayout(new BorderLayout(0, 0));
            
            JLabel lblNewLabel_3 = new JLabel("阻塞队列1");
            lblNewLabel_3.setFont(new Font("楷体", Font.BOLD, 17));
            panel_5.add(lblNewLabel_3, BorderLayout.NORTH);
            
            textArea_block_1 = new TextArea();
            textArea_block_1.setFont(new Font("Dialog", Font.PLAIN, 17));
            panel_5.add(textArea_block_1, BorderLayout.CENTER);
            textArea_block_1.setEditable(false);
            textArea_block_1.setBackground(new Color(255, 255, 255));
            
            Panel panel_6 = new Panel();
            panel_4.add(panel_6);
            panel_6.setLayout(new BorderLayout(0, 0));
            
            JLabel lblNewLabel_4 = new JLabel("阻塞队列2");
            lblNewLabel_4.setFont(new Font("楷体", Font.BOLD, 17));
            panel_6.add(lblNewLabel_4, BorderLayout.NORTH);
            
            textArea_block_2 = new TextArea();
            textArea_block_2.setFont(new Font("Dialog", Font.PLAIN, 17));
            panel_6.add(textArea_block_2, BorderLayout.CENTER);
            textArea_block_2.setEditable(false);
            textArea_block_2.setBackground(new Color(255, 255, 255));
            
            JPanel panel_3 = new JPanel();
            panel_3.setBackground(new Color(173, 198, 218));
            contentPane.add(panel_3, BorderLayout.SOUTH);
            
            JLabel lblNewLabel_2 = new JLabel("人工智能211 孔涵玥 11521118");
            lblNewLabel_2.setFont(new Font("黑体", Font.BOLD, 17));
            panel_3.add(lblNewLabel_2);
            
            JPanel panel_memory = new JPanel();
            panel_memory.setBorder(new EmptyBorder(6, 12, 6, 12));
            panel_memory.setBackground(new Color(173, 198, 218));
            contentPane.add(panel_memory, BorderLayout.EAST);
            panel_memory.setLayout(new GridLayout(17, 1, 0, 0));
            
            JLabel lblNewLabel = new JLabel("实时内存");
            lblNewLabel.setFont(new Font("楷体", Font.BOLD, 17));
            panel_memory.add(lblNewLabel);
            
            btnNewButton_0 = new JButton("0");
            btnNewButton_0.setFont(new Font("宋体", Font.BOLD, 20));
            panel_memory.add(btnNewButton_0);
            
            btnNewButton_1 = new JButton("0");
            btnNewButton_1.setFont(new Font("宋体", Font.BOLD, 20));
            panel_memory.add(btnNewButton_1);
            
            btnNewButton_2 = new JButton("0");
            btnNewButton_2.setFont(new Font("宋体", Font.BOLD, 20));
            panel_memory.add(btnNewButton_2);
            
            btnNewButton_3 = new JButton("0");
            btnNewButton_3.setFont(new Font("宋体", Font.BOLD, 20));
            panel_memory.add(btnNewButton_3);
            
            btnNewButton_4 = new JButton("0");
            btnNewButton_4.setFont(new Font("宋体", Font.BOLD, 20));
            panel_memory.add(btnNewButton_4);
            
            btnNewButton_5 = new JButton("0");
            btnNewButton_5.setFont(new Font("宋体", Font.BOLD, 20));
            panel_memory.add(btnNewButton_5);
            
            btnNewButton_6 = new JButton("0");
            btnNewButton_6.setFont(new Font("宋体", Font.BOLD, 20));
            panel_memory.add(btnNewButton_6);
            
            btnNewButton_7 = new JButton("0");
            btnNewButton_7.setFont(new Font("宋体", Font.BOLD, 20));
            panel_memory.add(btnNewButton_7);
            
            btnNewButton_8 = new JButton("0");
            btnNewButton_8.setFont(new Font("宋体", Font.BOLD, 20));
            panel_memory.add(btnNewButton_8);
            
            btnNewButton_9 = new JButton("0");
            btnNewButton_9.setFont(new Font("宋体", Font.BOLD, 20));
            panel_memory.add(btnNewButton_9);
            
            btnNewButton_10 = new JButton("0");
            btnNewButton_10.setFont(new Font("宋体", Font.BOLD, 20));
            panel_memory.add(btnNewButton_10);
            
            btnNewButton_11 = new JButton("0");
            btnNewButton_11.setFont(new Font("宋体", Font.BOLD, 20));
            panel_memory.add(btnNewButton_11);
            
            btnNewButton_12 = new JButton("0");
            btnNewButton_12.setFont(new Font("宋体", Font.BOLD, 20));
            panel_memory.add(btnNewButton_12);
            
            btnNewButton_13 = new JButton("0");
            btnNewButton_13.setFont(new Font("宋体", Font.BOLD, 20));
            panel_memory.add(btnNewButton_13);
            
            btnNewButton_14 = new JButton("0");
            btnNewButton_14.setFont(new Font("宋体", Font.BOLD, 20));
            panel_memory.add(btnNewButton_14);
            
            btnNewButton_15 = new JButton("0");
            btnNewButton_15.setFont(new Font("宋体", Font.BOLD, 20));
            panel_memory.add(btnNewButton_15);
        
        }

}
