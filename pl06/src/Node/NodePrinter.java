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

	private void printNode(ListNode listNode) { // ListNode일 때 이 메소드 실행. (overrode)
			if(listNode == ListNode.EMPTYLIST) { // ( ) 이면.
				ps.print("( ) ");
				return;
			}
			if(listNode == ListNode.ENDLIST) { // ) 라면 .
				return;
			}
			ps.print("( "); // 기본적으로 ListNode라서 ( 추가.
			if(listNode.car() instanceof ListNode) { // listNode.car() 원소 관련.
				ListNode listnode = (ListNode)listNode.car();
				printNode(listnode); // ListNode로 변환 후 메소드 실행.
			}
			else {
				printNode(listNode.car()); // car()에 들어있는 Node 출력.
			}
			
			if(listNode.cdr() instanceof ListNode) { // listNode.cdr() 원소 관련.
				ListNode listnode = (ListNode)listNode.cdr();
				printNode(listnode); // ListNode로 변환 후 메소드 실행.
			}
			else {
				printNode(listNode.cdr()); // cdr()에 들어있는 Node 출력.
			}
			
			ps.print(" ) "); // 메소드 종료 전 ) 출력.
	}
	
	private void printNode(QuoteNode quoteNode) { // QuoteNode일 때 이 메소드 실행. (overrode)
		if(quoteNode.nodeInside() == null) // 다음에 노드가 없다면 ?
			return;
		
		ps.print('\''); // quote 출력.
		if(quoteNode.nodeInside() instanceof ListNode) { // QuoteNode 체크는 밑의 Node에서 하므로 필요 X.
			ListNode listnode = (ListNode)quoteNode.nodeInside();
			printNode(listnode); // ListNode로 변환 후 메소드 실행.
			return;
		}
		printNode(quoteNode.nodeInside());
	}
	
	private void printNode(Node node) { // prettyPrint에 의해 처음 실행되는 printNode 함수.
		if(node == null)
			return;
		
		if(node instanceof ListNode) { // 만약 node가 ListNode라면?
			ListNode listnode = (ListNode)node;
			printNode(listnode); // ListNode로 변환 후 메소드 실행.
			return;
		}
		
		if(node instanceof QuoteNode) { // 만약 node가 QuoteNode라면?
			QuoteNode quotenode = (QuoteNode)node;
			printNode(quotenode); // QUoteNode로 변환 후 메소드 실행.
			return; 
		}
		ps.print("[" + node + "] "); // node가 ValueNode일 때.
	}	
	
	public void prettyPrint(Node node) {
		printNode(node);
	}
}
