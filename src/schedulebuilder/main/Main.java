package schedulebuilder.main;

import java.awt.EventQueue;


import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;







import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;

public class Main {


	public static Color BackgroundColor;
	public static Date CLOSE;
	public static ColumnSpec[] defaultColumns = new ColumnSpec[] {
			ColumnSpec.decode("100px"), ColumnSpec.decode("18px"),
			ColumnSpec.decode("64px"), ColumnSpec.decode("64px"),
			ColumnSpec.decode("18px"), ColumnSpec.decode("64px"),
			ColumnSpec.decode("64px"), ColumnSpec.decode("18px"),
			ColumnSpec.decode("64px"), ColumnSpec.decode("64px"),
			ColumnSpec.decode("18px"), ColumnSpec.decode("64px"),
			ColumnSpec.decode("64px"), ColumnSpec.decode("18px"),
			ColumnSpec.decode("64px"), ColumnSpec.decode("64px"),
			ColumnSpec.decode("18px"), ColumnSpec.decode("64px"),
			ColumnSpec.decode("64px"), ColumnSpec.decode("18px"),
			ColumnSpec.decode("64px"), ColumnSpec.decode("64px"),
			ColumnSpec.decode("50px"), };

	public static RowSpec[] defaultRows = new RowSpec[] {
			RowSpec.decode("25px"), RowSpec.decode("16px"),
			FormFactory.LINE_GAP_ROWSPEC, RowSpec.decode("16px"), };
	public static Color ForegroundColor;

	public static Date OPEN;
	private static Properties prefs;
	public static ArrayList<Employee> AllEmployees;
	
	

	public static final String programDataRoot = System
			.getProperty("user.home") + "\\Documents\\Schedules";
	public static void loadPreferences() {
		File prefFile = new File(programDataRoot + "\\schedulebuilder.ini");
		prefs = Properties.load(prefFile);
		try {
			if (prefs.isEmpty()) {
				System.out
						.println("Preferences failed to load or did not exist, generating new preferences file");
				prefFile.createNewFile();
				BackgroundColor = Color.DARK_GRAY;
				prefs.put("BackgroundColor", BackgroundColor);
				ForegroundColor = Color.WHITE;
				prefs.put("ForegroundColor", ForegroundColor);
				
				Calendar cal = CalendarFactory.getDefaultCalendar();
				cal.set(Calendar.HOUR_OF_DAY, 6);
				OPEN = cal.getTime();
				prefs.put("OPEN", OPEN);
				cal.set(Calendar.HOUR_OF_DAY, 21);
				cal.set(Calendar.MINUTE, 30);
				CLOSE = cal.getTime();
				prefs.put("CLOSE", CLOSE);
				if (!prefs.save())
					System.out.println("Preferences failed to save");
			}else{
				BackgroundColor = (Color) prefs.get("BackgroundColor");
				ForegroundColor = (Color) prefs.get("ForegroundColor");
				CLOSE = (Date) prefs.get("CLOSE");
				OPEN = (Date) prefs.get("OPEN");
			}
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}
	
	public static void loadEmployees(){
		File employees = new File(programDataRoot + "\\employees.emp");
		try{
			FileInputStream fileIn = new FileInputStream(employees);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			AllEmployees = (ArrayList<Employee>) in.readObject();
			in.close();
			fileIn.close();
		}
		catch(IOException i){
			i.printStackTrace();
			AllEmployees = new ArrayList<Employee>();
		}
		catch(ClassNotFoundException c){
			c.printStackTrace();
			AllEmployees = new ArrayList<Employee>();
		}
	}
	
	public static void saveEmployees() throws IOException{
		File employees = new File(programDataRoot + "\\employees.emp");
		employees.createNewFile();
		FileOutputStream fileOut = new FileOutputStream(employees);
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(AllEmployees);
		out.close();
		fileOut.close();
	}

	public static void main(String[] args) {
		loadPreferences();
		//TODO load employees from file
		loadEmployees();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewScheduleWindow nsw = new NewScheduleWindow();
					nsw.setFrameVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
