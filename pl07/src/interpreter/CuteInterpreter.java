// 201402407 ���ؿ�
package interpreter;

import parser.Token;
import parser.TokenType;
import Node.*;
import parser.ScannerMain;
import Node.CuteParser;
import Node.NodePrinter;
import java.io.File;


public class CuteInterpreter {

	private void errorLog(String err) {
		System.out.println(err);
	}
	
	public Node runExpr(Node rootExpr) {
		if (rootExpr == null)
			return null;
		
		if (rootExpr instanceof IdNode)
			return rootExpr;
		else if (rootExpr instanceof IntNode)
			return rootExpr;
		else if (rootExpr instanceof BooleanNode)
			return rootExpr;
		else if (rootExpr instanceof ListNode)
			return runList((ListNode) rootExpr);
		else
			errorLog("run Expr error");
		return null;
	}
	
	private Node runList(ListNode list) {
		if(list.equals(ListNode.EMPTYLIST))
			return list;
		if(list.car() instanceof FunctionNode) {
			return runFunction((FunctionNode)list.car(), list.cdr());
		}
		if(list.car() instanceof BinaryOpNode) {
			return runBinary(list);
		}
		return list;
	}
	
	private Node runFunction(FunctionNode operator, ListNode operand) {
		switch (operator.getFunctionType()) { // ���� ���� ����.
			case CAR: // List�� �� ó�� ���� ����.
			case CDR: // List�� �� ó�� ���Ҹ� ������ ������ list ����.
			case CONS: // �� ���� ���ҿ� �� ���� ����Ʈ�� �ٿ��� ���ο� ����Ʈ ����. (head + tail)
			case NULL_Q: // ����Ʈ�� null���� �˻�.
			case ATOM_Q: // list == !atom, not list == atom, null list == atom
			case EQ_Q: // ���� ������ ��
			case NOT: // EQ�� �ݴ�.
			case COND: // ���ǹ�.
			default:
					break;
		}
		return null;
	}
	
	private Node runBinary(ListNode list) { 
		BooleanNode operator = list.car(); // runBinary�ȿ� ���ؼ��� �߰�.
		
		switch (operator.binType) { // ���̳ʸ� ���� ���� ����
			case PLUS:
			case MINUS:
			case DIV:
			case TIMES:
			case LT:
			case GT:
			case EQ:
				default:
					break;
		}
		return null;
	}
	
	private Node runQuote(ListNode node) {
		return ((QuoteNode)node.car()).nodeInside();
	}
	
	public static void main(String[] args) throws Exception{ 
		ClassLoader cloader = CuteInterpreter.class.getClassLoader();
		File file = new File(cloader.getResource("interpreter/as07.txt").getFile());
		CuteParser cuteParser = new CuteParser(file);
		Node parseTree = cuteParser.parseExpr();
		CuteInterpreter i = new CuteInterpreter();
		Node resultNode = i.runExpr(parseTree);
		NodePrinter.getPrinter(System.out).prettyPrint(resultNode);
		System.out.println();
		System.out.println("201402407 ���ؿ�");
	}
}
