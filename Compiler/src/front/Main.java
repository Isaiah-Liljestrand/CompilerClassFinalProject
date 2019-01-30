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
		//Scanner fname = new Scanner(System.in);
		//System.out.print("Input name of file: ");
		//filename = fname.nextLine().trim();
		
		if (filename != null)
		{
			String Bigstring = null;
			List<String> lines = readFile(filename);
			System.out.printf("Number of characters in file = %d\n", countChars(lines));
			
			System.out.printf(filename);
			for (String line : lines) {
				Bigstring = Bigstring + line;
			}
			List<String> tokens = tokenizeNew(Bigstring);
			for (String tok : tokens) {
				System.out.println(tok);
			}
		}
		else{
			System.out.println("file not found");
			List<String> tokens = tokenizeNew("hello (the) world 32+ (4)");
			for (String tok : tokens) {
				System.out.println(tok);
			}
		}
	}
	
	/**
	 * Takes a string of text and splits it into tokens. Each possible token is defined using the global variable REGEX.
	 * @param text a string of text to scan
	 * @return A List of strings containing each found token
	 */
	private static List<String> tokenizeNew(String text) {
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
	
	private static String[] tokenize(String in) {
		String list[] = new String[1024];
		int j = 0;
		char tmp;
		in = in.trim();
		for(int i = 0; i < in.length(); i++) {
			in=in.trim();
			tmp = in.charAt(i);
			switch (tmp){
			case ' ':
			case '(':
			case '{':
				//System.out.println(tmp);
				list[j] = in.substring(0, i+1); //grab word
				in = in.substring(i+1); //remove the word from the input string
				j++; //index the list of tokens
				i = -1; //reseting the loop as in no longer contains the prior word, -1 to off set the loop's ++
				break;
			case ')':
			case '}':
			case ';':
			case '=':
			case '+':
			case '-':
			case '*':
			case '/':
				//System.out.println(tmp);
				list[j] = in.substring(0, i); //grab word
				list[j+1] = in.substring(i, i+1);
				in = in.substring(i+1); //remove the word from the input string
				j = j + 2; //index the list of tokens
				i = -1; //reseting the loop as in no longer contains the prior word, -1 to off set the loop's ++
				break;
			default:
				break;
			}
			//System.out.println(in);
			in = in.trim();
		}
		list[j] = in;
		return list;
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
		string = string + ";";
		return string;
	}
}
