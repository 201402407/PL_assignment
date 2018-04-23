package pl04;
//201402407 ���ؿ�
import ast.*;
import compile.*;

public class test {
	static int result, resultSum;
	static int count;
	
	public static void main(String... args) {
		Node node = TreeFactory.createtTree("( ( 3 ( 10 ) ) 6 ) 4 1 ( ) -2 ( ) )");
	
		System.out.println("�ִ밪 : " + test.max(node));
		System.out.println("�׸���");
		System.out.println("���� : " + test.sum(node));
	}
	
	public static int max(Node node) {
		if(node instanceof ListNode) { // ((ListNode)node).value = '('
			if(((ListNode)node).value != null) { // ���� ListNode�� value(Node)�� ������ ��.
				
				result = max(((ListNode)node).value);
			}
			if(node.getNext() != null) { // ���� getNext�� ������ ��.
				if(((ListNode)node).value == null) { // ListNode�̰� ���� ��尡 �����ϰ�, �ش� ��ȣ�� null�� ��.
				
					return max(((ListNode)node).getNext());
				}
				result = max(((ListNode)node).getNext()); // ListNode�̰� ���� ��尡 �����ϰ�, �ش� ��ȣ�� null�� �ƴ� ��.
			}
		}
		
		if(node instanceof IntNode) {
			if(node.getNext() != null) { // IntNode�̰� ���� ��尡 null�� �ƴ� �� .
				result = Integer.max(Integer.max(((IntNode)node).value, result), max(((IntNode)node).getNext()));	
			}
			else {
				result = Integer.max(result, ((IntNode)node).value); // IntNode�̰� ���� ��尡 null�� ��.	
			}
		}
		return result;
	} 
	
	public static int sum(Node node) {
			
		if(node instanceof ListNode) { // ((ListNode)node).value = '('
			if(((ListNode)node).value != null) { // ���� ListNode�� value(Node)�� ������ ��.
				resultSum += sum(((ListNode)node).value);
			}
			
			if(node.getNext() != null) { // ���� getNext�� ������ ��.
				if(((ListNode)node).value == null) { // ListNode�̰� ���� ��尡 �����ϰ�, �ش� ��ȣ�� null�� ��.
				
					return sum(((ListNode)node).getNext());
				}
				resultSum += sum(((ListNode)node).getNext()); // ListNode�̰� ���� ��尡 �����ϰ�, �ش� ��ȣ�� null�� �ƴ� ��.
			}
			if(node.getNext() == null) {
				if(((ListNode)node).value == null) {
					return 0;
				}
			}

		}
		
		if(node instanceof IntNode) {
			if(node.getNext() != null) { // IntNode�̰� ���� ��尡 null�� �ƴ� �� 
				return Integer.sum(((IntNode)node).value, sum(((IntNode)node).getNext()));
			}
				else {
				return ((IntNode)node).value; // IntNode�̰� ���� ��尡 null�� ��.	
				}
		}
		//System.out.println(resultSum);
		return resultSum;
	}
}
	
