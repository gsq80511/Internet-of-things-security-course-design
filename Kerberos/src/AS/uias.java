package AS;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;



public class uias {
	   //内部类，监听事件？
		JFrame jFrame = new JFrame("AS服务器使用界面");
	    private Container c = jFrame.getContentPane();
	    private JLabel a1 = new JLabel("明文");
	    private JTextField mingwen = new JTextField();
	    private JLabel a2 = new JLabel("密文");
	    private JTextField miwen = new JTextField();
	    
	    private JLabel a3 = new JLabel("Kc,tgs");
	    private JTextField key1 = new JTextField();
	    
	    private JLabel a4 = new JLabel("Kc");
	    private JTextField key2 = new JTextField();
	    
	    
	    public uias(String ming,String mi,String Kc_tgs,String Kc) {
	        //设置窗体的位置及大小
	        jFrame.setBounds(600, 200, 1000, 250);
	        //设置一层相当于桌布的东西
	        c.setLayout(new BorderLayout());//布局管理器
	        //设置按下右上角X号后关闭
	        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        //初始化--往窗体里放其他控件
	        init(ming,mi,Kc_tgs,Kc);
	        //设置窗体可见
	        jFrame.setVisible(true);
	    }
	    public void init(String ming,String mi,String Kc_tgs,String Kc) {
	        /*标题部分--North*/
	        JPanel titlePanel = new JPanel();
	        titlePanel.setLayout(new FlowLayout());
	       // titlePanel.add(new JLabel("XXX登录系统"));
	        c.add(titlePanel, "North");
	        
	        /*输入部分--Center*/
	        JPanel fieldPanel = new JPanel();
	        fieldPanel.setLayout(null);
	        a1.setBounds(50, 20, 50, 20);
	        a2.setBounds(50, 60, 50, 20);
	        a3.setBounds(50, 100, 50, 20);
	        a4.setBounds(50, 140, 50, 20);
	        fieldPanel.add(a1);
	        fieldPanel.add(a2);
	        fieldPanel.add(a3);
	        fieldPanel.add(a4);
	        mingwen.setBounds(110, 20, 400, 20);
	        mingwen.setText(ming);
	        miwen.setBounds(110, 60, 400, 20);
	        miwen.setText(mi);
	        key1.setBounds(110, 100, 400, 20);
	        key1.setText(Kc_tgs);
	        key2.setBounds(110, 140, 400, 20);
	        key2.setText(Kc);
	        fieldPanel.add(mingwen);
	        fieldPanel.add(miwen);
	        fieldPanel.add(key1);
	        fieldPanel.add(key2);
	        c.add(fieldPanel, "Center");
	        
	       
	       
	       
	     
	    }
	    public static void main(String[] args) {
	       new uias("m","m","2","d");
	    }
}
