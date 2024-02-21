package question_generator;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.beans.PropertyChangeEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;

public class QuestionEditorWindow extends JFrame {

	private JPanel contentPane;
	private JTextField txtQuestionName;
	private JTextField textOption1;
	private JTextField textOption2;
	private JTextField textOption3;
	private JTextField textOption4;
	private JComboBox cmbTopic;
	private JComboBox cmbSubject;
	private JComboBox cmbCorrectOption;
	private JButton btnCancel;
	private JButton btnSave;
	private JLabel lblError;
	
	private QuestionsEditor parent;
	private int id;
	
	private HashMap<Integer, String> topics;
	private HashMap<Integer, String> subjects;
	
	private boolean isNew;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					QuestionEditorWindow frame = new QuestionEditorWindow();
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
	public QuestionEditorWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 477, 499);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setBackground(QuizSelector.DARK_BLUE);
		
		JLabel lblQuestionName = new JLabel("Question");
		lblQuestionName.setForeground(Color.WHITE);
		lblQuestionName.setBounds(61, 9, 61, 21);
		contentPane.add(lblQuestionName);
		
		txtQuestionName = new JTextField();
		txtQuestionName.setBounds(125, 4, 346, 26);
		contentPane.add(txtQuestionName);
		txtQuestionName.setColumns(10);
		
		JLabel lblTopic = new JLabel("Topic");
		lblTopic.setForeground(Color.WHITE);
		lblTopic.setBounds(84, 39, 38, 16);
		contentPane.add(lblTopic);
		
		cmbTopic = new JComboBox();
		cmbTopic.setBounds(125, 33, 346, 27);
		cmbTopic.setEditable(true);
		contentPane.add(cmbTopic);
		
		JLabel lblSubject = new JLabel("Subject");
		lblSubject.setForeground(Color.WHITE);
		lblSubject.setBounds(74, 71, 61, 16);
		contentPane.add(lblSubject);
		
		cmbSubject = new JComboBox();
		cmbSubject.setBounds(125, 65, 346, 27);
		cmbSubject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				setTopics();
				System.out.println("HERE 2");
			}
		});
		cmbSubject.setEditable(true);
		contentPane.add(cmbSubject);
		
		JLabel lblOption1 = new JLabel("Option 1");
		lblOption1.setForeground(Color.WHITE);
		lblOption1.setBounds(61, 123, 61, 21);
		contentPane.add(lblOption1);
		
		textOption1 = new JTextField();
		textOption1.setBounds(125, 118, 346, 26);
		textOption1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) 
			{
				setCorrectOptions();
			}
		});
		textOption1.setColumns(10);
		contentPane.add(textOption1);
		
		JLabel lblOption2 = new JLabel("Option 2");
		lblOption2.setForeground(Color.WHITE);
		lblOption2.setBounds(61, 150, 61, 21);
		contentPane.add(lblOption2);
		
		textOption2 = new JTextField();
		textOption2.setBounds(125, 145, 346, 26);
		textOption2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) 
			{
				setCorrectOptions();
			}
		});
		textOption2.setColumns(10);
		contentPane.add(textOption2);
		
		textOption3 = new JTextField();
		textOption3.setBounds(125, 175, 346, 26);
		textOption3.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) 
			{
				setCorrectOptions();
			}
		});
		textOption3.setColumns(10);
		contentPane.add(textOption3);
		
		JLabel lblOption3 = new JLabel("Option 3");
		lblOption3.setForeground(Color.WHITE);
		lblOption3.setBounds(61, 180, 61, 21);
		contentPane.add(lblOption3);
		
		textOption4 = new JTextField();
		textOption4.setBounds(125, 209, 346, 26);
		textOption4.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) 
			{
				setCorrectOptions();
			}
		});
		textOption4.setColumns(10);
		contentPane.add(textOption4);
		
		JLabel lblOption4 = new JLabel("Option 4");
		lblOption4.setForeground(Color.WHITE);
		lblOption4.setBounds(61, 214, 61, 21);
		contentPane.add(lblOption4);
		
		cmbCorrectOption = new JComboBox();
		cmbCorrectOption.setBounds(125, 266, 346, 27);
		contentPane.add(cmbCorrectOption);
		
		JLabel lblCorrectOption = new JLabel("Correct Option");
		lblCorrectOption.setForeground(Color.WHITE);
		lblCorrectOption.setBounds(25, 271, 98, 16);
		contentPane.add(lblCorrectOption);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setForeground(Color.WHITE);
		btnCancel.setBounds(373, 436, 98, 29);
		btnCancel.setBorder(new LineBorder(Color.WHITE));
		btnCancel.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				cancel();
			}
		});
		contentPane.add(btnCancel);
		
		btnSave = new JButton("Save");
		btnSave.setForeground(Color.WHITE);
		btnSave.setBounds(263, 436, 98, 29);
		btnSave.setBorder(new LineBorder(Color.WHITE));
		btnSave.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				save();
			}
		});
		contentPane.add(btnSave);
		
		lblError = new JLabel("New label");
		lblError.setForeground(Color.RED);
		lblError.setBounds(25, 334, 446, 65);
		contentPane.add(lblError);
		lblError.setVisible(false);
		
		id = -1;
		
		setLocationRelativeTo(null);
		
		cmbSubject.setUI(new BasicComboBoxUI() 
        {
            @Override
            protected JButton createArrowButton() {
                BasicArrowButton arrowButton = new BasicArrowButton(BasicArrowButton.SOUTH, Color.decode("#044aba"), null, QuizSelector.LIGHT_BLUE, null);
                return arrowButton;
            }
        });
		
		cmbTopic.setUI(new BasicComboBoxUI() 
        {
            @Override
            protected JButton createArrowButton() {
                BasicArrowButton arrowButton = new BasicArrowButton(BasicArrowButton.SOUTH, Color.decode("#044aba"), null, QuizSelector.LIGHT_BLUE, null);
                return arrowButton;
            }
        });
		
		cmbCorrectOption.setUI(new BasicComboBoxUI() 
        {
            @Override
            protected JButton createArrowButton() {
                BasicArrowButton arrowButton = new BasicArrowButton(BasicArrowButton.SOUTH, Color.decode("#044aba"), null, QuizSelector.LIGHT_BLUE, null);
                return arrowButton;
            }
        });
	}
	
	public QuestionEditorWindow (QuestionsEditor parent, int ID)
	{
		this();
		this.parent = parent;
		this.id = ID;
		
		isNew = false;
		
		if (ID != -1)
		{
			Connection conn = null;
			
			try
			{
				topics = new HashMap<Integer, String>();
				
				conn = DatabaseManager.getConnection();
				String SQL = "SELECT * FROM questions where Question_ID = " + id;
				PreparedStatement pstmt = conn.prepareStatement(SQL);
				ResultSet rs = pstmt.executeQuery();
				String name = rs.getString(2);
				String topic = conn.prepareStatement("SELECT Topic_Name FROM topics WHERE Topic_ID = " + rs.getInt(3)).executeQuery().getString(1);
				String subject = conn.prepareStatement("SELECT Subject_Name FROM subjects WHERE Subject_ID = " + conn.prepareStatement("SELECT Subject_ID FROM topics WHERE Topic_ID = " + rs.getInt(3)).executeQuery().getInt(1)).executeQuery().getString(1);
				String option1 = rs.getString(6);
				String option2 = rs.getString(7);
				String option3 = rs.getString(8);
				String option4 = rs.getString(9);
				int correctOption = rs.getInt(10) - 1;
				
				txtQuestionName.setText(name);
				textOption1.setText(option1);
				textOption2.setText(option2);
				textOption3.setText(option3);
				textOption4.setText(option4);
				
				setCorrectOptions();
				cmbCorrectOption.setSelectedIndex(correctOption);
				
				subjects = new HashMap<Integer, String>();
				setSubjects();
				
				setTopics();
				
				cmbSubject.setSelectedItem(subject);
				cmbTopic.setSelectedItem(topic);
				
				
				pstmt.close();
				conn.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public QuestionEditorWindow(QuestionsEditor parent)
	{
		this();
		this.parent = parent;
		isNew = true;
		
		subjects = new HashMap<Integer, String>();
		topics = new HashMap<Integer, String>();
		setSubjects();
		cmbSubject.setSelectedIndex(0);
		setTopics();
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
			for (int key : subjects.keySet())
			{
				dcmb.addElement(subjects.get(key));
			}
			cmbSubject.setModel(dcmb);
			
			pstmtGetSubjects.close();
			conn.close();
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
			PreparedStatement pstmtGetTopics = conn.prepareStatement(getTopicsSQL);
			ResultSet rsTopics = pstmtGetTopics.executeQuery();
			
			if (topics.size() > 0)
			{
				topics.clear();
			}
			
			while(rsTopics.next())
			{                            
				topics.put(rsTopics.getInt(1), rsTopics.getString(2));
		    } 
			
			DefaultComboBoxModel dcmb = new DefaultComboBoxModel();
			for (int key : topics.keySet())
			{
				dcmb.addElement(topics.get(key));
			}
			cmbTopic.setModel(dcmb);
			
			pstmtGetTopics.close();
			conn.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void setCorrectOptions()
	{
		DefaultComboBoxModel dcbm = new  DefaultComboBoxModel();
		
		if (textOption1.getText().length() > 0)
		{
			dcbm.addElement(textOption1.getText());
		}
		
		if (textOption2.getText().length() > 0)
		{
			dcbm.addElement(textOption2.getText());
		}
		
		if (textOption3.getText().length() > 0)
		{
			dcbm.addElement(textOption3.getText());
		}
		
		if (textOption4.getText().length() > 0)
		{
			dcbm.addElement(textOption4.getText());
		}
		
		cmbCorrectOption.setModel(dcbm);
	}
	
	private void cancel()
	{
		this.setVisible(false);
		parent.setVisible(true);
	}
	
	private void save()
	{
		if (isNoError())
		{
			Connection conn = null;
			
			try
			{
				conn = DatabaseManager.getConnection();
				
				String name = txtQuestionName.getText();
				String subject= cmbSubject.getSelectedItem().toString();
				String Topic = cmbTopic.getSelectedItem().toString();
				String OptionOne = textOption1.getText();
				String OptionTwo = textOption2.getText();
				String OptionThree = textOption3.getText();
				String OptionFour = textOption4.getText();
				int correctOption = cmbCorrectOption.getSelectedIndex() + 1;
				
				int topicID = conn.prepareStatement("SELECT Topic_ID FROM topics WHERE Topic_Name = \"" + Topic + "\"").executeQuery().getInt(1);
				int subjectID = conn.prepareStatement("SELECT Subject_ID FROM subjects WHERE Subject_Name = \"" + subject + "\"").executeQuery().getInt(1);
				
				PreparedStatement pstmtSubCount = conn.prepareStatement("SELECT COUNT(Subject_ID) FROM subjects WHERE Subject_Name = \"" + subject + "\"");
				ResultSet rsSub = pstmtSubCount.executeQuery();
				int noSubs = rsSub.getInt(1);
				pstmtSubCount.close();
				
				PreparedStatement pstmtTopicCount = conn.prepareStatement("SELECT COUNT(Topic_ID) FROM topics WHERE Topic_Name = \"" + Topic + "\"");
				ResultSet rsTopic = pstmtTopicCount.executeQuery();
				int noTopics = rsTopic.getInt(1);
				pstmtTopicCount.close();
				
				if (noSubs == 0)
				{
					PreparedStatement pstmtAddSub = conn.prepareStatement("INSERT INTO subjects (Subject_Name) VALUES (\"" + subject + "\")");
					pstmtAddSub.execute();
					pstmtAddSub.close();
					
					PreparedStatement pstmt1 = conn.prepareStatement("SELECT Subject_ID FROM subjects WHERE Subject_Name = \"" + subject + "\"");
					subjectID = pstmt1.executeQuery().getInt(1);
					pstmt1.close();
				}
				
				if (noTopics == 0)
				{
					PreparedStatement pstmtAddTopic = conn.prepareStatement("INSERT INTO topics (Topic_Name, Subject_ID) VALUES (\"" + Topic + "\", " + subjectID + ")");
					pstmtAddTopic.execute();
					pstmtAddTopic.close();
					
					PreparedStatement pstmt1 = conn.prepareStatement("SELECT Topic_ID FROM topics WHERE Topic_Name = \"" + Topic + "\"");
					topicID = pstmt1.executeQuery().getInt(1);
					pstmt1.close();
				}
				
				String SQL = "";
				
				if (isNew)
				{
					SQL = "INSERT INTO questions (Question_Name, Topic_ID, Number_Correct, Total_Number, Option_One, Option_Two, Option_Three, Option_Four, Correct_Option) VALUES ('" + name + "', " + topicID +  ", 0, 0, '" + OptionOne + "', '" + OptionTwo + "', '" + OptionThree + "', '" + OptionFour + "', " + correctOption + ")";
				}
				else
				{
					SQL = "UPDATE questions SET Question_Name = \"" + name + "\", Topic_ID = " + topicID +  ", Number_Correct = 0, Total_Number = 0, Option_One = \"" + OptionOne + "\", Option_Two = \"" + OptionTwo + "\", Option_Three = \"" + OptionThree + "\", Option_Four = \"" + OptionFour + "\", Correct_Option = " + correctOption + " WHERE Question_ID = " + id;
				} 
				
				System.out.println(SQL);
				
				PreparedStatement pstmt = conn.prepareStatement(SQL);
				pstmt.execute();
				
				pstmt.close();
				
				conn.close();
				
				checkSubjectsAndTopics();
				
				this.setVisible(false);
				parent.setVisible(true);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private boolean isNoError()
	{
		int noErrors = 0;
		
		String errorString = "<html> Please add ";
		
		boolean isSameTopic = false;
		
		ArrayList<String> errors = new ArrayList<String>();
		
		if (txtQuestionName.getText().length() == 0)
		{	
			errors.add("the Question");
			
			noErrors++;
		}
		
		if (cmbSubject.getSelectedItem() == null || cmbSubject.getSelectedItem().toString().length() == 0)
		{			
			errors.add("the Subject");
			
			noErrors++;
		}
		
		if (cmbTopic.getSelectedItem() == null || cmbTopic.getSelectedItem().toString().length() == 0)
		{
			errors.add("the Topic");
			
			noErrors++;
		}
		else if (cmbSubject.getSelectedItem() != null || cmbSubject.getSelectedItem().toString().length() != 0)
		{
			Connection conn = null;
			
			try
			{
				conn = DatabaseManager.getConnection();
				
				String topic = cmbTopic.getSelectedItem().toString();
				String subject = cmbSubject.getSelectedItem().toString();
				
				int noTopics = conn.prepareStatement("SELECT COUNT(Topic_ID) FROM topics WHERE Topic_Name = \"" + topic + "\"").executeQuery().getInt(1);
				
				if (noTopics > 0)
				{
					String subjectInTopic = conn.prepareStatement("SELECT Subject_Name FROM topics JOIN subjects ON (topics.Subject_ID=subjects.Subject_ID) WHERE Topic_Name = \"" + topic + "\"").executeQuery().getString(1);
					
					if (!subjectInTopic.equals(subject))
					{
						isSameTopic = true;
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		if (textOption1.getText().length() == 0)
		{
			errors.add("Option 1");
			
			noErrors++;
		}
		
		if (textOption2.getText().length() == 0)
		{
			errors.add("Option 2");
			
			noErrors++;
		}
		
		if (textOption3.getText().length() == 0)
		{
			errors.add("Option 3");
			
			noErrors++;
		}
		
		if (textOption4.getText().length() == 0)
		{
			errors.add("Option 4");
			
			noErrors++;
		}
		
		for (int i = 0; i < errors.size(); i++)
		{
			if (i == errors.size() - 2)
			{
				errorString += errors.get(i) + ", and ";
			}
			else if (i == errors.size() - 1)
			{
				errorString += errors.get(i);
			}
			else
			{
				errorString += errors.get(i) + ", ";
			}
		}
		
		errorString += ".";
		
		if (isSameTopic)
		{
			if (noErrors == 0)
			{
				errorString = "<html> Please use a Unique Topic.";
			}
			else
			{
				errorString += " Please also use a Unique Topic.";
			}
			
			noErrors++;
		}
		
		lblError.setText(errorString += " </html>");
		
		
		if (noErrors == 0)
		{
			lblError.setVisible(false);
			return true;
		}
		else
		{
			lblError.setVisible(true);
			return false;
		}
	}
	
	public static void checkSubjectsAndTopics()
	{
		Connection conn = null;
		
		try
		{
			conn = DatabaseManager.getConnection();
			
			PreparedStatement pstmtTopics = conn.prepareStatement("SELECT * FROM topics");
			ResultSet rsTopics = pstmtTopics.executeQuery();
			
			while (rsTopics.next())
			{
				int topicID = rsTopics.getInt(1);
				
				PreparedStatement pstmtTopicCount = conn.prepareStatement("SELECT COUNT(Question_ID) FROM questions WHERE Topic_ID = " + topicID);
				ResultSet rsTopic = pstmtTopicCount.executeQuery();
				int noTopicQuestions = rsTopic.getInt(1);
				pstmtTopicCount.close();
				
				if (noTopicQuestions == 0)
				{
					PreparedStatement pstmtDelete = conn.prepareStatement("DELETE FROM topics WHERE Topic_ID = " + topicID);
					pstmtDelete.execute();
					pstmtDelete.close();
				}
			}
			
			PreparedStatement pstmtSubjects = conn.prepareStatement("SELECT * FROM subjects");
			ResultSet rsSubjects = pstmtSubjects.executeQuery();
			
			while (rsSubjects.next())
			{
				int subjectID = rsSubjects.getInt(1);
				
				PreparedStatement pstmtSubCount = conn.prepareStatement("SELECT COUNT(Topic_ID) FROM topics WHERE Subject_ID = " + subjectID);
				ResultSet rsSub = pstmtSubCount.executeQuery();
				int noSubTopics = rsSub.getInt(1);
				pstmtSubCount.close();
				
				if (noSubTopics == 0)
				{
					PreparedStatement pstmtDelete = conn.prepareStatement("DELETE FROM subjects WHERE Subject_ID = " + subjectID);
					pstmtDelete.execute();
					pstmtDelete.close();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
