package schedulebuilder.main;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;

public class Availability implements Serializable{
	private HashMap<Integer, Time> startTimes;
	private HashMap<Integer, Time> endTimes;
	public static final int SATURDAY = 0;
	public static final int SUNDAY = 1;
	public static final int MONDAY = 2;
	public static final int TUESDAY = 3;
	public static final int WEDNESDAY = 4;
	public static final int THURSDAY = 5;
	public static final int FRIDAY = 6;
	
	
	public Availability(){
		Calendar start, end;
		start = end = CalendarFactory.getDefaultCalendar();
		start.set(Calendar.MINUTE,0);
		start.set(Calendar.HOUR,0);
		start.set(Calendar.AM_PM, Calendar.AM);
		end.set(Calendar.MINUTE,59);
		end.set(Calendar.HOUR,11);
		end.set(Calendar.AM_PM, Calendar.PM);
		startTimes = new HashMap<Integer, Time>();
		endTimes = new HashMap<Integer, Time>();
		for(int k = 0;k<7;k++){
			startTimes.put(k, new Time(start));
			endTimes.put(k, new Time(end));
		}
	}
	
	public void setAvailability(int dayOfWeek, Calendar s, Calendar e){
		startTimes.put(dayOfWeek, new Time(s));
		endTimes.put(dayOfWeek, new Time(e));
	}
	
	public Calendar getStart(int dayOfWeek){
		return startTimes.get(dayOfWeek).toCalendar();
	}
	public Calendar getEnd(int dayOfWeek){
		return endTimes.get(dayOfWeek).toCalendar();
	}
	
	public void setUnavailable(int dayOfWeek){
		startTimes.remove(dayOfWeek);
		endTimes.remove(dayOfWeek);
	}
	
	public boolean hasNoAvailability(int dayOfWeek){
		return !startTimes.containsKey(dayOfWeek) || !endTimes.containsKey(dayOfWeek);
	}
	
	public boolean isAvailable(int dayOfWeek, Calendar cc){
		if(hasNoAvailability(dayOfWeek))
			return true;
		Time check = new Time(cc);
		return startTimes.get(dayOfWeek).compareTo(check) <= 0 && endTimes.get(dayOfWeek).compareTo(check) >= 0;
	}
	
	private class Time implements Comparable<Time>, Serializable{
		private final int hours;
		private final int minutes;
		Time(Calendar c){
			minutes = c.get(Calendar.MINUTE);
			hours = c.get(Calendar.HOUR) + ((c.get(Calendar.AM_PM)==Calendar.PM)?12:0);
		}
		
		@Override
		public int compareTo(Time other){
			if(this.hours*60 + this.minutes > other.getHours()*60 + other.getMinutes())
				return 1;
			else if(this.hours*60 + this.minutes < other.getHours()*60 + other.getMinutes())
				return -1;
			return 0;
		}
		
		public int getHours(){
			return hours;
		}
		public int getMinutes(){
			return minutes;
		}
		
		public Calendar toCalendar(){
			Calendar c = CalendarFactory.getDefaultCalendar();
			c.set(Calendar.MINUTE, minutes);
			if(hours>=12){
				c.set(Calendar.AM_PM, Calendar.PM);
				c.set(Calendar.HOUR, hours -12);
			}
			else{
				c.set(Calendar.AM_PM, Calendar.AM);
				c.set(Calendar.HOUR, hours);
			}
			return c;
		}
	}
}
