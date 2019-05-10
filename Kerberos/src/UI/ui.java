	package UI;

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
    //�ڲ��࣬�����¼���
	static String Kctgs;
	static String Kcv;
	static String IDc;
	static String TS5;
	private static String adress_as = "127.0.0.1";         //nihao
	private static String adress_tgs = "127.0.0.1";
	private static String adress_v = "127.0.0.1";
	JFrame jFrame = new JFrame("��¼");
    private Container c = jFrame.getContentPane();
    private JLabel a1 = new JLabel("�û���");
    private JTextField username = new JTextField();
    private JLabel a2 = new JLabel("��   ��");
    
    private JTextField password = new JTextField();
    
    private JTextField zhuce = new JTextField();
    
    private JButton button1 = new JButton("ȷ��");
    private JButton button2 = new JButton("ȡ��");
    private JButton button3 = new JButton("ע��");
	 private class SimpleListener implements ActionListener
   {
     /**
  ���� * ���ø��ڲ��������������¼�Դ�������¼�
   �� * ���ڴ����¼�����ģ�黯
   �� */
		 
		 public void actionPerformed(ActionEvent e) 
		 {
			 String buttonName = e.getActionCommand();
           	if (buttonName.equals("ȷ��"))
           	{
        	   String name = username.getText();
        	   String pwd = password.getText();
        	   if(name.equals("")||pwd.equals("")||name==""||pwd=="")
        	   {
        		   JOptionPane.showMessageDialog(null, "������������Ϣ", "", JOptionPane.ERROR_MESSAGE);
        		   return;
        	   }
        	   if(name.length()!=4)
        	   {
        		   JOptionPane.showMessageDialog(null, "�û���4λ", "", JOptionPane.ERROR_MESSAGE);
        		   return;
        	   }
        	   if(pwd.length()!=8)
        	   {
        		   JOptionPane.showMessageDialog(null, "����8λ", "", JOptionPane.ERROR_MESSAGE);
        		   return;
        	   }
        	   
        	   System.out.println(name+pwd);
        	   
        	 //ҳ����ȷ��Ҫ�ɵ�����
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
					   //?��װ���������ң���TGS��
					   //��V�����Լ����û���
						try {
							
							 new uitwo(IDc,Kcv);
							
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				 	   jFrame.dispose();
				 	   
				   }
           	
          
           	}
           	
           	else  if(buttonName.equals("ȡ��"))
           	{
           		username.setText("");
                password.setText("");
           	}
           	
           	else  if(buttonName.equals("ע��"))
           	{
				//System.out.println("1");
        		String name = username.getText();
         	    String pwd = password.getText();
         	    if(pwd.length()!=8)
         	    {
					//System.out.println(name+pwd);
         	    	JOptionPane.showMessageDialog(null, "ע������λ��Ҫ����8��", "", JOptionPane.ERROR_MESSAGE);
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
         	    //����Ѿ����˾�˵�Ѿ�ע����
         	   
           	}

           	
         }
     }
	 /*
		 * TS+1
		 */
		public static String TSadd(String TS, int sec) throws ParseException {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// ??????????
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
				AS_PublicKey="7168130713816454038100358793110942830483283087581237932856046709771692040864103191351343603638539598982200762071731689360973274442110986430996112040029763";
				data=rsa.RSA_Encryption(data,AS_PublicKey);
				String send=state+remnant+blank+data;
				output.writeUTF(send);
				String receive = null;
				receive = input.readUTF();
				String d = receive.substring(34);//����֮ǰ��data
				String receivejie = DES_wzj.des_jie_da(d, Kc);
				System.out.println("ui����֮���data"+receivejie);
				if(receive.substring(0, 4).equals("1001"))
				{
					String errortype = receivejie.substring(0, 4);
					if(errortype.equals("1111"))
					{
						//System.out.println(errortype);
						//д��log�ļ�
						//writelog.write_log(errortype,"src//AS//");
						JOptionPane.showMessageDialog(null, "���û��Ѿ�����", "", JOptionPane.ERROR_MESSAGE);
						return false;
					}
				}
				else
					{
						JOptionPane.showMessageDialog(null, "ע��ɹ�", "", JOptionPane.ERROR_MESSAGE);
						return true;
					}
				return true;
			}

    public static boolean denglu(String IDc1,String pwd) throws UnknownHostException, IOException, ParseException
    {
        System.out.println("--client--");
        //Socket(,)�ڲ����ֱ���Ϊ�����IP�Ͷ˿�
        Socket socket = new Socket(adress_as, 5299);
       // Socket socket = new Socket("127.0.0.1", 5299);
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());		
        String TS1 = Create_TS();
        String TS = zhuan(TS1);
        IDc = IDc1;
        String Kc = zhuan(pwd);
        //System.out.println("�û��������Կ"+Kc+"����Ϊ"+Kc.length());
        String IDtgs = "1101";
        String state = "0001";
        String remnant = "000000";
        String blank = "000000000000000000000000";
        String data = IDc+ IDtgs +TS;
        String send = state + remnant + blank +data ;
		output.writeUTF(send);		
        //����       
		//System.out.println("���˷��˷���AS��");
        String receive = null;
		receive = input.readUTF();
		//System.out.println("server����Ϣ��");
		
		//System.out.println("����֮ǰ"+receive);
		String d = receive.substring(34);
		//System.out.println("����֮ǰ��data");
		//System.out.println(d);
				
		
		String receivejie = DES_wzj.des_jie_da(d, Kc); 
		//System.out.println("ui����֮���data"+receivejie);
       // String Kctgs = receive.substring(34,90);   
        //���Ը��ݷ����ķ�����������������һ��
        if(receive.substring(0, 4).equals("1001"))
        {
     	  String errortype = receive.substring(34, 38);
     	  if(errortype.equals("0001"))
     	  {
     		  //д��log�ļ�
     		  JOptionPane.showMessageDialog(null, "�޴��û�", "", JOptionPane.ERROR_MESSAGE);
     		  return false;
     	  }
     	  else if(errortype.equals("0100"))
     	  {
     		 JOptionPane.showMessageDialog(null, "��ͬ��", "", JOptionPane.ERROR_MESSAGE);
    		  return false;
     	  }
        }
        else
        {    
        	
        	String Idtgs = huifu(receivejie.substring(64,96));
        //	System.out.println("Idtgs ������Կ���ܺ��Idtgs��"+Idtgs);
        //	System.out.println("�Ƿ����"+Idtgs.equals(IDtgs));
        	if(Idtgs.equals(IDtgs))
        	{
        		Kctgs = receivejie.substring(0,64);  ///!!!!!!!!
        		String ticket = receivejie.substring(288,672);
        //		System.out.println("C�յ�AS����Ticket");
        //		System.out.println(ticket);
        		String fa = split(receive.substring(0, 34)+Kctgs+Idtgs+receivejie.substring(96));
        //		System.out.println("�Ự��Կ"+Kctgs);
        		//System.out.println("Kc,tgs || IDtgs || TS2 || Lifetime2 || Tickettgs"+receive);
     	//��TGS ���� fa
        		
				if(send_TGS(fa))
				{
					return true;
				}
				
				return false;
        	}
        	else
        	{
        		//���ܺ��TGS���ȣ��������
        		 JOptionPane.showMessageDialog(null, "���벻��ȷ", "", JOptionPane.ERROR_MESSAGE);
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
	     		 JOptionPane.showMessageDialog(null, "TGS��Ʊ����", "", JOptionPane.ERROR_MESSAGE);
	     		  return false;
	     	  }
		}
		
		else 
		{
			String cjietgs = DES_wzj.des_jie_da(receive.substring(34),Kctgs);
			System.out.println("C�յ�TGS�������Ticketv");
			System.out.println(cjietgs.substring(220,604));
			
			String c_send_v = split(receive.substring(0,34)+cjietgs);
			if(send_V(c_send_v ))
			//����V
			{
				return true;
			}		
		}
		return false;
    }
    /*
     * ���͸�V
     */
    public static boolean send_V(String fa) throws UnknownHostException, IOException, ParseException 
    {
    	//C->V
    	//Socket socketv = new Socket("192.168.43.164", 5400);
    	Socket socketv = new Socket(adress_v, 5400);
		DataInputStream  inputv = new DataInputStream(socketv.getInputStream());
		DataOutputStream outputv = new DataOutputStream(socketv.getOutputStream());
		
		outputv.writeUTF(fa);
		//����V ��������Ϣ���е���EKc,v[TS5+1]
		 String receive1 = inputv.readUTF();
		 if(receive1.substring(0, 4).equals("1001"))
	        {
	     	  String errortype = receive1.substring(34, 38);
	     	  if(errortype.equals("0110"))
	     	  {
	     		  //д��log�ļ�
	     		  JOptionPane.showMessageDialog(null, "V��Ʊ�ݳ���", "", JOptionPane.ERROR_MESSAGE);
	     		  return false;
	     	  }
	     	 
	        }
		 else
		 {
			 System.out.println("TS6����ǰ"+receive1 +"length"+receive1 .length());
			 
			 String datajie = DES_wzj.des_jie_da(receive1.substring(34), Kcv);
			 System.out.println("TS6����ǰ"+datajie +"length"+datajie.length());
			 //data�ǽ��ܺ��TS5+1
			 String renzheng = datajie.substring(0,152);
			 String huifu = huifu(renzheng); //TS5+1;
			 System.out.println("C�ָ�������TS5+1����"+huifu);
			 String TS = TSadd(TS5,1);
			 if(huifu.equals(TS))
			 {
				 JOptionPane.showMessageDialog(null, "������֤�ɹ�", "", JOptionPane.ERROR_MESSAGE);
				 return true;
			 }
		 }
	
		return false;
    }

	
    public ui() {
        //���ô����λ�ü���С
        jFrame.setBounds(600, 200, 300, 250);
        //����һ���൱�������Ķ���
        c.setLayout(new BorderLayout());//���ֹ�����
        //���ð������Ͻ�X�ź�ر�
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //��ʼ��--��������������ؼ�
        init();
        //���ô���ɼ�
        jFrame.setVisible(true);
    }
    public void init() {
        /*���ⲿ��--North*/
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout());
        titlePanel.add(new JLabel("XXX��¼ϵͳ"));
        c.add(titlePanel, "North");
        
        /*���벿��--Center*/
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
        
        /*��ť����--South*/
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
    //����
    
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
	 * ������8λ8λ�ָ���
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
			return "��8��������";
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
	 * ��string�ַ������ascii������Ʊ����string�ַ���
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
			M1[i] = M[i]; //ÿһλ����int�ˣ����ڿ�ʼת��������
			tmp =  binaryToDecimal(M1[i]); //ÿһλ��ת���˶�����
			    for(int j =0;j<8;j++)
			    {
			    	s = s + String.valueOf(tmp[j]); //����string��
			    	
			    }
		}
 		return s;	
	}
	
   	/*
	 * ����һ���� ������һ��Ҫ���͵İ��ͳ�����Ϣ
	 */
	  public static String split(String get)
		{
			//��֪״̬��ǰ4λ������6λ������24��data��ʽ��֪��
	    	String state,remnant,blank,data;
	    	//substring ����ҿ�
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
	    	 * 0001 C->AS����Ʊ�� 
	    	 * 0010 AS->C����Ʊ��
	    	 * 0011 C->TGS
	    	 * 0100 TGS->C  
	    	 * 
	    	 */
	    	if(state.equals("0001"))
	    	{
	    		
	    	}
	    	else if(state.equals("0010"))
	    	{
	    		//��DES����
	    		String IDv = "1001";
	    		
	    		String Kc_tgs = data.substring(0, 64);
	    		String IDtgs = data.substring(64, 68);
	    		String TS2 = data.substring(68, 220);
	    		String lifetime2  = data.substring(220, 260);
	    		String Ticket = data.substring(260, 644);
	    		System.out.println("CҪ����TGS��ticket");
	    		System.out.println(Ticket);
	    		//���ж�ͬ��
	    		String auth = Create_Authenticator(IDc,Kc_tgs);
	    		//��������Ϊ
	    		System.out.println("CҪ����TGS��auth");
	    		System.out.println(auth);
	    		String data1 = IDv + Ticket + auth;
	    		return "0011"+"000000"+"000000000000000000000000"+data1;
	    	}
	    	
	    	else if(state.equals("0100"))
	    	{
	    		
	    		Kcv = data.substring(0, 64);
	    		 System.out.println("authen to v�����õ�Կ��"+ Kcv+"length"+Kcv.length());
	    		String IDv = data.substring(64,68);
	    		String TS = data.substring(68, 68+152);
	    		String Ticketv = data.substring(68+152);
	    		System.out.println("CҪ����V��ticket");
	    		System.out.println(Ticketv);
	    		System.out.println("CҪ����V��ticket����");
	    		System.out.println(Ticketv.length());
	    		String auth = Create_Authenticator(IDc,Kcv);
	    		System.out.println("CҪ����V��auth");
	    		System.out.println(auth);
	    		System.out.println("CҪ����V��auth����");
	    		System.out.println(auth.length());
	    		
	    		String datav = Ticketv + auth;
	    		System.out.println("TS5"+TS5);
	    		
	    		return "0101"+"000000"+"000000000000000000000000"+datav;
	    	
	    	}
	    	return get;
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
		System.out.println("����ǰauthenticator"+"length"+authen[0].length());
		System.out.println(authen[0]);
		int a = patch(authen); 
		String authjia = DES_wzj.des_jia_da(authen[0], key);
		System.out.println("���ܺ�authenticator"+"length"+authjia.length());
		System.out.println(authjia);
		// ��Kc��tgs���ܶԳƷ��� 
		return authjia;
	}
  

}