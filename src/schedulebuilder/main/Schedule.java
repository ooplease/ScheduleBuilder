package schedulebuilder.main;

import java.awt.Color;
import java.awt.Container;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class Schedule extends JPanel implements Serializable{
	private ArrayList<ScheduleLine> employees;
	private Calendar startDate;
	
	public Schedule(ArrayList<Employee> emp, Calendar startDate){
		this.startDate = startDate;
		employees = new ArrayList<ScheduleLine>();
		for(Employee e:emp)
			employees.add(new ScheduleLine(e, startDate));
		setLayout(new FormLayout(Main.defaultColumns,new RowSpec[]{}));
		setBackground(Main.BackgroundColor);
	}
	
	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	public void update(){
		ArrayList<RowSpec> row = new ArrayList<RowSpec>();
		for(int k = 0;k<employees.size();k++){
			row.add(FormFactory.LINE_GAP_ROWSPEC);
			row.add(RowSpec.decode("16px"));
		}
		
		FormLayout mgr = new FormLayout(Main.defaultColumns, row.toArray(new RowSpec[row.size()]));
		setLayout(mgr);
		for(int k = 0;k<employees.size();k++)
			employees.get(k).addToFrame(this, k+1);
	}
	
	public static Schedule loadFromFile(File sbf){
		try{
			FileInputStream fileIn = new FileInputStream(sbf);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			Schedule temp = (Schedule) in.readObject();
			in.close();
			fileIn.close();
			return temp;
		}
		catch(IOException i){
			i.printStackTrace();
			return null;
		}
		catch(ClassNotFoundException c){
			c.printStackTrace();
			return null;
		}
	}
	
	public boolean saveToFile(File sbf){
		try{
			sbf.createNewFile();
			FileOutputStream fileOut = new FileOutputStream(sbf);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this);
			out.close();
			fileOut.close();
			return true;
		}catch(IOException i){
			i.printStackTrace();
			return false;
		}
	}
}