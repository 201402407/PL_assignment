package Node;
// 201402407 ���ؿ�

public class BooleanNode extends Node {
	
	public boolean value;
	@Override
	public String toString() {
		return value ? "#T" : "#F";
	}
}
