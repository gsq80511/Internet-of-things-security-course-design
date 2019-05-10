package UI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import DES.DES_wzj;
import RSA.MR;
import RSA.RSAAA;
import RSA.hash_sig;

public class uitwo {
	private static String adress_v = "127.0.0.1";
	//private String uid=null;
	//private static String ps = "85375788006762656829011900815655768666459998557617662491715768020440605079393";
	private static BigInteger p ;
	//private static String qs = "64930615482244056100886787500614260285889422548937402689028437646786185670547";
    private static BigInteger q ;
   // private static String ds = "2408920253158763506212391437169762653784866007610945809508264587228971478530336684266432227467959116251284617520381007505323822347627794843171869290871617";
    private static BigInteger d ;
    private static BigInteger n ;
    private static BigInteger fn ;
    private static BigInteger e = BigInteger.valueOf(65537);
    private static String pkvs = "7837137785979871791891700201528161241909857988910850015158896906189939415077018837130932892270780996474539785044629220417652042177243731652078648461027777";
    private static BigInteger pkv = new BigInteger(pkvs);
    static String Content = "";
    //private Charset charset = Charset.forName("utf-8");
    private String name = "";
    private String Kc_v = "";
    private static String USER_EXIST = "system message: user exist, please change a name";
    private static String USER_CONTENT_SPILIT = "#@#";
    
	public JFrame jFrame = new JFrame("聊天");
	private Container c = jFrame.getContentPane();

	private JLabel a3 = new JLabel("key-c,v");
	private JTextField k3 = new JTextField();

	

	private JTextArea liaotiankuang = new JTextArea();
	private JScrollPane jsp=new JScrollPane(liaotiankuang);
	
	private JTextField shuru = new JTextField();

	
	//private JButton button2 = new JButton("退出");
	private JButton button3 = new JButton("发送");
	
    private final ByteBuffer sendBuffer=ByteBuffer.allocate(1024*8);
    private final ByteBuffer receiveBuffer=ByteBuffer.allocate(1024*8);
    private Selector selector;
    private SocketChannel socketChannel;
		/**
		 * 利用该内部类来监听所有事件源产生的事件 便于处理事件代码模块化
		 */
	private class SimpleListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			System.out.println("触发事件");
			String buttonName = e.getActionCommand();
			if (buttonName.equals("发送")) {
				// hash
				// 签名
				// 加密
				// 发送
				//BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(System.in));
				String info = shuru.getText()+"\n"; //消息
        		String IDc = name; //4 位用户名
        		String infozhuan = zhuan(info);
        		String TS = Create_TS(); //时间
        		String TSzhuan = zhuan(TS);
        		String final_send = "";
        		//对info进行 id+时间+info 然后加密传输。
        		String data = IDc+TSzhuan+infozhuan; //0和1
        		
        		String hs = "";
				try {
					hs = hash_sig.Sign(data,d,n);
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} 
        		
        		String[] send = new String[1];
        		send[0] = data + hs;
        		
        		int remnant = patch(send); //冗余为看补了多少0
        		String remsix = intsz_to_string(binaryToDecimalsix(remnant));
        		System.out.println("fasongqiande  remsix"+remsix);
        		int blank = data.length();
        		String blanktf = intsz_to_string(binaryToDecimaltf(blank));
        		System.out.println("info"+info);
        		 if("".equals(info)) 
        		 {
        			 return;
        		 }
        		final_send =  name+USER_CONTENT_SPILIT+
        				"0000"+remsix+blanktf+DES_wzj.des_jia_da(send[0], Kc_v);
		            sendMsg(final_send);
				shuru.setText(null);
				// 新窗口
			} else if (buttonName.equals("退出")) {
				// 
				
			} 
		}
	}

	// 需要参数的传入。显示
	public uitwo(String UID,String kcv) throws IOException {
		this.name=UID;
		this.Kc_v=kcv;
	
		p = create_p();
		q = create_q();
		// 设置窗体的位置及大小
		jFrame.setBounds(600, 200, 600, 700);
		// 设置一层相当于桌布的东西
		
		c.setLayout(new BorderLayout());// 布局管理器
		// 设置按下右上角X号后关闭
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// 初始化--往窗体里放其他控件
		init();
		this.socketChannel = SocketChannel.open();
        this.socketChannel.connect(new InetSocketAddress(adress_v,8080));
        this.socketChannel.configureBlocking(false);
        System.out.println("连接建立成功");
        this.selector=Selector.open();
        this.socketChannel.register(selector,SelectionKey.OP_READ);
        n = p.multiply(q);
        fn = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        d = RSAAA.cal(e,fn);
        String msg=name+","+n.toString();
        sendMsg(msg); //改成hello + name+Kc_v+Pkc4+64+n
        //输出发送？？？
		// 设置窗体可见
		jFrame.setVisible(true);
		Thread jilu=new Thread(() -> {
			try {
				receiveMsg();
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		});
        jilu.start();
	}
    private void sendMsg(String msg) {
    	try {
			//while ((msg = bufferedReader.readLine()) != null){
			    synchronized(sendBuffer){
			        sendBuffer.put((msg).getBytes());
			        sendBuffer.flip();
			        while(sendBuffer.hasRemaining()){
			            socketChannel.write(sendBuffer);
			        }
			        sendBuffer.compact();
			       // liaotiankuang.setText(liaotiankuang.getText()+"uid:"+uid+"\ntime:\n"+msg);
			    }
			//}
		} catch (IOException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
			}
    }
	public void init() {
		/* 标题部分--North */
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new FlowLayout());
		titlePanel.add(new JLabel(name+"的聊天室"));
		c.add(titlePanel, "North");

		/* 输入部分--Center */
		JPanel fieldPanel = new JPanel();
		fieldPanel.setLayout(null);
		
		a3.setBounds(50, 100, 100, 30);
	
	
		fieldPanel.add(a3);
	
		k3.setBounds(110, 100, 400, 20);
		k3.setText(Kc_v);
		//liaotiankuang.setBounds(110, 220, 400, 300);
		jsp.setBounds(110, 220, 400, 300);
		shuru.setBounds(110, 540, 300, 20);
		//button1.setBounds(110, 580, 60, 20);
		button3.setBounds(450, 540, 60, 20);
		
		fieldPanel.add(k3);
		
		//fieldPanel.add(liaotiankuang);
		fieldPanel.add(jsp);
		fieldPanel.add(shuru);
		fieldPanel.add(button3);
		c.add(fieldPanel, "Center");

		/* 按钮部分--South */
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		
		//buttonPanel.add(button2);
		c.add(buttonPanel, "South");
		SimpleListener ourListener = new SimpleListener();
		
		//button2.addActionListener(ourListener);
		button3.addActionListener(ourListener);

	}
	//接收消息线程
	  /**
     * 接收服务端发送的内容
     * @param key
	 * @throws Exception 
     */
    private void receive(SelectionKey key)throws Exception{
        SocketChannel socketChannel=(SocketChannel)key.channel();
        socketChannel.read(receiveBuffer);
        receiveBuffer.flip();
        String content="";
        content=content+Charset.forName("utf-8").decode(receiveBuffer);
        //若系统发送通知名字已经存在，则表示用户已经登陆，退出
        if(USER_EXIST.equals(content)) {
            //name = "";
        	//显示已登陆
        	
        	 JOptionPane.showMessageDialog(null, "用户已登陆", "", JOptionPane.ERROR_MESSAGE);
        }
        else {
        	//System.out.println("content::"+content);
            //content = "0000" + remsix+ blanktf +jia;
            System.out.println("用户处理广播数据");
            String remsix = content.substring(4, 10);
            int rem = cal_tf(remsix);   
            String blank = content.substring(10, 34);
            int blanktf = cal_tf(blank);
            String jia = content.substring(34);
            int len = jia.length();
            String jie = DES_wzj.des_jie_da(jia, Kc_v);
            String data = jie.substring(0, blanktf);
            String hs = jie.substring(blanktf,len-rem);
            String h = hash_sig.unSign(hs, pkv);
            boolean sig = hash_sig.hash_verify(data, hs,pkv);
            //先把data+hs取出
            //hs true？
            //然后set出来
            //if(sig) //认证成功发送到聊天框
            if(true)
            {
            	//分解data
            	String uid=data.substring(0, 4);
            	String time = data.substring(4, 156);
            	time=huifu(time);
            	String receiveData = data.substring(156);
            	receiveData=huifu(receiveData);
            	//Content = Content + data;
            	//liaotiankuang.setText(Content);
            	 liaotiankuang.setText(liaotiankuang.getText()+"uid:"+uid+"\ntime:"+time+"\n"+receiveData);
            }
        }
        
       
       // System.out.println("receive server message:"+receiveData);
       
        receiveBuffer.clear();
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    /**
     * 发送接收的内容至聊天框
     * @throws Exception 
     */
    private void receiveMsg() throws Exception{

        while (selector.select() > 0 ){

            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()){
                SelectionKey key = it.next();
                it.remove();

                if (key.isReadable()) {
                    System.out.println("isReadable");
                    receive(key);
                }

            }

        }
    }

	// 测试
	public static void main(String[] args) throws IOException {
		String Kcv = create_sessionkey();
		uitwo ui2 = new uitwo("0011",Kcv);
		Thread jilu=new Thread(() -> {
			try {
				ui2.receiveMsg();
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		});
        jilu.start();
        
	}
	/*
	 *十进制转二进制 
	 */
	public static int[] binaryToDecimaltf(int n){
		 	int j[] = new int[24];
			    for(int i = 23,k=0;i >= 0; i--,k++)
			    {
			    	j[k]= n >>> i & 1;
			      
			    }
			    return j;
	 }
	/*
	 *十进制转二进制 
	 */
	public static int[] binaryToDecimalsix(int n){
		 	int j[] = new int[6];
			    for(int i = 5,k=0;i >= 0; i--,k++)
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
		 public int cal_tf(String tf)
		    {
		    	int remnant=0,j=0;
		    	char[] M =tf.toCharArray();
		        int[] m = new int[M.length];
		        for(int i=0;i<M.length;i++)
		        {
		            m[i] = M[i] -'0';

		        }
		        for(int i =M.length-1;i>=0;i--)
		        {
		        	
		        	 remnant = remnant + m[i]*(int)Math.pow(2, j);
		        	 j++;
		        	 
		        }
		       return remnant;
		        
		    }
    public static String Create_TS()
    {
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Calendar calendar = Calendar.getInstance();    
	    calendar.setTime(new Date());   
	   // System.out.println();
	    return df.format(calendar.getTime());
    }
    public BigInteger create_p()
   	{
   		BigInteger bigPrimep;
   		SecureRandom random=new SecureRandom();
   		random.setSeed(new Date().getTime());	
   		int bitlength = 1024;
   		
   			do
   			{
   			System.out.println("p生成中");
   			bigPrimep = BigInteger.probablePrime(bitlength,random);
   			while(!MR.isprime(bigPrimep))
   			{
   				bigPrimep = BigInteger.probablePrime(bitlength,random);
   			}
   			}while((bigPrimep.subtract(BigInteger.ONE).mod(BigInteger.valueOf(2))).compareTo(BigInteger.ZERO)==0
   					&&MR.isprime(bigPrimep.subtract(BigInteger.ONE).divide(BigInteger.valueOf(2))));
   		
   			System.out.println("p "+bigPrimep);
   			return bigPrimep;
   	}
       
   	public BigInteger create_q()
   	{
   		BigInteger bigPrimeq;
   		SecureRandom random=new SecureRandom();
   		random.setSeed(new Date().getTime());	
   		int bitlength = 1024;
   		do
   		{
   		System.out.println("q生成中");
   		bigPrimeq = BigInteger.probablePrime(bitlength,random);
   		while(!MR.isprime(bigPrimeq))
   		{
   			bigPrimeq = BigInteger.probablePrime(bitlength,random);
   		}

   		
   		}while((bigPrimeq.subtract(BigInteger.ONE).mod(BigInteger.valueOf(2))).compareTo(BigInteger.ZERO)==0&&MR.isprime(bigPrimeq.subtract(BigInteger.ONE).divide(BigInteger.valueOf(2))));
   	
   		System.out.println("q "+bigPrimeq);
   		return bigPrimeq;
   	}
   	public static String intsz_to_string(int[] t)
   	{
   		String h= "";
   		for(int i=0;i<t.length;i++)
   		{
   			h = h + String.valueOf(t[i]);
   		}
   		return h;
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
	/*
	 * public void listerner() { //确认按下去获取 okbtn.addActionListener(new
	 * ActionListener() {
	 * 
	 * @Override public void actionPerformed(ActionEvent e) { String uname =
	 * username.getName(); String pwd = String.valueOf(password.getPassword());
	 * System.out.println(uname+pwd); } }); //取消按下去清空
	 * cancelbtn.addActionListener(new ActionListener() {
	 * 
	 * @Override public void actionPerformed(ActionEvent e) { username.setText("");
	 * password.setText(""); } }); }
	 */
}
