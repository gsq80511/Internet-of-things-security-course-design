package AS;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DES.DES_wzj;


public class V {

/*映射用户名+ 临时会话密钥 */  
	
	
public static Map<String, String> users_Kcv = new HashMap<String, String>();  
/*映射用户名+ 公钥 */  
public static Map<String, String> users_Kc = new HashMap<String, String>();  

public static void main(String[] args) throws IOException {
    	
    	//liaotianshi
	   // startuser_Kc();
	    Mythread3 t = new Mythread3();
	    new Thread(t).start();
		
        ServerSocket serverSocket = new ServerSocket(5400);
        System.out.println("server start at:" + new Date());
        Mythread2 a = new Mythread2();
        while (true) {
            Socket socket = serverSocket.accept();
            a.setSocket(socket);
            new Thread(a).start();
        }
    }
}

	class Mythread3 implements Runnable {
		
		@Override
		public void run() {
		  NIOSServer server = new NIOSServer(8080);  
	        server.listen();  
		}
		
	}

	class Mythread2 implements Runnable {
		// cache��id��ad
		  Socket socket;
	        public void setSocket(Socket socket){
	            this.socket = socket;
	        }
	    
	    
		private String Kv =zhuan("00000000");
		private String Kcv;
		private String id;
		private String TS;
		private String lifetime;
		

		/*
		 * ���ַ�����0������64��������
		 */
		public int patch(String[] a) {
			int length;
			length = a[0].length();
			int buzero;
			buzero = length % 64;
			if(buzero == 0)
    		{
    			return 0;
    		}
			buzero = 64 - buzero;
			// System.out.println("bu"+buzero);
			for (int i = 0; i < buzero; i++) {
				a[0] = a[0] + "0";
			}
			return buzero;

		}

		/*
		 * ʮ����ת������
		 */
		public static int[] binaryToDecimal(int n) {
			int j[] = new int[8];
			for (int i = 7, k = 0; i >= 0; i--, k++) {
				j[k] = n >>> i & 1;

			}
			return j;
		}

		/*/*
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
	 * 判断是否同步函数
	 */
	public static boolean  Confirm_syn(String TS) throws ParseException
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		Date utilDate = df.parse(TS);
		double ts = utilDate.getTime();
		System.out.println(ts);
		double syn_time = Double.parseDouble("300"); //这里与300s进行比较
		System.out.println(syn_time );
		double cha = new Date().getTime()-ts;
		cha = cha/1000;
		System.out.println("cha"+cha);
		if(cha>=syn_time)
		{
			System.out.println("票据超时");
			return false;
		}
		else 
		{
			System.out.println("票据没有过期");
			return true;
		}	
		
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
		 * ����TS
		 */
		public String Create_TS() {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			// System.out.println();
			return df.format(calendar.getTime());
		}

		/*
		 * ����һ���� ������һ��Ҫ���͵İ��ͳ�����Ϣ
		 */
		public String split(String get) throws ParseException {
			// ��;�лָ���ʾ�Ļ�Ҫsettext��
			// ��֪״̬��ǰ4λ������6λ������24��data��ʽ��֪��
			String state, remnant, blank, data;
			// substring ����ҿ�
			state = get.substring(0, 4);
			remnant = get.substring(4, 10);
			int duoyu = 0;
			char[] M = remnant.toCharArray();
			int[] m = new int[M.length];
			for (int i = 0; i < M.length; i++) {
				m[i] = M[i] - '0';

			}
			duoyu = m[0] * 32 + m[1] * 16 + m[2] * 8 + m[3] * 4 + m[4] * 2 + m[5] * 1;
			blank = get.substring(10, 34);
			data = get.substring(34, get.length() - duoyu);
			/*
			 * 0101 C->V 发送
			 */
			if (state.equals("0101")) {
				String Ticketv = data.substring(0, 384);
				// ��Kv����Ticketv���õ���
				Ticketv = DES_wzj.des_jie_da(Ticketv,Kv);
				Ticket tv = new Ticket();
				tv.Ksession = Ticketv.substring(0, 64);// 56
				Kcv = tv.Ksession;
				 System.out.println("v提取的Kcv"+ tv.Ksession+"length"+tv.Ksession.length());
				tv.IDc = Ticketv.substring(64, 68);// 56
				tv.ADc = Ticketv.substring(68, 188);// 56
				tv.IDt_v = Ticketv.substring(188, 192);// 56
				tv.TS = Ticketv.substring(192, 344); 
				tv.lifetime =  Ticketv.substring(344, 384);// 56
				boolean toot = true;
	              toot =  Confirm_time(huifu(tv.TS),huifu(tv.lifetime));
	              if(toot ==false)
	              {
	            	  return "1001"+"000000"+"000000000000000000000000"+"0111";
	              }


				String Authenticatorc = data.substring(384, 704);// 320
				System.out.println("V收到的authc 解密前");
				System.out.println(Authenticatorc);
				// ��Kcv����Authenticatorc���õ���
				String Authenticatorcjie =DES_wzj.des_jie_da(Authenticatorc, tv.Ksession);
				Authenticator a = new Authenticator();
				System.out.println("V收到的authc 解密后");
				System.out.println(Authenticatorcjie);
				a.IDc = Authenticatorcjie.substring(0, 4);
				a.ADc = Authenticatorcjie.substring(4, 124);
				a.TS = Authenticatorcjie.substring(124, 276);// 152
				//返回V解票据出错
				System.out.println("ticketv ->IDc"+tv.IDc);
				System.out.println("a ->IDc"+a.IDc);
				System.out.println("获得的TS5"+a.TS+"length" + a.TS.length());
				System.out.println("获得的TS5(恢复？？)"+huifu(a.TS));
				
				if(!tv.IDc.equals(a.IDc))
				{
				//////写入log
					return "1001" + "000000" + "000000000000000000000000"+"0110";
				}
				id = a.IDc;
				//解开了Ticketv 和 Authenticatorc
				String[] TS6 = new String[1];
				TS6[0] = TSadd(huifu(a.TS), 1);
				String[] TS6zhuan = new String[1];
				TS6zhuan[0] = zhuan(TS6[0]);
				patch(TS6zhuan);
				String V_send_C = DES_wzj.des_jia_da(TS6zhuan[0] ,tv.Ksession);
				System.out.println("TS(6)"+V_send_C  +"length"+V_send_C .length());
				new uiv(TS6zhuan[0],V_send_C,tv.Ksession);
				
				System.out.println("V put ------- "+a.IDc+" "+tv.Ksession);
				V.users_Kcv.put(a.IDc, tv.Ksession);
				return "0110"+"101000"+"000000000000000000000000"+V_send_C;
				
				/*
				 * if(!Confirm_syn(TS1)) { return
				 * "1001"+"000000"+"000000000000000000000000"+"0100"; }
				 */
				// ��֤��ݣ�
				/*if (!(IDc.equals(IDc_2) && ADc.equals(ADc_2)))
					return null;// ��ݴ��󣬷��ش����룬д��log
				else
					try {
						if (!Confirm_time(huifu(TS4), huifu(lifetime4)))
							return null;// ��ʱ�����ش����룬д��log
					} catch (ParseException e) {
						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
					}
				
				/*id = IDc;
				TS=TS4;
				lifetime=lifetime4;
				Kcv=Kcv1;*/
				// ���������ַ�����6��//��Kcv����TS5+1
			/*	String TS6 = null;
				try {
					TS6 = TSadd(huifu(TS5), 1);
				} catch (ParseException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
				String TS6zhuan = zhuan(TS6);

				// 64*3-152=192-152=40
				return "0110" + "101010" + "0000000000" + "0000000000" + "0000000000" + "0000000000" + TS6zhuan;
			} else
				return null;// ���ݰ�����������
*/
			}
			return get;
		}

		@Override
		public void run() {
			PrintWriter pw = null;
			// System.out.println(1);
			try {
				InetAddress address = socket.getInetAddress();
				System.out.println("connected with address:" + address.getHostAddress());

				DataInputStream input = new DataInputStream(socket.getInputStream());
				DataOutputStream output = new DataOutputStream(socket.getOutputStream());
				System.out.println("1");
				String receive1 = input.readUTF();
				System.out.println("2");
				 System.out.println("Ticketv");
				 System.out.println(receive1.substring(34,34+384)); 
				 System.out.println("Authenticator" );
				 System.out.println(receive1.substring(34+384));
				 String fa = split(receive1);
				 System.out.println("V要发给C的" + fa);
				
			/*	if(!fa.substring(0, 4).equals("1001"))
				{
					System.out.println("V put   "+id+" "+Kcv);
					if(V.users_Kcv.containsKey(id))
						V.users_Kcv.remove(id);
					
					V.users_Kcv.put(id, Kcv);
				}*/
				output.writeUTF(fa);
				output.flush();
				//socket.shutdownOutput();
				//socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

}

	


   


