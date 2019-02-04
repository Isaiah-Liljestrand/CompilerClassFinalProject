package front;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;

public class Main {
	
	
	public static void main(String[] args) {
		boolean tokenize = false;
		String filename = null;
		Scan test;
		//Check for command line arguments
		for (String arg: args) {
			if (arg == args[args.length - 1]) {
				if(args.length > 1) {
					filename = arg;
				}
			}
		}
		filename = "Testfile.txt";
		//To be removed when command line arguments are figured out
		Scanner fname = new Scanner(System.in);
		System.out.print("Input name of file: ");
		filename = fname.nextLine().trim();
		fname.close();
		
		if (filename != null)
		{
			test = new Scan(filename);
			

		}
		else{
			test = new Scan();
		}
	}
	


			/*System.out.println(filename);
			for (String line : lines) {
				bigString = bigString + line;
			}
			List<Token> tokens = tokenize(bigString);
			for (Token tok : tokens) {
				System.out.println(tok.token);
			}
		}
		else{
			System.out.println("file not found");
			List<Token> tokens = tokenize("hello (the) world 32+ (4)");
			for (Token tok : tokens) {
				System.out.println(tok.token);
			}
		}
	}*/

	
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

