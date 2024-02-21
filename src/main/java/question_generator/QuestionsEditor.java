package question_generator;

import java.awt.BorderLayout;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTable;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.Font;
import java.awt.Image;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JComboBox;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Icon;
import javax.swing.SwingConstants;

public class QuestionsEditor extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JComboBox cmbtFilter;
	private QuizSelector parent;
	
	public static ArrayList<String> subjects = new ArrayList<String>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					QuestionsEditor frame = new QuestionsEditor();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public QuestionsEditor() throws IOException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1286, 543);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(25, 25, 112));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnAdd = new JButton("+");
		btnAdd.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				newQuestion();
			}
		});
		btnAdd.setBounds(1225, 43, 29, 29);
		btnAdd.setForeground(Color.WHITE);
		btnAdd.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		btnAdd.setBorder(new LineBorder(Color.WHITE));
		contentPane.add(btnAdd);
		
		//BufferedImage myPicture = ImageIO.read(new File("search.gif"));
		ImageIcon icon = new ImageIcon("search.gif");
		Image scaleImage = icon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
		icon = new ImageIcon(scaleImage);
		JLabel lblSearch = new JLabel(icon);
		lblSearch.setHorizontalAlignment(SwingConstants.LEFT);
		lblSearch.setText("");
		lblSearch.setBounds(26, 43, 61, 29);
		lblSearch.setForeground(new Color(255, 255, 255));
		contentPane.add(lblSearch);
		
		JLabel lblNewLabel = new JLabel("< Back");
		lblNewLabel.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				goBack();
			}
		});
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setBounds(6, 6, 61, 16);
		contentPane.add(lblNewLabel);
		
		table = new JTable();
		table.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				if (table.getSelectedColumn() == table.getColumnCount() - 1)
				{
					deleteTest();
				}
				else if (table.getSelectedColumn() == table.getColumnCount() - 2)
				{
					openEditor();
				}
				
			}
		});
		
		cmbtFilter = new JComboBox();
		cmbtFilter.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() 
		{
			@Override
			public void keyReleased(KeyEvent e) 
			{
				loadTable();
			}
		});
		cmbtFilter.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				loadTable();
			}
		});
		
		cmbtFilter.setUI(new BasicComboBoxUI() 
        {
            @Override
            protected JButton createArrowButton() {
                BasicArrowButton arrowButton = new BasicArrowButton(BasicArrowButton.SOUTH, Color.decode("#044aba"), null, QuizSelector.LIGHT_BLUE, null);
                return arrowButton;
            }
        });
		cmbtFilter.setBounds(56, 43, 188, 27);
		cmbtFilter.setEditable(true);
		contentPane.add(cmbtFilter);
		table.setBounds(6, 109, 1248, 382);
		table.setRowHeight(30);
		table.setGridColor(new Color(192, 234, 252));
		table.setFillsViewportHeight(true);
		table.setBackground(new Color(192, 234, 252));
		table.setAutoCreateRowSorter(true);
		contentPane.add(table);
		
		table.setBackground(QuizSelector.LIGHT_BLUE);
		table.setGridColor(QuizSelector.LIGHT_BLUE);
		table.setFillsViewportHeight(true);
		MatteBorder border = new MatteBorder(1, 1, 0, 0, QuizSelector.DARK_BLUE);
		table.getTableHeader().setOpaque(false);
		table.getTableHeader().setBackground(QuizSelector.DARK_BLUE);
		table.getTableHeader().setForeground(Color.WHITE);
		table.setBorder(border);
		table.setAutoCreateRowSorter(true);
		table.getTableHeader().setReorderingAllowed(false);
		
		table.setAutoCreateRowSorter(true);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(6, 109, 1248, 382);
		contentPane.add(scrollPane);
		
		loadTable();
		
		setSubjectFilter();
		
		setLocationRelativeTo(null);
		
		//table.getColumnModel().getColumn(0).setMaxWidth(table.getColumnModel().getColumn(0).getWidth()/3);
		//table.getColumnModel().getColumn(1).setMinWidth(table.getColumnModel().getColumn(1).getWidth() * 5);
	}
	
	public QuestionsEditor (QuizSelector parent) throws IOException
	{
		this();
		this.parent = parent;
	}
	
	private void goBack()
	{
		this.setVisible(false);
		parent.setVisible(true);
	}
	
	public void loadTable()
	{
		Connection conn = null;
		
		try
		{
			conn = DatabaseManager.getConnection();
			
			String SQL = "";
			
			if (cmbtFilter.getSelectedItem() == null || cmbtFilter.getSelectedItem().toString().trim().equals("") || cmbtFilter.getSelectedItem().toString().trim().equals("All"))
			{
				SQL = "SELECT Question_ID, Question_Name, Topic_Name, Subject_Name FROM questions JOIN subjects ON (topics.Subject_ID=subjects.Subject_ID) JOIN topics ON (questions.Topic_ID=topics.Topic_ID)";
			}
			else
			{
				String search = cmbtFilter.getSelectedItem().toString().trim();
				SQL = "SELECT Question_ID, Question_Name, Topic_Name, Subject_Name FROM questions JOIN subjects ON (topics.Subject_ID=subjects.Subject_ID) JOIN topics ON (questions.Topic_ID=topics.Topic_ID) WHERE Question_Name LIKE \"%" + search + "%\" OR Topic_Name LIKE \"%" + search + "%\" OR Subject_Name LIKE \"%" + search + "%\"";
			}
			
			System.out.println(SQL);
			
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			ResultSet rs = pstmt.executeQuery();
			
			String[] colHeadings = {"ID", "Question", "Topic", "Subject"};
			DefaultTableModel dtm = buildTableModel(rs, colHeadings);
			String[] delete = new String[dtm.getRowCount()];
			String[] edit = new String[dtm.getRowCount()];
			
			String pencil = "✎";
			for (int i = 0; i < edit.length; i++)
			{
				edit[i] = pencil;
			}
			dtm.addColumn("", edit);
			
			int[] surrogates = {0xD83D, 0xDDD1};
			String bin = new String(surrogates, 0, surrogates.length);
			
			for (int i = 0; i < delete.length; i++)
			{
				delete[i] = bin;
			}
			
			dtm.addColumn("", delete);
			
			
			table.setModel(dtm);
			table.getColumnModel().getColumn(table.getColumnCount() - 1).setMaxWidth(50);
			table.getColumnModel().getColumn(table.getColumnCount() - 2).setMaxWidth(50);
			table.getColumnModel().getColumn(1).setMinWidth(300);;
			table.removeColumn(table.getColumnModel().getColumn(0));
			
			DefaultTableCellRenderer editRenderer = new DefaultTableCellRenderer() 
			{
			    Font font = new Font("SansSerif", Font.PLAIN, 20);;

			    @Override
			    public Component getTableCellRendererComponent(JTable table,
			            Object value, boolean isSelected, boolean hasFocus,
			            int row, int column) {
			        super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
			                row, column);
			        setFont(font);
			        return this;
			    }

			};
			
			DefaultTableCellRenderer deleteRenderer = new DefaultTableCellRenderer() 
			{
			    Font font = new Font("SansSerif", Font.PLAIN, 15);;

			    @Override
			    public Component getTableCellRendererComponent(JTable table,
			            Object value, boolean isSelected, boolean hasFocus,
			            int row, int column) {
			        super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
			                row, column);
			        setFont(font);
			        return this;
			    }

			};
			
			table.getColumnModel().getColumn(table.getColumnCount() - 2).setCellRenderer(editRenderer);
			table.getColumnModel().getColumn(table.getColumnCount() - 1).setCellRenderer(deleteRenderer);
			table.getTableHeader().setPreferredSize(
				     new Dimension(table.getWidth(), 30)
					);
			table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
			
			pstmt.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static DefaultTableModel buildTableModel(ResultSet rs, String[] ColumnNames)
	    	throws SQLException {
	ResultSetMetaData metaData = rs.getMetaData();
	// names of columns
	Vector<String> columnNames = new Vector<String>();	
	int columnCount = metaData.getColumnCount();
	
	if (ColumnNames.length == 0)
	{
		for (int column = 1; column <= columnCount; column++) 
		{
		    columnNames.add(metaData.getColumnName(column));
		}
	}
	else
	{
		for (int i = 0; i < ColumnNames.length; i++)
		{
			columnNames.add(ColumnNames[i]);
		}
	}
	// data of the table
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		while (rs.next()) 
		{
			int id = rs.getInt(1);
	    	Vector<Object> vector = new Vector<Object>();
	    	vector.add(id);
	    	for (int columnIndex = 2; columnIndex <= columnCount; columnIndex++) {
	        	vector.add(rs.getObject(columnIndex));
	    	}
	    	data.add(vector);
		}
		
		DefaultTableModel dtm = new DefaultTableModel(data, columnNames)
		{
			@Override
	         public boolean isCellEditable(int row, int column) 
	         {
	         	return false;
	         }
		};
		
		return dtm;
	}
	
	private void openEditor()
	{
		int selectedRow = table.getSelectedRow();
		int ID = (Integer) table.getModel().getValueAt(selectedRow, 0);
		QuestionEditorWindow qew = new QuestionEditorWindow (this, ID);
		this.setVisible(false);
		qew.setVisible(true);
		
		Connection conn = null;
		try
		{
			conn = DatabaseManager.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM questions WHERE Question_ID = " + ID);
			//System.out.println(getQuestionString(pstmt.executeQuery()));
			pstmt.close();
		}
		catch (Exception e) 
		{
		}
	}
	
	private void newQuestion()
	{
		QuestionEditorWindow qew = new QuestionEditorWindow (this);
		this.setVisible(false);
		qew.setVisible(true);
	}
	
	private void deleteTest()
	{
		int selectedRow = table.getSelectedRow();
		int ID = (Integer) table.getModel().getValueAt(selectedRow, 0);
		Connection conn = null;
		
		try
		{
			conn = DatabaseManager.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM questions WHERE Question_ID = " + ID);
			pstmt.execute();
			pstmt.close();
			loadTable();
			
			QuestionEditorWindow.checkSubjectsAndTopics();
			
			setSubjectFilter();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void setVisible (boolean visible)
	{
		super.setVisible(visible);
		
		if (visible == true)
		{
			loadTable();
			setSubjectFilter();
		}
	}
	
	private void setSubjectFilter()
	{
		Connection conn = null;
		
		System.out.println("HERE 1");
		
		try
		{
			conn = DatabaseManager.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT Subject_Name FROM subjects");
			ResultSet rs = pstmt.executeQuery();
			
			System.out.println("HERE 2");
			
			DefaultComboBoxModel dcmb = new DefaultComboBoxModel();
			dcmb.addElement("All");
			while (rs.next())
			{
				System.out.println("HERE 3: " + rs.getString(1));
				dcmb.addElement(rs.getString(1));
			}
			
			cmbtFilter.setModel(dcmb);
			
			pstmt.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private String getQuestionString (ResultSet rs)
	{
		String s = "";
		
		Connection conn = null;
		
		try
		{
			int topicID = rs.getInt(3);
			
			conn = DatabaseManager.getConnection();
			
			String question = rs.getString(2);
			
			String option1 = rs.getString(6);
			String option2 = rs.getString(7);
			String option3 = rs.getString(8);
			String option4 = rs.getString(9);
			
			PreparedStatement pstmtTopic = conn.prepareStatement("SELECT Topic_Name FROM topics WHERE Topic_ID = " + topicID);
			String topic = pstmtTopic.executeQuery().getString(1);
			pstmtTopic.close();
			
			PreparedStatement pstmtSubjectID = conn.prepareStatement("SELECT Subject_ID FROM topics WHERE Topic_ID = " + topicID);
			int subjectID = pstmtSubjectID.executeQuery().getInt(1);
			pstmtSubjectID.close();
			
			PreparedStatement pstmtSubject = conn.prepareStatement("SELECT Subject_Name FROM subjects WHERE Subject_ID = " + subjectID);
			String subject = pstmtSubject.executeQuery().getString(1);
			pstmtSubject.close();
			
			s = question + " " + topic + " " + subject + " " + option1 + " " + option2 + " " + option3 + " " + option4;
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return s;
	}
	
}
