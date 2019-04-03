package front;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Main {
	public static void main(String[] args) {
		boolean tokenize = true;
		boolean PrintParse = true; //assuming true for testing
		String filename = null;
		Scan scanner;
		Grammar grammar;
		SymbolTable symTable;
		//Check for command line arguments
		for (String arg: args) {
			if (arg.compareTo("-t") == 0) {
				tokenize = true;
			}
			
			if (arg == args[args.length - 1]) {
				if(args.length > 1) {
					filename = arg;
				}
			}
		}
		
		/**if (filename == null) {
			Scanner fname = new Scanner(System.in);
			System.out.print("Input name of file: ");
			filename = fname.nextLine().trim();
			fname.close();
			tokenize = true;
		}*/
		filename = "Testfile.txt";
		
		List<String> lines = readFile(filename);
		scanner = new Scan(lines);
		
		if(ErrorHandler.errorsExist()) {
			ErrorHandler.printStrings("Scanner");
			return;
		}
		
		if (tokenize) {
			System.out.println("Scanner:");
			scanner.printTokens();
		}
		
		grammar = new Grammar(scanner.tokens);
		if(ErrorHandler.errorsExist()) {
			ErrorHandler.printStrings("Parse Tree Creation");
			return;
		}
		
		Ptree.gotoChecker(grammar.root);
		if(ErrorHandler.errorsExist()) {
			ErrorHandler.printStrings("Goto Checker");
			return;
		}
		
		if (PrintParse && grammar != null){
			//System.out.print("check");
			grammar.printTree();
		}
		
		symTable = new SymbolTable();
		SymbolTable.buildDeclarationTable(grammar.root, symTable);
		symTable.printSymbolTable();
		if(ErrorHandler.errorsExist()) {
			ErrorHandler.printStrings("Symbol Table Creation");
			return;
		}
		
		System.out.println();
		IRcreation.createIR(grammar.root);
		IR.printIR();
		if(ErrorHandler.errorsExist()) {
			ErrorHandler.printStrings("IR creation");
			return;
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

