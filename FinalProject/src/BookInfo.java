import java.util.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField; 
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Position;


import se.datadosen.component.RiverLayout;

public class BookInfo {
	// DB ���� ������
	Connection conn;		// DB ���� Connection ��ü��������
	
	int bookno;				// å ��ȣ�� Ȯ�����ִ� ����
	String key;				// �α��� üũ�� ���� key����
	int checking = 0;		// ������ ���� ���� ���� ����
	// �ֻ��� ������
	JFrame frame;
	String frameTitle = "ȸ�� �ֹ���� ����";

	// �ؽ�Ʈ �ڽ���
	JTextField loginId;		// �α��� �ʵ� ���÷��̸� ���� �ڽ�
	JPasswordField loginPsw;// �α��� �н����� �ʵ� ���÷��̸� ���� �ڽ�
    JTextField name;		// name �ʵ� ���÷��̸� ���� �ڽ�
    JTextField writer;		// writer �ʵ� ���÷��̸� ���� �ڽ�
    JTextField price;		// price �ʵ� ���÷��̸� ���� �ڽ�
    JTextField sale_price;	// sale_price �ʵ� ���÷��̸� ���� �ڽ�
    JTextField stocked;
    JTextField publisher;
    JTextField category;
    JTextField bookBirth;		// bookBirth �ʵ� ���÷��̸� ���� �ڽ�
    JTextField bookOrder;
    JTextField detail_count;
    JTextField member;
    JTextField mem_id;
    JTextField mem_psw;
    JTextField mem_name;
    JTextField mem_address;
    JTextField mem_phone;
    JTextField mem_email;
    JTextField mem_zipcode;
    JTextField mem_birth;
    
    NameListListener nameList1 = new NameListListener(); //detail_id�� �̾Ƴ��� ���� ������
    NameListListener2 nameList2 = new NameListListener2(); //detail_id�� �̾Ƴ��� ���� ������
    

    // ������ ���� �ڽ�
    JTextField search;		// ������ ����  �ʵ�

    // ���� ��ư��
    JRadioButton stock = new JRadioButton("Y");			// ���� ���θ� ����ϱ� ���� ���� ��ư
    JRadioButton notStock = new JRadioButton("N");		// ���� ���θ� ����ϱ� ���� ���� ��ư
    
    // ��ư��
    JButton bSearch;		// ���� ������ ���� ��ư
    JButton bSave;			// ���� ������ ���� ��ư
    JButton bDelete;		// ���� ������ ���� ��ư
    JButton bNew;			// �ű� ������ ���� ��ư
    JButton bPrint;			// ����� ���� ��ư
    JButton bPreview;		// �̸����⸦ ���� ��ư
    JButton loginButton;	// �α����� ���� ��ư
    JButton newUserButton; 	// ���� â�� ���� ���� ��ư
    JButton logoutButton;	// �α׾ƿ��� ���� ��ư
    JButton orderListButton;	// �ֹ�������� ���� ���� ��ư
    
    JButton bNewDelete; // ���� â���� ���� �ִ°� ����� ���� ��ư
    JButton userMake;	// ���� â���� ȸ������ ��ư
    
    JPanel mainPanel;
    JPanel loginPanel;
    JPanel rootPanel;
    
    // ����Ʈ
    JList names = new JList();			// ������ �̸��� ������ �ִ� ����Ʈ
    
    public static void main(String[] args) {
    	
       BookInfo client = new BookInfo();
       client.loginGUI();
       client.dbConnectionInit();
    }
    
    // �α����� ���� �޼ҵ�
    public boolean login(String userId, char[] userPsw) {
    	key = userId;
    	PreparedStatement stmt;
    	ResultSet rs;
    	String SQL = "SELECT mem_psw FROM member WhERE mem_id = ?";
    	String str = new String(userPsw);
    	if(key.equals("root")) {
    		if(str.equals("root")) {
    			checking = 1;
    			return true;
    		}
    	}
    	try {
    		stmt = conn.prepareStatement(SQL);
    		stmt.setString(1,userId);
    		rs=stmt.executeQuery();
    		if(rs.next()) {
    			if(rs.getString(1).equals(str)) {
    				checking = 2;
    				return true; // �α��μ���
    			}
    			else
    				return false; // ��й�ȣ ����ġ
    		}
    		return false;
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	return false; //�����ͺ��̽� ����
    	
    }
    
    // �α��� ȭ�� GUI ����
    private void loginGUI() {
    	
    	frame = new JFrame("SCH ���� �α���");
    	frame.setLocationRelativeTo(null);
    	
    	ImageIcon icon = new ImageIcon("src/mainLogin.PNG");
    	Image img = icon.getImage() ;
    	ImagePanel mainPanel = new ImagePanel(img);
    	
    	icon = new ImageIcon("src/0.PNG");
    	img = icon.getImage();
    	Image newimg = img.getScaledInstance( 400, 130,  java.awt.Image.SCALE_SMOOTH );
    	icon = new ImageIcon( newimg );
    	JLabel mainLabel = new JLabel();
    	mainLabel.setIcon(icon);

    	JLabel logText = new JLabel("�� �� ��");
    	JLabel logText2 = new JLabel("�� �� �� ȣ");
    	loginId = new JTextField(16);
    	loginPsw = new JPasswordField(15);
    	loginButton = new JButton("�α���");
    	newUserButton = new JButton("ȸ������");
    	mainPanel.setLayout(null);
    	mainPanel.add(logText);
    	mainPanel.add(logText2);
    	mainPanel.add(loginId);
    	mainPanel.add(loginPsw);
    	mainPanel.add(loginButton);
    	mainPanel.add(newUserButton);
    	mainPanel.add(mainLabel);
    	
    	logText.setBounds(200,180,50,50);
    	logText2.setBounds(185,205,80,50);
    	loginId.setBounds(260,195,100,20);
    	loginPsw.setBounds(260, 220, 100, 20);
    	loginButton.setBounds(380,190,100,20);
    	newUserButton.setBounds(380,220,100,20);
    	mainLabel.setBounds(130,30,400,130);

	   	
	   	loginButton.addActionListener(new LoginButtonListener());
	   	newUserButton.addActionListener(new NewUserButtonListener());
		frame.add(mainPanel);
	   	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	   	frame.setSize(700,350);
        frame.setVisible(true);
    	
    }
    // ȸ������ �α����ϸ� ���̴� ���� ����ȭ��
    private void mainGUI() {
    	frame = new JFrame("���� ����");
    	frame.setLocationRelativeTo(null);
    	String userName=null;
	   	try {
	   		Statement stmt = conn.createStatement();
	   		ResultSet rs = stmt.executeQuery("SELECT name FROM member WHERE mem_id ='"+key+"'");
	   		rs.next();
	   		userName = rs.getString("name");
	   	}
	   	catch (SQLException sqlex) {
    		System.out.println("SQL ���� : " + sqlex.getMessage());
    		sqlex.printStackTrace();
    	}
	   	
	   	ImageIcon icon = new ImageIcon("src/mainImage01.PNG");
    	Image img = icon.getImage() ;
    	
    
    	ImagePanel storePanel = new ImagePanel(img);
	   	storePanel.setLayout(null);
	   	ImagePanel topPanel = new ImagePanel(img);
    	JPanel bookPanel = new JPanel();
    	JPanel buttonPanel = new JPanel();
    	JPanel topButtonPanel = new JPanel();
    	topButtonPanel.setLayout(new GridLayout(2,1));
    	
    	JLabel mainLabel = new JLabel(userName+"�� ȯ���մϴ�!");
    	JButton bookButton1 = new JButton();
    	JButton bookButton2 = new JButton();
    	JButton bookButton3 = new JButton();
    	JButton bookButton4 = new JButton();
    	JButton orderListButton = new JButton("�ֹ� ���");
    	JButton logoutButton = new JButton("�α� �ƿ�");
    	JButton nextButton = new JButton("����");
    	
    	icon = new ImageIcon("src/0.PNG");
    	img = icon.getImage();
    	Image newimg = img.getScaledInstance( 400, 100,  java.awt.Image.SCALE_SMOOTH );
    	icon = new ImageIcon( newimg );
    	mainLabel.setIcon(icon);
    	topPanel.add(mainLabel);
    	mainLabel.setBounds(10, 0,600,110 );
    	topButtonPanel.add(orderListButton,BorderLayout.EAST);
    	topButtonPanel.add(logoutButton, BorderLayout.EAST);
    	topPanel.add(topButtonPanel);
    	topButtonPanel.setBounds(560,30,110,50);
    	orderListButton.addActionListener(new UserOrderButtonListener());
    	logoutButton.addActionListener(new LogoutButtonListener());
    	
    	icon = new ImageIcon("src/1.PNG");
    	img = icon.getImage() ;  
    	newimg = img.getScaledInstance( 150, 150,  java.awt.Image.SCALE_SMOOTH ) ;
    	icon = new ImageIcon( newimg );
    	bookButton1.setIcon(icon);
    	
    	icon = new ImageIcon("src/2.PNG");
    	img = icon.getImage() ;  
    	newimg = img.getScaledInstance( 150, 150,  java.awt.Image.SCALE_SMOOTH ) ;
    	icon = new ImageIcon( newimg );
    	bookButton2.setIcon(icon);
    	
    	icon = new ImageIcon("src/3.PNG");
    	img = icon.getImage() ;  
    	newimg = img.getScaledInstance( 150, 150,  java.awt.Image.SCALE_SMOOTH ) ;
    	icon = new ImageIcon( newimg );
    	bookButton3.setIcon(icon);
    	
    	icon = new ImageIcon("src/4.PNG");
    	img = icon.getImage() ;  
    	newimg = img.getScaledInstance( 150, 150,  java.awt.Image.SCALE_SMOOTH ) ;
    	icon = new ImageIcon( newimg );
    	bookButton4.setIcon(icon);
    	
    	bookButton1.setPreferredSize(new Dimension(150, 150));
    	bookButton2.setPreferredSize(new Dimension(150, 150));
    	bookButton3.setPreferredSize(new Dimension(150, 150));
    	bookButton4.setPreferredSize(new Dimension(150, 150));
    	bookPanel.add(bookButton1,BorderLayout.CENTER);
    	bookPanel.add(bookButton2,BorderLayout.CENTER);
    	bookPanel.add(bookButton3,BorderLayout.CENTER);
    	bookPanel.add(bookButton4,BorderLayout.CENTER);
    	buttonPanel.add(nextButton,BorderLayout.CENTER);
    	
    	bookButton1.addActionListener(new BookListener1());
    	bookButton2.addActionListener(new BookListener2());
    	bookButton3.addActionListener(new BookListener3());
    	bookButton4.addActionListener(new BookListener4());
    	nextButton.addActionListener(new NextPageListener1());
    	
    	Color color = new Color(156,244,216);
    	
    	storePanel.add(topPanel);
    	storePanel.add(bookPanel);
    	storePanel.add(buttonPanel);
    	topPanel.setBounds(0, 0, 700, 100);
    	bookPanel.setBounds(40,110,620,160);
    	bookPanel.setBackground(color);
    	buttonPanel.setBounds(290,280,60,33);
    	buttonPanel.setBackground(color);
    	frame.add(storePanel);
	   	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	   	frame.setSize(700,360);
        frame.setVisible(true);
    }
    
    private void mainGUI2() {
    	frame = new JFrame("���� ����");
    	frame.setLocationRelativeTo(null);
    	String userName=null;
	   	
	   	try {
	   		Statement stmt = conn.createStatement();
	   		ResultSet rs = stmt.executeQuery("SELECT name FROM member WHERE mem_id ='"+key+"'");
	   		rs.next();
	   		userName = rs.getString("name");
	   	}
	   	catch (SQLException sqlex) {
    		System.out.println("SQL ���� : " + sqlex.getMessage());
    		sqlex.printStackTrace();
    	}
	   	ImageIcon icon = new ImageIcon("src/mainImage01.PNG");
    	Image img = icon.getImage() ;
    	
    
    	ImagePanel storePanel = new ImagePanel(img);
	   	storePanel.setLayout(null);
	   	ImagePanel topPanel = new ImagePanel(img);
    	JPanel bookPanel = new JPanel();
    	JPanel buttonPanel = new JPanel();
    	JPanel topButtonPanel = new JPanel();
    	topButtonPanel.setLayout(new GridLayout(2,1));
    	
    	JLabel mainLabel = new JLabel(userName+"�� ȯ���մϴ�!");
    	JButton bookButton1 = new JButton();
    	JButton bookButton2 = new JButton();
    	JButton bookButton3 = new JButton();
    	JButton bookButton4 = new JButton();
    	JButton orderListButton = new JButton("�ֹ� ���");
    	JButton logoutButton = new JButton("�α� �ƿ�");
    	JButton nextButton = new JButton("����");
    	JButton previous = new JButton("����");
    	
    	icon = new ImageIcon("src/0.PNG");
    	img = icon.getImage() ;  
    	Image newimg = img.getScaledInstance( 400, 100,  java.awt.Image.SCALE_SMOOTH ) ;
    	icon = new ImageIcon( newimg );
    	mainLabel.setIcon(icon);
    	topPanel.add(mainLabel);
    	mainLabel.setBounds(10, 0,600,110 );
    	topButtonPanel.add(orderListButton,BorderLayout.EAST);
    	topButtonPanel.add(logoutButton, BorderLayout.EAST);
    	topPanel.add(topButtonPanel);
    	topButtonPanel.setBounds(560,30,110,50);
    	orderListButton.addActionListener(new UserOrderButtonListener());
    	logoutButton.addActionListener(new LogoutButtonListener());
    	
    	icon = new ImageIcon("src/5.PNG");
    	img = icon.getImage() ;  
    	newimg = img.getScaledInstance( 150, 150,  java.awt.Image.SCALE_SMOOTH ) ;
    	icon = new ImageIcon( newimg );
    	bookButton1.setIcon(icon);
    	
    	icon = new ImageIcon("src/6.PNG");
    	img = icon.getImage() ;  
    	newimg = img.getScaledInstance( 150, 150,  java.awt.Image.SCALE_SMOOTH ) ;
    	icon = new ImageIcon( newimg );
    	bookButton2.setIcon(icon);
    	
    	icon = new ImageIcon("src/7.PNG");
    	img = icon.getImage() ;  
    	newimg = img.getScaledInstance( 150, 150,  java.awt.Image.SCALE_SMOOTH ) ;
    	icon = new ImageIcon( newimg );
    	bookButton3.setIcon(icon);
    	
    	icon = new ImageIcon("src/8.PNG");
    	img = icon.getImage() ;  
    	newimg = img.getScaledInstance( 150, 150,  java.awt.Image.SCALE_SMOOTH ) ;
    	icon = new ImageIcon( newimg );
    	bookButton4.setIcon(icon);
    	
    	bookButton1.setPreferredSize(new Dimension(150, 150));
    	bookButton2.setPreferredSize(new Dimension(150, 150));
    	bookButton3.setPreferredSize(new Dimension(150, 150));
    	bookButton4.setPreferredSize(new Dimension(150, 150));
    	bookPanel.add(bookButton1,BorderLayout.CENTER);
    	bookPanel.add(bookButton2,BorderLayout.CENTER);
    	bookPanel.add(bookButton3,BorderLayout.CENTER);
    	bookPanel.add(bookButton4,BorderLayout.CENTER);
    	buttonPanel.add(previous,BorderLayout.CENTER);
    	buttonPanel.add(nextButton,BorderLayout.CENTER);
    	
    	bookButton1.addActionListener(new BookListener5());
    	bookButton2.addActionListener(new BookListener6());
    	bookButton3.addActionListener(new BookListener7());
    	bookButton4.addActionListener(new BookListener8());
    	previous.addActionListener(new PreviousPageListener1());
    	nextButton.addActionListener(new NextPageListener2());
    	
    	Color color = new Color(156,244,216);
    	
    	storePanel.add(topPanel);
    	storePanel.add(bookPanel);
    	storePanel.add(buttonPanel);
    	topPanel.setBounds(0, 0, 700, 100);
    	bookPanel.setBounds(40,110,620,160);
    	bookPanel.setBackground(color);
    	buttonPanel.setBounds(240,280,200,33);
    	buttonPanel.setBackground(color);
    	frame.add(storePanel);
	   	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	   	frame.setSize(700,360);
        frame.setVisible(true);
    }
    
    private void mainGUI3() {
    	frame = new JFrame("���� ����");
    	frame.setLocationRelativeTo(null);
    	String userName=null;
	   	
	   	try {
	   		Statement stmt = conn.createStatement();
	   		ResultSet rs = stmt.executeQuery("SELECT name FROM member WHERE mem_id ='"+key+"'");
	   		rs.next();
	   		userName = rs.getString("name");
	   	}
	   	catch (SQLException sqlex) {
    		System.out.println("SQL ���� : " + sqlex.getMessage());
    		sqlex.printStackTrace();
    	}
	   	ImageIcon icon = new ImageIcon("src/mainImage01.PNG");
    	Image img = icon.getImage() ;
    	
    	ImagePanel storePanel = new ImagePanel(img);
	   	storePanel.setLayout(null);
	   	ImagePanel topPanel = new ImagePanel(img);
    	JPanel bookPanel = new JPanel();
    	JPanel buttonPanel = new JPanel();
    	JPanel topButtonPanel = new JPanel();
    	topButtonPanel.setLayout(new GridLayout(2,1));
    	
    	JLabel mainLabel = new JLabel(userName+"�� ȯ���մϴ�!");
    	JButton bookButton1 = new JButton();
    	JButton bookButton2 = new JButton();
    	JButton bookButton3 = new JButton();
    	JButton bookButton4 = new JButton();
    	JButton orderListButton = new JButton("�ֹ� ���");
    	JButton logoutButton = new JButton("�α� �ƿ�");
    	JButton previous = new JButton("����");
    	
    	icon = new ImageIcon("src/0.PNG");
    	img = icon.getImage() ;  
    	Image newimg = img.getScaledInstance( 400, 100,  java.awt.Image.SCALE_SMOOTH ) ;
    	icon = new ImageIcon( newimg );
    	mainLabel.setIcon(icon);
    	topPanel.add(mainLabel);
    	mainLabel.setBounds(10, 0,600,110 );
    	topButtonPanel.add(orderListButton,BorderLayout.EAST);
    	topButtonPanel.add(logoutButton, BorderLayout.EAST);
    	topPanel.add(topButtonPanel);
    	topButtonPanel.setBounds(560,30,110,50);
    	orderListButton.addActionListener(new UserOrderButtonListener());
    	logoutButton.addActionListener(new LogoutButtonListener());
    	
    	icon = new ImageIcon("src/9.PNG");
    	img = icon.getImage() ;  
    	newimg = img.getScaledInstance( 150, 150,  java.awt.Image.SCALE_SMOOTH ) ;
    	icon = new ImageIcon( newimg );
    	bookButton1.setIcon(icon);
    	
    	icon = new ImageIcon("src/10.PNG");
    	img = icon.getImage() ;  
    	newimg = img.getScaledInstance( 150, 150,  java.awt.Image.SCALE_SMOOTH ) ;
    	icon = new ImageIcon( newimg );
    	bookButton2.setIcon(icon);
    	
    	icon = new ImageIcon("src/11.PNG");
    	img = icon.getImage() ;  
    	newimg = img.getScaledInstance( 150, 150,  java.awt.Image.SCALE_SMOOTH ) ;
    	icon = new ImageIcon( newimg );
    	bookButton3.setIcon(icon);
    	
    	icon = new ImageIcon("src/12.PNG");
    	img = icon.getImage() ;  
    	newimg = img.getScaledInstance( 150, 150,  java.awt.Image.SCALE_SMOOTH ) ;
    	icon = new ImageIcon( newimg );
    	bookButton4.setIcon(icon);
    	
    	bookButton1.setPreferredSize(new Dimension(150, 150));
    	bookButton2.setPreferredSize(new Dimension(150, 150));
    	bookButton3.setPreferredSize(new Dimension(150, 150));
    	bookButton4.setPreferredSize(new Dimension(150, 150));
    	bookPanel.add(bookButton1,BorderLayout.CENTER);
    	bookPanel.add(bookButton2,BorderLayout.CENTER);
    	bookPanel.add(bookButton3,BorderLayout.CENTER);
    	bookPanel.add(bookButton4,BorderLayout.CENTER);
    	buttonPanel.add(previous,BorderLayout.CENTER);
    	
    	bookButton1.addActionListener(new BookListener9());
    	bookButton2.addActionListener(new BookListener10());
    	bookButton3.addActionListener(new BookListener11());
    	bookButton4.addActionListener(new BookListener12());
    	previous.addActionListener(new PreviousPageListener2());
    	
    	Color color = new Color(156,244,216);
    	
    	storePanel.add(topPanel);
    	storePanel.add(bookPanel);
    	storePanel.add(buttonPanel);
    	topPanel.setBounds(0, 0, 700, 100);
    	bookPanel.setBounds(40,110,620,160);
    	bookPanel.setBackground(color);
    	buttonPanel.setBounds(290,280,60,33);
    	buttonPanel.setBackground(color);
    	frame.add(storePanel);
	   	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	   	frame.setSize(700,360);
        frame.setVisible(true);
    }
    
    // å ������ ���̴� å ���� GUI
    private void bookGUI(int check) {
    	frame = new JFrame("å ����");
    	frame.setLocationRelativeTo(null);
    	ImageIcon icon = new ImageIcon("src/"+check+".PNG");
    	Image img;
    	Image newImg;
    	
    	img = icon.getImage() ;  
    	newImg = img.getScaledInstance( 200, 250,  java.awt.Image.SCALE_SMOOTH ) ;
    	icon = new ImageIcon( newImg );
    	
    	JLabel rightBook = new JLabel();
    	rightBook.setIcon(icon);
    	
    	icon = new ImageIcon("src/mainImage04.PNG");
    	img = icon.getImage() ;
    	
    	ImagePanel mainPanel = new ImagePanel(img);
    	
    	JButton buy = new JButton("����");
    	JButton returnMain = new JButton("��������");
    	
    	returnMain.addActionListener(new MainReturnListener());
    	buy.addActionListener(new BookBuyListener());
    	
    	name = new JTextField(20);
    	writer = new JTextField(20);
    	price = new JTextField(20);
    	sale_price = new JTextField(20);
    	stocked = new JTextField(20);
    	category = new JTextField(20);
    	publisher = new JTextField(20);
	   	bookBirth = new JTextField(20);
    	
    	ButtonGroup state = new ButtonGroup();
    	state.add(stock);
    	state.add(notStock);
    	
    	JLabel title = new JLabel("å �� ��");
	    JLabel bookName = new JLabel("å ����");
	    JLabel bookWriter = new JLabel("��  ��");
	    JLabel bookPrice = new JLabel("��  ��");
	    JLabel bookStock = new JLabel("���� ����");
	    JLabel bookCost = new JLabel("�� �� ��");
	    JLabel bookCategory = new JLabel("�� ��");
	    JLabel bookPublisher = new JLabel("�� �� ��");
	    JLabel bookDate = new JLabel("�� �� ��");
    	
    	mainPanel.add(title);
    	mainPanel.add(bookName);
    	mainPanel.add("tab", name);
    	mainPanel.add(bookWriter);
    	mainPanel.add("tab", writer);
    	mainPanel.add(bookPrice);
    	mainPanel.add("tab", price);
    	mainPanel.add(bookStock);
    	mainPanel.add("tab", stock);
    	mainPanel.add("tab", notStock);
    	mainPanel.add(bookCost);
    	mainPanel.add("tab", sale_price);
    	mainPanel.add(bookCategory);
    	mainPanel.add("tab", category);
    	mainPanel.add(bookPublisher);
    	mainPanel.add("tab", publisher);
    	mainPanel.add(bookDate);
    	mainPanel.add("tab", bookBirth);
    	
    	title.setBounds(170,10,100,30);
    	bookName.setBounds(70,50,50,20);
    	name.setBounds(130,50,175,20);
    	bookWriter.setBounds(80,75,50,20);
    	writer.setBounds(130,75,175,20);
    	bookPrice.setBounds(80,100,50,20);
    	price.setBounds(130,100,175,20);
    	bookStock.setBounds(60,125,70,20);
    	stock.setBounds(130,125,40,20);
    	notStock.setBounds(170,125,50,20);
    	bookCost.setBounds(70,150,50,20);
    	sale_price.setBounds(130,150,175,20);
    	bookCategory.setBounds(80,175,50,20);
    	category.setBounds(130,175,175,20);
    	bookPublisher.setBounds(70,200,50,20);
    	publisher.setBounds(130,200,175,20);
    	bookDate.setBounds(70,225,50,20);
    	bookBirth.setBounds(130,225,175,20);
    	
    	
    	mainPanel.add(buy);
    	mainPanel.add("center",returnMain);
    	buy.setBounds(120,260,70,30);
    	returnMain.setBounds(200,260,90,30);
    	
    	mainPanel.add(rightBook);
    	rightBook.setBounds(400,30,200,250);
	   	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.setSize(700,350);
        frame.setVisible(true);
        
        try {

    		System.out.println("�����222");
    		Statement stmt = conn.createStatement();				// SQL ���� ����� ���� Statement ��ü
    		ResultSet rs = stmt.executeQuery("SELECT * FROM book WHERE book_id="+check);
    		if(rs.next()) {												// �������� ���ϵǾ ù��° ������ ��� 
    		name.setText(rs.getString("book.name"));			// DB���� ���� �� ���� ������ �ý�Ʈ �ڽ� ä��
    		writer.setText(rs.getString("book.writer"));		
    		price.setText(rs.getString("book.price"));
    		sale_price.setText(rs.getString("book.sale_price"));
    		category.setText(rs.getString("book.category"));
    		publisher.setText(rs.getString("publisher"));
    		if (rs.getString("book.state").equals("Y"))			
    			stock.setSelected(true);
    		else
    			notStock.setSelected(true);
    		bookBirth.setText(rs.getDate("book_data").toString());
    		}
    		stmt.close();
    	} catch (SQLException sqlex) {
    		System.out.println("SQL ���� : " + sqlex.getMessage());
    		sqlex.printStackTrace();
    	} catch (Exception ex) {
    		System.out.println("DB Handling ����(����Ʈ ������) : " + ex.getMessage());
    		ex.printStackTrace();
    	}
    }
    
    // ������ ���� GUI
    private void rootGUI() {
    	frame = new JFrame("�����ڸ��");
    	frame.setLocationRelativeTo(null);
	   	try {
	   		Statement stmt = conn.createStatement();
	   	}
	   	catch (SQLException sqlex) {
    		System.out.println("SQL ���� : " + sqlex.getMessage());
    		sqlex.printStackTrace();
    	}

	   	
	    ImageIcon icon = new ImageIcon("src/mainImage03.PNG");
    	Image img = icon.getImage() ;

	   	ImagePanel rightTopPanel = new ImagePanel(img);
	   	
	   	JLabel nameLabel = new JLabel("ȸ�� ���");
        JScrollPane cScroller = new JScrollPane(names);
        cScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        cScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        names.setVisibleRowCount(7);
        names.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        names.setFixedCellWidth(100);
        rightTopPanel.add(nameLabel);
        rightTopPanel.add("p center", cScroller);
        
        nameLabel.setBounds(130,0,200,40);
        cScroller.setBounds(110,50,100,150);
	   	
        // �Է� â��� �� (������ ��� �г�)
	   	mem_name = new JTextField(20);
	   	mem_id = new JTextField(20);
	   	mem_psw = new JTextField(20);
	   	mem_address = new JTextField(20);
	   	mem_phone = new JTextField(20);
	   	mem_email = new JTextField(20);
	   	mem_zipcode = new JTextField(20);
	   	mem_birth = new JTextField(20);
	   	
	   	JLabel title = new JLabel("ȸ �� �� ��");
	   	JLabel memName = new JLabel("�� ��");
	   	JLabel memId = new JLabel("�� �� ��");
	   	JLabel memPsw = new JLabel("�� �� �� ȣ");
	   	JLabel memAddress = new JLabel("�� ��");
	   	JLabel memPhone = new JLabel("�� �� ��");
	   	JLabel memEmail = new JLabel("�� �� ��");
	   	JLabel memZip = new JLabel("�� �� �� ȣ");
	   	JLabel memDate = new JLabel("�� �� �� ��");
	    
	    //ǥ���� ���� �󺧵�
	   	rightTopPanel.add(title);
	   	rightTopPanel.add(memName);
	   	rightTopPanel.add("tab", mem_name);
	   	rightTopPanel.add(memId);
	   	rightTopPanel.add("tab", mem_id);
	   	rightTopPanel.add(memPsw);
	   	rightTopPanel.add("tab", mem_psw);
	   	rightTopPanel.add(memAddress);
	   	rightTopPanel.add("tab", mem_address);
	   	rightTopPanel.add(memPhone);
	   	rightTopPanel.add("tab", mem_phone);
	   	rightTopPanel.add(memEmail);
	   	rightTopPanel.add("tab", mem_email);
	   	rightTopPanel.add(memZip);
	   	rightTopPanel.add("tab", mem_zipcode);
	   	rightTopPanel.add(memDate);
	   	rightTopPanel.add("tab",mem_birth);
	   	
	   	title.setBounds(500,0,100,40);
	   	memName.setBounds(420,50,50,20);
	   	mem_name.setBounds(470,50,150,20);
	   	memId.setBounds(405,75,50,20);
	   	mem_id.setBounds(470,75,150,20);
	   	memPsw.setBounds(390,100,70,20);
	   	mem_psw.setBounds(470,100,150,20);
	   	memAddress.setBounds(420,125,70,20);
	   	mem_address.setBounds(470,125,150,20);
	   	memPhone.setBounds(405,150,50,20);
	   	mem_phone.setBounds(470,150,150,20);
	   	memEmail.setBounds(405,175,50,20);
	   	mem_email.setBounds(470,175,150,20);
	   	memZip.setBounds(390,200,70,20);
	   	mem_zipcode.setBounds(470,200,150,20);
	   	memDate.setBounds(390,225,70,20);
	   	mem_birth.setBounds(470,225,150,20);

        // ���� �ϴ� �г�
	   	search = new JTextField(20);
        bSearch = new JButton("�˻�");
        orderListButton = new JButton("�ֹ����");
        logoutButton = new JButton("�α׾ƿ�");
        rightTopPanel.add(search);
        rightTopPanel.add(bSearch);
        rightTopPanel.add(orderListButton);
        rightTopPanel.add(logoutButton);
        search.setBounds(80,260,180,20);
        bSearch.setBounds(50,300,60,30);
        orderListButton.setBounds(120,300,90,30);
        logoutButton.setBounds(220,300,90,30);
        
        bSave = new JButton("����");
        bDelete = new JButton("����");
        rightTopPanel.add(bSave);
        rightTopPanel.add("tab", bDelete);
        bSave.setBounds(450,300,60,30);
        bDelete.setBounds(520,300,60,30);

        

        // ActionListener�� ����
		names.addListSelectionListener(nameList2);
		MySearchListener l = new MySearchListener();
		search.addActionListener(l);								// �ؽ�Ʈ �ڽ����� ���� ���� ���� ���� �� ��
        bSearch.addActionListener(l);								// ��ư���� ���� ������ �� ������ �ڵ鷯 ���
        bSave.addActionListener(new UserSaveButtonListener());
        bDelete.addActionListener(new UserDeleteButtonListener());
        orderListButton.addActionListener(new RootUserOrderButtonListener());
        logoutButton.addActionListener(new LogoutButtonListener());
        
        // Ŭ���̾�� ������ â ����
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(rightTopPanel);
        frame.setSize(700,400);
        frame.setVisible(true);
    	
    }
    //�ű԰��� GUI
    private void newGUI() {
    	frame = new JFrame("ȸ�� ����");
    	frame.setLocationRelativeTo(null);
	   	try {
	   		Statement stmt = conn.createStatement();
	   	}
	   	catch (SQLException sqlex) {
    		System.out.println("SQL ���� : " + sqlex.getMessage());
    		sqlex.printStackTrace();
    	}

		//  (���� ��� �г�)
	   	ImageIcon icon;
    	Image img;
	   	
	   	JPanel leftTopPanel = new JPanel(new RiverLayout());
	   	leftTopPanel.setBackground(Color.white);
	   	icon = new ImageIcon("src/newUser.PNG");
	   	img = icon.getImage() ;  
    	Image newimg = img.getScaledInstance( 300, 250,  java.awt.Image.SCALE_SMOOTH ) ;
    	icon = new ImageIcon( newimg );
    	JLabel iii = new JLabel(icon);
	   	JButton returnLogin = new JButton("�ڷ�");
        
        leftTopPanel.add("br center", new JLabel("SCH ������ ���� ���� ȯ���մϴ�!"));
        leftTopPanel.add("br",iii);
	   	
        // �Է� â��� �� (������ ��� �г�)
	   	JPanel rightTopPanel = new JPanel(new RiverLayout());
	   	rightTopPanel.setBackground(Color.white);
	   	mem_name = new JTextField(20);
	   	mem_id = new JTextField(20);
	   	mem_psw = new JTextField(20);
	   	mem_address = new JTextField(20);
	   	mem_phone = new JTextField(20);
	   	mem_email = new JTextField(20);
	   	mem_zipcode = new JTextField(20);
	   	mem_birth = new JTextField(20);
	    
	    //ǥ���� ���� �󺧵�
	   	rightTopPanel.add("br center", new JLabel("ȸ �� �� ��"));
	   	rightTopPanel.add("p left", new JLabel("�� ��"));
	   	rightTopPanel.add("tab", mem_name);
	   	rightTopPanel.add("br", new JLabel("�� �� ��"));
	   	rightTopPanel.add("tab", mem_id);
	   	rightTopPanel.add("br", new JLabel("�� �� �� ȣ"));
	   	rightTopPanel.add("tab", mem_psw);
	   	rightTopPanel.add("br", new JLabel("�� ��"));
	   	rightTopPanel.add("tab", mem_address);
	   	rightTopPanel.add("br", new JLabel("�� �� ��"));
	   	rightTopPanel.add("tab", mem_phone);
	   	rightTopPanel.add("br", new JLabel("�� �� ��"));
	   	rightTopPanel.add("tab", mem_email);
	   	rightTopPanel.add("br", new JLabel("�� �� �� ȣ"));
	   	rightTopPanel.add("tab", mem_zipcode);
	   	rightTopPanel.add("br", new JLabel("�� �� �� ��"));
	   	rightTopPanel.add("tab",mem_birth);

        
        // ������ �ϴ� �г�
	   	JPanel rightBottomPanel = new JPanel(new RiverLayout());
	   	rightBottomPanel.setBackground(Color.white);
	   	JPanel tmpPanel = new JPanel(new RiverLayout());
	   	tmpPanel.setBackground(Color.white);
        userMake = new JButton("����");
        bNewDelete = new JButton("�ʱ�ȭ");
        tmpPanel.add("right",userMake);
        tmpPanel.add("right", bNewDelete);
        tmpPanel.add("right",returnLogin);
        rightBottomPanel.add("tap center", tmpPanel);
        rightBottomPanel.add("br", Box.createRigidArea(new Dimension(0,20)));

	   	// GUI ��ġ
        
        JPanel topPanel = new JPanel(new GridLayout(1,2));
        topPanel.add(leftTopPanel);
        topPanel.add(rightTopPanel);
        JPanel bottomPanel = new JPanel(new GridLayout(1,2));
        bottomPanel.add(rightBottomPanel);

        rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(Color.white);
        rootPanel.add(topPanel, BorderLayout.CENTER);
        rootPanel.add(bottomPanel, BorderLayout.SOUTH);

        // ActionListener�� ����
		names.addListSelectionListener(nameList2);
        userMake.addActionListener(new UserMakeButtonListener());
        bNewDelete.addActionListener(new NewButtonListener2());
        returnLogin.addActionListener(new LoginReturnListener());
        
        // Ŭ���̾�� ������ â ����
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(rootPanel);
        frame.setSize(700,400);
        frame.setVisible(true);
    	
    }

    //�ڱ� �ڽ��� �ֹ� ���
    private void setUpGUI() {
    	// build GUI
	   	frame = new JFrame(frameTitle);
	   	frame.setLocationRelativeTo(null);
	   	String userName=null;
	   	
	   	try {
	   		Statement stmt = conn.createStatement();
	   		ResultSet rs = stmt.executeQuery("SELECT name FROM member WHERE mem_id ='"+key+"'");
	   		rs.next();
	   		userName = rs.getString("name");
	   	}
	   	catch (SQLException sqlex) {
    		System.out.println("SQL ���� : " + sqlex.getMessage());
    		sqlex.printStackTrace();
    	}
    	

        ImageIcon icon = new ImageIcon("src/testimg02.PNG");
    	Image img = icon.getImage() ;
    	
    	ImagePanel rightTopPanel = new ImagePanel(img);
	   	// �ֹ� ��� ��ü�� �����ִ� ��Ʈ�� (���� ��� �г�)
	   	JLabel nameLabel = new JLabel(userName+" ���� �ֹ� ����");
	   	JButton mainReturn;
	   	
        JScrollPane cScroller = new JScrollPane(names);
        cScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        cScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        names.setVisibleRowCount(7);
        names.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        names.setFixedCellWidth(100);
        rightTopPanel.add("br center", nameLabel);
        rightTopPanel.add("p center", cScroller);
        nameLabel.setBounds(100,0,200,40);
        cScroller.setBounds(110,50,100,150);
	   	
        // �Է� â��� �� (������ ��� �г�)
	   	
	   	
	   	name = new JTextField(20);
	   	writer = new JTextField(20);
	   	detail_count = new JTextField(20);
	   	price = new JTextField(20);
	   	bookBirth = new JTextField(20);
	   	bookOrder = new JTextField(20);
	    ButtonGroup stockBook = new ButtonGroup();				// ���� ��ư �׷�
	    stockBook.add(stock);
	    stockBook.add(notStock);
	    
	    //ǥ���� ���� �󺧵�
	    JLabel title = new JLabel("�� �� �� ��");
	    JLabel bookName = new JLabel("å ����");
	    JLabel bookWriter = new JLabel("��  ��");
	    JLabel bookPrice = new JLabel("��  ��");
	    JLabel bookStock = new JLabel("���� ����");
	    JLabel bookDate = new JLabel("�� �� ��");
	    JLabel orderDate = new JLabel("�� �� ��");
	   	rightTopPanel.add(title);
	   	rightTopPanel.add(bookName);
	   	rightTopPanel.add(name);
	   	rightTopPanel.add(bookWriter);
	   	rightTopPanel.add(writer);
	   	rightTopPanel.add(bookPrice);
	   	rightTopPanel.add(price);
	   	rightTopPanel.add(bookStock);
	   	rightTopPanel.add(stock);
	   	rightTopPanel.add(notStock);
	   	rightTopPanel.add(bookDate);
	   	rightTopPanel.add( bookBirth);
	   	rightTopPanel.add(orderDate);
	   	rightTopPanel.add(bookOrder);
	   	title.setBounds(500,0,100,40);
	   	bookName.setBounds(420,50,50,20);
	   	name.setBounds(470,50,150,20);
	   	bookWriter.setBounds(425,75,50,20);
	   	writer.setBounds(470,75,150,20);
	   	bookPrice.setBounds(425,100,50,20);
	   	price.setBounds(470,100,150,20);
	   	bookStock.setBounds(405,125,70,20);
	   	stock.setBounds(480,125,40,17);
	   	notStock.setBounds(520,125,40,17);
	   	bookDate.setBounds(415,150,50,20);
	   	bookBirth.setBounds(470,150,150,20);
	   	orderDate.setBounds(415,175,50,20);
	   	bookOrder.setBounds(470,175,150,20);
	   	
	   	name.setEditable(false);
	   	writer.setEditable(false);
	   	price.setEditable(false);
	   	bookBirth.setEditable(false);
	   	bookOrder.setEditable(false);
	   	stock.setEnabled(false);
	   	notStock.setEnabled(false);

	   	
        bPrint = new JButton("���");
        bPreview = new JButton("�̸�����");
        rightTopPanel.add(bPrint);
        rightTopPanel.add(bPreview);
        bPrint.setBounds(80,250,60,30);
        bPreview.setBounds(150,250,90,30);
        
        mainReturn = new JButton("����ȭ������");
        rightTopPanel.add("center", mainReturn);
        mainReturn.setBounds(470,250,120,30);

	   	// GUI ��ġ
       

        // ActionListener�� ����
		names.addListSelectionListener(nameList1);
		MySearchListener l = new MySearchListener();
        mainReturn.addActionListener(new MainReturnListener());
        bPrint.addActionListener(new DisplayButtonListener());
        bPreview.addActionListener(new DisplayButtonListener());
        
        // Ŭ���̾�� ������ â ����
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(rightTopPanel);
        frame.setSize(700,350);
        frame.setVisible(true);
        }
    
    //�����ڰ� ������ �� �ִ� ȸ���� �ֹ� ��� GUI
    //�����ڰ� �� �� �ִ� ȸ���� ���Ÿ��
    private void rootSetUpGUI() {
    	// build GUI
	   	frame = new JFrame(frameTitle);
	   	JButton returnButton = new JButton();
	   	frame.setLocationRelativeTo(null);
	   	try {
	   		Statement stmt = conn.createStatement();
	   	}
	   	catch (SQLException sqlex) {
    		System.out.println("SQL ���� : " + sqlex.getMessage());
    		sqlex.printStackTrace();
    	}
	   	
	   	ImageIcon icon = new ImageIcon("src/mainImage02.PNG");
    	Image img = icon.getImage() ;

	   	// �ֹ� ��� ��ü�� �����ִ� ��Ʈ�� (���� ��� �г�)
    	ImagePanel rightTopPanel = new ImagePanel(img);
	   	JLabel nameLabel = new JLabel("�ֹ� ����");
	   	
        JScrollPane cScroller = new JScrollPane(names);
        cScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        cScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        names.setVisibleRowCount(7);
        names.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        names.setFixedCellWidth(100);
        rightTopPanel.add("br center", nameLabel);
        rightTopPanel.add("p center", cScroller);
        nameLabel.setBounds(120,0,200,40);
        cScroller.setBounds(110,50,100,150);
	   	
        
	   	name = new JTextField(20);
	   	writer = new JTextField(20);
	   	detail_count = new JTextField(20);
	   	price = new JTextField(20);
	   	bookBirth = new JTextField(20);
	   	bookOrder = new JTextField(20);
	    ButtonGroup stockBook = new ButtonGroup();				// ���� ��ư �׷�
	    stockBook.add(stock);
	    stockBook.add(notStock);
	    
	    //ǥ���� ���� �󺧵�
	    JLabel title = new JLabel("�� �� �� ��");
	    JLabel bookName = new JLabel("å ����");
	    JLabel bookWriter = new JLabel("��  ��");
	    JLabel bookPrice = new JLabel("��  ��");
	    JLabel bookStock = new JLabel("���� ����");
	    JLabel bookDate = new JLabel("�� �� ��");
	    JLabel orderDate = new JLabel("�� �� ��");
	    
	    rightTopPanel.add(title);
	   	rightTopPanel.add(bookName);
	   	rightTopPanel.add(name);
	   	rightTopPanel.add(bookWriter);
	   	rightTopPanel.add(writer);
	   	rightTopPanel.add(bookPrice);
	   	rightTopPanel.add(price);
	   	rightTopPanel.add(bookStock);
	   	rightTopPanel.add(stock);
	   	rightTopPanel.add(notStock);
	   	rightTopPanel.add(bookDate);
	   	rightTopPanel.add( bookBirth);
	   	rightTopPanel.add(orderDate);
	   	rightTopPanel.add(bookOrder);
	   	title.setBounds(500,0,100,40);
	   	bookName.setBounds(420,50,50,20);
	   	name.setBounds(470,50,150,20);
	   	bookWriter.setBounds(425,75,50,20);
	   	writer.setBounds(470,75,150,20);
	   	bookPrice.setBounds(425,100,50,20);
	   	price.setBounds(470,100,150,20);
	   	bookStock.setBounds(405,125,70,20);
	   	stock.setBounds(480,125,40,17);
	   	notStock.setBounds(520,125,40,17);
	   	bookDate.setBounds(415,150,50,20);
	   	bookBirth.setBounds(470,150,150,20);
	   	orderDate.setBounds(415,175,50,20);
	   	bookOrder.setBounds(470,175,150,20);
	   	
	   	name.setEditable(false);
	   	writer.setEditable(false);
	   	price.setEditable(false);
	   	bookBirth.setEditable(false);
	   	bookOrder.setEditable(false);
	   	stock.setEnabled(false);
	   	notStock.setEnabled(false);

        // ���� �ϴ� �г�
	   	
	   	search = new JTextField(20);
        bSearch = new JButton("�˻�");
        bPrint = new JButton("���");
        bPreview = new JButton("�̸�����");
        rightTopPanel.add(search);
        rightTopPanel.add(bSearch);
        rightTopPanel.add(bPrint);
        rightTopPanel.add(bPreview);
        
        search.setBounds(80,220,180,20);
        bSearch.setBounds(50,250,60,30);
        bPrint.setBounds(120,250,60,30);
        bPreview.setBounds(190,250,90,30);
        
        
        bDelete = new JButton("����");
        returnButton = new JButton("���ư���");
        rightTopPanel.add("tab", bDelete);
        rightTopPanel.add("tab",returnButton);
        bDelete.setBounds(450,250,60,30);
        returnButton.setBounds(520,250,90,30);

	   	// GUI ��ġ


        // ActionListener�� ����
		names.addListSelectionListener(nameList1);
		MySearchListener l = new MySearchListener();
		search.addActionListener(l);								// �ؽ�Ʈ �ڽ����� ���� ���� ���� ���� �� ��
        bSearch.addActionListener(l);								// ��ư���� ���� ������ �� ������ �ڵ鷯 ���
        bDelete.addActionListener(new UserOrderDeleteButtonListener());
        bPrint.addActionListener(new DisplayButtonListener());
        bPreview.addActionListener(new DisplayButtonListener());
        returnButton.addActionListener(new RootReturnListener());
        
        // Ŭ���̾�� ������ â ����
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(rightTopPanel);
        frame.setSize(700,350);
        frame.setVisible(true);
        }

    // DB�� �����ϴ� �޼ҵ�
    private void dbConnectionInit() {
    	try {
    		Class.forName("com.mysql.jdbc.Driver");					// JDBC����̹��� JVM�������� ��������
    		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/proj2", "root", "mite");	// DB �����ϱ�
    	}
        catch (ClassNotFoundException cnfe) {
            System.out.println("JDBC ����̹� Ŭ������ ã�� �� �����ϴ� : " + cnfe.getMessage());
        }
        catch (Exception ex) {
            System.out.println("DB ���� ���� : " + ex.getMessage());
        }
	}
    // ���Ÿ�� �ѷ��ִ� �޼ҵ�
    // DB�� �ִ� ��ü ���ڵ带 �ҷ��ͼ� ����Ʈ�� �ѷ��ִ� �޼�
    public void prepareList() {
    	try {
    		Statement stmt = conn.createStatement();			// SQL ���� �ۼ��� ����  Statement ��ü ����

    		// ���� DB�� �ִ� ���� �����ؼ� �α����� ȸ���� ���� ����� names ����Ʈ�� ����ϱ�
    		ResultSet rs = stmt.executeQuery("SELECT * FROM detail_order INNER JOIN ordered ON detail_order.order_id =ordered.order_id"
    				+ " WHERE ordered.mem_id IN (SELECT mem_id FROM member WHERE mem_id ='"+key+"')");
    		Vector<String> list = new Vector<String>();
    		Comparator<String> myComparator= new Comparator<String>(){
    	          
                @Override
                public int compare(String s1, String s2)
                {
                    Integer val1 = Integer.parseInt(s1);
                    Integer val2 = Integer.parseInt(s2);
                    return val1.compareTo(val2);
                }
            };
    		while (rs.next()) {
    			list.add(rs.getString("detail_id"));
    		}
    		stmt.close();										// statement�� ����� �ݴ� ����
    		Collections.sort(list,myComparator);								// �켱 ��������
    		names.setListData(list);							// names�� ���� �Ӽ��� �״�� �ΰ� ���빰�� �ٲ۴�
    		if (!list.isEmpty())								// ����Ʈ�� �ٲ�� ���� �׻� ù��° �������� ����Ű�� 
    			names.setSelectedIndex(0);
    	} catch (SQLException sqlex) {
    		System.out.println("SQL ���� : " + sqlex.getMessage());
    		sqlex.printStackTrace();
    	}
    }
    public void prepareList2() { //������ ��忡�� �ѷ��ִ� ��
    	try {
    		Statement stmt = conn.createStatement();			// SQL ���� �ۼ��� ����  Statement ��ü ����

    		// ���� DB�� �ִ� ���� �����ؼ� ȸ�� ����� names ����Ʈ�� ����ϱ�
    		ResultSet rs = stmt.executeQuery("SELECT * FROM member");
    		Vector<String> list = new Vector<String>();
    		while (rs.next()) {
    			list.add(rs.getString("name"));
    		}
    		stmt.close();										// statement�� ����� �ݴ� ����
    		Collections.sort(list);								// �켱 ��������
    		names.setListData(list);							// names�� ���� �Ӽ��� �״�� �ΰ� ���빰�� �ٲ۴�
    		if (!list.isEmpty())								// ����Ʈ�� �ٲ�� ���� �׻� ù��° �������� ����Ű�� 
    			names.setSelectedIndex(0);
    	} catch (SQLException sqlex) {
    		System.out.println("SQL ���� : " + sqlex.getMessage());
    		sqlex.printStackTrace();
    	}
    }
    
    //	�����ڰ� ���Ÿ�� �����Ҷ� �ѷ��ִ� ��
    public void prepareList3() {
    	try {
    		Statement stmt = conn.createStatement();			// SQL ���� �ۼ��� ����  Statement ��ü ����

    		// ���� DB�� �ִ� ���� �����ؼ� �α����� ȸ���� ���� ����� names ����Ʈ�� ����ϱ�
    		ResultSet rs = stmt.executeQuery("SELECT * FROM detail_order INNER JOIN ordered ON detail_order.order_id =ordered.order_id"
    				+ " WHERE ordered.mem_id IN (SELECT mem_id FROM member WHERE name ='"+(String)names.getSelectedValue()+"')");
    		Vector<String> list = new Vector<String>();
    		Comparator<String> myComparator= new Comparator<String>(){
    	          
                @Override
                public int compare(String s1, String s2)
                {
                    Integer val1 = Integer.parseInt(s1);
                    Integer val2 = Integer.parseInt(s2);
                    return val1.compareTo(val2);
                }
            };
    		while (rs.next()) {
    			list.add(rs.getString("detail_id"));
    		}
    		stmt.close();										// statement�� ����� �ݴ� ����
    		Collections.sort(list,myComparator);								// �켱 ��������
    		names.setListData(list);							// names�� ���� �Ӽ��� �״�� �ΰ� ���빰�� �ٲ۴�
    		if (!list.isEmpty())								// ����Ʈ�� �ٲ�� ���� �׻� ù��° �������� ����Ű�� 
    			names.setSelectedIndex(0);
    	} catch (SQLException sqlex) {
    		System.out.println("SQL ���� : " + sqlex.getMessage());
    		sqlex.printStackTrace();
    	}
    }

	// ����Ʈ �ڽ��� �׼��� �Ͼ�� detail_id�� �̿��� � å�� �����ߴ��� �̾��ִ� �޼ҵ�
	public class NameListListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent lse) {					// ����Ʈ�� ������ �ٲ𶧸��� ȣ��
			if (!lse.getValueIsAdjusting() && !names.isSelectionEmpty()) {  // ���� ������ �� ���� ��쿡 ó��
		    	try {

		    		System.out.println("�����");
		    		Statement stmt = conn.createStatement();				// SQL ���� ����� ���� Statement ��ü
		    		ResultSet rs = stmt.executeQuery("SELECT * FROM detail_order INNER JOIN book ON detail_order.book_id ="
		    				+ "book.book_id INNER JOIN ordered ON ordered.order_id = detail_order.order_id"
		    				+ " WHERE detail_id="+(String)names.getSelectedValue());
		    		if(rs.next()) {												// �������� ���ϵǾ ù��° ������ ��� 
		    		name.setText(rs.getString("book.name"));			// DB���� ���� �� ���� ������ �ý�Ʈ �ڽ� ä��
		    		writer.setText(rs.getString("book.writer"));		
		    		price.setText(rs.getString("book.sale_price"));	
		    		if (rs.getString("book.state").equals("Y"))			
		    			stock.setSelected(true);
		    		else
		    			notStock.setSelected(true);
		    		bookBirth.setText(rs.getDate("book_data").toString());
		    		bookOrder.setText(rs.getString("ordered.date"));
		    		}
		    		stmt.close();
		    	} catch (SQLException sqlex) {
		    		System.out.println("SQL ���� : " + sqlex.getMessage());
		    		sqlex.printStackTrace();
		    	} catch (Exception ex) {
		    		System.out.println("DB Handling ����(����Ʈ ������) : " + ex.getMessage());
		    		ex.printStackTrace();
		    	}
			}
		}
	}
	// ȸ������ ������ �̾��ֱ� ���� �޼ҵ�
	public class NameListListener2 implements ListSelectionListener { //������ ���
		public void valueChanged(ListSelectionEvent lse) {					// ����Ʈ�� ������ �ٲ𶧸��� ȣ��
			if (!lse.getValueIsAdjusting() && !names.isSelectionEmpty()) {  // ���� ������ �� ���� ��쿡 ó��
		    	try {
		    		Statement stmt = conn.createStatement();				// SQL ���� ����� ���� Statement ��ü
		    		ResultSet rs = stmt.executeQuery("SELECT * FROM member WHERE name = '" +
		    				(String)names.getSelectedValue() + "'");
		    		System.out.println("�����");
		    		if(rs.next()) {												// �������� ���ϵǾ ù��° ������ ��� 
		    		mem_name.setText(rs.getString("name"));			// DB���� ���� �� ���� ������ �ý�Ʈ �ڽ� ä��
		    		mem_id.setText(rs.getString("mem_id"));	
		    		key = mem_id.getText();
		    		mem_psw.setText(rs.getString("mem_psw"));	
		    		mem_address.setText(rs.getString("address"));
		    		mem_phone.setText(rs.getString("phone"));
		    		mem_email.setText(rs.getString("email"));
		    		mem_zipcode.setText(rs.getString("zip_code"));
		    		mem_birth.setText(rs.getDate("birth").toString());
		    		}
		    		stmt.close();
		    	} catch (SQLException sqlex) {
		    		System.out.println("SQL ���� : " + sqlex.getMessage());
		    		sqlex.printStackTrace();
		    	} catch (Exception ex) {
		    		System.out.println("DB Handling ����(����Ʈ ������) : " + ex.getMessage());
		    		ex.printStackTrace();
		    	}
			}
		}
	}
	
	// ���� ������Ʈ�� ������
	public class MySearchListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			int index = names.getNextMatch(search.getText().trim(), 0, Position.Bias.Forward);
			if (index != -1) {
				names.setSelectedIndex(index);
			}
			search.setText("");
		}
	}

	// ȸ�� ���� ���� ��ư�� ������
	public class UserDeleteButtonListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			int choice;
			choice=JOptionPane.showConfirmDialog(frame, "���� �����Ͻðڽ��ϱ�?", "ȸ�� ����"
					+ "", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if(choice == 0) {
				try {
					Statement stmt = conn.createStatement();				// SQL ���� �ۼ��� ����  Statement ��ü ����
					stmt.executeUpdate("DELETE FROM detail_order Using detail_order INNER JOIN ordered ON detail_order.order_id = "
							+ "ordered.order_id WHERE ordered.mem_id IN (SELECT mem_id FROM member WHERE name ="
							+ "'"+(String)names.getSelectedValue()+"')");
					stmt.executeUpdate("DELETE FROM ordered WHERE mem_id IN (SELECT mem_id FROM member WHERE name ="
							+ "'"+(String)names.getSelectedValue()+"')");
					stmt.executeUpdate("DELETE FROM member WHERE name = '" +
							(String)names.getSelectedValue() + "'");
					System.out.println("����Ʈ �����");
					stmt.close();
					prepareList2();											// ����Ʈ �ڽ� �� ����Ʈ�� �ٽ� ä�� 
				} catch (SQLException sqlex) {
					System.out.println("SQL ���� : " + sqlex.getMessage());
					sqlex.printStackTrace();
				} catch (Exception ex) {
					System.out.println("DB Handling ����(DELETE ������) : " + ex.getMessage());
					ex.printStackTrace();
				}
			}
		}
	}
	//�����ڰ� ������ ���Ÿ���� ����� ������
	public class UserOrderDeleteButtonListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			int choice;
			choice=JOptionPane.showConfirmDialog(frame, "���� �����Ͻðڽ��ϱ�?", "���� ����"
					+ "", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if(choice == 0) {
	    	try {
	    		Statement stmt = conn.createStatement();				// SQL ���� �ۼ��� ����  Statement ��ü ����
	    		stmt.executeUpdate("DELETE FROM ordered WHERE order_id IN (SELECT order_id FROM detail_order WHERE detail_id ="
	    			+ (String)names.getSelectedValue()+")");
	    		JOptionPane.showMessageDialog(null,"�����Ǿ����ϴ�!");
	    		stmt.close();
	    		prepareList3();											// ����Ʈ �ڽ� �� ����Ʈ�� �ٽ� ä�� 
	    	} catch (SQLException sqlex) {
	    		System.out.println("SQL ���� : " + sqlex.getMessage());
	    		sqlex.printStackTrace();
	    	} catch (Exception ex) {
	    		System.out.println("DB Handling ����(DELETE ������) : " + ex.getMessage());
	    		ex.printStackTrace();
	    	}
			}
		}
	}
	// �����ڰ� ���� ���� �����Ҷ� ���� ������
	// �����ڰ� ���� ���� ������ ���� ��ư�� ������
	public class UserSaveButtonListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
	    	try {
	    		Statement stmt = conn.createStatement();				// SQL ���� �ۼ��� ����  Statement ��ü ����
	    		stmt.executeUpdate("Update member SET name ='"+mem_name.getText()+"', mem_id = '"+mem_id.getText()+"',mem_psw ='"
	    				+ mem_psw.getText()+"',address ='"+mem_address.getText()+"',phone ='"+mem_phone.getText()+"',email ='"
	    						+ mem_email.getText()+"',zip_code ='"+mem_zipcode.getText()+"',birth='"+mem_birth.getText()+"'"
	    								+ " WHERE name ='"+(String)names.getSelectedValue()+"'");
	    		JOptionPane.showMessageDialog(null,"�����Ǿ����ϴ�!");
	    		stmt.close();
	    		prepareList2();											// �ٽ� �ѷ� 
	    	} catch (SQLException sqlex) {
	    		System.out.println("SQL ���� : " + sqlex.getMessage());
	    		JOptionPane.showMessageDialog(null,"������ ������ Ȯ���ϼ���!","Message",JOptionPane.ERROR_MESSAGE);
	    		sqlex.printStackTrace();
	    	} catch (Exception ex) {
	    		System.out.println("DB Handling ����(SAVE ������) : " + ex.getMessage());
	    		ex.printStackTrace();
	    	}
		}
	}
	// �ű԰��� â���� ������ ������ ����Ǵ� ������
	// �ű԰��� ������
	public class UserMakeButtonListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			
	    	try {
	    		int nameCheck = 0;
	    		Statement stmt = conn.createStatement();				// SQL ���� �ۼ��� ����  Statement ��ü ����
	    		ResultSet rs = stmt.executeQuery("SELECT * FROM member");
	    		while(rs.next()) {
	    			if(mem_id.getText().equals(rs.getString("mem_id")))
	    				nameCheck = 1;
	    		}
	    		if(nameCheck == 0) {
	    		stmt.executeUpdate("INSERT INTO member (name, mem_id, mem_psw, address, phone,email,zip_code,birth) VALUES ('" +	// �� ���ڵ�� ����
	    				mem_name.getText().trim() + "', '" +
	    				mem_id.getText().trim() + "', '" +
	    				mem_psw.getText().trim() + "', '" +
	    				mem_address.getText().trim() + "', '" +
	    				mem_phone.getText().trim() + "', '" +
	    				mem_email.getText().trim() + "', '" +
	    				mem_zipcode.getText().trim() + "', '" +
	    				mem_birth.getText().trim() + "')");
	    		JOptionPane.showMessageDialog(null,"���ԵǾ����ϴ�!");
	    		prepareList2();	
	    		mem_name.setText("");
				mem_id.setText("");
				mem_psw.setText("");
				mem_address.setText("");
				mem_phone.setText("");
				mem_email.setText("");
				mem_zipcode.setText("");
				mem_birth.setText("");
	    		}
	    		else {
	    			JOptionPane.showMessageDialog(null,"���̵� �ߺ��Ǿ����ϴ�!\n �ٽ��Է��ϼ���","Message",JOptionPane.ERROR_MESSAGE);
	    		}
	    		stmt.close();
	    	} catch (SQLException sqlex) {
	    		JOptionPane.showMessageDialog(null,"�׸��� ����� �Է��ϼ���!","Message",JOptionPane.ERROR_MESSAGE);
	    		System.out.println("SQL ���� : " + sqlex.getMessage());
	    		sqlex.printStackTrace();
	    	} catch (Exception ex) {
	    		System.out.println("DB Handling ����(SAVE ������) : " + ex.getMessage());
	    		ex.printStackTrace();
	    	}
			
		}
	}
	// ���� �����ִ� ������
	public class NewButtonListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			name.setText("");
			writer.setText("");
			price.setText("");
			stock.setSelected(true);
			notStock.setSelected(false);
			bookBirth.setText("");
			names.clearSelection();
		}
	}
	//ȸ�� �����Ҷ� �̿��Ѵ�.
	public class NewButtonListener2 implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			mem_name.setText("");
			mem_id.setText("");
			mem_psw.setText("");
			mem_address.setText("");
			mem_phone.setText("");
			mem_email.setText("");
			mem_zipcode.setText("");
			mem_birth.setText("");
		}
	}
	//�̸� ���� ������
	// ����� ���� �׼��� �߻��ϸ� ó���ϴ� ������ (print�� preview)
	public class DisplayButtonListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			// DB���� �������� �����͸� rowObjects�� ���·� �����ϰ� �̵��� ����Ʈ�� Printer �Ǵ� Preview�� ���� ��
			ArrayList<RowObjects> rowList = new ArrayList<RowObjects>();	// ����� ����Ʈ
			RowObjects line;												// �ϳ��� ��
			PrintObject word;												// �ϳ��� �ܾ�
			String myName = null;
			try {
	    		Statement stmt = conn.createStatement();					// SQL ���� ����� ���� Statement ��ü
	    		ResultSet rs = stmt.executeQuery("SELECT * FROM detail_order INNER JOIN book ON detail_order.book_id="
	    				+ "book.book_id INNER JOIN ordered ON ordered.order_id = "
	    				+ "detail_order.order_id INNER JOIN member ON member.mem_id = "
	    				+ "ordered.mem_id WHERE member.mem_id ='"+key+"'");
	    		while(rs.next()) {
	    			line = new RowObjects();								// 5���� �ܾ 1��
	    			line.add(new PrintObject(rs.getString("book.name"), 25));
	    			line.add(new PrintObject(rs.getString("book.writer"), 15));
	    			line.add(new PrintObject(rs.getString("book.sale_price"), 10));
	    			line.add(new PrintObject(rs.getString("book.state"), 5));
	    			line.add(new PrintObject(rs.getString("book_data"), 10));
	    			line.add(new PrintObject(rs.getString("ordered.date"), 10));
	    			rowList.add(line);										// ����ؾ� �� ��ü ����Ʈ�� ����									
	    		}
	    		rs = stmt.executeQuery("SELECT * FROM member WHERE mem_id ='"+key+"'");
	    		rs.next();
	    		myName = rs.getString("member.name");
	    		stmt.close();
	    		
	    		// �� �������� Į�� ����� ���� �� �� ������
	    		line = new RowObjects();									// 5���� �ܾ 1��
    			line.add(new PrintObject("å ����", 25));
    			line.add(new PrintObject("����", 15));
    			line.add(new PrintObject("����", 10));
    			line.add(new PrintObject("����", 5));
    			line.add(new PrintObject("�Ⱓ��", 10));
    			line.add(new PrintObject("������", 10));

    			if (e.getSource() == bPrint) {
	    			Printer prt = new Printer(new PrintObject(myName+"�� å ���� ���", 20), line, rowList, true);
	    			prt.print();
    			}
    			else {
	    			Preview prv = new Preview(new PrintObject(myName+"�� å ���� ���", 20), line, rowList, true);
	    			prv.preview();
    			}
    				
			} catch (SQLException sqlex) {
	    		System.out.println("SQL ���� : " + sqlex.getMessage());
	    		sqlex.printStackTrace();
	    	} catch (Exception ex) {
	    		System.out.println("DB Handling ����(����Ʈ ������) : " + ex.getMessage());
	    		ex.printStackTrace();
	    	}
		}
	}
	//�α��� ��ư ������
	public class LoginButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			
		   if(login(loginId.getText(),loginPsw.getPassword())) {
				if(checking == 1) {
					frame.dispose();
					names.removeListSelectionListener(nameList1);
					names.removeListSelectionListener(nameList2);
					rootGUI();
					prepareList2();
					checking = 0;
				}
				else {
					frame.dispose();
					names.removeListSelectionListener(nameList1);
					names.removeListSelectionListener(nameList2);
					mainGUI();
		    		prepareList();
		    		checking = 0;
				}
			}
			else
				JOptionPane.showMessageDialog(null,"���̵� ��й�ȣ�� Ȯ���ϼ���!","Message",JOptionPane.ERROR_MESSAGE);
		}
		
	}
	//�ű� ���� GUI �����ִ� ������
	public class NewUserButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			names.removeListSelectionListener(nameList1);
			names.removeListSelectionListener(nameList2);
			newGUI();
		}
	}
	//����ȭ�鿡�� �α����� ������ �ڽ��� ���Ÿ���� �� �� �ִ� GUI�� �����ִ� ������
	public class UserOrderButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			names.removeListSelectionListener(nameList1);
			names.removeListSelectionListener(nameList2);
			setUpGUI();
			prepareList();
		}
	}
	//rootâ���� ������ ����� �ֹ� ����� �����ִ� ������
	public class RootUserOrderButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			names.removeListSelectionListener(nameList1);
			names.removeListSelectionListener(nameList2);
			rootSetUpGUI();
			prepareList3();
		}
	}
	//�α׾ƿ��� ������ �ٽ� �α��� GUI�� �����ִ� ������
	public class LogoutButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			names.removeListSelectionListener(nameList1);
			names.removeListSelectionListener(nameList2);
			loginGUI();
		}
	}
	
	// �ٽ� ������ GUI�� �Ѿ���� ���ִ� ������
	public class RootReturnListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			names.removeListSelectionListener(nameList1);
			names.removeListSelectionListener(nameList2);
			rootGUI();
			prepareList2();
		}
	}
	// ����ȭ������ ���ư��� ������
	public class MainReturnListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			names.removeListSelectionListener(nameList1);
			names.removeListSelectionListener(nameList2);
			mainGUI();
		}
	}
	// �α���ȭ������ ���ư��� ������
	public class LoginReturnListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			names.removeListSelectionListener(nameList1);
			names.removeListSelectionListener(nameList2);
			loginGUI();
		}
	}
	
	public class BookListener1 implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			names.removeListSelectionListener(nameList1);
			names.removeListSelectionListener(nameList2);
			bookno=1;
			bookGUI(1);
		}
	}
	public class BookListener2 implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			names.removeListSelectionListener(nameList1);
			names.removeListSelectionListener(nameList2);
			bookno=2;
			bookGUI(2);
		}
	}
	public class BookListener3 implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			names.removeListSelectionListener(nameList1);
			names.removeListSelectionListener(nameList2);
			bookno=3;
			bookGUI(3);
		}
	}
	public class BookListener4 implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			names.removeListSelectionListener(nameList1);
			names.removeListSelectionListener(nameList2);
			bookno=4;
			bookGUI(4);
		}
	}
	public class BookListener5 implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			names.removeListSelectionListener(nameList1);
			names.removeListSelectionListener(nameList2);
			bookno=5;
			bookGUI(5);
		}
	}
	public class BookListener6 implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			names.removeListSelectionListener(nameList1);
			names.removeListSelectionListener(nameList2);
			bookno=6;
			bookGUI(6);
		}
	}
	public class BookListener7 implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			names.removeListSelectionListener(nameList1);
			names.removeListSelectionListener(nameList2);
			bookno=7;
			bookGUI(7);
		}
	}
	public class BookListener8 implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			names.removeListSelectionListener(nameList1);
			names.removeListSelectionListener(nameList2);
			bookno=8;
			bookGUI(8);
		}
	}
	public class BookListener9 implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			names.removeListSelectionListener(nameList1);
			names.removeListSelectionListener(nameList2);
			bookno=9;
			bookGUI(9);
		}
	}
	public class BookListener10 implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			names.removeListSelectionListener(nameList1);
			names.removeListSelectionListener(nameList2);
			bookno=10;
			bookGUI(10);
		}
	}
	public class BookListener11 implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			names.removeListSelectionListener(nameList1);
			names.removeListSelectionListener(nameList2);
			bookno=11;
			bookGUI(11);
		}
	}
	public class BookListener12 implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			names.removeListSelectionListener(nameList1);
			names.removeListSelectionListener(nameList2);
			bookno=12;
			bookGUI(12);
		}
	}
	
	// å ���� ������
	public class BookBuyListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			int choice;
			choice=JOptionPane.showConfirmDialog(frame, "���� �����Ͻðڽ��ϱ�?", "���� Ȯ��"
					+ "", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if(choice == 0) {
			try {
	    		Statement stmt = conn.createStatement();				// SQL ���� �ۼ��� ����  Statement ��ü ����
	    		ResultSet rs;
	    		String orderId = null;
	    		String memId = null;
	    		rs = stmt.executeQuery("SELECT * FROM member WHERE mem_id ='"+key+"'");
	    		if(rs.next()) {
	    			memId = rs.getString("mem_id");
	    		}
	    		System.out.println(memId);
	    		stmt.executeUpdate("INSERT INTO ordered (mem_id,date) VALUES ('" +	// �� ���ڵ�� ����
	    				 memId+"',NOW())");
	    		rs = stmt.executeQuery("SELECT * FROM ordered");
	    		while(rs.next()){
	    			orderId = rs.getString("order_id");
	    		}
	    		stmt.executeUpdate("INSERT INTO detail_order (order_id, book_id) VALUES ("+orderId+","+bookno+")");
	    		stmt.executeUpdate("UPDATE detail_order INNER JOIN book ON detail_order.book_id ="
	    				+ " book.book_id SET count =1 , detail_order.price = book.sale_price");
	    		stmt.executeUpdate("UPDATE ordered INNER JOIN detail_order ON ordered.order_id = "
	    				+ "detail_order.order_id SET ordered.price = detail_order.price");
	    		stmt.close();
	    		JOptionPane.showMessageDialog(null,"���ŵǾ����ϴ�!");
	    		prepareList();
	    	} catch (SQLException sqlex) {
	    		System.out.println("SQL ���� : " + sqlex.getMessage());
	    		sqlex.printStackTrace();
	    	} catch (Exception ex) {
	    		System.out.println("DB Handling ����(SAVE ������) : " + ex.getMessage());
	    		ex.printStackTrace();
	    	}
			}
		}
			
		}
	
	public class NextPageListener1 implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			names.removeListSelectionListener(nameList1);
			names.removeListSelectionListener(nameList2);
			mainGUI2();
		}
	}
	public class NextPageListener2 implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			names.removeListSelectionListener(nameList1);
			names.removeListSelectionListener(nameList2);
			mainGUI3();
		}
	}
	public class PreviousPageListener1 implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			names.removeListSelectionListener(nameList1);
			names.removeListSelectionListener(nameList2);
			mainGUI();
		}
	}
	public class PreviousPageListener2 implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			names.removeListSelectionListener(nameList1);
			names.removeListSelectionListener(nameList2);
			mainGUI2();
		}
	}
}
