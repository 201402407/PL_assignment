package Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;

import lexer.Scanner;
import lexer.Token;
import lexer.TokenType;

// 201402407 ���ؿ�

public class CuteParser {
	private Iterator<Token> tokens;
	
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
			IdNode idNode = new IdNode(); // IdNode ����.
			idNode.value = tLexeme; // �� ����
			return idNode; // IdNode ����.
		case INT: // INT�� ���.
			IntNode intNode = new IntNode(); // IntNode ����.
			if (tLexeme == null) // INT ���� �Ǽ� (?) null�̶� ?
				System.out.println("???");
			intNode.value = new Integer(tLexeme); // String->int �ϱ� ���� Integer(tLexeme)
			return intNode; // IntNode ����.
		
		// BinaryOpNode +, -, /, *, >, =, <�� �ش�
		case DIV:
		case EQ:
		case MINUS:
		case GT:
		case PLUS:
		case TIMES:
		case LT:
			BinaryOpNode binaryNode = new BinaryOpNode(); // BinaryOpNode ����.
			binaryNode.setValue(tType); // �� ���� ���� Type�� �����Ƿ� setValue ���.
			return binaryNode; // BinaryNode ����.
			
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
			// ���� ä���(BinaryOp����)
			FunctionNode functionNode = new FunctionNode(); // FunctionNode ����.
			functionNode.setValue(tType); // �� ��嵵 ���� BinaryNode�� ���������� Type�� �����Ƿ� setValue ���.
			return functionNode; // FunctionNode ����.
			
		// BooleanNode
		case FALSE:
			BooleanNode falseNode = new BooleanNode(); // BooleanNode ����.
			falseNode.value = false; // #F
			return falseNode;
		case TRUE:
			BooleanNode trueNode = new BooleanNode();
			trueNode.value = true; // #T
			return trueNode;
		
			// case L_PAREN�� ���� case R_PAREN�� ���
			// L_PAREN�� ��� pareseExprList()�� ȣ���Ͽ� ó��.
		case L_PAREN: // '(' �� ���.
			ListNode listNode = new ListNode();
			listNode.value = parseExprList();
			return listNode;
		case R_PAREN: // ')'�̸� null�� �ν�.
			return null;
		default:
			// head�� next�� ����� head�� ��ȯ�ϵ��� �ۼ�.
			System.out.println("Parsing Error!");
			return null;
		}
	}
	// List�� Value�� �����ϴ� �޼ҵ�
	private Node parseExprList() {
		Node head = parseExpr();
		// head�� next ��带 set�Ͻÿ�.
		if(head == null) // if next token is R_PAREN
			return null;
		head.setNext(parseExprList());
		return head;
	}
}
