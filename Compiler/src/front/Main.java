package front;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		boolean countcharacters = false;
		boolean tokenize = false;
		String filename = null;
		Main test = new Main();
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
			list = test.tokenize("hello (the) world 3+ (4)"); //very rudementry tokenizer testing (splits on spaces and ()
			for(int i = 0; i < 10; i++){
				System.out.println(list[i]);
			}
		}
	}
	
	
	private String[] tokenize(String in){
		String list[] = new String[1024];
		int j = 0;
		char tmp;
		in = in.trim();
		for(int i = 0; i < in.length(); i++){
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
