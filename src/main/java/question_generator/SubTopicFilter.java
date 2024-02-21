package question_generator;

import javax.swing.JComboBox;

public class SubTopicFilter extends FilterObject
{
	public JComboBox<String> subjectBox;
	public JComboBox<String> topicBox;
	
	public SubTopicFilter(JComboBox<String> subjectBox, JComboBox<String> topicBox)
	{
		type = 2;
		this.subjectBox = subjectBox;
		this.topicBox = topicBox;
	}
}
