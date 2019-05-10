package as;
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
<<<<<<< HEAD
//AS������

=======
import DES.*;
//AS服务器
import DB.mysql;
>>>>>>> refs/remotes/origin/master

public class AS{
	public static void main(String[] args) throws IOException {	
        ServerSocket serverSocket = new ServerSocket(5299);
        System.out.println("server start at:" + new Date());
        Mythread a = new Mythread();
        //���� + ����
        while (true) {
            Socket socket = serverSocket.accept();
            a.setSocket(socket);
            new Thread(a).start();
        }
    }
}
    class Mythread  implements Runnable{
    	/*
    	 * ���ַ�����0������64��������
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
    	 *ʮ����ת������ 
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
<<<<<<< HEAD
    	 * ����һ���ַ�����ת�ɶ�����
=======
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
>>>>>>> refs/remotes/origin/master
    	 */
    	public static String zhuan(String daizhuan) 
    	{
    		int length = daizhuan.length();
    		char M[] = daizhuan.toCharArray();
    		int M1[] = new int[M.length];
    		int []tmp = new int[8]; 
    		String s ="";  //���ж����Ƶ��ۼ�
    		for(int i=0;i<M.length;i++)
    		{
<<<<<<< HEAD
    			M1[i] = M[i]-'0'; //ÿһλ����int�ˣ����ڿ�ʼת��������
    			tmp =  binaryToDecimal(M1[i]); //ÿһλ��ת���˶�����
=======
    			M1[i] = M[i]; //每一位都是int了，现在开始转换二进制
    			tmp =  binaryToDecimal(M1[i]); //每一位都转成了二进制
>>>>>>> refs/remotes/origin/master
    			    for(int j =0;j<8;j++)
    			    {
    			    	s = s + String.valueOf(tmp[j]); //����string��
    			    	
    			    }
    		}
     		return s;	
    	}
    	
    	
    	/*
    	 * �������56λ�Ự��Կ
    	 * 
    	 */
    	public static String create_sessionkey()
    	{
    		
    		String s = "";
    		for(int i=0;i<64;i++)
    		{
    			int r = (int)(2*Math.random()); //ÿ������0��1 
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
    	 * �ж��Ƿ�ͬ������
    	 */
    	public static boolean  Confirm_syn(String TS) throws ParseException
    	{
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
    		Date utilDate = df.parse(TS);
    		double ts = utilDate.getTime();
    		System.out.println(ts);
    		double syn_time = Double.parseDouble("300"); //������300s���бȽ�
    		System.out.println(syn_time );
    		double cha = new Date().getTime()-ts;
    		cha = cha/1000;
    		System.out.println("cha"+cha);
    		if(cha>=syn_time)
    		{
    			System.out.println("Ʊ�ݳ�ʱ");
    			return false;
    		}
    		else 
    		{
    			System.out.println("Ʊ��û�й���");
    			return true;
    		}	
    		
    	}
<<<<<<< HEAD
    	/*
    	 * �ж�Ʊ���Ƿ�ʱ
    	 */
    	public static boolean Confirm_time(String TS,String lifetime) throws ParseException
    	{
    	 //TSΪƱ��ʱ�䣬get���ڵ�ʱ�䣬������lifetime�Ƚϡ����ڳ��ڣ�����true��
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
    		Date utilDate = df.parse(TS);
    		double ts = utilDate.getTime();
    		double lifetime1 = Integer.parseInt(lifetime);
    		double cha = new Date().getTime()-ts;
    		if(cha>=lifetime1)
    		{
    			System.out.println("��ʱ");
    			return false;
    		}
    		else 
    		{
    			return true;
    		}
    		
    	}
    	/*
    	 * ����TS
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
    	 * Ʊ�ݣ����ز���0��ֵ����ticket
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
    		ticket[0] = data[0];  //��ʵҪkeydes ���ܺ��
    		return databu; 
    		
    	}
=======
>>>>>>> refs/remotes/origin/master
    
    	/*
    	 * ����һ���� ������һ��Ҫ���͵İ��ͳ�����Ϣ
    	 */
        public static String split(String get) throws ParseException
    	{
        	//��;�лָ���ʾ�Ļ�Ҫsettext��
    		//��֪״̬��ǰ4λ������6λ������24��data��ʽ��֪��
        	String state,remnant,blank,data;
        	//substring ����ҿ�
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
<<<<<<< HEAD
        	 * 0001 C->AS����Ʊ�� 
        	 * 0010 AS->C����Ʊ��
=======
        	 * 0001 C->AS申请票据 
        	 * 0010 AS->C返回票据
        	 * 0011 C->TGS
>>>>>>> refs/remotes/origin/master
        	 */
        	if(state.equals("0001"))
        	{
        		String IDc = data.substring(0, 4);
        		System.out.println("server收到了"+IDc +"的请求");
        		String IDtgs = data.substring(4, 8);
        		String TS1 = data.substring(8, 160);
<<<<<<< HEAD
        		//���ж�ͬ������ͬ��������Ӧ������
        		/*if(!Confirm_syn(TS1))
=======
        		TS1=huifu(TS1);
        		//先判断同步？不同步返回相应出错码
        		if(!Confirm_syn(TS1))
>>>>>>> refs/remotes/origin/master
        		{
        			return "1001"+"000000"+"000000000000000000000000"+"0100";
<<<<<<< HEAD
        		}*/
        		//IDc database �ҵ����ؼ��� �Ҳ���
        		if(true)
=======
                }
        		
        		//IDc database 找到返回加密 找不到
        		
        		if(!mysql.search_UID(IDc))
        		{
        			System.out.println("找不到用户");
        			return "1001"+"000000"+"000000000000000000000000"+"0001";
        		}
        		else
>>>>>>> refs/remotes/origin/master
        		{
<<<<<<< HEAD
        			//��C����Կ���ܷ��͡�
        			//����Kc,56λ��0��1 ,DES
        	    	String Kc = "00000000000000000000000000000000000000000000000000000";  //�������ݿ�������ɵ�
        	    	String Ktgs = "00000000000000000000000000000000000000000000000000000"; 
        	    	//�Ự��Կ��ʱ����
=======
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
>>>>>>> refs/remotes/origin/master
        	    	String Kc_tgs = create_sessionkey();
        	    	System.out.println("��ʱ���ɵĻỰ��ԿΪ��"+ Kc_tgs);
        	    	String TS2 = Create_TS();
        	    	String TSzhuan = zhuan(TS2);
        	    	String lifetime2 = "99999";
        	    	String lifetimezhuan = zhuan(lifetime2);
        	    	String[] ticket = new String[1];
        	    	CreateTicket(ticket,Ktgs,Kc_tgs,TSzhuan,lifetimezhuan,IDc,IDtgs);
        	    	System.out.println("AS生成的TGS票据"+ticket[0]);
        	    	String[] as_dao_c = new String[1];
        	    	as_dao_c[0] = Kc_tgs+IDtgs + TSzhuan + lifetimezhuan+ ticket[0];
        	    	int buas_dao_c = patch(as_dao_c);
<<<<<<< HEAD
        	    	//���� asdaoc ���ǵü��ܡ�������
        	    	return "0010"+"00000100"+"000000000000000000000000"+as_dao_c[0];
        			
        		}
        		//�������
        		else 
        		{
        			return "1001"+"000000"+"000000000000000000000000"+"0010";
        		}
=======
        	    	String send = "0010"+"111100"+"000000000000000000000000" + as_dao_c[0];
        	    	System.out.println("加密前data"+ as_dao_c[0]);
        	    	String jmh = DES_wzj.des_jia_da(as_dao_c[0], Kc) ;
        	    	String sendz = "0010"+"111100"+"000000000000000000000000" + jmh ;
        	    	System.out.println("加密后data"+jmh);
        	    	
        	    	new uias(send,sendz,Kc_tgs,Kc);
        	    	//传输 asdaoc ，记得加密。！！！
        	    	return sendz;
        			}
>>>>>>> refs/remotes/origin/master
        		
        		}

        	
        	//注册！！！！！
        	else if(state.equals("0111"))
        			
        			{
        		
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
                    //���յ���
                    String fa = split(receive1);
                    System.out.println("Kc,tgs || IDtgs || TS2 || Lifetime2 || Tickettgs\n"+fa);
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

    }

