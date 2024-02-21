package question_generator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class QuizSelector extends JFrame {

	private JPanel contentPane;
	private ButtonGroup G;
	
	private JLabel lblQuestion;
	
	private JRadioButton rbOptionOne;
	private JRadioButton rbOptionTwo;
	private JRadioButton rbOptionThree;
	private JRadioButton rbOptionFour;
	
	private JButton btnGenerate;
	private JButton btnSubmit;
	private JLabel lblCheck;
	
	private JComboBox cmbTopic;
	private JComboBox cmbSubject;

	private int currQuestion = 0;
	boolean answered;
	
	private HashMap<Integer, String> subjects;
	private HashMap<Integer, String> topics;
	private JButton btnAnalyse;
	private JButton btnEditQuestions;
	
	public static Color LIGHT_BLUE = new Color(192, 234, 252);
	public static Color DARK_BLUE = new Color(1, 21, 61);
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					QuizSelector frame = new QuizSelector();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public QuizSelector() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 676, 475);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setBackground(new Color(1, 24, 61));
		
		cmbSubject = new JComboBox();
		cmbSubject.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				setTopics();
			}
		});
		cmbSubject.setBounds(20, 51, 228, 27);
		contentPane.add(cmbSubject);
		
		JLabel lblSubject = new JLabel("Subject");
		lblSubject.setForeground(Color.WHITE);
		lblSubject.setBounds(20, 23, 61, 16);
		contentPane.add(lblSubject);
		
		cmbTopic = new JComboBox();
		cmbTopic.setBounds(20, 132, 228, 27);
		contentPane.add(cmbTopic);
		
		JLabel lblTopic = new JLabel("Topic");
		lblTopic.setForeground(Color.WHITE);
		lblTopic.setBounds(24, 104, 47, 16);
		contentPane.add(lblTopic);
		
		btnGenerate = new JButton("New Question");
		btnGenerate.setForeground(Color.WHITE);
		btnGenerate.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				generateQuestion();
			}
		});
		btnGenerate.setBorder(new LineBorder(Color.WHITE));
		btnGenerate.setBounds(20, 189, 172, 40);
		contentPane.add(btnGenerate);
		
		lblQuestion = new JLabel("Question");
		lblQuestion.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		lblQuestion.setForeground(Color.WHITE);
		lblQuestion.setBounds(302, 23, 347, 40);
		contentPane.add(lblQuestion);
		
		rbOptionOne = new JRadioButton("Option 1");
		rbOptionOne.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		rbOptionOne.setForeground(Color.WHITE);
		rbOptionOne.setBounds(302, 75, 347, 46);
		contentPane.add(rbOptionOne);
		
		rbOptionTwo = new JRadioButton("Option 2");
		rbOptionTwo.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		rbOptionTwo.setForeground(Color.WHITE);
		rbOptionTwo.setBounds(302, 133, 347, 43);
		contentPane.add(rbOptionTwo);
		
		rbOptionFour = new JRadioButton("Option 4");
		rbOptionFour.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		rbOptionFour.setForeground(Color.WHITE);
		rbOptionFour.setBounds(302, 240, 347, 56);
		contentPane.add(rbOptionFour);
		
		rbOptionThree = new JRadioButton("Option 3");
		rbOptionThree.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		rbOptionThree.setForeground(Color.WHITE);
		rbOptionThree.setBounds(302, 188, 347, 46);
		contentPane.add(rbOptionThree);
		
		G = new ButtonGroup();
		G.add(rbOptionOne);
		G.add(rbOptionTwo);
		G.add(rbOptionThree);
		G.add(rbOptionFour);
		
		btnSubmit = new JButton("Submit");
		btnSubmit.setForeground(Color.WHITE);
		btnSubmit.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				checkAnswer();
			}
		});
		btnSubmit.setBounds(302, 317, 117, 35);
		btnSubmit.setBorder(new LineBorder(Color.WHITE));
		contentPane.add(btnSubmit);
		
		lblCheck = new JLabel("Check");
		lblCheck.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		lblCheck.setForeground(Color.WHITE);
		lblCheck.setBounds(302, 364, 347, 80);
		contentPane.add(lblCheck);
		
		btnAnalyse = new JButton("Analyse");
		btnAnalyse.setForeground(Color.WHITE);
		btnAnalyse.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				openFilter();
			}
		});
		btnAnalyse.setBorder(new LineBorder(Color.WHITE));
		btnAnalyse.setBounds(20, 297, 172, 40);
		contentPane.add(btnAnalyse);
		
		
		btnEditQuestions = new JButton("Edit Questions");
		btnEditQuestions.setForeground(Color.WHITE);
		btnEditQuestions.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				try {
					openEditor();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnEditQuestions.setBorder(new LineBorder(Color.WHITE));
		btnEditQuestions.setBounds(20, 354, 172, 40);
		contentPane.add(btnEditQuestions);
		
		lblQuestion.setVisible(false);
		rbOptionOne.setVisible(false);
		rbOptionTwo.setVisible(false);
		rbOptionThree.setVisible(false);
		rbOptionFour.setVisible(false);
		btnSubmit.setVisible(false);
		lblCheck.setVisible(false);
		
		cmbSubject.setUI(new BasicComboBoxUI() 
        {
            @Override
            protected JButton createArrowButton() {
                BasicArrowButton arrowButton = new BasicArrowButton(BasicArrowButton.SOUTH, Color.decode("#044aba"), null, LIGHT_BLUE, null);
                return arrowButton;
            }
        });
		
		cmbTopic.setUI(new BasicComboBoxUI() 
        {
            @Override
            protected JButton createArrowButton() {
                BasicArrowButton arrowButton = new BasicArrowButton(BasicArrowButton.SOUTH, Color.decode("#044aba"), null, LIGHT_BLUE, null);
                return arrowButton;
            }
        });
		
		subjects = new HashMap<Integer, String>();
		topics = new HashMap<Integer, String>();
		setSubjects();
		
		answered = true;
		
		generateQuestion();
		
		setLocationRelativeTo(null);
	}
	
	private void checkAnswer()
	{
		if (!answered)
		{
			if (G.isSelected(null))
			{
				lblCheck.setText("Please select an answer.");
				lblCheck.setForeground(Color.RED);
				lblCheck.setVisible(true);
			}
			else
			{
				Connection conn = null;
				
				try
				{
					conn = DatabaseManager.getConnection();
					
					PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM questions WHERE Question_ID = " + currQuestion);
					ResultSet rs = pstmt.executeQuery();
					
					int correctAnswer = rs.getInt(10);
					String correctAnswerString = "";
					
					int answer = 0;
					
					if (correctAnswer == 1)
					{
						correctAnswerString = rs.getString(6);
					}
					else if (correctAnswer == 2)
					{
						correctAnswerString = rs.getString(7);
					}
					else if (correctAnswer == 3)
					{
						correctAnswerString = rs.getString(8);
					}
					else if (correctAnswer == 4)
					{
						correctAnswerString = rs.getString(9);
					}
					
					if (G.getSelection().equals(rbOptionOne.getModel()))
					{
						answer = 1;
					}
					else if (G.getSelection().equals(rbOptionTwo.getModel()))
					{
						answer = 2;
					}
					else if (G.getSelection().equals(rbOptionThree.getModel()))
					{
						answer = 3;
					}
					else if (G.getSelection().equals(rbOptionFour.getModel()))
					{
						answer = 4;
					}
					
					int numberCorrect = rs.getInt(4);
					int totalNumber = rs.getInt(5);
					
					int newTotal = totalNumber + 1;
					int newNumberCorrect = numberCorrect;
					
					if (correctAnswer == answer)
					{
						lblCheck.setText("Congratulations! Your Answer is Correct.");
						lblCheck.setForeground(Color.GREEN);
						lblCheck.setVisible(true);
						System.out.println("Correct Answer");
						
						newNumberCorrect = numberCorrect + 1;
					}
					else
					{
						lblCheck.setText("<html> Your Answer is Wrong. The Correct Answer is \"" + correctAnswerString + "\". </html>");
						lblCheck.setForeground(Color.RED);
						lblCheck.setVisible(true);
						System.out.println("Wrong Answer");
					}
					
					System.out.println("Correct: " + numberCorrect);
					System.out.println("Total: " + totalNumber);
					System.out.println("New Correct: " + newNumberCorrect);
					System.out.println("New Total: " + newTotal);
					
					pstmt.close();
					
					System.out.println("UPDATE questions SET Number_Correct = " + newNumberCorrect + ", Total_Number = " + newTotal + " WHERE Question_ID = " + currQuestion);
					
					PreparedStatement pstmtNumber = conn.prepareStatement("UPDATE questions SET Number_Correct = " + newNumberCorrect + ", Total_Number = " + newTotal + " WHERE Question_ID = " + currQuestion);
					pstmtNumber.execute();
					pstmtNumber.close();
					
					System.out.println(answer);
					
					btnSubmit.setVisible(false);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	private void generateQuestion()
	{
		Connection conn = null;
		
		try
		{
			conn = DatabaseManager.getConnection();
			
			HashMap<Integer, Integer> questions = new HashMap<Integer, Integer>();
			
			PreparedStatement pstmtCountQuestions = conn.prepareStatement("SELECT Count(Question_ID) FROM questions");
			int noQuestions = pstmtCountQuestions.executeQuery().getInt(1);
			
			int noInts = 0;
			int numberQuestions = 0;
			
			PreparedStatement pstmtQuestions = conn.prepareStatement("SELECT * FROM questions");
			ResultSet rsQuestions = pstmtQuestions.executeQuery();
			
			String topic = cmbTopic.getSelectedItem().toString();
			String subject = cmbSubject.getSelectedItem().toString();
			
			while (rsQuestions.next())
			{	
				int currTopicID = rsQuestions.getInt(3);
				String currTopic = conn.prepareStatement("SELECT Topic_Name FROM topics WHERE Topic_ID = " + currTopicID).executeQuery().getString(1);
				
				int currSubjectID = conn.prepareStatement("SELECT Subject_ID FROM topics WHERE Topic_ID = " + currTopicID).executeQuery().getInt(1);
				String currSubject = conn.prepareStatement("SELECT Subject_Name FROM subjects WHERE Subject_ID = " + currSubjectID).executeQuery().getString(1);
				
				if (subject.equals("Any") || (topic.equals("Any") && currSubject.equals(subject)) || currTopic.equals(topic))
				{
					int sum = 0;
					
					int noTotal = rsQuestions.getInt(5);
					int noCorrect = rsQuestions.getInt(4);
					
					if (noTotal == 0)
					{
						sum = 100;
					}
					else
					{
						sum = (int) Math.round((((double)noTotal - (double)noCorrect)/(double)noTotal) * 100);
					}
					
					if (sum == 0)
					{
						sum = 1;
					}
					
					for (int i = noInts; i < noInts + sum; i++)
					{
						questions.put(i, rsQuestions.getInt(1));
					}
					
					System.out.println(rsQuestions.getString(2) + ": " + sum);
					
					noInts += sum;
					numberQuestions++;
				}
			}
			
			System.out.println("Total: " + noInts + ", Number: " + numberQuestions);
			
			int random = (int) Math.round(Math.random() * (double) noInts);
			
			System.out.println("Random: " + random);
			
			if (random < 0)
			{
				random = 0;
			}
			else if (random > noInts - 1)
			{
				random = noInts - 1;
			}
			
			int questionID = questions.get(random);
			
			if (questionID == currQuestion && numberQuestions > 1)
			{
				generateQuestion();
				return;
			}
			
			PreparedStatement pstmtQuestion = conn.prepareStatement("SELECT * FROM questions WHERE Question_ID = " + questionID);
			ResultSet rsQuestion = pstmtQuestion.executeQuery();
			
			lblQuestion.setText("<html>" + rsQuestion.getString(2) + "</html>");
			rbOptionOne.setText("<html>" + rsQuestion.getString(6) + "</html>");
			rbOptionTwo.setText("<html>" + rsQuestion.getString(7) + "</html>");
			rbOptionThree.setText("<html>" + rsQuestion.getString(8) + "</html>");
			rbOptionFour.setText("<html>" + rsQuestion.getString(9) + "</html>");
			
			lblQuestion.setVisible(true);
			rbOptionOne.setVisible(true);
			rbOptionTwo.setVisible(true);
			rbOptionThree.setVisible(true);
			rbOptionFour.setVisible(true);
			btnSubmit.setVisible(true);
			
			lblCheck.setVisible(false);
			
			System.out.println(rsQuestion.getString(2));
			
			answered = false;
			
			btnSubmit.setVisible(true);
			
			currQuestion = questionID;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void setSubjects()
	{
		Connection conn = null;
		
		try
		{
			conn = DatabaseManager.getConnection();
			
			String getSubjectsSQL = "SELECT Subject_ID, Subject_Name FROM subjects";
			PreparedStatement pstmtGetSubjects = conn.prepareStatement(getSubjectsSQL);
			ResultSet rsSubjects = pstmtGetSubjects.executeQuery();
			
			if (subjects.size() > 0)
			{
				subjects.clear();
			}
			
			while(rsSubjects.next())
			{                            
				subjects.put(rsSubjects.getInt(1), rsSubjects.getString(2));
		    } 
			
			DefaultComboBoxModel dcmb = new DefaultComboBoxModel();
			dcmb.addElement("Any");
			for (int key : subjects.keySet())
			{
				dcmb.addElement(subjects.get(key));
			}
			cmbSubject.setModel(dcmb);
			
			cmbSubject.setSelectedIndex(0);
			setTopics();
			
			pstmtGetSubjects.close();
			//conn.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void setTopics()
	{
		Connection conn = null;
		
		try
		{
			conn = DatabaseManager.getConnection();
			
			String getTopicsSQL = "SELECT Topic_ID, Topic_Name FROM topics JOIN subjects ON (topics.Subject_ID=subjects.Subject_ID) where Subject_Name = \"" + cmbSubject.getSelectedItem().toString() + "\"";
			
			System.out.println(getTopicsSQL);
			
			PreparedStatement pstmtGetTopics = conn.prepareStatement(getTopicsSQL);
			ResultSet rsTopics = pstmtGetTopics.executeQuery();
			
			if (topics.size() > 0)
			{
				topics.clear();
			}
			
			while(rsTopics.next())
			{                            
				topics.put(rsTopics.getInt(1), rsTopics.getString(2));
				
				System.out.println(rsTopics.getInt(1) + ": " + rsTopics.getString(2));
		    } 
			
			DefaultComboBoxModel dcmb = new DefaultComboBoxModel();
			dcmb.addElement("Any");
			for (int key : topics.keySet())
			{
				dcmb.addElement(topics.get(key));
			}
			cmbTopic.setModel(dcmb);
			
			pstmtGetTopics.close();
			//conn.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void openEditor() throws IOException
	{
		QuestionsEditor qe = new QuestionsEditor(this);
		this.setVisible(false);
		qe.setVisible(true);
	}
	
	private void openFilter()
	{
		Filter filter = new Filter(this);
		this.setVisible(false);
		filter.setVisible(true);
	}
	
	public void setVisible (boolean visible)
	{
		super.setVisible(visible);
		
		if (visible == true)
		{
			setSubjects();
			generateQuestion();
		}
	}
}
