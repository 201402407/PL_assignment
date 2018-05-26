package Node;
// 201402407 이해원

public interface ListNode extends Node {
	
	Node car();
	ListNode cdr();
	
	static ListNode cons(Node head, ListNode tail) {
		return new ListNode() {
			@Override
			public Node car() {
				return head;
			}
			@Override
			public ListNode cdr() {
				return tail;
			}
		};
	}
	
	static ListNode EMPTYLIST = new ListNode() { // ListNode 괄호 안에 원소가 하나도 없는 경우. ( ) 인 경우.
		@Override
		public Node car() {
			return null;
		}
		
		@Override
		public ListNode cdr() {
			return null;
		}
	};
	
	static ListNode ENDLIST = new ListNode() { // ListNode의 괄호 ) 만 있는 경우.
		@Override
		public Node car() {
			return null;
		}
		
		@Override
		public ListNode cdr() {
			return null;
		}
	};
}
