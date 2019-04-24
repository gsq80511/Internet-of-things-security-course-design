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
//AS服务器


public class AS{
	public static void main(String[] args) throws IOException {	
        ServerSocket serverSocket = new ServerSocket(5299);
        System.out.println("server start at:" + new Date());
        Mythread a = new Mythread();
        //接收 + 处理
        while (true) {
            Socket socket = serverSocket.accept();
            a.setSocket(socket);
            new Thread(a).start();
        }
    }
}
    class Mythread  implements Runnable{
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
    	 * 输入一串字符串，转成二进制
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
    			M1[i] = M[i]-'0'; //每一位都是int了，现在开始转换二进制
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
    		for(int i=0;i<56;i++)
    		{
    			int r = (int)(2*Math.random()); //每次生成0和1 
    			s = s + r;
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
    	public static int CreateTicket(String[] ticket,String keydes,String sessionkey,String TS,String lifetime)
    	{
    		
    		String IDc = "0000";
    		String ADc = "255.255.255.255";
    		String ADczhuan = zhuan(ADc);
    		System.out.println(ADczhuan);
    		String IDtgs = "1101";
    		String[] data = new String[1];
    		data[0] = sessionkey + IDc + ADczhuan + IDtgs + TS + lifetime;
    		int databu = patch(data);
    		ticket[0] = data[0];  //其实要keydes 加密后的
    		return databu; 
    		
    	}
    
    	/*
    	 * 由上一步包 返回下一步要发送的包和出错信息
    	 */
        public static String split(String get)
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
        	 */
        	if(state.equals("0001"))
        	{
        		String IDc = data.substring(0, 4);
        		String IDtgs = data.substring(4, 8);
        		String TS1 = data.substring(8, 160);
        		//先判断同步？不同步返回相应出错码
        		/*if(!Confirm_syn(TS1))
        		{
        			return "1001"+"000000"+"000000000000000000000000"+"0100";
        		}*/
        		//IDc database 找到返回加密 找不到
        		if(true)
        		{
        			//用C的密钥加密发送。
        			//假设Kc,56位的0和1 ,DES
        	    	String Kc = "00000000000000000000000000000000000000000000000000000";  //根据数据库口令生成的
        	    	String Ktgs = "00000000000000000000000000000000000000000000000000000"; 
        	    	//会话密钥临时生成
        	    	String Kc_tgs = create_sessionkey();
        	    	System.out.println("临时生成的会话密钥为："+ Kc_tgs);
        	    	String TS2 = Create_TS();
        	    	String TSzhuan = zhuan(TS2);
        	    	String lifetime2 = "99999";
        	    	String lifetimezhuan = zhuan(lifetime2);
        	    	String[] ticket = new String[1];
        	    	CreateTicket(ticket,Ktgs,Kc_tgs,TSzhuan,lifetimezhuan);
        	    	String[] as_dao_c = new String[1];
        	    	as_dao_c[0] = Kc_tgs+IDtgs + TSzhuan + lifetimezhuan+ ticket[0];
        	    	int buas_dao_c = patch(as_dao_c);
        	    	//传输 asdaoc ，记得加密。！！！
        	    	return "0010"+"00000100"+"000000000000000000000000"+as_dao_c[0];
        			
        		}
        		//两种情况
        		else 
        		{
        			return "1001"+"000000"+"000000000000000000000000"+"0010";
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
                    System.out.println("Kc,tgs || IDtgs || TS2 || Lifetime2 || Tickettgs"+fa);
                    output.writeUTF(fa);
                    
                    
                    output.flush();
                    socket.shutdownOutput();
                    socket.close();
                } catch (
                        IOException e) {
                    e.printStackTrace();
                }
            }

    }

