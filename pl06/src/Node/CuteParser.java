package Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;

import parser.Scanner;
import parser.Token;
import parser.TokenType;

// 201402407 ���ؿ�

public class CuteParser {
	private Iterator<Token> tokens;
	private static Node END_OF_LIST = new Node() {}; // EOL
	
	public CuteParser(File file) { // ������.
		try {
			tokens = Scanner.scan(file); // Scanner Ŭ������ scan �Լ��� �̿��ؼ� file�� Token(����)���� �и��ؼ� ������ Iterator�� ����.
		} catch(FileNotFoundException e) { // File�� �������� ���� �� ���� ó��.
			e.printStackTrace();
		}
	}
	
	private Token getNextToken() { // ���� Token �б�.
		if(!tokens.hasNext())
			return null;
		return tokens.next();
	}
	
	public Node parseExpr() {
		Token t = getNextToken(); // ���� Token�� �ҷ��ͼ� �ֱ�. (head�� ?)
		if(t == null) { // Token�� null(������)
			System.out.println("No more token");
			return null;
		}
		TokenType tType = t.type(); // ��ūŸ��
		String tLexeme = t.lexme(); // ����.
		
		switch (tType) { // ��ūŸ�� ����ġ.
		case ID: // ID�� ���.
			return new IdNode(tLexeme);
		case INT: // INT�� ���.
			if (tLexeme == null) // INT ���� �Ǽ� (?) null�̶� ?
				System.out.println("???");
			return new IntNode(tLexeme);
		
		// BinaryOpNode +, -, /, *, >, =, <�� �ش�
		case DIV:
		case EQ:
		case MINUS:
		case GT:
		case PLUS:
		case TIMES:
		case LT:
			return new BinaryOpNode(tType);
			
		// FunctionNode Ű���尡 FunctionNode�� �ش�
		case ATOM_Q:
		case CAR:
		case CDR:
		case COND:
		case CONS:
		case DEFINE:
		case EQ_Q:
		case LAMBDA:
		case NOT:
		case NULL_Q:
			return new FunctionNode(tType);
			
		// BooleanNode
		case FALSE:
			return BooleanNode.FALSE_NODE;
		case TRUE:
			return BooleanNode.TRUE_NODE;
			
			// case L_PAREN�� ���� case R_PAREN�� ���
			// L_PAREN�� ��� pareseExprList()�� ȣ���Ͽ� ó��.
		case L_PAREN: // '(' �� ���.
			return parseExprList();
			
		case R_PAREN: // ')'�̸� END_OF_LIST�� �ؼ� ')' ǥ��.
			return END_OF_LIST;
			
		case APOSTROPHE:
			return new QuoteNode(parseExpr());
		case QUOTE:
			return new QuoteNode(parseExpr());
			
		default:
			// head�� next�� ����� head�� ��ȯ�ϵ��� �ۼ�.
			System.out.println("Parsing Error!");
			return null;
		}
	}
	// List�� Value�� �����ϴ� �޼ҵ�
	private ListNode parseExprList() {
		Node head = parseExpr(); // token�� �о�鼭 tokenType head�� �ֱ�. ListNode�� ó�� ����.
		
		if(head == null) // if next token is null(the others)
			return null;
		if (head == END_OF_LIST) // if next token is RPAREN
			return ListNode.ENDLIST;

		ListNode tail = parseExprList(); // tail �ִ� �κ�. ������ ���� ���ҵ��� ��� �ִ�. 
		// ������ �κ��� ENDLIST�� �����ϹǷ� ������ ���Ұ� ����.
		if(tail == null)
			return null;
		
		return ListNode.cons(head, tail);
	}
}
