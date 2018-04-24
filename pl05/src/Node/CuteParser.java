package Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;

import lexer.Scanner;
import lexer.Token;
import lexer.TokenType;

// 201402407 이해원

public class CuteParser {
	private Iterator<Token> tokens;
	
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
			IdNode idNode = new IdNode(); // IdNode 생성.
			idNode.value = tLexeme; // 값 대입
			return idNode; // IdNode 리턴.
		case INT: // INT일 경우.
			IntNode intNode = new IntNode(); // IntNode 생성.
			if (tLexeme == null) // INT 외의 실수 (?) null이란 ?
				System.out.println("???");
			intNode.value = new Integer(tLexeme); // String->int 하기 위한 Integer(tLexeme)
			return intNode; // IntNode 리턴.
		
		// BinaryOpNode +, -, /, *, >, =, <가 해당
		case DIV:
		case EQ:
		case MINUS:
		case GT:
		case PLUS:
		case TIMES:
		case LT:
			BinaryOpNode binaryNode = new BinaryOpNode(); // BinaryOpNode 생성.
			binaryNode.setValue(tType); // 이 노드는 값이 Type만 있으므로 setValue 사용.
			return binaryNode; // BinaryNode 리턴.
			
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
			// 내용 채우기(BinaryOp참고)
			FunctionNode functionNode = new FunctionNode(); // FunctionNode 생성.
			functionNode.setValue(tType); // 이 노드도 위의 BinaryNode와 마찬가지로 Type만 있으므로 setValue 사용.
			return functionNode; // FunctionNode 리턴.
			
		// BooleanNode
		case FALSE:
			BooleanNode falseNode = new BooleanNode(); // BooleanNode 생성.
			falseNode.value = false; // #F
			return falseNode;
		case TRUE:
			BooleanNode trueNode = new BooleanNode();
			trueNode.value = true; // #T
			return trueNode;
		
			// case L_PAREN일 경우와 case R_PAREN일 경우
			// L_PAREN일 경우 pareseExprList()를 호출하여 처리.
		case L_PAREN: // '(' 인 경우.
			ListNode listNode = new ListNode();
			listNode.value = parseExprList();
			return listNode;
		case R_PAREN: // ')'이면 null로 인식.
			return null;
		default:
			// head의 next를 만들고 head를 반환하도록 작성.
			System.out.println("Parsing Error!");
			return null;
		}
	}
	// List의 Value를 생성하는 메소드
	private Node parseExprList() {
		Node head = parseExpr();
		// head의 next 노드를 set하시오.
		if(head == null) // if next token is R_PAREN
			return null;
		head.setNext(parseExprList());
		return head;
	}
}
