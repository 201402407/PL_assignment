package pl04;
//201402407 이해원
import ast.*;
import compile.*;

public class test {
	static int result, resultSum;
	static int count;
	
	public static void main(String... args) {
		Node node = TreeFactory.createtTree("( ( 3 ( 10 ) ) 6 ) 4 1 ( ) -2 ( ) )");
	
		System.out.println("최대값 : " + test.max(node));
		System.out.println("그리고");
		System.out.println("총합 : " + test.sum(node));
	}
	
	public static int max(Node node) {
		if(node instanceof ListNode) { // ((ListNode)node).value = '('
			if(((ListNode)node).value != null) { // 현재 ListNode의 value(Node)가 존재할 때.
				
				result = max(((ListNode)node).value);
			}
			if(node.getNext() != null) { // 다음 getNext가 존재할 때.
				if(((ListNode)node).value == null) { // ListNode이고 다음 노드가 존재하고, 해당 괄호가 null일 때.
				
					return max(((ListNode)node).getNext());
				}
				result = max(((ListNode)node).getNext()); // ListNode이고 다음 노드가 존재하고, 해당 괄호가 null이 아닐 때.
			}
		}
		
		if(node instanceof IntNode) {
			if(node.getNext() != null) { // IntNode이고 다음 노드가 null이 아닐 때 .
				result = Integer.max(Integer.max(((IntNode)node).value, result), max(((IntNode)node).getNext()));	
			}
			else {
				result = Integer.max(result, ((IntNode)node).value); // IntNode이고 다음 노드가 null일 때.	
			}
		}
		return result;
	} 
	
	public static int sum(Node node) {
			
		if(node instanceof ListNode) { // ((ListNode)node).value = '('
			if(((ListNode)node).value != null) { // 현재 ListNode의 value(Node)가 존재할 때.
				resultSum += sum(((ListNode)node).value);
			}
			
			if(node.getNext() != null) { // 다음 getNext가 존재할 때.
				if(((ListNode)node).value == null) { // ListNode이고 다음 노드가 존재하고, 해당 괄호가 null일 때.
				
					return sum(((ListNode)node).getNext());
				}
				resultSum += sum(((ListNode)node).getNext()); // ListNode이고 다음 노드가 존재하고, 해당 괄호가 null이 아닐 때.
			}
			if(node.getNext() == null) {
				if(((ListNode)node).value == null) {
					return 0;
				}
			}

		}
		
		if(node instanceof IntNode) {
			if(node.getNext() != null) { // IntNode이고 다음 노드가 null이 아닐 때 
				return Integer.sum(((IntNode)node).value, sum(((IntNode)node).getNext()));
			}
				else {
				return ((IntNode)node).value; // IntNode이고 다음 노드가 null일 때.	
				}
		}
		//System.out.println(resultSum);
		return resultSum;
	}
}
	
