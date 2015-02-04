package schedulebuilder.main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class NewScheduleWindow {

	private JFrame frame;
	private JTextField textField;
	private JPanel loadPane,newPane;
	private JList<File> list;
	private DefaultListModel<File> listData;
	private JButton btnLoadSelectedFile;
	private FilenameFilter extension = new FilenameFilter(){
		@Override
		public boolean accept(File dir, String name) {
			return name.toLowerCase().endsWith(".sbf");
		}
	};
	private JLabel lblNewLabel;
	private JLabel lblCloseTime;
	private JSpinner spinner;
	private JSpinner spinner_1;
	private JLabel lblStartOfWeek;
	private JScrollPane scrollPane;
	private JButton btnAddEmployee;
	private JButton btnAccept;
	private JPanel employeeList;

	/**
	 * Create the application.
	 */
	public NewScheduleWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				if(e.getPropertyName().equalsIgnoreCase("enable")){
					refreshEmployeeList();
					frame.repaint();
				}
			}
		});
		frame.setBounds(100, 100, 475, 375);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loadPane = new JPanel();
		loadPane.setBackground(Main.BackgroundColor);
		newPane = new JPanel();
		newPane.setBackground(Main.BackgroundColor);
		JTabbedPane tabbedPane = new JTabbedPane();
		
		frame.getContentPane().add(tabbedPane);
		
		tabbedPane.addTab("Load Schedule", loadPane);
		prepLoadPane();
		tabbedPane.addTab("New Schedule", newPane);
		prepNewPane();
		
	}
	
	protected void refreshEmployeeList() {
		employeeList.removeAll();
		for(Employee e: Main.AllEmployees){
			EmpCheckBox chckbxName = new EmpCheckBox(e);
			chckbxName.setForeground(Main.ForegroundColor);
			chckbxName.setBackground(Main.BackgroundColor.brighter());
			
			chckbxName.setAlignmentY(Component.TOP_ALIGNMENT);
			employeeList.add(chckbxName);
		}
	}

	public void setFrameVisible(boolean b){
		frame.setVisible(b);
	}
	
	private class loadButtonAction implements ActionListener{
		private JFrame parent;
		public loadButtonAction(JFrame parent){
			this.parent = parent;
		}
		
		public void actionPerformed(ActionEvent arg0) {
			parent.setVisible(false);
			parent.setEnabled(false);
			new MainWindow(list.getSelectedValue());
			
		}
	}
	
	private void prepLoadPane(){
		loadPane.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("200px"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("120px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("95px"),},
			new RowSpec[] {
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("23px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(144dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		textField = new JTextField();
		loadPane.add(textField, "2, 2, 3, 1, fill, center");
		textField.setColumns(30);
		textField.setText(System.getProperty("user.home")+"\\Documents\\Schedules");
		
		JButton btnBrowse = new JButton("browse...");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				final JFileChooser jfc = new JFileChooser(textField.getText());
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = jfc.showOpenDialog(loadPane);
				if(jfc.getSelectedFile()!=null){
					textField.setText(jfc.getSelectedFile().getAbsolutePath());
					listData.clear();
					for(File f:new File(textField.getText()).listFiles(extension))
						listData.addElement(f);
				}
			}
		});
		loadPane.add(btnBrowse, "6, 2, fill, top");
		
		listData = new DefaultListModel<File>();
		for(File f:new File(textField.getText()).listFiles(extension))
			listData.addElement(f);
		list = new JList<File>(listData);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setForeground(Main.ForegroundColor);
		list.setBackground(Main.BackgroundColor.brighter());
		loadPane.add(list, "2, 4, 5, 1, fill, fill");
		
		btnLoadSelectedFile = new JButton("Load Selected File");
		btnLoadSelectedFile.addActionListener(new loadButtonAction(frame));
		loadPane.add(btnLoadSelectedFile, "4, 6, 3, 1");
	}
	
	private JDatePickerImpl datePicker;
	
	private void prepNewPane(){
		
		newPane.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("120px"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("100px"),
				ColumnSpec.decode("100px"),
				ColumnSpec.decode("max(27dlu;default):grow"),
				FormFactory.UNRELATED_GAP_COLSPEC},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("20px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("185px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		lblNewLabel = new JLabel("Open Time:");
		lblNewLabel.setForeground(Main.ForegroundColor);
		newPane.add(lblNewLabel, "2, 2, left, default");
		
		Calendar cal = CalendarFactory.getDefaultCalendar();
		cal.set(Calendar.HOUR_OF_DAY,0);
		Date minTime = cal.getTime();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		Date maxTime = cal.getTime();
		spinner = ComponentFactory.TimeSpinner(Main.OPEN,minTime,maxTime);
		spinner.setFont(new Font("Tahoma", Font.PLAIN, 11));
		spinner.setValue(Main.OPEN);
		
		newPane.add(spinner, "4, 2, 2, 1, left, center");
		
		lblCloseTime = new JLabel("Close Time:");
		lblCloseTime.setForeground(Main.ForegroundColor);
		newPane.add(lblCloseTime, "2, 4, left, default");
		
		spinner_1 = ComponentFactory.TimeSpinner(Main.CLOSE,minTime,maxTime);
		spinner_1.setFont(new Font("Tahoma", Font.PLAIN, 11));
		spinner_1.setValue(Main.CLOSE);
		newPane.add(spinner_1, "4, 4, 2, 1, left, center");
		
		lblStartOfWeek = new JLabel("Start of week:");
		lblStartOfWeek.setForeground(Main.ForegroundColor);
		newPane.add(lblStartOfWeek, "2, 6, left, default");
		
		UtilDateModel model = new UtilDateModel();
		Calendar now = Calendar.getInstance();
		while(now.get(Calendar.DAY_OF_WEEK)!=Calendar.SATURDAY)
			now.add(Calendar.DATE, 1);
		model.setDate(now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DATE));
		model.setSelected(true);
		JDatePanelImpl datePanel = new JDatePanelImpl(model);
		datePanel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter("MM-dd-yyyy"));
		
		datePicker.setMinimumSize(new Dimension(52, 20));
		datePicker.setPreferredSize(new Dimension(200, 20));
		datePicker.getJFormattedTextField().setPreferredSize(new Dimension(178, 20));
		datePicker.getJFormattedTextField().setFont(new Font("Tahoma", Font.PLAIN, 11));
		datePicker.getJFormattedTextField().setBackground(Color.WHITE);
		datePicker.setBackground(Color.WHITE);
		datePicker.setForeground(Color.BLACK);
		datePicker.setFont(new Font("Tahoma", Font.PLAIN, 11));
		
		employeeList = new JPanel();
		employeeList.setBackground(Main.BackgroundColor.brighter());
		scrollPane = new JScrollPane(employeeList);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		employeeList.setLayout(new BoxLayout(employeeList, BoxLayout.Y_AXIS));
		refreshEmployeeList();
		
		
		newPane.add(datePicker,"4, 6, 2, 1, center, bottom");
		
		
		newPane.add(scrollPane, "2, 8, 5, 1, fill, fill");
		
		btnAddEmployee = new JButton("Add Employee");
		btnAddEmployee.setPreferredSize(new Dimension(150, 20));
		btnAddEmployee.addActionListener(new addEmployeeAction());
		newPane.add(btnAddEmployee, "2, 10, 3, 1, left, default");
		
		btnAccept = new JButton("Accept");
		btnAccept.addActionListener(new acceptNewSchedule());
		btnAccept.setPreferredSize(new Dimension(150, 20));
		newPane.add(btnAccept, "5, 10, 2, 1, right, default");
	}
	
	private class DateLabelFormatter extends AbstractFormatter {
		 
	    private SimpleDateFormat dateFormatter;
	    
	    public DateLabelFormatter(String datePattern){
	    	super();
	    	dateFormatter = new SimpleDateFormat(datePattern);
	    }
	     
	    @Override
	    public Object stringToValue(String text) throws ParseException {
	    	//TODO Modify to use default Calendar
	        return dateFormatter.parseObject(text);
	    }
	 
	    @Override
	    public String valueToString(Object value) throws ParseException {
	        if (value != null) {
	            Calendar cal = (Calendar) value;
	            return dateFormatter.format(cal.getTime());
	        }
	         
	        return "";
	    }
	 
	}
	
	private class EmpCheckBox extends JCheckBox{
		private Employee employee;
		public EmpCheckBox(Employee emp){
			super(emp.getName());
			employee = emp;
		}
		public Employee getEmployee(){
			return employee;
		}
	}
	
	private class addEmployeeAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			frame.setEnabled(false);
			new AddEmployee(frame).setVisible(true);
		}
		
	}
	
	private class acceptNewSchedule implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			Component[] comps = employeeList.getComponents();
			ArrayList<Employee> selected = new ArrayList<Employee>();
			for(Component c: comps){
				if(c instanceof EmpCheckBox){
					EmpCheckBox current = (EmpCheckBox) c;
					if(current.isSelected()){
						selected.add(current.getEmployee());
					}
				}
			}
			Calendar startDate = CalendarFactory.getDefaultCalendarAtDate(((Date)datePicker.getModel().getValue()));
			
			Schedule sch = new Schedule(selected,startDate);
			MainWindow mw = new MainWindow(sch);
			try {
				Main.saveEmployees();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			mw.setVisible(true);
			frame.dispose();
		}
		
	}
}
