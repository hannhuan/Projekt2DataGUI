package KrustyKookies;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JTextField;

public class CheckInput {

	public static String checkDate(String date) {
		String s = "invalid";
		if (date.length() == 10) {

			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date d = new Date();
			String realDate = sdf.format(d);
			if (realDate.compareTo(date) < 0) {
				s = "valid";
			}
		}

		return s;

	}

	public static String checkNbr(String text) {
		String s = "invalid";
		try {
			int i = Integer.parseInt(text);
			if (i >= 0 && i < 5000) {
				s = "valid";
			}
		} catch (NumberFormatException e) {
		}
		return s;
	}

	public static String checkChoose(String customer, String cookie) {
		String s = "valid";
		if (customer == null || cookie == null
				|| cookie.equals("Choose Cookie")
				|| customer.equals("Choose Customer")) {
			s = "invalid";
		}
		return s;
	}

	public static boolean checkPalletID(String text) {
		// kolla siffror, och längd och att den finns
		if (text.length() == 10) {
			for (int i = 0; i < text.length(); i++) {
				int value = Character.getNumericValue(text.charAt(i));
				if (value < 0 || value > 9) {
					return false;
				}
			}
			return true;

		}
		return false;
	}

	public static boolean checkSearchDate(String date) {
		if (date.trim().length() == 16) {
			StringBuilder year = new StringBuilder();
			StringBuilder month = new StringBuilder();
			StringBuilder day = new StringBuilder();
			StringBuilder hour = new StringBuilder();
			StringBuilder min = new StringBuilder();
			for (int i = 0; i < date.length(); i++) {
				if (checkInt(i, date) == false){ 
					String s = String.valueOf(date.charAt(i));
					if( s.equals("-") && (i==4 || i==7)) {
						
					} else if ( s.equals(" ") && i==10){
						
					} else if ( s.equals(":") && i==13){
						
					} else {
						return false;
					}
				
				} else {
					if (i < 4) {
						year.append(date.charAt(i));
					} else if (i > 4 && i < 7) {
						month.append(date.charAt(i));
					} else if (i > 7 && i < 10) {
						day.append(date.charAt(i));
					} else if (i > 10 && i < 13) {
						hour.append(date.charAt(i));
					} else if (i > 13) {
						min.append(date.charAt(i));
					} 

					}
				}
				
				try {
					LocalTime.of(Integer.parseInt(hour.toString()),
							Integer.parseInt(min.toString()));
					LocalDate.of(Integer.parseInt(year.toString()),
							Integer.parseInt(month.toString()),
							Integer.parseInt(day.toString()));
				} catch (DateTimeException e) {
					return false;
				}
	
			

			return true;
		} else 
			return false;


	}

	private static boolean checkInt(int i, String date) {
		int value = Character.getNumericValue(date.charAt(i));
		if (value < 0 || value > 9) {
			return false;
		}
		return true;
	}


}
