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
//AS·şÎñÆ÷

=======
import DES.*;
//ASæœåŠ¡å™¨
import DB.mysql;
>>>>>>> refs/remotes/origin/master

public class AS{
	public static void main(String[] args) throws IOException {	
        ServerSocket serverSocket = new ServerSocket(5299);
        System.out.println("server start at:" + new Date());
        Mythread a = new Mythread();
        //½ÓÊÕ + ´¦Àí
        while (true) {
            Socket socket = serverSocket.accept();
            a.setSocket(socket);
            new Thread(a).start();
        }
    }
}
    class Mythread  implements Runnable{
    	/*
    	 * °Ñ×Ö·û´®²¹0£¬²¹³É64µÄÕûÊı±¶
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
    	 *Ê®½øÖÆ×ª¶ş½øÖÆ 
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
    	 * ÊäÈëÒ»´®×Ö·û´®£¬×ª³É¶ş½øÖÆ
=======
    	 * äºŒè¿›åˆ¶8ä½8ä½æ¢å¤æˆ
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
    			return "é8çš„æ•´æ•°å€";
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
    	 * å°†stringå­—ç¬¦ä¸²ç¼–ç¨‹asciiç äºŒè¿›åˆ¶ç¼–ç çš„stringå­—ç¬¦ä¸²
>>>>>>> refs/remotes/origin/master
    	 */
    	public static String zhuan(String daizhuan) 
    	{
    		int length = daizhuan.length();
    		char M[] = daizhuan.toCharArray();
    		int M1[] = new int[M.length];
    		int []tmp = new int[8]; 
    		String s ="";  //½øĞĞ¶ş½øÖÆµÄÀÛ¼Ó
    		for(int i=0;i<M.length;i++)
    		{
<<<<<<< HEAD
    			M1[i] = M[i]-'0'; //Ã¿Ò»Î»¶¼ÊÇintÁË£¬ÏÖÔÚ¿ªÊ¼×ª»»¶ş½øÖÆ
    			tmp =  binaryToDecimal(M1[i]); //Ã¿Ò»Î»¶¼×ª³ÉÁË¶ş½øÖÆ
=======
    			M1[i] = M[i]; //æ¯ä¸€ä½éƒ½æ˜¯intäº†ï¼Œç°åœ¨å¼€å§‹è½¬æ¢äºŒè¿›åˆ¶
    			tmp =  binaryToDecimal(M1[i]); //æ¯ä¸€ä½éƒ½è½¬æˆäº†äºŒè¿›åˆ¶
>>>>>>> refs/remotes/origin/master
    			    for(int j =0;j<8;j++)
    			    {
    			    	s = s + String.valueOf(tmp[j]); //¼ÓÈëstringÖĞ
    			    	
    			    }
    		}
     		return s;	
    	}
    	
    	
    	/*
    	 * Ëæ»úÉú³É56Î»»á»°ÃÜÔ¿
    	 * 
    	 */
    	public static String create_sessionkey()
    	{
    		
    		String s = "";
    		for(int i=0;i<64;i++)
    		{
    			int r = (int)(2*Math.random()); //Ã¿´ÎÉú³É0ºÍ1 
    			s = s + r;
    		}
    		return s;
    	}

    	/*
    	 * ç”ŸæˆTS
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
    	 * ç¥¨æ®ï¼Œè¿”å›è¡¥çš„0ï¼Œå€¼ä¼ ç»™ticket
    	 */
    	public static int CreateTicket(String[] ticket,String keydes,String sessionkey,String TS,String lifetime,String IDc,String IDtgs)
    	{
    		
    		String ADc = "255.255.255.255";
    		String ADczhuan = zhuan(ADc);
    		System.out.println(ADczhuan);
    		
    		String[] data = new String[1];
    		data[0] = sessionkey + IDc + ADczhuan + IDtgs + TS + lifetime;
    		int databu = patch(data);
    		ticket[0] = DES_wzj.des_jia_da(data[0],keydes);  //å…¶å®è¦keydes åŠ å¯†åçš„
    		return databu; 
    		
    	}
    	/*
    	 * ÅĞ¶ÏÊÇ·ñÍ¬²½º¯Êı
    	 */
    	public static boolean  Confirm_syn(String TS) throws ParseException
    	{
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//ÉèÖÃÈÕÆÚ¸ñÊ½
    		Date utilDate = df.parse(TS);
    		double ts = utilDate.getTime();
    		System.out.println(ts);
    		double syn_time = Double.parseDouble("300"); //ÕâÀïÓë300s½øĞĞ±È½Ï
    		System.out.println(syn_time );
    		double cha = new Date().getTime()-ts;
    		cha = cha/1000;
    		System.out.println("cha"+cha);
    		if(cha>=syn_time)
    		{
    			System.out.println("Æ±¾İ³¬Ê±");
    			return false;
    		}
    		else 
    		{
    			System.out.println("Æ±¾İÃ»ÓĞ¹ıÆÚ");
    			return true;
    		}	
    		
    	}
<<<<<<< HEAD
    	/*
    	 * ÅĞ¶ÏÆ±¾İÊÇ·ñ³¬Ê±
    	 */
    	public static boolean Confirm_time(String TS,String lifetime) throws ParseException
    	{
    	 //TSÎªÆ±¾İÊ±¼ä£¬getÏÖÔÚµÄÊ±¼ä£¬×÷²îÓëlifetime±È½Ï¡£´óÓÚ³¬ÆÚ£¬·µ»Øtrue¡£
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//ÉèÖÃÈÕÆÚ¸ñÊ½
    		Date utilDate = df.parse(TS);
    		double ts = utilDate.getTime();
    		double lifetime1 = Integer.parseInt(lifetime);
    		double cha = new Date().getTime()-ts;
    		if(cha>=lifetime1)
    		{
    			System.out.println("³¬Ê±");
    			return false;
    		}
    		else 
    		{
    			return true;
    		}
    		
    	}
    	/*
    	 * Éú³ÉTS
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
    	 * Æ±¾İ£¬·µ»Ø²¹µÄ0£¬Öµ´«¸øticket
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
    		ticket[0] = data[0];  //ÆäÊµÒªkeydes ¼ÓÃÜºóµÄ
    		return databu; 
    		
    	}
=======
>>>>>>> refs/remotes/origin/master
    
    	/*
    	 * ÓÉÉÏÒ»²½°ü ·µ»ØÏÂÒ»²½Òª·¢ËÍµÄ°üºÍ³ö´íĞÅÏ¢
    	 */
        public static String split(String get) throws ParseException
    	{
        	//ÖĞÍ¾ÓĞ»Ö¸´ÏÔÊ¾µÄ»¹Òªsettext¡£
    		//ÒÑÖª×´Ì¬ÂëÇ°4Î»£¬ÈßÓà6Î»£¬±£Áô24£¬data¸ñÊ½ÒÑÖª¡£
        	String state,remnant,blank,data;
        	//substring ×ó±ÕÓÒ¿ª
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
        	 * 0001 C->ASÉêÇëÆ±¾İ 
        	 * 0010 AS->C·µ»ØÆ±¾İ
=======
        	 * 0001 C->ASç”³è¯·ç¥¨æ® 
        	 * 0010 AS->Cè¿”å›ç¥¨æ®
        	 * 0011 C->TGS
>>>>>>> refs/remotes/origin/master
        	 */
        	if(state.equals("0001"))
        	{
        		String IDc = data.substring(0, 4);
        		System.out.println("serveræ”¶åˆ°äº†"+IDc +"çš„è¯·æ±‚");
        		String IDtgs = data.substring(4, 8);
        		String TS1 = data.substring(8, 160);
<<<<<<< HEAD
        		//ÏÈÅĞ¶ÏÍ¬²½£¿²»Í¬²½·µ»ØÏàÓ¦³ö´íÂë
        		/*if(!Confirm_syn(TS1))
=======
        		TS1=huifu(TS1);
        		//å…ˆåˆ¤æ–­åŒæ­¥ï¼Ÿä¸åŒæ­¥è¿”å›ç›¸åº”å‡ºé”™ç 
        		if(!Confirm_syn(TS1))
>>>>>>> refs/remotes/origin/master
        		{
        			return "1001"+"000000"+"000000000000000000000000"+"0100";
<<<<<<< HEAD
        		}*/
        		//IDc database ÕÒµ½·µ»Ø¼ÓÃÜ ÕÒ²»µ½
        		if(true)
=======
                }
        		
        		//IDc database æ‰¾åˆ°è¿”å›åŠ å¯† æ‰¾ä¸åˆ°
        		
        		if(!mysql.search_UID(IDc))
        		{
        			System.out.println("æ‰¾ä¸åˆ°ç”¨æˆ·");
        			return "1001"+"000000"+"000000000000000000000000"+"0001";
        		}
        		else
>>>>>>> refs/remotes/origin/master
        		{
<<<<<<< HEAD
        			//ÓÃCµÄÃÜÔ¿¼ÓÃÜ·¢ËÍ¡£
        			//¼ÙÉèKc,56Î»µÄ0ºÍ1 ,DES
        	    	String Kc = "00000000000000000000000000000000000000000000000000000";  //¸ù¾İÊı¾İ¿â¿ÚÁîÉú³ÉµÄ
        	    	String Ktgs = "00000000000000000000000000000000000000000000000000000"; 
        	    	//»á»°ÃÜÔ¿ÁÙÊ±Éú³É
=======
        			//ç”¨Cçš„å¯†é’¥åŠ å¯†å‘é€ã€‚
        			//å‡è®¾Kc,56ä½çš„0å’Œ1 ,DES
        			
        			String pwd_c = mysql.search_pw(IDc);
        			System.out.println("å¯†ç ä¸º"+pwd_c);
        	    	//String Kc = "00000000000000000000000000000000000000000000000000000";  //æ ¹æ®æ•°æ®åº“å£ä»¤ç”Ÿæˆçš„
        			String Ktgs = zhuan(mysql.search_pw(IDtgs));
        			//String Ktgs = "00000000000000000000000000000000000000000000000000000"; 
        		
        			String Kc = zhuan(mysql.search_pw(IDc));
        			System.out.println("ç”Ÿæˆçš„kcä¸º"+Kc);
        			//ä¼šè¯å¯†é’¥ä¸´æ—¶ç”Ÿæˆ			
>>>>>>> refs/remotes/origin/master
        	    	String Kc_tgs = create_sessionkey();
        	    	System.out.println("ÁÙÊ±Éú³ÉµÄ»á»°ÃÜÔ¿Îª£º"+ Kc_tgs);
        	    	String TS2 = Create_TS();
        	    	String TSzhuan = zhuan(TS2);
        	    	String lifetime2 = "99999";
        	    	String lifetimezhuan = zhuan(lifetime2);
        	    	String[] ticket = new String[1];
        	    	CreateTicket(ticket,Ktgs,Kc_tgs,TSzhuan,lifetimezhuan,IDc,IDtgs);
        	    	System.out.println("ASç”Ÿæˆçš„TGSç¥¨æ®"+ticket[0]);
        	    	String[] as_dao_c = new String[1];
        	    	as_dao_c[0] = Kc_tgs+IDtgs + TSzhuan + lifetimezhuan+ ticket[0];
        	    	int buas_dao_c = patch(as_dao_c);
<<<<<<< HEAD
        	    	//´«Êä asdaoc £¬¼ÇµÃ¼ÓÃÜ¡££¡£¡£¡
        	    	return "0010"+"00000100"+"000000000000000000000000"+as_dao_c[0];
        			
        		}
        		//Á½ÖÖÇé¿ö
        		else 
        		{
        			return "1001"+"000000"+"000000000000000000000000"+"0010";
        		}
=======
        	    	String send = "0010"+"111100"+"000000000000000000000000" + as_dao_c[0];
        	    	System.out.println("åŠ å¯†å‰data"+ as_dao_c[0]);
        	    	String jmh = DES_wzj.des_jia_da(as_dao_c[0], Kc) ;
        	    	String sendz = "0010"+"111100"+"000000000000000000000000" + jmh ;
        	    	System.out.println("åŠ å¯†ådata"+jmh);
        	    	
        	    	new uias(send,sendz,Kc_tgs,Kc);
        	    	//ä¼ è¾“ asdaoc ï¼Œè®°å¾—åŠ å¯†ã€‚ï¼ï¼ï¼
        	    	return sendz;
        			}
>>>>>>> refs/remotes/origin/master
        		
        		}

        	
        	//æ³¨å†Œï¼ï¼ï¼ï¼ï¼
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
                    //½ÓÊÕµ½µÄ
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

