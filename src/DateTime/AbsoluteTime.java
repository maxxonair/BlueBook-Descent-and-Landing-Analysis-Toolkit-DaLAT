package DateTime;


public class AbsoluteTime {
	
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	private int second;
	private int millisecond;
	
	String utcString ="";
	long J2000;
	
	public AbsoluteTime(int year, int month, int day, int hour, int minute, int second, int millisecond) {
		this.year = year; 
		this.month=month;
		this.day=day;
		this.hour=hour;
		this.minute=minute;
		this.second=second;
		this.millisecond=millisecond;
		
		
		utcString = createUniformUTCString(year,  month,  day,  hour,  minute,  second,  millisecond);
		J2000 = TimeUtils.UTC2J2000(utcString);
		
		
	}
	
	
	public static void main(String[] args) {
		
		AbsoluteTime myTime = new AbsoluteTime(2019, 2, 5, 17, 56, 0, 0);
		
		System.out.println(myTime.getUtcString());
		System.out.println(myTime.getJ2000());
		
	}
	
	
	private String createUniformUTCString(int year, int month, int day, int hour, int minute, int second, int millisecond) {
		String utcString = "";
		if(numberOfDigits(year)==4) {
			utcString=year+":";
		} else {
			// draw error : year format does not fit
		}
		
		if(numberOfDigits(month)==2 && month<=12 && month>0) {
			utcString=utcString+month+":";
		} else if(numberOfDigits(month)==1 && month>0) {
			utcString=utcString+"0"+month+":";
		} else {
			// draw error : month format does not fit
		}
		
		if(numberOfDigits(day)==2 && day<=31 && day>0) {
			utcString=utcString+day+":";
		} else if(numberOfDigits(day)==1 && day>=0) {
			utcString=utcString+"0"+day+" ";
		} else {
			// draw error : day format does not fit
		}
		
		if(numberOfDigits(hour)==2 && hour<=24 && hour>=0) {
			utcString=utcString+hour+":";
		} else if(numberOfDigits(hour)==1 && hour>=0) {
			utcString=utcString+"0"+hour+":";
		} else {
			// draw error : hour format does not fit
		}
		
		if(numberOfDigits(minute)==2 && minute<=60 && minute>=0) {
			utcString=utcString+minute+":";
		} else if(numberOfDigits(minute)==1 && minute>=0) {
			utcString=utcString+"0"+minute+":";
		} else {
			// draw error : minute format does not fit
		}
		
		if(numberOfDigits(second)==2 && second<=60 && second>=0) {
			utcString=utcString+second;
		} else if(numberOfDigits(second)==1 && second>=0) {
			utcString=utcString+"0"+second;
		} else {
			// draw error : second format does not fit
		}
		return utcString;
	}
	
	private int numberOfDigits(int input) {
		return (int) String.valueOf(input).length();
	}


	public int getYear() {
		return year;
	}


	public int getMonth() {
		return month;
	}


	public int getDay() {
		return day;
	}


	public int getHour() {
		return hour;
	}


	public int getMinute() {
		return minute;
	}


	public int getSecond() {
		return second;
	}


	public int getMillisecond() {
		return millisecond;
	}


	public String getUtcString() {
		return utcString;
	}


	public long getJ2000() {
		return J2000;
	}


	public void setYear(int year) {
		this.year = year;
		utcString = createUniformUTCString(year,  month,  day,  hour,  minute,  second,  millisecond);
		J2000 = TimeUtils.UTC2J2000(utcString);
	}


	public void setMonth(int month) {
		this.month = month;
		utcString = createUniformUTCString(year,  month,  day,  hour,  minute,  second,  millisecond);
		J2000 = TimeUtils.UTC2J2000(utcString);
	}


	public void setDay(int day) {
		this.day = day;
		utcString = createUniformUTCString(year,  month,  day,  hour,  minute,  second,  millisecond);
		J2000 = TimeUtils.UTC2J2000(utcString);
	}


	public void setHour(int hour) {
		this.hour = hour;
		utcString = createUniformUTCString(year,  month,  day,  hour,  minute,  second,  millisecond);
		J2000 = TimeUtils.UTC2J2000(utcString);
	}


	public void setMinute(int minute) {
		this.minute = minute;
		utcString = createUniformUTCString(year,  month,  day,  hour,  minute,  second,  millisecond);
		J2000 = TimeUtils.UTC2J2000(utcString);
	}


	public void setSecond(int second) {
		this.second = second;
		utcString = createUniformUTCString(year,  month,  day,  hour,  minute,  second,  millisecond);
		J2000 = TimeUtils.UTC2J2000(utcString);
	}


	public void setMillisecond(int millisecond) {
		this.millisecond = millisecond;
		utcString = createUniformUTCString(year,  month,  day,  hour,  minute,  second,  millisecond);
		J2000 = TimeUtils.UTC2J2000(utcString);
	}
	
	

}
