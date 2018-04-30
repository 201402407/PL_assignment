package Node;
// 201402407 이해원

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
			ps.print("( ");
			if(listNode.car() instanceof ListNode) { // listNode.car() 원소 관련.
				ListNode listnode = (ListNode)listNode.car();
				printNode(listnode);
			}
			else {
				printNode(listNode.car());
			}
			
			if(listNode.cdr() instanceof ListNode) { // listNode.cdr() 원소 관련.
				ListNode listnode = (ListNode)listNode.cdr();
				printNode(listnode);
			}
			else {
				printNode(listNode.cdr());
			}
			
			ps.print(" ) ");
	}
	
	private void printNode(QuoteNode quoteNode) {
		if(quoteNode.nodeInside() == null) // 다음에 노드가 없다면 ?
			return;
		
		ps.print('\'');
		if(quoteNode.nodeInside() instanceof ListNode) { // QuoteNode 체크는 밑의 Node에서 하므로 필요 X.
			ListNode listnode = (ListNode)quoteNode.nodeInside();
			printNode(listnode);
			return;
		}
		printNode(quoteNode.nodeInside());
	}
	
	private void printNode(Node node) {
		if(node == null)
			return;
		
		if(node instanceof ListNode) { // 만약 node가 ListNode라면?
			ListNode listnode = (ListNode)node;
			printNode(listnode);
			return;
		}
		
		if(node instanceof QuoteNode) { // 만약 node가 QuoteNode라면?
			QuoteNode quotenode = (QuoteNode)node;
			printNode(quotenode);
			return;
		}
		ps.print("[" + node + "] ");
	}	
	
	public void prettyPrint(Node node) {
		printNode(node);
	}
}
