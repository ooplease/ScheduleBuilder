package schedulebuilder.main;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarFactory{
	
	public static Calendar getDefaultCalendar(){
		GregorianCalendar cal = new GregorianCalendar(1970,Calendar.JANUARY,1,0,0,0);
		return cal;
	}
	
	public static Calendar getDefaultCalendarAtTime(Date time){
		GregorianCalendar cal = new GregorianCalendar(1970,Calendar.JANUARY,1,0,0,0);
		cal.set(Calendar.HOUR_OF_DAY, time.getHours());
		cal.set(Calendar.MINUTE, time.getMinutes());
		return cal;
	}
	
	public static Calendar getDefaultCalendarAtDate(Date date){
		Calendar startDate = CalendarFactory.getDefaultCalendar();
		Calendar temp = new GregorianCalendar();
		temp.setTime(date);
		startDate.set(Calendar.YEAR, temp.get(Calendar.YEAR));
		startDate.set(Calendar.MONTH, temp.get(Calendar.MONTH));
		startDate.set(Calendar.DATE, temp.get(Calendar.DATE));
		return startDate;
	}

}
