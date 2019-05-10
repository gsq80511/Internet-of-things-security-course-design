package V;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetSocketAddress;  
import java.net.ServerSocket;  
import java.net.Socket;  
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;  
import java.nio.channels.Selector;  
import java.nio.channels.ServerSocketChannel;  
import java.nio.channels.SocketChannel;  
import java.nio.charset.Charset;  
import java.util.HashMap;  
import java.util.Map;  
import java.util.Set;  
import DES.DES_wzj;
import RSA.hash_sig;
public class NIOSServer {  
	private static String ps = "75024187990667581340503949487555065911779464845090833495766820052240137739557";
	private static BigInteger p = new BigInteger(ps);
	private static String qs = "104461480968707719516839818238749079435408057381727391771233227567281594756461";
    private static BigInteger q = new BigInteger(qs);
    private static String ds = "3877372409060704075259723321701467874752967566907935988701223328597625701427459894896059162217263599422703515642741056725228000387855616606542605322884993";
    private static BigInteger d = new BigInteger(ds);;
    private static BigInteger n  = p.multiply(q);
    private static BigInteger e = BigInteger.valueOf(65537);
    
    private static String USER_EXIST = "system message: user exist, please change a name";
    //相当于自定义协议格式，与客户端协商好
    private static String USER_CONTENT_SPILIT = "#@#";
    
    private static boolean flag = false;
    
    //private static Map users_skey =  new HashMap(); //用来试验selectionkey与username 可关联  sk + name
    
    private int port = 8080;  
    //解码buffer  
    private Charset cs = Charset.forName("utf-8");  
    /*接受数据缓冲区*/  
    private static ByteBuffer sBuffer = ByteBuffer.allocate(1024*8);  
    /*发送数据缓冲区*/  
    private static ByteBuffer rBuffer = ByteBuffer.allocate(1024*8);  
    /*映射客户端channel */  
    private Map<SocketChannel, String> clientsMap = new HashMap<SocketChannel, String>();  
    private static Selector selector;  
      
    public NIOSServer(int port){  
        this.port = port;  
        try {  
            init();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    private void init() throws IOException{  
        /* 
         *启动服务器端，配置为非阻塞，绑定端口，注册accept事件 
         *ACCEPT事件：当服务端收到客户端连接请求时，触发该事件 
         */  
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();  
        serverSocketChannel.configureBlocking(false);  
        ServerSocket serverSocket = serverSocketChannel.socket();  
        serverSocket.bind(new InetSocketAddress(port));  
        selector = Selector.open();  
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);  
        System.out.println("server start on port:"+port);  
    }  
    /** 
     * 服务器端轮询监听，select方法会一直阻塞直到有相关事件发生或超时 
     */  
    public void listen(){  
        while (true) {  
            try {  
                selector.select();//返回值为本次触发的事件数  
                Set<SelectionKey> selectionKeys = selector.selectedKeys();  
                for(SelectionKey key : selectionKeys){  
                    handle(key);  
                }  
                selectionKeys.clear();//清除处理过的事件  
            } catch (Exception e) {  
                e.printStackTrace();  
                break;  
            }  
              
        }  
    }  
  
    /** 
     * 处理不同的事件 
     * @throws Exception 
     */  
    private void handle(SelectionKey selectionKey) throws Exception {  
        ServerSocketChannel server = null;  
        SocketChannel client = null;  
        String receiveText=null;  
        int count=0;  
        if (selectionKey.isAcceptable()) {  
            /* 
             * 客户端请求连接事件 
             * serversocket为该客户端建立socket连接，将此socket注册READ事件，监听客户端输入 
             * READ事件：当客户端发来数据，并已被服务器控制线程正确读取时，触发该事件 
             */  
            server = (ServerSocketChannel) selectionKey.channel();  
            client = server.accept();  
            client.configureBlocking(false);  
            client.register(selector, SelectionKey.OP_READ);  
        } else if (selectionKey.isReadable()) {  
            /* selectionKey.isValid() &&
             * READ事件，收到客户端发送数据，读取数据后继续注册监听客户端 
             */  
            client = (SocketChannel) selectionKey.channel();  
            rBuffer.clear();  
            try{  
            	 count = client.read(rBuffer);  
            }catch(IOException e){  
//            	for(Map.Entry<SocketChannel, String> entry : clientsMap.entrySet()){  
//                    SocketChannel temp = entry.getValue();
//                    if(client.equals(temp)){  
//                    	clientsMap.remove(entry.getKey() , value);
//                   }  
            	clientsMap.remove(client, clientsMap.get(client));
            	selectionKey.cancel();  
            	 client.socket().close();  
            	 client.close();  
                return;  
            }  
            if (count > 0) {  
                rBuffer.flip();  
                receiveText = String.valueOf(cs.decode(rBuffer).array());  
                System.out.println(client.toString()+":"+receiveText);  
                dispatch(client, receiveText);  
                client = (SocketChannel) selectionKey.channel();  
                client.register(selector, SelectionKey.OP_READ);  
            }  
        }   
    }  
      
    /** 
     * 把当前客户端信息 推送到其他客户端 
     * @throws Exception 
     */  
    private void dispatch(SocketChannel client,String info) throws Exception{  
        Socket s = client.socket();  
       // String name = "["+s.getInetAddress().toString().substring(1)+":"+Integer.toHexString(client.hashCode())+"]"; 
  
        if(!clientsMap.isEmpty()&&!info.contains(",")){  //转发
        	System.out.println("转发　");
            String uid=clientsMap.get(client);
//            for(Map.Entry<String, SocketChannel> entry : clientsMap.entrySet()){  
//                SocketChannel temp = entry.getValue();
//                if(client.equals(temp)){  
//                	uid=entry.getKey();
//               }  
//            }
            
              System.out.println("uid get　:"+uid);
        	  String message = info.substring(4+USER_CONTENT_SPILIT.length());
              //message = "0000"+remsix+blanktf+DES_wzj.des_jia_da(send[0], Kc_v);
              //String uid = message.substring(0, 4);
              String remsix = message.substring(4, 10);
              System.out.println("冗余位6"+remsix);
              String blanktf = message.substring(10, 34);
              System.out.println("空白位24"+blanktf);
              int remnant = 0;
              char[] M =remsix.toCharArray();
              int[] m = new int[M.length];
              for(int i=0;i<M.length;i++)
              {
                  m[i] = M[i] -'0';

              }
              remnant = m[0]*32 + m[1]*16 + m[2]*8 + m[3]*4 + m[4]*2 + m[5]*1;
              System.out.println("冗余位10jinzhi"+remnant);
              int blank = cal_tf(blanktf); //data长度
              System.out.println("data 长度"+blank);
              ///////
              String jia = message.substring(34);
              //jia = DES_wzj.des_jia_da(send[0], Kc_v)
              String jie = DES_wzj.des_jie_da(jia, V.users_Kcv.get(uid));
              int len = jie.length();
              //jie = send[0] = send[0] = data + hs;
              String data =jie.substring(0,blank);
              String hs = jie.substring(blank,len-remnant);
              BigInteger kc =new BigInteger(V.users_Kc.get(uid));//用户公钥
              String h = hash_sig.unSign(hs,kc);
              boolean sighash = false;
              sighash = hash_sig.hash_verify(data,h,kc);
              String[] send = new String[1];
              String remsixv = null;
             //if(sighash)
              if(true)
              {
              	//进行V的私钥加密，h
              	String hsv =hash_sig.Sign(h, d, n); //签名后加在尾巴
              	send[0] = data + hsv;
              	int bu = patch(send);
              	remsixv = binaryToDecimalsix(bu);
              	//send = 没有对称密钥的，补好了的
              	}
             else {
            	 System.out.println("签名认证失败");
             }
            for(Map.Entry<SocketChannel, String> entry : clientsMap.entrySet()){  
                SocketChannel temp = entry.getKey();
                String other_name = entry.getValue();
                System.out.println("服务器帮忙转发给其它：" + other_name);
                //BigInteger other_keyc =new BigInteger(users_Kc.get(other_name).toString());//用户公钥非对称
                //System.out.println("非对称的公钥"+keyc);
                String other_Kcv = (String)V.users_Kcv.get(other_name);//会话密钥
               // System.out.println("64位对称密钥"+Kcv);
                String jiami = DES_wzj.des_jia_da(send[0],other_Kcv);
                jiami = "0000" + remsixv+blanktf+jiami;
                //if(!client.equals(temp)){  
                    //sBuffer.clear();  
                //sBuffer.put((name+":"+info).getBytes());  
                    //sBuffer.put((info).getBytes());  
                    //sBuffer.flip();  
                    //输出到通道  
                    //temp.write(sBuffer); 
                System.out.println("服务器send");
                temp.write(cs.encode(jiami)); 
                
                System.out.println(cs.encode(jiami)+"服务器send hou");
               // }  
            }  
        }  
        else {
        	//初始化hello + name+Kc_v+Pkc4+64+n
        	System.out.println("初始化！");
            String name = info.substring(0,4);
            String pkc = info.substring(5);
            if(clientsMap.containsValue(name)) {
            	System.out.println("拦截！");
            	client.write(cs.encode(USER_EXIST));
            	}
            else {
            	clientsMap.put(client, name);//name + sc
            	V.users_Kc.put(name, pkc);
            	System.out.println("clientsmap put :"+ name + client);
            }
        }
        
       
    }  
    public static void main(String[] args) throws IOException {  
        NIOSServer server = new NIOSServer(8080);  
        server.listen();  
         
    }  
    /*
  	 *十进制转二进制 
  	 */
  	public static String binaryToDecimalsix(int n){
  		 	int j[] = new int[6];
  			    for(int i = 5,k=0;i >= 0; i--,k++)
  			    {
  			    	j[k]= n >>> i & 1;
  			      
  			    }
  			String s = "";
  			for(int i:j) {
  				s+=String.valueOf(i);
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
      //TODO 要是能检测下线，就不用这么统计了
      public static int OnlineNum(Selector selector) {
          int res = 0;
          for(SelectionKey key : selector.keys())
          {
              Channel targetchannel = key.channel();
              
              if(targetchannel instanceof SocketChannel)
              {
                  res++;
              }
          }
          return res;
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
      
}  