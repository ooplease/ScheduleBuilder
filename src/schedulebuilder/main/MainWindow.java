package schedulebuilder.main;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Calendar;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.jgoodies.forms.layout.FormLayout;

public class MainWindow {

	private JFrame frmScheduleBuilder;

	private JPanel internalPanel;

	private Schedule schedule;

	public MainWindow(Schedule sch) {
		schedule = sch;
		initialize();
	}
	public MainWindow(File sch){
		schedule = Schedule.loadFromFile(sch);
		initialize();
		frmScheduleBuilder.setVisible(true);
	}
	
	public void setVisible(boolean v){
		frmScheduleBuilder.setVisible(v);
	}

	private void initialize() {
		frmScheduleBuilder = new JFrame();

		frmScheduleBuilder.setIconImage(Toolkit.getDefaultToolkit().getImage(
				Main.class.getResource("/schedulemake/res/clock.png")));
		frmScheduleBuilder.setTitle("Schedule Builder");
		frmScheduleBuilder.addKeyListener(new Shortcuts());
		frmScheduleBuilder.setBounds(100, 100, 1200, 450);
		frmScheduleBuilder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frmScheduleBuilder.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic(KeyEvent.VK_F);
		menuBar.add(mnFile);

		JMenu mnNew = new JMenu("New");
		mnFile.add(mnNew);

		JMenuItem mntmSchedule = new JMenuItem("Schedule");
		mnNew.add(mntmSchedule);

		JMenuItem mntmEmployee = new JMenuItem("Employee");
		mnNew.add(mntmEmployee);

		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.setMnemonic(KeyEvent.VK_S);
		mnFile.add(mntmSave);

		JMenuItem mntmPrint = new JMenuItem("Print");
		mntmPrint.setMnemonic(KeyEvent.VK_P);
		mnFile.add(mntmPrint);

		JMenuItem mntmSyncWithCalendar = new JMenuItem("Sync With Calendar");
		mntmSyncWithCalendar.setMnemonic(KeyEvent.VK_Y);
		mnFile.add(mntmSyncWithCalendar);

		JMenu mnEdit = new JMenu("Edit");
		mnEdit.setMnemonic(KeyEvent.VK_E);
		menuBar.add(mnEdit);

		JMenuItem mntmSettings = new JMenuItem("Settings");
		mnEdit.setMnemonic(KeyEvent.VK_S);
		mnEdit.add(mntmSettings);

		JMenu mnHelp = new JMenu("Help");
		mnHelp.setMnemonic(KeyEvent.VK_H);
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		mntmAbout.setMnemonic(KeyEvent.VK_A);

		JPanel topPane = new JPanel(new FormLayout(Main.defaultColumns, Main.defaultRows));
		topPane.setBackground(Main.BackgroundColor);

		internalPanel = new JPanel();
		internalPanel.setLayout(new BoxLayout(internalPanel, BoxLayout.Y_AXIS));
		internalPanel.setBackground(Main.BackgroundColor);

		frmScheduleBuilder.setContentPane(new JScrollPane(internalPanel));

		JLabel lblName = new JLabel("Name");
		lblName.setForeground(Main.ForegroundColor);
		topPane.add(lblName, "1, 2, left, center");

		JLabel[] days = new JLabel[7];

		days[Calendar.SUNDAY-1] = new JLabel("Sunday");
		days[Calendar.SUNDAY-1].setForeground(Main.ForegroundColor);
		//topPane.add(lblSunday, "6, 2, 2, 1, center, center");

		days[Calendar.MONDAY-1] = new JLabel("Monday");
		days[Calendar.MONDAY-1].setForeground(Main.ForegroundColor);
		//topPane.add(lblMonday, "9, 2, 2, 1, center, center");

		days[Calendar.TUESDAY-1] = new JLabel("Tuesday");
		days[Calendar.TUESDAY-1].setForeground(Main.ForegroundColor);
		//topPane.add(lblTuesday, "12, 2, 2, 1, center, center");

		days[Calendar.WEDNESDAY-1] = new JLabel("Wednesday");
		days[Calendar.WEDNESDAY-1].setForeground(Main.ForegroundColor);
		//topPane.add(lblWednesday, "15, 2, 2, 1, center, center");

		days[Calendar.THURSDAY] = new JLabel("Thursday");
		days[Calendar.THURSDAY-1].setForeground(Main.ForegroundColor);
		//topPane.add(lblThursday, "18, 2, 2, 1, center, center");

		days[Calendar.FRIDAY-1] = new JLabel("Friday");
		days[Calendar.FRIDAY-1].setForeground(Main.ForegroundColor);
		//topPane.add(lblFriday, "21, 2, 2, 1, center, center");
		
		days[Calendar.SATURDAY-1] = new JLabel("Saturday");
		days[Calendar.SATURDAY-1].setForeground(Main.ForegroundColor);
		//topPane.add(lblSaturday, "3, 2, 2, 1, center, center");
		
		int startOfWeek = schedule.getStartDate().get(Calendar.DAY_OF_WEEK)-1;
		for(int k = 0;k<7;k++){
			topPane.add(days[startOfWeek],(3*(1+k))+", 2, 2, 1, center, center");
			if(startOfWeek == 6)
				startOfWeek = 0;
			else
				startOfWeek++;
		}

		JLabel lblTotals = new JLabel("Totals");
		lblTotals.setForeground(Main.ForegroundColor);
		topPane.add(lblTotals, "23, 2, right, center");

		for (int k = 0; k < 7; k++) {
			JLabel lblIn = new JLabel("In");
			lblIn.setForeground(Main.ForegroundColor);
			int x = k * 3 + 3;
			topPane.add(lblIn, x + ", 4, center, center");

			JLabel lblOut = new JLabel("Out");
			lblOut.setForeground(Main.ForegroundColor);
			x++;
			topPane.add(lblOut, x + ", 4, center, center");
		}
		schedule.update();
		topPane.setSize(new Dimension(1172, 64));
		topPane.setMaximumSize(new Dimension(1172, 64));

		topPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		internalPanel.add(topPane);
		schedule.setAlignmentX(Component.LEFT_ALIGNMENT);
		internalPanel.add(schedule);
	}
	
	private class Shortcuts extends KeyAdapter {
		private boolean ctrlHeld = false;

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
				ctrlHeld = true;
			}
			// TODO Add shortcut keys
			switch (e.getKeyCode()) {
			case KeyEvent.VK_S:
				// save
				break;
			case KeyEvent.VK_P:
				// print
				break;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
				ctrlHeld = false;
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			if (e.getKeyChar() == '\177') {
				// delete row method (when "delete" is typed)
				System.out.println("Key \"Delete\" Typed");
			}
		}
	}

}
