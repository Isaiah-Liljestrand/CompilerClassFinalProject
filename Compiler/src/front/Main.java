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
	public static Pattern REGEX = Pattern.compile("(\\w+)|\\(|\\)|\\{|\\}");
	
	public static void main(String[] args) {
		boolean countcharacters = false;
		boolean tokenize = false;
		String filename = null;
		//Main test = new Main();
		for (String arg: args) {
			if (arg.equals("-s"))
				countcharacters = true;
			if (arg == args[args.length - 1])
				filename = arg;
		}
		
		Scanner fname = new Scanner(System.in);
		System.out.print("Input name of file: ");
		filename = fname.nextLine().trim(); 
		
		if (filename != null)
		{
			String Bigstring = "";
			List<String> lines = readFile(filename);
			//if (countcharacters) {
			System.out.printf("Number of characters in file = %d\n", countChars(lines));
			//}
			
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
			//String list[] = new String[1024];
			//list = test.tokenize("hello (the) world 3+ (4)"); //very rudementry tokenizer testing (splits on spaces and ()
			List<String> tokens = tokenizeNew("hello (the) world 32+ (4)");
			for (String tok : tokens) {
				System.out.println(tok);
			}
			/*for(int i = 0; i < 10; i++){
				System.out.println(list[i]);
			}*/
		}
	}
	
	private static List<String> tokenizeNew(String text) {
		List<String> tokens = new ArrayList<String>();
		String currentToken = "";
		for (char character : text.toCharArray()) {
			String currentWithNewChar = currentToken + character;
			boolean validToken = false;
			Matcher m = REGEX.matcher(currentWithNewChar);
			if (m.matches()) {
				currentToken = currentWithNewChar;
			}
			else {
				Matcher test = REGEX.matcher(currentToken);
				if (test.matches()) {
					tokens.add(currentToken);
				}
				currentToken = String.valueOf(character);
			}
		}
		Matcher test = REGEX.matcher(currentToken);
		if (test.matches()) {
			tokens.add(currentToken);
		}
		return tokens;
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

	/*private int count(String check){
		int i = 0, cnt = 0;
		for(i = 0; i < check.length(); i++){
			cnt++;
		}
		return cnt;
	}*/
}
