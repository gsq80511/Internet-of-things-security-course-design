package RSA;

import java.math.BigInteger;
import java.security.MessageDigest;

public class hash_sig {
	 private static BigInteger n ;
	 private static BigInteger e = BigInteger.valueOf(65537);

//传入string，生成哈希码，返回值2进制string
    public static String hash(String a) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(a.getBytes());
        byte byteData[] = md.digest();
        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        // System.out.println("Digest(in hex format):: " + sb.toString());
        //convert the byte to hex format method 2
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            String hex = Integer.toHexString(0xff & byteData[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        //System.out.println("Digest(in hex format):: " + hexString.toString());
        String c=hexString.toString();
        c=c.toUpperCase();
        c=hextoBinary(c);
        char hexChar=c.charAt(0);
        if(hexChar=='0')
            c="1"+c;
        return c;
    }

    //验证哈希码与原文的哈希码是否一致，一致返回true，否则返回false
    public static boolean hash_verify(String M, String hash_num,BigInteger n) throws Exception {
        String b = hash(M);
        String c=unSign(hash_num,n);
        if (b.equals(c))
            return true;
        else
            return false;
    }

    //2进制转10进制Bigint
    public static BigInteger BinarytoDecimal(String Binary){
        BigInteger b=BigInteger.valueOf(0);
        for(int i=0;i<Binary.length();i++)
        {
            char BinaryChar=Binary.charAt(i);
            String c=String.valueOf(BinaryChar);
            BigInteger d=new BigInteger(c);
            BigInteger Binaryint=BigInteger.valueOf(2);
            for(int n=0;n<(Binary.length()-1-i);n++)
                d=d.multiply(Binaryint);
            b=b.add(d);
        }
        return b;

    }



    //将hex16进制转化为2进制
    public static String hextoBinary(String hex){

        String binaryValue="a";//最终数据
        for(int i=0;i<hex.length();i++)
        {
            char hexChar=hex.charAt(i);
            if(i==0)
                binaryValue=hexCharToBinary(hexChar);
            else
                binaryValue=binaryValue+hexCharToBinary(hexChar);
        }
        return binaryValue;

    }
    //hex2binary的附属函数，处理各个字符
    public static String hexCharToBinary(char hexChar) {
        String a=String.valueOf(hexChar);
        if(hexChar>='A'&&hexChar<='F') {
            int i=0;
            if(hexChar=='A')
                i=10;
            else if(hexChar=='B')
                i=11;
            else if(hexChar=='C')
                i=12;
            else if(hexChar=='D')
                i=13;
            else if(hexChar=='E')
                i=14;
            else if(hexChar=='F')
                i=15;
            a=Integer.toBinaryString(i);
            int g=a.length();
            if(g<4)
                for(int k=1;k<=4-g;k++) {
                    a = "0" + a;
                   // System.out.println(k);
                }
            //System.out.println(a);
            return a;
        }
        else {
            int i=Integer.parseInt(a);
            a=Integer.toBinaryString(i);
            int g=a.length();
            if(g<4)
                for(int k=1;k<=4-g;k++) {
                    a = "0" + a;
                    //System.out.println(k);
                }
            //System.out.println(new BigInteger(a));
            //System.out.println(a);
            return a;
        }
    }

    //签名函数传入任意string原文,私钥sk,与n,返回签名二进制String
    public static String Sign(String M,BigInteger sk,BigInteger n)throws Exception{
        String a=hash(M);

        BigInteger b=BinarytoDecimal(a);
        b=b.modPow(sk,n);
        return b.toString(2);
    }

    //传入参数 密文C，与公钥n，返回二进制String
    public static String unSign(String C,BigInteger pk){
        BigInteger c=BinarytoDecimal(C);
        BigInteger a=c.modPow(e,pk);
        return a.toString(2);
    }
}
