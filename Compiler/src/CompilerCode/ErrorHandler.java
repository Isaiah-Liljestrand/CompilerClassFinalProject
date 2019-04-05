package CompilerCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles errors
 * checks, prints, and returns errors
 * Should be called from main after every  distinct step of the compilation
 * @author Isaiah-Liljestrand
 */
public class ErrorHandler {
	protected static List<String> errors;
	
	public static void checkInit() {
		if (errors == null) {
			errors = new ArrayList<String>();
		}
	}
	
	/**
	 * Tells the caller whether any errors have been reported yet
	 * @return true if errors exist, false if no errors have been reported
	 */
	public static boolean errorsExist() {
		checkInit();
		if(errors.size() == 0) {
			return false;
		}
		return true;
	}
	
	/**
	 * Adds an error to the current list of reported errors
	 * @param errorString error string to be reported
	 */
	
	public static void addError(String errorString) {
		checkInit();
		errors.add(errorString);
	}
	
	/**
	 * Prints all errors that have been reported up to this point in compilation
	 * @param errorLocation should report which part of the compilation where there was failure
	 */
	public static void printStrings(String errorLocation) {
		checkInit();
		int i = 1;
		for(String error : errors) {
			System.out.println("Error#:" + i + " Location:" + errorLocation + " ErrorMessage:" + error);
			i++;
		}
	}
}