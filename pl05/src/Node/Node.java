package Node;
//201402407 ÀÌÇØ¿ø

public abstract class Node {
	Node next;
	public Node() { this.next = null; }
	public void setNext(Node next) { this.next = next; }
	public void setLastNext(Node next) {
		if(this.next != null) this.next.setLastNext(next);
		else this.next = next;
	}
	public Node getNext() { return next; }
}
