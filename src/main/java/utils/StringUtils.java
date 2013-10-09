package utils;

public class StringUtils {

	public static boolean containsSpaces(String string) {
		return string.indexOf(' ') > 0;
	}

	public static boolean containsAllDigits(String string) {
		for(int i = 0; i < string.length(); i++) {
			char cur = string.charAt(i);
			if(!Character.isDigit(cur)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNumeric(String text) {
		try {
			if(text.isEmpty()) {
				return false;
			}
			Double.parseDouble(text);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static final boolean containsOnlyCharacters(String text) {
		for(int i = 0; i < text.length(); i++) {
			char curChar = text.charAt(i);
			if(!Character.isLetter(curChar)){
				return false;
			}
		}
		return true;
	}
	
	public static boolean containsSpecialCharacters(String text) {
		for(int i = 0; i < text.length(); i++) {
			char curChar = text.charAt(i);
			if(! (Character.isLetter(curChar) || Character.isDigit(curChar))) {
				return true;
			}
		}
		
		return false;
	}
}
