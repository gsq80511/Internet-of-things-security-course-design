package as;

public class public_function {
	public void split(String s){}
	boolean Confirm_time(String TS,String lifetime)
	{
		//Switch 状态码
		//Case 不同拆分，赋值给本地变量使用。
		//如果字段里有时间字段，则要比较执行confirm函数，如TGS和V都收到这种字段，要看票据是否过期，所以他们要比较，如果超期，则发送给C你的票据过期，关闭线程。

		return true;
	}
	boolean Confirm_syn(String TS)
	{
	 //TS为票据时间，get现在的时间，作差与300s比较。
	//大于不同步，返回false。
		return true;
	}
	public String patch(String a)
	{
	     //根据状态码规则，在识别出message部分之后 对其进行补足64位
		return null;
	}
	public String create_TS()
	{
	//获取当前时间，格式化后，返回String。例如：2019-04-18 19:00:00
		return null;
	} 
	public String  Create_Authenticator(String key)
	{
	     //调用函数生成A ，加密返回string。
		return null;
	}
	public String create_sessionkey()
	{
	//随机生成56位密钥。
		return null;
	}
	public String DES_Encryption(String a)
	{
	    //将拼接好的一串string进行加密
		return null;
	}
	public String RSA_Encryption(String a)
	{
	   // 将拼接好的一串string进行加密
		return null;
	}
	public String DES_Decryption(String a)
	{
	    //将拼接好的一串string进行加密
		return null;
	}
	public String RSA_Decryption(String a)
	{
		return null;
	    //将拼接好的一串string进行加密
	}
	public boolean send(String a)
	{
		return true;
	    //将拼接好的一串string进行加密
	}
	public String get()
	{
		return null;
	    //将拼接好的一串string进行加密
	}

	public String CreateTicket(String key)
	{
		return null;
	//例如：Tickettgs = EKtgs [Kc,tgs || IDC || ADC || IDtgs || TS2 || Lifetime2] 
	//Key = ktgs
	//调用函数生成ticket ，加密返回string。
	}
	public String search(String IDc)
	{
		return null;
	//通过ID查找口令，null说明没有，有口令，转成56位密钥。
	}
	public boolean insert(String ID,String password)
	{
		return true;
	 //插入用户名密码，成功true。
	}
}
