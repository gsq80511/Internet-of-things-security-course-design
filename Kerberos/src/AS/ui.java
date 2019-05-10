package AS;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import DES.DES_wzj;
import DES.k_desjie;
import RSA.rsa;


public class ui {
    //内部类，监听事件？
	static String Kctgs;
	static String Kcv;
	static String IDc;
	static String TS5;
	private static String adress_as = "127.0.0.1";
	private static String adress_tgs = "127.0.0.1";
	private static String adress_v = "127.0.0.1";
	JFrame jFrame = new JFrame("登录");
    private Container c = jFrame.getContentPane();
    private JLabel a1 = new JLabel("用户名");
    private JTextField username = new JTextField();
    private JLabel a2 = new JLabel("密   码");
    
    private JTextField password = new JTextField();
    
    private JTextField zhuce = new JTextField();
    
    private JButton button1 = new JButton("确定");
    private JButton button2 = new JButton("取消");
    private JButton button3 = new JButton("注册");
	 private class SimpleListener implements ActionListener
   {
     /**
  　　 * 利用该内部类来监听所有事件源产生的事件
   　 * 便于处理事件代码模块化
   　 */
		 
		 public void actionPerformed(ActionEvent e) 
		 {
			 String buttonName = e.getActionCommand();
           	if (buttonName.equals("确定"))
           	{
        	   String name = username.getText();
        	   String pwd = password.getText();
        	   if(name.equals("")||pwd.equals("")||name==""||pwd=="")
        	   {
        		   JOptionPane.showMessageDialog(null, "请输入完整信息", "", JOptionPane.ERROR_MESSAGE);
        		   return;
        	   }
        	   if(name.length()!=4)
        	   {
        		   JOptionPane.showMessageDialog(null, "用户名4位", "", JOptionPane.ERROR_MESSAGE);
        		   return;
        	   }
        	   if(pwd.length()!=8)
        	   {
        		   JOptionPane.showMessageDialog(null, "密码8位", "", JOptionPane.ERROR_MESSAGE);
        		   return;
        	   }
        	   
        	   System.out.println(name+pwd);
        	   
        	 //页面点击确定要干的事情
        	   boolean deng = false;
			try {
				deng = denglu(name,pwd);
				System.out.println("deng"+deng);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	   
				if(deng)
				   {
					   // 假装进入聊天室（无TGS）
					   //向V发送自己的用户名
						try {
							
							 new uitwo(IDc,Kcv);
							
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				 	   jFrame.dispose();
				 	   
				   }
           	
          
           	}
           	
           	else  if(buttonName.equals("取消"))
           	{
           		username.setText("");
                password.setText("");
           	}
           	
           	else  if(buttonName.equals("注册"))
           	{
				//System.out.println("1");
        		String name = username.getText();
         	    String pwd = password.getText();
         	    if(pwd.length()!=8)
         	    {
					//System.out.println(name+pwd);
         	    	JOptionPane.showMessageDialog(null, "注册密码位数要等于8！", "", JOptionPane.ERROR_MESSAGE);
         	    }
         	    if(pwd.length()==8)
				{
					//System.out.println(name+pwd);
					boolean zhu = false;
					try {
						zhu = zhuce(name,pwd);
						System.out.println("zhu "+zhu);
					} catch (IOException e1) {
						// TODO Auto-generated catch block

						e1.printStackTrace();
					}
				}
         	    //如果已经有了就说已经注册了
         	   
           	}

           	
         }
     }
	 /*
		 * TS+1
		 */
		public static String TSadd(String TS, int sec) throws ParseException {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// �������ڸ�ʽ
			Date utilDate = df.parse(TS);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(utilDate);
			calendar.add(Calendar.SECOND, sec);
			return df.format(calendar.getTime());

		}
		 public static boolean zhuce(String IDc,String pwd) throws UnknownHostException, IOException
			{
			 	System.out.println("--client--");
				//Socket socket = new Socket("127.0.0.1", 5299);
			 	Socket socket = new Socket(adress_as, 5299);
				DataInputStream input = new DataInputStream(socket.getInputStream());
				DataOutputStream output = new DataOutputStream(socket.getOutputStream());
				String TS1 = Create_TS();
				String TS = zhuan(TS1);
				String Kc = zhuan(pwd);
				String IDtgs = "1101";
				String state = "0111";
				String remnant = "000000";
				String blank = "000000000000000000000000";
				String data = IDc+pwd;
				String AS_PublicKey=" ";
				//AS_PublicKey=readSSL.readSSL();
				String filepath="";
				//AS_PublicKey=readSSL.readSSL(filepath);
				AS_PublicKey="65537";
				data=rsa.RSA_Encryption(data,AS_PublicKey);
				String send=state+remnant+blank+data;
				output.writeUTF(send);
				String receive = null;
				receive = input.readUTF();
				String d = receive.substring(34);//解密之前的data
				String receivejie = DES_wzj.des_jie_da(d, Kc);
				System.out.println("ui解密之后的data"+receivejie);
				if(receive.substring(0, 4).equals("1001"))
				{
					String errortype = receivejie.substring(0, 4);
					if(errortype.equals("1111"))
					{
						//System.out.println(errortype);
						//写入log文件
						//writelog.write_log(errortype,"src//AS//");
						JOptionPane.showMessageDialog(null, "该用户已经存在", "", JOptionPane.ERROR_MESSAGE);
						return false;
					}
				}
				else
					{
						JOptionPane.showMessageDialog(null, "注册成功", "", JOptionPane.ERROR_MESSAGE);
						return true;
					}
				return true;
			}

    public static boolean denglu(String IDc1,String pwd) throws UnknownHostException, IOException, ParseException
    {
        System.out.println("--client--");
        //Socket(,)内参数分别设为服务端IP和端口
        Socket socket = new Socket(adress_as, 5299);
       // Socket socket = new Socket("127.0.0.1", 5299);
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());		
        String TS1 = Create_TS();
        String TS = zhuan(TS1);
        IDc = IDc1;
        String Kc = zhuan(pwd);
        //System.out.println("用户输入的密钥"+Kc+"长度为"+Kc.length());
        String IDtgs = "1101";
        String state = "0001";
        String remnant = "000000";
        String blank = "000000000000000000000000";
        String data = IDc+ IDtgs +TS;
        String send = state + remnant + blank +data ;
		output.writeUTF(send);		
        //发送       
		//System.out.println("发了发了发给AS了");
        String receive = null;
		receive = input.readUTF();
		//System.out.println("server有消息了");
		
		//System.out.println("解密之前"+receive);
		String d = receive.substring(34);
		//System.out.println("解密之前的data");
		//System.out.println(d);
				
		
		String receivejie = DES_wzj.des_jie_da(d, Kc); 
		//System.out.println("ui解密之后的data"+receivejie);
       // String Kctgs = receive.substring(34,90);   
        //可以根据发来的反馈看看进步进行下一步
        if(receive.substring(0, 4).equals("1001"))
        {
     	  String errortype = receive.substring(34, 38);
     	  if(errortype.equals("0001"))
     	  {
     		  //写入log文件
     		  JOptionPane.showMessageDialog(null, "无此用户", "", JOptionPane.ERROR_MESSAGE);
     		  return false;
     	  }
     	  else if(errortype.equals("0100"))
     	  {
     		 JOptionPane.showMessageDialog(null, "不同步", "", JOptionPane.ERROR_MESSAGE);
    		  return false;
     	  }
        }
        else
        {    
        	
        	String Idtgs = huifu(receivejie.substring(64,96));
        //	System.out.println("Idtgs （由密钥解密后的Idtgs）"+Idtgs);
        //	System.out.println("是否相等"+Idtgs.equals(IDtgs));
        	if(Idtgs.equals(IDtgs))
        	{
        		Kctgs = receivejie.substring(0,64);  ///!!!!!!!!
        		String ticket = receivejie.substring(288,672);
        //		System.out.println("C收到AS给的Ticket");
        //		System.out.println(ticket);
        		String fa = split(receive.substring(0, 34)+Kctgs+Idtgs+receivejie.substring(96));
        //		System.out.println("会话密钥"+Kctgs);
        		//System.out.println("Kc,tgs || IDtgs || TS2 || Lifetime2 || Tickettgs"+receive);
     	//给TGS 发送 fa
        		
				if(send_TGS(fa))
				{
					return true;
				}
				
				return false;
        	}
        	else
        	{
        		//加密后的TGS不等，密码错误
        		 JOptionPane.showMessageDialog(null, "密码不正确", "", JOptionPane.ERROR_MESSAGE);
        		 return false;
        		
        	}
        }
        return true;
    }
    public static boolean send_TGS(String fa) throws IOException, ParseException{
    	Socket sockettgs = new Socket(adress_tgs, 5300);
    	//Socket sockettgs = new Socket("127.0.0.1", 5300);
		DataInputStream  inputtgs = new DataInputStream(sockettgs.getInputStream());
		DataOutputStream outputtgs = new DataOutputStream(sockettgs.getOutputStream());
		outputtgs.writeUTF(fa);
		String receive = inputtgs.readUTF();
		//System.out.println("tou + EKc,tgs[Kc,v || IDV || TS4 || Ticketv]"+receive);
		String state = receive.substring(0, 4);
		if(state.equals("1001"))
		{
			String errortype = receive.substring(34, 38);
	     	  if(errortype.equals("0101"))
	     	  {
	     		 JOptionPane.showMessageDialog(null, "TGS解票出错", "", JOptionPane.ERROR_MESSAGE);
	     		  return false;
	     	  }
		}
		
		else 
		{
			String cjietgs = DES_wzj.des_jie_da(receive.substring(34),Kctgs);
			System.out.println("C收到TGS解出来的Ticketv");
			System.out.println(cjietgs.substring(220,604));
			
			String c_send_v = split(receive.substring(0,34)+cjietgs);
			if(send_V(c_send_v ))
			//发给V
			{
				return true;
			}		
		}
		return false;
    }
    /*
     * 发送给V
     */
    public static boolean send_V(String fa) throws UnknownHostException, IOException, ParseException 
    {
    	//C->V
    	//Socket socketv = new Socket("192.168.43.164", 5400);
    	Socket socketv = new Socket(adress_v, 5400);
		DataInputStream  inputv = new DataInputStream(socketv.getInputStream());
		DataOutputStream outputv = new DataOutputStream(socketv.getOutputStream());
		
		outputv.writeUTF(fa);
		//根据V 反馈的信息进行弹窗EKc,v[TS5+1]
		 String receive1 = inputv.readUTF();
		 if(receive1.substring(0, 4).equals("1001"))
	        {
	     	  String errortype = receive1.substring(34, 38);
	     	  if(errortype.equals("0110"))
	     	  {
	     		  //写入log文件
	     		  JOptionPane.showMessageDialog(null, "V解票据出错", "", JOptionPane.ERROR_MESSAGE);
	     		  return false;
	     	  }
	     	 
	        }
		 else
		 {
			 System.out.println("TS6解密前"+receive1 +"length"+receive1 .length());
			 
			 String datajie = DES_wzj.des_jie_da(receive1.substring(34), Kcv);
			 System.out.println("TS6解密前"+datajie +"length"+datajie.length());
			 //data是解密后的TS5+1
			 String renzheng = datajie.substring(0,152);
			 String huifu = huifu(renzheng); //TS5+1;
			 System.out.println("C恢复出来的TS5+1？？"+huifu);
			 String TS = TSadd(TS5,1);
			 if(huifu.equals(TS))
			 {
				 JOptionPane.showMessageDialog(null, "整个认证成功", "", JOptionPane.ERROR_MESSAGE);
				 return true;
			 }
		 }
	
		return false;
    }

	
    public ui() {
        //设置窗体的位置及大小
        jFrame.setBounds(600, 200, 300, 250);
        //设置一层相当于桌布的东西
        c.setLayout(new BorderLayout());//布局管理器
        //设置按下右上角X号后关闭
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //初始化--往窗体里放其他控件
        init();
        //设置窗体可见
        jFrame.setVisible(true);
    }
    public void init() {
        /*标题部分--North*/
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout());
        titlePanel.add(new JLabel("XXX登录系统"));
        c.add(titlePanel, "North");
        
        /*输入部分--Center*/
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(null);
        a1.setBounds(50, 20, 50, 20);
        a2.setBounds(50, 60, 50, 20);
        fieldPanel.add(a1);
        fieldPanel.add(a2);
        username.setBounds(110, 20, 120, 20);
        //username.setText("hh");
        password.setBounds(110, 60, 120, 20);
       
        fieldPanel.add(username);
        fieldPanel.add(password);
       
        c.add(fieldPanel, "Center");
        
        /*按钮部分--South*/
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(button1); 
        buttonPanel.add(button2);
        buttonPanel.add(button3);
        c.add(buttonPanel, "South");
        SimpleListener ourListener = new SimpleListener();
        button1.addActionListener(ourListener);
        button2.addActionListener(ourListener);
        button3.addActionListener(ourListener);
    }
    //测试
    
    public static void main(String[] args) {
        new ui();
    }
    public static String Create_TS()
    {
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Calendar calendar = Calendar.getInstance();    
	    calendar.setTime(new Date());   
	   // System.out.println();
	    return df.format(calendar.getTime());
    }
    /*
	 *十进制转二进制 
	 */
	public static int[] binaryToDecimal(int n){
		 	int j[] = new int[8];
			    for(int i = 7,k=0;i >= 0; i--,k++)
			    {
			    	j[k]= n >>> i & 1;
			      
			    }
			    return j;
	 }
	/*
	 * 二进制8位8位恢复成
	 */
	public static String huifu(String a)
	{
		int length = a.length();
		char[] M = a.toCharArray();
		int[] m = new int[length];
		for(int i=0;i<length;i++)
		{
			m[i] = M[i]-'0';
			//System.out.print(m[i]);
		}
		
		String huifu = "";
		if(length%8!=0)
		{
			return "非8的整数倍";
		}
		//zhuan
		for(int i=0;i<length;i=i+8)
		{
			char h;
			//System.out.print(m[i]*128 + m[i+1]*64 + m[i+2]*32 + m[i+3]*16 + m[i+4]*8 + m[i+5]*4 + m[i+6]*2 + m[i+7]*1+" ");
			h = (char)(m[i]*128 + m[i+1]*64 + m[i+2]*32 + m[i+3]*16 + m[i+4]*8 + m[i+5]*4 + m[i+6]*2 + m[i+7]*1);
			//System.out.print("h"+h);
			
			huifu = huifu+ String.valueOf(h);
		}
		return huifu;
	}
	/*
	 * 将string字符串编程ascii码二进制编码的string字符串
	 */
	public static String zhuan(String daizhuan) 
	{
		int length = daizhuan.length();
		char M[] = daizhuan.toCharArray();
		int M1[] = new int[M.length];
		int []tmp = new int[8]; 
		String s ="";  //进行二进制的累加
		for(int i=0;i<M.length;i++)
		{
			M1[i] = M[i]; //每一位都是int了，现在开始转换二进制
			tmp =  binaryToDecimal(M1[i]); //每一位都转成了二进制
			    for(int j =0;j<8;j++)
			    {
			    	s = s + String.valueOf(tmp[j]); //加入string中
			    	
			    }
		}
 		return s;	
	}
	
   	/*
	 * 由上一步包 返回下一步要发送的包和出错信息
	 */
	  public static String split(String get)
		{
			//已知状态码前4位，冗余6位，保留24，data格式已知。
	    	String state,remnant,blank,data;
	    	//substring 左闭右开
	    	state = get.substring(0, 4);
	    	remnant = get.substring(4, 10); 
	    	int duoyu = 0;
	    	char[] M =remnant.toCharArray();
	    	int[] m = new int[M.length];
	    	for(int i=0;i<M.length;i++)
	    	{
	    		m[i] = M[i] -'0';
	    		
	    	}
	    	duoyu = m[0]*32 + m[1]*16 + m[2]*8 + m[3]*4 + m[4]*2 + m[5]*1;
	    	blank = get.substring(10, 34);
	    	data = get.substring(34,get.length()-duoyu); 
			//////////
	    	/*
	    	 * 0001 C->AS申请票据 
	    	 * 0010 AS->C返回票据
	    	 * 0011 C->TGS
	    	 * 0100 TGS->C  
	    	 * 
	    	 */
	    	if(state.equals("0001"))
	    	{
	    		
	    	}
	    	else if(state.equals("0010"))
	    	{
	    		//先DES解密
	    		String IDv = "1001";
	    		
	    		String Kc_tgs = data.substring(0, 64);
	    		String IDtgs = data.substring(64, 68);
	    		String TS2 = data.substring(68, 220);
	    		String lifetime2  = data.substring(220, 260);
	    		String Ticket = data.substring(260, 644);
	    		System.out.println("C要发给TGS的ticket");
	    		System.out.println(Ticket);
	    		//先判断同步
	    		String auth = Create_Authenticator(IDc,Kc_tgs);
	    		//发送内容为
	    		System.out.println("C要发给TGS的auth");
	    		System.out.println(auth);
	    		String data1 = IDv + Ticket + auth;
	    		return "0011"+"000000"+"000000000000000000000000"+data1;
	    	}
	    	
	    	else if(state.equals("0100"))
	    	{
	    		
	    		Kcv = data.substring(0, 64);
	    		 System.out.println("authen to v加密用的钥匙"+ Kcv+"length"+Kcv.length());
	    		String IDv = data.substring(64,68);
	    		String TS = data.substring(68, 68+152);
	    		String Ticketv = data.substring(68+152);
	    		System.out.println("C要发给V的ticket");
	    		System.out.println(Ticketv);
	    		System.out.println("C要发给V的ticket长度");
	    		System.out.println(Ticketv.length());
	    		String auth = Create_Authenticator(IDc,Kcv);
	    		System.out.println("C要发给V的auth");
	    		System.out.println(auth);
	    		System.out.println("C要发给V的auth长度");
	    		System.out.println(auth.length());
	    		
	    		String datav = Ticketv + auth;
	    		System.out.println("TS5"+TS5);
	    		
	    		return "0101"+"000000"+"000000000000000000000000"+datav;
	    	
	    	}
	    	return get;
		}
	  /*
  	 * 随机生成56位会话密钥
  	 * 
  	 */
  	public static String create_sessionkey()
  	{
  		
  		String s = "";
  		for(int i=0;i<64;i++)
  		{
  			int r = (int)(2*Math.random()); //每次生成0和1 
  			s = s + r;
  		}
  		return s;
  	}
    public static int patch(String[] a)
	{
		int length;
		length = a[0].length();
		int buzero;
		buzero = length%64;
		if(buzero == 0)
		{
			return 0;
		}
		buzero = 64-buzero;
		//
		for(int i=0;i<buzero;i++)
		{
			a[0] = a[0] + "0";
		}
			return buzero;	
	}
	public static String Create_Authenticator(String IDc,String key)
	{
		
		String ADc = "255.255.255.255";
		String ADczhuan = zhuan(ADc);
		String TS3 = Create_TS();
		TS5 = TS3;
		String TSzhuan  = zhuan(TS3);
		String[] authen = new String[1];
		authen[0] = IDc+ADczhuan+TSzhuan;
		System.out.println("加密前authenticator"+"length"+authen[0].length());
		System.out.println(authen[0]);
		int a = patch(authen); 
		String authjia = DES_wzj.des_jia_da(authen[0], key);
		System.out.println("加密后authenticator"+"length"+authjia.length());
		System.out.println(authjia);
		// 用Kc，tgs加密对称发送 
		return authjia;
	}
  

}