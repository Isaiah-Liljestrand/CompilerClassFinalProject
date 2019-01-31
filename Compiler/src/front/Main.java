package front;


import java.util.Scanner;

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
}
	
	

