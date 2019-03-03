package front;

import front.Token.type_enum;

/**
 * IR entry
 * @author Ben
 *
 */
public class IRent {
	Element destination;
	Element in1;
	Element in2;
	Element opp;
	
	IRent(){
		
	}
	
	IRent(Element target, Element src){
		destination = target;
		in1 = src;
	}
	
	IRent(Token target, Token src){
		destination = new Element(target);
		in1 = new Element(src);
	}
	
	IRent(Element target, Element src1, Element src2){
		destination = target;
		in1 = src1;
		in2 = src2;
	}
	
	IRent(Token target, Token src1, Token src2){
		destination = new Element(target);
		in1 = new Element(src1);
		in2 = new Element(src2);
	}
	
	IRent(Element target, Element src1, Element operand, Element src2){
		destination = target;
		in1 = src1;
		in2 = src2;
		opp = operand;
	}
	
	
	IRent(Token target, Token src1, Token operand, Token src2){
		destination = new Element(target);
		in1 = new Element(src1);
		in2 = new Element(src2);
		opp = new Element(operand);
	}
	
	public void Optimize(){
		//Basically if it's some constant number operator number
		//ie. 2 + 2
		if(in1.value != null && in2.value != null && opp != null){
			switch(opp.type){
			case additionOperator:
				destination.value = in1.value + in2.value;
				destination.type = type_enum.constant;
				break;
			case subtractionOperator:
				destination.value = in1.value - in2.value;
				destination.type = type_enum.constant;
				break;
			case divisionOperator:
				destination.value = in1.value / in2.value;
				destination.type = type_enum.constant;
				break;
			case multiplicationOperator:
				destination.value = in1.value * in2.value;
				destination.type = type_enum.constant;
				break;
			case modulusOperator:
				destination.value = in1.value % in2.value;
				destination.type = type_enum.constant;
				break;
			default:
				break;
			}
		}
	}
	
	protected void printEnt(){
		if(destination != null){
			System.out.print(destination.token + "<- ");
		}
		if(in1 != null){
			System.out.print(in1.token);
		}
		if(opp != null){
			System.out.print(" " + opp.token);
		}
		if(in2 != null){
			System.out.print(" " + in2.token);
		}
		System.out.println();
	}

}
