package schedulebuilder.main;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ScheduleLine implements ChangeListener, Serializable{
	
	private Employee emp;
	private JLabel total;
	private JLabel empName;
	private JCheckBox[] isOn;
	private JSpinner[] times;
	private Calendar startDate;
	

	public ScheduleLine(Employee empl, Calendar startDate) {
		this.startDate = startDate;
		emp = empl;
		total = new JLabel("0");
		total.setForeground(Main.ForegroundColor);
		isOn = new JCheckBox[7];
		times = new JSpinner[14];
		
		Date initialTime = Main.OPEN;
		
		for (int k = 0; k < times.length; k++) {
			times[k] = ComponentFactory.NonVisibleTimeSpinner(CalendarFactory.getDefaultCalendarAtTime(initialTime).getTime(), Main.OPEN, Main.CLOSE);
			times[k].setFont(new Font("Tahoma", Font.PLAIN, 9));
			times[k].addChangeListener(this);
		}
		empName = new JLabel(emp.getName().split(" ")[0]);
		empName.setForeground(Main.ForegroundColor);
		empName.setPreferredSize(new Dimension(140, 16));
	}

	public void addToFrame(Container frmScheduleBuilder, int lineNumber) {
		int yPos = lineNumber * 2;
		frmScheduleBuilder.add(empName,
				"1, " + yPos + ", center, center");
		int x = 0;
		for (int j = 0; j < 14; j++) {
			JLabel off = new JLabel("OFF");
			off.setForeground(Main.ForegroundColor);
			isOn[j / 2] = ComponentFactory.isWorkingBox(times[j], times[j + 1],
					off);
			x += 3;
			frmScheduleBuilder.add(isOn[j / 2],
					(x - 1) + ", " + yPos + ", center, center");
			frmScheduleBuilder.add(off,
					x + ", " + yPos + ", 2, 1, center, center");
			frmScheduleBuilder.add(times[j],
					x + ", " + yPos + ", center, center");
			j++;
			int x2 = x + 1;
			frmScheduleBuilder.add(times[j],
					x2 + ", " + yPos + ", center, center");
		}
		frmScheduleBuilder.add(total,
				"23, " + yPos + ", center, center");
	}
	

	@Override
	public void stateChanged(ChangeEvent e) {
		int spinNumber = findByInstance(e.getSource());
		if(spinNumber == -1)
			return;
		if(spinNumber%2==0){
			((SpinnerDateModel) times[spinNumber+1].getModel()).setStart((Comparable) times[spinNumber].getValue());
		}else{
			((SpinnerDateModel) times[spinNumber-1].getModel()).setEnd((Comparable) times[spinNumber].getValue());
		}
		recalculateTotal();
	}
	
	private int findByInstance(Object js){
		for(int k = 0;k<14;k++){
			if(js == times[k])
				return k;
		}
		return -1;
	}
	
	private void recalculateTotal(){
		Float value = 0f;
		Float[] diffs = new Float[7];
		for(int k = 0;k<14;k++){
			Date in = (Date) times[k].getValue();
			k++;
			Date out = (Date) times[k].getValue();
			diffs[k/2] = (out.getTime()-in.getTime())/(1000f*60f*60f);
		}
		for(Float f:diffs)
			value+=f;
		total.setText(value+"");
	}
}
