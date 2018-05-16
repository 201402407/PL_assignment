package Node;
// 201402407 ���ؿ�

import java.io.PrintStream;
import java.util.StringTokenizer;

public class NodePrinter {
	PrintStream ps;
	
	public static NodePrinter getPrinter(PrintStream ps) {
		return new NodePrinter(ps);
	}
	
	private NodePrinter(PrintStream ps) {
		this.ps = ps;
	}

	private void printNode(ListNode listNode) { // ListNode�� �� �� �޼ҵ� ����. (overrode)
			if(listNode.equals(ListNode.EMPTYLIST)) { // ( ) �̸�.
				ps.print("( ) ");
				return;
			}
			if(listNode.equals(ListNode.ENDLIST)) { // ) ��� .
				return;
			}
			printNode(listNode.car());
			if(listNode.cdr().equals(ListNode.EMPTYLIST)) { // listNode.car() ���� ����.
				ps.print(" ");
			}
			printNode(listNode.cdr());
	}
	
	private void printNode(QuoteNode quoteNode) { // QuoteNode�� �� �� �޼ҵ� ����. (overrode)
		if(quoteNode.nodeInside() == null) // ������ ��尡 ���ٸ� ?
			return;
		
		ps.print("'"); // quote ���.
		printNode(quoteNode.nodeInside());
	}
	
	private void printNode(Node node) { // prettyPrint�� ���� ó�� ����Ǵ� printNode �Լ�.
		if(node == null)
			return;
		
		if(node instanceof ListNode) { // ���� node�� ListNode���?
			ps.print("(");
			printNode((ListNode)node);
			ps.print(")");
		}
		else if(node instanceof QuoteNode) {
			printNode((QuoteNode)node);
		}
		else {
			String temp = node.toString();
			StringTokenizer st = new StringTokenizer(temp, " ");
			st.nextToken();
			ps.print(" " + st.nextToken());
		}
	}	
	
	public void prettyPrint(Node node) {
		printNode(node);
	}
}
