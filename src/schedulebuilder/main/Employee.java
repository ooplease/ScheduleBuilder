package schedulebuilder.main;

import java.io.Serializable;

public class Employee implements Serializable{
	static final int CUSTOMERCAREAGENT = 0;
	static final int ASSISTANTMANAGER = 1;
	static final int MANAGER = 2;
	
	private int minHours;
	private int maxHours;
	private String name;
	private int jobTitle;
	private Availability availability;
	private String email;
	
	public Employee(String name,String email,int minHours,int maxHours,int jobTitle){
		this.minHours = minHours;
		this.maxHours = maxHours;
		this.name = name;
		this.jobTitle = jobTitle;
		this.availability = new Availability();
		this.email = email;
	}

	public int getMinHours() {
		return minHours;
	}

	public void setMinHours(int minHours) {
		this.minHours = minHours;
	}

	public int getMaxHours() {
		return maxHours;
	}

	public void setMaxHours(int maxHours) {
		this.maxHours = maxHours;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(int jobTitle) {
		this.jobTitle = jobTitle;
	}
	
	public Availability getAvailability(){
		return availability;
	}
	
	public void setAvailability(Availability a){
		availability = a;
	}

	public void setEmail(String email){
		this.email = email;
	}
	
	public String getEmail(){
		return email;
	}

}
