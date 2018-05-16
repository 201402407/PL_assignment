package Node;
// 201402407 이해원

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

	private void printNode(ListNode listNode) { // ListNode일 때 이 메소드 실행. (overrode)
			if(listNode.equals(ListNode.EMPTYLIST)) { // ( ) 이면.
				ps.print("( ) ");
				return;
			}
			if(listNode.equals(ListNode.ENDLIST)) { // ) 라면 .
				return;
			}
			printNode(listNode.car());
			if(listNode.cdr().equals(ListNode.EMPTYLIST)) { // listNode.car() 원소 관련.
				ps.print(" ");
			}
			printNode(listNode.cdr());
	}
	
	private void printNode(QuoteNode quoteNode) { // QuoteNode일 때 이 메소드 실행. (overrode)
		if(quoteNode.nodeInside() == null) // 다음에 노드가 없다면 ?
			return;
		
		ps.print("'"); // quote 출력.
		printNode(quoteNode.nodeInside());
	}
	
	private void printNode(Node node) { // prettyPrint에 의해 처음 실행되는 printNode 함수.
		if(node == null)
			return;
		
		if(node instanceof ListNode) { // 만약 node가 ListNode라면?
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
