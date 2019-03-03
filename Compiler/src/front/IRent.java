package front;

/**
 * IR entry
 * @author Ben
 *
 */
public class IRent {
	Element destination;
	Element in1;
	Element in2;
	
	IRent(){
		
	}
	
	IRent(Element target, Element src){
		Element destination = target;
		Element in1 = src;
	}
	
	IRent(Element target, Element src1, Element src2){
		Element destination = target;
		Element in1 = src1;
		Element in2 = src2;
	}
	
	

}
