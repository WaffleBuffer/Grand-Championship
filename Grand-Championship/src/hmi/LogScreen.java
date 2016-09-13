package hmi;

import java.awt.BorderLayout;
import java.time.LocalDateTime;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import utilities.Fonts;

/**
 * Screen displaying the game's log
 * @author Thomas
 *
 */
public class LogScreen extends JFrame{
	
	/**
	 * stuff for serialization
	 */
	private static final long serialVersionUID = -5696399741411777390L;
	/**
	 * The text displayed (must contains all logs)
	 */
	private String logs = Fonts.CSS + System.lineSeparator();
	/**
	 * The screen where the logs will be displayed
	 */
	private final JTextPane screen;
	
	/**
	 * Constructor of he JFrame
	 */
	public LogScreen() {
		super("Log");
		
		//The main content panel
		final JPanel content = new JPanel(new BorderLayout());
		// JTextPane parse html!
		screen = new JTextPane();
		screen.setContentType("text/html");
		displayLog("<b>Welcome!</b>");
		// To scroll
		final JScrollPane scroll = new JScrollPane(screen);
		content.add(scroll);
		
		// Final tweaks
		this.setContentPane(content);
		// TODO : make something good about this.
		this.setSize(600, 500);
		this.setVisible(true);
	}
	
	/**
	 * Add a log to the display. The log should be in html.
	 * @param log The log to add. Should be in html.
	 * @see Fonts#wrapHtml(String, utilities.Fonts.LogType)
	 */
	public void displayLog (final String log) {
		this.logs += LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getHour() + " : " + log + "<br><br>";
		screen.setText(this.logs);
	}
}
