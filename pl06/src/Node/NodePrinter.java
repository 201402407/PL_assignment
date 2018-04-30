package Node;
// 201402407 ÀÌÇØ¿ø

import java.io.PrintStream;

public class NodePrinter {
	PrintStream ps;
	
	public static NodePrinter getPrinter(PrintStream ps) {
		return new NodePrinter(ps);
	}
	
	private NodePrinter(PrintStream ps) {
		this.ps = ps;
	}
	
	private void printNode(ListNode listNode) {
			if(listNode == ListNode.EMPTYLIST) {
				ps.print("( ) ");
				return;
			}
			if(listNode == ListNode.ENDLIST) {
				return;
			}
			
	}
	
	private void printNode(QuoteNode quoteNode) {
		if(quoteNode.nodeInside() == null)
			return;
		
	}
	
	private void printNode(Node node) {
		if(node == null)
			return;
		
	}	
	
	public void prettyPrint(Node node) {
		printNode(node);
	}
}
