package schedulebuilder.main;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;

public class ComponentFactory {
	
	static final Font spinnerFont = new Font("Tahoma", Font.PLAIN, 9);
	static JSpinner NonVisibleTimeSpinner(Date initial,Date start,Date end){

		JSpinner temp = TimeSpinner(initial,start,end);
		
		temp.setEnabled(false);
		temp.setVisible(false);
		return temp;
	}
	
	static JSpinner TimeSpinner(Date initial, Date start,Date end){
		IncrementSpinnerDateModel model = new IncrementSpinnerDateModel(initial,start,end, 30);
		model.setLinkedModel(new SpinnerDateModel(initial,start,end,Calendar.MINUTE));
		
		JSpinner temp = new JSpinner();
		temp.setModel(model);
		temp.setEditor(new JSpinner.DateEditor(temp,"h:mm a"));
		
		temp.setPreferredSize(new Dimension(64,16));
		
		return temp;
	}
	
	static JCheckBox isWorkingBox(JSpinner in, JSpinner out, JLabel off){
		JCheckBox box = new JCheckBox("New check box");
		box.setPreferredSize(new Dimension(16, 16));
		box.setMinimumSize(new Dimension(16, 16));
		box.setMaximumSize(new Dimension(16, 16));
		box.setHideActionText(true);
		box.setBackground(Main.BackgroundColor);
		box.addActionListener(new isWorkingBoxListener(in,out,off));
		return box;
	}
	
	
	static class isWorkingBoxListener implements ActionListener{
		
		Component[] controlled;
		
		public isWorkingBoxListener(Component...con){
			controlled = con;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			for(Component c:controlled){
				c.setEnabled(!c.isEnabled());
				c.setVisible(!c.isVisible());
				
			}
		}
	}
	
	static class IncrementSpinnerDateModel extends SpinnerDateModel{
		private int inc;
		SpinnerModel linkedModel = null;
		
		public IncrementSpinnerDateModel(Date current,Date start,Date end, int incrementInMinutes){
			super(current, start, end,Calendar.MINUTE);
			inc = incrementInMinutes;
		}

		public void setLinkedModel(SpinnerModel linkedModel){
			this.linkedModel = linkedModel;
		}
		
		@Override
		public Object getNextValue(){
			Date next = (Date) getValue();
			if(next == null){
				if(linkedModel!=null){
					linkedModel.setValue(linkedModel.getNextValue());
				}
				return next;
			}
			Calendar cal = CalendarFactory.getDefaultCalendarAtTime(next);
			int remainder = cal.get(Calendar.MINUTE)%inc;
			if(remainder!=0)
				cal.add(Calendar.MINUTE, remainder);
			else
				cal.add(Calendar.MINUTE, inc);
			next = cal.getTime();
			if(getEnd().compareTo(next)<0){
				while(getEnd().compareTo(next)<0){
					cal.add(Calendar.MINUTE, -1);
					next = cal.getTime();
				}
				setValue(next);
			}
			else
				setValue(next);
			return next;
		}
		
		@Override
		public Object getPreviousValue(){
			Date next = (Date) getValue();
			if(next == null){
				if(linkedModel!=null){
					linkedModel.setValue(linkedModel.getNextValue());
				}
				return next;
			}
			Calendar cal = CalendarFactory.getDefaultCalendarAtTime(next);
			int remainder = cal.get(Calendar.MINUTE)%inc;
			if(remainder!=0)
				cal.add(Calendar.MINUTE, -remainder);
			else
				cal.add(Calendar.MINUTE, -inc);
			next = cal.getTime();
			if(getStart().compareTo(next)>0){
				while(getStart().compareTo(next)>0){
					cal.add(Calendar.MINUTE, 1);
					next = cal.getTime();
				}
				setValue(next);
			}
			else
				setValue(next);
			return next;
		}
		
	}
	
}
