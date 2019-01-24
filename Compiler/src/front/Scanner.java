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
		Scanner test = new Scanner();
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
		else{
			System.out.println("file not found");
			String list[] = new String[1024];
			list = test.tokenize("hello, (I am the) world"); //very rudementry tokenizer testing (splits on spaces and ()
			for(int i = 0; i < 5; i++){
				System.out.println(list[i]);
			}
		}
	}
	
	private String[] tokenize(String in){
		String list[] = new String[1024];
		int j = 0;
		char tmp;
		for(int i = 0; i < in.length(); i++){
			tmp = in.charAt(i);
			System.out.println(tmp);
			switch (tmp){
			case '(':
				for (int k = 1; k < in.length(); k++){ //looking for )
					tmp = in.charAt(k);
					if(tmp == ')'){
						list[j] = in.substring(0, k + 1);
						if(in.charAt(k + 1) == ' '){ //might not be a space after )
							in = in.substring(k + 2);
						}
						else{
							in = in.substring(k + 1);
						}
						j++;
						i = -1; //-1 to off set the loop's ++
						break;
					}
				}
				break;
			case ' ': //found end of a word
				list[j] = in.substring(0, i); //grab word, minus spaces
				in = in.substring(i + 1); //remove the word from the input string
				j++; //index the list of tokens
				i = -1; //reseting the loop as in no longer contains the prior word, -1 to off set the loop's ++
				break;		
			}
		}
		list[j] = in;
		return list;
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
