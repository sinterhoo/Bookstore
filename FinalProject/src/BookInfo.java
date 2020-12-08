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
	// DB 관련 변수들
	Connection conn;		// DB 연결 Connection 객체참조변수
	
	int bookno;				// 책 번호를 확인해주는 변수
	String key;				// 로그인 체크를 위한 key변수
	int checking = 0;		// 관리자 모드로 들어가기 위한 변수
	// 최상위 프레임
	JFrame frame;
	String frameTitle = "회원 주문목록 내역";

	// 텍스트 박스들
	JTextField loginId;		// 로그인 필드 디스플레이를 위한 박스
	JPasswordField loginPsw;// 로그인 패스워드 필드 디스플레이를 위한 박스
    JTextField name;		// name 필드 디스플레이를 위한 박스
    JTextField writer;		// writer 필드 디스플레이를 위한 박스
    JTextField price;		// price 필드 디스플레이를 위한 박스
    JTextField sale_price;	// sale_price 필드 디스플레이를 위한 박스
    JTextField stocked;
    JTextField publisher;
    JTextField category;
    JTextField bookBirth;		// bookBirth 필드 디스플레이를 위한 박스
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
    
    NameListListener nameList1 = new NameListListener(); //detail_id로 뽑아내기 위한 리스너
    NameListListener2 nameList2 = new NameListListener2(); //detail_id로 뽑아내기 위한 리스너
    

    // 색인을 위한 박스
    JTextField search;		// 색인을 위한  필드

    // 라디오 버튼들
    JRadioButton stock = new JRadioButton("Y");			// 절판 여부를 출력하기 위한 라디오 버튼
    JRadioButton notStock = new JRadioButton("N");		// 절판 여부를 출력하기 위한 라디오 버튼
    
    // 버튼들
    JButton bSearch;		// 색인 실행을 위한 버튼
    JButton bSave;			// 저장 실행을 위한 버튼
    JButton bDelete;		// 삭제 실행을 위한 버튼
    JButton bNew;			// 신규 실행을 위한 버튼
    JButton bPrint;			// 출력을 위한 버튼
    JButton bPreview;		// 미리보기를 위한 버튼
    JButton loginButton;	// 로그인을 위한 버튼
    JButton newUserButton; 	// 가입 창을 띄우기 위한 버튼
    JButton logoutButton;	// 로그아웃을 위한 버튼
    JButton orderListButton;	// 주문목록으로 들어가기 위한 버튼
    
    JButton bNewDelete; // 가입 창에서 적혀 있는거 지우기 위한 버튼
    JButton userMake;	// 가입 창에서 회원가입 버튼
    
    JPanel mainPanel;
    JPanel loginPanel;
    JPanel rootPanel;
    
    // 리스트
    JList names = new JList();			// 강아지 이름을 나열해 주는 리스트
    
    public static void main(String[] args) {
    	
       BookInfo client = new BookInfo();
       client.loginGUI();
       client.dbConnectionInit();
    }
    
    // 로그인을 위한 메소드
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
    				return true; // 로그인성공
    			}
    			else
    				return false; // 비밀번호 불일치
    		}
    		return false;
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	return false; //데이터베이스 오류
    	
    }
    
    // 로그인 화면 GUI 구성
    private void loginGUI() {
    	
    	frame = new JFrame("SCH 서점 로그인");
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

    	JLabel logText = new JLabel("아 이 디");
    	JLabel logText2 = new JLabel("비 밀 번 호");
    	loginId = new JTextField(16);
    	loginPsw = new JPasswordField(15);
    	loginButton = new JButton("로그인");
    	newUserButton = new JButton("회원가입");
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
    // 회원으로 로그인하면 보이는 서점 메인화면
    private void mainGUI() {
    	frame = new JFrame("서점 메인");
    	frame.setLocationRelativeTo(null);
    	String userName=null;
	   	try {
	   		Statement stmt = conn.createStatement();
	   		ResultSet rs = stmt.executeQuery("SELECT name FROM member WHERE mem_id ='"+key+"'");
	   		rs.next();
	   		userName = rs.getString("name");
	   	}
	   	catch (SQLException sqlex) {
    		System.out.println("SQL 에러 : " + sqlex.getMessage());
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
    	
    	JLabel mainLabel = new JLabel(userName+"님 환영합니다!");
    	JButton bookButton1 = new JButton();
    	JButton bookButton2 = new JButton();
    	JButton bookButton3 = new JButton();
    	JButton bookButton4 = new JButton();
    	JButton orderListButton = new JButton("주문 목록");
    	JButton logoutButton = new JButton("로그 아웃");
    	JButton nextButton = new JButton("다음");
    	
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
    	frame = new JFrame("서점 메인");
    	frame.setLocationRelativeTo(null);
    	String userName=null;
	   	
	   	try {
	   		Statement stmt = conn.createStatement();
	   		ResultSet rs = stmt.executeQuery("SELECT name FROM member WHERE mem_id ='"+key+"'");
	   		rs.next();
	   		userName = rs.getString("name");
	   	}
	   	catch (SQLException sqlex) {
    		System.out.println("SQL 에러 : " + sqlex.getMessage());
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
    	
    	JLabel mainLabel = new JLabel(userName+"님 환영합니다!");
    	JButton bookButton1 = new JButton();
    	JButton bookButton2 = new JButton();
    	JButton bookButton3 = new JButton();
    	JButton bookButton4 = new JButton();
    	JButton orderListButton = new JButton("주문 목록");
    	JButton logoutButton = new JButton("로그 아웃");
    	JButton nextButton = new JButton("다음");
    	JButton previous = new JButton("이전");
    	
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
    	frame = new JFrame("서점 메인");
    	frame.setLocationRelativeTo(null);
    	String userName=null;
	   	
	   	try {
	   		Statement stmt = conn.createStatement();
	   		ResultSet rs = stmt.executeQuery("SELECT name FROM member WHERE mem_id ='"+key+"'");
	   		rs.next();
	   		userName = rs.getString("name");
	   	}
	   	catch (SQLException sqlex) {
    		System.out.println("SQL 에러 : " + sqlex.getMessage());
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
    	
    	JLabel mainLabel = new JLabel(userName+"님 환영합니다!");
    	JButton bookButton1 = new JButton();
    	JButton bookButton2 = new JButton();
    	JButton bookButton3 = new JButton();
    	JButton bookButton4 = new JButton();
    	JButton orderListButton = new JButton("주문 목록");
    	JButton logoutButton = new JButton("로그 아웃");
    	JButton previous = new JButton("이전");
    	
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
    
    // 책 누르면 보이는 책 정보 GUI
    private void bookGUI(int check) {
    	frame = new JFrame("책 정보");
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
    	
    	JButton buy = new JButton("구매");
    	JButton returnMain = new JButton("메인으로");
    	
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
    	
    	JLabel title = new JLabel("책 정 보");
	    JLabel bookName = new JLabel("책 제목");
	    JLabel bookWriter = new JLabel("저  자");
	    JLabel bookPrice = new JLabel("가  격");
	    JLabel bookStock = new JLabel("절판 여부");
	    JLabel bookCost = new JLabel("판 매 가");
	    JLabel bookCategory = new JLabel("장 르");
	    JLabel bookPublisher = new JLabel("출 판 사");
	    JLabel bookDate = new JLabel("출 간 일");
    	
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

    		System.out.println("실행됨222");
    		Statement stmt = conn.createStatement();				// SQL 문장 만들기 위한 Statement 객체
    		ResultSet rs = stmt.executeQuery("SELECT * FROM book WHERE book_id="+check);
    		if(rs.next()) {												// 여러개가 리턴되어도 첫번째 것으로 사용 
    		name.setText(rs.getString("book.name"));			// DB에서 리턴 된 값을 가지고 택스트 박스 채움
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
    		System.out.println("SQL 에러 : " + sqlex.getMessage());
    		sqlex.printStackTrace();
    	} catch (Exception ex) {
    		System.out.println("DB Handling 에러(리스트 리스너) : " + ex.getMessage());
    		ex.printStackTrace();
    	}
    }
    
    // 관리자 전용 GUI
    private void rootGUI() {
    	frame = new JFrame("관리자모드");
    	frame.setLocationRelativeTo(null);
	   	try {
	   		Statement stmt = conn.createStatement();
	   	}
	   	catch (SQLException sqlex) {
    		System.out.println("SQL 에러 : " + sqlex.getMessage());
    		sqlex.printStackTrace();
    	}

	   	
	    ImageIcon icon = new ImageIcon("src/mainImage03.PNG");
    	Image img = icon.getImage() ;

	   	ImagePanel rightTopPanel = new ImagePanel(img);
	   	
	   	JLabel nameLabel = new JLabel("회원 목록");
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
	   	
        // 입력 창들과 라벨 (오른쪽 상단 패널)
	   	mem_name = new JTextField(20);
	   	mem_id = new JTextField(20);
	   	mem_psw = new JTextField(20);
	   	mem_address = new JTextField(20);
	   	mem_phone = new JTextField(20);
	   	mem_email = new JTextField(20);
	   	mem_zipcode = new JTextField(20);
	   	mem_birth = new JTextField(20);
	   	
	   	JLabel title = new JLabel("회 원 정 보");
	   	JLabel memName = new JLabel("이 름");
	   	JLabel memId = new JLabel("아 이 디");
	   	JLabel memPsw = new JLabel("비 밀 번 호");
	   	JLabel memAddress = new JLabel("주 소");
	   	JLabel memPhone = new JLabel("핸 드 폰");
	   	JLabel memEmail = new JLabel("이 메 일");
	   	JLabel memZip = new JLabel("우 편 번 호");
	   	JLabel memDate = new JLabel("생 년 월 일");
	    
	    //표식을 위한 라벨들
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

        // 왼쪽 하단 패널
	   	search = new JTextField(20);
        bSearch = new JButton("검색");
        orderListButton = new JButton("주문목록");
        logoutButton = new JButton("로그아웃");
        rightTopPanel.add(search);
        rightTopPanel.add(bSearch);
        rightTopPanel.add(orderListButton);
        rightTopPanel.add(logoutButton);
        search.setBounds(80,260,180,20);
        bSearch.setBounds(50,300,60,30);
        orderListButton.setBounds(120,300,90,30);
        logoutButton.setBounds(220,300,90,30);
        
        bSave = new JButton("수정");
        bDelete = new JButton("삭제");
        rightTopPanel.add(bSave);
        rightTopPanel.add("tab", bDelete);
        bSave.setBounds(450,300,60,30);
        bDelete.setBounds(520,300,60,30);

        

        // ActionListener의 설정
		names.addListSelectionListener(nameList2);
		MySearchListener l = new MySearchListener();
		search.addActionListener(l);								// 텍스트 박스에서 리턴 눌러 색인 시작 할 때
        bSearch.addActionListener(l);								// 버튼으로 색인 시작할 때 동일한 핸들러 사용
        bSave.addActionListener(new UserSaveButtonListener());
        bDelete.addActionListener(new UserDeleteButtonListener());
        orderListButton.addActionListener(new RootUserOrderButtonListener());
        logoutButton.addActionListener(new LogoutButtonListener());
        
        // 클라이언드 프레임 창 조정
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(rightTopPanel);
        frame.setSize(700,400);
        frame.setVisible(true);
    	
    }
    //신규가입 GUI
    private void newGUI() {
    	frame = new JFrame("회원 가입");
    	frame.setLocationRelativeTo(null);
	   	try {
	   		Statement stmt = conn.createStatement();
	   	}
	   	catch (SQLException sqlex) {
    		System.out.println("SQL 에러 : " + sqlex.getMessage());
    		sqlex.printStackTrace();
    	}

		//  (왼쪽 상단 패널)
	   	ImageIcon icon;
    	Image img;
	   	
	   	JPanel leftTopPanel = new JPanel(new RiverLayout());
	   	leftTopPanel.setBackground(Color.white);
	   	icon = new ImageIcon("src/newUser.PNG");
	   	img = icon.getImage() ;  
    	Image newimg = img.getScaledInstance( 300, 250,  java.awt.Image.SCALE_SMOOTH ) ;
    	icon = new ImageIcon( newimg );
    	JLabel iii = new JLabel(icon);
	   	JButton returnLogin = new JButton("뒤로");
        
        leftTopPanel.add("br center", new JLabel("SCH 서점에 오신 것을 환영합니다!"));
        leftTopPanel.add("br",iii);
	   	
        // 입력 창들과 라벨 (오른쪽 상단 패널)
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
	    
	    //표식을 위한 라벨들
	   	rightTopPanel.add("br center", new JLabel("회 원 정 보"));
	   	rightTopPanel.add("p left", new JLabel("이 름"));
	   	rightTopPanel.add("tab", mem_name);
	   	rightTopPanel.add("br", new JLabel("아 이 디"));
	   	rightTopPanel.add("tab", mem_id);
	   	rightTopPanel.add("br", new JLabel("비 밀 번 호"));
	   	rightTopPanel.add("tab", mem_psw);
	   	rightTopPanel.add("br", new JLabel("주 소"));
	   	rightTopPanel.add("tab", mem_address);
	   	rightTopPanel.add("br", new JLabel("핸 드 폰"));
	   	rightTopPanel.add("tab", mem_phone);
	   	rightTopPanel.add("br", new JLabel("이 메 일"));
	   	rightTopPanel.add("tab", mem_email);
	   	rightTopPanel.add("br", new JLabel("우 편 번 호"));
	   	rightTopPanel.add("tab", mem_zipcode);
	   	rightTopPanel.add("br", new JLabel("생 년 월 일"));
	   	rightTopPanel.add("tab",mem_birth);

        
        // 오른쪽 하단 패널
	   	JPanel rightBottomPanel = new JPanel(new RiverLayout());
	   	rightBottomPanel.setBackground(Color.white);
	   	JPanel tmpPanel = new JPanel(new RiverLayout());
	   	tmpPanel.setBackground(Color.white);
        userMake = new JButton("가입");
        bNewDelete = new JButton("초기화");
        tmpPanel.add("right",userMake);
        tmpPanel.add("right", bNewDelete);
        tmpPanel.add("right",returnLogin);
        rightBottomPanel.add("tap center", tmpPanel);
        rightBottomPanel.add("br", Box.createRigidArea(new Dimension(0,20)));

	   	// GUI 배치
        
        JPanel topPanel = new JPanel(new GridLayout(1,2));
        topPanel.add(leftTopPanel);
        topPanel.add(rightTopPanel);
        JPanel bottomPanel = new JPanel(new GridLayout(1,2));
        bottomPanel.add(rightBottomPanel);

        rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(Color.white);
        rootPanel.add(topPanel, BorderLayout.CENTER);
        rootPanel.add(bottomPanel, BorderLayout.SOUTH);

        // ActionListener의 설정
		names.addListSelectionListener(nameList2);
        userMake.addActionListener(new UserMakeButtonListener());
        bNewDelete.addActionListener(new NewButtonListener2());
        returnLogin.addActionListener(new LoginReturnListener());
        
        // 클라이언드 프레임 창 조정
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(rootPanel);
        frame.setSize(700,400);
        frame.setVisible(true);
    	
    }

    //자기 자신의 주문 목록
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
    		System.out.println("SQL 에러 : " + sqlex.getMessage());
    		sqlex.printStackTrace();
    	}
    	

        ImageIcon icon = new ImageIcon("src/testimg02.PNG");
    	Image img = icon.getImage() ;
    	
    	ImagePanel rightTopPanel = new ImagePanel(img);
	   	// 주문 목록 전체를 보여주는 컨트롤 (왼쪽 상단 패널)
	   	JLabel nameLabel = new JLabel(userName+" 님의 주문 내역");
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
	   	
        // 입력 창들과 라벨 (오른쪽 상단 패널)
	   	
	   	
	   	name = new JTextField(20);
	   	writer = new JTextField(20);
	   	detail_count = new JTextField(20);
	   	price = new JTextField(20);
	   	bookBirth = new JTextField(20);
	   	bookOrder = new JTextField(20);
	    ButtonGroup stockBook = new ButtonGroup();				// 라디오 버튼 그룹
	    stockBook.add(stock);
	    stockBook.add(notStock);
	    
	    //표식을 위한 라벨들
	    JLabel title = new JLabel("구 매 내 역");
	    JLabel bookName = new JLabel("책 제목");
	    JLabel bookWriter = new JLabel("저  자");
	    JLabel bookPrice = new JLabel("가  격");
	    JLabel bookStock = new JLabel("절판 여부");
	    JLabel bookDate = new JLabel("출 간 일");
	    JLabel orderDate = new JLabel("주 문 일");
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

	   	
        bPrint = new JButton("출력");
        bPreview = new JButton("미리보기");
        rightTopPanel.add(bPrint);
        rightTopPanel.add(bPreview);
        bPrint.setBounds(80,250,60,30);
        bPreview.setBounds(150,250,90,30);
        
        mainReturn = new JButton("메인화면으로");
        rightTopPanel.add("center", mainReturn);
        mainReturn.setBounds(470,250,120,30);

	   	// GUI 배치
       

        // ActionListener의 설정
		names.addListSelectionListener(nameList1);
		MySearchListener l = new MySearchListener();
        mainReturn.addActionListener(new MainReturnListener());
        bPrint.addActionListener(new DisplayButtonListener());
        bPreview.addActionListener(new DisplayButtonListener());
        
        // 클라이언드 프레임 창 조정
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(rightTopPanel);
        frame.setSize(700,350);
        frame.setVisible(true);
        }
    
    //관리자가 열람할 수 있는 회원의 주문 목록 GUI
    //관리자가 볼 수 있는 회원의 구매목록
    private void rootSetUpGUI() {
    	// build GUI
	   	frame = new JFrame(frameTitle);
	   	JButton returnButton = new JButton();
	   	frame.setLocationRelativeTo(null);
	   	try {
	   		Statement stmt = conn.createStatement();
	   	}
	   	catch (SQLException sqlex) {
    		System.out.println("SQL 에러 : " + sqlex.getMessage());
    		sqlex.printStackTrace();
    	}
	   	
	   	ImageIcon icon = new ImageIcon("src/mainImage02.PNG");
    	Image img = icon.getImage() ;

	   	// 주문 목록 전체를 보여주는 컨트롤 (왼쪽 상단 패널)
    	ImagePanel rightTopPanel = new ImagePanel(img);
	   	JLabel nameLabel = new JLabel("주문 내역");
	   	
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
	    ButtonGroup stockBook = new ButtonGroup();				// 라디오 버튼 그룹
	    stockBook.add(stock);
	    stockBook.add(notStock);
	    
	    //표식을 위한 라벨들
	    JLabel title = new JLabel("구 매 내 역");
	    JLabel bookName = new JLabel("책 제목");
	    JLabel bookWriter = new JLabel("저  자");
	    JLabel bookPrice = new JLabel("가  격");
	    JLabel bookStock = new JLabel("절판 여부");
	    JLabel bookDate = new JLabel("출 간 일");
	    JLabel orderDate = new JLabel("주 문 일");
	    
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

        // 왼쪽 하단 패널
	   	
	   	search = new JTextField(20);
        bSearch = new JButton("검색");
        bPrint = new JButton("출력");
        bPreview = new JButton("미리보기");
        rightTopPanel.add(search);
        rightTopPanel.add(bSearch);
        rightTopPanel.add(bPrint);
        rightTopPanel.add(bPreview);
        
        search.setBounds(80,220,180,20);
        bSearch.setBounds(50,250,60,30);
        bPrint.setBounds(120,250,60,30);
        bPreview.setBounds(190,250,90,30);
        
        
        bDelete = new JButton("삭제");
        returnButton = new JButton("돌아가기");
        rightTopPanel.add("tab", bDelete);
        rightTopPanel.add("tab",returnButton);
        bDelete.setBounds(450,250,60,30);
        returnButton.setBounds(520,250,90,30);

	   	// GUI 배치


        // ActionListener의 설정
		names.addListSelectionListener(nameList1);
		MySearchListener l = new MySearchListener();
		search.addActionListener(l);								// 텍스트 박스에서 리턴 눌러 색인 시작 할 때
        bSearch.addActionListener(l);								// 버튼으로 색인 시작할 때 동일한 핸들러 사용
        bDelete.addActionListener(new UserOrderDeleteButtonListener());
        bPrint.addActionListener(new DisplayButtonListener());
        bPreview.addActionListener(new DisplayButtonListener());
        returnButton.addActionListener(new RootReturnListener());
        
        // 클라이언드 프레임 창 조정
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(rightTopPanel);
        frame.setSize(700,350);
        frame.setVisible(true);
        }

    // DB를 연결하는 메소드
    private void dbConnectionInit() {
    	try {
    		Class.forName("com.mysql.jdbc.Driver");					// JDBC드라이버를 JVM영역으로 가져오기
    		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/proj2", "root", "mite");	// DB 연결하기
    	}
        catch (ClassNotFoundException cnfe) {
            System.out.println("JDBC 드라이버 클래스를 찾을 수 없습니다 : " + cnfe.getMessage());
        }
        catch (Exception ex) {
            System.out.println("DB 연결 에러 : " + ex.getMessage());
        }
	}
    // 구매목록 뿌려주는 메소드
    // DB에 있는 전체 레코드를 불러와서 리스트에 뿌려주는 메소
    public void prepareList() {
    	try {
    		Statement stmt = conn.createStatement();			// SQL 문을 작성을 위한  Statement 객체 생성

    		// 현재 DB에 있는 내용 추출해서 로그인한 회원의 구매 목록을 names 리스트에 출력하기
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
    		stmt.close();										// statement는 사용후 닫는 습관
    		Collections.sort(list,myComparator);								// 우선 정렬하자
    		names.setListData(list);							// names의 각종 속성은 그대로 두고 내용물만 바꾼다
    		if (!list.isEmpty())								// 리스트가 바뀌고 나면 항상 첫번째 아이텀을 가리키게 
    			names.setSelectedIndex(0);
    	} catch (SQLException sqlex) {
    		System.out.println("SQL 에러 : " + sqlex.getMessage());
    		sqlex.printStackTrace();
    	}
    }
    public void prepareList2() { //관리자 모드에서 뿌려주는 것
    	try {
    		Statement stmt = conn.createStatement();			// SQL 문을 작성을 위한  Statement 객체 생성

    		// 현재 DB에 있는 내용 추출해서 회원 목록을 names 리스트에 출력하기
    		ResultSet rs = stmt.executeQuery("SELECT * FROM member");
    		Vector<String> list = new Vector<String>();
    		while (rs.next()) {
    			list.add(rs.getString("name"));
    		}
    		stmt.close();										// statement는 사용후 닫는 습관
    		Collections.sort(list);								// 우선 정렬하자
    		names.setListData(list);							// names의 각종 속성은 그대로 두고 내용물만 바꾼다
    		if (!list.isEmpty())								// 리스트가 바뀌고 나면 항상 첫번째 아이텀을 가리키게 
    			names.setSelectedIndex(0);
    	} catch (SQLException sqlex) {
    		System.out.println("SQL 에러 : " + sqlex.getMessage());
    		sqlex.printStackTrace();
    	}
    }
    
    //	관리자가 구매목록 열람할때 뿌려주는 것
    public void prepareList3() {
    	try {
    		Statement stmt = conn.createStatement();			// SQL 문을 작성을 위한  Statement 객체 생성

    		// 현재 DB에 있는 내용 추출해서 로그인한 회원의 구매 목록을 names 리스트에 출력하기
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
    		stmt.close();										// statement는 사용후 닫는 습관
    		Collections.sort(list,myComparator);								// 우선 정렬하자
    		names.setListData(list);							// names의 각종 속성은 그대로 두고 내용물만 바꾼다
    		if (!list.isEmpty())								// 리스트가 바뀌고 나면 항상 첫번째 아이텀을 가리키게 
    			names.setSelectedIndex(0);
    	} catch (SQLException sqlex) {
    		System.out.println("SQL 에러 : " + sqlex.getMessage());
    		sqlex.printStackTrace();
    	}
    }

	// 리스트 박스에 액션이 일어나면 detail_id를 이용해 어떤 책을 구매했는지 뽑아주는 메소드
	public class NameListListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent lse) {					// 리스트의 선택이 바뀔때마다 호출
			if (!lse.getValueIsAdjusting() && !names.isSelectionEmpty()) {  // 현재 선택이 다 끝난 경우에 처리
		    	try {

		    		System.out.println("실행됨");
		    		Statement stmt = conn.createStatement();				// SQL 문장 만들기 위한 Statement 객체
		    		ResultSet rs = stmt.executeQuery("SELECT * FROM detail_order INNER JOIN book ON detail_order.book_id ="
		    				+ "book.book_id INNER JOIN ordered ON ordered.order_id = detail_order.order_id"
		    				+ " WHERE detail_id="+(String)names.getSelectedValue());
		    		if(rs.next()) {												// 여러개가 리턴되어도 첫번째 것으로 사용 
		    		name.setText(rs.getString("book.name"));			// DB에서 리턴 된 값을 가지고 택스트 박스 채움
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
		    		System.out.println("SQL 에러 : " + sqlex.getMessage());
		    		sqlex.printStackTrace();
		    	} catch (Exception ex) {
		    		System.out.println("DB Handling 에러(리스트 리스너) : " + ex.getMessage());
		    		ex.printStackTrace();
		    	}
			}
		}
	}
	// 회원들의 정보를 뽑아주기 위한 메소드
	public class NameListListener2 implements ListSelectionListener { //관리자 모드
		public void valueChanged(ListSelectionEvent lse) {					// 리스트의 선택이 바뀔때마다 호출
			if (!lse.getValueIsAdjusting() && !names.isSelectionEmpty()) {  // 현재 선택이 다 끝난 경우에 처리
		    	try {
		    		Statement stmt = conn.createStatement();				// SQL 문장 만들기 위한 Statement 객체
		    		ResultSet rs = stmt.executeQuery("SELECT * FROM member WHERE name = '" +
		    				(String)names.getSelectedValue() + "'");
		    		System.out.println("실행됨");
		    		if(rs.next()) {												// 여러개가 리턴되어도 첫번째 것으로 사용 
		    		mem_name.setText(rs.getString("name"));			// DB에서 리턴 된 값을 가지고 택스트 박스 채움
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
		    		System.out.println("SQL 에러 : " + sqlex.getMessage());
		    		sqlex.printStackTrace();
		    	} catch (Exception ex) {
		    		System.out.println("DB Handling 에러(리스트 리스너) : " + ex.getMessage());
		    		ex.printStackTrace();
		    	}
			}
		}
	}
	
	// 색인 컴포넌트의 리스너
	public class MySearchListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			int index = names.getNextMatch(search.getText().trim(), 0, Position.Bias.Forward);
			if (index != -1) {
				names.setSelectedIndex(index);
			}
			search.setText("");
		}
	}

	// 회원 정보 삭제 버튼의 리스너
	public class UserDeleteButtonListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			int choice;
			choice=JOptionPane.showConfirmDialog(frame, "정말 삭제하시겠습니까?", "회원 삭제"
					+ "", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if(choice == 0) {
				try {
					Statement stmt = conn.createStatement();				// SQL 문을 작성을 위한  Statement 객체 생성
					stmt.executeUpdate("DELETE FROM detail_order Using detail_order INNER JOIN ordered ON detail_order.order_id = "
							+ "ordered.order_id WHERE ordered.mem_id IN (SELECT mem_id FROM member WHERE name ="
							+ "'"+(String)names.getSelectedValue()+"')");
					stmt.executeUpdate("DELETE FROM ordered WHERE mem_id IN (SELECT mem_id FROM member WHERE name ="
							+ "'"+(String)names.getSelectedValue()+"')");
					stmt.executeUpdate("DELETE FROM member WHERE name = '" +
							(String)names.getSelectedValue() + "'");
					System.out.println("딜리트 실행됨");
					stmt.close();
					prepareList2();											// 리스트 박스 새 리스트로 다시 채움 
				} catch (SQLException sqlex) {
					System.out.println("SQL 에러 : " + sqlex.getMessage());
					sqlex.printStackTrace();
				} catch (Exception ex) {
					System.out.println("DB Handling 에러(DELETE 리스너) : " + ex.getMessage());
					ex.printStackTrace();
				}
			}
		}
	}
	//관리자가 유저의 구매목록을 지우는 리스너
	public class UserOrderDeleteButtonListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			int choice;
			choice=JOptionPane.showConfirmDialog(frame, "정말 삭제하시겠습니까?", "구매 삭제"
					+ "", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if(choice == 0) {
	    	try {
	    		Statement stmt = conn.createStatement();				// SQL 문을 작성을 위한  Statement 객체 생성
	    		stmt.executeUpdate("DELETE FROM ordered WHERE order_id IN (SELECT order_id FROM detail_order WHERE detail_id ="
	    			+ (String)names.getSelectedValue()+")");
	    		JOptionPane.showMessageDialog(null,"삭제되었습니다!");
	    		stmt.close();
	    		prepareList3();											// 리스트 박스 새 리스트로 다시 채움 
	    	} catch (SQLException sqlex) {
	    		System.out.println("SQL 에러 : " + sqlex.getMessage());
	    		sqlex.printStackTrace();
	    	} catch (Exception ex) {
	    		System.out.println("DB Handling 에러(DELETE 리스너) : " + ex.getMessage());
	    		ex.printStackTrace();
	    	}
			}
		}
	}
	// 관리자가 유저 정보 수정할때 쓰는 리스너
	// 관리자가 유저 정보 수정을 위한 버튼의 리스너
	public class UserSaveButtonListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
	    	try {
	    		Statement stmt = conn.createStatement();				// SQL 문을 작성을 위한  Statement 객체 생성
	    		stmt.executeUpdate("Update member SET name ='"+mem_name.getText()+"', mem_id = '"+mem_id.getText()+"',mem_psw ='"
	    				+ mem_psw.getText()+"',address ='"+mem_address.getText()+"',phone ='"+mem_phone.getText()+"',email ='"
	    						+ mem_email.getText()+"',zip_code ='"+mem_zipcode.getText()+"',birth='"+mem_birth.getText()+"'"
	    								+ " WHERE name ='"+(String)names.getSelectedValue()+"'");
	    		JOptionPane.showMessageDialog(null,"수정되었습니다!");
	    		stmt.close();
	    		prepareList2();											// 다시 뿌려 
	    	} catch (SQLException sqlex) {
	    		System.out.println("SQL 에러 : " + sqlex.getMessage());
	    		JOptionPane.showMessageDialog(null,"수정할 내용을 확인하세요!","Message",JOptionPane.ERROR_MESSAGE);
	    		sqlex.printStackTrace();
	    	} catch (Exception ex) {
	    		System.out.println("DB Handling 에러(SAVE 리스너) : " + ex.getMessage());
	    		ex.printStackTrace();
	    	}
		}
	}
	// 신규가입 창에서 가입을 누르면 실행되는 리스너
	// 신규가입 리스너
	public class UserMakeButtonListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			
	    	try {
	    		int nameCheck = 0;
	    		Statement stmt = conn.createStatement();				// SQL 문을 작성을 위한  Statement 객체 생성
	    		ResultSet rs = stmt.executeQuery("SELECT * FROM member");
	    		while(rs.next()) {
	    			if(mem_id.getText().equals(rs.getString("mem_id")))
	    				nameCheck = 1;
	    		}
	    		if(nameCheck == 0) {
	    		stmt.executeUpdate("INSERT INTO member (name, mem_id, mem_psw, address, phone,email,zip_code,birth) VALUES ('" +	// 새 레코드로 변경
	    				mem_name.getText().trim() + "', '" +
	    				mem_id.getText().trim() + "', '" +
	    				mem_psw.getText().trim() + "', '" +
	    				mem_address.getText().trim() + "', '" +
	    				mem_phone.getText().trim() + "', '" +
	    				mem_email.getText().trim() + "', '" +
	    				mem_zipcode.getText().trim() + "', '" +
	    				mem_birth.getText().trim() + "')");
	    		JOptionPane.showMessageDialog(null,"가입되었습니다!");
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
	    			JOptionPane.showMessageDialog(null,"아이디가 중복되었습니다!\n 다시입력하세요","Message",JOptionPane.ERROR_MESSAGE);
	    		}
	    		stmt.close();
	    	} catch (SQLException sqlex) {
	    		JOptionPane.showMessageDialog(null,"항목을 제대로 입력하세요!","Message",JOptionPane.ERROR_MESSAGE);
	    		System.out.println("SQL 에러 : " + sqlex.getMessage());
	    		sqlex.printStackTrace();
	    	} catch (Exception ex) {
	    		System.out.println("DB Handling 에러(SAVE 리스너) : " + ex.getMessage());
	    		ex.printStackTrace();
	    	}
			
		}
	}
	// 내용 지워주는 리스너
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
	//회원 가입할때 이용한다.
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
	//미리 보기 리스너
	// 출력을 위한 액션이 발생하면 처리하는 리스너 (print와 preview)
	public class DisplayButtonListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			// DB에서 가져오는 데이터를 rowObjects의 형태로 저장하고 이들의 리스트를 Printer 또는 Preview로 보내 줌
			ArrayList<RowObjects> rowList = new ArrayList<RowObjects>();	// 행들의 리스트
			RowObjects line;												// 하나의 행
			PrintObject word;												// 하나의 단어
			String myName = null;
			try {
	    		Statement stmt = conn.createStatement();					// SQL 문장 만들기 위한 Statement 객체
	    		ResultSet rs = stmt.executeQuery("SELECT * FROM detail_order INNER JOIN book ON detail_order.book_id="
	    				+ "book.book_id INNER JOIN ordered ON ordered.order_id = "
	    				+ "detail_order.order_id INNER JOIN member ON member.mem_id = "
	    				+ "ordered.mem_id WHERE member.mem_id ='"+key+"'");
	    		while(rs.next()) {
	    			line = new RowObjects();								// 5개의 단어가 1줄
	    			line.add(new PrintObject(rs.getString("book.name"), 25));
	    			line.add(new PrintObject(rs.getString("book.writer"), 15));
	    			line.add(new PrintObject(rs.getString("book.sale_price"), 10));
	    			line.add(new PrintObject(rs.getString("book.state"), 5));
	    			line.add(new PrintObject(rs.getString("book_data"), 10));
	    			line.add(new PrintObject(rs.getString("ordered.date"), 10));
	    			rowList.add(line);										// 출력해야 될 전체 리스트를 만듬									
	    		}
	    		rs = stmt.executeQuery("SELECT * FROM member WHERE mem_id ='"+key+"'");
	    		rs.next();
	    		myName = rs.getString("member.name");
	    		stmt.close();
	    		
	    		// 각 페이지의 칼럼 헤더를 위해 한 줄 만들음
	    		line = new RowObjects();									// 5개의 단어가 1줄
    			line.add(new PrintObject("책 제목", 25));
    			line.add(new PrintObject("저자", 15));
    			line.add(new PrintObject("가격", 10));
    			line.add(new PrintObject("절판", 5));
    			line.add(new PrintObject("출간일", 10));
    			line.add(new PrintObject("구매일", 10));

    			if (e.getSource() == bPrint) {
	    			Printer prt = new Printer(new PrintObject(myName+"의 책 구매 목록", 20), line, rowList, true);
	    			prt.print();
    			}
    			else {
	    			Preview prv = new Preview(new PrintObject(myName+"의 책 구매 목록", 20), line, rowList, true);
	    			prv.preview();
    			}
    				
			} catch (SQLException sqlex) {
	    		System.out.println("SQL 에러 : " + sqlex.getMessage());
	    		sqlex.printStackTrace();
	    	} catch (Exception ex) {
	    		System.out.println("DB Handling 에러(리스트 리스너) : " + ex.getMessage());
	    		ex.printStackTrace();
	    	}
		}
	}
	//로그인 버튼 리스너
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
				JOptionPane.showMessageDialog(null,"아이디나 비밀번호를 확인하세요!","Message",JOptionPane.ERROR_MESSAGE);
		}
		
	}
	//신규 가입 GUI 열어주는 리스너
	public class NewUserButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			names.removeListSelectionListener(nameList1);
			names.removeListSelectionListener(nameList2);
			newGUI();
		}
	}
	//메인화면에서 로그인한 유저가 자신의 구매목록을 볼 수 있는 GUI를 열어주는 리스너
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
	//root창에서 선택한 사람의 주문 목록을 보여주는 리스너
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
	//로그아웃을 누르면 다시 로그인 GUI를 열어주는 리스너
	public class LogoutButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			names.removeListSelectionListener(nameList1);
			names.removeListSelectionListener(nameList2);
			loginGUI();
		}
	}
	
	// 다시 관리자 GUI로 넘어오게 해주는 리스너
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
	// 메인화면으로 돌아가는 리스너
	public class MainReturnListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			names.removeListSelectionListener(nameList1);
			names.removeListSelectionListener(nameList2);
			mainGUI();
		}
	}
	// 로그인화면으로 돌아가는 리스너
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
	
	// 책 구매 리스너
	public class BookBuyListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			int choice;
			choice=JOptionPane.showConfirmDialog(frame, "정말 구매하시겠습니까?", "구매 확인"
					+ "", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if(choice == 0) {
			try {
	    		Statement stmt = conn.createStatement();				// SQL 문을 작성을 위한  Statement 객체 생성
	    		ResultSet rs;
	    		String orderId = null;
	    		String memId = null;
	    		rs = stmt.executeQuery("SELECT * FROM member WHERE mem_id ='"+key+"'");
	    		if(rs.next()) {
	    			memId = rs.getString("mem_id");
	    		}
	    		System.out.println(memId);
	    		stmt.executeUpdate("INSERT INTO ordered (mem_id,date) VALUES ('" +	// 새 레코드로 변경
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
	    		JOptionPane.showMessageDialog(null,"구매되었습니다!");
	    		prepareList();
	    	} catch (SQLException sqlex) {
	    		System.out.println("SQL 에러 : " + sqlex.getMessage());
	    		sqlex.printStackTrace();
	    	} catch (Exception ex) {
	    		System.out.println("DB Handling 에러(SAVE 리스너) : " + ex.getMessage());
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
