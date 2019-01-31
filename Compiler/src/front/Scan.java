package front;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * The Scanner Class, creates a REGEX list of entities in the input stream
 * Named Scan as Scanner already exists in java
 */
public class Scan {
	
	List<String> tokens;
	
	/**
	 * Basic Constructor
	 */
	Scan(){
		System.out.println("file not found");
		tokens = tokenize("hello (the) world 32+ (4)");
		PrintOut();
	}
	
	/**
	 * Constructor with file name as an argument
	 */
	Scan(String Fname){
		String bigString = "";
		List<String> lines = readFile(Fname);
		System.out.printf("Number of characters in file = %d\n", countChars(lines));
		
		System.out.println(Fname);
		for (String line : lines) {
			bigString = bigString + line;
		}
		tokens = tokenize(bigString);
		PrintOut();
	}
	
	public void PrintOut(){
		for (String tok : tokens) {
			System.out.println(tok);
		}
	}
	
	/**
	 * The Pattern List for this Scanner
	 */
	public static Pattern REGEX = Pattern.compile(buildRegularExpression());

	/**
	 * Takes a string of text and splits it into tokens. Each possible token is defined using the global variable REGEX.
	 * @param text a string of text to scan
	 * @return A List of strings containing each found token
	 */
	public static List<String> tokenize(String text) {
		//The basic logic of this function is to slowly build up tokens character by character.
		//If the current token plus an additional character is still valid according to regular expressions, then the token is still valid.
		//If the current token plus the new character is not valid, then the current token should be added to the list of valid tokens.
		List<String> tokens = new ArrayList<String>(); //List to return
		String currentToken = "";
		//Loop through every character in the string
		for (char character : text.toCharArray()) {
			String currentWithNewChar = currentToken + character; //Testing current token with appended new character
			if (stringMatchesToken(currentWithNewChar)) { //If any regular expression matches, then the current token has the new character appended
				currentToken = currentWithNewChar;
			}
			else { //If no regular expression matches, then the current token is finished.
				//If the current token is valid, add it to our list of tokens
				if (stringMatchesToken(currentToken)) {
					tokens.add(currentToken);
				}
				//Reset current token to be the newest character
				currentToken = String.valueOf(character);
			}
		}
		//At end of loop check if the current token is valid. If so, add it to our list of tokens.
		if (stringMatchesToken(currentToken)) {
			tokens.add(currentToken);
		}
		return tokens;
	}
	
	/**
	 * Checks if a string matches any token in the global variable REGEX
	 * @param value The string to check
	 * @return A boolean. True if there is a match. False otherwise
	 */
	private static boolean stringMatchesToken(String value) {
		Matcher test = REGEX.matcher(value); //Checks for matches using the global REGEX variable.
		return test.matches();
	}
	
	/**
	 * Read in file and return a list of strings for each line
	 * @param filename The name of the file to open.
	 * @return a list of strings
	 */
	public static List<String> readFile(String filename)
	{
		List<String> lines = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       lines.add(line);
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lines;
	}
	
	public static int countChars(List<String> lines) {
		int count = 0;
		for (String line: lines)
		{
			count += line.length();
		}
		return count;
	}
	
	/**
	 * Builds a regular expression to separate out tokens
	 * @return string usable by Pattern.compile()
	 */
	private static String buildRegularExpression() {
		String string = "\\{|\\}|"; // { and }
		string = string + "\\[|\\]|"; // [ and ]
		string = string + "\\(|\\)|"; // ( and )
		string = string + "[a-zA-Z]\\w*|"; // accepts any expressions that only use letters and digits that start with a letter
		string = string + "\\d\\d*|"; // accepts any length of number
		string = string + "/|\\*|"; // / and *
		string = string + "\\+|\\-|"; // + and -
		string = string + "\\^|\\|"; // ^ and |
		string = string + "&|%|"; // & and %
		string = string + "\\!|\\?|"; // ! and ?
		string = string + ";|\\=|";      //; and =
		string = string + "0x[a-f0-9][a-f0-9]*"; // accepts hexidecimal input
		return string;
	}
}
