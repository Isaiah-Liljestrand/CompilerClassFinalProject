package front;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Scanner {

	public static void main(String[] args) {
		boolean countcharacters = false;
		boolean tokenize = false;
		String filename = null;
		for (String arg: args) {
			if (arg.equals("-s"))
				countcharacters = true;
			if (arg == args[args.length - 1])
				filename = arg;
		}
		
		if (filename != null)
		{
			List<String> lines = read_file(filename);
			if (countcharacters) {
				System.out.printf("Number of characters in file = %d\n", count_chars(lines));
			}
		}
	}
	
	private static List<String> read_file(String filename)
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
	
	private static int count_chars(List<String> lines) {
		int count = 0;
		for (String line: lines)
		{
			count += line.length();
		}
		return count;
	}

	private int count(String check){
		int i = 0, cnt = 0;
		for(i = 0; i < check.length(); i++){
			cnt++;
		}
		return cnt;
	}

}
