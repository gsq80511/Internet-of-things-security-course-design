package DES;

public class DES_wzj {
    static int init_message[] = { 58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17,  9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7 };

    static int left_key[] = { 57,49,41,33,25,17,9,
            1,58,50,42,34,26,18,
            10,2,59,51,43,35,27,
            19,11,3,60,52,44,36 };

    static int right_key[] = { 63,55,47,39,31,23,15,
            7,62,54,46,38,30,22,
            14,6,61,53,45,37,29,
            21,13,5,28,20,12,4 };

    static int sub_key[] = { 14, 17, 11, 24,  1,  5,
            3, 28, 15,  6, 21, 10,
            23, 19, 12,  4, 26,  8,
            16,  7, 27, 20, 13,  2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32 };

    static int key_shift_sizes[] = { 1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1,0 };

    //非线性变换的盒子
    static int S1[] = { 14,  4, 13,  1,  2, 15, 11,  8,  3, 10,  6, 12,  5,  9,  0,  7,
            0, 15,  7,  4, 14,  2, 13,  1, 10,  6, 12, 11,  9,  5,  3,  8,
            4,  1, 14,  8, 13,  6,  2, 11, 15, 12,  9,  7,  3, 10,  5,  0,
            15, 12,  8,  2,  4,  9,  1,  7,  5, 11,  3, 14, 10,  0,  6, 13 };

    static int S2[] = { 15,  1,  8, 14,  6, 11,  3,  4,  9,  7,  2, 13, 12,  0,  5, 10,
            3, 13,  4,  7, 15,  2,  8, 14, 12,  0,  1, 10,  6,  9, 11,  5,
            0, 14,  7, 11, 10,  4, 13,  1,  5,  8, 12,  6,  9,  3,  2, 15,
            13,  8, 10,  1,  3, 15,  4,  2, 11,  6,  7, 12,  0,  5, 14,  9 };

     static int S3[] = { 10,  0,  9, 14,  6,  3, 15,  5,  1, 13, 12,  7, 11,  4,  2,  8,
            13,  7,  0,  9,  3,  4,  6, 10,  2,  8,  5, 14, 12, 11, 15,  1,
            13,  6,  4,  9,  8, 15,  3,  0, 11,  1,  2, 12,  5, 10, 14,  7,
            1, 10, 13,  0,  6,  9,  8,  7,  4, 15, 14,  3, 11,  5,  2, 12 };

     static int S4[] = { 7, 13, 14,  3,  0,  6,  9, 10,  1,  2,  8,  5, 11, 12,  4, 15,
            13,  8, 11,  5,  6, 15,  0,  3,  4,  7,  2, 12,  1, 10, 14,  9,
            10,  6,  9,  0, 12, 11,  7, 13, 15,  1,  3, 14,  5,  2,  8,  4,
            3, 15,  0,  6, 10,  1, 13,  8,  9,  4,  5, 11, 12,  7,  2, 14 };

    static  int S5[] = { 2, 12,  4,  1,  7, 10, 11,  6,  8,  5,  3, 15, 13,  0, 14,  9,
            14, 11,  2, 12,  4,  7, 13,  1,  5,  0, 15, 10,  3,  9,  8,  6,
            4,  2,  1, 11, 10, 13,  7,  8, 15,  9, 12,  5,  6,  3,  0, 14,
            11,  8, 12,  7,  1, 14,  2, 13,  6, 15,  0,  9, 10,  4,  5,  3 };

    static int S6[] = { 12,  1, 10, 15,  9,  2,  6,  8,  0, 13,  3,  4, 14,  7,  5, 11,
            10, 15,  4,  2,  7, 12,  9,  5,  6,  1, 13, 14,  0, 11,  3,  8,
            9, 14, 15,  5,  2,  8, 12,  3,  7,  0,  4, 10,  1, 13, 11,  6,
            4,  3,  2, 12,  9,  5, 15, 10, 11, 14,  1,  7,  6,  0,  8, 13 };

    static int S7[] = { 4, 11,  2, 14, 15,  0,  8, 13,  3, 12,  9,  7,  5, 10,  6,  1,
            13,  0, 11,  7,  4,  9,  1, 10, 14,  3,  5, 12,  2, 15,  8,  6,
            1,  4, 11, 13, 12,  3,  7, 14, 10, 15,  6,  8,  0,  5,  9,  2,
            6, 11, 13,  8,  1,  4, 10,  7,  9,  5,  0, 15, 14,  2,  3, 12 };

     static int S8[] = { 13,  2,  8,  4,  6, 15, 11,  1, 10,  9,  3, 14,  5,  0, 12,  7,
            1, 15, 13,  8, 10,  3,  7,  4, 12,  5,  6, 11,  0, 14,  9,  2,
            7, 11,  4,  1,  9, 12, 14,  2,  0,  6, 10, 13, 15,  3,  5,  8,
            2,  1, 14,  7,  4, 10,  8, 13, 15, 12,  9,  0,  3,  5,  6, 11 };

    static int message_expansion[] = { 32,  1,  2,  3,  4,  5,
            4,  5,  6,  7,  8,  9,
            8,  9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32,  1 };

    static int message_pbox[] = { 16,  7, 20, 21,
            29, 12, 28, 17,
            1, 15, 23, 26,
            5, 18, 31, 10,
            2,  8, 24, 14,
            32, 27,  3,  9,
            19, 13, 30,  6,
            22, 11,  4, 25 };

    static int final_message_p[] = { 40,  8, 48, 16, 56, 24, 64, 32,
            39,  7, 47, 15, 55, 23, 63, 31,
            38,  6, 46, 14, 54, 22, 62, 30,
            37,  5, 45, 13, 53, 21, 61, 29,
            36,  4, 44, 12, 52, 20, 60, 28,
            35,  3, 43, 11, 51, 19, 59, 27,
            34,  2, 42, 10, 50, 18, 58, 26,
            33,  1, 41,  9, 49, 17, 57, 25 };

//tag represent the direction of moving of the key
     static int []key_shift(int tag,int key[],int times) {
        if (tag == 0) {
            int cur0 = key[0];
            if (key_shift_sizes[times] == 1) {
                for (int i = 0; i < 27; i++) {
                    key[i] = key[i + 1];
                }
                key[27] = cur0;
            }
            else {
                int cur1 = key[1];
                for (int i = 0; i < 26; i++) {
                    key[i] = key[i + 1];
                }
                key[26] = cur0;
                key[27] = cur1;
            }
        }
        else {
            int cur27 = key[27];
            if (key_shift_sizes[15 - times] == 1) {
                for (int i = 27; i >0; i--) {
                    key[i] = key[i - 1];
                }
                key[0] = cur27;
            }
            else {
                int cur26 = key[26];
                for (int i = 26; i > 0; i--) {
                    key[i] = key[i - 1];
                }
                key[0] = cur26;
                key[1] = cur27;
            }
        }
        return key;
    }

     static int []change16toarray(int input[]) {
        int i=0;
        int []output = new int [64];
        int N = 0x80000000;
        for (i = 0; i < 32; i++) {
            output[i] = (input[0] & (N >> i)) >> (32 - i - 1);
        }
        for (; i < 64; i++) {
            output[i] = (input[1] & (N >> (i - 32))) >> (64 - i - 1);
        }
        for (int j = 0; j < 64; j++) {
            System.out.print(output[j]);
        }System.out.println();
        return output;
    }

    /*int[] input1(int tag) {
        String str;//存储输入的字符串
        if (tag == 0) System.out.println("please input the M:");
        else System.out.println("please input the D:" );
        str = "0000000000000000";
        str = "0x" + str;
        int []M = new int[2];
        char []M_left_char = new char[10];
        char []M_right_char = new char[10];
        for (int i = 0; i < 10; i++)M_left_char[i] = str.at(i);
        for (int i = 0; i < 10; i++) {
            if (i >= 2) M_right_char[i] = str.at(i + 8);
            else M_right_char[i] = str.at(i);
        }
        M[0] = (int)strtoul(M_left_char, 0, 16);
        M[1] = (int)strtoul(M_right_char, 0, 16);
        return change16toarray(M);
    }

    int* input2() {
        string str;
        cout << "please input the Key:" << endl;
        cin >> str;
        str = "0x" + str;
        int Key[2];
        char Key_left_char[10], Key_right_char[10];
        for (int i = 0; i < 10; i++)Key_left_char[i] = str.at(i);
        for (int i = 0; i < 10; i++) {
            if (i >= 2) Key_right_char[i] = str.at(i + 8);
            else Key_right_char[i] = str.at(i);
        }
        Key[0] = (int)strtoul(Key_left_char, 0, 16);
        Key[1] = (int)strtoul(Key_right_char, 0, 16);
        return change16toarray(Key);
    }*/

    static void printf16(int n, int s[]) {
        for (int i = 0; i < n / 4; i++) {
            int val = s[4 * i] * 8 + s[4 * i + 1] * 4 + s[4 * i + 2] * 2 + s[4 * i + 3];
            if (val < 10)System.out.print(val);
            else {
                if (val == 10)System.out.print("A");
                else if (val == 11)System.out.print("B");
                else if (val == 12)System.out.print("C");
                else if (val == 13)System.out.print("D");
                else if (val == 14)System.out.print("E");
                else if (val == 15)System.out.print("F");
            }
        }System.out.print("\n");
    }

//进入16轮循环前先对明文进行ip置换
    static int[] message_ip(int m[]) {
        int[] m_ip = new int[64];
        for (int i = 0; i < 64; i++) {
            m_ip[i] = m[init_message[i] - 1];
        }
        return m_ip;
    }

//右侧明文进行ep置换
    static int[] message_ep(int m[]) {
        int[] m_ep = new int[48];
        //System.out.print("ep扩展：");
        for (int i = 0; i < 48; i++){
            m_ep[i] = m[message_expansion[i] - 1];
            //System.out.print("%d",mp[i]);
        }
        //System.out.print("\n");
        ///System.out.print("f变换中的扩展运算："); //printf16(48, m_ep);
        return m_ep;
    }

//ep置换后与每轮的key做异或操作，返回48位选择数组
    static int[] message_key_xor(int m[], int k[]) {
        int []choose = new int [48];
        for (int i = 0; i < 48; i++) {
            choose[i] = m[i] ^ k[i];
        }
        //System.out.print("f变换中的异或运换：");
        //printf16(48, choose);
        return choose;
    }

//非线性s盒变换，返回32位数组
    static int[] nonlinear_transformation(int choose[]) {
        int[] result = new int[32];
        int []row = new int[8];
        int []col = new int[8];
        int []cur = new int[8];
        for (int i = 0; i < 8; i++) {
            row[i] = choose[i * 6] * 2 + choose[i * 6 + 5];//行号
            col[i] = choose[i * 6 + 1] * 8 + choose[i * 6 + 2] * 4 + choose[i * 6 + 3] * 2 + choose[i * 6 + 4] * 1;//列号
        }
        //十进制转换结果
        cur[0] = S1[col[0] + row[0] * 16];
        cur[1] = S2[col[1] + row[1] * 16];
        cur[2] = S3[col[2] + row[2] * 16];
        cur[3] = S4[col[3] + row[3] * 16];
        cur[4] = S5[col[4] + row[4] * 16];
        cur[5] = S6[col[5] + row[5] * 16];
        cur[6] = S7[col[6] + row[6] * 16];
        cur[7] = S8[col[7] + row[7] * 16];
        for (int i = 0; i < 8; i++) {
            result[4 * i] = (cur[i] & 8) >> 3;
            result[1 + 4 * i] = (cur[i] & 4) >> 2;
            result[2 + 4 * i] = (cur[i] & 2) >> 1;
            result[3 + 4 * i] = cur[i] & 1;
        }
        ////System.out.print("f变换中的S盒代换：");
        //printf16(32, result);
        return result;
    }

    static int[] init_key(int Key[]) {
        int []output = new int [56];
        for (int i = 0; i < 28; i++) {
            output[i] = Key[left_key[i] - 1];
        }
        for (int i = 0; i < 28; i++) {
            output[i+28] = Key[right_key[i] - 1];
        }
       // System.out.print("K+: ");
       // for (int i = 0; i < 56; i++)System.out.print(output[i]); System.out.println();
        return output;
    }

//times是第几轮循环，tag是加解密标识
    static int[] key_shift(int key[],int times,int tag) {
	/*for (i = 0; i < 28; i++) {
		cout << key[i];
	}cout << endl;*/
        if (tag == 0) {
            int key1 = key[0];
            if (key_shift_sizes[times] == 1) {
                for (int i = 0; i < 27; i++) {
                    key[i] = key[i + 1];
                }
                key[27] = key1;
            }
            if (key_shift_sizes[times] == 2) {
                int key2 = key[1];
                for (int i = 0; i < 26; i++) {
                    key[i] = key[i + 2];
                }
                key[26] = key1;
                key[27] = key2;
            }
        }
        else {
            int key27 = key[27];
            if (key_shift_sizes[16 - times] == 1) {
                for (int i = 26; i >= 0; i--) {
                    key[i + 1] = key[i];
                }
                key[0] = key27;
            }
            if (key_shift_sizes[16 - times] == 2) {
                int key26 = key[26];
                for (int i = 25; i >= 0; i--) {
                    key[i + 2] = key[i];
                }
                key[0] = key26;
                key[1] = key27;
            }
        }
        return key;
    }

    static int[] key_combine(int left[], int right[]) {
        int[] combine_key = new int [48];
        int []key = new int[56];
        int i = 0;
        for (; i < 28; i++) {
            key[i] = left[i];
            key[i + 28] = right[i];
        }
        for (i = 0; i < 48; i++) {
            combine_key[i] = key[sub_key[i] - 1];
        }

	/*for (i = 0; i < 48; i++) {
		cout << combine_key[i];
	}cout << endl;*/
       // System.out.print("密钥压缩置换：");
        //printf16(48, combine_key);
        return combine_key;
    }

//p盒置换,置换后即将与左侧相异或
    static int[] message_p(int message_right[]) {
        int[] m_p = new int[32];
        for (int i = 0; i < 32; i++) {
            m_p[i] = message_right[message_pbox[i] - 1];
        }
      //  System.out.print( "f变换中的P盒置换：");
        //printf16(32, m_p);
        return m_p;
    }


    static int[] message_left_right_xor(int message_left[], int message_right[] ) {
        int[] final_right = new int[32];
        for (int i = 0; i < 32; i++) {
            final_right[i] = message_left[i] ^ message_right[i];
        }
        //System.out.print("异或运算：");
        //printf16(32, final_right);
        return final_right;
    }

    static int[] message_exchange(int left[], int right[]) {
        int[] m = new int[64];
        for (int i = 0; i < 32; i++) {
            m[i] = right[i];
        }
        for (int i = 0; i < 32; i++) {
            m[i+32] = left[i];
        }
        return m;
    }

    static int[] message_final_ip(int m[]) {
        int[] message_to_d = new int[64];
        for (int i = 0; i < 64; i++) {
            message_to_d[i] = m[final_message_p[i] - 1];
        }
        return message_to_d;
    }

    public static void main(String args[]){
        DES_wzj des = new DES_wzj();
        String M ="0100010101100111010001010110011101000101011001110100010101100111";
        String K = "0100010101100111010001010110011101000101011001110100010101100111";
        String mi = des.des(0,M,K); 
        System.out.println("加密"+mi);
        
    }
    /*
	 * des循环加密,64的整数倍
	 */
	public static String des_jia_da(String big,String key)
	{
		int length = big.length();
		char[] M = big.toCharArray();
		int cishu = length/64;
		System.out.println("cishu"+cishu);
		String jiamihou = "";
		for(int i =0;i<cishu;i++)
		{
			String yaojiamixiao = big.substring(64*i, 64*(i+1));
			String jiamixiao = des(0,yaojiamixiao,key);
			//System.out.println("加密后小"+jiamixiao);
			jiamihou = jiamihou + jiamixiao;
		}
		
		return jiamihou;
		
	}
	/*
	 * des循环解密,64的整数倍
	 */
	public static String des_jie_da(String big,String key)
	{
		int length = big.length();
		char[] M = big.toCharArray();
		int cishu = length/64;
		System.out.println("cishu"+cishu);
		String jiemihou = "";
		for(int i =0;i<cishu;i++)
		{
			String jiemixiao = big.substring(64*i, 64*(i+1));
			//System.out.println("每次要解密的64位"+jiemixiao);
			//System.out.println("解密小"+jiemixiao);
			jiemihou = jiemihou + des(1,jiemixiao,key);
		}
		return jiemihou;
	}
    
    public static String des(int tag,String str_M,String str_K) {
            //0 means encrypt and other mean decrypt
           // System.out.println("M->D:0  D->M:1" );
            if (tag != 1 && tag != 0) return str_M;
            char []cM = str_M.toCharArray();
            char []cK = str_K.toCharArray();
            int[] M = new int[64];
            int[] K = new int[64];
            for(int i = 0;i<64;i++){
                M[i] = cM[i] - '0';
                K[i] = cK[i] - '0';
            }
           // System.out.println("M:");
            M = message_ip(M);
            K = init_key(K);
            int[] M_left = new int[32];
            int[] M_right = new int[32];
            for(int i=0;i<32;i++){
                M_left[i] = M[i];
                M_right[i] = M[i+32];
            }
            int[] K_left = new int[28];
            int[] K_right = new int[28];
            for(int i=0;i<28;i++){
                K_left[i] = K[i];
                K_right[i] = K[i+28];
            }
            int[] cur;
            for (int times = 0; times < 16; times++) {
              //  System.out.println("第" +times +"轮");
                cur = M_right;
			/*for (int i = 0; i < 56; i++) {
				cout << K[i];
				if (i == 27)cout << endl;
			}cout << endl;*/
                /*System.out.//printf16(56, K); System.out.//printf16(28, K_left); System.out.//printf16(28, K_right);*/
                M_right = message_left_right_xor(M_left, message_p(nonlinear_transformation(message_key_xor(message_ep(M_right), key_combine(key_shift(K_left, times, tag), key_shift(K_right, times, tag))))));
                M_left = cur;
                //printf16(32, M_left); //printf16(32, M_right);
            }
            String Dfinal = "";
            int[] D = message_final_ip(message_exchange(M_left, M_right));
            for(int i=0;i<D.length;i++)
            {
            	Dfinal = Dfinal +String.valueOf(D[i]);
            }
          /*  if (tag == 0)System.out.print("D is:");
            else System.out.print("M is:");
            //printf16(64, D);
            for (int i = 0; i < 64; i++) {
                System.out.print(D[i]);
            }System.out.println();
            System.out.print("-------------------------\n");*/
            return Dfinal;
    }
}
