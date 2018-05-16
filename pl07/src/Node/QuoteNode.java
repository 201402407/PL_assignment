package Node;
// 201402407 이해원

public class QuoteNode implements Node {
	Node quoted;
	
	public QuoteNode(Node quoted) { // ' 다음 노드를 인자로 받음
		this.quoted = quoted;
	}
	
	@Override
	public String toString() {
		return quoted.toString();
	}
	
	public Node nodeInside() { // ' 다음 노드를 리턴해준다.
		// TODO Auto-generated method stub
		return quoted;
	}
}
