package UI;

import java.io.*;

/**
 * Created by Nickwong on 31/07/2018.
 * 根据1-8楼的建议，优化了代码
 */
public class readSSL {
   public static void main(String[] args) throws IOException {
       //readSSL("C://Users//hasee//Desktop//cer");
       System.out.println( readSSL("C://Users//hasee//Desktop//cer"));
   }

   public static String readSSL(String filepath) {
       String[] a= { "ID", "n", "autograph" };
       ChangetoTXT(filepath);
       readFile(a,filepath+".txt");
       ChangetoSSL(filepath);
       return a[1];
    }

    public static void ChangetoSSL(String filepath){
        File file=new File(filepath+".txt");
        file.renameTo(new File(filepath+".ssl"));
    }

    public static void ChangetoTXT(String filepath){
        File file=new File(filepath+".ssl");
        file.renameTo(new File(filepath+".txt"));
    }
    /**
     * 读入TXT文件
     */
    public static void readFile(String a[],String filepath) {
        String pathname = filepath; // 绝对路径或相对路径都可以，写入文件时演示相对路径,读取以上路径的input.txt文件
        //防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw;
        //不关闭文件会导致资源的泄露，读写文件都同理
        //Java7的try-with-resources可以优雅关闭文件，异常时自动关闭文件；详细解读https://stackoverflow.com/a/12665271
        try (FileReader reader = new FileReader(pathname);
             BufferedReader br = new BufferedReader(reader) // 建立一个对象，它把文件内容转成计算机能读懂的语言
        ) {
            String line;
            //网友推荐更加简洁的写法
            int i=0;
            while ((line = br.readLine()) != null) {
                // 一次读入一行数据
                //在这里写那个SSL变量=文件里面该行
                //但是java能用引用在函数内部更改函数外不变量吗，神秘
                //记得加上参数
                a[i]=line;
                i++;
                //System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入TXT文件
     */
    public static void writeFile(String str) {
            FileOutputStream o = null;

              String path="YourFilePath";

              String filename="YourFileName.txt";

            byte[] buff = new byte[]{};
            try{
                File file = new File(path+filename);
                if(!file.exists()){
                    file.createNewFile();
                }
                buff=str.getBytes();
                o=new FileOutputStream(file,true);
                o.write(buff);
                o.flush();
                o.close();
            }
            /*File writeName = new File("output.txt"); // 相对路径，如果没有则要建立一个新的output.txt文件
            writeName.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
            try (FileWriter writer = new FileWriter(writeName);
                 BufferedWriter out = new BufferedWriter(writer)
            ) {
                out.write("我会写入文件啦1\r\n"); // \r\n即为换行
                out.write(a); // \r\n即为换行
                out.flush(); // 把缓存区内容压入文件*/

         catch (IOException e) {
            e.printStackTrace();
        }
    }
}



