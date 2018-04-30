package Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;

import parser.Scanner;
import parser.Token;
import parser.TokenType;

// 201402407 이해원

public class CuteParser {
	private Iterator<Token> tokens;
	private static Node END_OF_LIST = new Node() {}; // EOL
	
	public CuteParser(File file) { // 생성자.
		try {
			tokens = Scanner.scan(file); // Scanner 클래스의 scan 함수를 이용해서 file을 Token(글자)별로 분리해서 각각을 Iterator에 넣음.
		} catch(FileNotFoundException e) { // File이 존재하지 않을 시 예외 처리.
			e.printStackTrace();
		}
	}
	
	private Token getNextToken() { // 다음 Token 읽기.
		if(!tokens.hasNext())
			return null;
		return tokens.next();
	}
	
	public Node parseExpr() {
		Token t = getNextToken(); // 다음 Token을 불러와서 넣기. (head는 ?)
		if(t == null) { // Token이 null(없으면)
			System.out.println("No more token");
			return null;
		}
		TokenType tType = t.type(); // 토큰타입
		String tLexeme = t.lexme(); // 글자.
		
		switch (tType) { // 토큰타입 스위치.
		case ID: // ID인 경우.
			return new IdNode(tLexeme);
		case INT: // INT일 경우.
			if (tLexeme == null) // INT 외의 실수 (?) null이란 ?
				System.out.println("???");
			return new IntNode(tLexeme);
		
		// BinaryOpNode +, -, /, *, >, =, <가 해당
		case DIV:
		case EQ:
		case MINUS:
		case GT:
		case PLUS:
		case TIMES:
		case LT:
			return new BinaryOpNode(tType);
			
		// FunctionNode 키워드가 FunctionNode에 해당
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
			
			// case L_PAREN일 경우와 case R_PAREN일 경우
			// L_PAREN일 경우 pareseExprList()를 호출하여 처리.
		case L_PAREN: // '(' 인 경우.
			return parseExprList();
			
		case R_PAREN: // ')'이면 END_OF_LIST로 해서 ')' 표시.
			return END_OF_LIST;
			
		case APOSTROPHE:
			return new QuoteNode(parseExpr());
		case QUOTE:
			return new QuoteNode(parseExpr());
			
		default:
			// head의 next를 만들고 head를 반환하도록 작성.
			System.out.println("Parsing Error!");
			return null;
		}
	}
	// List의 Value를 생성하는 메소드
	private ListNode parseExprList() {
		Node head = parseExpr(); // token을 읽어가면서 tokenType head에 넣기. ListNode의 처음 원소.
		
		if(head == null) // if next token is null(the others)
			return null;
		if (head == END_OF_LIST) // if next token is RPAREN
			return ListNode.ENDLIST;

		ListNode tail = parseExprList(); // tail 넣는 부분. 나머지 뒤의 원소들을 담고 있다. 
		// 마지막 부분은 ENDLIST로 리턴하므로 나머지 원소가 담긴다.
		if(tail == null)
			return null;
		
		return ListNode.cons(head, tail);
	}
}
