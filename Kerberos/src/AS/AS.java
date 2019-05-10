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
import DES.*;
import RSA.rsa;
import log.writelog;
//import RSA.*;
//AS服务器
import DB.mysql;

public class AS{
	public static void main(String[] args) throws IOException {	
        ServerSocket serverSocket = new ServerSocket(5299);
        System.out.println("server start at:" + new Date());
        Mythread a = new Mythread();
        //接收 + 处理
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("server accept");
            a.setSocket(socket);
            new Thread(a).start();
            /*while(socket.isClosed()==true) {
            socket = serverSocket.accept();
            a.setSocket(socket);
            new Thread(a).start();
            }*/
            
        }
    }
}
    class Mythread  implements Runnable{
    	private static String AS_PersonalKey="1904224418687832290207474351710659851362039131403472121260109147765768320665288691837384302733004665164426618537907696015945110556503320283527137259190913";
        private static String AS_PublicKey="7168130713816454038100358793110942830483283087581237932856046709771692040864103191351343603638539598982200762071731689360973274442110986430996112040029763";


    
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
    	 * 票据，返回补的0，值传给ticket
    	 */
    	public static int CreateTicket(String[] ticket,String keydes,String sessionkey,String TS,String lifetime,String IDc,String IDtgs)
    	{
    		
    		String ADc = "255.255.255.255";
    		String ADczhuan = zhuan(ADc);
    		System.out.println(ADczhuan);
    		
    		String[] data = new String[1];
    		data[0] = sessionkey + IDc + ADczhuan + IDtgs + TS + lifetime;
    		int databu = patch(data);
    		ticket[0] = DES_wzj.des_jia_da(data[0],keydes);  //其实要keydes 加密后的
    		return databu; 
    		
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
    	 * 由上一步包 返回下一步要发送的包和出错信息
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
        	System.out.println("length"+M.length);
        	for(int i=0;i<M.length;i++)
        	{
        		m[i] = M[i] -'0';
        		System.out.print(m[i]);
        	}
        	System.out.println();
        	duoyu = m[0]*32 + m[1]*16 + m[2]*8 + m[3]*4 + m[4]*2 + m[5]*1;
        	System.out.print("duoyu"+duoyu);
        	blank = get.substring(10, 34);
        	data = get.substring(34,get.length()-duoyu); 
        	System.out.println("dtaa"+data);
        	/*
        	 * normal
        	 * 0001 C->AS申请票据 
        	 * 0010 AS->C返回票据
        	 * 0011 C->TGS
        	 */
        	if(state.equals("0001"))
        	{
        		String IDc = data.substring(0, 4);
        		System.out.println("server收到了"+IDc +"的请求");
        		String IDtgs = data.substring(4, 8);
        		String TS1 = data.substring(8, 160);
        		TS1=huifu(TS1);
        		//先判断同步？不同步返回相应出错码
        		if(!Confirm_syn(TS1))
        		{
        			return "1001"+"000000"+"000000000000000000000000"+"0100";
                }
        		
        		//IDc database 找到返回加密 找不到
        		
        		if(!mysql.search_UID(IDc))
        		{
        			System.out.println("找不到用户");
        			writelog.write_log(Create_TS()+"找不到用户","src//AS//");
        			return "1001"+"000000"+"000000000000000000000000"+"0001";
        		}
        		else
        		{
        			//用C的密钥加密发送。
        			//假设Kc,56位的0和1 ,DES
        			
        			String pwd_c = mysql.search_pw(IDc);
        			System.out.println("密码为"+pwd_c);
        	    	//String Kc = "00000000000000000000000000000000000000000000000000000";  //根据数据库口令生成的
        			String Ktgs = zhuan(mysql.search_pw(IDtgs));
        			//String Ktgs = "00000000000000000000000000000000000000000000000000000"; 
        		
        			String Kc = zhuan(mysql.search_pw(IDc));
        			System.out.println("生成的kc为"+Kc);
        			//会话密钥临时生成			
        	    	String Kc_tgs = create_sessionkey();
        	    	System.out.println("临时生成的会话密钥为："+ Kc_tgs);
        	    	String TS2 = Create_TS();
        	    	String TSzhuan = zhuan(TS2);
        	    	String lifetime2 = "99999";
        	    	String lifetimezhuan = zhuan(lifetime2);
        	    	String[] ticket = new String[1];
        	    	CreateTicket(ticket,Ktgs,Kc_tgs,TSzhuan,lifetimezhuan,IDc,IDtgs);
        	    	System.out.println("AS生成的TGS票据"+ticket[0]);
        	    	String[] as_dao_c = new String[1];
        	    	as_dao_c[0] = Kc_tgs+zhuan(IDtgs) + TSzhuan + lifetimezhuan+ ticket[0];
        	    	int buas_dao_c = patch(as_dao_c);
        	    	String send = "0010"+"111100"+"000000000000000000000000" + as_dao_c[0];
        	    	System.out.println("加密前data"+ as_dao_c[0]);
        	    	String jmh = DES_wzj.des_jia_da(as_dao_c[0], Kc) ;
        	    	String sendz = "0010"+"100000"+"000000000000000000000000" + jmh ;
        	    	System.out.println("加密后data"+jmh);
        	    	
//        	       	String xs_send = decimaltox(send);
//        	    	String xs_sendz = decimaltox(sendz);
//        	    	String xs_Kc_tgs = decimaltox(Kc_tgs);
//        	    	String xs_Kc = decimaltox(Kc);
//        	    	new uias(xs_send,xs_sendz,xs_Kc_tgs,xs_Kc);
        	    	new uias(send,sendz,Kc_tgs,Kc);
        	    	//传输 asdaoc ，记得加密。！！！
        	    	return sendz;
        			}
        		
        		}

        	
        	//注册！！！！！
            else if(state.equals("0111"))

                    {
            	data=rsa.RSA_Decryption(data,AS_PersonalKey,AS_PublicKey);

                        String IDc = data.substring(0, 4);
                        String pwd=data.substring(4,12);
                        /*String Kc = zhuan(mysql.search_pw(IDc));*/
                        /*System.out.println("server收到了"+IDc +"的请求");
                        String IDtgs = data.substring(4, 8);
                        String TS1 = data.substring(8, 160);*/
                          if(mysql.search_UID(IDc))//用户民已经存在
                        {
                            System.out.println("该用户已经存在");
                            String Kc = zhuan(pwd);
                            String[] errorcode_0 = new String[1];
                            errorcode_0[0] = "1111";
                            patch(errorcode_0);
                            String errorcode=errorcode_0[0];
                            String jmh = DES_wzj.des_jia_da(errorcode, Kc) ;
                            String sendz = "1001" + "000000" + "000000000000000000000000" + jmh ;
                            //System.out.println(jmh);
                            return sendz;
                        }
                          else if(!mysql.search_UID(IDc))
                        {
                            mysql.insert(IDc,pwd);
                            String Kc = zhuan(pwd);
                            String[] errorcode_0 = new String[1];
                            errorcode_0[0] = "1111";
                            patch(errorcode_0);
                            String errorcode=errorcode_0[0];
                            String jmh = DES_wzj.des_jia_da(errorcode,Kc ) ;
                            String sendz = "1000" + "000000" + "000000000000000000000000" + jmh ;
                            //System.out.println(jmh);
                            return sendz;
                            //return "1000" + "000000" + "000000000000000000000000" + "0000";
                        }
                    }
        	return get;
    	
    	}
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
                    System.out.println("IDC ||IDtgs ||TS1"+receive1);
                    //接收到的
                    String fa = split(receive1);
                    System.out.println("Kc,tgs || IDtgs || TS2 || Lifetime2 || Tickettgs\n"+fa);
                    output.writeUTF(fa);
                    
                    output.flush();
                    //socket.shutdownOutput();
                    //socket.close();
                    
                } catch (
                        IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }

        	public static String decimaltox(String x1)
        	{
        		
        		char[] x = x1.toCharArray();
        		int length = x.length;
        		char[] k = new char[length/4];
        		int[] r =new int[4];
        		int temp ;
        		for(int i=0,j=0;i+3<length;i=i+4,j++)
        		{
        			r[0] = x[i]-'0';
        			r[1] = x[i+1]-'0';
        			r[2] = x[i+2]-'0';
        			r[3] = x[i+3]-'0';
        			temp = r[0]*8+r[1]*4+r[2]*2+r[3];
        			
        			if(temp<10)
        			{
        				String str1 = String.valueOf(temp);
        				//k[j]=str1;
        				///char[] arr = str1.toCharArray();
        				/*for (int j = 0; j < arr.length; j++) {
        				    System.out.print(arr[j]);
        				}*/
        				//k[j]=arr[arr.length-1];
        			}
        			else if(temp==10)
        			{
        				k[j]='A';
        			}
        			else if(temp==11)
        			{
        				k[j]='B';
        			}
        			else if(temp==12)
        			{
        				k[j]='C';
        			}
        			else if(temp==13)
        			{
        				k[j]='D';
        			}
        			else if(temp==14)
        			{
        				k[j]='E';
        			}
        			else if(temp==15)
        			{
        				k[j]='F';
        			}
        			
        			
        		}
        		String k1 = k.toString();
        		return k1;
        	}
        

    }

