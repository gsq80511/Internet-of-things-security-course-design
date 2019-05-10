package AS;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;


public class client {
	/*
	 * 把字符串补0，补成64的整数倍
	 */
	public static int patch(String[] a)
	{
		int length;
		length = a[0].length();
		int buzero;
		buzero = length%64;
		buzero = 64-buzero;
		//System.out.println("bu"+buzero);
		for(int i=0;i<buzero;i++)
		{
			a[0] = a[0] + "0";
		}
			return buzero;	
	}
	
    public static void main(String[] args) throws Exception {

    	//页面点击确定要干的事情
    	
        System.out.println("--client--");

        //Socket(,)内参数分别设为服务端IP和端口
        Socket socket = new Socket("172.20.10.6", 5299);

        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        String TS1 = Create_TS();
        String TS = zhuan(TS1);
        String IDc = "0000";
        String IDtgs = "1101";
        
        String state = "0001";
        String remnant = "000000";
        String blank = "0000000000000000000000000000000000000";
        String data = IDc+ IDtgs +TS;
        String send = state + remnant + blank +data ;
        String x = "qiuqiu";
        output.writeUTF(send);
        //发送
        System.out.println("--client--");
        String receive = input.readUTF();
        String Kctgs = receive.substring(34,90);
        String fa = split(receive);
        if(fa.substring(0, 4).equals("1001"))
        {
     	  String errortype = fa.substring(34, 38);
     	  if(errortype.equals("0001"))
     	  {
     		  //写入log文件
     		  JOptionPane.showMessageDialog(null, "标题【出错啦】", "无此用户", JOptionPane.ERROR_MESSAGE);
     	  }
     	  else  if(errortype.equals("0010"))
     	  {
     		  JOptionPane.showMessageDialog(null, "标题【出错啦】", "密码不正确", JOptionPane.ERROR_MESSAGE);
     		  
     	  }
        }
        //接收来的恢复先。
       // String huifu = huifu(receive);
        else
        {
     	   System.out.println(Kctgs);
     	   System.out.println("Kc,tgs || IDtgs || TS2 || Lifetime2 || Tickettgs"+receive);
     	   output.writeUTF(fa);
			
     	   // 假装进入聊天室（无TGS）
     	  
     	   
        }
        
       // System.out.println(huifu);
        
        
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
	public static int[] binaryToDecimal(int n){
	 	int j[] = new int[8];
		    for(int i = 7,k=0;i >= 0; i--,k++)
		    {
		    	j[k]= n >>> i & 1;
		      
		    }
		    return j;
 }
	public static String Create_Authenticator(String key)
	{
		String IDc = "0000";
		String ADc = "255.255.255.255";
		String ADczhuan = zhuan(ADc);
		String TS3 = Create_TS();
		String TSzhuan  = zhuan(TS3);
		String[] authen = new String[1];
		authen[0] = IDc+ADczhuan+TSzhuan;
		int a = patch(authen);  
		// 用Kc，tgs加密对称发送 
		return authen[0];
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
		}
		String huifu = "";
		if(length%8!=0)
		{
			return "非8的整数倍";
		}
		//zhuan
		for(int i=0;i<length;i=i+8)
		{
			char h ; 
			h = (char)(m[i]*128 + m[i+1]*64 + m[i+2]*32 + m[i+3]*16 + m[i+4]*8 + m[i+5]*4 + m[i+6]*2 + m[i+7*1]);
			huifu = huifu+h;
		}
		return huifu;
	}
	
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
    	 */
    	if(state.equals("0001"))
    	{
    		
    	}
    	else if(state.equals("0010"))
    	{
    		//先DES解密
    		String IDv = "1001";
    		String Kc_tgs = data.substring(0, 56);
    		String IDtgs = data.substring(56, 60);
    		String TS2 = data.substring(60, 212);
    		String lifetime2  = data.substring(212, 252);
    		String Ticket = data.substring(252, 636);
    		//先判断同步
    		String auth = Create_Authenticator(Kc_tgs);
    		//发送内容为
    		String data1 = IDv + Ticket + auth;
    		return "0011"+"000000"+"000000000000000000000000"+data1;
    	}
    	return null;
	}
}
