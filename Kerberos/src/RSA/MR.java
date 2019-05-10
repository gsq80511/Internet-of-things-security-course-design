package RSA;

import java.math.BigInteger;

public class MR {
	public static void main(String[] args) { 
		System.out.println("如果是素数：true ，如果不是素数：false");
		System.out.println(isprime(BigInteger.valueOf(51472)));
	}
	
	public static boolean isprime(BigInteger n)
	{
		int times = 5;
		BigInteger []prime = {BigInteger.valueOf(2),BigInteger.valueOf(3),BigInteger.valueOf(5),BigInteger.valueOf(7),BigInteger.valueOf(11)};
		for(int i=0;i<times;i++){
	        if(n.compareTo(prime[i])==0)
	        {     	
	        		return true;
	        }
	       // System.out.println("n yu prime"+n +" "+ prime[i]);
	        
	        if(!Miller_Rabbin1(n,prime[i]))
	        	return false;//未通过探测 返回假
	    }
	    return true;//所有探测结束 返回真
	
	}
	//////
	
	public static boolean Miller_Rabbin1(BigInteger n, BigInteger a)
	{
		BigInteger s = n.subtract(BigInteger.ONE);	
		int  r = 0;
		if(n.mod(a).compareTo(BigInteger.ZERO)==0)
		     return false;
		while((s.and(BigInteger.ONE)).compareTo(BigInteger.ZERO)==0)
		{
			s = s.shiftRight(1);// s 左移动一位
			r++;
		}
		//System.out.println("r:"+r);
		s = n.subtract(BigInteger.ONE);	
		BigInteger k = qpow(a,s,n);
		if(k.compareTo(BigInteger.ONE)==0)
			return true;
	    for(int i=0;i<r;i++,k=k.multiply(k).mod(n)){
	    	System.out.println("i:"+i);
	        if(k.compareTo(n.subtract(BigInteger.ONE))==0)
	        	return true;
	    }
	    return false;
	}
	
	public static boolean Miller_Rabbin(BigInteger n, BigInteger a)
	{
		BigInteger s = n.subtract(BigInteger.ONE);	
		BigInteger d,k ;
		int  r = 0;
		if(n.mod(a).compareTo(BigInteger.ZERO)==0)
		     return false;
		
		while((s.and(BigInteger.ONE)).compareTo(BigInteger.ZERO)==0)
		{
			s = s.shiftRight(1);// s 左移动一位
			r++;
		}
		System.out.println("r:"+r);
		s = n.subtract(BigInteger.ONE);	
		d = s.divide(BigInteger.valueOf(2).pow(r));
		k = a.pow(d.intValue());
		for(int j =0;j<r;k = k.multiply(k),j++)
			{
			System.out.println("j:"+j);
				BigInteger result ;
				result = k.mod(n);
				if(result.compareTo(BigInteger.ONE)==0) return true;
				if(result.compareTo(n.subtract(BigInteger.ONE))==0) return true;
			}
		return false;
	}
	
	public static BigInteger qpow(BigInteger a,BigInteger b,BigInteger n)//快速幂
	  {
		
		// System.out.println("qpow begins");
		 BigInteger ans=BigInteger.ONE,buff = a;
		     while(b.compareTo(BigInteger.ZERO)!=0)
		      {
		    	 
		         if(b.and(BigInteger.ONE).compareTo(BigInteger.ZERO)!=0)
		         
		         ans=(ans.multiply(buff)).mod(n);
		         buff=buff.multiply(buff).mod(n);
		         
		         /*res = mod_mul(res,a,n);
		         a = mod_mul(a,a,n);
		         */
		        // System.out.println("b??"+b);
		         b = b.shiftRight(1);
		        
		     }
		     //System.out.println("qpow ends");
		     return ans;    
	 }
	
	/*public static BigInteger mod_mul(BigInteger a,BigInteger b,BigInteger n)
	{
		System.out.println("t?? 5");
		BigInteger res = BigInteger.ZERO;
	    while (b!=BigInteger.ZERO) {
	        if(b.and(BigInteger.ONE)!=BigInteger.ZERO)
	            res = (res.add(a)).mod( n);
	        a = (a.add(a)).mod(n);
	        b.shiftRight(1);
	        System.out.println("t?? ????");
	    }
	    System.out.println("t?? 6");
	    return res;
	}*/
}
