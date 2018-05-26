package Node;
// 201402407 ���ؿ�

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
	
	static ListNode EMPTYLIST = new ListNode() { // ListNode ��ȣ �ȿ� ���Ұ� �ϳ��� ���� ���. ( ) �� ���.
		@Override
		public Node car() {
			return null;
		}
		
		@Override
		public ListNode cdr() {
			return null;
		}
	};
	
	static ListNode ENDLIST = new ListNode() { // ListNode�� ��ȣ ) �� �ִ� ���.
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
