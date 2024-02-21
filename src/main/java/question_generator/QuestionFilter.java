package question_generator;

import javax.swing.JComboBox;
import javax.swing.JTextField;

public class QuestionFilter extends FilterObject
{
	public JComboBox<String> questionBox;
	public JTextField questionSearch;
	
	public QuestionFilter(JComboBox<String> questionBox, JTextField questionSearch)
	{
		type = 1;
		this.questionBox = questionBox;
		this.questionSearch = questionSearch;
	}
}
