package question_generator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AnalysisWindow extends JFrame {

	private JPanel contentPane;
	private JTextField txtGoal;
	private String SQL;
	private QuizSelector parent;
	
	private JLabel lblAverage;
	private JLabel lblGoal;
	private JLabel lblAnalysis;
	private JLabel lblError;
	
	private double average;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AnalysisWindow frame = new AnalysisWindow();
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
	public AnalysisWindow() 
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 361, 331);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblAverage = new JLabel("Average:");
		lblAverage.setForeground(Color.WHITE);
		lblAverage.setBounds(32, 54, 304, 16);
		contentPane.add(lblAverage);
		
		lblGoal = new JLabel("Goal:");
		lblGoal.setForeground(Color.WHITE);
		lblGoal.setBounds(29, 91, 38, 16);
		contentPane.add(lblGoal);
		contentPane.setBackground(new Color(1, 24, 61));
		
		txtGoal = new JTextField();
		txtGoal.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				analyse();
			}
		});
		txtGoal.setBounds(73, 86, 130, 26);
		contentPane.add(txtGoal);
		txtGoal.setColumns(10);
		
		lblAnalysis = new JLabel("Analysis");
		lblAnalysis.setForeground(Color.WHITE);
		lblAnalysis.setBounds(32, 124, 304, 103);
		contentPane.add(lblAnalysis);
		
		JLabel lblBack = new JLabel("< Back");
		lblBack.setForeground(Color.WHITE);
		lblBack.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				goBack();
			}
		});
		lblBack.setBounds(6, 6, 61, 16);
		contentPane.add(lblBack);
		
		lblError = new JLabel("error");
		lblError.setForeground(Color.WHITE);
		lblError.setBounds(31, 239, 305, 40);
		contentPane.add(lblError);
		lblError.setVisible(false);
		
		setLocationRelativeTo(null);
	}
	
	private void analyse()
	{
		if (isNoError())
		{
			if (txtGoal.getText().equals(""))
			{
				lblAnalysis.setVisible(false);
			}
			else
			{
				Connection conn = null;
				
				try
				{
					double goal = Double.parseDouble(txtGoal.getText());
					double correct = 0;
					double total = 0;
					
					double averageHere = (double) Math.round(average * 100) / 100;
					
					System.out.println("Goal: " + goal);
					System.out.println("Average: " + averageHere);
					
					conn = DatabaseManager.getConnection();
					PreparedStatement pstmt = conn.prepareStatement(SQL);
					ResultSet rs = pstmt.executeQuery();
					
					double count = 0;
					double sum = 0;
					
					while (rs.next())
					{
						double numberCorrect = (double) rs.getInt(4);
						double totalCorrect = (double) rs.getInt(5);
						
						correct += numberCorrect;
						total += totalCorrect;
					}
					
					pstmt.close();
					
					if (goal == 0)
					{
						lblAnalysis.setText("<html>Your goal will be achieved no matter the number of questions you get wrong.</html>");
					}
					else if (goal == 100)
					{
						if (averageHere == 100)
						{
							lblAnalysis.setText("<html>Congratulations, You have achieved your goal! However, you cannot get anymore questions wrong.</html>");
						}
						else
						{
							lblAnalysis.setText("<html>You cannot achieve your goal no matter the number of questions you get correct.</html>");
						}
					}
					else if (goal > averageHere)
					{
						int number = (int) Math.ceil((correct - (goal/100)*total)/((goal/100) - 1));
						
						if (number == 0)
						{
							number++;
						}
						
						lblAnalysis.setText("<html>You need to get " + number + " more questions correct to achieve your goal.</html>");
					}
					else if (goal < averageHere)
					{
						int number = (int) Math.floor((correct - (goal/100)*total)/((goal/100)));
						
						if (number == 0)
						{
							lblAnalysis.setText("<html>Congratulations, You have achieved your goal! However, you cannot get anymore questions wrong.</html>");
						}
						else
						{
							lblAnalysis.setText("<html>Congratulations, You have achieved your goal! You can get " + number + " more questions wrong while staying above your goal.</html>");
						}
					}
					else
					{
						lblAnalysis.setText("<html>Congratulations, You have achieved your goal! However, you cannot get anymore questions wrong.</html>");
					}
					
					lblAnalysis.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		else
		{
			lblAnalysis.setVisible(false);
		}
	}
	
	private boolean isNoError()
	{
		boolean isNoError = true;
		
		try
		{
			Double.parseDouble(txtGoal.getText());
			lblError.setVisible(false);
		}
		catch (Exception e)
		{
			isNoError = false;
			lblError.setText("Please Enter a Valid Goal");
			lblError.setVisible(true);
		}
		
		return isNoError;
	}
	
	private double getAverage()
	{	
		Connection conn = null;
		
		try
		{
			conn = DatabaseManager.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			ResultSet rs = pstmt.executeQuery();
			
			double count = 0;
			double correctSum = 0;
			double totalSum = 0;
			
			while (rs.next())
			{
				double numberCorrect = (double) rs.getInt(4);
				double totalCorrect = (double) rs.getInt(5);
				
				System.out.println(rs.getString(2));
				
				correctSum += numberCorrect;
				totalSum += totalCorrect;
				
				count ++;
			}
			
			pstmt.close();
			
			double average = (correctSum / totalSum) * 100;
			this.average = average;
			return average;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public AnalysisWindow(QuizSelector parent, String SQL)
	{
		this();
		this.parent = parent;
		this.SQL = SQL;
		
		double average = getAverage();
		average = (double) Math.round(average * 100) / 100.0;
		
		if (average == -1)
		{
			lblAverage.setText("You do not have any such Tests");
			lblGoal.setVisible(false);
			txtGoal.setVisible(false);
		}
		else
		{
			lblAverage.setText("Your Accuracy Rate is " + average + "%");
		}
		
		lblAnalysis.setVisible(false);
	}
	
	private void goBack()
	{
		this.setVisible(false);
		parent.setVisible(true);
	}
}
