package RSA;

import java.math.BigInteger;
import java.security.MessageDigest;

public class rsa {
    //需要设置特定的全局变量
    private static String pp="61897745366657098766880288279996728386262786690180378671558149612776198071489";
    private static String qq="78582339589653486843209618542257983455522202322697738828527562742914986996523";
    private static String dd="4208050731683168969076418828153653974032949763564682443553337601093462849448508864466850332852723787634798073102917103227766511833757694293834551756034817";
    private static String ee="65537";
    private static BigInteger p=new BigInteger(pp);
    private static BigInteger q=new BigInteger(qq);
    private static BigInteger d=new BigInteger(dd);
    private static BigInteger e=new BigInteger(ee);
    private static BigInteger n=p.multiply(q);
    public static void main(String[] args) throws Exception {
        String a = "193162wqy";
        String b = hash(a);
        System.out.println(b);
        String hex=b;
        System.out.println("hex:"+hex);
        System.out.println("Decimal:"+hexToDecimal(hex));
        System.out.println("sign:"+Sign(hexToDecimal(hex)));
        if(hash_verify(a,hexToDecimal(hex)))
            System.out.println("this is honesty.");
        else
            System.out.println("this is faker");
    }

    //传入string，生成哈希码，返回值16进制string
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
        return c.toUpperCase();
    }

    //验证哈希码与原文的哈希码是否一致，一致返回true，否则返回false
    public static boolean hash_verify(String M, BigInteger hash_num) throws Exception {
        String b = hash(M);
        BigInteger c=hexToDecimal(b);
        if (c.equals(hash_num))
            return true;
        else
            return false;
    }

    //将hex16进制转化为十进制
    public static BigInteger hexToDecimal(String hex)
    {
        BigInteger decimalValue=BigInteger.valueOf(0);//最终数据
        BigInteger H=BigInteger.valueOf(16);//当前进制，16
        for(int i=hex.length()-1;i>=0;i--)
        {
            char hexChar=hex.charAt(i);
            BigInteger H_place=BigInteger.valueOf(1);//当前位的16的n次方
            BigInteger num=hexCharToDecimal(hexChar);//存储当前位的16-10进制的转化
            for(int j=0;j<hex.length()-1-i;j++){
                H_place=H_place.multiply(H);
            }
            //System.out.println("num1:"+num);
            num=num.multiply(H_place);
            //System.out.println("i:"+i);
            //System.out.println("num2:"+num);
            decimalValue=decimalValue.add(num);
        }
        return decimalValue;
    }
    //对十六进制中的ABCDEF进行处理
    public static BigInteger hexCharToDecimal(char hexChar)
    {
        String a;
        if(hexChar>='A'&&hexChar<='F') {
            a=String.valueOf(10 + hexChar - 'A');
            //System.out.println(new BigInteger(a));
            return new BigInteger(a);
        }
        else {
            a=String.valueOf(hexChar - '0');
            //System.out.println(new BigInteger(a));
            return new BigInteger(a);
        }
    }

    public static BigInteger Sign(BigInteger hex){
        return hex.modPow(d,n);
    }
    public static String RSA_Encryption(String hex,String pkey){
        BigInteger M=new BigInteger(hex);
        BigInteger K=new BigInteger(pkey);
        BigInteger E=BigInteger.valueOf(65537);
        BigInteger C=M.modPow(E,K);
        String C2= C.toString();
        if(C2.length()<12)
        {
            int n=12-C2.length();
            for(int i=0;i<n;i++)
            {
                C2="0"+C2;
            }
        }
        return C2;
    }

    public static String RSA_Decryption(String hex,String PK,String n){
        BigInteger M=new BigInteger(hex);
        BigInteger K=new BigInteger(PK);
        BigInteger N=new BigInteger(n);
        BigInteger C=M.modPow(K,N);
        String C2= C.toString();
        if(C2.length()<12)
        {
            int m=12-C2.length();
            for(int i=0;i<m;i++)
            {
                C2="0"+C2;
            }
        }
        return C2;
    }

}

