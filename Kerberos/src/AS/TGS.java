package AS;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import DB.mysql;
import DES.DES_wzj;
class Ticket
{
	public String Ksession;
	public String IDc;
	public String ADc;
	public String IDt_v;
	public String TS;
	public String lifetime;
}
class Authenticator
{
	public String IDc;
	public String ADc;
	public String TS;
}
public class TGS {
public static void main(String[] args) throws IOException {
    	
    	//TGS到C的过程
    	
        ServerSocket serverSocket = new ServerSocket(5300);
        System.out.println("server start at:" + new Date());
        Mythread1 a = new Mythread1();
        while (true) {
            Socket socket = serverSocket.accept();
            a.setSocket(socket);
            new Thread(a).start();
        }
    }
   
}
    class Mythread1  implements Runnable{
        Socket socket;
        public void setSocket(Socket socket){
            this.socket = socket;
        }
        @Override
        public void run(){
            //System.out.println(1);
                try {
                    InetAddress address = socket.getInetAddress();
                    System.out.println("connected with address:"+address.getHostAddress());
                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                    
                    String receive1 = input.readUTF();
                  //  System.out.println("tou +IDV || Tickettgs || Authenticatorc"+receive1);
                    String fa = split(receive1);
                   // System.out.println("TGS要发给C的！"+fa);
                    //出错 或者 成功：EKc,tgs[Kc,v || IDV || TS4 || Ticketv]
                    output.writeUTF(fa);
                   
                  
            output.flush();
            socket.shutdownOutput();
                    socket.close();
                } catch (
                        IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
    	/*
		 * TS+1
		 */
		public String TSadd(String TS, int sec) throws ParseException {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// �������ڸ�ʽ
			Date utilDate = df.parse(TS);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(utilDate);
			calendar.add(Calendar.SECOND, sec);
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

    	/*
    	 * 生成TS
    	 */
    	public static String Create_TS()
        {
        	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	Calendar calendar = Calendar.getInstance();    
    	    calendar.setTime(new Date());   
    	   // System.out.println();
    	    return df.format(calendar.getTime());
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
    	 * 判断票据是否超时
    	 */
    	public static boolean Confirm_time(String TS,String lifetime) throws ParseException
    	{
    	 //TS为票据时间，get现在的时间，作差与lifetime比较。大于超期，返回true。
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    		Date utilDate = df.parse(TS);
    		double ts = utilDate.getTime();
    		double lifetime1 = Integer.parseInt(lifetime);
    		double cha = new Date().getTime()-ts;
    		if(cha>=lifetime1)
    		{
    			System.out.println("超时");
    			return false;
    		}
    		else 
    		{
    			return true;
    		}
    		
    	}
    	/*
    	 * 票据，返回补的0，值传给ticket
    	 */
    	public static int CreateTicket(String[] ticket,String keydes,String sessionkey,String TS,String lifetime,String IDc,String IDv)
    	{
    		
    		String ADc = "255.255.255.255";
    		String ADczhuan = zhuan(ADc);
    		System.out.println(ADczhuan);
    		System.out.println("TGS要发送给V的票据AD长度"+ADczhuan.length());
    		String[] data = new String[1];
    		data[0] = sessionkey + IDc + ADczhuan + IDv + TS + lifetime;
    		System.out.println("TGS要发送给V的票据长度(补之前)"+data[0].length());
    		int databu = patch(data);
    	//	System.out.println("不会是64吧！！"+databu);
    		ticket[0] = DES_wzj.des_jia_da(data[0],keydes);  //其实要keydes 加密后的
    		return databu; 
    		
    	}
    	/*
    	 * 把字符串补0，补成64的整数倍
    	 */
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
    		//System.out.println("bu"+buzero);
    		for(int i=0;i<buzero;i++)
    		{
    			a[0] = a[0] + "0";
    		}
    			return buzero;
    		
    	}
        /*
         * 0011  C-> TGS
         * 0100  TGS->C
         */
        public static String split(String get) throws ParseException
        {
            //中途有恢复显示的还要settext。
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
            /*
             * normal
             * 0001 C->AS申请票据
             * 0010 AS->C返回票据
             * 0011 C->TGS
             */
            if(state.equals("0011"))
            {

                String IDv = data.substring(0, 4);
                String Ticket_tgs = data.substring(4, 388);
                //此处应用confirm
                String Authenticator_c = data.substring(388, 708);
                //先判断同步？不同步返回相应出错码
            		/*if(!Confirm_syn(TS1))
            		{
            			return "1001"+"000000"+"000000000000000000000000"+"0100";
            		}*/
              String IDtgs = "1101";
              String Ktgs =  zhuan(mysql.search_pw(IDtgs));
              System.out.println("TGS收到的票据 解密前"+ Ticket_tgs);

              String jiet = DES_wzj.des_jie_da(Ticket_tgs, Ktgs);
              
              Ticket t = new Ticket();
              t.Ksession = jiet.substring(0, 64);
              t.IDc = jiet.substring(64, 68);
              t.ADc = jiet.substring(68, 188);
              t.IDt_v = jiet.substring(188, 192);
              t.TS = jiet.substring(192, 344);
              t.lifetime = jiet.substring(344, 384);
              boolean toot = true;
              toot =  Confirm_time(huifu(t.TS),huifu(t.lifetime));
              if(toot ==false)
              {
            	  return "1001"+"000000"+"000000000000000000000000"+"0011";
              }
              System.out.println("TGS收到的Auth 解密前"+ Authenticator_c+"length"+Authenticator_c.length());
              String jiea = DES_wzj.des_jie_da(Authenticator_c,t.Ksession);
              System.out.println("TGS收到的Auth 解密后"+ jiea+"length"+jiea.length());
              Authenticator a = new Authenticator();
              a.IDc = jiea.substring(0, 4);
              a.ADc = jiea.substring(4, 124);
              a.TS = jiea.substring(124, 276);
                    //用C的密钥加密发送。
                    //假设Kc,56位的0和1 ,DES
                    //String Kc = "00000000000000000000000000000000000000000000000000000";  //根据数据库口令生成的
               String Kv = zhuan(mysql.search_pw(IDv));
               
               System.out.println("TGS 收到的IDc"+t.IDc + "authen 里的IDc"+a.IDc);
                    //会话密钥临时生成
              if(t.IDc.equals(a.IDc))
              {
                    String Kc_v = create_sessionkey();
                    System.out.println("临时生成的会话密钥为："+ Kc_v);
                    System.out.println("临时生成的会话密钥长度"+Kc_v.length());
                    String TS2 = Create_TS();
                    String TSzhuan = zhuan(TS2);
                    System.out.println("TGS要发送给V的票据TS长度"+TSzhuan.length());
                    String lifetime2 = "99999";
                    String lifetimezhuan = zhuan(lifetime2);
                    System.out.println("TGS要发送给V的票据lifetime长度"+lifetimezhuan.length());
                    //String[] ticket = new String[1];
                    String[] Ticket_v = new String[1];
                    CreateTicket(Ticket_v,Kv,Kc_v,TSzhuan,lifetimezhuan,t.IDc,IDv);
                    System.out.println("TGS要发送给V的票据");
                    System.out.println(Ticket_v[0]);
                    System.out.println("TGS要发送给V的票据长度");
                    System.out.println(Ticket_v[0].length());
                    
                    //CreateTicket(ticket,Ktgs,Kc_tgs,TSzhuan,lifetimezhuan);
                    String[] TGS_to_C = new String[1];
                    TGS_to_C[0] = Kc_v+IDv + TSzhuan + Ticket_v[0];
                    int buas_dao_c = patch(TGS_to_C);
                    String tgsjia = DES_wzj.des_jia_da(TGS_to_C[0],t.Ksession) ;
                    new uitgs(TGS_to_C[0],tgsjia,t.Ksession,Kc_v,Kv,Ktgs);
                    return "0100"+"100100"+"000000000000000000000000"+tgsjia;
              }
              //返回TGS出错码
              else 
              {
            	  return  "1001"+"000000"+"000000000000000000000000" +"0101";
              }
             

            }

            return get;
        }

    }



