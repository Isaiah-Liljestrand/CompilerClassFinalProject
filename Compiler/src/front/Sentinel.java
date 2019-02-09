package front;

public class Sentinel {
	Sentinel Left;
	Sentinel Right;
	Token Value;
	
	Sentinel(){
		
	}
	
	Sentinel(Token Value){
		this.Value = Value;
	}
	
	Sentinel(Sentinel Left, Sentinel Right){
		this.Left = Left;
		this.Right = Right;
	}
	
	Sentinel(Token Value, Sentinel Left, Sentinel Right){
		this.Value = Value;
		this.Left = Left;
		this.Right = Right;
	}

	/**
	 * @return the left
	 */
	protected Sentinel getLeft() {
		return Left;
	}

	/**
	 * @param left the left to set
	 */
	protected void setLeft(Sentinel left) {
		Left = left;
	}

	/**
	 * @return the right
	 */
	protected Sentinel getRight() {
		return Right;
	}

	/**
	 * @param right the right to set
	 */
	protected void setRight(Sentinel right) {
		Right = right;
	}

	/**
	 * @return the value
	 */
	protected Token getValue() {
		return Value;
	}

	/**
	 * @param value the value to set
	 */
	protected void setValue(Token value) {
		Value = value;
	}
}
