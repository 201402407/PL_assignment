package pl01;
//201402407 이해원
import org.w3c.dom.Node;

public class RecusionLinkedList {
	private Node head;
	private static char UNDEF = Character.MIN_VALUE;
	// 새롭게 생성된 노드를 리스트의 처음으로 연결 
	private void linkFirst(char element) { // 새롭게 생성된 노드를 리스트의 처음으로 연결 
		head = new Node(element, head);
	}
	
	private void linkLast(char element, Node x) { // 과제(1) 주어진 Node x의 마지막으로 연결된 Node의 다음으로 새롭게 생성된 노드를 연결 
		if(x.next == null) { // 다음 원소로 연결
			this.linkNext(element, x);
		}
		else {// 다음 노드 방문 recursion
			this.linkLast(element, x.next);
		}
	}
	
	private void linkNext(char element, Node pred) { // 이전 Node의 다음 Node로 새롭게 생성된 노드를 연결
		Node next = pred.next;
		pred.next = new Node(element, next);
	}
	
	private char unlinkFirst() { // 리스트의 첫번째 원소 해제(삭제)
		Node x = head;
		char element = x.item;
		head = head.next;
		x.item = UNDEF;
		x.next = null;
		return element;
	}
	
	private char unlinkNext(Node pred) { // 이전Node 의 다음 Node연결 해제(삭제) 
		Node x = pred.next;
		Node next = x.next;
		char element = x.item;
		x.item = UNDEF;
		x.next = null;
		pred.next = next;
		return element;
	}
	
	private Node node(int index, Node x) { //  과제 (2) x노드에서 index만큼 떨어진 Node 반환
		if(x.next == null && index > 0) {
			throw new NullPointerException("index 만큼 떨어진 Node가 존재하지 않습니다 : " + index);
		}
		if(index == 0) {
			return x;
		}
		return this.node(index - 1, x.next);
		
	}
	
	private int length(Node x) { // 과제 (3) 노드로부터  끝까지의 리스트의 노드 갯수 반환 
		if(x.next == null) {
			return 1;
		}
		return length(x.next) + 1;
		
	}
	
	private String toString(Node x) { //  과제 (4) 노드로부터 시작하는 리스트의 내용 반환
		if(x == null) {
			//throw new NullPointerException("");
			return "";
		}
		System.out.print(x.item + " ");
		return toString(x.next);
	}
	
	private void reverse(Node x, Node pred) { // 현재 노드의 이전 노드부터 리스트의 끝까지를 거꾸로 만듬
			if(x == null) {
				head = pred;
			}
			else {
				reverse(x.next, x);
				x.next = pred;
			}
	}
	
	public boolean add(char element) { //  원소를 리스트의 마지막에 추가 
		if(head == null) {
			linkFirst(element);
		}
		else {
			linkLast(element, head);
		}
		return true;
	}
	
	public void add(int index, char element) { // 원소를 주어진 index 위치에 추가 
		if(!(index >= 0 && index <= size()))
			throw new IndexOutOfBoundsException("" + index);
		if(index == 0)
			linkFirst(element);
		else
			linkNext(element, node(index - 1, head));
	}
	
	public char get(int index) { // 리스트에서 index 위치의 원소 반환 
		if(!(index >= 0 && index < size()))
			throw new IndexOutOfBoundsException("" + index);
		
		return node(index, head).item;
	}
	
	public char remove(int index) { // 리스트에서 index 위치의 원소 삭제 
		if(!(index >= 0 && index < size()))
			throw new IndexOutOfBoundsException("" + index);
		if(index == 0) {
			return unlinkFirst();
		}
		return unlinkNext(node(index - 1, head));
	}
	
	public void reverse() { // 리스트를 거꾸로 만듬 
		reverse(head, null);
	}
	
	public int size() { // 리스트의 원소 갯수 반환 
		return length(head);
	}
	
	@Override
	public String toString() {
		if(head == null)
			return "[]";
		return "[" + toString(head)+ "]";
	}
	
	private static class Node { // 리스트에 사용될 자료구조 
		char item;
		Node next;
		
		Node(char element, Node next){
			this.item = element;
			this.next = next;
		}
	}
}
