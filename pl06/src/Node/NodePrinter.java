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

	private void printNode(ListNode listNode) { // ListNode�� �� �� �޼ҵ� ����. (overrode)
			if(listNode == ListNode.EMPTYLIST) { // ( ) �̸�.
				ps.print("( ) ");
				return;
			}
			if(listNode == ListNode.ENDLIST) { // ) ��� .
				return;
			}
			ps.print("( "); // �⺻������ ListNode�� ( �߰�.
			if(listNode.car() instanceof ListNode) { // listNode.car() ���� ����.
				ListNode listnode = (ListNode)listNode.car();
				printNode(listnode); // ListNode�� ��ȯ �� �޼ҵ� ����.
			}
			else {
				printNode(listNode.car()); // car()�� ����ִ� Node ���.
			}
			
			if(listNode.cdr() instanceof ListNode) { // listNode.cdr() ���� ����.
				ListNode listnode = (ListNode)listNode.cdr();
				printNode(listnode); // ListNode�� ��ȯ �� �޼ҵ� ����.
			}
			else {
				printNode(listNode.cdr()); // cdr()�� ����ִ� Node ���.
			}
			
			ps.print(" ) "); // �޼ҵ� ���� �� ) ���.
	}
	
	private void printNode(QuoteNode quoteNode) { // QuoteNode�� �� �� �޼ҵ� ����. (overrode)
		if(quoteNode.nodeInside() == null) // ������ ��尡 ���ٸ� ?
			return;
		
		ps.print('\''); // quote ���.
		if(quoteNode.nodeInside() instanceof ListNode) { // QuoteNode üũ�� ���� Node���� �ϹǷ� �ʿ� X.
			ListNode listnode = (ListNode)quoteNode.nodeInside();
			printNode(listnode); // ListNode�� ��ȯ �� �޼ҵ� ����.
			return;
		}
		printNode(quoteNode.nodeInside());
	}
	
	private void printNode(Node node) { // prettyPrint�� ���� ó�� ����Ǵ� printNode �Լ�.
		if(node == null)
			return;
		
		if(node instanceof ListNode) { // ���� node�� ListNode���?
			ListNode listnode = (ListNode)node;
			printNode(listnode); // ListNode�� ��ȯ �� �޼ҵ� ����.
			return;
		}
		
		if(node instanceof QuoteNode) { // ���� node�� QuoteNode���?
			QuoteNode quotenode = (QuoteNode)node;
			printNode(quotenode); // QUoteNode�� ��ȯ �� �޼ҵ� ����.
			return; 
		}
		ps.print("[" + node + "] "); // node�� ValueNode�� ��.
	}	
	
	public void prettyPrint(Node node) {
		printNode(node);
	}
}
