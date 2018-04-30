package Node;
// 201402407 ���ؿ�

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
			if(listNode.car() instanceof ListNode) { // listNode.car() ���� ����.
				ListNode listnode = (ListNode)listNode.car();
				printNode(listnode);
			}
			else {
				printNode(listNode.car());
			}
			
			if(listNode.cdr() instanceof ListNode) { // listNode.cdr() ���� ����.
				ListNode listnode = (ListNode)listNode.cdr();
				printNode(listnode);
			}
			else {
				printNode(listNode.cdr());
			}
			
			ps.print(" ) ");
	}
	
	private void printNode(QuoteNode quoteNode) {
		if(quoteNode.nodeInside() == null) // ������ ��尡 ���ٸ� ?
			return;
		
		ps.print('\'');
		if(quoteNode.nodeInside() instanceof ListNode) { // QuoteNode üũ�� ���� Node���� �ϹǷ� �ʿ� X.
			ListNode listnode = (ListNode)quoteNode.nodeInside();
			printNode(listnode);
			return;
		}
		printNode(quoteNode.nodeInside());
	}
	
	private void printNode(Node node) {
		if(node == null)
			return;
		
		if(node instanceof ListNode) { // ���� node�� ListNode���?
			ListNode listnode = (ListNode)node;
			printNode(listnode);
			return;
		}
		
		if(node instanceof QuoteNode) { // ���� node�� QuoteNode���?
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
