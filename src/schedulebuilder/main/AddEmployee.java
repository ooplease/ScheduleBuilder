package schedulebuilder.main;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class AddEmployee{

	private JPanel contentPane;
	private JTextField nameField;
	private JComboBox<String> jobTitleSelector;
	private JLabel lblNewLabel_1;
	private JSpinner minHours;
	private JLabel lblNewLabel_2;
	private JSpinner maxHours;
	private JLabel lblNewLabel_3;
	private JTextField emailField;
	private JButton btnSet;
	private JButton btnCreateEmployee;
	
	private JFrame window;
	
	private JFrame parent;
	
	private Availability avail;
	private AvailabilityWindow awin;
	private JButton btnNewButton;
	
	public AddEmployee(JFrame parent){
		this();
		this.parent = parent;
	}
	
	public void setVisible(boolean b){
		window.setVisible(b);
	}
	
	public void setAvailability(Availability a){
		if(a != null)
			avail = a;
		window.setEnabled(true);
	}
	
	public JFrame getWindow(){
		return window;
	}

	/**
	*@wbp.parser.constructor
	*/
	public AddEmployee() {
		window = new JFrame();
		window.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				if(e.getPropertyName().equalsIgnoreCase("avail")){
					avail = awin.getAvailability();
				}
			}
		});
		window.setAlwaysOnTop(true);
		window.setPreferredSize(new Dimension(350, 350));
		window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		window.setBounds(100, 100, 275, 275);
		contentPane = new JPanel();
		contentPane.setBackground(Main.BackgroundColor);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		window.setContentPane(contentPane);
		contentPane.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("80px"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("22px"),
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("22px"),
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("22px"),
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("22px"),
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("22px"),
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("22px"),
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblNewLabel = new JLabel("Name:");
		lblNewLabel.setForeground(Main.ForegroundColor);
		contentPane.add(lblNewLabel, "2, 2, default, fill");
		
		nameField = new JTextField();
		lblNewLabel.setLabelFor(nameField);
		nameField.setSize(new Dimension(150, 15));
		nameField.setPreferredSize(new Dimension(150, 15));
		nameField.setMinimumSize(new Dimension(150, 15));
		nameField.setMaximumSize(new Dimension(150, 15));
		contentPane.add(nameField, "4, 2, fill, fill");
		
		JLabel lblPosition = new JLabel("Job Title:");
		lblPosition.setForeground(Main.ForegroundColor);
		contentPane.add(lblPosition, "2, 4, fill, fill");
		
		jobTitleSelector = new JComboBox<String>();
		lblPosition.setLabelFor(jobTitleSelector);
		jobTitleSelector.setModel(new DefaultComboBoxModel<String>(new String[] {"Customer Care Agent", "Assistant Manager", "Manager"}));
		contentPane.add(jobTitleSelector, "4, 4, fill, fill");
		
		lblNewLabel_1 = new JLabel("Minimum Hours:");
		lblNewLabel_1.setForeground(Main.ForegroundColor);
		contentPane.add(lblNewLabel_1, "2, 6, fill, fill");
		
		minHours = new JSpinner();
		lblNewLabel_1.setLabelFor(minHours);
		minHours.setModel(new SpinnerNumberModel(0, 0, 75, 5));
		contentPane.add(minHours, "4, 6, fill, fill");
		
		lblNewLabel_2 = new JLabel("Maximum Hours:");
		lblNewLabel_2.setForeground(Main.ForegroundColor);
		contentPane.add(lblNewLabel_2, "2, 8, fill, fill");
		
		maxHours = new JSpinner();
		lblNewLabel_2.setLabelFor(maxHours);
		maxHours.setModel(new SpinnerNumberModel(0, 0, 75, 5));
		contentPane.add(maxHours, "4, 8, fill, fill");
		
		lblNewLabel_3 = new JLabel("E-Mail:");
		lblNewLabel_3.setForeground(Main.ForegroundColor);
		contentPane.add(lblNewLabel_3, "2, 10, fill, fill");
		
		emailField = new JTextField();
		lblNewLabel_3.setLabelFor(emailField);
		contentPane.add(emailField, "4, 10, fill, fill");
		emailField.setColumns(10);
		
		btnSet = new JButton("Set Availability...");
		btnSet.addActionListener(new availabilityListener(this));
		contentPane.add(btnSet, "2, 12, 3, 1");
		
		btnCreateEmployee = new JButton("Create Employee");
		btnCreateEmployee.addActionListener(new createEmployeeListener());
		contentPane.add(btnCreateEmployee, "2, 14, 3, 1");
		
		btnNewButton = new JButton("Cancel");
		btnNewButton.addActionListener(new cancelListener());
		contentPane.add(btnNewButton, "2, 16, 3, 1");
	}
	
	private class createEmployeeListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String warning = "";
			if(nameField.getText().equals("")){
				warning += "Employee requires a name\n";
			}
			if(emailField.getText().equals("")){
				warning += "Employee requires an email address\n";
			}
			if((Integer)minHours.getValue()>(Integer)maxHours.getValue()){
				warning += "Miniumum hours cannot be greater than maximum hours\n";
			}
			if((Integer)maxHours.getValue()<1){
				warning += "Maximum hours must be greater than 0\n";
			}
			if(avail==null){
				warning += "Availability must be set\n";
			}
			if(!warning.equals("")){
				JOptionPane.showMessageDialog(window,warning,"Warning",JOptionPane.WARNING_MESSAGE);
				return;
			}
			Employee temp = new Employee(nameField.getText(), emailField.getText(), (Integer) minHours.getValue(), (Integer) maxHours.getValue(), jobTitleSelector.getSelectedIndex());
			temp.setAvailability(avail);
			Main.AllEmployees.add(temp);
			parent.setEnabled(true);
			window.dispose();
			parent.firePropertyChange("enable", 0, 1);
		}
		
	}
	
	private class cancelListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0){
			parent.setEnabled(true);
			window.dispose();
			parent.firePropertyChange("enable",0,1);
		}
		
	}
	
	private class availabilityListener implements ActionListener{
		private AddEmployee ae;
		
		public availabilityListener(AddEmployee ae){
			this.ae = ae;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			AvailabilityWindow awin = new AvailabilityWindow(ae,avail);
			window.setEnabled(false);
			awin.setVisible(true);
		}
		
	}

}
