package DateTime;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class TimeUtils {
	
	private static long DiffUnixEpochToJ2000inSeconds = 946684800;
	static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss", Locale.ENGLISH);
	
	public static long UTC2J2000(String UTC) {
		try {
		LocalDateTime dateTime = LocalDateTime.parse(UTC, formatter);
			long result = dateTime.toEpochSecond(ZoneOffset.UTC);
			return result-DiffUnixEpochToJ2000inSeconds;
		} catch ( DateTimeParseException e) {
			//System.out.println("Error/TimeUtils/UTC2J2000: Input String could not be parsed.");
			return 0;
		}
	}
	
	public static String J20002UTC(long J2000) {
		long input =  J2000 + DiffUnixEpochToJ2000inSeconds;
		LocalDateTime dateTime = LocalDateTime.ofEpochSecond(input , 0, ZoneOffset.UTC);
		return dateTime.format(formatter) ;
	}

}
