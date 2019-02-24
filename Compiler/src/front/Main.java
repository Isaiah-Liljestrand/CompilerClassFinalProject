package front;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		boolean tokenize = false;
		boolean abstractSyntaxTree = true; //set true for testing
		boolean PrintParse = true; //assuming true for testing
		String filename = null;
		Scan scanner;
		//Check for command line arguments
		for (String arg: args) {
			if (arg.compareTo("-t") == 0) {
				tokenize = true;
			}
			
			if (arg.compareTo("-a") == 0) {
				abstractSyntaxTree = true;
			}
			
			if (arg == args[args.length - 1]) {
				if(args.length > 1) {
					filename = arg;
				}
			}
		}
		
		if (filename == null) {
			Scanner fname = new Scanner(System.in);
			System.out.print("Input name of file: ");
			filename = fname.nextLine().trim();
			fname.close();
			tokenize = true;
		}

		
		List<String> lines = readFile(filename);
		scanner = new Scan(lines);
		Grammar grammar = null;
		
		if (tokenize) {
			System.out.println("Scanner:");
			scanner.printTokens();
		}
		
		//This is a parse tree not an abstract syntax tree
		grammar = new Grammar(scanner.getTokens());
		
		//if(!Ptree.gotoChecker(grammar.root)) {
		//	System.out.println("goto statements do not match up correctly");
		//}
		
		if (abstractSyntaxTree) {
			//grammar = new Grammar(scanner.getTokens());
			
		}
		if (PrintParse && grammar != null){
			System.out.print("check");
			grammar.printTree();
		}
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
}

