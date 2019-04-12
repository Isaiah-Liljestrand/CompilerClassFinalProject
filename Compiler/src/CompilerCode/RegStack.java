package CompilerCode;

import java.util.ArrayList;

/**
 * Keeps track of the values on the stack and in the registers.
 * @author Ben
 *
 */
public class RegStack {
	int regNum;
	private ArrayList<Integer> stack;
	private ArrayList<Integer> lastUsed;
	
	/**enum enum_register{
		rax (0),
		rbx (1),
		rcx (2),
		rsi (3),
		rbp (4),
		rsp (5),
		r8 (6),
		r9 (),
		r10 (),
		r11 (),
		r12 (),
		r13 (),
		r14 (),
		r15 ()
	};
	*/
	
	RegStack(){
		this.regNum = 16;
		setup(regNum);
	}
	
	RegStack(int regNum){
		this.regNum = regNum;
		setup(regNum);
	}
	
	private void setup(int regNum){
		int i = 0;
		stack = new ArrayList<Integer>();
		lastUsed = new ArrayList<Integer>();
		for(i = 0; i < regNum; i++){
			stack.add(0);
			lastUsed.add(1);
		}
		//sets the stack and base to never be used for standard operations
		if(lastUsed.size() >= 5){
			lastUsed.set(4, 0);
			lastUsed.set(5, 0);
		}
	}
	
	/**
	 * useful for selecting a reg with setReg 
	 */
	public int findLRUReg(){
		int holder = 0;
		for(int i = 0; i < regNum; i++){
			if(lastUsed.get(i) < lastUsed.get(holder)){
				holder = i;
			}
		}
		return holder;
	}
	
	
	/**
	 * sets value to the register reg
	 * 
	 * @param reg
	 * @param value
	 * @return stack value if non empty (0 value) register
	 */
	public int setReg(int reg, int value){
		/**
		 * change reg value on AR here
		 */
		if(stack.get(reg) != 0){ //non empty register 
			return pushRegToStack(reg);
		}
		stack.set(reg, value);
		incrementLRU();
		lastUsed.set(reg, 1);
		return reg;
	}
	
	/**
	 * if you don't care what reg it's using
	 * @param value
	 * @return
	 */
	public int setReg(int value){
		/**
		 * change reg value on AR here
		 */
		return setReg(findLRUReg(), value);
	}

	
	public int getReg(int reg){
		incrementLRU();
		lastUsed.set(reg, 1);
		return stack.get(reg); 
	}
	
	private void incrementLRU(){
		int i = 0;
		for(i = 0; i < regNum; i++){
			if(lastUsed.get(i) != 0){ //making sure not a protected register (rpb, rsp)
				lastUsed.set(i, lastUsed.get(i)+1);
			}
		}
	}
	
	public int pushRegToStack(int reg){
		int tmp = pushToStack(stack.get(reg));
		stack.set(reg, 0);
		return tmp;
	}
	
	/**
	 * 
	 * @param val
	 * @return the location onto he stack
	 */
	public int pushToStack(int val){
		int i = regNum; 
		//checks for free spots on stack
		for(i = regNum; i < lastUsed.size(); i++){
			if(lastUsed.get(i) == 0){ //free spot already allocated stack				
				lastUsed.set(i + regNum, 1); //marks as now used space
				return i - regNum;
			}
		}
			//no already allocated stack spaces are free
		stack.add(val);
		lastUsed.add(1);
		stack.set(5, stack.get(5) + 4); //increments the stack pointer by 4 (# bytes in an int)
		return(stack.size() - regNum); //new is last item in stack
	}
	
	public int cutFromStack(int offset){
		lastUsed.set(offset + regNum, 0); //marks as free space
		return stack.get(offset + regNum);
	}
	
	public int copyFromStack(int offset){
		return stack.get(offset + regNum);
	}
	
	public int getrbp(){
		if(stack.size() >= 5){
			return 4;
		}
		return -1;
	}
	
	public int getrsp(){
		if(stack.size() >= 6){
			return 5;
		}
		return -1;
	}
	
	/** 
	 * if you want a guarantee that reg will not auto pop
	 * @param reg
	 */
	public void protectReg(int reg){
		if(reg < regNum)
			lastUsed.set(reg, 0);
	}
	
	/** 
	 * allows reg to pop
	 * ::CAUTION:: can cause the base or stack pointers to be overwritten automatically
	 * @param reg
	 */
	public void unprotectReg(int reg){
		if(reg < regNum)
			lastUsed.set(reg, 1);
	}
}
