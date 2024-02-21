package question_generator;

import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JTextField;

public class TimesAskedFilter extends FilterObject
{
	public JComboBox<String> timesBox;
	public JSpinner spinner;
	
	public TimesAskedFilter(JComboBox<String> timesBox, JSpinner spinner)
	{
		type = 3;
		this.timesBox = timesBox;
		this.spinner = spinner;
	}
}
