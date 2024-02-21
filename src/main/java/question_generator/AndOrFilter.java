package question_generator;

import javax.swing.JComboBox;
import javax.swing.JSpinner;

public class AndOrFilter extends FilterObject
{
	public JComboBox<String> andOrBox;
	
	public AndOrFilter(JComboBox<String> andOrBox)
	{
		type = 0;
		this.andOrBox = andOrBox;
	}
}
