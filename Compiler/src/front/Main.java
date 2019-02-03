package front;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	public static Pattern REGEX = Pattern.compile(buildRegularExpression());
	
	public static void main(String[] args) {
		boolean tokenize = false;
		String filename = null;
		//Check for command line arguments
		for (String arg: args) {
			if (arg == args[args.length - 1]) {
				if(args.length > 1) {
					filename = arg;
				}
			}
		}
		
		//To be removed when command line arguments are figured out
		Scanner fname = new Scanner(System.in);
		System.out.print("Input name of file: ");
		filename = fname.nextLine().trim();
		fname.close();
		
		if (filename != null)
		{
			String bigString = "";
			List<String> lines = readFile(filename);
			System.out.printf("Number of characters in file = %d\n", countChars(lines));
			
			System.out.println(filename);
			for (String line : lines) {
				bigString = bigString + line;
			}
			List<String> tokens = tokenize(bigString);
			for (String tok : tokens) {
				System.out.println(tok);
			}
		}
		else{
			System.out.println("file not found");
			List<Token> tokens = tokenize("hello (the) world 32+ (4)");
			for (Token tok : tokens) {
				System.out.println(tok.token);
			}
		}
	}
	
	/**
	 * Takes a string of text and splits it into tokens. Each possible token is defined using the global variable REGEX.
	 * @param text a string of text to scan
	 * @return A List of strings containing each found token
	 */
	private static List<Token> tokenize(String text) {
		//The basic logic of this function is to slowly build up tokens character by character.
		//If the current token plus an additional character is still valid according to regular expressions, then the token is still valid.
		//If the current token plus the new character is not valid, then the current token should be added to the list of valid tokens.
		List<Token> tokens = new ArrayList<Token>(); //List to return
		String currentToken = "";
		Token token = new Token();
		//Loop through every character in the string
		for (char character : text.toCharArray()) {
			String currentWithNewChar = currentToken + character; //Testing current token with appended new character
			if (stringMatchesToken(currentWithNewChar) || (currentWithNewChar.charAt(0) == '\"' && !currentWithNewChar.substring(1).contains("\""))) { //If any regular expression matches, then the current token has the new character appended
				currentToken = currentWithNewChar;
			}
			else { //If no regular expression matches, then the current token is finished.
				//If the current token is valid, add it to our list of tokens
				if (stringMatchesToken(currentToken)) {
					token.setToken(currentToken);
					token.addTokenIdentity();
					tokens.add(token);
				}
				//Reset current token to be the newest character
				currentToken = String.valueOf(character);
			}
		}
		//At end of loop check if the current token is valid. If so, add it to our list of tokens.
		if (stringMatchesToken(currentToken)) {
			token.setToken(currentToken);
			token.addTokenIdentity();
			tokens.add(token);
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
	private static List<String> readFile(String filename)
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
	/**
	 * Counts the number of characters in a given list of strings
	 * @param lines strings being passed in
	 * @return number of characters
	 */
	private static int countChars(List<String> lines) {
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
		String string = "\\{|\\}|"; 				// { and }
		string = string + "\\[|\\]|"; 				// [ and ]
		string = string + "\\(|\\)|"; 				// ( and )
		string = string + "[a-zA-Z]\\w*|"; 			// accepts any expressions that only use letters and digits that start with a letter
		string = string + "\\d++|"; 				// accepts any length of number
		string = string + "/|\\*|"; 				// / and *
		string = string + "\\+|\\-|"; 				// + and -
		string = string + "\\^|\\|"; 				// ^ and |
		string = string + "&|%|"; 					// & and %
		string = string + "\\!|"; 					// !
		string = string + ";|\\=|";      			// ; and =
		string = string + "0x[a-f0-9]++"; 			// Accepts hex input
		string = string + "\".*\"|";  				// Accepts string literals
		string = string + "\\+\\+|"; 				// ++
		string = string + "\\-\\-|"; 				// --
		string = string + "\\-\\=";  				// -=
		string = string + "\\+\\=";  				// +=
		string = string + "\\*\\="; 				// *=
		string = string + "\\/\\=";  				// /=
		string = string + "\\=\\=";					// ==
		string = string + "\\&\\&";					// &&
		string = string + "\\|\\|";					// ||
		return string;
	}
}
