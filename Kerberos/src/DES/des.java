package DES;

import java.io.IOException;
import java.util.Scanner;

public class des {
	public static void main(String[] args) 
	{
		String jia = "";
		for(int i=0;i<64;i++)
		{
			char M = '0';
			jia = jia +M;
		}
		jia = "0000000000000000000000000000000000000000000000000000000000000000";
		String key = "";
		for(int i=0;i<64;i++)
		{
			char M = '0';
			key =  key + M;
		}
		key = "0000000000000000000000000000000000000000000000000000000000000000";
		System.out.println("key "+key);
		String jiami = desda(jia,key);
		System.out.println("jiamihou"+jiami);
	}
	
	public static String des(String a,String key)
	{
		//64位
		char M[] = new char[64];
		M  = a.toCharArray();
		char Key[] = new char[56];
		Key = key.toCharArray(); //key
		double begintime = System.nanoTime();
		char ip_converted[] = new char[64];
		//先IP置换
		//System.out.print("ip处理：");
		for(int i =0;i<64;i++)
		{	
			ip_converted[i] = ip(M)[i];
		}
		/*for(int i=0;i<64;i++)
		{
			System.out.print(ip_converted[i]);  //输出iP打乱后的
		}
		*/
		//System.out.println();
		char left[] = new char[32];
		char right[] = new char[32];
		char temp[] = new char[32]; 
		for(int i =0;i<32;i++)
		{
			left[i] = ip_converted[i];
		}
		for(int i =32;i<64;i++)
		{
			right[i-32]=ip_converted[i];
		}
		/////////////////////////////////////////左右分开了
		//右边 ep 扩展 32 ->48
		
		for(int k=0;k<16;k++)
		{
		//System.out.println("第"+(k+1));
		char[] ep_converted = new char[48]; 
		for(int i =0;i<48;i++)
		{	
			ep_converted[i] = ep(right)[i];
		}
		//System.out.println();
		//System.out.print("ep");
		/*for(int i =0;i<48;i++)
		{
			System.out.print(ep_converted[i]);
		}
		*/
		System.out.println();
		char[] subkey = new char[48]; // 子钥匙
		for(int i =0;i<48;i++)
		{	
			subkey[i] = key_gen(Key,1)[i];
		}
		//变长异或操作,结果存入yiuo_one,48
		int[] yihuo_one = new int[48];
		for(int i =0;i<48;i++)
		{	
			yihuo_one[i] = yihuocaozuo(ep_converted,subkey,48)[i];
		}
		//System.out.print("ep yu key yihuo");
		/*for(int i =0;i<48;i++)
		{
			System.out.print(yihuo_one[i]);
		}
		System.out.println();
		*/
		//将yihuo one放入s 盒子，出来32位的数组
		char[] hezi1 = new char[32];
		for(int i =0;i<32;i++)
		{
			hezi1[i] = she(yihuo_one)[i];
		}
		/*System.out.print("32daluanqian!!!!!!!");
		for(int i =0;i<8;i++)
		{
			System.out.print(decimaltox(hezi1,32)[i]);
		}
		System.out.println("32daluanqian!!!!!!!");
		
		*/
		
		//p32打乱
		char[] hezinew = new char[32];
		for(int i =0;i<32;i++)
		{
			hezinew[i] = p32(hezi1)[i];
		}
/*
		System.out.print("p32x!!!!!!!");
		for(int i =0;i<32;i++)
		{
			System.out.print(hezinew[i]);
		}
		System.out.println("p32s!!!!!!!");
		*/
		//与左边异或，左边变成异或后的
		int[] yihuo_left = new int[32];
		for(int i =0;i<32;i++)
		{
			yihuo_left[i] = yihuocaozuo(hezinew,left,32)[i];
		}
		for(int i =0;i<32;i++)
		{
			left[i] = int_to_char(yihuo_left,32)[i];
		}
		//left yu right huhuan
		
		for(int i=0;i<32;i++)
		{
			temp[i] = right[i];	
		}
		for(int i=0;i<32;i++)
		{
			right[i] = left[i];
		}
		
		for(int i=0;i<32;i++)
		{
			left[i] = temp[i];
		}
		
		/*for(int i=0;i<8;i++)
		{
			System.out.print(decimaltox(left,32)[i]);
		}
		//System.out.print("right zai houn");
		for(int i=0;i<8;i++)
		{
			System.out.print(decimaltox(right,32)[i]);
		}
		System.out.println();
		*/
				
		//16lun
		}
		//////////////////////////////////////////////////////
		//合并
		for(int i=0;i<32;i++)
		{
			temp[i] = right[i];	
		}
		for(int i=0;i<32;i++)
		{
			right[i] = left[i];
		}
		
		for(int i=0;i<32;i++)
		{
			left[i] = temp[i];
		}
		char C[] = new char[64];
		for(int i=0;i<32;i++)
		{
			C[i] = left[i];
		}
		for(int i=32;i<64;i++)
		{
			C[i] = right[i-32];
		}
		char Cnew[] = new char[64];
		for(int i=0;i<64;i++)
		{
			Cnew[i] = ip_one(C)[i];
		}
		
		/////////////////////////////
		/*System.out.print("ipzhiqian密文为：");
		for(int i=0;i<64;i++)
		{
			System.out.print(C[i]);
		}
		System.out.println();
		System.out.print("ipzhizou密文为：");
		*/
		String miwen = "";
		for(int i=0;i<64;i++)
		{
			//System.out.print(Cnew[i]);
			miwen = miwen + Cnew[i];
		}
		
		System.out.println();
		char[] sixteen = new char[8];
		/*int [] de = new int[64];
		for(int i=0;i<64;i++)
		{
			de[i] = Integer.valueOf(C[i]);
		}
		/*System.out.println("de为：");
		for(int i=0;i<64;i++)
		{
			System.out.print(de[i]) ;
		}*/
		System.out.println();
		/*System.out.println("最后16密文为：");
		for(int i=0;i<16;i++)
		{
			System.out.print(decimaltox(Cnew,64)[i]);
		}
		System.out.println();
		
*/
		double  endtime = System.nanoTime();
		double costTime = (endtime - begintime)/1000000000;
		//System.out.println("time :"+ costTime+"s");
		return miwen;
	}
	/*
	 * des循环加密,64的整数倍
	 */
	public static String desda(String big,String key)
	{
		int length = big.length();
		char[] M = big.toCharArray();
		int cishu = length/64;
		System.out.println("cishu"+cishu);
		String jiamihou = "";
		for(int i =0;i<cishu;i++)
		{
			String jiamixiao = big.substring(64*i, 64*(i+1));
			jiamihou = jiamihou + des(jiamixiao,key);
		}
		return jiamihou;
		
	}
	

	
	
	//置换函数IP,输入明文char型 返回 char型 64位都是
		public static char[] ip(char[] M)
		{
			char x[] = new char[64];
			int ip[] = {
					58,50,42,34,26,18,10,2,
					60,52,44,36,28,20,12,4,
					62,54,46,38,30,22,14,6,
					64,56,48,40,32,24,16,8,
					57,49,41,33,25,17,9,1,
					59,51,43,35,27,19,11,3,
					61,53,45,37,29,21,13,5,
					63,55,47,39,31,23,15,7
			};
			
			for(int i=0;i<64;i++)
			{
				x[i]=M[ip[i]-1];
			}				
			return x;
		}
		
		public static char[] ep(char[] right)
		{
			//System.out.println("ep扩展");
			char ep_converted[] = new char[48];
			
			int expand[] = 
					{32,  1,  2,  3,  4,  5,
					4,  5,  6,  7,  8,  9,
					8,  9, 10, 11, 12, 13,
					12, 13, 14, 15, 16, 17,
					16, 17, 18, 19, 20, 21,
					20, 21, 22, 23, 24, 25,
					24, 25, 26, 27, 28, 29,
					28, 29, 30, 31, 32, 1};
			for(int i =0;i<48;i++)
			{
				ep_converted[i] = right[expand[i]-1];
			};
			//System.out.println(ep_converted);
			return ep_converted;	
		}
		
		public static char[] key_gen(char[] Key,int turn)
		{
			//使用大密钥生成一个个小的密钥
			char key[] = new char[56];
			char left_ls[] = new char[28];
			char right_ls[] = new char[28];
			
			char key_converted[] = new char[48];
			int yi=0; //移动次数
			char temp;
			
			//进行换位子 C 和 D ，64变28位
			int c[] = {
					57,49,41,33,25,17,9,
					1,58,50,42,34,26,18,
					10,2,59,51,43,35,27,
					19,11,3,60,52,44,36
			};
			int d[] = {
					63,55,47,39,31,23,15,
					7,62,54,46,38,30,22,
					14,6,61,53,45,37,29,
					21,13,5,28,20,12,4
			};

			//左边28，右边28
			for(int i =0;i<28;i++)
			{
				left_ls[i] = Key[c[i]-1];
			}
			
			for(int i =0;i<28;i++)
			{
				right_ls[i] =Key[d[i]-1];
			}
			
			turn++;
			//换位以后进行移动位置操作,根据次数来,对应位移
			if(turn==1) 
			{
				yi =1;
			}
			else if(turn ==2)
			{
				yi = 2;
			}
			else if(turn ==3)
			{
				yi = 4;
			}
			else if(turn ==4)
			{
				yi = 6;
			}
			else if(turn ==5)
			{
				yi = 8;
			}
			else if(turn ==6)
			{
				yi = 10;
			}
			else if(turn ==7)
			{
				yi = 12;
			}
			else if(turn ==8)
			{
				yi = 14;
			}
			else if(turn ==9)
			{
				yi = 15;
			}
			else if(turn ==10)
			{
				yi = 17;
			}
			else if(turn ==11)
			{
				yi = 19;
			}
			else if(turn ==12)
			{
				yi = 21;
			}
			else if(turn ==13)
			{
				yi = 23;
			}
			else if(turn ==14)
			{
				yi = 25;
			}
			else if(turn ==15)
			{
				yi = 27;
			}
			else if(turn ==16)
			{
				yi = 28;
			}
			//开始移位left right,28位
			
			for(int i =0;i<yi;i++)
			{
				temp = left_ls[0];
				for(int j=0;j<27;j++)
			{
				left_ls[j] = left_ls[j+1];	
			}
				left_ls[left_ls.length-1] = temp;
			}
			
			for(int i =0;i<yi;i++)
			{
				temp = right_ls[0];
				for(int j=0;j<27;j++)
			{
				right_ls[j] = right_ls[j+1];	
			}
				right_ls[right_ls.length-1] = temp;
			}
			
			//合并移位的左右部分，需要56位的
			for(int i =0;i<28;i++)
			{
				key[i] =  left_ls[i];
			}
			for(int i =0;i<28;i++)
			{
				key[i+28] = right_ls[i];
			}
			//要保留下来做下一次的移位，并且与进入盒子变成48位的
			int p[] = {
					14,17,11,24,1,5,
					3,28,15,6,21,10,
					23,19,12,4,26,8,
					16,7,27,20,13,2,
					41,52,31,37,47,55,
					30,40,51,45,33,48,
					44,49,39,56,34,53,
					46,42,50,36,29,32
			};
			for(int i =0;i<48;i++)
			{
				key_converted[i] = key[p[i]-1];
			}
			return key_converted;
		}
		public static int[] yihuocaozuo(char[] x1,char[]x2,int length)
		{
			int[] yihuo = new int[length];
			
			for(int i =0;i<length;i++)
			{
				if(x1[i]==x2[i])
				{
					yihuo[i] = 0;
				}
				else
				{
					yihuo[i] = 1;
				}
			}
			
			return yihuo;
		}
		
		public static char[] int_to_char(int x[],int length)
		{
			char []c = new char[length];
			for(int i=0;i<length;i++)
			{
				String str1 = String.valueOf(x[i]);
				char[] arr = str1.toCharArray();
				/*for (int j = 0; j < arr.length; j++) {
				    System.out.print(arr[j]);
				}*/
				c[i]=arr[arr.length-1];
			}
			return c;
		}
		public static char[] she(int[] yihuo_one)
		{
			//将yihuo_one 分为8组，每组6个
			char[] r = new char[32];
			
int[][] s1 = new int[][]{
				{14,4,13,1,2,15,11,8,3,10,6,12,5,9,0,7},
				{0,15,7,4,14,2,13,1,10,6,12,11,9,5,3,8},
				{4,1,14,8,13,6,2,11,15,12,9,7,3,10,5,0},
				{15,12,8,2,4,9,1,7,5,11,3,14,10,0,6,13}
				};
int[][] s2 = new int[][]{
				{15,1,8,14,6,11,3,4,9,7,2,13,12,0,5,10},
				{3,13,4,7,15,2,8,14,12,0,1,10,6,9,11,5},
				{0,14,7,11,10,4,13,1,5,8,12,6,9,3,2,15},
				{13,8,10,1,3,15,4,2,11,6,7,12,0,5,14,9}
				};
int[][] s3 = new int[][]{
				{10,0,9,14,6,3,15,5,1,13,12,7,11,4,2,8},
				{13,7,0,9,3,4,6,10,2,8,5,14,12,11,15,1},
				{13,6,4,9,8,15,3,0,11,1,2,12,5,10,14,7},
				{1,10,13,0,6,9,8,7,4,15,14,3,11,5,2,12}
				};
int[][] s4 = new int[][]{
				{7,13,14,3,0,6,9,10,1,2,8,5,11,12,4,15},
				{13,8,11,5,6,15,0,3,4,7,2,12,1,10,14,9},
				{10,6,9,0,12,11,7,13,15,1,3,14,5,2,8,4},
				{3,15,0,6,10,1,13,8,9,4,5,11,12,7,2,14}
				};
int[][] s5 = new int[][]{
				{2,12,4,1,7,10,11,6,8,5,3,15,13,0,14,9},
				{14,11,2,12,4,7,13,1,5,0,15,10,3,9,8,6},
				{4,2,1,11,10,13,7,8,15,9,12,5,6,3,0,14},
				{11,8,12,7,1,14,2,13,6,15,0,9,10,4,5,3}
				};
int[][] s6 = new int[][]{
				{12,1,10,15,9,2,6,8,0,13,3,4,14,7,5,11},
				{10,15,4,2,7,12,9,5,6,1,13,14,0,11,3,8},
				{9,14,15,5,2,8,12,3,7,0,4,10,1,13,11,6},
				{4,3,2,12,9,5,15,10,11,14,1,7,6,0,8,13}
				};
int[][] s7 = new int[][]{
				{4,11,2,14,15,0,8,13,3,12,9,7,5,10,6,1},
				{13,0,11,7,4,9,1,10,14,3,5,12,2,15,8,6},
				{1,4,11,13,12,3,7,14,10,15,6,8,0,5,9,2},
				{6,11,13,8,1,4,10,7,9,5,0,15,14,2,3,12}
				};
int[][] s8 = new int[][]{
				{13,2,8,4,6,15,11,1,10,9,3,14,5,0,12,7},
				{1,15,13,8,10,3,7,4,12,5,6,11,0,14,9,2},
				{7,11,4,1,9,12,14,2,0,6,10,13,15,3,5,8},
				{2,1,14,7,4,10,8,13,15,12,9,0,3,5,6,11}
				};
				int z1[] = new int[6];
				int z2[] = new int[6];
				int z3[] = new int[6];
				int z4[] = new int[6];
				int z5[] = new int[6];
				int z6[] = new int[6];
				int z7[] = new int[6];
				int z8[] = new int[6];
				char four1[] = new char[4];
				char four2[] = new char[4];
				char four3[] = new char[4];
				char four4[] = new char[4];
				char four5[] = new char[4];
				char four6[] = new char[4];
				char four7[] = new char[4];
				char four8[] = new char[4];
				for(int j=0;j<6;j++)
				{
					z1[j] = yihuo_one[j];
					z2[j] = yihuo_one[j+6];
					z3[j] = yihuo_one[j+12];
					z4[j] = yihuo_one[j+18];
					z5[j] = yihuo_one[j+24];
					z6[j] = yihuo_one[j+30];
					z7[j] = yihuo_one[j+36];
					z8[j] = yihuo_one[j+42];
				};
				for(int i=0;i<4;i++)
				{
				four1[i] = choose(z1,s1)[i];
				four2[i] = choose(z2,s2)[i];
				four3[i]=  choose(z3,s3)[i];
				four4[i] = choose(z4,s4)[i];
				four5[i] = choose(z5,s5)[i];
				four6[i] = choose(z6,s6)[i];
				four7[i] = choose(z7,s7)[i];
				four8[i]= choose(z8,s8)[i];
				}
				//将盒子的内容拼接起来，就是32位的int型
				for(int i=0;i<4;i++)
				{
					r[i] = four1[i];
					r[i+4] = four2[i];
					r[i+8] = four3[i];
					r[i+12] = four4[i];
					r[i+16] = four5[i];
					r[i+20] = four6[i];
					r[i+24] = four7[i];
					r[i+28] = four8[i];
				}
				
				return r;
		}
		public static char[] choose(int[] six,int[][] s)
		{
			int hang,lie;
			hang = six[0]*2+six[5];
			lie = six[1]*8+six[2]*4+six[3]*2+six[4];
			int zhao;
			char[] x = new char[4];
			zhao = s[hang][lie];
			for(int i = 0;i<4;i++)
			{
				x[i] = int_to_char(binaryToDecimal(zhao),4)[i];
			}
			return x;
			
		}
		
		public static int[] binaryToDecimal(int n){
			 int j[] = new int[4];
			
				    for(int i = 3,k=0;i >= 0; i--,k++)
				    {
				    	j[k]= n >>> i & 1;
				      
				    }
				 
				    return j;
		 }
		
		public static char[] p32(char[] x)
		{
			char finals[] = new char[32];
		
			int p32[] = new int[]{
					16,7,20,21,29,12,28,
					17,1,15,23,26,5,18,31
					,10,2,8,24,14,32,27,3,
					9,19,13,30,6,22,11,4,25};
			for(int i =0;i<32;i++)
			{
				finals[i] = x[p32[i]-1];
			}
			
			return finals;
		}
		
		public static char[] ip_one(char[] M)
		{
			char x[] = new char[64];
			int ip[] = {
					    40,  8, 48, 16, 56, 24, 64, 32,
						39,  7, 47, 15, 55, 23, 63, 31,
						38,  6, 46, 14, 54, 22, 62, 30,
						37,  5, 45, 13, 53, 21, 61, 29,
						36,  4, 44, 12, 52, 20, 60, 28,
						35,  3, 43, 11, 51, 19, 59, 27,
						34,  2, 42, 10, 50, 18, 58, 26,
						33,  1, 41,  9, 49, 17, 57, 25
			};
			
			for(int i=0;i<64;i++)
			{
				x[i]=M[ip[i]-1];
			}			
			return x;
		}
		public static char[] decimaltox(char[] x,int length)
		{
			char[] k = new char[length/4];
			int[] r =new int[4];
			int temp ;
			for(int i=0,j=0;i+3<length;i=i+4,j++)
			{
				r[0] = x[i]-'0';
				r[1] = x[i+1]-'0';
				r[2] = x[i+2]-'0';
				r[3] = x[i+3]-'0';
				temp = r[0]*8+r[1]*4+r[2]*2+r[3];
				
				if(temp<10)
				{
					String str1 = String.valueOf(temp);
					char[] arr = str1.toCharArray();
					/*for (int j = 0; j < arr.length; j++) {
					    System.out.print(arr[j]);
					}*/
					k[j]=arr[arr.length-1];
				}
				else if(temp==10)
				{
					k[j]='A';
				}
				else if(temp==11)
				{
					k[j]='B';
				}
				else if(temp==12)
				{
					k[j]='C';
				}
				else if(temp==13)
				{
					k[j]='D';
				}
				else if(temp==14)
				{
					k[j]='E';
				}
				else if(temp==15)
				{
					k[j]='F';
				}
				
				
			}
			return k;
		}
}
