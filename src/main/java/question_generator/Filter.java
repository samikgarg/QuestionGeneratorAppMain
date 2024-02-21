package question_generator;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class Filter extends JFrame {

    private JPanel filterPanel;
    private JLabel filterByLabel;
    private JComboBox<String> comboBox;
    private JButton addButton;
    private JScrollPane scrollPane;
    private JPanel layoutPanel;
    private JButton submitButton;
    private JPanel bottomPanel;
    private JLabel errorLabel;
    
    private QuizSelector parent;
    
    //private HashMap <Integer, ArrayList<Object>> filters;
    private ArrayList<FilterObject> filters;
    
    private HashMap<Integer, String> subjects;
	private HashMap<Integer, String> topics;
    
    int numberAdded;

    public Filter() {
        setTitle("Filter");
        setSize(620, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        filters = new ArrayList<FilterObject>();

        filterPanel = new JPanel();
        filterByLabel = new JLabel("Filter By:");
        filterByLabel.setForeground(Color.WHITE);
        DefaultComboBoxModel dcmb = new DefaultComboBoxModel();
        dcmb.addElement("Question");
        dcmb.addElement("Subject and Topic");
        dcmb.addElement("Times Asked");
        comboBox = new JComboBox()/*<>(new String[] {"Question", "Subject and Topic", "Times Asked"});*/;
        comboBox.setModel(dcmb);
        
        addButton = new JButton(" + ");
        addButton.setForeground(Color.WHITE);
        addButton.setBorder(new LineBorder(Color.WHITE));
        addButton.setFont(new Font("Lucida Grande", Font.BOLD, 15));
        
        JLabel backButtonLabel = new JLabel("< Back");
        backButtonLabel.setForeground(Color.WHITE);
        //backButtonLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        backButtonLabel.setPreferredSize(new Dimension(150, backButtonLabel.getPreferredSize().height));
        backButtonLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButtonLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                goBack();
            }
        });
        
        bottomPanel = new JPanel();
        
        bottomPanel.setBackground(new Color(1, 24, 61));
        
        filterPanel.add(backButtonLabel);

        filterPanel.add(filterByLabel);
        filterPanel.add(comboBox);
        filterPanel.add(addButton);
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        layoutPanel = new JPanel();
        layoutPanel.setLayout(new BoxLayout(layoutPanel, BoxLayout.Y_AXIS));
        layoutPanel.setBackground(new Color(1, 24, 61));

        scrollPane = new JScrollPane(layoutPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        scrollPane.setBackground(new Color(1, 24, 61));

        submitButton = new JButton("  Submit  ");
        submitButton.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		submit();
        	}
        });
        submitButton.setBorder(new LineBorder(Color.WHITE));
        submitButton.setForeground(Color.WHITE);
        
        subjects = new HashMap<Integer, String>();
		topics = new HashMap<Integer, String>();
        
        bottomPanel.add(submitButton);
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        numberAdded = 0;

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = (String) comboBox.getSelectedItem();
                switch (selected) {
                    case "Question":
                        addQuestionLayout();
                        break;
                    case "Subject and Topic":
                        addSubjectTopicLayout();
                        break;
                    case "Times Asked":
                        addTimesAskedLayout();
                        break;
                }
                revalidate();
                repaint();
                
                numberAdded++;
            }
        });
        
        
        addSubjectTopicLayout();
        revalidate();
        repaint();
        numberAdded++;
        

        // add the back button label to the content pane
       // getContentPane().add(backButtonLabel, BorderLayout.NORTH);
        
        errorLabel = new JLabel("Error message here");
        bottomPanel.add(errorLabel);
        
        //scrollPane.getVerticalScrollBar().setBackground(QuizSelector.DARK_BLUE);

        getContentPane().setLayout(new BorderLayout());
        //add(backButtonLabel, BorderLayout.NORTH);
        getContentPane().add(filterPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        //add(submitButton, BorderLayout.SOUTH);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        
        filterPanel.setBackground(new Color(1, 24, 61));
        
        setLocationRelativeTo(null);
        
        errorLabel.setVisible(false);
        errorLabel.setForeground(Color.RED);
        
        //add(errorLabel, BorderLayout.SOUTH);

     // add the error label to the filter panel below the submit button
        /*filterPanel.add(errorLabel, new GridBagConstraints(
	         0, // gridx
	         6, // gridy
	         3, // gridwidth
	         1, // gridheight
	         0, // weightx
	         0, // weighty
	         GridBagConstraints.CENTER, // anchor
	         GridBagConstraints.NONE, // fill
	         new Insets(10, 0, 0, 0), // insets
	         0, // ipadx
	         0  // ipady
        ));*/
        
        //addSubjectTopicLayout();

        setVisible(true);
        
        comboBox.setUI(new BasicComboBoxUI() 
        {
            @Override
            protected JButton createArrowButton() {
                BasicArrowButton arrowButton = new BasicArrowButton(BasicArrowButton.SOUTH, Color.decode("#044aba"), null, QuizSelector.LIGHT_BLUE, null);
                return arrowButton;
            }
        });
    }
    
    public Filter (QuizSelector parent)
    {
    	this();
    	this.parent = parent;
    }
    
    private void submit()
    {
    	if (isNoError())
    	{
    		String SQL = "SELECT * FROM questions JOIN subjects ON (topics.Subject_ID=subjects.Subject_ID) JOIN topics ON (questions.Topic_ID=topics.Topic_ID) WHERE ";
    		
    		if (filters.size() == 0)
    		{
    			SQL = "SELECT * FROM questions JOIN subjects ON (topics.Subject_ID=subjects.Subject_ID) JOIN topics ON (questions.Topic_ID=topics.Topic_ID)";
    		}
    		else
    		{
    			for (int i = 0; i < filters.size(); i+=2)
            	{
            		int type = filters.get(i).type;
            		
            		if (i > 0)
            		{
            			AndOrFilter aof = (AndOrFilter) filters.get(i - 1);
            			String AndOr = aof.andOrBox.getSelectedItem().toString().trim();
            			SQL += AndOr + " ";
            		}
            		
            		switch (type)
            		{
            		case 1:
            			QuestionFilter qf = (QuestionFilter) filters.get(i);
            			int selected = qf.questionBox.getSelectedIndex();
            			String search = qf.questionSearch.getText();
            			
            			SQL += "Question_Name ";
           				
           				switch (selected)
           				{
           				case 0:
           					SQL += "= \"" + search + "\" ";
           					break;
           				case 1:
           					SQL += "LIKE \"%" + search + "%\" ";
           					break;
           				case 2:
           					SQL += "LIKE \"%" + search + "\" ";
           					break;
           				case 3:
           					SQL += "LIKE \"" + search + "%\" ";
           					break;
            			}
            			break;
            			
            		case 2:
            			SubTopicFilter stf = (SubTopicFilter) filters.get(i);
            			String subject = stf.subjectBox.getSelectedItem().toString();
            			String topic = stf.topicBox.getSelectedItem().toString();
            			
            			if (topic.equals("Any") && !subject.equals("Any"))
            			{
            				SQL += "Subject_Name = \"" + subject + "\" ";
            			}
            			else if (!topic.equals("Any"))
            			{
            				SQL += "Topic_Name = \"" + topic + "\" ";
            			}
            			else
            			{
            				SQL += "Topic_Name LIKE \"%%\"";
            			}
           				break;
           				
           			case 3:
           				TimesAskedFilter taf = (TimesAskedFilter) filters.get(i);
            			int inequality = taf.timesBox.getSelectedIndex();
            			int value = Integer.parseInt(taf.spinner.getValue().toString());
            			
            			SQL += "Total_Number ";
           				
           				switch (inequality)
           				{
           				case 0:
           					SQL += "= " + value + " ";
           					break;
           				case 1:
           					SQL += "> " + value + " ";
           					break;
           				case 2:
           					SQL += "< " + value + " ";
           					break;
           				case 3:
           					SQL += ">= " + value + " ";
           					break;
           				case 4:
           					SQL += "<= " + value + " ";
           					break;
            			}
           				break;
            		}
            	}
    		}
    		
    		if (SQL.equals("SELECT * FROM questions JOIN subjects ON (topics.Subject_ID=subjects.Subject_ID) JOIN topics ON (questions.Topic_ID=topics.Topic_ID) WHERE "))
    		{
    			SQL =  "SELECT * FROM questions JOIN subjects ON (topics.Subject_ID=subjects.Subject_ID) JOIN topics ON (questions.Topic_ID=topics.Topic_ID)";
    		}
    		
    		SQL = SQL.trim();
        	
        	System.out.println(SQL);
        	
        	AnalysisWindow aw = new AnalysisWindow(parent, SQL);
        	this.setVisible(false);
        	aw.setVisible(true);
    	}
    }
    
    private boolean isNoError()
    {
    	boolean isNoError = true;
    	
    	for (int i = 0; i < filters.size(); i++)
    	{
    		FilterObject currFilter = filters.get(i);
    		
    		if (currFilter.type == 1)
    		{
    			QuestionFilter qf = (QuestionFilter) currFilter;
    			
    			if (qf.questionSearch.getText().trim().equals(""))
    			{
    				isNoError = false;
    				errorLabel.setText("Please Fill All Text Fields");
    				errorLabel.setVisible(true);
    			}
    		}
    	}
    	
    	return isNoError;
    }

    private void addQuestionLayout() {
        JPanel questionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.X_AXIS));
        questionPanel.setBackground(QuizSelector.DARK_BLUE);

        JLabel label = new JLabel("Question:");
        label.setForeground(Color.WHITE);
        JComboBox<String> comboBox = new JComboBox<>(new String[] {"Equals", "Contains", "Begins with", "Ends with"});
        comboBox.setUI(new BasicComboBoxUI() 
        {
            @Override
            protected JButton createArrowButton() {
                BasicArrowButton arrowButton = new BasicArrowButton(BasicArrowButton.SOUTH, Color.decode("#044aba"), null, QuizSelector.LIGHT_BLUE, null);
                return arrowButton;
            }
        });
        JTextField textField = new JTextField(30);
        Dimension maxTextFieldSize = new Dimension(Integer.MAX_VALUE, 20);
        textField.setMaximumSize(maxTextFieldSize);
        Dimension sizeCB = new Dimension();
        sizeCB.width = comboBox.getWidth();
        sizeCB.height = 20;
        comboBox.setMaximumSize(sizeCB);

        questionPanel.add(new JLabel("   "));
        questionPanel.add(label);
        questionPanel.add(new JLabel("   "));
        questionPanel.add(comboBox);
        questionPanel.add(new JLabel("   "));
        questionPanel.add(textField);
        
        if (numberAdded > 0)
        {
        	layoutPanel.add(addComboBoxPanel());
        }
        
        QuestionFilter qf = new QuestionFilter(comboBox, textField);
        filters.add(qf);
        
        JPanel extraPanel = new JPanel();
        extraPanel.setLayout(new BoxLayout(extraPanel, BoxLayout.X_AXIS));
        extraPanel.setBackground(QuizSelector.DARK_BLUE);
        extraPanel.add(new JLabel("   "));
        
        JPanel extraPanel2 = new JPanel();
        extraPanel2.setLayout(new BoxLayout(extraPanel2, BoxLayout.X_AXIS));
        extraPanel2.setBackground(QuizSelector.DARK_BLUE);
        extraPanel2.add(new JLabel("   "));

        layoutPanel.add(extraPanel);
        layoutPanel.add(questionPanel);
        layoutPanel.add(extraPanel2);
    }
    

    private void addSubjectTopicLayout() {
        JPanel subjectTopicPanel = new JPanel();
        subjectTopicPanel.setLayout(new BoxLayout(subjectTopicPanel, BoxLayout.X_AXIS));
        subjectTopicPanel.setBackground(QuizSelector.DARK_BLUE);

        JLabel subjectLabel = new JLabel("Subject:");
        subjectLabel.setForeground(Color.WHITE);
        final JComboBox<String> subjectComboBox = new JComboBox<String>();
        JLabel topicLabel = new JLabel("Topic:");
        topicLabel.setForeground(Color.WHITE);
        final JComboBox<String> topicComboBox = new JComboBox<String>();
        

        subjectTopicPanel.add(new JLabel("   "));
        subjectTopicPanel.add(subjectLabel);
        subjectTopicPanel.add(new JLabel("   "));
        subjectTopicPanel.add(subjectComboBox);
        subjectTopicPanel.add(new JLabel("   "));
        subjectTopicPanel.add(topicLabel);
        subjectTopicPanel.add(new JLabel("   "));
        subjectTopicPanel.add(topicComboBox);

        if (numberAdded > 0)
        {
        	layoutPanel.add(addComboBoxPanel());
        }
        
        subjectComboBox.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				setTopics(subjectComboBox, topicComboBox);
			}
		});
        
        SubTopicFilter stf = new SubTopicFilter(subjectComboBox, topicComboBox);
        filters.add(stf);
        
        setSubjects(subjectComboBox, topicComboBox
        		);
        
        JPanel extraPanel = new JPanel();
        extraPanel.setLayout(new BoxLayout(extraPanel, BoxLayout.X_AXIS));
        extraPanel.setBackground(QuizSelector.DARK_BLUE);
        extraPanel.add(new JLabel("   "));
        
        JPanel extraPanel2 = new JPanel();
        extraPanel2.setLayout(new BoxLayout(extraPanel2, BoxLayout.X_AXIS));
        extraPanel2.setBackground(QuizSelector.DARK_BLUE);
        extraPanel2.add(new JLabel("   "));
        
        layoutPanel.add(extraPanel);
        layoutPanel.add(subjectTopicPanel);
        layoutPanel.add(extraPanel2);
        
       Dimension sizeCBSub = new Dimension();
        sizeCBSub.width = Integer.MAX_VALUE;
        sizeCBSub.height = 20;
        subjectComboBox.setMaximumSize(sizeCBSub);
        
        Dimension sizeCBTopic = new Dimension();
        sizeCBTopic.width = Integer.MAX_VALUE;
        sizeCBTopic.height = 20;
        topicComboBox.setMaximumSize(sizeCBTopic);
        
        subjectComboBox.setUI(new BasicComboBoxUI() 
        {
            @Override
            protected JButton createArrowButton() {
                BasicArrowButton arrowButton = new BasicArrowButton(BasicArrowButton.SOUTH, Color.decode("#044aba"), null, QuizSelector.LIGHT_BLUE, null);
                return arrowButton;
            }
        });
        
        topicComboBox.setUI(new BasicComboBoxUI() 
        {
            @Override
            protected JButton createArrowButton() {
                BasicArrowButton arrowButton = new BasicArrowButton(BasicArrowButton.SOUTH, Color.decode("#044aba"), null, QuizSelector.LIGHT_BLUE, null);
                return arrowButton;
            }
        });
        
        
    }

    private void addTimesAskedLayout() {
        JPanel timesAskedPanel = new JPanel();
        timesAskedPanel.setLayout(new BoxLayout(timesAskedPanel, BoxLayout.X_AXIS));
        timesAskedPanel.setBackground(QuizSelector.DARK_BLUE);

        JLabel label = new JLabel("Total Number of Times Asked:");
        label.setForeground(Color.WHITE);
        JComboBox<String> comboBox = new JComboBox<>(new String[] {"Equal To", "Greater Than", "Lesser Than", "Greater Than or Equal To", "Lesser Than or Equal To"});
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, 999999, 1));
        Dimension maxTextFieldSize = new Dimension(Integer.MAX_VALUE, 20);
        spinner.setMaximumSize(maxTextFieldSize);
        timesAskedPanel.add(new JLabel("   "));
        timesAskedPanel.add(label);
        timesAskedPanel.add(new JLabel("   "));
        timesAskedPanel.add(comboBox);
        timesAskedPanel.add(new JLabel("   "));
        timesAskedPanel.add(spinner);
        
        comboBox.setUI(new BasicComboBoxUI() 
        {
            @Override
            protected JButton createArrowButton() {
                BasicArrowButton arrowButton = new BasicArrowButton(BasicArrowButton.SOUTH, Color.decode("#044aba"), null, QuizSelector.LIGHT_BLUE, null);
                return arrowButton;
            }
        });
        
        Dimension sizeCB = new Dimension();
        sizeCB.width = Integer.MAX_VALUE;
        sizeCB.height = 20;
        comboBox.setMaximumSize(sizeCB);

        if (numberAdded > 0)
        {
        	layoutPanel.add(addComboBoxPanel());
        }
        
        TimesAskedFilter taf = new TimesAskedFilter(comboBox, spinner);
        filters.add(taf);
        
        JPanel extraPanel = new JPanel();
        extraPanel.setLayout(new BoxLayout(extraPanel, BoxLayout.X_AXIS));
        extraPanel.setBackground(QuizSelector.DARK_BLUE);
        extraPanel.add(new JLabel("   "));
        
        JPanel extraPanel2 = new JPanel();
        extraPanel2.setLayout(new BoxLayout(extraPanel2, BoxLayout.X_AXIS));
        extraPanel2.setBackground(QuizSelector.DARK_BLUE);
        extraPanel2.add(new JLabel("   "));
        
        layoutPanel.add(extraPanel2);
        layoutPanel.add(timesAskedPanel);
        layoutPanel.add(extraPanel);
    }
    
    private void setSubjects(JComboBox<String> cmbSubject, JComboBox<String> cmbTopic)
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
			setTopics(cmbSubject, cmbTopic);
			
			pstmtGetSubjects.close();
			//conn.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void setTopics(JComboBox cmbSubject, JComboBox<String> cmbTopic)
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

    private JPanel addComboBoxPanel() {
        JPanel comboBoxPanel = new JPanel();
        comboBoxPanel.setLayout(new BoxLayout(comboBoxPanel, BoxLayout.X_AXIS));
        JComboBox<String> comboBox = new JComboBox<>(new String[] {"And", "Or"});
        comboBoxPanel.add(new JLabel("   "));
        comboBoxPanel.add(comboBox);
        comboBoxPanel.add(new JLabel("   "));
        
        comboBoxPanel.setBackground(QuizSelector.DARK_BLUE);
        
        AndOrFilter aof = new AndOrFilter(comboBox);
        filters.add(aof);
        
        comboBox.setUI(new BasicComboBoxUI() 
        {
            @Override
            protected JButton createArrowButton() {
                BasicArrowButton arrowButton = new BasicArrowButton(BasicArrowButton.SOUTH, Color.decode("#044aba"), null, QuizSelector.LIGHT_BLUE, null);
                return arrowButton;
            }
        });
        
        Dimension sizeCB = new Dimension();
        sizeCB.width = Integer.MAX_VALUE;
        sizeCB.height = 20;
        comboBox.setMaximumSize(sizeCB);
        
        return comboBoxPanel;
    }

    public static void main(String[] args) {
        Filter filter = new Filter();
    }
    
    private void goBack()
    {
    	this.setVisible(false);
    	parent.setVisible(true);
    }
}
        



