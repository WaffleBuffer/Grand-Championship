package hmi;

import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class LogScreen extends JFrame{

	public LogScreen() {
		super("Log");
		
		//The main content panel
		final JPanel content = new JPanel();
		// JTextPane parse html!
		final JTextPane screen = new JTextPane();
		screen.setText("<b>Welcome!</b>");
		// To scroll
		final JScrollPane scroll = new JScrollPane(screen);
		content.add(scroll);
		
		// Final tweaks
		this.setContentPane(content);
		// TODO : make something good about this.
		this.setSize(600, 500);
		this.setVisible(true);
	}
}
