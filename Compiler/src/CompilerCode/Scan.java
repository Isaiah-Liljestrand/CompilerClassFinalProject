package CompilerCode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * The Scanner Class, creates a REGEX list of entities in the input stream
 * Named Scan as Scanner already exists in java
 */
public class Scan {
	protected List<Token> tokens;
	
	/**
	 * Main and only constructor for the scan class
	 * @param file filename to be passed in
	 */
	Scan(List<String> file) {
		tokens = tokenize(file);
	}
	
	
	public void printTokens() {
		for (Token tok : this.tokens) {
			System.out.println(tok.token + "\t" + tok.type.toString());
		}
	}
	
	/**
	 * The Pattern List for this Scanner
	 */
	private static Pattern REGEX = Pattern.compile(buildRegularExpression());

	/**
	 * Takes a string of text and splits it into tokens. Each possible token is defined using the global variable REGEX.
	 * @param text a string of text to scan
	 * @return A List of strings containing each found token
	 */
	private static List<Token> tokenize(List<String> text) {
		//The basic logic of this function is to slowly build up tokens character by character.
		//If the current token plus an additional character is still valid according to regular expressions, then the token is still valid.
		//If the current token plus the new character is not valid, then the current token should be added to the list of valid tokens.
		List<Token> tokens = new ArrayList<Token>(); //List to return
		String tokenString;
		boolean isComment = false;
		Pattern commentRegex = Pattern.compile("/\\*");
		char character;
		int i, j;
		int lineNumber = 0;
		//Loop through every character in the string
		for(String line : text) {
			lineNumber++;
			i = 0;
			if(isComment) {
				for(i = 0; i < line.length(); i++) {
					if(line.charAt(i) == '*' && line.charAt(i + 1) == '/') {
						i += 2;
						isComment = false;
						break;
					}
				}
			}
			
			if(!isComment) {
				for (; i < line.length(); i++) {
					character = line.charAt(i);
					if(!isValidCharacter(character)) {
						ErrorHandler.addError("Chraracter " + character + " not recognized");
					} else if(!(character == ' ') && !(character == '\t')) {
						for(j = line.length(); j > i; j--) {
							tokenString = line.substring(i, j);
							if(stringMatchesToken(tokenString)) {
								if(commentRegex.matcher(tokenString).matches()) {
									isComment = true;
									break;
								}
								tokens.add(new Token(tokenString, lineNumber));
								break;
							}
						}
						if(j == i) {
							ErrorHandler.addError("Section " + line.substring(i, line.length()) + " not recognized");
							break;
						}
						i = j - 1;
						if(isComment) {
							break;
						}
					}
				}
			}
		}
		if(isComment) {
			ErrorHandler.addError("Comment started but not finished");
		}
		removeComments(tokens);
		return tokens;
	}
	
	/**
	 * Removes all comments from allocated tokens
	 * @param tokens all tokens parsed from the scannar
	 */
	private static void removeComments(List<Token> tokens) {
		Pattern commentRegex = Pattern.compile("//.*|/\\*.*\\*/");
		List<Token> t = new ArrayList<Token>();
		for(Token tok : tokens) {
			if(commentRegex.matcher(tok.token).matches()) {
				t.add(tok);
			}
		}
		tokens.removeAll(t);
	}
	
	/**
	 * Checks if any unsupported characters are in the string
	 * @param character character in code
	 * @return true if character is supported. False if not
	 */
	private static boolean isValidCharacter(char character) {
		switch(character) {
		case '@':
		case '#':
		case '$':
		case '[':
		case ']':
		case '\"':
		case '.':
		case '\\':
			return false;
		}
		return true;
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
	 * Builds a regular expression to separate out tokens
	 * @return string usable by Pattern.compile()
	 */
	private static String buildRegularExpression() {
		String string = "\\{|\\}|"; 				// { and }
		string = string + "\\(|\\)|"; 				// ( and )
		string = string + "\\d+|";					// Accepts any digits
		string = string + "[a-zA-Z]\\w*|"; 			// accepts any expressions that only use letters and digits that start with a letter
		string = string + "/|\\*|"; 				// / and *
		string = string + "\\+|\\-|"; 				// + and -
		string = string + "\\^|\\||"; 				// ^ and |
		string = string + "&|%|"; 					// & and %
		string = string + "\\!|,|"; 				// ! and ,
		string = string + ";|\\=|";      			// ; and =
		string = string + ":|";						// :
		string = string + "\'[a-zA-Z]\'|";			// 'character'
		string = string + "0x[a-f0-9]+|"; 			// Accepts hex input
		string = string + "/\\*.*\\*/|";			// Accepts any comments of the form /*......*/
		string = string + "//.*|";					// Accepts any comments or the form //......
		string = string + "/\\*|";					// Accepts /*
		string = string + "\\+\\+|"; 				// ++
		string = string + "\\-\\-|"; 				// --
		string = string + "\\-\\=|";  				// -=
		string = string + "\\+\\=|";  				// +=
		string = string + "\\*\\=|"; 				// *=
		string = string + "\\/\\=|";  				// /=
		string = string + "\\!\\=|";				// !=
		string = string + "\\=\\=|";				// ==
		string = string + "\\&\\&|";				// &&
		string = string + "\\|\\|";					// ||
		return string;
	}
}
