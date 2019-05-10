package RSA;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
public class RSAAA {
	public static BigInteger create_p()
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
	public static BigInteger create_q()
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
	  private static BigInteger x; //存储临时的位置变量x，y 用于递归     
	  private static BigInteger y;
	  public static void main(String[] args) { 
		Scanner w1 = new Scanner(System.in);
		System.out.println("请输入你要加密的明文"); // 56位密钥
		String M;
		M = w1.nextLine();
		BigInteger Mingwen = new BigInteger(M);	
		System.out.println("M "+Mingwen);
		double begintime = System.nanoTime();
			SecureRandom random=new SecureRandom();
			random.setSeed(new Date().getTime());	
			BigInteger bigPrimep,bigPrimeq;
			BigInteger p,q;
			int bitlength = 1024;
			BigInteger n,fn,e;
			BigInteger inzi;
			
			do
			{
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
		   //
			//System.out.println("验证是否为强素数！分解p = 2* p1+1");
			
			//p = (bigPrimep.subtract(BigInteger.ONE)).divide(BigInteger.valueOf(2));
			
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
				
		         n = bigPrimep.multiply(bigPrimeq);//生成n
		         //生成fn
		         fn = bigPrimep.subtract(BigInteger.ONE).multiply(bigPrimeq.subtract(BigInteger.ONE));
		         //生成一个比k小的b,或者使用65537
		         e= BigInteger.valueOf(65537);
		         inzi = e.gcd(fn);
		        // System.out.println("gcd "+ inzi);
		         if(inzi.compareTo(BigInteger.ONE)!=0)
		         {
		        	 System.out.println("fn and e 不互质");
		         }
		         
			}while(inzi.compareTo(BigInteger.ONE)!=0);
		         
		         
		         //根据扩展欧几里得算法生成b 
		         BigInteger d = cal(e,fn);
		         System.out.println("d: "+ d);
		         BigInteger C;
		         C = Mingwen.modPow(e, n); 
		         System.out.println("C: "+ C);
		         Mingwen = C.modPow(d, n);
		         System.out.println("jiemihou M : "+ Mingwen);
		         double  endtime = System.nanoTime();
					double costTime = (endtime - begintime)/1000000000;
					System.out.println("time :"+ costTime+"s");
	}
	  //欧几里得扩展算法
	  public static BigInteger ex_gcd(BigInteger a,BigInteger b){
	        if(b.intValue()==0){
                x=new BigInteger("1");
	            y=new BigInteger("0");
	             return a;
	         }
	         BigInteger ans=ex_gcd(b,a.mod(b));
	         BigInteger temp=x;
	         x=y;
	         y=temp.subtract(a.divide(b).multiply(y));
	         return ans;    
	     }
	     //求反模 
	     public static BigInteger cal(BigInteger a,BigInteger k){
	         BigInteger gcd=ex_gcd(a,k);
	         if(BigInteger.ONE.mod(gcd).intValue()!=0){
	             return new BigInteger("-1");
	         }
	         //由于我们只求乘法逆元 所以这里使用BigInteger.One,实际中如果需要更灵活可以多传递一个参数,表示模的值来代替这里
	         x=x.multiply(BigInteger.ONE.divide(gcd));
	         k=k.abs();
	         BigInteger ans=x.mod(k);
	         if(ans.compareTo(BigInteger.ZERO)<0) ans=ans.add(k);
	      return ans;
	         
	     }
}

