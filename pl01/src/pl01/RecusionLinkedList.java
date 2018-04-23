package pl01;
//201402407 ���ؿ�
import org.w3c.dom.Node;

public class RecusionLinkedList {
	private Node head;
	private static char UNDEF = Character.MIN_VALUE;
	// ���Ӱ� ������ ��带 ����Ʈ�� ó������ ���� 
	private void linkFirst(char element) { // ���Ӱ� ������ ��带 ����Ʈ�� ó������ ���� 
		head = new Node(element, head);
	}
	
	private void linkLast(char element, Node x) { // ����(1) �־��� Node x�� ���������� ����� Node�� �������� ���Ӱ� ������ ��带 ���� 
		if(x.next == null) { // ���� ���ҷ� ����
			this.linkNext(element, x);
		}
		else {// ���� ��� �湮 recursion
			this.linkLast(element, x.next);
		}
	}
	
	private void linkNext(char element, Node pred) { // ���� Node�� ���� Node�� ���Ӱ� ������ ��带 ����
		Node next = pred.next;
		pred.next = new Node(element, next);
	}
	
	private char unlinkFirst() { // ����Ʈ�� ù��° ���� ����(����)
		Node x = head;
		char element = x.item;
		head = head.next;
		x.item = UNDEF;
		x.next = null;
		return element;
	}
	
	private char unlinkNext(Node pred) { // ����Node �� ���� Node���� ����(����) 
		Node x = pred.next;
		Node next = x.next;
		char element = x.item;
		x.item = UNDEF;
		x.next = null;
		pred.next = next;
		return element;
	}
	
	private Node node(int index, Node x) { //  ���� (2) x��忡�� index��ŭ ������ Node ��ȯ
		if(x.next == null && index > 0) {
			throw new NullPointerException("index ��ŭ ������ Node�� �������� �ʽ��ϴ� : " + index);
		}
		if(index == 0) {
			return x;
		}
		return this.node(index - 1, x.next);
		
	}
	
	private int length(Node x) { // ���� (3) ���κ���  �������� ����Ʈ�� ��� ���� ��ȯ 
		if(x.next == null) {
			return 1;
		}
		return length(x.next) + 1;
		
	}
	
	private String toString(Node x) { //  ���� (4) ���κ��� �����ϴ� ����Ʈ�� ���� ��ȯ
		if(x == null) {
			//throw new NullPointerException("");
			return "";
		}
		System.out.print(x.item + " ");
		return toString(x.next);
	}
	
	private void reverse(Node x, Node pred) { // ���� ����� ���� ������ ����Ʈ�� �������� �Ųٷ� ����
			if(x == null) {
				head = pred;
			}
			else {
				reverse(x.next, x);
				x.next = pred;
			}
	}
	
	public boolean add(char element) { //  ���Ҹ� ����Ʈ�� �������� �߰� 
		if(head == null) {
			linkFirst(element);
		}
		else {
			linkLast(element, head);
		}
		return true;
	}
	
	public void add(int index, char element) { // ���Ҹ� �־��� index ��ġ�� �߰� 
		if(!(index >= 0 && index <= size()))
			throw new IndexOutOfBoundsException("" + index);
		if(index == 0)
			linkFirst(element);
		else
			linkNext(element, node(index - 1, head));
	}
	
	public char get(int index) { // ����Ʈ���� index ��ġ�� ���� ��ȯ 
		if(!(index >= 0 && index < size()))
			throw new IndexOutOfBoundsException("" + index);
		
		return node(index, head).item;
	}
	
	public char remove(int index) { // ����Ʈ���� index ��ġ�� ���� ���� 
		if(!(index >= 0 && index < size()))
			throw new IndexOutOfBoundsException("" + index);
		if(index == 0) {
			return unlinkFirst();
		}
		return unlinkNext(node(index - 1, head));
	}
	
	public void reverse() { // ����Ʈ�� �Ųٷ� ���� 
		reverse(head, null);
	}
	
	public int size() { // ����Ʈ�� ���� ���� ��ȯ 
		return length(head);
	}
	
	@Override
	public String toString() {
		if(head == null)
			return "[]";
		return "[" + toString(head)+ "]";
	}
	
	private static class Node { // ����Ʈ�� ���� �ڷᱸ�� 
		char item;
		Node next;
		
		Node(char element, Node next){
			this.item = element;
			this.next = next;
		}
	}
}
