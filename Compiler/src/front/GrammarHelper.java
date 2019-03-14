package front;

import java.util.List;

import front.Token.type_enum;

/**
 * Small functions that aid in grammar operations
 * Mostly verify tokens and then create Ptrees from them
 * @author Isaiah-Liljestrand
 */
public class GrammarHelper {
	
	/**
	 * Tests that the passed in token is a goto keyword
	 * @param token should be goto
	 * @return tree node or null
	 */
	public static Ptree gotoFunction(Token token) {
		if(token.type == type_enum.k_goto) {
			return new Ptree(token);
		}
		return null;
	}
	
	/**
	 * tests if the token passed in is an if keyword
	 * @param token should be if
	 * @return either a tree node or null
	 */
	public static Ptree ifFunction(Token token) {
		if(token.type == type_enum.k_if) {
			return new Ptree(token);
		}
		return null;
	}
	
	/**
	 * checks if the token passed in is the else keyword
	 * @param token should be else
	 * @return Ptree with else token or null
	 */
	public static Ptree elseFunction(Token token) {
		if(token.type == type_enum.k_else) {
			return new Ptree(token);
		}
		return null;
	}
	
	/**
	 * Checks if the token passed in is a while function keyword
	 * @param token should be the while keyword
	 * @return Ptree node containing the keyword or null
	 */
	public static Ptree whileFunction(Token token) {
		if(token.type == type_enum.k_while) {
			return new Ptree(token);
		}
		return null;
	}
	
	/**
	 * Checks if the token passed in is a for function keyword
	 * @param token should be the for keyword
	 * @return Ptree node containing the keyword or null
	 */
	public static Ptree forFunction(Token token) {
		if(token.type == type_enum.k_for) {
			return new Ptree(token);
		}
		return null;
	}
	
	/**
	 * Checks if the token is a constant
	 * @param token should be either a char constant or an int constant
	 * @return Ptree node if the statment evaluates to true
	 */
	public static Ptree constant(Token token) {
		if (token.type == type_enum.number) {
			return new Ptree(token);
		}
		return null;
	}
	
	/**
	 * Checks that the passed in token is a return call
	 * @param token should be a return statement
	 * @return Ptree containing return statement or null
	 */
	public static Ptree returnFunction(Token token) {
		if(token.type == type_enum.k_return) {
			return new Ptree(token);
		}
		return null;
	}
	
	
	/**
	 * Tests if the token passed in is a break keyword
	 * @param token should be break
	 * @return new node or null
	 */
	public static Ptree breakFunction(Token token) {
		if(token.type == type_enum.k_break) {
			return new Ptree(token);
		}
		return null;
	}
	
	
	
	
	

	
	/**
	 * Simply checks if passed in token is an identifier
	 * @param token should be identifier
	 * @return Ptree identifier leaf node or null if not valid
	 */
	public static Ptree identifier(Token token) {
		if(token.type == type_enum.identifier) {
			return new Ptree(token);
		}
		return null;
	}
	
	/**
	 * Checks if the passed in token is an assignment operator of any sort
	 * @param token token to be checked
	 * @return Ptree containing the token if it passes the test
	 */
	public static Ptree assignmentOperator(Token token) {
		switch(token.type) { 
		case additionAssignmentOperator:
		case subtractionAssignmentOperator:
		case multiplicationAssignmentOperator:
		case divisionAssignmentOperator:
		case assignmentOperator:
			return new Ptree(token);
		default:
			return null;
		}
	}
	
	/**
	 * Checks if the token passed in is a logical or operator
	 * @param token to be checked
	 * @return Ptree containing or token or null if invalid
	 */
	public static Ptree logicOr(Token token) {
		if(token.type == type_enum.orLogicOperator) {
			return new Ptree(token);
		}
		return null;
	}
	
	/**
	 * Checks if the token passed in is a logical and operator
	 * @param token to be checked
	 * @return Ptree containing and token or null if input token is invalid
	 */
	public static Ptree logicAnd(Token token) {
		if(token.type == type_enum.andLogicOperator) {
			return new Ptree(token);
		}
		return null;
	}
	
	/**
	 * Checks if the token passed in is a bitwise or operator
	 * @param token to be checked
	 * @return Ptree containing or token or null if input token is invalid
	 */
	public static Ptree bitOr(Token token) {
		if(token.type == type_enum.orOperator) {
			return new Ptree(token);
		}
		return null;
	}
	
	/**
	 * Checks if the token passed in is a bitwise xor operator
	 * @param token to be checked
	 * @return Ptree containing xor token or null if input token is invalid
	 */
	public static Ptree bitXor(Token token) {
		if(token.type == type_enum.xorOperator) {
			return new Ptree(token);
		}
		return null;
	}
	
	/**
	 * Checks if the token passed in is a bitwise and operator
	 * @param token to be checked
	 * @return Ptree containing and token or null if input token is invalid
	 */
	public static Ptree bitAnd(Token token) {
		if(token.type == type_enum.andOperator) {
			return new Ptree(token);
		}
		return null;
	}
	
	/**
	 * Checks if the passed in token is a not operator
	 * @param token to be checked
	 * @return 
	 */
	public static Ptree notToken(Token token) {
		if(token.type == type_enum.notOperator) {
			return new Ptree(token);
		}
		return null;
	}
	
	
	/**
	 * Simply checks if passed in token is an open parenthesis
	 * @param token should be an open parenthesis
	 * @return Ptree node with an open parenthesis or null
	 */
	public static Ptree openParenthesis(Token token) {
		if(token.type == type_enum.openParenthesis) {
			return new Ptree(token);
		}
		return null;
	}
	
	/**
	 * Simply checks if passed in token is an closed parenthesis
	 * @param token should be an closed parenthesis
	 * @return Ptree node with an closed parenthesis or null
	 */
	public static Ptree closedParenthesis(Token token) {
		if(token.type == type_enum.closedParenthesis) {
			return new Ptree(token);
		}
		return null;
	}
	
	/**
	 * Simply checks if passed in token is an open curly bracket
	 * @param token should be an open curly bracket
	 * @return Ptree leaf node or null depending on validity
	 */
	public static Ptree openCurlyBracket(Token token) {
		if(token.type == type_enum.openCurlyBracket) {
			return new Ptree(token);
		}
		return null;
	}
	
	/**
	 * Simply checks if passed in token is a closed curly bracket
	 * @param token should be an closed curly bracket
	 * @return Ptree leaf node or null depending on validity
	 */
	public static Ptree closedCurlyBracket(Token token) {
		if(token.type == type_enum.closedCurlyBracket) {
			return new Ptree(token);
		}
		return null;
	}
	
	/**
	 * Checks if the passed in token is an increment operator
	 * @param token token to be checked
	 * @return Ptree containing the token if valid, null otherwise
	 */
	public static Ptree increment(Token token) {
		if(token.type == type_enum.incrementOperator) {
			return new Ptree(token);
		}
		return null;
	}
	
	/**
	 * Checks if the passed in token is a decrementOperator
	 * @param token token to be checked
	 * @return Ptree containing the token if valid, null otherwise
	 */
	public static Ptree decrement(Token token) {
		if(token.type == type_enum.decrementOperator) {
			return new Ptree(token);
		}
		return null;
	}
	/**
	 * Simply checks if passed in token is a semicolon
	 * @param token should be a semicolon
	 * @return Ptree leaf node with a semicolon or null depending on validity
	 */
	public static Ptree semicolon(Token token) {
		if(token.type == type_enum.semicolon) {
			return new Ptree(token);
		}
		return null;
	}
	
	/**
	 * simply checks if passed in token is a comma
	 * @param token should be a comma
	 * @return Ptree leaf node with a semicolon or null depending on validity
	 */
	public static Ptree comma(Token token) {
		if(token.type == type_enum.comma) {
			return new Ptree(token);
		}
		return null;
	}
	
	/**
	 * Simply checks if passed in token is a colon
	 * @param token should be colon
	 * @return Ptree node containing Ptree or null
	 */
	public static Ptree colon(Token token) {
		if(token.type == type_enum.colon) {
			return new Ptree(token);
		}
		return null;
	}
	
	/**
	 * Checks if passed in token is an equals sign
	 * @param token should be an equals sign
	 * @return Ptree leaf node with an equals sign depending on validity
	 */
	public static Ptree equals(Token token) {
		if(token.type == type_enum.assignmentOperator) {
			return new Ptree(token);
		}
		return null;
	}
	

	
	/**
	 * Finds the matching parenthesis in a list of tokens
	 * @param tokens list of tokens from an undefined source
	 * @param startindex index of the parenthesis to be matched
	 * @return the index of the parenthesis that matches the one passed in
	 */
	public static int findMatchingParenthesis(List<Token> tokens, int startindex) {
		int numBrackets = 1, i = startindex;
		boolean forward = true;
		type_enum matchingBracket = tokens.get(startindex).type;
		type_enum otherBracket = null;
		if(tokens.get(startindex).type == type_enum.openParenthesis) {
			otherBracket = type_enum.closedParenthesis;
		} else if (tokens.get(startindex).type == type_enum.closedParenthesis) {
			forward = false;
			otherBracket = type_enum.openParenthesis;
		} else {
			return -1;
		}
		while(i < tokens.size() && i > -1) {
			if(forward) {
				i++;
			} else {
				i--;
			}
			if(tokens.get(i).type == matchingBracket) {
				numBrackets++;
			} else if(tokens.get(i).type == otherBracket) {
				numBrackets--;
				if(numBrackets == 0) {
					return i;
				}
			}
		}

		return -1;
	}
	
	/**
	 * Finds the matching curly bracket in a list of tokens
	 * @param tokens list of tokens from an undefined source
	 * @param startindex index of the curly bracket to be matched
	 * @return the index of the bracket that matches the one passed in
	 */
	public static int findMatchingBracket(List<Token> tokens, int startindex) {
		//System.out.println("Start index = " + startindex);
		//System.out.println(tokens.get(startindex).getToken());
		int numBrackets = 1, i = startindex;
		boolean forward = true;
		type_enum matchingBracket = tokens.get(startindex).type;
		type_enum otherBracket = null;
		if(tokens.get(startindex).type == type_enum.openCurlyBracket) {
			otherBracket = type_enum.closedCurlyBracket;
		} else if (tokens.get(startindex).type == type_enum.closedCurlyBracket) {
			forward = false;
			otherBracket = type_enum.openCurlyBracket;
		} else {
			return -1;
		}
		while(i < tokens.size() && i > -1) {
			if(forward) {
				i++;
			} else {
				i--;
			}
			if(i >= 0 && tokens.get(i).type == matchingBracket) {
				numBrackets++;
			} else if(i >= 0 && tokens.get(i).type == otherBracket) {
				numBrackets--;
				if(numBrackets == 0) {
					return i;
				}
			}
		}

		return -1;
	}
	
	/**
	 * Finds a token scans from back to front
	 * @param tokens tokens to be scanned through
	 * @param type enumeration that describes what this tree is
	 * @return index of element or -1 if failed
	 */
	public static int findObject(List<Token> tokens, type_enum type) {
		int index = tokens.size() - 1, pCount = 0;
		while(index > -1) {
			if(tokens.get(index).type == type) {
				if(pCount == 0) {
					return index;
				}
			}
			switch(tokens.get(index).type) {
			case openParenthesis:
				pCount--;
				break;
			case closedParenthesis:
				pCount++;
				break;
			default:	
			}
			index--;
		}
		return -1;
	}
	
	/**
	 * Finds an object in a list of tokens by scanning from front to back
	 * @param tokens tokens to be scanned through
	 * @param type enumeration that describes what token to search for
	 * @return index of element or -1 if failed
	 */
	public static int findObjectForward(List<Token> tokens, type_enum type) {
		int index = 0, pCount = 0;
		while(index < tokens.size()) {
			if(tokens.get(index).type == type) {
				if(pCount == 0) {
					return index;
				}
			}
			switch(tokens.get(index).type) {
			case openParenthesis:
				pCount++;
				break;
			case closedParenthesis:
				pCount--;
				break;
			default:	
			}
			index++;
		}
		return -1;
	}
	/**
	 * Finds either of two specified tokens scans from back to front
	 * @param tokens tokens to be scanned through
	 * @param type token type to find
	 * @param type2 other token type to find
	 * @return index of token found or -1 if failed
	 */
	public static int findObject(List<Token> tokens, type_enum type, type_enum type2) {
		int index = tokens.size() - 1, pCount = 0;
		while(index > -1) {
			if(tokens.get(index).type == type || tokens.get(index).type == type2) {
				if(pCount == 0) {
					return index;
				}
			}
			switch(tokens.get(index).type) {
			case openParenthesis:
				pCount--;
				break;
			case closedParenthesis:
				pCount++;
				break;
			default:	
			}
			index--;
		}
		return -1;
	}
	
	/**
	 * Finds either of three specified tokens scans from back to front
	 * @param tokens tokens to be scanned through
	 * @param type token type to find
	 * @param type2 other token type to find
	 * @param type3 other token type to find
	 * @return index of token found or -1 if failed
	 */
	public static int findObject(List<Token> tokens, type_enum type, type_enum type2, type_enum type3) {
		int index = tokens.size() - 1, pCount = 0;
		while(index > -1) {
			if(tokens.get(index).type == type || tokens.get(index).type == type2 || tokens.get(index).type == type3) {
				if(pCount == 0) {
					return index;
				}
			}
			switch(tokens.get(index).type) {
			case openParenthesis:
				pCount--;
				break;
			case closedParenthesis:
				pCount++;
				break;
			default:	
			}
			index--;
		}
		return -1;
	}
}
