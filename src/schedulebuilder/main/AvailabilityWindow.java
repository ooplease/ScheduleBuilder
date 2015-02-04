package schedulebuilder.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JButton;

public class AvailabilityWindow {
	private JFrame window;
	private AddEmployee parent;
	private Availability avail;
	private JSpinner[] ins;
	private JSpinner[] outs;

	public AvailabilityWindow(AddEmployee parent, Availability avail) {
		this.avail = avail;
		this.parent = parent;
		window = new JFrame();
		window.setAlwaysOnTop(true);
		window.setPreferredSize(new Dimension(350, 350));
		window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		window.setBounds(100, 100, 1050, 275);
		JPanel innerpane = new JPanel();
		JScrollPane scrollpane = new JScrollPane(innerpane);
		scrollpane.setBorder(BorderFactory.createEmptyBorder());
		JPanel bottompane = new JPanel();
		bottompane.setBackground(Main.BackgroundColor);
		window.getContentPane().setLayout(new BoxLayout(window.getContentPane(),BoxLayout.Y_AXIS));
		window.add(scrollpane);
		window.add(bottompane);
		innerpane.setLayout(
						new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("18px"),
				ColumnSpec.decode("64px"),
				ColumnSpec.decode("64px"),
				ColumnSpec.decode("18px"),
				ColumnSpec.decode("64px"),
				ColumnSpec.decode("64px"),
				ColumnSpec.decode("18px"),
				ColumnSpec.decode("64px"),
				ColumnSpec.decode("64px"),
				ColumnSpec.decode("18px"),
				ColumnSpec.decode("64px"),
				ColumnSpec.decode("64px"),
				ColumnSpec.decode("18px"),
				ColumnSpec.decode("64px"),
				ColumnSpec.decode("64px"),
				ColumnSpec.decode("18px"),
				ColumnSpec.decode("64px"),
				ColumnSpec.decode("64px"),
				ColumnSpec.decode("18px"),
				ColumnSpec.decode("64px"),
				ColumnSpec.decode("64px"),},
			new RowSpec[] {
				RowSpec.decode("25px"),
				RowSpec.decode("16px"),
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("16px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		innerpane.setBackground(Main.BackgroundColor);
		
		
		JLabel lblSat = new JLabel("Saturday");
		lblSat.setForeground(Main.ForegroundColor);
		innerpane.add(lblSat, "3, 2, 2, 1, center, default");

		JLabel lblSun = new JLabel("Sunday");
		lblSun.setForeground(Main.ForegroundColor);
		innerpane.add(lblSun, "6, 2, 2, 1, center, default");

		JLabel lblMon = new JLabel("Monday");
		lblMon.setForeground(Main.ForegroundColor);
		innerpane.add(lblMon, "9, 2, 2, 1, center, default");

		JLabel lblTue = new JLabel("Tuesday");
		lblTue.setForeground(Main.ForegroundColor);
		innerpane.add(lblTue, "12, 2, 2, 1, center, default");

		JLabel lblWed = new JLabel("Wednesday");
		lblWed.setForeground(Main.ForegroundColor);
		innerpane.add(lblWed, "15, 2, 2, 1, center, default");

		JLabel lblThu = new JLabel("Thursday");
		lblThu.setForeground(Main.ForegroundColor);
		innerpane.add(lblThu, "18, 2, 2, 1, center, default");

		JLabel lblFri = new JLabel("Friday");
		lblFri.setForeground(Main.ForegroundColor);
		innerpane.add(lblFri, "21, 2, 2, 1, center, default");
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new cancel());
		bottompane.add(btnCancel);
		
		JButton btnAccept = new JButton("Accept");
		btnAccept.addActionListener(new accept());
		bottompane.add(btnAccept);
		
		ins = new JSpinner[7];
		outs = new JSpinner[7];
		
		Date start;
		Date end;
		Calendar c = CalendarFactory.getDefaultCalendar();
		boolean preset;
		preset = avail!=null;
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.AM_PM, Calendar.AM);
		start = c.getTime();
		c.set(Calendar.HOUR, 11);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.AM_PM, Calendar.PM);
		end = c.getTime();
		
		
		Font spinFont = new Font("Tahoma",Font.PLAIN,9);
		
		for(int k = 0;k<7;k++){
			boolean available = true;
			Date currentStart = start;
			Date currentEnd = end;
			if(preset){
				if(avail.hasNoAvailability(k)){
					available = false;
				}else{
					currentStart = avail.getStart(k).getTime();
					currentEnd = avail.getEnd(k).getTime();
				}
			}
			JSpinner in = ComponentFactory.TimeSpinner(currentStart, currentStart, currentEnd);
			in.setFont(spinFont);
			ins[k] = in;
			JSpinner out = ComponentFactory.TimeSpinner(currentEnd, currentStart, currentEnd);
			out.setFont(spinFont);
			outs[k] = out;
			JLabel none = new JLabel("NONE");
			none.setForeground(Main.ForegroundColor);
			JCheckBox box = ComponentFactory.isWorkingBox(in, out, none);
			none.setVisible(!available);
			in.setVisible(available);
			out.setVisible(available);
			innerpane.add(none,(3*k+3)+", 4, 2, 1, center, center");
			innerpane.add(box,(3*k+2)+", 4");
			innerpane.add(in,(3*k+3)+", 4");
			innerpane.add(out,(3*k+4)+", 4");
		}
	}
	
	public void setVisible(boolean b){window.setVisible(b);}

	public Availability getAvailability() {
		return avail;
	}
	
	private class accept implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			avail = new Availability();
			for(int k = 0;k<7;k++){
				if(ins[k].isVisible()){
					Calendar in = CalendarFactory.getDefaultCalendar();
					in.setTime(((Date)ins[k].getValue()));
					Calendar out = CalendarFactory.getDefaultCalendar();
					out.setTime(((Date)outs[k].getValue()));
					avail.setAvailability(k, in, out);
				}else{
					avail.setUnavailable(k);
				}
			}
			parent.setAvailability(avail);
			window.dispose();
		}
		
	}
	
	private class cancel implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			parent.setAvailability(null);
			window.dispose();
		}
		
	}

}
